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

package com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_16;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.PaletteType;

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
        } else {
            blockCount = Integer.MAX_VALUE;
        }

        if (isSixteen) {
            // singleton palette got added with 1.18 which isn't supported by this chunk section implementation
            dataPalette = DataPalette.read(in, PaletteType.CHUNK, false);
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

    @Override
    public int getBlockId(int x, int y, int z) {
        return this.dataPalette.get(x, y, z);
    }

    public void set(int x, int y, int z, int state) {
        int curr = this.dataPalette.set(x, y, z, state);
        // Pre-1.14 we don't get block counts
        if (blockCount == Integer.MAX_VALUE) return;
        if (state != AIR && curr == AIR) {
            this.blockCount++;
        } else if (state == AIR && curr != AIR) {
            this.blockCount--;
        }
    }

    @Override
    public boolean isEmpty() {
        // Pre-1.14 we have to calculate the value
        if (blockCount == Integer.MAX_VALUE) {
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        if (this.dataPalette.get(x, y, z) != AIR) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        // 1.14+, we can rely on the value
        return this.blockCount == 0;
    }

    public NibbleArray3d getSkyLight() {
        return this.skyLight;
    }

    public void setSkyLight(NibbleArray3d skyLight) {
        this.skyLight = skyLight;
    }

    public NibbleArray3d getBlockLight() {
        return this.blockLight;
    }

    public void setBlockLight(NibbleArray3d blockLight) {
        this.blockLight = blockLight;
    }
}
