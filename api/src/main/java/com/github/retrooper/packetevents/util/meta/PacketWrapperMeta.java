package com.github.retrooper.packetevents.util.meta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PacketWrapperMeta {
    private final Map<String, Object> data = new HashMap<>();
    private final long timestamp = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10);

    public void put(@NotNull String key, @NotNull Object value) {
        this.data.put(key, value);
    }

    public @Nullable Object getMeta(@NotNull String key) {
        return this.data.get(key);
    }

    public @NotNull Map<String, Object> get() {
        return this.data;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public boolean isExpired() {
        return getTimestamp() < System.currentTimeMillis();
    }
}
