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

package com.github.retrooper.packetevents.protocol.valueproviders.ints;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

/**
 * @versions 1.17+
 */
@NullMarked
public final class UniformInt implements IntProvider {

    public static final NbtMapCodec<UniformInt> MAP_CODEC = new NbtMapCodec<UniformInt>() {
        @Override
        public UniformInt decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            int minInclusive = tag.getNumberTagValueOrThrow("min_inclusive").intValue();
            int maxInclusive = tag.getNumberTagValueOrThrow("max_inclusive").intValue();
            return new UniformInt(minInclusive, maxInclusive);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, UniformInt value) throws NbtCodecException {
            tag.setTag("min_inclusive", new NBTInt(value.minInclusive));
            tag.setTag("max_inclusive", new NBTInt(value.maxInclusive));
        }
    };

    private final int minInclusive;
    private final int maxInclusive;

    public UniformInt(int minInclusive, int maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    @Override
    public IntProviderType<?> getType() {
        return IntProviderTypes.UNIFORM;
    }

    @Override
    public int getSample(Random random) {
        return this.minInclusive + random.nextInt(this.maxInclusive - this.minInclusive + 1);
    }

    @Override
    public int getMin() {
        return this.minInclusive;
    }

    @Override
    public int getMax() {
        return this.maxInclusive;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        UniformInt that = (UniformInt) obj;
        if (this.minInclusive != that.minInclusive) return false;
        return this.maxInclusive == that.maxInclusive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.minInclusive, this.maxInclusive);
    }
}
