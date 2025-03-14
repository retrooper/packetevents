package com.github.retrooper.packetevents.event;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RootEventManager extends InheritableEventManager {
    private final ReferenceQueue<Object> queue = new ReferenceQueue<>();
    private final Map<WeakKey, EventManager> children = new ConcurrentHashMap<>();
    private InheritableEventManager.ListenerStore store;

    @Override
    public PacketListenerCommon registerListener(PacketListenerCommon listener) {
        this.cleanup();
        return super.registerListener(listener);
    }

    @Override
    public void unregisterListener(PacketListenerCommon listener) {
        this.cleanup();
        super.unregisterListener(listener);
    }

    public EventManager getChildren(Object key) {
        this.cleanup();

        final WeakKey weakKey = new WeakKey(key, this.queue);
        return this.children.computeIfAbsent(weakKey, ($) -> new InheritableEventManager());
    }

    private void cleanup() {
        WeakKey weakKey;
        while ((weakKey = (WeakKey) this.queue.poll()) != null) {
            this.children.remove(weakKey);
        }
    }

    public static class WeakKey extends WeakReference<Object> {
        private final int hashCode;

        public WeakKey(Object referent, ReferenceQueue<Object> queue) {
            super(referent, queue);
            this.hashCode = System.identityHashCode(referent);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof WeakKey)) {
                return false;
            }

            final WeakKey other = (WeakKey) obj;
            final Object thisReferent = this.get();
            final Object otherReferent = other.get();

            // Check hashcode incase both been GC'ed
            return this.hashCode == other.hashCode && thisReferent == otherReferent;
        }
    }
}
