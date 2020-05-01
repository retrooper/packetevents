package me.purplex.packetevents.packetwrappers.in._1_14;

import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.packetwrappers.api.WrappedVersionPacket;
import net.minecraft.server.v1_14_R1.PacketPlayInBlockDig;

public class WrappedPacketPlayInBlockDig_1_14 extends WrappedVersionPacket {
    public PlayerDigType digType;

    public WrappedPacketPlayInBlockDig_1_14(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInBlockDig p = (PacketPlayInBlockDig) packet;
        String name = p.d().name();
        this.digType = PlayerDigType.valueOf(name);
    }
}
