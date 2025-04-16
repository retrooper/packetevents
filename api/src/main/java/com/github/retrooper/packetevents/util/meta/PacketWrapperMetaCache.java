package com.github.retrooper.packetevents.util.meta;

import org.jetbrains.annotations.NotNull;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class PacketWrapperMetaCache {
    private static final Map<IdentityKey, PacketWrapperMeta> META_CACHE = new ConcurrentHashMap<>();

    public static void setMeta(@NotNull Object buffer, @NotNull String key, @NotNull Object value) {
        META_CACHE.computeIfAbsent(new IdentityKey(buffer), unused -> new PacketWrapperMeta())
                .put(key, value);
    }

    public static boolean hasMeta(@NotNull Object buffer, @NotNull String key) {
        return META_CACHE.computeIfAbsent(new IdentityKey(buffer), unused -> new PacketWrapperMeta())
                .has(key);
    }

    public static Object getMeta(@NotNull Object buffer, @NotNull String key) {
        return META_CACHE.computeIfAbsent(new IdentityKey(buffer), unused -> new PacketWrapperMeta())
                .get(key);
    }

    public static void clean() {
        Set<IdentityKey> toRemoveKeys = new HashSet<>();
        META_CACHE.forEach((identityKey, meta) -> {
            if (meta.isExpired()) {
                toRemoveKeys.add(identityKey);
            }
        });
        toRemoveKeys.forEach(META_CACHE::remove);
    }

    static {
        ThreadFactory threadFactory = new ThreadFactory() {
            private int counter = 0;

            @Override
            public @NotNull Thread newThread(@NotNull Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName("packet-events - meta clean #" + counter++);
                thread.setDaemon(false);
                return thread;
            }
        };

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(threadFactory);
        executor.scheduleAtFixedRate(PacketWrapperMetaCache::clean, 0, 10, TimeUnit.SECONDS);
    }

    private static final class IdentityKey {
        private final Object ref;

        public IdentityKey(Object ref) {
            this.ref = ref;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IdentityKey that = (IdentityKey) o;
            return ref == that.ref;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(ref);
        }
    }
}
