package io.github.retrooper.packetevents.packetwrappers.play.out.namedsoundeffect;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;

//TODO finish
class WrappedPacketOutNamedSoundEffect extends WrappedPacket implements SendableWrapper {
    private static boolean soundEffectVarExists;
    public WrappedPacketOutNamedSoundEffect(NMSPacket packet) {
        super(packet);
        /*net.minecraft.server.v1_7_R4.PacketPlayOutNamedSoundEffect nse1;
        net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect nse2;
        net.minecraft.server.v1_9_R1.PacketPlayOutNamedSoundEffect nse3;
        net.minecraft.server.v1_12_R1.PacketPlayOutNamedSoundEffect nse4;
        net.minecraft.server.v1_13_R2.PacketPlayOutNamedSoundEffect nse5;
        net.minecraft.server.v1_16_R2.PacketPlayOutNamedSoundEffect nse6;*/
    }

    @Override
    protected void load() {
        soundEffectVarExists = Reflection.getField(packet.getClass(), NMSUtils.soundEffectClass, 0) != null;
    }

    @Override
    public Object asNMSPacket() {
        return null;
    }

    public String getSoundEffectName() {
        if (soundEffectVarExists) {
            Object soundEffect = readObject(0, NMSUtils.soundEffectClass);
            WrappedPacket soundEffectWrapper = new WrappedPacket(new NMSPacket(soundEffect));
            Object minecraftKey = soundEffectWrapper.readObject(0, NMSUtils.minecraftKeyClass);
            return NMSUtils.getStringFromMinecraftKey(minecraftKey);
        }
        else {
            return readString(0);
        }
    }

}
