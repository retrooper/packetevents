package io.github.retrooper.packetevents.packetwrappers.play.out.namedsoundeffect;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3d;

//TODO Test sending
public class WrappedPacketOutNamedSoundEffect extends WrappedPacket implements SendableWrapper {
    private static boolean soundEffectVarExists;
    private static float pitchMultiplier = 63.0F;
    private String soundEffectName;
    private Vector3d effectPosition;
    private float volume, pitch;

    public WrappedPacketOutNamedSoundEffect(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutNamedSoundEffect(String soundEffectName, Vector3d effectPosition, float volume, float pitch) {
        this.soundEffectName = soundEffectName;
        this.effectPosition = effectPosition;
        this.volume = volume;
        this.pitch = pitch;
    }

    public WrappedPacketOutNamedSoundEffect(String soundEffectName, double effectPositionX, double effectPositionY, double effectPositionZ, float volume, float pitch) {
        this.soundEffectName = soundEffectName;
        this.effectPosition = new Vector3d(effectPositionX, effectPositionY, effectPositionZ);
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    protected void load() {
        soundEffectVarExists = Reflection.getField(packet.getClass(), NMSUtils.soundEffectClass, 0) != null;
        pitchMultiplier = version.isNewerThan(ServerVersion.v_1_9_4) ? 1 : version.isNewerThan(ServerVersion.v_1_8_8) ? 63.5F : 63.0F;
        net.minecraft.server.v1_7_R4.PacketPlayOutNamedSoundEffect a0;
        net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect a1;
        net.minecraft.server.v1_9_R1.PacketPlayOutNamedSoundEffect a2;
        net.minecraft.server.v1_9_R2.PacketPlayOutNamedSoundEffect a3;
        net.minecraft.server.v1_12_R1.PacketPlayOutNamedSoundEffect a4;
        net.minecraft.server.v1_13_R1.PacketPlayOutNamedSoundEffect a5;
        net.minecraft.server.v1_13_R2.PacketPlayOutNamedSoundEffect a6;
        net.minecraft.server.v1_16_R2.PacketPlayOutNamedSoundEffect a7;
        //TODO use default constructor, initiate the sound category and the soundeffect, DEAL WITH SOUND CATEGORTIES AS THEY DONT EXIST ON 1.8!!1

    }

    public String getSoundEffectName() {
        if (packet != null) {
            if (soundEffectVarExists) {
                Object soundEffect = readObject(0, NMSUtils.soundEffectClass);
                WrappedPacket soundEffectWrapper = new WrappedPacket(new NMSPacket(soundEffect));
                Object minecraftKey = soundEffectWrapper.readObject(0, NMSUtils.minecraftKeyClass);
                return NMSUtils.getStringFromMinecraftKey(minecraftKey);
            } else {
                return readString(0);
            }
        } else {
            return soundEffectName;
        }
    }

    public void setSoundEffectName(String name) {
        if (packet != null) {
            if (soundEffectVarExists) {
                Object soundEffect = readObject(0, NMSUtils.soundEffectClass);
                WrappedPacket soundEffectWrapper = new WrappedPacket(new NMSPacket(soundEffect));
                Object minecraftKey = NMSUtils.generateMinecraftKey(name);
                soundEffectWrapper.write(NMSUtils.minecraftKeyClass, 0, minecraftKey);
            } else {
                writeString(0, name);
            }
        } else {
            soundEffectName = name;
        }
    }

    public Vector3d getEffectPosition() {
        if (packet != null) {
            double x = readInt(0) / 8.0D;
            double y = readInt(1) / 8.0D;
            double z = readInt(2) / 8.0D;
            return new Vector3d(x, y, z);
        } else {
            return effectPosition;
        }
    }

    public void setEffectPosition(Vector3d effectPosition) {
        if (packet != null) {
            writeInt(0, (int)(effectPosition.x  / 8.0D));
            writeInt(1, (int)(effectPosition.y  / 8.0D));
            writeInt(2, (int)(effectPosition.z  / 8.0D));
        }
        else {
            this.effectPosition = effectPosition;
        }
    }

    //Might be more than 1.0 on some older versions. 1 is 100%
    public float getVolume() {
        if (packet != null) {
            return readFloat(0);
        }
        else {
            return volume;
        }
    }

    public void setVolume(float volume) {
        if (packet != null) {
            writeFloat(0, volume);
        }
        else {
            this.volume = volume;
        }
    }

    public float getPitch() {
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_10)) {
                return readInt(3) / pitchMultiplier;
            } else {
                return readFloat(1);
            }
        }
        else {
            return pitch;
        }
    }

    public void setPitch(float pitch) {
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_10)) {
                writeInt(1, (int) (pitch * pitchMultiplier));
            } else {
                writeFloat(1, pitch);
            }
        }
        else {
            this.pitch = pitch;
        }
    }

    @Override
    public Object asNMSPacket() {
        return null;
    }
}
