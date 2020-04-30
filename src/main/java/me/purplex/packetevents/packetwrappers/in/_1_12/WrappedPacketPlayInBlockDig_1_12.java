package me.purplex.packetevents.packetwrappers.in._1_12;

import me.purplex.packetevents.enums.PlayerDigType;
import net.minecraft.server.v1_12_R1.PacketPlayInBlockDig;

public class WrappedPacketPlayInBlockDig_1_12 {
    private final Object packet;
    public WrappedPacketPlayInBlockDig_1_12(Object packet) {
        this.packet = packet;
        setupDigType();
    }

    private void setupDigType() {
        PacketPlayInBlockDig p = (PacketPlayInBlockDig)packet;
        String name = p.c().name();
        this.digType = PlayerDigType.valueOf(name);
    }

    public PlayerDigType digType;

}