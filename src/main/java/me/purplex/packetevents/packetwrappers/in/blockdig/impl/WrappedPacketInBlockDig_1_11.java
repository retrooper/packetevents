package me.purplex.packetevents.packetwrappers.in.blockdig.impl;

import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_11_R1.PacketPlayInBlockDig;

public class WrappedPacketInBlockDig_1_11 extends WrappedVersionPacket {

    public PlayerDigType digType;

    public WrappedPacketInBlockDig_1_11(Object packet) {
       super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInBlockDig p = (PacketPlayInBlockDig)packet;
        String name = p.c().name();
        this.digType = PlayerDigType.valueOf(name);
    }

}
