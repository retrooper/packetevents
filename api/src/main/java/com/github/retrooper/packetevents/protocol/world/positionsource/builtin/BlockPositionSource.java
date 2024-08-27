/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.positionsource.builtin;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.positionsource.PositionSource;
import com.github.retrooper.packetevents.protocol.world.positionsource.PositionSourceTypes;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class BlockPositionSource extends PositionSource {

    private Vector3i pos;

    public BlockPositionSource(Vector3i pos) {
        super(PositionSourceTypes.BLOCK);
        this.pos = pos;
    }

    public static BlockPositionSource read(PacketWrapper<?> wrapper) {
        return new BlockPositionSource(wrapper.readBlockPosition());
    }

    public static void write(PacketWrapper<?> wrapper, BlockPositionSource source) {
        wrapper.writeBlockPosition(source.pos);
    }

    public static BlockPositionSource decodeSource(NBTCompound compound, ClientVersion version) {
        NBTIntArray arr = compound.getTagOfTypeOrThrow("pos", NBTIntArray.class);
        return new BlockPositionSource(new Vector3i(arr.getValue()));
    }

    public static void encodeSource(BlockPositionSource source, ClientVersion version, NBTCompound compound) {
        compound.setTag("pos", new NBTIntArray(new int[]{source.pos.x, source.pos.y, source.pos.z}));
    }

    public Vector3i getPos() {
        return this.pos;
    }

    public void setPos(Vector3i pos) {
        this.pos = pos;
    }
}
