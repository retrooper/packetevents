package me.purplex.packetevents.packetwrappers.login.handshake;

import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.WrappedPacket;

public class WrappedPacketLoginHandshake extends WrappedPacket {
    public String hostname;
    public int port;
    public int protocolVersion;
    public WrappedPacketLoginHandshake(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        if(version == ServerVersion.v_1_7_10) {
            WrappedPacketLoginHandshake_1_7_10 p = new WrappedPacketLoginHandshake_1_7_10(packet);
            this.hostname = p.hostname;
            this.port = p.port;
            this.protocolVersion = p.protocolVersion;
        }
        else if(version == ServerVersion.v_1_8) {
            WrappedPacketLoginHandshake_1_8 p = new WrappedPacketLoginHandshake_1_8(packet);
            this.hostname = p.hostname;
            this.port = p.port;
            this.protocolVersion = p.protocolVersion;
        }
        else if(version == ServerVersion.v_1_8_3) {
            WrappedPacketLoginHandshake_1_8_3 p = new WrappedPacketLoginHandshake_1_8_3(packet);
            this.hostname = p.hostname;
            this.port = p.port;
            this.protocolVersion = p.protocolVersion;
        }
        else if(version == ServerVersion.v_1_8_8) {
            WrappedPacketLoginHandshake_1_8_8 p = new WrappedPacketLoginHandshake_1_8_8(packet);
            this.hostname = p.hostname;
            this.port = p.port;
            this.protocolVersion = p.protocolVersion;
        }
        else if(version == ServerVersion.v_1_9) {
            WrappedPacketLoginHandshake_1_9 p = new WrappedPacketLoginHandshake_1_9(packet);
            this.hostname = p.hostname;
            this.port = p.port;
            this.protocolVersion = p.protocolVersion;
        }
        else if(version==ServerVersion.v_1_9_4) {
            WrappedPacketLoginHandshake_1_9_4 p = new WrappedPacketLoginHandshake_1_9_4(packet);
            this.hostname = p.hostname;
            this.port = p.port;
            this.protocolVersion = p.protocolVersion;
        }
        else if(version==ServerVersion.v_1_10) {
            WrappedPacketLoginHandshake_1_10 p = new WrappedPacketLoginHandshake_1_10(packet);
            this.hostname = p.hostname;
            this.port = p.port;
            this.protocolVersion = p.protocolVersion;
        }
        else if(version == ServerVersion.v_1_11) {
            WrappedPacketLoginHandshake_1_11 p = new WrappedPacketLoginHandshake_1_11(packet);
            this.hostname = p.hostname;
            this.port = p.port;
            this.protocolVersion = p.protocolVersion;
        }
        else if(version == ServerVersion.v_1_12) {
            WrappedPacketLoginHandshake_1_12 p = new WrappedPacketLoginHandshake_1_12(packet);
            this.hostname = p.hostname;
            this.port = p.port;
            this.protocolVersion = p.protocolVersion;
        }
        else if(version == ServerVersion.v_1_13) {
            WrappedPacketLoginHandshake_1_13 p = new WrappedPacketLoginHandshake_1_13(packet);
            this.hostname = p.hostname;
            this.port = p.port;
            this.protocolVersion = p.protocolVersion;
        }
        else if(version == ServerVersion.v_1_13_2) {
            WrappedPacketLoginHandshake_1_13_2 p = new WrappedPacketLoginHandshake_1_13_2(packet);
            this.hostname = p.hostname;
            this.port = p.port;
            this.protocolVersion = p.protocolVersion;
        }
        else if(version == ServerVersion.v_1_14) {
            WrappedPacketLoginHandshake_1_14 p = new WrappedPacketLoginHandshake_1_14(packet);
            this.hostname = p.hostname;
            this.port = p.port;
            this.protocolVersion = p.protocolVersion;
        }
        else if(version == ServerVersion.v_1_15) {
            WrappedPacketLoginHandshake_1_15 p = new WrappedPacketLoginHandshake_1_15(packet);
            this.hostname = p.hostname;
            this.port = p.port;
            this.protocolVersion = p.protocolVersion;
        }
        else {
            throwUnsupportedVersion();
        }
    }
}
