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

package com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_8;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;
import com.github.retrooper.packetevents.protocol.world.chunk.ShortArray3d;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;

public class Chunk_v1_8 implements BaseChunk {
    private ShortArray3d blocks;
    private NibbleArray3d blocklight;
    private NibbleArray3d skylight;

    public Chunk_v1_8(boolean skylight) {
        this(new ShortArray3d(4096), new NibbleArray3d(4096), skylight ? new NibbleArray3d(4096) : null);
    }

    public Chunk_v1_8(ShortArray3d blocks, NibbleArray3d blocklight, NibbleArray3d skylight) {
        this.blocks = blocks;
        this.blocklight = blocklight;
        this.skylight = skylight;
    }

    public ShortArray3d getBlocks() {
        return this.blocks;
    }

    public NibbleArray3d getBlockLight() {
        return this.blocklight;
    }

    public NibbleArray3d getSkyLight() {
        return this.skylight;
    }

    @Override
    public int getBlockId(int x, int y, int z) {
        return this.blocks.get(x, y, z);
    }

    @Override
    public void set(int x, int y, int z, int combinedID) {
        this.blocks.set(x, y, z, combinedID);
    }

    @Override
    public boolean isEmpty() {
        for(short block : this.blocks.getData()) {
            if(block != 0) {
                return false;
            }
        }

        return true;
    }
}
