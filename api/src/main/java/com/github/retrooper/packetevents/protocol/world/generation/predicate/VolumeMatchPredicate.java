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
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
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
public final class VolumeMatchPredicate implements BlockPredicate {

    public static final NbtMapCodec<VolumeMatchPredicate> MAP_CODEC = new NbtMapCodec<VolumeMatchPredicate>() {
        @Override
        public VolumeMatchPredicate decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            Vector3i min = tag.getOrThrow("min", Vector3i.CODEC, wrapper);
            Vector3i max = tag.getOrThrow("max", Vector3i.CODEC, wrapper);
            BlockPredicate match = tag.getOrThrow("match", BlockPredicate.CODEC, wrapper);
            return new VolumeMatchPredicate(min, max, match);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, VolumeMatchPredicate value) throws NbtCodecException {
            tag.set("min", value.min, Vector3i.CODEC, wrapper);
            tag.set("max", value.max, Vector3i.CODEC, wrapper);
            tag.set("match", value.match, BlockPredicate.CODEC, wrapper);
        }
    };

    private final Vector3i min;
    private final Vector3i max;
    private final BlockPredicate match;

    public VolumeMatchPredicate(Vector3i min, Vector3i max, BlockPredicate match) {
        this.min = min;
        this.max = max;
        this.match = match;
    }

    public Vector3i getMin() {
        return this.min;
    }

    public Vector3i getMax() {
        return this.max;
    }

    public BlockPredicate getMatch() {
        return this.match;
    }

    @Override
    public BlockPredicateType<?> getType() {
        return BlockPredicateTypes.VOLUME_MATCH;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        VolumeMatchPredicate that = (VolumeMatchPredicate) obj;
        if (!this.min.equals(that.min)) return false;
        if (!this.max.equals(that.max)) return false;
        return this.match.equals(that.match);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.min, this.max, this.match);
    }
}
