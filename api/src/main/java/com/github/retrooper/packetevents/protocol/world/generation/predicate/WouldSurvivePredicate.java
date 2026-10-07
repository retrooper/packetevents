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
import com.github.retrooper.packetevents.protocol.world.states.BlockStateCodec;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public final class WouldSurvivePredicate implements BlockPredicate {

    public static final NbtMapCodec<WouldSurvivePredicate> MAP_CODEC = new NbtMapCodec<WouldSurvivePredicate>() {
        @Override
        public WouldSurvivePredicate decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            Vector3i offset = StateTestingPredicate.OFFSET_CODEC.decode(tag, wrapper);
            WrappedBlockState state = tag.getOrThrow("state", BlockStateCodec.CODEC, wrapper);
            return new WouldSurvivePredicate(offset, state);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, WouldSurvivePredicate value) throws NbtCodecException {
            StateTestingPredicate.OFFSET_CODEC.encode(tag, wrapper, value.offset);
            tag.set("state", value.state, BlockStateCodec.CODEC, wrapper);
        }
    };

    private final Vector3i offset;
    private final WrappedBlockState state;

    public WouldSurvivePredicate(Vector3i offset, WrappedBlockState state) {
        this.offset = offset;
        this.state = state;
    }

    public Vector3i getOffset() {
        return this.offset;
    }

    public WrappedBlockState getState() {
        return this.state;
    }

    @Override
    public BlockPredicateType<?> getType() {
        return BlockPredicateTypes.WOULD_SURVIVE;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        WouldSurvivePredicate that = (WouldSurvivePredicate) obj;
        if (!this.offset.equals(that.offset)) return false;
        return this.state.equals(that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.offset, this.state);
    }
}
