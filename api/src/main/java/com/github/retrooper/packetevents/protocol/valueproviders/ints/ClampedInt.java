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
public final class ClampedInt implements IntProvider {

    public static final NbtMapCodec<ClampedInt> MAP_CODEC = new NbtMapCodec<ClampedInt>() {
        @Override
        public ClampedInt decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            IntProvider source = tag.getOrThrow("source", IntProvider.CODEC, wrapper);
            int minInclusive = tag.getNumberTagValueOrThrow("min_inclusive").intValue();
            int maxInclusive = tag.getNumberTagValueOrThrow("max_inclusive").intValue();
            return new ClampedInt(source, minInclusive, maxInclusive);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, ClampedInt value) throws NbtCodecException {
            tag.set("source", value.source, IntProvider.CODEC, wrapper);
            tag.setTag("min_inclusive", new NBTInt(value.minInclusive));
            tag.setTag("max_inclusive", new NBTInt(value.maxInclusive));
        }
    };

    private final IntProvider source;
    private final int minInclusive;
    private final int maxInclusive;

    public ClampedInt(IntProvider source, int minInclusive, int maxInclusive) {
        this.source = source;
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    @Override
    public IntProviderType<?> getType() {
        return IntProviderTypes.CLAMPED;
    }

    @Override
    public int getSample(Random random) {
        return Math.max(this.minInclusive, Math.min(this.maxInclusive, this.source.getSample(random)));
    }

    @Override
    public int getMin() {
        return Math.max(this.minInclusive, this.source.getMin());
    }

    @Override
    public int getMax() {
        return Math.min(this.maxInclusive, this.source.getMax());
    }

    public IntProvider getSource() {
        return this.source;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        ClampedInt that = (ClampedInt) obj;
        if (this.minInclusive != that.minInclusive) return false;
        if (this.maxInclusive != that.maxInclusive) return false;
        return this.source.equals(that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.source, this.minInclusive, this.maxInclusive);
    }
}
