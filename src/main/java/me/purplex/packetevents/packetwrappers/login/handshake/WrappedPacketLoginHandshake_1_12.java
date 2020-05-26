package me.purplex.packetevents.packetwrappers.login.handshake;

import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_12_R1.PacketHandshakingInSetProtocol;

class WrappedPacketLoginHandshake_1_12 extends WrappedVersionPacket {
    public String hostname;
    public int port;
    public int protocolVersion;
    WrappedPacketLoginHandshake_1_12(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketHandshakingInSetProtocol p = (PacketHandshakingInSetProtocol)packet;
        this.hostname = p.hostname;
        this.port = p.port;
        this.protocolVersion = p.b();
    }
}
