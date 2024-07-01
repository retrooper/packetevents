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

package com.github.retrooper.packetevents.protocol.world.chunk.reader.impl;

import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.ByteArray3d;
import com.github.retrooper.packetevents.protocol.world.chunk.NetworkChunkData;
import com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_7.Chunk_v1_7;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;

import java.util.BitSet;

public class ChunkReader_v1_7 implements ChunkReader {
    @Override
    public BaseChunk[] read(DimensionType dimensionType, BitSet primarySet, BitSet sevenExtendedMask, boolean fullChunk, boolean hasSkyLight, boolean checkForSky, int chunkSize, byte[] data, NetStreamInput dataIn) {
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

    public static NetworkChunkData chunksToData(Chunk_v1_7[] chunks, byte[] biomes) {
        int chunkMask = 0;
        int extendedChunkMask = 0;
        boolean fullChunk = biomes != null;
        boolean sky = false;
        int length = fullChunk ? biomes.length : 0;
        byte[] data = null;
        int pos = 0;
        // 0 = Determine length and masks.
        // 1 = Add blocks.
        // 2 = Add metadata.
        // 3 = Add block light.
        // 4 = Add sky light.
        // 5 = Add extended block data.
        for(int pass = 0; pass < 6; pass++) {
            for(int ind = 0; ind < chunks.length; ++ind) {
                Chunk_v1_7 chunk = chunks[ind];
                if(chunk != null && (!fullChunk || !chunk.isEmpty())) {
                    if(pass == 0) {
                        chunkMask |= 1 << ind;
                        if(chunk.getExtendedBlocks() != null) {
                            extendedChunkMask |= 1 << ind;
                        }

                        length += chunk.getBlocks().getData().length;
                        length += chunk.getMetadata().getData().length;
                        length += chunk.getBlockLight().getData().length;
                        if(chunk.getSkyLight() != null) {
                            length += chunk.getSkyLight().getData().length;
                        }

                        if(chunk.getExtendedBlocks() != null) {
                            length += chunk.getExtendedBlocks().getData().length;
                        }
                    }

                    if(pass == 1) {
                        ByteArray3d blocks = chunk.getBlocks();
                        System.arraycopy(blocks.getData(), 0, data, pos, blocks.getData().length);
                        pos += blocks.getData().length;
                    }

                    if(pass == 2) {
                        byte meta[] = chunk.getMetadata().getData();
                        System.arraycopy(meta, 0, data, pos, meta.length);
                        pos += meta.length;
                    }

                    if(pass == 3) {
                        byte blocklight[] = chunk.getBlockLight().getData();
                        System.arraycopy(blocklight, 0, data, pos, blocklight.length);
                        pos += blocklight.length;
                    }

                    if(pass == 4 && chunk.getSkyLight() != null) {
                        byte skylight[] = chunk.getSkyLight().getData();
                        System.arraycopy(skylight, 0, data, pos, skylight.length);
                        pos += skylight.length;
                        sky = true;
                    }

                    if(pass == 5 && chunk.getExtendedBlocks() != null) {
                        byte extended[] = chunk.getExtendedBlocks().getData();
                        System.arraycopy(extended, 0, data, pos, extended.length);
                        pos += extended.length;
                    }
                }
            }

            if(pass == 0) {
                data = new byte[length];
            }
        }

        // Add biomes.
        if(fullChunk) {
            System.arraycopy(biomes, 0, data, pos, biomes.length);
            pos += biomes.length;
        }

        return new NetworkChunkData(chunkMask, extendedChunkMask, fullChunk, sky, data);
    }
}
