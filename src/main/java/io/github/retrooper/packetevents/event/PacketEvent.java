package io.github.retrooper.packetevents.event;


import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.enums.ServerVersion;

public class PacketEvent {
    protected static ServerVersion version = PacketEvents.getAPI().getServerUtilities().getServerVersion();
    private long timestamp = PacketEvents.getAPI().currentMillis();

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
