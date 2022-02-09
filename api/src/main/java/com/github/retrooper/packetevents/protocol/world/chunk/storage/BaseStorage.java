package com.github.retrooper.packetevents.protocol.world.chunk.storage;

public abstract class BaseStorage {
    public abstract long[] getData();

    public abstract int getBitsPerEntry();

    abstract int getSize();

    public abstract int get(int index);

    public abstract void set(int index, int value);
}
