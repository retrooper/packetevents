package com.github.retrooper.packetevents.protocol.world.chunk.palette;

public enum PaletteType {
    BIOME(1, 3, 64),
    CHUNK(4, 8, 4096);

    private final int minBitsPerEntry;
    private final int maxBitsPerEntry;
    private final int storageSize;

    PaletteType(int minBitsPerEntry, int maxBitsPerEntry, int storageSize) {
        this.minBitsPerEntry = minBitsPerEntry;
        this.maxBitsPerEntry = maxBitsPerEntry;
        this.storageSize = storageSize;
    }

    public int getMaxBitsPerEntry() {
        return maxBitsPerEntry;
    }

    public int getMinBitsPerEntry() {
        return minBitsPerEntry;
    }

    public int getStorageSize() {
        return storageSize;
    }
}