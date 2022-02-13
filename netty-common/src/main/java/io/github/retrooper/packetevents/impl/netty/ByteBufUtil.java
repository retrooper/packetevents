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

package io.github.retrooper.packetevents.impl.netty;

import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.impl.netty.buffer.ByteBufImpl;
import io.netty.buffer.Unpooled;

public class ByteBufUtil {
    public static ByteBufAbstract wrappedBuffer(byte[] bytes) {
        return new ByteBufImpl(Unpooled.wrappedBuffer(bytes));
    }

    public static ByteBufAbstract copiedBuffer(byte[] bytes) {
        return new ByteBufImpl(Unpooled.copiedBuffer(bytes));
    }

    public static ByteBufAbstract buffer() {
        return new ByteBufImpl(Unpooled.buffer());
    }

    public static ByteBufAbstract buffer(int initialCapacity) {
        return new ByteBufImpl(Unpooled.buffer(initialCapacity));
    }

    public static ByteBufAbstract buffer(int initialCapacity, int maxCapacity) {
        return new ByteBufImpl(Unpooled.buffer(initialCapacity, maxCapacity));
    }

    public static ByteBufAbstract directBuffer() {
        return new ByteBufImpl(Unpooled.directBuffer());
    }

    public static ByteBufAbstract directBuffer(int initialCapacity) {
        return new ByteBufImpl(Unpooled.directBuffer(initialCapacity));
    }

    public static ByteBufAbstract directBuffer(int initialCapacity, int maxCapacity) {
        return new ByteBufImpl(Unpooled.directBuffer(initialCapacity, maxCapacity));
    }

    public static ByteBufAbstract compositeBuffer() {
        return new ByteBufImpl(Unpooled.compositeBuffer());
    }

    public static ByteBufAbstract compositeBuffer(int maxNumComponents) {
        return new ByteBufImpl(Unpooled.compositeBuffer(maxNumComponents));
    }
}
