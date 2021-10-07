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

import com.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.retrooper.packetevents.netty.buffer.ByteBufAllocatorAbstract;
import net.minecraft.util.io.netty.buffer.ByteBufAllocator;

public class ByteBufAllocatorLegacy implements ByteBufAllocatorAbstract {
    private final ByteBufAllocator byteBufAllocator;

    public ByteBufAllocatorLegacy(Object rawByteBufAllocator) {
        this.byteBufAllocator = (ByteBufAllocator) rawByteBufAllocator;
    }

    @Override
    public Object rawByteBufAllocator() {
        return byteBufAllocator;
    }

    @Override
    public ByteBufAbstract buffer() {
        return new ByteBufLegacy(byteBufAllocator.buffer());
    }

    @Override
    public ByteBufAbstract buffer(int initialCapacity) {
        return new ByteBufLegacy(byteBufAllocator.buffer(initialCapacity));
    }

    @Override
    public ByteBufAbstract buffer(int initialCapacity, int maxCapacity) {
        return new ByteBufLegacy(byteBufAllocator.buffer(initialCapacity, maxCapacity));
    }

    @Override
    public ByteBufAbstract ioBuffer() {
        return new ByteBufLegacy(byteBufAllocator.ioBuffer());
    }

    @Override
    public ByteBufAbstract ioBuffer(int initialCapacity) {
        return new ByteBufLegacy(byteBufAllocator.ioBuffer(initialCapacity));
    }

    @Override
    public ByteBufAbstract ioBuffer(int initialCapacity, int maxCapacity) {
        return new ByteBufLegacy(byteBufAllocator.ioBuffer(initialCapacity, maxCapacity));
    }

    @Override
    public ByteBufAbstract heapBuffer() {
        return new ByteBufLegacy(byteBufAllocator.heapBuffer());
    }

    @Override
    public ByteBufAbstract heapBuffer(int initialCapacity) {
        return new ByteBufLegacy(byteBufAllocator.heapBuffer(initialCapacity));
    }

    @Override
    public ByteBufAbstract heapBuffer(int initialCapacity, int maxCapacity) {
        return new ByteBufLegacy(byteBufAllocator.buffer(initialCapacity, maxCapacity));
    }

    @Override
    public ByteBufAbstract directBuffer() {
        return new ByteBufLegacy(byteBufAllocator.directBuffer());
    }

    @Override
    public ByteBufAbstract directBuffer(int initialCapacity) {
        return new ByteBufLegacy(byteBufAllocator.directBuffer(initialCapacity));
    }

    @Override
    public ByteBufAbstract directBuffer(int initialCapacity, int maxCapacity) {
        return new ByteBufLegacy(byteBufAllocator.directBuffer(initialCapacity, maxCapacity));
    }

    @Override
    public ByteBufAbstract compositeBuffer() {
        return new ByteBufLegacy(byteBufAllocator.compositeBuffer());
    }

    @Override
    public ByteBufAbstract compositeBuffer(int maxNumComponents) {
        return new ByteBufLegacy(byteBufAllocator.compositeBuffer(maxNumComponents));
    }

    @Override
    public ByteBufAbstract compositeHeapBuffer() {
        return new ByteBufLegacy(byteBufAllocator.compositeHeapBuffer());
    }

    @Override
    public ByteBufAbstract compositeHeapBuffer(int maxNumComponents) {
        return new ByteBufLegacy(byteBufAllocator.compositeHeapBuffer(maxNumComponents));
    }

    @Override
    public ByteBufAbstract compositeDirectBuffer() {
        return new ByteBufLegacy(byteBufAllocator.compositeDirectBuffer());
    }

    @Override
    public ByteBufAbstract compositeDirectBuffer(int maxNumComponents) {
        return new ByteBufLegacy(byteBufAllocator.compositeDirectBuffer(maxNumComponents));
    }

    @Override
    public boolean isDirectBufferPooled() {
        return byteBufAllocator.isDirectBufferPooled();
    }
}

