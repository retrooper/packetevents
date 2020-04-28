package me.purplex.packetevents;

import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.events.manager.PacketManager;

public class PacketEvents {
    private static PacketManager packetManager;

    public static ServerVersion getServerVersion() {
        return ServerVersion.getVersion();
    }


    public static PacketManager getPacketManager() {
        if (packetManager == null) {
            packetManager = new PacketManager();
        }
        return packetManager;
    }
}
