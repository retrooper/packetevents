package io.github.retrooper.packetevents.packetwrappers.login;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import net.minecraft.server.v1_15_R1.PacketHandshakingInSetProtocol;

public class WrappedPacketLoginHandshake extends WrappedPacket {
    private int protocolVersion, port;
    private String hostname;

    PacketHandshakingInSetProtocol p;
    public WrappedPacketLoginHandshake(final Object packet) {
        super(packet);
    }
}
