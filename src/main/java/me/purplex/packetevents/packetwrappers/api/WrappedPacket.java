package me.purplex.packetevents.packetwrappers.api;

import me.purplex.packetevents.enums.ServerVersion;

/**
 * All the wrappers are still under development.
 * It is a lot of work, I must make 13 wrappers for each packet.
 */
public class WrappedPacket {
    protected Object packet;
    protected static final ServerVersion version = ServerVersion.getVersion();
    public WrappedPacket(Object packet) {
        this.packet = packet;
        setup();
    }

    protected void setup() {

    }
}
