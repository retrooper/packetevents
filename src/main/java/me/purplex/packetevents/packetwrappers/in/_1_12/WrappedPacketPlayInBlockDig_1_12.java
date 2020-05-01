package me.purplex.packetevents.packetwrappers.in._1_12;

import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.packetwrappers.api.WrappedVersionPacket;
import net.minecraft.server.v1_12_R1.PacketPlayInBlockDig;

public class WrappedPacketPlayInBlockDig_1_12 extends WrappedVersionPacket {

    public PlayerDigType digType;

    public WrappedPacketPlayInBlockDig_1_12(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInBlockDig p = (PacketPlayInBlockDig) packet;
        String name = p.c().name();
        this.digType = PlayerDigType.valueOf(name);
    }
}