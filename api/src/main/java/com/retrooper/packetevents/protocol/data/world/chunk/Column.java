/*
 * This class was taken from MCProtocolLib.
 *
 * https://github.com/Steveice10/MCProtocolLib
 */

package com.retrooper.packetevents.protocol.data.world.chunk;

import com.retrooper.packetevents.protocol.data.nbt.NBTCompound;

import java.util.Arrays;

public class Column {
    private final int x;
    private final int z;
    private final boolean fullChunk;
    private final Chunk[] chunks;
    private final NBTCompound[] tileEntities;
    private final boolean hasHeightMaps;
    private final NBTCompound heightMaps;
    private final boolean hasBiomeData;
    private final int[] biomeData;

    public Column(int x, int z, boolean fullChunk, Chunk[] chunks, NBTCompound[] tileEntities, int[] biomeData) {
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

    public Column(int x, int z, boolean fullChunk, Chunk[] chunks, NBTCompound[] tileEntities) {
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

    public Column(int x, int z, boolean fullChunk, Chunk[] chunks, NBTCompound[] tileEntities, NBTCompound heightMaps) {
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

    public Column(int x, int z, boolean fullChunk, Chunk[] chunks, NBTCompound[] tileEntities, NBTCompound heightMaps, int[] biomeData) {
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

    public Chunk[] getChunks() {
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
