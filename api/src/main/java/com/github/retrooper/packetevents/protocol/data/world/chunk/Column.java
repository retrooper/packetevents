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

/*
 * This class was taken from MCProtocolLib.
 *
 * https://github.com/Steveice10/MCProtocolLib
 */

package com.github.retrooper.packetevents.protocol.data.world.chunk;

import com.github.retrooper.packetevents.protocol.data.nbt.NBTCompound;

import java.util.Arrays;

public class Column {
    private final int x;
    private final int z;
    private final boolean fullChunk;
    private final BaseChunk[] chunks;
    private final NBTCompound[] tileEntities;
    private final boolean hasHeightMaps;
    private final NBTCompound heightMaps;
    private final boolean hasBiomeData;
    private final int[] biomeData;

    public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, NBTCompound[] tileEntities, int[] biomeData) {
        this.x = x;
        this.z = z;
        this.fullChunk = fullChunk;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.tileEntities = tileEntities != null ? tileEntities : new NBTCompound[0];
        this.hasHeightMaps = false;
        this.heightMaps = new NBTCompound();
        this.hasBiomeData = true;
        this.biomeData = biomeData != null ? Arrays.copyOf(biomeData, biomeData.length) : null;
    }

    public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, NBTCompound[] tileEntities) {
        this.x = x;
        this.z = z;
        this.fullChunk = fullChunk;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.tileEntities = tileEntities != null ? tileEntities : new NBTCompound[0];
        this.hasHeightMaps = false;
        this.heightMaps = new NBTCompound();
        this.hasBiomeData = false;
        this.biomeData = new int[1024];
    }

    public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, NBTCompound[] tileEntities, NBTCompound heightMaps) {
        this.x = x;
        this.z = z;
        this.fullChunk = fullChunk;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.tileEntities = tileEntities != null ? tileEntities : new NBTCompound[0];
        this.hasHeightMaps = true;
        this.heightMaps = heightMaps;
        this.hasBiomeData = false;
        this.biomeData = new int[1024];
    }

    public Column(int x, int z, boolean fullChunk, BaseChunk[] chunks, NBTCompound[] tileEntities, NBTCompound heightMaps, int[] biomeData) {
        this.x = x;
        this.z = z;
        this.fullChunk = fullChunk;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.tileEntities = tileEntities != null ? tileEntities : new NBTCompound[0];
        this.hasHeightMaps = true;
        this.heightMaps = heightMaps;
        this.hasBiomeData = true;
        this.biomeData = biomeData != null ? Arrays.copyOf(biomeData, biomeData.length) : null;
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

    public NBTCompound[] getTileEntities() {
        return tileEntities;
    }

    public boolean hasHeightMaps() {
        return hasHeightMaps;
    }

    public NBTCompound getHeightMaps() {
        return heightMaps;
    }

    public boolean hasBiomeData() {
        return hasBiomeData;
    }

    public int[] getBiomeData() {
        return biomeData;
    }
}
