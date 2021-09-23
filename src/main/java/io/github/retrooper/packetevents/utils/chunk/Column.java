/*
 * This class was taken from MCProtocolLib.
 *
 * https://github.com/Steveice10/MCProtocolLib
 */

package io.github.retrooper.packetevents.utils.chunk;

import io.github.retrooper.packetevents.utils.nbt.NBTCompound;

import java.util.Arrays;

public class Column {
    private final int x;
    private final int z;
    private final Chunk[] chunks;
    private final NBTCompound[] tileEntities;
    private final NBTCompound heightMaps;
    private final int[] biomeData;

    /**
     * @deprecated Non-full chunks no longer exist since 1.17.
     */
    @Deprecated
    public Column(int x, int z, Chunk[] chunks, NBTCompound[] tileEntities, NBTCompound heightMaps) {
        this(x, z, chunks, tileEntities, heightMaps, new int[1024]);
    }

    public Column(int x, int z, Chunk[] chunks, NBTCompound[] tileEntities, NBTCompound heightMaps, int[] biomeData) {
        this.x = x;
        this.z = z;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.biomeData = biomeData != null ? Arrays.copyOf(biomeData, biomeData.length) : null;
        this.tileEntities = tileEntities != null ? tileEntities : new NBTCompound[0];
        this.heightMaps = heightMaps;
    }
}
