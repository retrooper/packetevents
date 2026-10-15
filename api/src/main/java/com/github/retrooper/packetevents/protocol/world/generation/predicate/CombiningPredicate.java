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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public abstract class CombiningPredicate implements BlockPredicate {

    protected final List<BlockPredicate> predicates;

    protected CombiningPredicate(List<BlockPredicate> predicates) {
        this.predicates = Collections.unmodifiableList(predicates);
    }

    protected static <T extends CombiningPredicate> NbtMapCodec<T> codec(ListDecoder<T> decoder) {
        return new NbtMapCodec<T>() {
            @Override
            public T decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
                List<BlockPredicate> predicates = tag.getListOrEmpty("predicates", BlockPredicate.CODEC, wrapper);
                return decoder.decode(predicates);
            }

            @Override
            public void encode(NBTCompound tag, PacketWrapper<?> wrapper, T value) throws NbtCodecException {
                tag.setList("predicates", value.predicates, BlockPredicate.CODEC, wrapper);
            }
        };
    }

    public List<BlockPredicate> getPredicates() {
        return this.predicates;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        CombiningPredicate that = (CombiningPredicate) obj;
        return this.predicates.equals(that.predicates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.predicates);
    }

    @FunctionalInterface
    protected interface ListDecoder<T> {

        T decode(List<BlockPredicate> predicates);
    }
}
