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
        //net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving sp;
    }

    @Override
    public Object asNMSPacket() {
        return null;
    }
}
