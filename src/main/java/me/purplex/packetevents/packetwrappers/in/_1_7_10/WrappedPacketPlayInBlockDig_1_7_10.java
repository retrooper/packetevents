package me.purplex.packetevents.packetwrappers.in._1_7_10;

import me.purplex.packetevents.enums.PlayerDigType;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockDig;

public class WrappedPacketPlayInBlockDig_1_7_10 {
    private final Object packet;
    public WrappedPacketPlayInBlockDig_1_7_10(Object packet) {
        this.packet = packet;
        setupDigType();
    }

    private void setupDigType() {
        PacketPlayInBlockDig p = (PacketPlayInBlockDig)packet;
        int status = p.e();
        this.digType = PlayerDigType.values()[status];
    }

    public PlayerDigType digType;

}
