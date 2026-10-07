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
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 26.3+
 */
@NullMarked
public abstract class StateTestingPredicate implements BlockPredicate {

    protected static final NbtMapCodec<Vector3i> OFFSET_CODEC = new NbtMapCodec<Vector3i>() {
        @Override
        public Vector3i decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            return tag.getOr("offset", Vector3i.CODEC, Vector3i.zero(), wrapper);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, Vector3i value) throws NbtCodecException {
            if (value.getX() != 0 || value.getY() != 0 || value.getZ() != 0) {
                tag.set("offset", value, Vector3i.CODEC, wrapper);
            }
        }
    };

    protected final Vector3i offset;

    protected StateTestingPredicate(Vector3i offset) {
        this.offset = offset;
    }

    public Vector3i getOffset() {
        return this.offset;
    }
}
