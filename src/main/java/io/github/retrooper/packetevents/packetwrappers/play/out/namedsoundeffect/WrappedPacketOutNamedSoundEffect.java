package io.github.retrooper.packetevents.packetwrappers.play.out.namedsoundeffect;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

//TODO finish wrapper and TEST and make sendable
class WrappedPacketOutNamedSoundEffect extends WrappedPacket {
    private static boolean soundEffectVarExists;

    public WrappedPacketOutNamedSoundEffect(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        soundEffectVarExists = Reflection.getField(packet.getClass(), NMSUtils.soundEffectClass, 0) != null;
        net.minecraft.server.v1_7_R4.PacketPlayOutNamedSoundEffect a0;
        net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect a1;
        net.minecraft.server.v1_9_R1.PacketPlayOutNamedSoundEffect a2;
        net.minecraft.server.v1_9_R2.PacketPlayOutNamedSoundEffect a3;
        net.minecraft.server.v1_12_R1.PacketPlayOutNamedSoundEffect a4;
        net.minecraft.server.v1_13_R1.PacketPlayOutNamedSoundEffect a5;
        net.minecraft.server.v1_13_R2.PacketPlayOutNamedSoundEffect a6;
        net.minecraft.server.v1_16_R2.PacketPlayOutNamedSoundEffect a7;

    }

    public String getSoundEffectName() {
        if (soundEffectVarExists) {
            Object soundEffect = readObject(0, NMSUtils.soundEffectClass);
            WrappedPacket soundEffectWrapper = new WrappedPacket(new NMSPacket(soundEffect));
            Object minecraftKey = soundEffectWrapper.readObject(0, NMSUtils.minecraftKeyClass);
            return NMSUtils.getStringFromMinecraftKey(minecraftKey);
        } else {
            return readString(0);
        }
    }

    public void setSoundEffectName(String name) {
        if (soundEffectVarExists) {
            Object soundEffect = readObject(0, NMSUtils.soundEffectClass);
            WrappedPacket soundEffectWrapper = new WrappedPacket(new NMSPacket(soundEffect));
            Object minecraftKey = NMSUtils.generateMinecraftKey(name);
            soundEffectWrapper.write(NMSUtils.minecraftKeyClass, 0, minecraftKey);
        } else {
            writeString(0, name);
        }
    }

    public double getEffectPositionX() {
        return readInt(0) / 8.0D;
    }

    public void setEffectPositionX(double x) {
        writeInt(0, (int) (x * 8.0D));
    }

    public double getEffectPositionY() {
        return readInt(1) / 8.0D;
    }

    public void setEffectPositionY(double y) {
        writeInt(1, (int) (y * 8.0D));
    }

    public double getEffectPositionZ() {
        return readInt(2) / 8.0D;
    }

    public void setEffectPositionZ(double z) {
        writeInt(2, (int) (z * 8.0D));
    }

    //Might be more than 1.0 on some older versions
    public float getVolume() {
        return readFloat(0);
    }

    public void setVolume(float volume) {
        writeFloat(0, volume);
    }

    public float getPitch() {
        if (version.isOlderThan(ServerVersion.v_1_10)) {
            return readInt(1);
        } else {
            return readFloat(1);
        }
    }

    public void setPitch(float pitch) {
        if (version.isOlderThan(ServerVersion.v_1_10)) {
            writeInt(1, (int) pitch);
        } else {
            writeFloat(1, pitch);
        }
    }

    public void setPitch(int pitch) {
        if (version.isOlderThan(ServerVersion.v_1_10)) {
            writeInt(1, pitch);
        } else {
            writeFloat(1, pitch);
        }
    }
}
