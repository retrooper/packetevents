package me.purplex.packetevents.packetwrappers.login.handshake;

import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_7_R4.PacketHandshakingInSetProtocol;

class WrappedPacketLoginHandshake_1_7_10 extends WrappedVersionPacket {
    public String hostname;
    public int port;
    public int protocolVersion;
    WrappedPacketLoginHandshake_1_7_10(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketHandshakingInSetProtocol p = (PacketHandshakingInSetProtocol)packet;
        this.hostname = p.b;
        this.port = p.c;
        this.protocolVersion = p.d();
    }
}
