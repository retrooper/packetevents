package me.purplex.packetevents.events.packetevent.packet.impl.in._1_9_4;

import me.purplex.packetevents.enums.PlayerDigType;
import net.minecraft.server.v1_9_R2.PacketPlayInBlockDig;

public class WrappedPacketPlayInBlockDig_1_9_4 {
    private final Object packet;
    public WrappedPacketPlayInBlockDig_1_9_4(Object packet) {
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
