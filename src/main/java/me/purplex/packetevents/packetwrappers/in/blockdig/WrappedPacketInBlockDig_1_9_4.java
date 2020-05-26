package me.purplex.packetevents.packetwrappers.in.blockdig;

import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_9_R2.PacketPlayInBlockDig;

class WrappedPacketInBlockDig_1_9_4 extends WrappedVersionPacket {

    public PlayerDigType digType;

    WrappedPacketInBlockDig_1_9_4(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInBlockDig p = (PacketPlayInBlockDig)packet;
        String name = p.c().name();
        this.digType = PlayerDigType.valueOf(name);
    }

}
