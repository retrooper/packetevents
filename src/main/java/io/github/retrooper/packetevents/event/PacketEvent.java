/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.event;


import io.github.retrooper.packetevents.enums.ServerVersion;

public abstract class PacketEvent {
    public static ServerVersion version;
    private long timestamp = System.currentTimeMillis();

    /**
     * Timestamp of the packet
     *
     * @return timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public abstract boolean isAsyncByDefault();
}
