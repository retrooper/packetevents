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

package io.github.retrooper.packetevents.packetwrappers.play.out.entityeffect;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Constructor;
import java.util.Optional;

public class WrappedPacketOutEntityEffect extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static boolean v_1_7_10, v_1_17, v_1_18_2, v_1_19;
    private static Constructor<?> packetConstructor;
    private int effectID;
    private int amplifier;
    private int duration;
    private byte byteMask;
    private boolean byteMaskInitialized = false;

    public WrappedPacketOutEntityEffect(NMSPacket packet) {
        super(packet, v_1_17 ? 3 : 0);
    }

    public WrappedPacketOutEntityEffect(Entity entity, int effectID, int amplifier, int duration) {
        super(v_1_17 ? 3 : 0);
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.effectID = effectID;
        this.amplifier = amplifier;
        this.duration = duration;
        this.byteMaskInitialized = true;
    }

    public WrappedPacketOutEntityEffect(int entityID, int effectID, int amplifier, int duration, boolean hideParticles, boolean ambient, boolean showIcon) {
        super(v_1_17 ? 3 : 0);
        this.entityID = entityID;
        this.effectID = effectID;
        this.amplifier = amplifier;
        this.duration = duration;
        this.byteMaskInitialized = true;
        this.byteMask = 0;
        if (version.isOlderThan(ServerVersion.v_1_9)) {
            //hideParticles field is used as a boolean
            byteMask = hideParticles ? (byte) 1 : (byte) 0;
        } else {
            if (hideParticles) {
                byteMask |= 2;
            }
            if (version.isNewerThan(ServerVersion.v_1_8_8)) {
                if (ambient) {
                    byteMask |= 1;
                }

                if (version.isNewerThan(ServerVersion.v_1_12_2)) {
                    if (showIcon) {
                        byteMask |= 4;
                    }
                }
            }
        }
    }

    public WrappedPacketOutEntityEffect(Entity entity, PotionEffectType effectType, int amplifier, int duration) {
        this(entity, effectType.getId(), amplifier, duration);
    }

    public WrappedPacketOutEntityEffect(int entityID, PotionEffectType effectType, int amplifier, int duration, boolean hideParticles, boolean ambient, boolean showIcon) {
        this(entityID, effectType.getId(), amplifier, duration, hideParticles, ambient, showIcon);
    }

    public WrappedPacketOutEntityEffect(Entity entity, Effect effect, int amplifier, int duration) {
        this(entity, effect.getId(), amplifier, duration);
    }

    public WrappedPacketOutEntityEffect(int entityID, Effect effect, int amplifier, int duration, boolean hideParticles, boolean ambient, boolean showIcon) {
        this(entityID, effect.getId(), amplifier, duration, hideParticles, ambient, showIcon);
    }

    @Override
    protected void load() {
        v_1_7_10 = version.isOlderThan(ServerVersion.v_1_8);
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        v_1_18_2 = version.isNewerThanOrEquals(ServerVersion.v_1_18_2);
        v_1_19 = version.isNewerThanOrEquals(ServerVersion.v_1_19);
        try {
            if (v_1_17) {
                packetConstructor = PacketTypeClasses.Play.Server.ENTITY_EFFECT.getConstructor(NMSUtils.packetDataSerializerClass);
            } else {
                packetConstructor = PacketTypeClasses.Play.Server.ENTITY_EFFECT.getConstructor();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getEffectId() {
        if (packet != null) {
            if (v_1_19) {
                Object mobEffectList = readObject(0, NMSUtils.mobEffectListClass);
                return NMSUtils.getEffectId(mobEffectList);
            } else if (v_1_18_2) {
                return readInt(4);
            } else {
                return readByte(0);
            }
        } else {
            return effectID;
        }
    }

    public void setEffectId(int effectID) {
        if (packet != null) {
            if (v_1_19) {
                Object mobEffectList = NMSUtils.getMobEffectListById(effectID);
                write(NMSUtils.mobEffectListClass, 0, mobEffectList);
            } else if (v_1_18_2) {
                writeInt(4, effectID);
            } else {
                writeByte(0, (byte) effectID);
            }
        } else {
            this.effectID = effectID;
        }
    }

    public int getAmplifier() {
        if (packet != null) {
            return readByte(v_1_18_2 ? 0 : 1);
        } else {
            return amplifier;
        }
    }

    public void setAmplifier(int amplifier) {
        if (packet != null) {
            writeByte(v_1_18_2 ? 0 : 1, (byte) amplifier);
        } else {
            this.amplifier = amplifier;
        }
    }

    public int getDuration() {
        if (packet != null) {
            //1.7 and only 1.7(below 1.8)
            if (v_1_7_10) {
                return readShort(1);
            }
            else if (v_1_19) {
                return readInt(4);
            }
            //1.18.2+
            else if (v_1_18_2) {
                return readInt(5);
            }
            //1.17 - 1.8
            else {
                return readInt(v_1_17 ? 4 : 1);
            }
        } else {
            return duration;
        }
    }

    public void setDuration(int duration) {
        if (packet != null) {
            this.duration = duration;
            if (v_1_7_10) {
                if (duration > Short.MAX_VALUE) {
                    duration = Short.MAX_VALUE;
                } else if (duration < Short.MIN_VALUE) {
                    duration = Short.MIN_VALUE;
                }
                writeShort(0, (short) duration);
            } else if (v_1_19) {
                writeInt(4, duration);
            } else if (v_1_18_2) {
                writeInt(5, duration);
            } else {
                writeInt(v_1_17 ? 4 : 1, duration);
            }
        } else {
            this.duration = duration;
        }
    }

    private Optional<Byte> getByteMask() {
        if (v_1_7_10) {
            return Optional.empty();
        }
        if (packet != null && !byteMaskInitialized) {
            return Optional.of(byteMask = readByte(v_1_18_2 ? 1 : 2));
        } else {
            return Optional.of(byteMask);
        }
    }

    private void setByteMask(byte byteMask) {
        if (!v_1_7_10) {
            this.byteMask = byteMask;
            if (packet != null) {
                writeByte(v_1_18_2 ? 1 : 2, byteMask);
            }
        }
    }

    public Optional<Boolean> shouldHideParticles() {
        Optional<Byte> byteMaskOptional = getByteMask();
        if (!byteMaskOptional.isPresent()) {
            return Optional.empty();
        }
        byte byteMask = byteMaskOptional.get();
        if (version.isOlderThan(ServerVersion.v_1_9)) {
            //hideParticles field is used as a boolean
            return Optional.of(byteMask == 1);
        } else {
            return Optional.of((byteMask & 2) == 2);
        }
    }

    public void setShouldHideParticles(boolean hideParticles) {
        if (version.isNewerThan(ServerVersion.v_1_7_10)) {
            Optional<Byte> byteMaskOptional = getByteMask();
            if (byteMaskOptional.isPresent()) {
                byte byteMask = byteMaskOptional.get();
                boolean currentHideParticles = shouldHideParticles().get();
                if (hideParticles) {
                    byteMask |= 2;
                } else {
                    if (currentHideParticles) {
                        byteMask -= 2;
                    }
                }
                setByteMask(byteMask);
            }
        }
    }

    public Optional<Boolean> isAmbient() {
        if (version.isOlderThan(ServerVersion.v_1_9)) {
            return Optional.empty();
        }
        Optional<Byte> byteMaskOptional = getByteMask();
        if (!byteMaskOptional.isPresent()) {
            return Optional.empty();
        }
        byte byteMask = byteMaskOptional.get();
        return Optional.of((byteMask & 1) == 1);
    }

    public void setIsAmbient(boolean ambient) {
        if (version.isNewerThan(ServerVersion.v_1_8_8)) {
            Optional<Byte> byteMaskOptional = getByteMask();
            if (byteMaskOptional.isPresent()) {
                byte byteMask = byteMaskOptional.get();
                boolean currentAmbient = isAmbient().get();
                if (ambient) {
                    byteMask |= 1;
                } else {
                    if (currentAmbient) {
                        byteMask -= 1;
                    }
                }
                setByteMask(byteMask);
            }
        }
    }

    public Optional<Boolean> shouldShowIcon() {
        if (version.isOlderThan(ServerVersion.v_1_13)) {
            return Optional.empty();
        }
        Optional<Byte> byteMaskOptional = getByteMask();
        if (!byteMaskOptional.isPresent()) {
            return Optional.empty();
        }
        byte byteMask = byteMaskOptional.get();
        return Optional.of((byteMask & 4) == 4);
    }

    public void setShowIcon(boolean showIcon) {
        if (version.isNewerThan(ServerVersion.v_1_12_2)) {
            Optional<Byte> byteMaskOptional = getByteMask();
            if (byteMaskOptional.isPresent()) {
                byte byteMask = byteMaskOptional.get();
                boolean currentShowIcon = shouldShowIcon().get();
                if (showIcon) {
                    byteMask |= 4;
                } else {
                    if (currentShowIcon) {
                        byteMask -= 4;
                    }
                }
                setByteMask(byteMask);
            }
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (v_1_17) {
            //Lazy and stupid way
            //was size 12
            byte[] buffer = new byte[30];
            Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(PacketEvents.get().getByteBufUtil().newByteBuf(buffer));
            packetInstance = packetConstructor.newInstance(packetDataSerializer);
        } else {
            packetInstance = packetConstructor.newInstance();

        }
        WrappedPacketOutEntityEffect wrappedPacketOutEntityEffect =
                new WrappedPacketOutEntityEffect(new NMSPacket(packetInstance));
        wrappedPacketOutEntityEffect.setEntityId(getEntityId());
        wrappedPacketOutEntityEffect.setEffectId(getEffectId());
        wrappedPacketOutEntityEffect.setAmplifier(getAmplifier());
        wrappedPacketOutEntityEffect.setDuration(getDuration());
        Optional<Byte> optionalByteMask = getByteMask();
        optionalByteMask.ifPresent(wrappedPacketOutEntityEffect::setByteMask);
        return packetInstance;
    }
}
