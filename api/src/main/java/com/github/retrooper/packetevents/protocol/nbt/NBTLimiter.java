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
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.NonExtendable
public interface NBTLimiter {

    int DEFAULT_MAX_SIZE = Integer.getInteger("packetevents.nbt.default-max-size", 1 << 20); // 2MiB
    int DEFAULT_MAX_DEPTH = Integer.getInteger("packetevents.nbt.default-max-depth", 512);

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

            @Override
            public void enterDepth() {
                // no-op
            }

            @Override
            public void exitDepth() {
                // no-op
            }
        };
    }

    static NBTLimiter forBuffer(Object byteBuf) {
        return forBuffer(byteBuf, DEFAULT_MAX_SIZE);
    }

    static NBTLimiter forBuffer(Object byteBuf, int maxBytes) {
        return forBuffer(byteBuf, maxBytes, DEFAULT_MAX_DEPTH);
    }

    static NBTLimiter forBuffer(Object byteBuf, int maxBytes, int maxDepth) {
        return new NBTLimiter() {
            private int bytes;
            private int depth;

            @Override
            public void increment(int amount) {
                if (amount < 0) {
                    throw new IllegalArgumentException("Can't increment NBT limiter by negative amount: " + amount);
                } else if (this.bytes + amount > maxBytes) {
                    throw new IllegalArgumentException("NBT size limit reached (" + this.bytes + "/" + maxBytes + ")");
                }
                this.bytes += amount;
            }

            @Override
            public void checkReadability(int length) {
                int readableBytes = ByteBufHelper.readableBytes(byteBuf);
                if (length > readableBytes) {
                    throw new IllegalArgumentException("Can't read more than possible: " + length + " > " + readableBytes);
                }
            }

            @Override
            public void enterDepth() {
                if (this.depth >= maxDepth) {
                    throw new IllegalArgumentException("NBT depth limit reached (" + this.depth + "/" + maxDepth + ")");
                }
                this.depth++;
            }

            @Override
            public void exitDepth() {
                if (this.depth <= 0) {
                    throw new IllegalArgumentException("Can't exit top-level depth");
                }
                this.depth--;
            }
        };
    }

    void increment(int amount);

    void checkReadability(int length);

    void enterDepth();

    void exitDepth();
}
