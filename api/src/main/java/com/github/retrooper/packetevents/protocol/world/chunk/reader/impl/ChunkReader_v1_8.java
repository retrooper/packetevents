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
import com.github.retrooper.packetevents.protocol.world.chunk.NetworkChunkData;
import com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;
import com.github.retrooper.packetevents.protocol.world.chunk.ShortArray3d;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_8.Chunk_v1_8;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.BitSet;

public class ChunkReader_v1_8 implements ChunkReader {

    @Override
    public BaseChunk[] read(DimensionType dimensionType, BitSet set, BitSet sevenExtendedMask, boolean fullChunk, boolean hasSkyLight, boolean checkForSky, int chunkSize, byte[] data, NetStreamInput dataIn) {
        Chunk_v1_8[] chunks = new Chunk_v1_8[16];
        int pos = 0;
        int expected = fullChunk ? 256 : 0; // 256 if full chunk for the biome data, always sent if full chunk
        boolean sky = false;

        ShortBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();

        // 0 = Calculate expected length and determine if the packet has skylight.
        // 1 = Create chunks from mask and get blocks.
        // 2 = Get block light.
        // 3 = Get sky light.
        for (int pass = 0; pass < 4; pass++) {
            for (int ind = 0; ind < 16; ind++) {
                if (set.get(ind)) {
                    if (pass == 0) {
                        // Block length + Blocklight length
                        expected += (4096 * 2) + 2048;
                    }

                    if (pass == 1) {
                        chunks[ind] = new Chunk_v1_8(sky || hasSkyLight);
                        ShortArray3d blocks = chunks[ind].getBlocks();
                        buf.position(pos / 2);
                        buf.get(blocks.getData(), 0, blocks.getData().length);
                        pos += blocks.getData().length * 2;
                    }

                    if (pass == 2) {
                        NibbleArray3d blocklight = chunks[ind].getBlockLight();
                        System.arraycopy(data, pos, blocklight.getData(), 0, blocklight.getData().length);
                        pos += blocklight.getData().length;
                    }

                    if (pass == 3 && (sky || hasSkyLight)) {
                        NibbleArray3d skylight = chunks[ind].getSkyLight();
                        System.arraycopy(data, pos, skylight.getData(), 0, skylight.getData().length);
                        pos += skylight.getData().length;
                    }
                }
            }

            if (pass == 0 && data.length > expected) {
                // If we have more data than blocks and blocklight combined, there must be skylight data as well.
                sky = checkForSky;
            }
        }

        return chunks;
    }

    public static NetworkChunkData chunksToData(Chunk_v1_8[] chunks, byte[] biomes) {
        int chunkMask = 0;
        boolean fullChunk = biomes != null;
        boolean sky = false;
        int length = fullChunk ? biomes.length : 0;
        byte[] data = null;
        int pos = 0;
        ShortBuffer buf = null;

        // 0 = Determine length and masks.
        // 1 = Add blocks.
        // 2 = Add block light.
        // 3 = Add sky light.
        for (int pass = 0; pass < 4; pass++) {
            for (int ind = 0; ind < chunks.length; ++ind) {
                Chunk_v1_8 chunk = chunks[ind];
                if (chunk != null && (!fullChunk || !chunk.isEmpty())) {
                    if (pass == 0) {
                        chunkMask |= 1 << ind;
                        length += chunk.getBlocks().getData().length * 2;
                        length += chunk.getBlockLight().getData().length;
                        if (chunk.getSkyLight() != null) {
                            length += chunk.getSkyLight().getData().length;
                        }
                    }

                    if (pass == 1) {
                        short blocks[] = chunk.getBlocks().getData();
                        buf.position(pos / 2);
                        buf.put(blocks, 0, blocks.length);
                        pos += blocks.length * 2;
                    }

                    if (pass == 2) {
                        byte blocklight[] = chunk.getBlockLight().getData();
                        System.arraycopy(blocklight, 0, data, pos, blocklight.length);
                        pos += blocklight.length;
                    }

                    if (pass == 3 && chunk.getSkyLight() != null) {
                        byte skylight[] = chunk.getSkyLight().getData();
                        System.arraycopy(skylight, 0, data, pos, skylight.length);
                        pos += skylight.length;
                        sky = true;
                    }
                }
            }

            if (pass == 0) {
                data = new byte[length];
                buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
            }
        }

        // Add biomes.
        if (fullChunk) {
            System.arraycopy(biomes, 0, data, pos, biomes.length);
            pos += biomes.length;
        }

        return new NetworkChunkData(chunkMask, fullChunk, sky, data);
    }
}

