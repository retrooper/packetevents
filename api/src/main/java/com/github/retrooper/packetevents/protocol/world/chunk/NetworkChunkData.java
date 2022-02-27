package com.github.retrooper.packetevents.protocol.world.chunk;

public class NetworkChunkData {
    private final int mask;
    private int extendedChunkMask; // 1.7 only
    private final boolean fullChunk;
    private final boolean sky;
    private byte[] data;

    public NetworkChunkData(int mask, boolean fullChunk, boolean sky, byte[] data) {
        this.mask = mask;
        this.fullChunk = fullChunk;
        this.sky = sky;
        this.data = data;
    }

    public NetworkChunkData(int chunkMask, int extendedChunkMask, boolean fullChunk, boolean sky, byte[] data) {
        this(chunkMask, fullChunk, sky, data);
        this.extendedChunkMask = extendedChunkMask;
    }

    public int getMask() {
        return this.mask;
    }

    public int getExtendedChunkMask() {
        return this.extendedChunkMask;
    }

    public boolean isFullChunk() {
        return this.fullChunk;
    }

    public boolean hasSkyLight() {
        return this.sky;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
