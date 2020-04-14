package me.retrooper.packetevents;

import me.retrooper.packetevents.events.PacketManager;

public class PacketEvents {
    private static PacketManager packetManager;


    public static PacketManager getPacketManager() {
        if(packetManager == null) {
            packetManager = new PacketManager();
        }
        return packetManager;
    }
}
