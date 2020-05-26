package me.purplex.packetevents.packetwrappers.in.blockdig.impl;

import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_15_R1.PacketPlayInBlockDig;

public class WrappedPacketInBlockDig_1_15 extends WrappedVersionPacket {
    public PlayerDigType digType;

    public WrappedPacketInBlockDig_1_15(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInBlockDig p = (PacketPlayInBlockDig) packet;
        String name = p.d().name();
        this.digType = PlayerDigType.valueOf(name);
    }
}