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

package com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamInputWrapper;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutputWrapper;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.PaletteType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class Chunk_v1_18 implements BaseChunk {

    private static final int AIR = 0;

    private int blockCount;
    private final DataPalette chunkData;
    private final DataPalette biomeData;

    public Chunk_v1_18() {
        this.chunkData = PaletteType.CHUNK.create();
        this.biomeData = PaletteType.BIOME.create();
    }

    public Chunk_v1_18(int blockCount, DataPalette chunkData, DataPalette biomeData) {
        this.blockCount = blockCount;
        this.chunkData = chunkData;
        this.biomeData = biomeData;
    }

    public static Chunk_v1_18 read(PacketWrapper<?> wrapper) {
        boolean paletteLengthPrefix = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5);
        return read(new NetStreamInputWrapper(wrapper), paletteLengthPrefix);
    }

    /**
     * @deprecated use {@link #read(PacketWrapper)} instead
     */
    @Deprecated
    public static Chunk_v1_18 read(NetStreamInput in) {
        return read(in, true);
    }

    /**
     * @deprecated use {@link #read(PacketWrapper)} instead
     */
    @Deprecated
    public static Chunk_v1_18 read(NetStreamInput in, boolean paletteLengthPrefix) {
        int blockCount = in.readShort();
        DataPalette chunkPalette = DataPalette.read(in, PaletteType.CHUNK,
                true, paletteLengthPrefix);
        DataPalette biomePalette = DataPalette.read(in, PaletteType.BIOME,
                true, paletteLengthPrefix);
        return new Chunk_v1_18(blockCount, chunkPalette, biomePalette);
    }

    public static void write(PacketWrapper<?> wrapper, Chunk_v1_18 section) {
        boolean paletteLengthPrefix = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5);
        write(new NetStreamOutputWrapper(wrapper), section, paletteLengthPrefix);
    }

    /**
     * @deprecated use {@link #write(PacketWrapper, Chunk_v1_18)} instead
     */
    @Deprecated
    public static void write(NetStreamOutput out, Chunk_v1_18 section) {
        write(out, section, true);
    }

    /**
     * @deprecated use {@link #write(PacketWrapper, Chunk_v1_18)} instead
     */
    @Deprecated
    public static void write(NetStreamOutput out, Chunk_v1_18 section, boolean paletteLengthPrefix) {
        out.writeShort(section.blockCount);
        DataPalette.write(out, section.chunkData, paletteLengthPrefix);
        DataPalette.write(out, section.biomeData, paletteLengthPrefix);
    }

    @Override
    public int getBlockId(int x, int y, int z) {
        return this.chunkData.get(x, y, z);
    }

    @Override
    public void set(int x, int y, int z, int state) {
        int curr = this.chunkData.set(x, y, z, state);
        if (state != AIR && curr == AIR) {
            this.blockCount++;
        } else if (state == AIR && curr != AIR) {
            this.blockCount--;
        }
    }

    @Override
    public boolean isEmpty() {
        return this.blockCount == 0;
    }

    public int getBlockCount() {
        return blockCount;
    }

    public void setBlockCount(int blockCount) {
        this.blockCount = blockCount;
    }

    public DataPalette getChunkData() {
        return chunkData;
    }

    public DataPalette getBiomeData() {
        return biomeData;
    }
}
