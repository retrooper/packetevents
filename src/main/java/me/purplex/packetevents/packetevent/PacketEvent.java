package me.purplex.packetevents.packetevent;


import me.purplex.packetevents.enums.ServerVersion;

public class PacketEvent {
    protected long timestamp;
    protected static ServerVersion version;
    static {
        version = ServerVersion.getVersion();
    }

    /**
     * Timestamp of the packet
     * @return
     */
    public long getTimestamp() {
        return timestamp;
    }
}
