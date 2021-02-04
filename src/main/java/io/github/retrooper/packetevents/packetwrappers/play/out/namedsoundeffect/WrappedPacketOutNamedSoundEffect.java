package io.github.retrooper.packetevents.packetwrappers.play.out.namedsoundeffect;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

//TODO test
class WrappedPacketOutNamedSoundEffect extends WrappedPacket implements SendableWrapper {
    private static boolean soundEffectVarExists;
    public WrappedPacketOutNamedSoundEffect(NMSPacket packet) {
        super(packet);
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

    public double getEffectPositionX() {
        return readInt(0) / 8.0D;
    }

    public double getEffectPositionY() {
        return readInt(1) / 8.0D;
    }

    public double getEffectPositionZ() {
        return readInt(2) / 8.0D;
    }

    //Might be more than 1.0 on some older versions
    public float getVolume(){
        return readFloat(0);
    }

    public float getPitch() {
        if (version.isOlderThan(ServerVersion.v_1_10)) {
            return readInt(1);
        }
        else {
            return readFloat(1);
        }
    }
}
