package me.purplex.packetevents;

import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetevent.manager.PacketManager;

public class PacketEvents {
    private final static PacketManager packetManager;

    static {
        packetManager = new PacketManager();
    }
    public static ServerVersion getServerVersion() {
        return ServerVersion.getVersion();
    }


    public static PacketManager getPacketManager() {
        return packetManager;
    }
}
