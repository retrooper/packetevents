/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.util;

import com.github.retrooper.packetevents.util.MathUtil;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Less/low precision encoding/decoding for {@link Vector3d},
 * introduced in Minecraft 1.21.9.
 */
@NullMarked
@ApiStatus.Internal
public final class LpVector3d {

    private static final double ABS_MAX_VALUE = Double.longBitsToDouble(0x420FFFFFFFF80000L);
    private static final double ABS_MIN_VALUE = Double.longBitsToDouble(0x3F00004001000400L);

    private LpVector3d() {
    }

    public static Vector3d read(PacketWrapper<?> wrapper) {
        int b0 = wrapper.readUnsignedByte();
        if (b0 == 0) {
            return Vector3d.zero();
        }
        long bits = b0 | ((long) wrapper.readUnsignedByte() << 8) | (wrapper.readUnsignedInt() << 16);
        long max = b0 & 0b11L;
        if ((b0 & 0b100L) != 0) {
            // continuation bit is set, read more
            max |= ((long) wrapper.readVarInt() & 0xFFFFFFFFL) << 2;
        }
        return new Vector3d(
                unpack(bits >> 3) * (double) max,
                unpack(bits >> 18) * (double) max,
                unpack(bits >> 33) * (double) max
        );
    }

    public static void write(PacketWrapper<?> wrapper, Vector3d vector) {
        double x = sanitize(vector.x);
        double y = sanitize(vector.y);
        double z = sanitize(vector.z);
        double max = MathUtil.absMax(x, MathUtil.absMax(y, z));
        if (max < ABS_MIN_VALUE) {
            wrapper.writeByte(0);
            return;
        }
        long maxLong = MathUtil.ceilLong(max);
        boolean large = (maxLong & 0b11L) != maxLong;
        long mul = large ? ((maxLong & 0b11L) | 0b100L) : maxLong;
        long packedX = pack(x / (double) maxLong) << 3;
        long packedY = pack(y / (double) maxLong) << 18;
        long packedZ = pack(z / (double) maxLong) << 33;
        long bits = mul | packedX | packedY | packedZ;
        wrapper.writeShortLE((short) bits);
        wrapper.writeInt((int) (bits >> 16));
        if (large) {
            wrapper.writeVarInt((int) (maxLong >> 2));
        }
    }

    private static double sanitize(double comp) {
        if (!Double.isNaN(comp)) {
            return MathUtil.clamp(comp, -ABS_MAX_VALUE, ABS_MAX_VALUE);
        }
        return 0d;
    }

    private static long pack(double comp) {
        return Math.round((comp * 0.5d + 0.5d) * (double) ((1 << 15) - 2));
    }

    private static double unpack(long bits) {
        return Math.min((double) (bits & ((1L << 15) - 1)), (double) ((1 << 15) - 2)) * 2d / (double) ((1 << 15) - 2) - 1d;
    }
}
