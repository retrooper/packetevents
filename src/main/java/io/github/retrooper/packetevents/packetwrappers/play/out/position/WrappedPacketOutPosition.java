/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.packetwrappers.play.out.position;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public final class WrappedPacketOutPosition extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private static byte constructorMode = 0;
    private static Class<? extends Enum<?>> enumPlayerTeleportFlagsClass;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private Set<PlayerTeleportFlags> relativeFlags;
    private int teleportID;

    @Deprecated
    private boolean onGround;

    public WrappedPacketOutPosition(NMSPacket packet) {
        super(packet);
        relativeFlags = new HashSet<>();
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_7_10})
    @Deprecated
    public WrappedPacketOutPosition(double x, double y, double z, float yaw, float pitch, boolean onGround, Set<PlayerTeleportFlags> relativeFlags) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.relativeFlags = relativeFlags;
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_8, ServerVersion.v_1_8_8})
    public WrappedPacketOutPosition(double x, double y, double z, float yaw, float pitch, Set<PlayerTeleportFlags> relativeFlags) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.relativeFlags = relativeFlags;
    }

    public WrappedPacketOutPosition(double x, double y, double z, float yaw, float pitch, Set<PlayerTeleportFlags> relativeFlags, int teleportID) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.relativeFlags = relativeFlags;
        this.teleportID = teleportID;
    }

    @Override
    protected void load() {
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
                try {
                    packetConstructor = PacketTypeClasses.Play.Server.POSITION.getConstructor(double.class, double.class, double.class, float.class, float.class, Set.class, int.class);
                } catch (NoSuchMethodException e3) {
                    throw new IllegalStateException("Failed to locate a supported constructor of the PacketPlayOutPosition packet class.");
                }
            }
        }
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_7_10})
    @Deprecated
    public boolean isOnGround() throws UnsupportedOperationException {
        if (packet != null) {
            if (version.isNewerThan(ServerVersion.v_1_7_10)) {
                throwUnsupportedOperation();
            }
            return readBoolean(0);
        } else {
            return onGround;
        }
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_7_10})
    @Deprecated
    public void setOnGround(boolean onGround) {
        if (packet != null) {
            if (version.isNewerThan(ServerVersion.v_1_7_10)) {
                throwUnsupportedOperation();
            }
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

    public double getX() {
        if (packet != null) {
            return readDouble(0);
        } else {
            return x;
        }
    }

    public void setX(double x) {
        if (packet != null) {
            writeDouble(0, x);
        } else {
            this.x = x;
        }
    }

    public double getY() {
        if (packet != null) {
            return readDouble(1);
        } else {
            return y;
        }
    }

    public void setY(double y) {
        if (packet != null) {
            writeDouble(1, y);
        } else {
            this.y = y;
        }
    }

    public double getZ() {
        if (packet != null) {
            return readDouble(2);
        } else {
            return z;
        }
    }

    public void setZ(double z) {
        if (packet != null) {
            writeDouble(2, z);
        } else {
            this.z = z;
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

    @SupportedVersions(ranges = {ServerVersion.v_1_9, ServerVersion.ERROR})
    public int getTeleportId() throws UnsupportedOperationException {
        if (version.isOlderThan(ServerVersion.v_1_9)) {
            throwUnsupportedOperation();
        }
        if (packet != null) {
            return readInt(0);
        } else {
            return teleportID;
        }
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_9, ServerVersion.ERROR})
    public void setTeleportId(int teleportID) throws UnsupportedOperationException {
        if (packet != null) {
            writeInt(0, teleportID);
        } else {
            this.teleportID = teleportID;
        }
    }

    @Override
    public Object asNMSPacket() {

        Set<Object> nmsRelativeFlags = new HashSet<>();
        if (constructorMode != 0) {
            for (PlayerTeleportFlags flag : getRelativeFlags()) {
                nmsRelativeFlags.add(EnumUtil.valueOf(enumPlayerTeleportFlagsClass, flag.name()));
            }
        }
        try {
            switch (constructorMode) {
                case 0:
                    //1.7.10
                    return packetConstructor.newInstance(getX(), getY(), getZ(), getYaw(), getPitch(), isOnGround(), getRelativeFlagsMask());
                case 1:
                    //1.8 -> 1.8.8
                    return packetConstructor.newInstance(getX(), getY(), getZ(), getYaw(), getPitch(), nmsRelativeFlags);
                case 2:
                    return packetConstructor.newInstance(getX(), getY(), getZ(), getYaw(), getPitch(), nmsRelativeFlags, getTeleportId());
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
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
