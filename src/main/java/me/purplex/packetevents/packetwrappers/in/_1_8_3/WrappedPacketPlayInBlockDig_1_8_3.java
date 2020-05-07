package me.purplex.packetevents.packetwrappers.in._1_8_3;

import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_8_R2.PacketPlayInBlockDig;

public class WrappedPacketPlayInBlockDig_1_8_3 extends WrappedVersionPacket {
    public PlayerDigType digType;

    public WrappedPacketPlayInBlockDig_1_8_3(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInBlockDig p = (PacketPlayInBlockDig) packet;
        String name = p.c().name();
        this.digType = PlayerDigType.valueOf(name);
    }


}
