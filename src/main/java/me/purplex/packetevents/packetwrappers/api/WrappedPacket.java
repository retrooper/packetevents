package me.purplex.packetevents.packetwrappers.api;

import me.purplex.packetevents.enums.ServerVersion;

public class WrappedPacket {
    protected Object packet;
    protected static ServerVersion version;
    static {
        version = ServerVersion.getVersion();
    }
    public WrappedPacket(Object packet) {
        this.packet = packet;
        setup();
    }

    protected void setup() {

    }
}
