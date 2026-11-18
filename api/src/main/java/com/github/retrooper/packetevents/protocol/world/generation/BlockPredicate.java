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

package com.github.retrooper.packetevents.protocol.world.generation;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 26.3+
 */
@NullMarked
public interface BlockPredicate {

    NbtCodec<BlockPredicate> CODEC = new NbtCodec<BlockPredicate>() {
        @Override
        public BlockPredicate decode(NBT nbt, PacketWrapper<?> wrapper) throws NbtCodecException {
            NBTCompound compound = nbt.castOrThrow(NBTCompound.class);
            BlockPredicateType<?> type = compound.getOrThrow("type", BlockPredicateType.CODEC, wrapper);
            return type.getCodec().decode(compound, wrapper);
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, BlockPredicate value) throws NbtCodecException {
            @SuppressWarnings("unchecked")
            BlockPredicateType<BlockPredicate> unsafeType = (BlockPredicateType<BlockPredicate>) value.getType();
            NBTCompound compound = new NBTCompound();
            unsafeType.getCodec().encode(compound, wrapper, value);
            compound.set("type", value.getType(), BlockPredicateType.CODEC, wrapper);
            return compound;
        }
    };

    BlockPredicateType<?> getType();
}
