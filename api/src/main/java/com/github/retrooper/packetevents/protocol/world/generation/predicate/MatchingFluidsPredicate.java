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
import com.github.retrooper.packetevents.protocol.world.generation.fluids.Fluid;
import com.github.retrooper.packetevents.protocol.world.generation.fluids.Fluids;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public final class MatchingFluidsPredicate extends StateTestingPredicate {

    public static final NbtMapCodec<MatchingFluidsPredicate> MAP_CODEC = new NbtMapCodec<MatchingFluidsPredicate>() {
        private final NbtCodec<MappedEntitySet<Fluid>> fluidsCodec = MappedEntitySet.codec(Fluids.getRegistry());

        @Override
        public MatchingFluidsPredicate decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            Vector3i offset = OFFSET_CODEC.decode(tag, wrapper);
            MappedEntitySet<Fluid> fluids = tag.getOrThrow("fluids", this.fluidsCodec, wrapper);
            return new MatchingFluidsPredicate(offset, fluids);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, MatchingFluidsPredicate value) throws NbtCodecException {
            OFFSET_CODEC.encode(tag, wrapper, value.offset);
            tag.set("fluids", value.fluids, this.fluidsCodec, wrapper);
        }
    };

    private final MappedEntitySet<Fluid> fluids;

    public MatchingFluidsPredicate(Vector3i offset, MappedEntitySet<Fluid> fluids) {
        super(offset);
        this.fluids = fluids;
    }

    public MappedEntitySet<Fluid> getFluids() {
        return this.fluids;
    }

    @Override
    public BlockPredicateType<?> getType() {
        return BlockPredicateTypes.MATCHING_FLUIDS;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        MatchingFluidsPredicate that = (MatchingFluidsPredicate) obj;
        if (!this.offset.equals(that.offset)) return false;
        return this.fluids.equals(that.fluids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.offset, this.fluids);
    }
}
