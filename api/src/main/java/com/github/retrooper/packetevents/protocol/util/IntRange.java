/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

@NullMarked
public final class IntRange {

    public static final NbtCodec<IntRange> CODEC = new NbtCodec<IntRange>() {
        @Override
        public IntRange decode(NBT tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            if (tag instanceof NBTNumber) {
                int value = ((NBTNumber) tag).getAsInt();
                return new IntRange(value, value);
            } else if (tag instanceof NBTCompound) {
                NBTCompound compound = (NBTCompound) tag;
                int minInclusive = compound.getNumberTagValueOrThrow("min_inclusive").intValue();
                int maxInclusive = compound.getNumberTagValueOrThrow("max_inclusive").intValue();
                return new IntRange(minInclusive, maxInclusive);
            }
            int[] values = NbtCodecs.INT_ARRAY.decode(tag, wrapper);
            if (values.length != 2) {
                throw new NbtCodecException("Expected 2 range components, but got " + values.length);
            }
            return new IntRange(values[0], values[1]);
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, IntRange value) {
            if (value.minInclusive == value.maxInclusive) {
                return new NBTInt(value.minInclusive);
            }
            return new NBTList<>(NBTType.INT, Arrays.asList(
                    new NBTInt(value.minInclusive), new NBTInt(value.maxInclusive)));
        }
    };

    private final int minInclusive;
    private final int maxInclusive;

    public IntRange(int minInclusive, int maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    public boolean contains(int value) {
        return value >= this.minInclusive && value <= this.maxInclusive;
    }

    public int getMinInclusive() {
        return this.minInclusive;
    }

    public int getMaxInclusive() {
        return this.maxInclusive;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        IntRange that = (IntRange) obj;
        if (this.minInclusive != that.minInclusive) return false;
        return this.maxInclusive == that.maxInclusive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.minInclusive, this.maxInclusive);
    }

    @Override
    public String toString() {
        return "[" + this.minInclusive + "-" + this.maxInclusive + "]";
    }
}
