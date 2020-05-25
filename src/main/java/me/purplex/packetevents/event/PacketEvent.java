package me.purplex.packetevents.event;


import me.purplex.packetevents.PacketEvents;
import me.purplex.packetevents.enums.ServerVersion;

public class PacketEvent {
    protected long timestamp;
    protected static ServerVersion version = PacketEvents.getServerVersion();

    /**
     * Timestamp of the packet
     * @return
     */
    public long getTimestamp() {
        return timestamp;
    }
}
