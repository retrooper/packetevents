/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2016-2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.packetwrappers.play.out.namedsoundeffect;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

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

    public WrappedPacketOutNamedSoundEffect(String soundEffectName, @Nullable SoundCategory soundCategory, Vector3d effectPosition, float volume, float pitch) {
        this.soundEffectName = soundEffectName;
        this.soundCategory = soundCategory;
        this.effectPosition = effectPosition;
        this.volume = volume;
        this.pitch = pitch;
    }

    public WrappedPacketOutNamedSoundEffect(String soundEffectName, @Nullable SoundCategory soundCategory, double effectPositionX, double effectPositionY, double effectPositionZ, float volume, float pitch) {
        this.soundEffectName = soundEffectName;
        this.soundCategory = soundCategory;
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

    public Optional<SoundCategory> getSoundCategory() {
        if (version.isOlderThan(ServerVersion.v_1_9)) {
            return Optional.empty();
        }
        if (packet != null) {
            Enum<?> enumConst = readEnumConstant(0, enumSoundCategoryClass);
            return Optional.ofNullable(SoundCategory.getByName(enumConst.name()));
        } else {
            return Optional.of(soundCategory);
        }
    }

    public void setSoundCategory(SoundCategory soundCategory) {
        if (version.isOlderThan(ServerVersion.v_1_9)) {
            return;
        }
        if (packet != null) {
            Enum<?> enumConst = EnumUtil.valueOf(enumSoundCategoryClass, soundCategory.name());
            writeEnumConstant(0, enumConst);
        } else {
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
            writeInt(0, (int) (effectPosition.x / 8.0D));
            writeInt(1, (int) (effectPosition.y / 8.0D));
            writeInt(2, (int) (effectPosition.z / 8.0D));
        } else {
            this.effectPosition = effectPosition;
        }
    }

    //Might be more than 1.0 on some older versions. 1 is 100%
    public float getVolume() {
        if (packet != null) {
            return readFloat(0);
        } else {
            return volume;
        }
    }

    public void setVolume(float volume) {
        if (packet != null) {
            writeFloat(0, volume);
        } else {
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
        } else {
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
        } else {
            this.pitch = pitch;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object packetPlayOutNamedSoundEffectInstance = packetDefaultConstructor.newInstance();
        WrappedPacketOutNamedSoundEffect wrappedPacketOutNamedSoundEffect = new WrappedPacketOutNamedSoundEffect(new NMSPacket(packetPlayOutNamedSoundEffectInstance));
        wrappedPacketOutNamedSoundEffect.setSoundEffectName(getSoundEffectName());
        if (soundEffectVarExists) {
            wrappedPacketOutNamedSoundEffect.setSoundCategory(getSoundCategory().get());
        }
        wrappedPacketOutNamedSoundEffect.setEffectPosition(getEffectPosition());
        wrappedPacketOutNamedSoundEffect.setPitch(getPitch());
        wrappedPacketOutNamedSoundEffect.setVolume(getVolume());
        return packetPlayOutNamedSoundEffectInstance;
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
