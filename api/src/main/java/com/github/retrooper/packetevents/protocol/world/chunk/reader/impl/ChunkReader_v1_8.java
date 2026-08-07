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

import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.NetworkChunkData;
import com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_8.Chunk_v1_8;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.BitSet;

public class ChunkReader_v1_8 implements ChunkReader {

    private static final int SECTION_COUNT = 16;
    private static final int BLOCK_COUNT = 4096;
    private static final int BLOCK_DATA_LENGTH = BLOCK_COUNT * Short.BYTES;
    private static final int LIGHT_DATA_LENGTH = BLOCK_COUNT / 2;
    private static final int BIOME_DATA_LENGTH = 16 * 16;

    @Override
    public BaseChunk[] read(
            DimensionType dimensionType, BitSet chunkMask, BitSet secondaryChunkMask, boolean fullChunk,
            boolean hasBlockLight, boolean hasSkyLight, int chunkSize, int arrayLength, PacketWrapper<?> wrapper
    ) {
        int populatedSections = 0;
        for (int index = 0; index < SECTION_COUNT; index++) {
            if (chunkMask.get(index)) {
                populatedSections++;
            }
        }

        int sectionDataLength = populatedSections * (BLOCK_DATA_LENGTH + LIGHT_DATA_LENGTH);
        int expectedWithoutSkyLight = sectionDataLength + (fullChunk ? BIOME_DATA_LENGTH : 0);
        // Bulk packets specify skylight; regular packets infer it from the payload length.
        boolean skyLight = hasBlockLight || arrayLength > expectedWithoutSkyLight && hasSkyLight;
        int requiredSectionDataLength = sectionDataLength
                + (skyLight ? populatedSections * LIGHT_DATA_LENGTH : 0);
        if (arrayLength < requiredSectionDataLength) {
            throw new IllegalArgumentException("1.8 chunk data is shorter than its section mask requires ("
                    + arrayLength + " < " + requiredSectionDataLength + ")");
        }

        Chunk_v1_8[] chunks = new Chunk_v1_8[SECTION_COUNT];
        // Reuse one buffer for little-endian block data.
        byte[] encodedBlocks = populatedSections == 0 ? null : new byte[BLOCK_DATA_LENGTH];
        ShortBuffer decodedBlocks = encodedBlocks == null ? null
                : ByteBuffer.wrap(encodedBlocks).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();

        for (int index = 0; index < SECTION_COUNT; index++) {
            if (chunkMask.get(index)) {
                Chunk_v1_8 chunk = new Chunk_v1_8(skyLight);
                chunks[index] = chunk;
                ByteBufHelper.readBytes(wrapper.buffer, encodedBlocks);
                short[] blocks = chunk.getBlocks().getData();
                decodedBlocks.position(0);
                decodedBlocks.get(blocks, 0, blocks.length);
            }
        }

        for (int index = 0; index < SECTION_COUNT; index++) {
            Chunk_v1_8 chunk = chunks[index];
            if (chunk != null) {
                ByteBufHelper.readBytes(wrapper.buffer, chunk.getBlockLight().getData());
            }
        }

        if (skyLight) {
            for (int index = 0; index < SECTION_COUNT; index++) {
                Chunk_v1_8 chunk = chunks[index];
                if (chunk != null) {
                    ByteBufHelper.readBytes(wrapper.buffer, chunk.getSkyLight().getData());
                }
            }
        }

        return chunks;
    }

    @Deprecated
    public static NetworkChunkData chunksToData(Chunk_v1_8[] chunks, byte[] biomes) {
        boolean fullChunk = biomes != null;
        int chunkMask = calculateChunkMask(chunks, fullChunk);
        boolean sky = hasSkyLight(chunks, chunkMask);
        byte[] data = new byte[calculateDataLength(chunks, biomes, chunkMask)];
        int pos = 0;
        ShortBuffer blockData = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();

        for (int index = 0; index < chunks.length; index++) {
            if ((chunkMask & 1 << index) != 0) {
                short[] blocks = chunks[index].getBlocks().getData();
                blockData.position(pos / Short.BYTES);
                blockData.put(blocks, 0, blocks.length);
                pos += blocks.length * Short.BYTES;
            }
        }

        for (int index = 0; index < chunks.length; index++) {
            if ((chunkMask & 1 << index) != 0) {
                byte[] blockLight = chunks[index].getBlockLight().getData();
                System.arraycopy(blockLight, 0, data, pos, blockLight.length);
                pos += blockLight.length;
            }
        }

        for (int index = 0; index < chunks.length; index++) {
            if ((chunkMask & 1 << index) != 0) {
                NibbleArray3d skyLight = chunks[index].getSkyLight();
                if (skyLight != null) {
                    byte[] skyLightData = skyLight.getData();
                    System.arraycopy(skyLightData, 0, data, pos, skyLightData.length);
                    pos += skyLightData.length;
                }
            }
        }

        if (fullChunk) {
            System.arraycopy(biomes, 0, data, pos, biomes.length);
        }

        return new NetworkChunkData(chunkMask, fullChunk, sky, data);
    }

    @ApiStatus.Internal
    public static int calculateChunkMask(Chunk_v1_8[] chunks, boolean fullChunk) {
        int chunkMask = 0;
        for (int index = 0; index < chunks.length; index++) {
            Chunk_v1_8 chunk = chunks[index];
            if (chunk != null && (!fullChunk || !chunk.isEmpty())) {
                chunkMask |= 1 << index;
            }
        }
        return chunkMask;
    }

    @ApiStatus.Internal
    public static int calculateDataLength(Chunk_v1_8[] chunks, byte[] biomes, int chunkMask) {
        int length = biomes == null ? 0 : biomes.length;
        for (int index = 0; index < chunks.length; index++) {
            if ((chunkMask & 1 << index) != 0) {
                Chunk_v1_8 chunk = chunks[index];
                length += chunk.getBlocks().getData().length * Short.BYTES;
                length += chunk.getBlockLight().getData().length;
                if (chunk.getSkyLight() != null) {
                    length += chunk.getSkyLight().getData().length;
                }
            }
        }
        return length;
    }

    @ApiStatus.Internal
    public static boolean hasSkyLight(Chunk_v1_8[] chunks, int chunkMask) {
        for (int index = 0; index < chunks.length; index++) {
            if ((chunkMask & 1 << index) != 0 && chunks[index].getSkyLight() != null) {
                return true;
            }
        }
        return false;
    }

    @ApiStatus.Internal
    public static void writeChunkData(
            PacketWrapper<?> wrapper, Chunk_v1_8[] chunks, byte[] biomes, int chunkMask, int dataLength
    ) {
        ensureWritable(wrapper, dataLength);

        int largestBlockDataLength = 0;
        for (int index = 0; index < chunks.length; index++) {
            if ((chunkMask & 1 << index) != 0) {
                largestBlockDataLength = Math.max(largestBlockDataLength,
                        chunks[index].getBlocks().getData().length * Short.BYTES);
            }
        }

        byte[] encodedBlocks = largestBlockDataLength == 0 ? null : new byte[largestBlockDataLength];
        ShortBuffer blockData = encodedBlocks == null ? null
                : ByteBuffer.wrap(encodedBlocks).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        for (int index = 0; index < chunks.length; index++) {
            if ((chunkMask & 1 << index) != 0) {
                short[] blocks = chunks[index].getBlocks().getData();
                blockData.position(0);
                blockData.put(blocks, 0, blocks.length);
                ByteBufHelper.writeBytes(wrapper.buffer, encodedBlocks, 0, blocks.length * Short.BYTES);
            }
        }

        for (int index = 0; index < chunks.length; index++) {
            if ((chunkMask & 1 << index) != 0) {
                ByteBufHelper.writeBytes(wrapper.buffer, chunks[index].getBlockLight().getData());
            }
        }

        for (int index = 0; index < chunks.length; index++) {
            if ((chunkMask & 1 << index) != 0 && chunks[index].getSkyLight() != null) {
                ByteBufHelper.writeBytes(wrapper.buffer, chunks[index].getSkyLight().getData());
            }
        }

        if (biomes != null) {
            ByteBufHelper.writeBytes(wrapper.buffer, biomes);
        }
    }

    @ApiStatus.Internal
    public static void ensureWritable(PacketWrapper<?> wrapper, int additionalBytes) {
        int writerIndex = ByteBufHelper.writerIndex(wrapper.buffer);
        int requiredCapacity = Math.addExact(writerIndex, additionalBytes);
        if (requiredCapacity > ByteBufHelper.capacity(wrapper.buffer)) {
            ByteBufHelper.capacity(wrapper.buffer, requiredCapacity);
        }
    }

}
