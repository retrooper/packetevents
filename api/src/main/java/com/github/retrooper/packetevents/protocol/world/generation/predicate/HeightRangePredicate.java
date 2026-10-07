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

package com.github.retrooper.packetevents.protocol.world.generation.predicate;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.generation.BlockPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.BlockPredicateType;
import com.github.retrooper.packetevents.protocol.world.generation.BlockPredicateTypes;
import com.github.retrooper.packetevents.protocol.util.VerticalAnchor;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public final class HeightRangePredicate implements BlockPredicate {

    public static final NbtMapCodec<HeightRangePredicate> MAP_CODEC = new NbtMapCodec<HeightRangePredicate>() {
        @Override
        public HeightRangePredicate decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            VerticalAnchor minInclusive = tag.getOrThrow("min_inclusive", VerticalAnchor.MAP_CODEC.codec(), wrapper);
            VerticalAnchor maxInclusive = tag.getOrThrow("max_inclusive", VerticalAnchor.MAP_CODEC.codec(), wrapper);
            return new HeightRangePredicate(minInclusive, maxInclusive);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, HeightRangePredicate value) throws NbtCodecException {
            tag.set("min_inclusive", value.minInclusive, VerticalAnchor.MAP_CODEC.codec(), wrapper);
            tag.set("max_inclusive", value.maxInclusive, VerticalAnchor.MAP_CODEC.codec(), wrapper);
        }
    };

    private final VerticalAnchor minInclusive;
    private final VerticalAnchor maxInclusive;

    public HeightRangePredicate(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    public VerticalAnchor getMinInclusive() {
        return this.minInclusive;
    }

    public VerticalAnchor getMaxInclusive() {
        return this.maxInclusive;
    }

    @Override
    public BlockPredicateType<?> getType() {
        return BlockPredicateTypes.HEIGHT_RANGE;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        HeightRangePredicate that = (HeightRangePredicate) obj;
        if (!this.minInclusive.equals(that.minInclusive)) return false;
        return this.maxInclusive.equals(that.maxInclusive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.minInclusive, this.maxInclusive);
    }
}
