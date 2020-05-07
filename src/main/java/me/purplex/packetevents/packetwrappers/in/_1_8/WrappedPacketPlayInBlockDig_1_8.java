package me.purplex.packetevents.packetwrappers.in._1_8;

import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_8_R1.PacketPlayInBlockDig;

public class WrappedPacketPlayInBlockDig_1_8 extends WrappedVersionPacket {
    public WrappedPacketPlayInBlockDig_1_8(Object packet) {
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
