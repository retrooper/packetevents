package me.purplex.packetevents.event;


import me.purplex.packetevents.PacketEvents;
import me.purplex.packetevents.enums.ServerVersion;

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
