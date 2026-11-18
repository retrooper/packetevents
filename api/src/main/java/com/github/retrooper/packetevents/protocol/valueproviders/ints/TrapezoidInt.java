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
public final class TrapezoidInt implements IntProvider {

    public static final NbtMapCodec<TrapezoidInt> MAP_CODEC = new NbtMapCodec<TrapezoidInt>() {
        @Override
        public TrapezoidInt decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            int minInclusive = tag.getNumberTagValueOrThrow("min").intValue();
            int maxInclusive = tag.getNumberTagValueOrThrow("max").intValue();
            int plateau = tag.getNumberTagValueOrThrow("plateau").intValue();
            return new TrapezoidInt(minInclusive, maxInclusive, plateau);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, TrapezoidInt value) throws NbtCodecException {
            tag.setTag("min", new NBTInt(value.minInclusive));
            tag.setTag("max", new NBTInt(value.maxInclusive));
            tag.setTag("plateau", new NBTInt(value.plateau));
        }
    };

    private final int minInclusive;
    private final int maxInclusive;
    private final int plateau;

    public TrapezoidInt(int minInclusive, int maxInclusive, int plateau) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
        this.plateau = plateau;
    }

    @Override
    public IntProviderType<?> getType() {
        return IntProviderTypes.TRAPEZOID;
    }

    @Override
    public int getSample(Random random) {
        if (this.plateau == 0 && this.maxInclusive == -this.minInclusive) {
            return random.nextInt(this.maxInclusive + 1) - random.nextInt(this.maxInclusive + 1);
        }

        int range = this.maxInclusive - this.minInclusive;
        if (this.plateau == range) {
            return this.minInclusive + random.nextInt(range + 1);
        }

        int plateauStart = (range - this.plateau) / 2;
        int plateauEnd = range - plateauStart;
        return this.minInclusive
                + random.nextInt(plateauEnd + 1)
                + random.nextInt(plateauStart + 1);
    }

    @Override
    public int getMin() {
        return this.minInclusive;
    }

    @Override
    public int getMax() {
        return this.maxInclusive;
    }

    public int getPlateau() {
        return this.plateau;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        TrapezoidInt that = (TrapezoidInt) obj;
        if (this.minInclusive != that.minInclusive) return false;
        if (this.maxInclusive != that.maxInclusive) return false;
        return this.plateau == that.plateau;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.minInclusive, this.maxInclusive, this.plateau);
    }
}
