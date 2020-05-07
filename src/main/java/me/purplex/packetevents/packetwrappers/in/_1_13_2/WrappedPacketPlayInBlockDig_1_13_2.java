package me.purplex.packetevents.packetwrappers.in._1_13_2;

import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_13_R2.PacketPlayInBlockDig;

public class WrappedPacketPlayInBlockDig_1_13_2 extends WrappedVersionPacket {
    public PlayerDigType digType;

    public WrappedPacketPlayInBlockDig_1_13_2(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInBlockDig p = (PacketPlayInBlockDig) packet;
        String name = p.d().name();
        this.digType = PlayerDigType.valueOf(name);
    }

}
