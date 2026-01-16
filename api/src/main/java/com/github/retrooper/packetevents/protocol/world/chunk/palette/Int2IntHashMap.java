package com.github.retrooper.packetevents.protocol.world.chunk.palette;

import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;

/**
 * A simple open-addressing hash map optimized for int keys and values.</br>
 * It uses linear probing and supports a default return value of {@link #EMPTY_VALUE}.<br/>
 * Only to be used with non-negative keys and values.
 */
@ApiStatus.Internal
class Int2IntHashMap {

    private static final float LOAD_FACTOR = 0.75f;
    public static final int EMPTY_VALUE = -1;

    private int[] data;
    private int size;
    private int threshold;

    public Int2IntHashMap(int expectedSize) {
        int capacity = tableSizeFor(expectedSize);
        this.threshold = (int) (capacity * LOAD_FACTOR);
        this.data = new int[capacity * 2];
        Arrays.fill(this.data, EMPTY_VALUE);
    }

    private static int tableSizeFor(int cap) {
        if (cap <= 1) return 1;
        return 1 << (32 - Integer.numberOfLeadingZeros(cap - 1));
    }

    public int get(int key) {
        int capacity = data.length >> 1;
        int mask = capacity - 1;
        int index = hash(key) & mask;
        while (data[index * 2] != EMPTY_VALUE) {
            if (data[index * 2] == key) {
                return data[index * 2 + 1];
            }
            index = (index + 1) & mask;
        }
        return EMPTY_VALUE;
    }

    public void put(int key, int value) {
        if (size >= threshold) {
            resize((data.length >> 1) * 2);
        }
        int capacity = data.length >> 1;
        int mask = capacity - 1;
        int index = hash(key) & mask;
        while (data[index * 2] != EMPTY_VALUE) {
            if (data[index * 2] == key) {
                data[index * 2 + 1] = value;
                return;
            }
            index = (index + 1) & mask;
        }
        data[index * 2] = key;
        data[index * 2 + 1] = value;
        size++;
    }

    public void putIfAbsent(int key, int value) {
        if (size >= threshold) {
            resize((data.length >> 1) * 2);
        }
        int capacity = data.length >> 1;
        int mask = capacity - 1;
        int index = hash(key) & mask;
        while (data[index * 2] != EMPTY_VALUE) {
            if (data[index * 2] == key) {
                return;
            }
            index = (index + 1) & mask;
        }
        data[index * 2] = key;
        data[index * 2 + 1] = value;
        size++;
    }

    private void resize(int newCapacity) {
        int[] oldData = data;
        int oldCapacity = oldData.length >> 1;

        threshold = (int) (newCapacity * LOAD_FACTOR);
        data = new int[newCapacity * 2];
        Arrays.fill(data, EMPTY_VALUE);
        size = 0;

        for (int i = 0; i < oldCapacity; i++) {
            int key = oldData[i * 2];
            if (key != EMPTY_VALUE) {
                put(key, oldData[i * 2 + 1]);
            }
        }
    }

    /**
     * MurmurHash3 for better distribution.
     */
    private int hash(int key) {
        key ^= key >>> 16;
        key *= 0x85ebca6b;
        key ^= key >>> 13;
        key *= 0xc2b2ae35;
        key ^= key >>> 16;
        return key;
    }
}
