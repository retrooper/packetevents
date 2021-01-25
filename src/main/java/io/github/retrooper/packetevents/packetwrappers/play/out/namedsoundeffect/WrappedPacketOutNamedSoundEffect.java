package io.github.retrooper.packetevents.packetwrappers.play.out.namedsoundeffect;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
//TODO finish
class WrappedPacketOutNamedSoundEffect extends WrappedPacket implements SendableWrapper {
    public WrappedPacketOutNamedSoundEffect(NMSPacket packet) {
        super(packet);
       /* net.minecraft.server.v1_7_R4.PacketPlayOutNamedSoundEffect nse1;
        net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect nse2;
        net.minecraft.server.v1_9_R1.PacketPlayOutNamedSoundEffect nse3;
        net.minecraft.server.v1_12_R1.PacketPlayOutNamedSoundEffect nse4;
        net.minecraft.server.v1_13_R2.PacketPlayOutNamedSoundEffect nse5;
        net.minecraft.server.v1_16_R2.PacketPlayOutNamedSoundEffect nse6;*/
    }

    @Override
    public Object asNMSPacket() {
        return null;
    }

}
