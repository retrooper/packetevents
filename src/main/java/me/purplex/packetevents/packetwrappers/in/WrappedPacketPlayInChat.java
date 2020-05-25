package me.purplex.packetevents.packetwrappers.in;

import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.WrappedPacket;
import me.purplex.packetevents.packetwrappers.in._1_10.WrappedPacketPlayInChat_1_10;
import me.purplex.packetevents.packetwrappers.in._1_11.WrappedPacketPlayInChat_1_11;
import me.purplex.packetevents.packetwrappers.in._1_12.WrappedPacketPlayInChat_1_12;
import me.purplex.packetevents.packetwrappers.in._1_13.WrappedPacketPlayInChat_1_13;
import me.purplex.packetevents.packetwrappers.in._1_13_2.WrappedPacketPlayInChat_1_13_2;
import me.purplex.packetevents.packetwrappers.in._1_14.WrappedPacketPlayInChat_1_14;
import me.purplex.packetevents.packetwrappers.in._1_15.WrappedPacketPlayInChat_1_15;
import me.purplex.packetevents.packetwrappers.in._1_7_10.WrappedPacketPlayInChat_1_7_10;
import me.purplex.packetevents.packetwrappers.in._1_8.WrappedPacketPlayInChat_1_8;
import me.purplex.packetevents.packetwrappers.in._1_8_3.WrappedPacketPlayInChat_1_8_3;
import me.purplex.packetevents.packetwrappers.in._1_8_8.WrappedPacketPlayInChat_1_8_8;
import me.purplex.packetevents.packetwrappers.in._1_9.WrappedPacketPlayInChat_1_9;
import me.purplex.packetevents.packetwrappers.in._1_9_4.WrappedPacketPlayInChat_1_9_4;

public class WrappedPacketPlayInChat extends WrappedPacket {
    public String message;
    public WrappedPacketPlayInChat(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        if(version == ServerVersion.v_1_7_10) {
            WrappedPacketPlayInChat_1_7_10 p = new WrappedPacketPlayInChat_1_7_10(this.packet);
            this.message = p.message;
        }
        else if (version == ServerVersion.v_1_8) {
            WrappedPacketPlayInChat_1_8 p = new WrappedPacketPlayInChat_1_8(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_8_3) {
            WrappedPacketPlayInChat_1_8_3 p = new WrappedPacketPlayInChat_1_8_3(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_8_8) {
            WrappedPacketPlayInChat_1_8_8 p = new WrappedPacketPlayInChat_1_8_8(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_9) {
            WrappedPacketPlayInChat_1_9 p = new WrappedPacketPlayInChat_1_9(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_9_4) {
            WrappedPacketPlayInChat_1_9_4 p = new WrappedPacketPlayInChat_1_9_4(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_10) {
            WrappedPacketPlayInChat_1_10 p = new WrappedPacketPlayInChat_1_10(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_11) {
            WrappedPacketPlayInChat_1_11 p = new WrappedPacketPlayInChat_1_11(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_12) {
            WrappedPacketPlayInChat_1_12 p = new WrappedPacketPlayInChat_1_12(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_13) {
            WrappedPacketPlayInChat_1_13 p = new WrappedPacketPlayInChat_1_13(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_13_2) {
            WrappedPacketPlayInChat_1_13_2 p = new WrappedPacketPlayInChat_1_13_2(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_14) {
            WrappedPacketPlayInChat_1_14 p = new WrappedPacketPlayInChat_1_14(packet);
            this.message = p.message;
        } else if (version == ServerVersion.v_1_15) {
            WrappedPacketPlayInChat_1_15 p = new WrappedPacketPlayInChat_1_15(packet);
            this.message = p.message;
        }
    }
}
