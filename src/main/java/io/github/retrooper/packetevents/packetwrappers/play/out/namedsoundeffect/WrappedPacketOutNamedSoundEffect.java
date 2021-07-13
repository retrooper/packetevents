/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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
    private static boolean v_1_9, v_1_17;
    private static Constructor<?> packetConstructor, soundEffectConstructor;
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
        v_1_9 = version.isNewerThanOrEquals(ServerVersion.v_1_9);
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        soundEffectVarExists = NMSUtils.soundEffectClass != null;
        if (soundEffectVarExists) {
            enumSoundCategoryClass = NMSUtils.getNMSEnumClassWithoutException("SoundCategory");
            if (enumSoundCategoryClass == null) {
                enumSoundCategoryClass = NMSUtils.getNMEnumClassWithoutException("sounds.SoundCategory");
            }
            try {
                soundEffectConstructor = NMSUtils.soundEffectClass.getConstructor(NMSUtils.minecraftKeyClass);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        pitchMultiplier = version.isNewerThan(ServerVersion.v_1_9_4) ? 1 : version.isNewerThan(ServerVersion.v_1_8_8) ? 63.5F : 63.0F;
        try {
            if (v_1_17) {
                packetConstructor = PacketTypeClasses.Play.Server.NAMED_SOUND_EFFECT.getConstructor(NMSUtils.soundEffectClass,
                        enumSoundCategoryClass, double.class, double.class, double.class, float.class, float.class);
            }
            else {
                packetConstructor = PacketTypeClasses.Play.Server.NAMED_SOUND_EFFECT.getConstructor();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public String getSoundEffectName() {
        if (packet != null) {
            if (soundEffectVarExists) {
                Object soundEffect = readObject(0, NMSUtils.soundEffectClass);
                WrappedPacket soundEffectWrapper = new WrappedPacket(new NMSPacket(soundEffect));
                return soundEffectWrapper.readMinecraftKey(0);
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
                Object minecraftKey = NMSUtils.generateMinecraftKeyNew(name);
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
       if (v_1_9) {
           if (packet != null) {
               Enum<?> enumConst = readEnumConstant(0, enumSoundCategoryClass);
               return Optional.ofNullable(SoundCategory.values()[enumConst.ordinal()]);
           } else {
               return Optional.of(soundCategory);
           }
       }
       else {
           return Optional.empty();
       }
    }

    public void setSoundCategory(SoundCategory soundCategory) {
        if (!v_1_9) {
            return;
        }
        if (packet != null) {
            Enum<?> enumConst = EnumUtil.valueByIndex(enumSoundCategoryClass, soundCategory.ordinal());
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
            return readFloat(v_1_17 ? 1 : 0);
        } else {
            return volume;
        }
    }

    public void setVolume(float volume) {
        if (packet != null) {
            writeFloat(v_1_17 ? 1 : 0, volume);
        } else {
            this.volume = volume;
        }
    }

    public float getPitch() {
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_10)) {
                return readInt(3) / pitchMultiplier;
            } else {
                return readFloat(v_1_17 ? 2: 1);
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
                writeFloat(v_1_17 ? 2 : 1, pitch);
            }
        } else {
            this.pitch = pitch;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (v_1_17) {
            Object nmsSoundEffect = soundEffectConstructor.newInstance(NMSUtils.generateMinecraftKeyNew(getSoundEffectName()));
            Object nmsSoundCategory = EnumUtil.valueByIndex(enumSoundCategoryClass, getSoundCategory().get().ordinal());
            Vector3d effectPos = getEffectPosition();
            packetInstance = packetConstructor.newInstance(nmsSoundEffect, nmsSoundCategory, effectPos.x, effectPos.y, effectPos.z, getVolume(), getPitch());
        }
        else {
            packetInstance = packetConstructor.newInstance();
            WrappedPacketOutNamedSoundEffect wrappedPacketOutNamedSoundEffect = new WrappedPacketOutNamedSoundEffect(new NMSPacket(packetInstance));
            wrappedPacketOutNamedSoundEffect.setSoundEffectName(getSoundEffectName());
            if (soundEffectVarExists) {
                wrappedPacketOutNamedSoundEffect.setSoundCategory(getSoundCategory().get());
            }
            wrappedPacketOutNamedSoundEffect.setEffectPosition(getEffectPosition());
            wrappedPacketOutNamedSoundEffect.setPitch(getPitch());
            wrappedPacketOutNamedSoundEffect.setVolume(getVolume());
        }
        return packetInstance;
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
        VOICE
    }
}
