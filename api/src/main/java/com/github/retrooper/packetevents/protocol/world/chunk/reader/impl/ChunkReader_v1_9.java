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
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_12.Chunk_v1_12;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_15.Chunk_v1_15;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;

import java.io.ByteArrayInputStream;
import java.util.BitSet;

public class ChunkReader_v1_9 implements ChunkReader {
    private static boolean V_1_13_OR_NEWER;
    private static boolean OLDER_THAN_V_1_14;
    private static boolean CHECKED_VERSION;

    @Override
    public BaseChunk[] read(BitSet set, int chunkSize, byte[] data) {
        if (!CHECKED_VERSION) {
            V_1_13_OR_NEWER = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13);
            OLDER_THAN_V_1_14 = PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_14);
            CHECKED_VERSION = true;
        }
        boolean isFlattened = V_1_13_OR_NEWER;

        NetStreamInput dataIn = new NetStreamInput(new ByteArrayInputStream(data));


        BaseChunk[] chunks = new BaseChunk[chunkSize];

        for (int index = 0; index < chunks.length; ++index) {
            if (set.get(index)) {
                chunks[index] = isFlattened ? Chunk_v1_15.read(dataIn) : new Chunk_v1_12(dataIn);

                // Advance the data past the blocklight and skylight bytes
                if (OLDER_THAN_V_1_14) {
                    dataIn.readBytes(4096);
                }
            }
        }

        return chunks;
    }
}
