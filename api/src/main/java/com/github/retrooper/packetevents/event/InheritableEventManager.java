package com.github.retrooper.packetevents.event;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

public class InheritableEventManager extends EventManager {

    @Override
    public void callEvent(PacketEvent event) {

    }

    @Override
    public void callEvent(PacketEvent event, @Nullable Runnable postCallListenerAction) {

    }

    @Override
    public PacketListenerCommon registerListener(PacketListener listener, PacketListenerPriority priority) {
        return null;
    }

    @Override
    public PacketListenerCommon registerListener(PacketListenerCommon listener) {
        return null;
    }

    @Override
    public PacketListenerCommon[] registerListeners(PacketListenerCommon... listeners) {
        return new PacketListenerCommon[0];
    }

    @Override
    public void unregisterListener(PacketListenerCommon listener) {

    }

    @Override
    public void unregisterListeners(PacketListenerCommon... listeners) {

    }

    @Override
    public void unregisterAllListeners() {

    }

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

        public PacketListenerCommon[] get() {
            return this.stored.get();
        }

        public void register(PacketListenerCommon listener) {
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
                if (insertionPoint < 0) {
                    insertionPoint = -insertionPoint - 1;
                }

                // Copy current into modified + 1
                System.arraycopy(current, insertionPoint, modified, insertionPoint + 1, current.length - insertionPoint);
                modified[insertionPoint] = listener;

            } while (!this.stored.compareAndSet(current, modified));
        }

        public void remove(PacketListenerCommon listener) {
            PacketListenerCommon[] current;
            PacketListenerCommon[] modified;

            do {
                current = this.stored.get();

                final int index = Arrays.binarySearch(current, listener, LISTENER_COMPARATOR);
                if (index != 0) {
                    return;
                }

                modified = Arrays.copyOf(current, current.length - 1);

                System.arraycopy(current, 0, modified, 0, index);
                System.arraycopy(current, index + 1, modified, index, current.length - index - 1);

            } while (!this.stored.compareAndSet(current, modified));
        }

        public void call(UserConnectEvent event) {
            for (final PacketListenerCommon listener : this.stored.get()) {
                listener.onUserConnect(event);
            }
        }
    }
}
