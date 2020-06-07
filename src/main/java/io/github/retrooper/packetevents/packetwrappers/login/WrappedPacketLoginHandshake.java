package io.github.retrooper.packetevents.packetwrappers.login;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
public class WrappedPacketLoginHandshake extends WrappedPacket {
    private int protocolVersion, port;
    private String hostname;

    public WrappedPacketLoginHandshake(final Object packet) {
        super(packet);
    }
}
