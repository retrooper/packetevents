package me.purplex.packetevents.packetwrappers.api.version;

import me.purplex.packetevents.PacketEvents;
import me.purplex.packetevents.enums.ServerVersion;

public abstract class WrappedVersionPacket {
    protected final Object packet;
    protected static ServerVersion version = PacketEvents.getServerVersion();
    public  WrappedVersionPacket(Object packet) {
        this.packet = packet;
        setup();
    }

    protected abstract void setup();
}
