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

package io.github.retrooper.packetevents.utils.netty;

import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;

public class RawNettyUtil {
    public static int readVarInt(Object buffer) {
        int value = 0;
        int length = 0;
        byte currentByte;
        do {
            currentByte = readByte(buffer);
            value |= (currentByte & 0x7F) << (length * 7);
            length++;
            if (length > 5) {
                throw new RuntimeException("VarInt is too large. Must be smaller than 5 bytes.");
            }
        } while ((currentByte & 0x80) == 0x80);
        return value;
    }

    public static void writeVarInt(Object buffer, int value) {
        while (true) {
            if ((value & ~0x7F) == 0) {
                writeByte(buffer, value);
                return;
            }
            writeByte(buffer, (value & 0x7F) | 0x80);
            value >>>= 7;
        }
    }

    public static byte readByte(Object buffer) {
        if (SpigotReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
            return RawNettyUtilModern.readByte(buffer);
        } else {
            return RawNettyUtilLegacy.readByte(buffer);
        }
    }

    public static void writeByte(Object buffer, int value) {
        if (SpigotReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
            RawNettyUtilModern.writeByte(buffer, value);
        }
        else {
            RawNettyUtilLegacy.writeByte(buffer, value);
        }
    }
}
