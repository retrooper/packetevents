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

package com.github.retrooper.packetevents.protocol.world.chunk.reader.impl;

import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.ByteArray3d;
import com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_7.Chunk_v1_7;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;

import java.util.BitSet;

public class ChunkReader_v1_7 implements ChunkReader {
    @Override
    public BaseChunk[] read(BitSet primarySet, BitSet sevenExtendedMask, boolean fullChunk, boolean hasSkyLight, boolean checkForSky, int chunkSize, byte[] data, NetStreamInput dataIn) {
        Chunk_v1_7[] chunks = new Chunk_v1_7[16];
        int pos = 0;
        int expected = 0;
        boolean sky = false;

        // 0 = Calculate expected length and determine if the packet has skylight.
        // 1 = Create chunks from mask and get blocks.
        // 2 = Get metadata.
        // 3 = Get block light.
        // 4 = Get sky light.
        // 5 = Get extended block data - This doesn't exist!
        //
        // Fun fact, a mojang dev (forgot who) wanted to do the flattening in 1.8
        // So the extended block data was likely how mojang wanted to get around the 255 block id limit
        // Before they decided to quite using magic values and instead went with the new 1.13 solution
        //
        // That's probably why extended block data exists, although yeah it was never used.
        for (int pass = 0; pass < 5; pass++) {
            for (int ind = 0; ind < 16; ind++) {
                if (primarySet.get(ind)) {
                    if (pass == 0) {
                        expected += 10240;
                        if (sevenExtendedMask.get(ind)) {
                            expected += 2048;
                        }
                    }

                    if (pass == 1) {
                        chunks[ind] = new Chunk_v1_7(sky, sevenExtendedMask.get(ind));
                        ByteArray3d blocks = chunks[ind].getBlocks();
                        System.arraycopy(data, pos, blocks.getData(), 0, blocks.getData().length);
                        pos += blocks.getData().length;
                    }

                    if (pass == 2) {
                        NibbleArray3d metadata = chunks[ind].getMetadata();
                        System.arraycopy(data, pos, metadata.getData(), 0, metadata.getData().length);
                        pos += metadata.getData().length;
                    }

                    if (pass == 3) {
                        NibbleArray3d blocklight = chunks[ind].getBlockLight();
                        System.arraycopy(data, pos, blocklight.getData(), 0, blocklight.getData().length);
                        pos += blocklight.getData().length;
                    }

                    if (pass == 4 && sky) {
                        NibbleArray3d skylight = chunks[ind].getSkyLight();
                        System.arraycopy(data, pos, skylight.getData(), 0, skylight.getData().length);
                        pos += skylight.getData().length;
                    }
                }
            }

            if (pass == 0 && data.length >= expected) {
                sky = true;
            }
        }

        return chunks;
    }
}
