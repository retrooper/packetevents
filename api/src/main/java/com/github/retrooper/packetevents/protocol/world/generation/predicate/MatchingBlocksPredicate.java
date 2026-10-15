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

import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.generation.BlockPredicateType;
import com.github.retrooper.packetevents.protocol.world.generation.BlockPredicateTypes;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public final class MatchingBlocksPredicate extends StateTestingPredicate {

    public static final NbtMapCodec<MatchingBlocksPredicate> MAP_CODEC = new NbtMapCodec<MatchingBlocksPredicate>() {
        private final NbtCodec<MappedEntitySet<StateType.Mapped>> blocksCodec = MappedEntitySet.codec(StateTypes.getRegistry());

        @Override
        public MatchingBlocksPredicate decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            Vector3i offset = OFFSET_CODEC.decode(tag, wrapper);
            MappedEntitySet<StateType.Mapped> blocks = tag.getOrThrow("blocks", this.blocksCodec, wrapper);
            return new MatchingBlocksPredicate(offset, blocks);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, MatchingBlocksPredicate value) throws NbtCodecException {
            OFFSET_CODEC.encode(tag, wrapper, value.offset);
            tag.set("blocks", value.blocks, this.blocksCodec, wrapper);
        }
    };

    private final MappedEntitySet<StateType.Mapped> blocks;

    public MatchingBlocksPredicate(Vector3i offset, MappedEntitySet<StateType.Mapped> blocks) {
        super(offset);
        this.blocks = blocks;
    }

    public MappedEntitySet<StateType.Mapped> getBlocks() {
        return this.blocks;
    }

    @Override
    public BlockPredicateType<?> getType() {
        return BlockPredicateTypes.MATCHING_BLOCKS;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        MatchingBlocksPredicate that = (MatchingBlocksPredicate) obj;
        if (!this.offset.equals(that.offset)) return false;
        return this.blocks.equals(that.blocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.offset, this.blocks);
    }
}
