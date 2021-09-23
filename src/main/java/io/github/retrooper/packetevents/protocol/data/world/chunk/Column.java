/*
 * This class was taken from MCProtocolLib.
 *
 * https://github.com/Steveice10/MCProtocolLib
 */

package io.github.retrooper.packetevents.protocol.data.world.chunk;

import io.github.retrooper.packetevents.protocol.data.nbt.NBTCompound;

import java.util.Arrays;

public class Column {
    private final int x;
    private final int z;
    private final Chunk[] chunks;
    private final NBTCompound[] tileEntities;
    private final NBTCompound heightMaps;
    private final int[] biomeData;
    private final boolean fullChunk;

    public Column(int x, int z, Chunk[] chunks, NBTCompound[] tileEntities, NBTCompound heightMaps) {
        this.x = x;
        this.z = z;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.biomeData = new int[1024];
        this.tileEntities = tileEntities != null ? tileEntities : new NBTCompound[0];
        this.heightMaps = heightMaps;
        this.fullChunk = false;
    }

    public Column(int x, int z, Chunk[] chunks, NBTCompound[] tileEntities, NBTCompound heightMaps, int[] biomeData) {
        this.x = x;
        this.z = z;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.biomeData = biomeData != null ? Arrays.copyOf(biomeData, biomeData.length) : null;
        this.tileEntities = tileEntities != null ? tileEntities : new NBTCompound[0];
        this.heightMaps = heightMaps;
        this.fullChunk = true;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public Chunk[] getChunks() {
        return chunks;
    }

    public NBTCompound[] getTileEntities() {
        return tileEntities;
    }

    public NBTCompound getHeightMaps() {
        return heightMaps;
    }

    public int[] getBiomeData() {
        return biomeData;
    }

    public boolean isFullChunk() {
        return fullChunk;
    }
}
