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

package io.github.retrooper.packetevents.packetwrappers.play.out.entityeffect;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.util.Optional;

public class WrappedPacketOutEntityEffect extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static Constructor<?> packetDefaultConstructor;
    private int effectID = -1;
    private int amplifier = -1;
    private int duration = -1;
    private byte byteMask;
    private boolean byteMaskInitialized = false;

    public WrappedPacketOutEntityEffect(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutEntityEffect(Entity entity, int effectID, int amplifier, int duration) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.effectID = effectID;
        this.amplifier = amplifier;
        this.duration = duration;
        this.byteMaskInitialized = true;
    }

    public WrappedPacketOutEntityEffect(int entityID, int effectID, int amplifier, int duration, boolean hideParticles, boolean ambient, boolean showIcon) {
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

    @Override
    protected void load() {
        try {
            packetDefaultConstructor = PacketTypeClasses.Play.Server.ENTITY_EFFECT.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getEffectId() {
        if (effectID != -1) {
            return effectID;
        }
        if (packet != null) {
            return effectID = readByte(0);
        } else {
            return effectID;
        }
    }

    public void setEffectId(int effectID) {
        if (packet != null) {
            writeByte(0, (byte) (this.effectID = effectID));
        } else {
            this.effectID = effectID;
        }
    }

    public int getAmplifier() {
        if (amplifier != -1) {
            return amplifier;
        }
        if (packet != null) {
            return amplifier = readByte(1);
        } else {
            return amplifier;
        }
    }

    public void setAmplifier(int amplifier) {
        if (packet != null) {
            writeByte(1, (byte) (this.amplifier = amplifier));
        } else {
            this.amplifier = amplifier;
        }
    }

    public int getDuration() {
        if (duration != -1) {
            return duration;
        }
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                return duration = readShort(1);
            } else {
                return duration = readInt(1);
            }
        } else {
            return duration;
        }
    }

    public void setDuration(int duration) {
        if (packet != null) {
            this.duration = duration;
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                if (duration > Short.MAX_VALUE) {
                    duration = Short.MAX_VALUE;
                } else if (duration < Short.MIN_VALUE) {
                    duration = Short.MIN_VALUE;
                }
                writeShort(0, (short) duration);
            } else {
                writeInt(1, duration);
            }
        } else {
            this.duration = duration;
        }
    }

    private Optional<Byte> getByteMask() {
        if (version.isOlderThan(ServerVersion.v_1_8)) {
            return Optional.empty();
        }
        if (packet != null && !byteMaskInitialized) {
            return Optional.of(byteMask = readByte(2));
        } else {
            return Optional.of(byteMask);
        }
    }

    private void setByteMask(byte byteMask) {
        if (version.isNewerThan(ServerVersion.v_1_7_10)) {
            this.byteMask = byteMask;
            if (packet != null) {
                writeByte(2, byteMask);
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
        Object packetPlayOutEntityEffectInstance = packetDefaultConstructor.newInstance();
        WrappedPacketOutEntityEffect wrappedPacketOutEntityEffect =
                new WrappedPacketOutEntityEffect(new NMSPacket(packetPlayOutEntityEffectInstance));
        wrappedPacketOutEntityEffect.setEntityId(getEntityId());
        wrappedPacketOutEntityEffect.setEffectId(getEffectId());
        wrappedPacketOutEntityEffect.setAmplifier(getAmplifier());
        wrappedPacketOutEntityEffect.setDuration(getDuration());
        Optional<Byte> optionalByteMask = getByteMask();
        optionalByteMask.ifPresent(wrappedPacketOutEntityEffect::setByteMask);
        return packetPlayOutEntityEffectInstance;
    }
}
