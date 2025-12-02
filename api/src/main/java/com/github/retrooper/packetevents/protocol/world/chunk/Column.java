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

/*
 * This class was taken from MCProtocolLib.
 *
 * https://github.com/Steveice10/MCProtocolLib
 */

package com.github.retrooper.packetevents.protocol.world.chunk;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTLongArray;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class Column {
    private final int x;
    private final int z;
    private final boolean fullChunk;
    private final BaseChunk[] chunks;
    private final TileEntity[] tileEntities;
    private final boolean hasHeightmaps;
    private @Nullable NBTCompound heightmapsNbt;
    private @Nullable Map<HeightmapType, long[]> heightmaps;
    private final boolean hasBiomeData;

    private int[] biomeDataInts;
    private byte[] biomeDataBytes;

    public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, TileEntity[] tileEntities, int[] biomeData) {
        this.x = x;
        this.z = z;
        this.fullChunk = fullChunk;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.tileEntities = tileEntities != null ? tileEntities : new TileEntity[0];
        this.hasHeightmaps = false;
        this.heightmapsNbt = new NBTCompound();
        this.hasBiomeData = true;
        this.biomeDataInts = biomeData != null ? Arrays.copyOf(biomeData, biomeData.length) : null;
    }

    public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, TileEntity[] tileEntities) {
        this.x = x;
        this.z = z;
        this.fullChunk = fullChunk;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.tileEntities = tileEntities != null ? tileEntities : new TileEntity[0];
        this.hasHeightmaps = false;
        this.heightmapsNbt = new NBTCompound();
        this.hasBiomeData = false;
        this.biomeDataInts = new int[1024];
    }

    public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, TileEntity[] tileEntities, NBTCompound heightmapsNbt) {
        this.x = x;
        this.z = z;
        this.fullChunk = fullChunk;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.tileEntities = tileEntities != null ? tileEntities : new TileEntity[0];
        this.hasHeightmaps = true;
        this.heightmapsNbt = heightmapsNbt;
        this.hasBiomeData = false;
        this.biomeDataInts = new int[1024];
    }

    /**
     * Added with 1.21.5 because of new heightmaps format
     */
    public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, TileEntity[] tileEntities, Map<HeightmapType, long[]> heightmaps) {
        this.x = x;
        this.z = z;
        this.fullChunk = fullChunk;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.tileEntities = tileEntities != null ? tileEntities : new TileEntity[0];
        this.hasHeightmaps = true;
        this.heightmapsNbt = null;
        this.heightmaps = heightmaps;
        this.hasBiomeData = false;
        this.biomeDataInts = new int[1024];
    }

    public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, TileEntity[] tileEntities, NBTCompound heightmapsNbt, int[] biomeDataInts) {
        this.x = x;
        this.z = z;
        this.fullChunk = fullChunk;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.tileEntities = tileEntities != null ? tileEntities : new TileEntity[0];
        this.hasHeightmaps = true;
        this.heightmapsNbt = heightmapsNbt;
        this.hasBiomeData = true;
        this.biomeDataInts = biomeDataInts != null ? Arrays.copyOf(biomeDataInts, biomeDataInts.length) : null;
    }

    public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, TileEntity[] tileEntities, NBTCompound heightmapsNbt, byte[] biomeData) {
        this.x = x;
        this.z = z;
        this.fullChunk = fullChunk;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.tileEntities = tileEntities != null ? tileEntities : new TileEntity[0];
        this.hasHeightmaps = true;
        this.heightmapsNbt = heightmapsNbt;
        this.hasBiomeData = true;
        this.biomeDataBytes = biomeData != null ? Arrays.copyOf(biomeData, biomeData.length) : null;
    }

    public Column(int chunkX, int chunkZ, boolean fullChunk, BaseChunk[] chunks, TileEntity[] tileEntities, byte[] biomeDataBytes) {
        this.x = chunkX;
        this.z = chunkZ;
        this.fullChunk = fullChunk;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.tileEntities = tileEntities != null ? tileEntities : new TileEntity[0];
        this.hasHeightmaps = false;
        this.heightmapsNbt = new NBTCompound();
        this.hasBiomeData = true;
        this.biomeDataBytes = biomeDataBytes != null ? Arrays.copyOf(biomeDataBytes, biomeDataBytes.length) : null;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public boolean isFullChunk() {
        return fullChunk;
    }

    public BaseChunk[] getChunks() {
        return chunks;
    }

    public TileEntity[] getTileEntities() {
        return tileEntities;
    }

    public boolean hasHeightMaps() {
        return hasHeightmaps;
    }

    /**
     * @deprecated Heightmaps are no longer serialized to nbt as of 1.21.5,
     * use the common method {@link #getHeightmaps()} instead
     */
    @Deprecated
    public NBTCompound getHeightMaps() {
        // convert back to legacy format
        if (this.heightmapsNbt == null) {
            this.heightmapsNbt = new NBTCompound();
            for (Map.Entry<HeightmapType, long[]> entry : this.getHeightmaps().entrySet()) {
                this.heightmapsNbt.setTag(entry.getKey().getSerializationKey(), new NBTLongArray(entry.getValue()));
            }
        }
        return this.heightmapsNbt;
    }

    /**
     * May be empty if heightmaps aren't present; this
     * lazily parses the heightmaps nbt to a map, if below 1.21.5
     */
    public Map<HeightmapType, long[]> getHeightmaps() {
        if (this.heightmaps == null) {
            if (!this.hasHeightmaps || this.heightmapsNbt.isEmpty()) {
                this.heightmaps = Collections.emptyMap();
            } else {
                // parse heightmaps nbt to map
                this.heightmaps = new EnumMap<>(HeightmapType.class);
                for (Map.Entry<String, NBT> tag : this.heightmapsNbt.getTags().entrySet()) {
                    HeightmapType heightmapType = HeightmapType.getHeightmapType(tag.getKey());
                    if (heightmapType != null && tag.getValue() instanceof NBTLongArray) {
                        long[] array = ((NBTLongArray) tag.getValue()).getValue();
                        this.heightmaps.put(heightmapType, array);
                    }
                }
            }
        }
        return this.heightmaps;
    }

    public boolean hasBiomeData() {
        return hasBiomeData;
    }

    public int[] getBiomeDataInts() {
        return biomeDataInts;
    }

    public byte[] getBiomeDataBytes() {
        return biomeDataBytes;
    }
}
