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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_16.Chunk_v1_9;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;

import java.io.IOException;
import java.util.BitSet;

public class ChunkReader_v1_9 implements ChunkReader {

    @Override
    public BaseChunk[] read(BitSet set, BitSet sevenExtendedMask, boolean fullChunk, boolean hasBlockLight, boolean hasSkyLight, int chunkSize, byte[] data, NetStreamInput dataIn) {
        BaseChunk[] chunks = new BaseChunk[chunkSize];

        Throwable throwable = null;

        try {
            for (int index = 0; index < chunks.length; ++index) {
                if (set.get(index)) {
                    chunks[index] = new Chunk_v1_9(dataIn, hasBlockLight, hasSkyLight);
                }
            }
        } catch (Throwable e) {
            throwable = e;
        }

        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14)) return chunks;

        try {
            // Unfortunately, this is needed to detect whether the chunks contain skylight or not.
            // Yes, this hack is required all the way from 1.9 through 1.13!
            //
            // Minimum is more than 256 bytes when it is a full chunks for biome data
            // if not a full chunk, any leftover data means it has skylight
            int minimum = fullChunk ? 256 : 0;
            if ((dataIn.available() > minimum || throwable != null ) && !hasSkyLight) {
                dataIn.reset();
                return read(set, sevenExtendedMask, fullChunk, hasBlockLight, true, chunkSize, data, dataIn);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return chunks;
    }
}
