package com.github.retrooper.packetevents.event;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.exception.InvalidHandshakeException;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public class InheritableEventManager extends EventManager {
    private ListenerStore store;
    /**
     * We store the listeners in a sorted manner aka:
     * 1) We sort by their priority
     * 2) We sort by their creation time (this is to ensure thread-safety)
     * 3) We sort by their identity hashcode (this to ensure that even in the case that two listeners are created at the same time, it would still register)
     */
    private static final Comparator<PacketListenerCommon> LISTENER_COMPARATOR = Comparator
            .<PacketListenerCommon>comparingInt(listener -> listener.getPriority().ordinal())
            .thenComparingLong(PacketListenerCommon::getCreationTimeStamp)
            .thenComparingInt(System::identityHashCode);

    public InheritableEventManager(ListenerStore store) {
        this.store = store;
    }

    public InheritableEventManager() {
        this(new ListenerStore());
    }

    @Override
    public void callEvent(PacketEvent event) {
        this.callEvent(event, null);
    }

    @Override
    public void callEvent(PacketEvent event, @Nullable Runnable postCallListenerAction) {
        for (final PacketListenerCommon listener : this.store.get()) {
            try {
                event.call(listener);
            } catch (Exception t) {
                // ignore handshake exceptions
                if (t.getClass() != InvalidHandshakeException.class) {
                    PacketEvents.getAPI().getLogger().log(Level.WARNING, "PacketEvents caught an unhandled exception while calling your listener.", t);
                }
            }

            if (postCallListenerAction != null) {
                postCallListenerAction.run();
            }
        }

        // For performance reasons, we don't want to re-encode the packet if it's not needed.
        if (event instanceof ProtocolPacketEvent && !((ProtocolPacketEvent) event).needsReEncode()) {
            ((ProtocolPacketEvent) event).setLastUsedWrapper(null);
        }
    }

    @Override
    public PacketListenerCommon registerListener(PacketListener listener, PacketListenerPriority priority) {
        final PacketListenerCommon packetListenerAbstract = listener.asAbstract(priority);
        return this.registerListener(packetListenerAbstract);
    }

    @Override
    public PacketListenerCommon registerListener(PacketListenerCommon listener) {
        this.store.add(listener);
        return listener;
    }

    @Override
    public PacketListenerCommon[] registerListeners(PacketListenerCommon... listeners) {
        for (final PacketListenerCommon listener : listeners) {
            this.registerListener(listener);
        }

        return listeners;
    }

    @Override
    public void unregisterListener(PacketListenerCommon listener) {
        this.store.remove(listener);
    }

    @Override
    public void unregisterListeners(PacketListenerCommon... listeners) {
        for (final PacketListenerCommon listener : listeners) {
            this.unregisterListener(listener);
        }
    }

    @Override
    public void unregisterAllListeners() {
        this.store.clear();
    }

    /**
     * This store is very speedy & thread safe.
     * <p>
     * Features
     * <p>
     * - Add/Remove listeners using CAS
     * <p>
     * - Iterate based on array snapshots
     * <p>
     * TODO: If needed could make this completely thread safe and so it tracks changed made to the store
     */
    public static class ListenerStore {
        private final AtomicReference<PacketListenerCommon[]> stored = new AtomicReference<>();

        @Override
        public ListenerStore clone() {
            if (this.stored.get() == null) {
                return new ListenerStore();
            }

            final ListenerStore clone = new ListenerStore();
            final PacketListenerCommon[] copying = this.stored.get();
            clone.stored.set(Arrays.copyOf(copying, copying.length));

            return clone;
        }

        public PacketListenerCommon[] get() {
            return this.stored.get();
        }

        public boolean add(PacketListenerCommon listener) {
            PacketListenerCommon[] current;
            PacketListenerCommon[] modified;

            do {
                current = this.stored.get();
                if (current == null) {
                    modified = new PacketListenerCommon[]{listener};
                    continue;
                }

                modified = Arrays.copyOf(current, current.length + 1);

                // Find insertion point
                int insertionPoint = Arrays.binarySearch(current, listener, LISTENER_COMPARATOR);

                if (insertionPoint >= 0) {
                    return false;
                }

                // To index
                insertionPoint = -insertionPoint - 1;

                // Copy current into modified + 1
                System.arraycopy(current, insertionPoint, modified, insertionPoint + 1, current.length - insertionPoint);
                modified[insertionPoint] = listener;

            } while (!this.stored.compareAndSet(current, modified));

            return true;
        }

        public boolean remove(PacketListenerCommon listener) {
            PacketListenerCommon[] current;
            PacketListenerCommon[] modified;

            do {
                current = this.stored.get();

                final int index = Arrays.binarySearch(current, listener, LISTENER_COMPARATOR);
                if (index < 0 || current[index] != listener) {
                    return false;
                }

                modified = Arrays.copyOf(current, current.length - 1);

                System.arraycopy(current, 0, modified, 0, index);
                System.arraycopy(current, index + 1, modified, index, current.length - index - 1);
            } while (!this.stored.compareAndSet(current, modified));

            return true;
        }

        public void call(UserConnectEvent event) {
            for (final PacketListenerCommon listener : this.stored.get()) {
                listener.onUserConnect(event);
            }
        }

        public void clear() {
            PacketListenerCommon[] current;
            PacketListenerCommon[] modified;

            do {
                current = this.stored.get();
                modified = null;
            } while (!this.stored.compareAndSet(current, modified));
        }
    }
}
