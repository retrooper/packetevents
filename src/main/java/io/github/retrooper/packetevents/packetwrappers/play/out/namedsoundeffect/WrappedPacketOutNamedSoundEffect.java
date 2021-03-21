package io.github.retrooper.packetevents.packetwrappers.play.out.namedsoundeffect;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//TODO Test sending
public class WrappedPacketOutNamedSoundEffect extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetDefaultConstructor, soundEffectConstructor;
    private static Class<? extends Enum<?>> enumSoundCategoryClass;
    private static boolean soundEffectVarExists;
    private static float pitchMultiplier = 63.0F;
    private String soundEffectName;
    private SoundCategory soundCategory;
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
        soundEffectVarExists = NMSUtils.soundEffectClass != null;
        if (soundEffectVarExists) {
            enumSoundCategoryClass = NMSUtils.getNMSEnumClassWithoutException("SoundCategory");
            try {
                soundEffectConstructor = NMSUtils.soundEffectClass.getConstructor(NMSUtils.minecraftKeyClass);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        pitchMultiplier = version.isNewerThan(ServerVersion.v_1_9_4) ? 1 : version.isNewerThan(ServerVersion.v_1_8_8) ? 63.5F : 63.0F;
        try {
            packetDefaultConstructor = PacketTypeClasses.Play.Server.NAMED_SOUND_EFFECT.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
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
                Object minecraftKey = NMSUtils.generateMinecraftKey(name);
                Object soundEffect = null;
                try {
                    soundEffect = soundEffectConstructor.newInstance(minecraftKey);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                write(NMSUtils.soundEffectClass, 0, soundEffect);
            } else {
                writeString(0, name);
            }
        } else {
            soundEffectName = name;
        }
    }

    //1.9 -> latest
    @SupportedVersions(ranges = {ServerVersion.v_1_9, ServerVersion.ERROR})
    public SoundCategory getSoundCategory() throws UnsupportedOperationException {
        if (version.isOlderThan(ServerVersion.v_1_9)) {
            throwUnsupportedOperation();
        }
        if (packet != null) {
            Enum<?> enumConst = readEnumConstant(0, enumSoundCategoryClass);
            return SoundCategory.getByName(enumConst.name());
        }
        else {
            return soundCategory;
        }
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_9, ServerVersion.ERROR})
    public void setSoundCategory(SoundCategory soundCategory) {
        if (packet != null) {
            Enum<?> enumConst = EnumUtil.valueOf(enumSoundCategoryClass, soundCategory.name());
            writeEnumConstant(0, enumConst);
        }
        else {
            this.soundCategory = soundCategory;
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
        try {
            Object packetPlayOutNamedSoundEffectInstance = packetDefaultConstructor.newInstance();
            WrappedPacketOutNamedSoundEffect wrappedPacketOutNamedSoundEffect = new WrappedPacketOutNamedSoundEffect(new NMSPacket(packetPlayOutNamedSoundEffectInstance));
            wrappedPacketOutNamedSoundEffect.setSoundEffectName(getSoundEffectName());
            if (soundEffectVarExists) {
                wrappedPacketOutNamedSoundEffect.setSoundCategory(getSoundCategory());
            }
            wrappedPacketOutNamedSoundEffect.setEffectPosition(getEffectPosition());
            wrappedPacketOutNamedSoundEffect.setPitch(getPitch());
            wrappedPacketOutNamedSoundEffect.setVolume(getVolume());
            return packetPlayOutNamedSoundEffectInstance;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum SoundCategory {
        MASTER,
        MUSIC,
        RECORDS,
        WEATHER,
        BLOCKS,
        HOSTILE,
        NEUTRAL,
        PLAYERS,
        AMBIENT,
        VOICE;
        @Nullable
        public static SoundCategory getByName(String name) {
            for (SoundCategory value : values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return value;
                }
            }
            return null;
        }
    }
}
