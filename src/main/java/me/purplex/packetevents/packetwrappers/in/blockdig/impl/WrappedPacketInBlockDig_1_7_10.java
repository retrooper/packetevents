package me.purplex.packetevents.packetwrappers.in.blockdig.impl;

import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockDig;

public class WrappedPacketInBlockDig_1_7_10 extends WrappedVersionPacket {
    public WrappedPacketInBlockDig_1_7_10(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInBlockDig p = (PacketPlayInBlockDig)packet;
        int status = p.e();
        this.digType = PlayerDigType.values()[status];
    }

    public PlayerDigType digType;

}
