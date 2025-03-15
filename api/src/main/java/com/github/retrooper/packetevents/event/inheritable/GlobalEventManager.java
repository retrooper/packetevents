package com.github.retrooper.packetevents.event.inheritable;

import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListenerCommon;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GlobalEventManager extends InheritableEventManager {
    private final ReferenceQueue<Object> queue = new ReferenceQueue<>();
    private final Map<WeakKey, EventManager> children = new ConcurrentHashMap<>();
    private final ReadWriteLock listenerConsistencyLock = new ReentrantReadWriteLock();
    private final InheritableEventManager.ListenerStore store = new ListenerStore();

    @Override
    public PacketListenerCommon registerListener(PacketListenerCommon listener) {
        this.cleanup();

        this.listenerConsistencyLock.readLock().lock();
        try {
            if (!this.store.add(listener)) {
                return listener;
            }

            for (final EventManager eventManager : this.children.values()) {
                eventManager.registerListener(listener);
            }

            return listener;
        } finally {
            this.listenerConsistencyLock.readLock().unlock();
        }
    }

    @Override
    public void unregisterListener(PacketListenerCommon listener) {
        this.cleanup();

        this.listenerConsistencyLock.readLock().lock();
        try {
            if (!this.store.remove(listener)) {
                return;
            }

            for (final EventManager eventManager : this.children.values()) {
                eventManager.unregisterListener(listener);
            }
        } finally {
            this.listenerConsistencyLock.readLock().unlock();
        }
    }

    public int childrenCount() {
        this.cleanup();

        return this.children.size();
    }

    public EventManager getChildren(Object key) {
        this.cleanup();

        final WeakKey weakKey = new WeakKey(key, this.queue);
        return this.children.computeIfAbsent(weakKey, ($) -> {
            this.listenerConsistencyLock.writeLock().lock();
            try {
                return new InheritableEventManager(this.store.clone());
            } finally {
                this.listenerConsistencyLock.writeLock().unlock();
            }
        });
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
