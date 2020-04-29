package me.purplex.packetevents.events.packetevent.packet.impl.in._1_14;

import me.purplex.packetevents.enums.PlayerDigType;
import net.minecraft.server.v1_14_R1.PacketPlayInBlockDig;

public class WrappedPacketPlayInBlockDig_1_14 {
    private final Object packet;
    public WrappedPacketPlayInBlockDig_1_14(Object packet) {
        this.packet = packet;
        setupDigType();
    }

    private void setupDigType() {
        PacketPlayInBlockDig p = (PacketPlayInBlockDig)packet;
        String name = p.d().name();
        this.digType = PlayerDigType.valueOf(name);
    }

    public PlayerDigType digType;

}
