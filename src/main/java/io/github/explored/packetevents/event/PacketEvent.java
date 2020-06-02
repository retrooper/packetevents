package io.github.explored.packetevents.event;


import io.github.explored.packetevents.PacketEvents;
import io.github.explored.packetevents.enums.ServerVersion;

/**
 * Incoming and sent packets are called asynchronously,
 * recommended to avoid non thread safe bukkit operations
 */
public class PacketEvent {
    protected long timestamp = PacketEvents.currentTimeMS();
    protected static ServerVersion version = PacketEvents.getServerVersion();

    /**
     * Timestamp of the packet
     * @return
     */
    public long getTimestamp() {
        return timestamp;
    }
}
