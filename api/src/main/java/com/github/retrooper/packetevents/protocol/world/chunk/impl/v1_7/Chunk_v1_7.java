/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_7;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.ByteArray3d;
import com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;

public class Chunk_v1_7 implements BaseChunk {
    private final ByteArray3d blocks;
    private NibbleArray3d metadata;
    private NibbleArray3d blocklight;
    private NibbleArray3d skylight;
    private NibbleArray3d extendedBlocks;

    public Chunk_v1_7(boolean skylight, boolean extended) {
        this(new ByteArray3d(4096), new NibbleArray3d(4096), new NibbleArray3d(4096), skylight ? new NibbleArray3d(4096) : null, extended ? new NibbleArray3d(4096) : null);
    }

    public Chunk_v1_7(ByteArray3d blocks, NibbleArray3d metadata, NibbleArray3d blocklight, NibbleArray3d skylight, NibbleArray3d extendedBlocks) {
        this.blocks = blocks;
        this.metadata = metadata;
        this.blocklight = blocklight;
        this.skylight = skylight;
        this.extendedBlocks = extendedBlocks;
    }

    @Override
    public int getBlockId(int x, int y, int z) {
        return blocks.get(x, y, z) | (extendedBlocks.get(x, y, z) << 12);
    }

    @Override
    public void set(int x, int y, int z, int combinedID) {
        blocks.set(x, y, z, combinedID & 0xFF);
        extendedBlocks.set(x, y, z, combinedID >> 12);
    }

    @Override
    public boolean isEmpty() {
        for(byte block : this.blocks.getData()) {
            if(block != 0) {
                return false;
            }
        }
        return true;
    }

    public ByteArray3d getBlocks() {
        return this.blocks;
    }

    public NibbleArray3d getMetadata() {
        return this.metadata;
    }

    public NibbleArray3d getBlockLight() {
        return this.blocklight;
    }

    public NibbleArray3d getSkyLight() {
        return this.skylight;
    }

    public NibbleArray3d getExtendedBlocks() {
        return this.extendedBlocks;
    }
}
