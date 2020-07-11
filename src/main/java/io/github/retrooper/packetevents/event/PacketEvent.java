package io.github.retrooper.packetevents.event;


import io.github.retrooper.packetevents.enums.ServerVersion;

public class PacketEvent {
    protected static ServerVersion version = ServerVersion.getVersion();
    private long timestamp = System.currentTimeMillis();

    /**
     * Timestamp of the packet
     *
     * @return
     */
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
