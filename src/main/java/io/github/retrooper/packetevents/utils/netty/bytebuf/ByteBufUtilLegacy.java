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

package io.github.retrooper.packetevents.utils.netty.bytebuf;

import net.minecraft.util.io.netty.buffer.Unpooled;

final class ByteBufUtilLegacy {
    static ByteBufAbstract wrappedBuffer(byte[] bytes) {
        return new ByteBufLegacy(Unpooled.wrappedBuffer(bytes));
    }

    static ByteBufAbstract copiedBuffer(byte[] bytes) {
        return new ByteBufLegacy(Unpooled.copiedBuffer(bytes));
    }

    static ByteBufAbstract buffer() {
        return new ByteBufLegacy(Unpooled.buffer());
    }

    static ByteBufAbstract buffer(int initialCapacity) {
        return new ByteBufLegacy(Unpooled.buffer(initialCapacity));
    }

    static ByteBufAbstract buffer(int initialCapacity, int maxCapacity) {
        return new ByteBufLegacy(Unpooled.buffer(initialCapacity, maxCapacity));
    }

    static ByteBufAbstract directBuffer() {
        return new ByteBufLegacy(Unpooled.directBuffer());
    }

    static ByteBufAbstract directBuffer(int initialCapacity) {
        return new ByteBufLegacy(Unpooled.directBuffer(initialCapacity));
    }

    static ByteBufAbstract directBuffer(int initialCapacity, int maxCapacity) {
        return new ByteBufLegacy(Unpooled.directBuffer(initialCapacity, maxCapacity));
    }

    static ByteBufAbstract compositeBuffer() {
        return new ByteBufLegacy(Unpooled.compositeBuffer());
    }

    static ByteBufAbstract compositeBuffer(int maxNumComponents) {
        return new ByteBufLegacy(Unpooled.compositeBuffer(maxNumComponents));
    }
}
