package io.github.retrooper.packetevents.packetwrappers.play.out.spawnplayer;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutSpawnPlayer extends WrappedPacketEntityAbstraction implements SendableWrapper {
    public WrappedPacketOutSpawnPlayer(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving a0;
        net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving a1;
        net.minecraft.server.v1_9_R1.PacketPlayOutSpawnEntityLiving a2;
        net.minecraft.server.v1_9_R2.PacketPlayOutSpawnEntityLiving a3;
        net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving a4;
        net.minecraft.server.v1_13_R1.PacketPlayOutSpawnEntityLiving a5;
        net.minecraft.server.v1_13_R2.PacketPlayOutSpawnEntityLiving a6;
        net.minecraft.server.v1_16_R2.PacketPlayOutSpawnEntityLiving a7;
    }
    /*
    public Vector3d getPosition() {
        if (packet != null) {
            //2,3,4
            int x = readInt(2);
            int y = readInt(3);
            int z = readInt(4);
            return new Vector3d(x, y, z);
        }
        else {
            return null;
        }
    }*/

    @Override
    public Object asNMSPacket() {
        return null;
    }
}
