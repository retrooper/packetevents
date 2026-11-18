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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public final class NotPredicate implements BlockPredicate {

    public static final NbtMapCodec<NotPredicate> MAP_CODEC = new NbtMapCodec<NotPredicate>() {
        @Override
        public NotPredicate decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            BlockPredicate predicate = tag.getOrThrow("predicate", BlockPredicate.CODEC, wrapper);
            return new NotPredicate(predicate);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, NotPredicate value) throws NbtCodecException {
            tag.set("predicate", value.predicate, BlockPredicate.CODEC, wrapper);
        }
    };

    private final BlockPredicate predicate;

    public NotPredicate(BlockPredicate predicate) {
        this.predicate = predicate;
    }

    public BlockPredicate getPredicate() {
        return this.predicate;
    }

    @Override
    public BlockPredicateType<?> getType() {
        return BlockPredicateTypes.NOT;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        NotPredicate that = (NotPredicate) obj;
        return this.predicate.equals(that.predicate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.predicate);
    }
}
