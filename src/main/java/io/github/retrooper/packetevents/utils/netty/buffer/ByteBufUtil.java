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

package io.github.retrooper.packetevents.utils.netty.buffer;

import io.github.retrooper.packetevents.utils.nms.MinecraftReflection;

public final class ByteBufUtil {
    public static ByteBufAbstract wrappedBuffer(byte[] bytes) {
        if (!MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            return ByteBufUtilLegacy.wrappedBuffer(bytes);
        } else {
            return ByteBufUtilModern.wrappedBuffer(bytes);
        }
    }

    public static ByteBufAbstract copiedBuffer(byte[] bytes) {
        if (!MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            return ByteBufUtilLegacy.copiedBuffer(bytes);
        } else {
            return ByteBufUtilModern.copiedBuffer(bytes);
        }
    }

    public static ByteBufAbstract buffer() {
        if (!MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            return ByteBufUtilLegacy.buffer();
        } else {
            return ByteBufUtilModern.buffer();
        }
    }

    public static ByteBufAbstract buffer(int initialCapacity) {
        if (!MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            return ByteBufUtilLegacy.buffer(initialCapacity);
        } else {
            return ByteBufUtilModern.buffer(initialCapacity);
        }
    }

    public static ByteBufAbstract buffer(int initialCapacity, int maxCapacity) {
        if (!MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            return ByteBufUtilLegacy.buffer(initialCapacity, maxCapacity);
        } else {
            return ByteBufUtilModern.buffer(initialCapacity, maxCapacity);
        }
    }

    public static ByteBufAbstract directBuffer() {
        if (!MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            return ByteBufUtilLegacy.directBuffer();
        } else {
            return ByteBufUtilModern.directBuffer();
        }
    }

    public static ByteBufAbstract directBuffer(int initialCapacity) {
        if (!MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            return ByteBufUtilLegacy.directBuffer(initialCapacity);
        } else {
            return ByteBufUtilModern.directBuffer(initialCapacity);
        }
    }

    public static ByteBufAbstract directBuffer(int initialCapacity, int maxCapacity) {
        if (!MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            return ByteBufUtilLegacy.directBuffer(initialCapacity, maxCapacity);
        } else {
            return ByteBufUtilModern.directBuffer(initialCapacity, maxCapacity);
        }
    }

    public static ByteBufAbstract compositeBuffer() {
        if (!MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            return ByteBufUtilLegacy.compositeBuffer();
        } else {
            return ByteBufUtilModern.compositeBuffer();
        }
    }

    public static ByteBufAbstract compositeBuffer(int maxNumComponents) {
        if (!MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            return ByteBufUtilLegacy.compositeBuffer(maxNumComponents);
        } else {
            return ByteBufUtilModern.compositeBuffer(maxNumComponents);
        }
    }
}
