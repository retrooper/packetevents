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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18.Chunk_v1_18;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;
import com.github.retrooper.packetevents.protocol.world.chunk.storage.BaseStorage;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

import java.util.BitSet;

public class ChunkReader_v1_18 implements ChunkReader {

    @ApiStatus.Internal
    public static int getMojangZeroByteSuffixLength(BaseChunk[] chunks) {
        // work around https://bugs.mojang.com/browse/MC-296121
        int mojangPleaseFixThisZeroByteSuffixLength = 0;
        for (BaseChunk chunk : chunks) {
            BaseStorage chunkStorage = ((Chunk_v1_18) chunk).getChunkData().storage;
            int chunkStorageLen = ByteBufHelper.getByteSize(chunkStorage != null ? chunkStorage.getData().length : 0);
            BaseStorage biomeStorage = ((Chunk_v1_18) chunk).getBiomeData().storage;
            int biomeStorageLen = ByteBufHelper.getByteSize(biomeStorage != null ? biomeStorage.getData().length : 0);
            mojangPleaseFixThisZeroByteSuffixLength += chunkStorageLen + biomeStorageLen;
        }
        return mojangPleaseFixThisZeroByteSuffixLength;
    }

    @Override
    public BaseChunk[] read(
            DimensionType dimensionType, BitSet chunkMask, BitSet secondaryChunkMask, boolean fullChunk,
            boolean hasBlockLight, boolean hasSkyLight, int chunkSize, int arrayLength, PacketWrapper<?> wrapper
    ) {
        int ri = ByteBufHelper.readerIndex(wrapper.buffer);
        BaseChunk[] chunks = new BaseChunk[chunkSize];
        for (int i = 0; i < chunkSize; ++i) {
            chunks[i] = Chunk_v1_18.read(wrapper);
        }
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_6)
                && wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)
                // viaversion doesn't add this zero-byte-prefix; only skip it if we are missing bytes
                && ByteBufHelper.readerIndex(wrapper.buffer) - ri < arrayLength) {
            ByteBufHelper.skipBytes(wrapper.buffer, getMojangZeroByteSuffixLength(chunks));
        }
        return chunks;
    }
}
