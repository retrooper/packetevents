package me.purplex.packetevents.packetwrappers.in.chat;

import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.WrappedPacket;

public class WrappedPacketInChat extends WrappedPacket {
    public String message;
    public WrappedPacketInChat(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        if(version == ServerVersion.v_1_7_10) {
            WrappedPacketInChat_1_7_10 p = new WrappedPacketInChat_1_7_10(this.packet);
            this.message = p.message;
        }
        else if (version == ServerVersion.v_1_8) {
            WrappedPacketInChat_1_8 p = new WrappedPacketInChat_1_8(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_8_3) {
            WrappedPacketInChat_1_8_3 p = new WrappedPacketInChat_1_8_3(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_8_8) {
            WrappedPacketInChat_1_8_8 p = new WrappedPacketInChat_1_8_8(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_9) {
            WrappedPacketInChat_1_9 p = new WrappedPacketInChat_1_9(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_9_4) {
            WrappedPacketInChat_1_9_4 p = new WrappedPacketInChat_1_9_4(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_10) {
            WrappedPacketInChat_1_10 p = new WrappedPacketInChat_1_10(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_11) {
            WrappedPacketInChat_1_11 p = new WrappedPacketInChat_1_11(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_12) {
            WrappedPacketInChat_1_12 p = new WrappedPacketInChat_1_12(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_13) {
            WrappedPacketInChat_1_13 p = new WrappedPacketInChat_1_13(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_13_2) {
            WrappedPacketInChat_1_13_2 p = new WrappedPacketInChat_1_13_2(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_14) {
            WrappedPacketInChat_1_14 p = new WrappedPacketInChat_1_14(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_15) {
            WrappedPacketInChat_1_15 p = new WrappedPacketInChat_1_15(packet);
            this.message = p.message;
        }
        else {
            throwUnsupportedVersion();
        }
    }
}
