/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.data.world.chunk.impl.v1_7;

import com.github.retrooper.packetevents.protocol.data.world.blockstate.BaseBlockState;
import com.github.retrooper.packetevents.protocol.data.world.blockstate.MagicBlockState;
import com.github.retrooper.packetevents.protocol.data.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.data.world.chunk.ByteArray3d;
import com.github.retrooper.packetevents.protocol.data.world.chunk.NibbleArray3d;
import com.github.retrooper.packetevents.util.Vector3i;

public class Chunk_v1_7 implements BaseChunk {
    private final ByteArray3d blocks;
    private final NibbleArray3d extendedBlocks;

    public Chunk_v1_7() {
        blocks = new ByteArray3d(4096);
        extendedBlocks = new NibbleArray3d(4096);
    }

    @Override
    public BaseBlockState get(int x, int y, int z) {
        return new MagicBlockState(blocks.get(x, y, z), extendedBlocks.get(x, y, z));
    }

    @Override
    public void set(int x, int y, int z, int combinedID) {
        blocks.set(x, y, z, combinedID & 0xFF);
        extendedBlocks.set(x, y, z, combinedID >> 12);
    }

    @Override
    public boolean isKnownEmpty() {
        return false;
    }

    public ByteArray3d getBlocks() {
        return blocks;
    }

    public NibbleArray3d getMetadata() {
        return extendedBlocks;
    }
}
