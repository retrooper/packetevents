package io.github.retrooper.packetevents.packetwrappers.play.out.mapchunk;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

class WrappedPacketOutMapChunk extends WrappedPacket {
    public WrappedPacketOutMapChunk(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        Object block = new net.minecraft.server.v1_8_R3.Block(null, null);
        net.minecraft.server.v1_7_R4.PacketPlayOutMapChunk a0;
        net.minecraft.server.v1_8_R3.PacketPlayOutMapChunk a1;
        net.minecraft.server.v1_9_R1.PacketPlayOutMapChunk a2;
        net.minecraft.server.v1_9_R2.PacketPlayOutMapChunk a3;
        net.minecraft.server.v1_12_R1.PacketPlayOutMapChunk a4;
        net.minecraft.server.v1_13_R1.PacketPlayOutMapChunk a5;
        net.minecraft.server.v1_13_R2.PacketPlayOutMapChunk a6;
        net.minecraft.server.v1_16_R2.PacketPlayOutMapChunk a7;

    }


}
