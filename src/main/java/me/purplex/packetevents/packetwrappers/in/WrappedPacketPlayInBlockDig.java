package me.purplex.packetevents.packetwrappers.in;

import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.WrappedPacket;
import me.purplex.packetevents.packetwrappers.in._1_10.WrappedPacketPlayInBlockDig_1_10;
import me.purplex.packetevents.packetwrappers.in._1_11.WrappedPacketPlayInBlockDig_1_11;
import me.purplex.packetevents.packetwrappers.in._1_12.WrappedPacketPlayInBlockDig_1_12;
import me.purplex.packetevents.packetwrappers.in._1_13.WrappedPacketPlayInBlockDig_1_13;
import me.purplex.packetevents.packetwrappers.in._1_13_2.WrappedPacketPlayInBlockDig_1_13_2;
import me.purplex.packetevents.packetwrappers.in._1_14.WrappedPacketPlayInBlockDig_1_14;
import me.purplex.packetevents.packetwrappers.in._1_15.WrappedPacketPlayInBlockDig_1_15;
import me.purplex.packetevents.packetwrappers.in._1_7_10.WrappedPacketPlayInBlockDig_1_7_10;
import me.purplex.packetevents.packetwrappers.in._1_8.WrappedPacketPlayInBlockDig_1_8;
import me.purplex.packetevents.packetwrappers.in._1_8_3.WrappedPacketPlayInBlockDig_1_8_3;
import me.purplex.packetevents.packetwrappers.in._1_8_8.WrappedPacketPlayInBlockDig_1_8_8;
import me.purplex.packetevents.packetwrappers.in._1_9.WrappedPacketPlayInBlockDig_1_9;
import me.purplex.packetevents.packetwrappers.in._1_9_4.WrappedPacketPlayInBlockDig_1_9_4;

public class WrappedPacketPlayInBlockDig extends WrappedPacket {

    private PlayerDigType digType;

    public WrappedPacketPlayInBlockDig(Object packet) {
        super(packet);
    }

    public PlayerDigType getPlayerDigType() {
        return digType;
    }

    @Override
    public void setup() {
        if(version == ServerVersion.v_1_7_10) {
            WrappedPacketPlayInBlockDig_1_7_10 p = new WrappedPacketPlayInBlockDig_1_7_10(this.packet);
            this.digType = p.digType;
        }
        else if (version == ServerVersion.v_1_8) {
            WrappedPacketPlayInBlockDig_1_8 p = new WrappedPacketPlayInBlockDig_1_8(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_8_3) {
            WrappedPacketPlayInBlockDig_1_8_3 p = new WrappedPacketPlayInBlockDig_1_8_3(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_8_8) {
            WrappedPacketPlayInBlockDig_1_8_8 p = new WrappedPacketPlayInBlockDig_1_8_8(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_9) {
            WrappedPacketPlayInBlockDig_1_9 p = new WrappedPacketPlayInBlockDig_1_9(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_9_4) {
            WrappedPacketPlayInBlockDig_1_9_4 p = new WrappedPacketPlayInBlockDig_1_9_4(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_10) {
            WrappedPacketPlayInBlockDig_1_10 p = new WrappedPacketPlayInBlockDig_1_10(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_11) {
            WrappedPacketPlayInBlockDig_1_11 p = new WrappedPacketPlayInBlockDig_1_11(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_12) {
            WrappedPacketPlayInBlockDig_1_12 p = new WrappedPacketPlayInBlockDig_1_12(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_13) {
            WrappedPacketPlayInBlockDig_1_13 p = new WrappedPacketPlayInBlockDig_1_13(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_13_2) {
            WrappedPacketPlayInBlockDig_1_13_2 p = new WrappedPacketPlayInBlockDig_1_13_2(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_14) {
            WrappedPacketPlayInBlockDig_1_14 p = new WrappedPacketPlayInBlockDig_1_14(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_15) {
            WrappedPacketPlayInBlockDig_1_15 p = new WrappedPacketPlayInBlockDig_1_15(packet);
            this.digType = p.digType;
        }
    }
}
