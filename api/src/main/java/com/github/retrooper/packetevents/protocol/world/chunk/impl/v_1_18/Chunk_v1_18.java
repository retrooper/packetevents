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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutputWrapper;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.PaletteType;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Chunk_v1_18 implements BaseChunk {

    private final ClientVersion version;

    private int blockCount;
    /**
     * @versions 26.1+
     */
    private int fluidCount;
    private final DataPalette chunkData;
    private final DataPalette biomeData;

    @Deprecated
    public Chunk_v1_18() {
        this(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    public Chunk_v1_18(ClientVersion version) {
        this.version = version;
        this.chunkData = PaletteType.CHUNK.create();
        this.biomeData = PaletteType.BIOME.create();
    }

    /**
     * @versions -1.21.11
     */
    @ApiStatus.Obsolete
    @Deprecated
    public Chunk_v1_18(int blockCount, DataPalette chunkData, DataPalette biomeData) {
        this(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), blockCount, 0, chunkData, biomeData);
    }

    /**
     * @versions 26.1+
     */
    @Deprecated
    public Chunk_v1_18(
            int blockCount, int fluidCount,
            DataPalette chunkData, DataPalette biomeData
    ) {
        this(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), blockCount, fluidCount, chunkData, biomeData);
    }

    /**
     * @versions -1.21.11
     */
    @ApiStatus.Obsolete
    public Chunk_v1_18(ClientVersion version, int blockCount, DataPalette chunkData, DataPalette biomeData) {
        this(version, blockCount, 0, chunkData, biomeData);
    }

    /**
     * @versions 26.1+
     */
    public Chunk_v1_18(
            ClientVersion version, int blockCount, int fluidCount,
            DataPalette chunkData, DataPalette biomeData
    ) {
        this.version = version;
        this.blockCount = blockCount;
        this.fluidCount = fluidCount;
        this.chunkData = chunkData;
        this.biomeData = biomeData;
    }

    public static Chunk_v1_18 read(PacketWrapper<?> wrapper) {
        ClientVersion version = wrapper.getServerVersion().toClientVersion();
        int blockCount = wrapper.readShort();
        int fluidCount = version.isNewerThanOrEquals(ClientVersion.V_26_1) ? wrapper.readShort() : 0;
        DataPalette chunkPalette = DataPalette.read(wrapper, PaletteType.CHUNK);
        DataPalette biomePalette = DataPalette.read(wrapper, PaletteType.BIOME);
        return new Chunk_v1_18(version, blockCount, fluidCount, chunkPalette, biomePalette);
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
        return read(in, paletteLengthPrefix, false);
    }

    /**
     * @deprecated use {@link #read(PacketWrapper)} instead
     */
    @Deprecated
    public static Chunk_v1_18 read(NetStreamInput in, boolean paletteLengthPrefix, boolean hasFluidCount) {
        return read(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), in, paletteLengthPrefix, hasFluidCount);
    }

    /**
     * @deprecated use {@link #read(PacketWrapper)} instead
     */
    @Deprecated
    public static Chunk_v1_18 read(ClientVersion version, NetStreamInput in, boolean paletteLengthPrefix, boolean hasFluidCount) {
        int blockCount = in.readShort();
        int fluidCount = hasFluidCount ? in.readShort() : 0;
        DataPalette chunkPalette = DataPalette.read(in, PaletteType.CHUNK,
                true, paletteLengthPrefix);
        DataPalette biomePalette = DataPalette.read(in, PaletteType.BIOME,
                true, paletteLengthPrefix);
        return new Chunk_v1_18(version, blockCount, fluidCount, chunkPalette, biomePalette);
    }

    public static void write(PacketWrapper<?> wrapper, Chunk_v1_18 section) {
        boolean paletteLengthPrefix = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5);
        boolean hasFluidCount = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_1);
        write(new NetStreamOutputWrapper(wrapper), section, paletteLengthPrefix, hasFluidCount);
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
        write(out, section, paletteLengthPrefix, false);
    }

    /**
     * @deprecated use {@link #write(PacketWrapper, Chunk_v1_18)} instead
     */
    @Deprecated
    public static void write(NetStreamOutput out, Chunk_v1_18 section, boolean paletteLengthPrefix, boolean hasFluidCount) {
        out.writeShort(section.blockCount);
        if (hasFluidCount) {
            out.writeShort(section.fluidCount);
        }
        DataPalette.write(out, section.chunkData, paletteLengthPrefix);
        DataPalette.write(out, section.biomeData, paletteLengthPrefix);
    }

    @Override
    public int getBlockId(int x, int y, int z) {
        return this.chunkData.get(x, y, z);
    }

    @Override
    public void set(int x, int y, int z, WrappedBlockState state) {
        int curr = this.chunkData.getAndSet(x, y, z, state.getGlobalId());
        WrappedBlockState currState = WrappedBlockState.getByGlobalId(this.version, curr, false);

        // track block count
        if (currState.getType().isAir()) {
            if (!state.getType().isAir()) {
                this.blockCount++;
            }
        } else if (state.getType().isAir()) {
            this.blockCount--;
        }
        if (this.version.isNewerThanOrEquals(ClientVersion.V_26_1)) {
            // track fluid count
            if (!currState.isFluid()) {
                if (state.isFluid()) {
                    this.fluidCount++;
                }
            } else if (!state.isFluid()) {
                this.fluidCount--;
            }
        }
    }

    @Override
    public void set(int x, int y, int z, int combinedID) {
        this.set(x, y, z, WrappedBlockState.getByGlobalId(this.version, combinedID, false));
    }

    @Override
    public void set(ClientVersion version, int x, int y, int z, int combinedID) {
        this.set(x, y, z, WrappedBlockState.getByGlobalId(version, combinedID, false));
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

    /**
     * @versions 26.1+
     */
    public int getFluidCount() {
        return this.fluidCount;
    }

    /**
     * @versions 26.1+
     */
    public void setFluidCount(int fluidCount) {
        this.fluidCount = fluidCount;
    }

    public DataPalette getChunkData() {
        return chunkData;
    }

    public DataPalette getBiomeData() {
        return biomeData;
    }
}
