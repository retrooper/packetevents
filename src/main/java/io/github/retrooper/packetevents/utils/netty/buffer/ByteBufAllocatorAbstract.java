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

import io.github.retrooper.packetevents.utils.MinecraftReflectionUtil;

public interface ByteBufAllocatorAbstract {
    static ByteBufAllocatorAbstract generate(Object rawByteBufAllocator) {
        if (MinecraftReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
            return new ByteBufAllocatorModern(rawByteBufAllocator);
        } else {
            return new ByteBufAllocatorLegacy(rawByteBufAllocator);
        }
    }

    Object rawByteBufAllocator();

    ByteBufAbstract buffer();

    ByteBufAbstract buffer(int initialCapacity);

    ByteBufAbstract buffer(int initialCapacity, int maxCapacity);

    ByteBufAbstract ioBuffer();

    ByteBufAbstract ioBuffer(int initialCapacity);

    ByteBufAbstract ioBuffer(int initialCapacity, int maxCapacity);

    ByteBufAbstract heapBuffer();

    ByteBufAbstract heapBuffer(int initialCapacity);

    ByteBufAbstract heapBuffer(int initialCapacity, int maxCapacity);

    ByteBufAbstract directBuffer();

    ByteBufAbstract directBuffer(int initialCapacity);

    ByteBufAbstract directBuffer(int initialCapacity, int maxCapacity);

    //TODO Make CompositeByteBufAbstract for the rest of the composite prefixed methods
    ByteBufAbstract compositeBuffer();

    ByteBufAbstract compositeBuffer(int maxNumComponents);

    ByteBufAbstract compositeHeapBuffer();

    ByteBufAbstract compositeHeapBuffer(int maxNumComponents);

    ByteBufAbstract compositeDirectBuffer();

    ByteBufAbstract compositeDirectBuffer(int maxNumComponents);

    boolean isDirectBufferPooled();
}
