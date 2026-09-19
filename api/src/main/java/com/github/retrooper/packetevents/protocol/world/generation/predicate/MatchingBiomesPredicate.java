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
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.biome.Biome;
import com.github.retrooper.packetevents.protocol.world.biome.Biomes;
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
public final class MatchingBiomesPredicate implements BlockPredicate {

    public static final NbtMapCodec<MatchingBiomesPredicate> MAP_CODEC = new NbtMapCodec<MatchingBiomesPredicate>() {
        @Override
        public MatchingBiomesPredicate decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            MappedEntitySet<Biome> biomes = MappedEntitySet.decode(
                    tag.getTagOrThrow("biomes"), wrapper, Biomes.getRegistry());
            return new MatchingBiomesPredicate(biomes);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, MatchingBiomesPredicate value) throws NbtCodecException {
            tag.setTag("biomes", MappedEntitySet.encode(wrapper, value.biomes));
        }
    };

    private final MappedEntitySet<Biome> biomes;

    public MatchingBiomesPredicate(MappedEntitySet<Biome> biomes) {
        this.biomes = biomes;
    }

    public MappedEntitySet<Biome> getBiomes() {
        return this.biomes;
    }

    @Override
    public BlockPredicateType<?> getType() {
        return BlockPredicateTypes.MATCHING_BIOMES;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        MatchingBiomesPredicate that = (MatchingBiomesPredicate) obj;
        return this.biomes.equals(that.biomes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.biomes);
    }
}
