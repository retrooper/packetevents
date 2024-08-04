/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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
package com.github.retrooper.packetevents.protocol.nbt;

import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import org.jetbrains.annotations.NotNull;

public interface NBTLimiter {

    int DEFAULT_MAX_SIZE = 2097152;

    static NBTLimiter noop() {
        return new NBTLimiter() {
            @Override
            public void increment(int amount) {
                // no-op
            }

            @Override
            public void checkReadability(int length) {
                // no-op
            }
        };
    }

    static NBTLimiter forBuffer(final @NotNull Object byteBuf) {
        return forBuffer(byteBuf, DEFAULT_MAX_SIZE);
    }

    static NBTLimiter forBuffer(final @NotNull Object byteBuf, final int max) {
        return new NBTLimiter() {
            private int bytes;

            @Override
            public void increment(int amount) {
                bytes += amount;

                if (bytes > max) throw new IllegalArgumentException("NBT size limit reached (" + bytes + "/" + max + ")");
            }

            @Override
            public void checkReadability(int length) {
                if (length > ByteBufHelper.readableBytes(byteBuf)) throw new IllegalArgumentException("Length is too large: " + length + ", readable: " + ByteBufHelper.readableBytes(byteBuf));
            }
        };
    }

    void increment(int amount);

    void checkReadability(int length);

}
