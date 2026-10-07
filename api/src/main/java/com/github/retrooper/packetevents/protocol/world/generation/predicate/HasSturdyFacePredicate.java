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
import com.github.retrooper.packetevents.protocol.world.Direction;
import com.github.retrooper.packetevents.protocol.world.generation.BlockPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.BlockPredicateType;
import com.github.retrooper.packetevents.protocol.world.generation.BlockPredicateTypes;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public final class HasSturdyFacePredicate implements BlockPredicate {

    public static final NbtMapCodec<HasSturdyFacePredicate> MAP_CODEC = new NbtMapCodec<HasSturdyFacePredicate>() {
        @Override
        public HasSturdyFacePredicate decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            Vector3i offset = StateTestingPredicate.OFFSET_CODEC.decode(tag, wrapper);
            Direction direction = tag.getOrThrow("direction", Direction.CODEC, wrapper);
            return new HasSturdyFacePredicate(offset, direction);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, HasSturdyFacePredicate value) throws NbtCodecException {
            StateTestingPredicate.OFFSET_CODEC.encode(tag, wrapper, value.offset);
            tag.set("direction", value.direction, Direction.CODEC, wrapper);
        }
    };

    private final Vector3i offset;
    private final Direction direction;

    public HasSturdyFacePredicate(Vector3i offset, Direction direction) {
        this.offset = offset;
        this.direction = direction;
    }

    public Vector3i getOffset() {
        return this.offset;
    }

    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public BlockPredicateType<?> getType() {
        return BlockPredicateTypes.HAS_STURDY_FACE;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        HasSturdyFacePredicate that = (HasSturdyFacePredicate) obj;
        if (!this.offset.equals(that.offset)) return false;
        return this.direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.offset, this.direction);
    }
}
