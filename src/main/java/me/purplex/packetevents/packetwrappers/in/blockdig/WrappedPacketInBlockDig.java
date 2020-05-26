package me.purplex.packetevents.packetwrappers.in.blockdig;

import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.WrappedPacket;

public class WrappedPacketInBlockDig extends WrappedPacket {

    public PlayerDigType digType;

    public WrappedPacketInBlockDig(Object packet) {
        super(packet);
    }


    @Override
    public void setup() {
        if(version == ServerVersion.v_1_7_10) {
            WrappedPacketInBlockDig_1_7_10 p = new WrappedPacketInBlockDig_1_7_10(this.packet);
            this.digType = p.digType;
        }
        else if (version == ServerVersion.v_1_8) {
            WrappedPacketInBlockDig_1_8 p = new WrappedPacketInBlockDig_1_8(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_8_3) {
            WrappedPacketInBlockDig_1_8_3 p = new WrappedPacketInBlockDig_1_8_3(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_8_8) {
            WrappedPacketInBlockDig_1_8_8 p = new WrappedPacketInBlockDig_1_8_8(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_9) {
            WrappedPacketInBlockDig_1_9 p = new WrappedPacketInBlockDig_1_9(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_9_4) {
            WrappedPacketInBlockDig_1_9_4 p = new WrappedPacketInBlockDig_1_9_4(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_10) {
            WrappedPacketInBlockDig_1_10 p = new WrappedPacketInBlockDig_1_10(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_11) {
            WrappedPacketInBlockDig_1_11 p = new WrappedPacketInBlockDig_1_11(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_12) {
            WrappedPacketInBlockDig_1_12 p = new WrappedPacketInBlockDig_1_12(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_13) {
            WrappedPacketInBlockDig_1_13 p = new WrappedPacketInBlockDig_1_13(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_13_2) {
            WrappedPacketInBlockDig_1_13_2 p = new WrappedPacketInBlockDig_1_13_2(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_14) {
            WrappedPacketInBlockDig_1_14 p = new WrappedPacketInBlockDig_1_14(packet);
            this.digType = p.digType;
        } else if (version == ServerVersion.v_1_15) {
            WrappedPacketInBlockDig_1_15 p = new WrappedPacketInBlockDig_1_15(packet);
            this.digType = p.digType;
        }
        else {
            throwUnsupportedVersion();
        }
    }
}
