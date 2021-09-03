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

package io.github.retrooper.packetevents.utils;

import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.vector.Vector3i;

public final class PacketWrapperUtil {
    public static Vector3i readVectorFromLong(long val) {
        return new Vector3i((int) (val >> 38), (int) (val << 26 >> 52), (int) (val << 38 >> 38));
    }

    public static long generateLongFromVector(int x, int y, int z) {
        return ((long) x & 67108863L) << 38 | ((long) y & 4095L) << 26 | (long) z & 67108863L;
    }

    public static long generateLongFromVector(Vector3i vector) {
        return generateLongFromVector(vector.x, vector.y, vector.z);
    }

    public static int readVarInt(ByteBufAbstract byteBuf) {
        byte b0;
        int i = 0;
        int j = 0;
        do {
            b0 = byteBuf.readByte();
            i |= (b0 & Byte.MAX_VALUE) << j++ * 7;
            if (j > 5)
                throw new RuntimeException("VarInt too big");
        } while ((b0 & 128) == 128);
        return i;
    }
}
