package me.purplex.packetevents.packetwrappers.in._1_11;

import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.packetwrappers.api.WrappedVersionPacket;
import net.minecraft.server.v1_11_R1.PacketPlayInBlockDig;

public class WrappedPacketPlayInBlockDig_1_11 extends WrappedVersionPacket {

    public PlayerDigType digType;

    public WrappedPacketPlayInBlockDig_1_11(Object packet) {
       super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInBlockDig p = (PacketPlayInBlockDig)packet;
        String name = p.c().name();
        this.digType = PlayerDigType.valueOf(name);
    }

}
