package com.github.retrooper.packetevents.util.meta;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class PacketWrapperMetaCache {
    private static final Map<Object, PacketWrapperMeta> META_CACHE = new ConcurrentHashMap<>();

    public static void setMeta(@NotNull Object buffer, @NotNull String key, @NotNull Object value) {
        META_CACHE.computeIfAbsent(buffer, __ -> new PacketWrapperMeta()).put(key, value);
    }

    public static @NotNull Map<String, Object> getMeta(@NotNull Object buffer) {
        return META_CACHE.computeIfAbsent(buffer, __ -> new PacketWrapperMeta()).get();
    }

    public static void clean() {
        Set<Object> toRemoveKeys = new HashSet<>();

        META_CACHE.forEach((o, packetWrapperMeta) -> {
            if (packetWrapperMeta.isExpired()) {
                toRemoveKeys.add(o);
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

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(threadFactory);
        scheduledExecutorService.scheduleAtFixedRate(PacketWrapperMetaCache::clean, 0, 10, TimeUnit.SECONDS);
    }
}
