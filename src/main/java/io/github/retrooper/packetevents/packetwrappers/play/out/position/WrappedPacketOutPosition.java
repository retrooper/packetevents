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

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.util.HashSet;
import java.util.Set;
//TODO support sending
public final class WrappedPacketOutPosition extends WrappedPacket {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private final Set<PlayerTeleportFlags> relativeFlags = new HashSet<>();

    public WrappedPacketOutPosition(NMSPacket packet) {
        super(packet);
    }

    protected Set<PlayerTeleportFlags> getRelativeFlags() {
        if (relativeFlags.isEmpty()) {
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                byte relativeBitMask = readByte(0);

                if ((relativeBitMask & 0x01) != 0) {
                    relativeFlags.add(PlayerTeleportFlags.X);
                }

                if ((relativeBitMask & 0x02) != 0) {
                    relativeFlags.add(PlayerTeleportFlags.Y);
                }

                if ((relativeBitMask & 0x04) != 0) {
                    relativeFlags.add(PlayerTeleportFlags.Z);
                }

                if ((relativeBitMask & 0x08) != 0) {
                    relativeFlags.add(PlayerTeleportFlags.Y_ROT);
                }

                if ((relativeBitMask & 0x10) != 0) {
                    relativeFlags.add(PlayerTeleportFlags.X_ROT);
                }
            } else {
                Set<Enum> set = (Set<Enum>) readObject(0, Set.class);
                for (Enum e : set) {
                    relativeFlags.add(PlayerTeleportFlags.valueOf(e.name()));
                }
            }
        }
        return relativeFlags;
    }

    /**
     * Get the X position.
     *
     * @return Get X Position
     */
    public double getX() {
        if (packet != null) {
            return readDouble(0);
        } else {
            return x;
        }
    }

    /**
     * Get the Y position.
     *
     * @return Get Y Position
     */
    public double getY() {
        if (packet != null) {
            return readDouble(1);
        } else {
            return y;
        }
    }

    /**
     * Get the Z position.
     *
     * @return Get Z Position
     */
    public double getZ() {
        if (packet != null) {
            return readDouble(2);
        } else {
            return z;
        }
    }

    public boolean isRelativeX() {
        return getRelativeFlags().contains(PlayerTeleportFlags.X);
    }

    public boolean isRelativeY() {
        return getRelativeFlags().contains(PlayerTeleportFlags.Y);
    }

    public boolean isRelativeZ() {
        return getRelativeFlags().contains(PlayerTeleportFlags.Z);
    }

    public boolean isRelativeYaw() {
        return getRelativeFlags().contains(PlayerTeleportFlags.X_ROT);
    }

    public boolean isRelativePitch() {
        return getRelativeFlags().contains(PlayerTeleportFlags.Y_ROT);
    }

    /**
     * Get the Yaw.
     *
     * @return Get Yaw
     */
    public float getYaw() {
        if (packet != null) {
            return readFloat(0);
        } else {
            return yaw;
        }
    }

    /**
     * Get the Pitch,
     *
     * @return Get Pitch
     */
    public float getPitch() {
        if (packet != null) {
            return readFloat(1);
        } else {
            return pitch;
        }
    }

    public enum PlayerTeleportFlags {
        X,
        Y,
        Z,
        Y_ROT,
        X_ROT;

        byte flagValue;

        PlayerTeleportFlags() {
            flagValue = (byte) ordinal();
        }

        PlayerTeleportFlags(byte flagValue) {
            this.flagValue = flagValue;
        }

        public byte getFlagValue() {
            return flagValue;
        }
    }
}
