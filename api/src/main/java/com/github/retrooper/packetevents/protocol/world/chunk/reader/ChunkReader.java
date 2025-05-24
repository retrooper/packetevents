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

package com.github.retrooper.packetevents.protocol.world.chunk.reader;

import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.io.ByteArrayInputStream;
import java.util.BitSet;

public interface ChunkReader {

    @Deprecated
    default BaseChunk[] read(
            Dimension dimension, BitSet chunkMask, BitSet secondaryChunkMask, boolean fullChunk,
            boolean hasBlockLight, boolean hasSkyLight, int chunkSize, byte[] data, NetStreamInput dataIn
    ) {
        // backwards compat
        DimensionType dimensionType = dimension.asDimensionType(null, null);
        PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(
                UnpooledByteBufAllocationHelper.wrappedBuffer(data));
        try {
            return this.read(dimensionType, chunkMask, secondaryChunkMask, fullChunk,
                    hasBlockLight, hasSkyLight, chunkSize, data.length, wrapper);
        } finally {
            ByteBufHelper.release(wrapper.buffer);
        }
    }

    @Deprecated
    default BaseChunk[] read(
            DimensionType dimensionType, BitSet chunkMask, BitSet secondaryChunkMask, boolean fullChunk,
            boolean hasBlockLight, boolean hasSkyLight, int chunkSize, byte[] data, NetStreamInput dataIn
    ) {
        // backwards compat
        Dimension dimension = Dimension.fromDimensionType(dimensionType, null, null);
        return this.read(dimension, chunkMask, secondaryChunkMask, fullChunk,
                hasBlockLight, hasSkyLight, chunkSize, data, dataIn);
    }

    default BaseChunk[] read(
            DimensionType dimensionType, BitSet chunkMask, BitSet secondaryChunkMask,
            boolean fullChunk, boolean hasBlockLight, boolean hasSkyLight, int chunkSize,
            int arrayLength, PacketWrapper<?> wrapper
    ) {
        // backwards compat
        byte[] data = wrapper.readByteArrayOfSize(arrayLength);
        NetStreamInput dataIn = new NetStreamInput(new ByteArrayInputStream(data));
        return this.read(dimensionType, chunkMask, secondaryChunkMask, fullChunk,
                hasBlockLight, hasSkyLight, chunkSize, data, dataIn);
    }
}
