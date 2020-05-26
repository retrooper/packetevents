package me.purplex.packetevents.packetwrappers.in.blockdig.impl;

import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;

public class WrappedPacketInBlockDig_1_8_8 extends WrappedVersionPacket {
    public WrappedPacketInBlockDig_1_8_8(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInBlockDig p = (PacketPlayInBlockDig)packet;
        String name = p.c().name();
        this.digType = PlayerDigType.valueOf(name);
    }

    public PlayerDigType digType;

}
