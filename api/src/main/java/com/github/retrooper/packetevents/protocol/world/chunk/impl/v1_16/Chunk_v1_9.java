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

package com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_16;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.PaletteType;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;

public class Chunk_v1_9 implements BaseChunk {
    private static final int AIR = 0;

    private int blockCount;
    private final DataPalette dataPalette;

    private NibbleArray3d blockLight;
    private NibbleArray3d skyLight;

    public Chunk_v1_9(int blockCount, DataPalette dataPalette) {
        this.blockCount = blockCount;
        this.dataPalette = dataPalette;
    }

    // This handles 1.9 through 1.17 chunk data!
    public Chunk_v1_9(NetStreamInput in, boolean hasBlockLight, boolean hasSkyLight) {
        boolean isFourteen = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14);
        boolean isSixteen = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_16);

        // 1.14+ includes block count in chunk data
        if (isFourteen) {
            blockCount = in.readShort();
        }

        if (isSixteen) {
            dataPalette = DataPalette.read(in, PaletteType.CHUNK);
        } else {
            dataPalette = DataPalette.readLegacy(in);
        }

        this.blockLight = hasBlockLight ? new NibbleArray3d(in, 2048) : null;
        this.skyLight = hasSkyLight ? new NibbleArray3d(in, 2048) : null;
    }

    public static void write(NetStreamOutput out, Chunk_v1_9 chunk) {
        boolean isFourteen = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14);

        // 1.14+ includes block count in chunk data
        if (isFourteen) {
            out.writeShort(chunk.blockCount);
        }

        DataPalette.write(out, chunk.dataPalette);

        if (chunk.blockLight != null) {
            out.writeBytes(chunk.blockLight.getData());
        }
        if (chunk.skyLight != null) {
            out.writeBytes(chunk.skyLight.getData());
        }
    }

    public WrappedBlockState get(int x, int y, int z) {
        return WrappedBlockState.getByGlobalId(this.dataPalette.get(x, y, z));
    }

    public void set(int x, int y, int z, int state) {
        int curr = this.dataPalette.set(x, y, z, state);
        if (state != AIR && curr == AIR) {
            this.blockCount++;
        } else if (state == AIR && curr != AIR) {
            this.blockCount--;
        }
    }

    @Override
    public boolean isKnownEmpty() {
        return this.blockCount == 0;
    }
}