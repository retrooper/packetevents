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
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
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
public final class ClampedNormalInt implements IntProvider {

    public static final NbtMapCodec<ClampedNormalInt> MAP_CODEC = new NbtMapCodec<ClampedNormalInt>() {
        @Override
        public ClampedNormalInt decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            float mean = tag.getNumberTagValueOrThrow("mean").floatValue();
            float deviation = tag.getNumberTagValueOrThrow("deviation").floatValue();
            int minInclusive = tag.getNumberTagValueOrThrow("min_inclusive").intValue();
            int maxInclusive = tag.getNumberTagValueOrThrow("max_inclusive").intValue();
            return new ClampedNormalInt(mean, deviation, minInclusive, maxInclusive);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, ClampedNormalInt value) throws NbtCodecException {
            tag.setTag("mean", new NBTFloat(value.mean));
            tag.setTag("deviation", new NBTFloat(value.deviation));
            tag.setTag("min_inclusive", new NBTInt(value.minInclusive));
            tag.setTag("max_inclusive", new NBTInt(value.maxInclusive));
        }
    };

    private final float mean;
    private final float deviation;
    private final int minInclusive;
    private final int maxInclusive;

    public ClampedNormalInt(float mean, float deviation, int minInclusive, int maxInclusive) {
        this.mean = mean;
        this.deviation = deviation;
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    @Override
    public IntProviderType<?> getType() {
        return IntProviderTypes.CLAMPED_NORMAL;
    }

    @Override
    public int getSample(Random random) {
        return (int) Math.max(this.minInclusive, Math.min(this.maxInclusive,
                random.nextGaussian() * this.deviation + this.mean));
    }

    @Override
    public int getMin() {
        return this.minInclusive;
    }

    @Override
    public int getMax() {
        return this.maxInclusive;
    }

    public float getMean() {
        return this.mean;
    }

    public float getDeviation() {
        return this.deviation;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        ClampedNormalInt that = (ClampedNormalInt) obj;
        if (Float.compare(that.mean, this.mean) != 0) return false;
        if (Float.compare(that.deviation, this.deviation) != 0) return false;
        if (this.minInclusive != that.minInclusive) return false;
        return this.maxInclusive == that.maxInclusive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mean, this.deviation, this.minInclusive, this.maxInclusive);
    }
}
