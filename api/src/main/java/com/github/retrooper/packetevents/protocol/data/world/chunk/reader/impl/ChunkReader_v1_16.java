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

package com.github.retrooper.packetevents.protocol.data.world.chunk.reader.impl;

import com.github.retrooper.packetevents.protocol.data.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.data.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.data.world.chunk.impl.v1_16.Chunk_v1_16;
import com.github.retrooper.packetevents.protocol.data.world.chunk.reader.ChunkReader;

import java.io.ByteArrayInputStream;
import java.util.BitSet;

public class ChunkReader_v1_16 implements ChunkReader {
    @Override
    public BaseChunk[] read(BitSet set, int chunkSize, byte[] data) {
        NetStreamInput dataIn = new NetStreamInput(new ByteArrayInputStream(data));
        BaseChunk[] chunks = new BaseChunk[chunkSize];
        for (int index = 0; index < chunks.length; ++index) {
            if (set.get(index)) {
                chunks[index] = Chunk_v1_16.read(dataIn);
            }
        }
        return chunks;
    }
}
