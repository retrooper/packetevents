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

package io.github.retrooper.packetevents.packetwrappers.play.out.position;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3d;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class WrappedPacketOutPosition extends WrappedPacket implements SendableWrapper {
    private static boolean v_1_8, v_1_17;
    private static Constructor<?> packetConstructor;
    private static byte constructorMode = 0;
    private static Class<? extends Enum<?>> enumPlayerTeleportFlagsClass;
    private Vector3d position;
    private float yaw;
    private float pitch;
    private Set<PlayerTeleportFlags> relativeFlags;
    private int teleportID;

    private boolean onGround;

    public WrappedPacketOutPosition(NMSPacket packet) {
        super(packet);
        relativeFlags = new HashSet<>();
    }

    @Deprecated
    public WrappedPacketOutPosition(double x, double y, double z, float yaw, float pitch, boolean onGround, Set<PlayerTeleportFlags> relativeFlags) {
        this.position = new Vector3d(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.relativeFlags = relativeFlags;
    }

    @Deprecated
    public WrappedPacketOutPosition(double x, double y, double z, float yaw, float pitch, Set<PlayerTeleportFlags> relativeFlags) {
        this.position = new Vector3d(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
        this.relativeFlags = relativeFlags;
    }

    public WrappedPacketOutPosition(double x, double y, double z, float yaw, float pitch, Set<PlayerTeleportFlags> relativeFlags, int teleportID) {
        this.position = new Vector3d(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
        this.relativeFlags = relativeFlags;
        this.teleportID = teleportID;
    }

    public WrappedPacketOutPosition(Vector3d position, float yaw, float pitch, Set<PlayerTeleportFlags> relativeFlags, int teleportID) {
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.relativeFlags = relativeFlags;
        this.teleportID = teleportID;
    }

    public WrappedPacketOutPosition(Vector3d position, float yaw, float pitch, Set<PlayerTeleportFlags> relativeFlags, int teleportID, boolean onGround) {
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.relativeFlags = relativeFlags;
        this.teleportID = teleportID;
        this.onGround= onGround;
    }

    @Override
    protected void load() {
        v_1_8 = version.isNewerThanOrEquals(ServerVersion.v_1_8);
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        enumPlayerTeleportFlagsClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Server.POSITION, "EnumPlayerTeleportFlags");
        try {
            //1.7.10
            packetConstructor = PacketTypeClasses.Play.Server.POSITION.getConstructor(double.class, double.class, double.class, float.class, float.class, boolean.class, byte.class);
        } catch (NoSuchMethodException e) {
            constructorMode = 1;
            try {
                //1.8 -> 1.8.8
                packetConstructor = PacketTypeClasses.Play.Server.POSITION.getConstructor(double.class, double.class, double.class, float.class, float.class, Set.class);
            } catch (NoSuchMethodException e2) {
                constructorMode = 2;
                //1.9 -> 1.16.5
                try {
                    packetConstructor = PacketTypeClasses.Play.Server.POSITION.getConstructor(double.class, double.class, double.class, float.class, float.class, Set.class, int.class);
                } catch (NoSuchMethodException e3) {
                    constructorMode = 3;
                    //1.17
                    try {
                        packetConstructor = PacketTypeClasses.Play.Server.POSITION.getConstructor(double.class, double.class, double.class, float.class, float.class, Set.class, int.class, boolean.class);
                    } catch (NoSuchMethodException e4) {
                        throw new IllegalStateException("Failed to locate a supported constructor of the PacketPlayOutPosition packet class.");
                    }
                }
            }
        }
    }

    public Optional<Boolean> isOnGround() {
        //1.7.10 and 1.17+ support this field
        if (v_1_8 && !v_1_17) {
            return Optional.empty();
        }
        if (packet != null) {
            return Optional.of(readBoolean(0));
        } else {
            return Optional.of(onGround);
        }
    }

    public void setOnGround(boolean onGround) {
        if (v_1_8 && !v_1_17) {
            return;
        }
        if (packet != null) {
            writeBoolean(0, onGround);
        } else {
            this.onGround = onGround;
        }
    }

    public byte getRelativeFlagsMask() {
        byte relativeMask = 0;
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                relativeMask = readByte(0);
            } else {
                Set<PlayerTeleportFlags> flags = getRelativeFlags();
                for (PlayerTeleportFlags flag : flags) {
                    relativeMask |= flag.maskFlag;
                }
            }

        } else {
            Set<PlayerTeleportFlags> flags = getRelativeFlags();
            for (PlayerTeleportFlags flag : flags) {
                relativeMask |= flag.maskFlag;
            }
        }

        return relativeMask;
    }

    public void setRelativeFlagsMask(byte mask) {
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                writeByte(0, mask);
            } else {
                Set<Enum<?>> nmsRelativeFlags = new HashSet<>();
                for (PlayerTeleportFlags flag : PlayerTeleportFlags.values()) {
                    if ((mask & flag.maskFlag) == flag.maskFlag) {
                        nmsRelativeFlags.add(EnumUtil.valueOf(enumPlayerTeleportFlagsClass, flag.name()));
                    }
                }
                write(Set.class, 0, nmsRelativeFlags);
            }
        } else {
            relativeFlags.clear();
            for (PlayerTeleportFlags flag : PlayerTeleportFlags.values()) {
                if ((mask & flag.maskFlag) == flag.maskFlag) {
                    relativeFlags.add(flag);
                }
            }
        }
    }

    public Set<PlayerTeleportFlags> getRelativeFlags() {
        if (packet != null) {
            Set<PlayerTeleportFlags> relativeFlags = new HashSet<>();
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                byte relativeBitMask = readByte(0);
                for (PlayerTeleportFlags flag : PlayerTeleportFlags.values()) {
                    if ((relativeBitMask & flag.maskFlag) == flag.maskFlag) {
                        relativeFlags.add(flag);
                    }
                }
            } else {
                Set<Enum<?>> set = readObject(0, Set.class);
                for (Enum<?> e : set) {
                    relativeFlags.add(PlayerTeleportFlags.valueOf(e.name()));
                }
            }
            return relativeFlags;
        } else {
            return relativeFlags;
        }
    }

    public void setRelativeFlags(Set<PlayerTeleportFlags> flags) {
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                byte relativeBitMask = 0;
                for (PlayerTeleportFlags flag : flags) {
                    relativeBitMask |= flag.maskFlag;
                }
                writeByte(0, relativeBitMask);
            } else {
                Set<Enum<?>> nmsRelativeFlags = new HashSet<>();
                for (PlayerTeleportFlags flag : flags) {
                    nmsRelativeFlags.add(EnumUtil.valueOf(enumPlayerTeleportFlagsClass, flag.name()));
                }
                write(Set.class, 0, nmsRelativeFlags);
            }
        } else {
            this.relativeFlags = flags;
        }
    }

    public Vector3d getPosition() {
        if (packet != null) {
            double x = readDouble(0);
            double y = readDouble(1);
            double z = readDouble(2);
            return new Vector3d(x, y, z);
        } else {
            return position;
        }
    }

    public void setPosition(Vector3d position) {
        if (packet != null) {
            writeDouble(0, position.x);
            writeDouble(1, position.y);
            writeDouble(2, position.z);
        } else {
            this.position = position;
        }
    }

    public float getYaw() {
        if (packet != null) {
            return readFloat(0);
        } else {
            return yaw;
        }
    }

    public void setYaw(float yaw) {
        if (packet != null) {
            writeFloat(0, yaw);
        } else {
            this.yaw = yaw;
        }
    }

    public float getPitch() {
        if (packet != null) {
            return readFloat(1);
        } else {
            return pitch;
        }
    }

    public void setPitch(float pitch) {
        if (packet != null) {
            writeFloat(1, pitch);
        } else {
            this.pitch = pitch;
        }
    }

    public Optional<Integer> getTeleportId() {
        if (version.isOlderThan(ServerVersion.v_1_9)) {
            return Optional.empty();
        }
        if (packet != null) {
            return Optional.of(readInt(0));
        } else {
            return Optional.of(teleportID);
        }
    }

    public void setTeleportId(int teleportID) {
        if (version.isOlderThan(ServerVersion.v_1_9)) {
            return;
        }
        if (packet != null) {
            writeInt(0, teleportID);
        } else {
            this.teleportID = teleportID;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Set<Object> nmsRelativeFlags = new HashSet<>();
        if (constructorMode != 0) {
            for (PlayerTeleportFlags flag : getRelativeFlags()) {
                nmsRelativeFlags.add(EnumUtil.valueOf(enumPlayerTeleportFlagsClass, flag.name()));
            }
        }
        Vector3d position = getPosition();
        switch (constructorMode) {
            case 0:
                //1.7.10
                return packetConstructor.newInstance(position.x, position.y, position.z, getYaw(), getPitch(), isOnGround(), getRelativeFlagsMask());
            case 1:
                //1.8 -> 1.8.8
                return packetConstructor.newInstance(position.x, position.y, position.z, getYaw(), getPitch(), nmsRelativeFlags);
            case 2:
                //1.9 -> 1.16.5
                return packetConstructor.newInstance(position.x, position.y, position.z, getYaw(), getPitch(), nmsRelativeFlags, getTeleportId().get());
            case 3:
                //1.17
                return packetConstructor.newInstance(position.x, position.y, position.z, getYaw(), getPitch(), nmsRelativeFlags, getTeleportId(), isOnGround().get());
            default:
                return null;
        }
    }

    public enum PlayerTeleportFlags {
        X(0x01),
        Y(0x02),
        Z(0x04),
        Y_ROT(0x08),
        X_ROT(0x10);

        final byte maskFlag;

        PlayerTeleportFlags(int maskFlag) {
            this.maskFlag = (byte) maskFlag;
        }
    }
}
