package me.purplex.packetevents.packetwrappers.in.blockdig.impl;

import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_8_R2.PacketPlayInBlockDig;

public class WrappedPacketInBlockDig_1_8_3 extends WrappedVersionPacket {
    public PlayerDigType digType;

    public WrappedPacketInBlockDig_1_8_3(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInBlockDig p = (PacketPlayInBlockDig) packet;
        String name = p.c().name();
        this.digType = PlayerDigType.valueOf(name);
    }


}
