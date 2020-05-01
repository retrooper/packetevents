package me.purplex.packetevents.packetwrappers.api;

import me.purplex.packetevents.enums.ServerVersion;

public abstract class WrappedVersionPacket {
    protected final Object packet;
    protected static ServerVersion version;
    static {
        version = ServerVersion.getVersion();
    }
    public  WrappedVersionPacket(Object packet) {
        this.packet = packet;
        setup();
    }

    protected abstract void setup();
}
