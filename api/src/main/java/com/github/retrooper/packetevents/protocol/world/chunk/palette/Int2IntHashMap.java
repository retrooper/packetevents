package com.github.retrooper.packetevents.protocol.world.chunk.palette;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;

/**
 * A simple open-addressing hash map optimized for int keys and values.</br>
 * It uses linear probing and supports a default return value of {@link #EMPTY_VALUE}.<br/>
 * Only to be used with non-negative keys and values.
 * <p>
 * Greatly inspired by <a href="https://github.com/aeron-io/agrona/blob/5512dda83e1b1a8dc38a6286e440237b1a7e480c/agrona/src/main/java/org/agrona/collections/Int2IntHashMap.java">Int2IntHashMap.java</a>, licensed under the terms of the Apache License 2.0.
 */
@NullMarked
@ApiStatus.Internal
final class Int2IntHashMap {

    private static final float LOAD_FACTOR = 0.75f;
    public static final int EMPTY_VALUE = -1;

    private long[] data;
    private int size;
    private int threshold;

    public Int2IntHashMap(int expectedSize) {
        int capacity = tableSizeFor(expectedSize);
        this.threshold = (int) (capacity * LOAD_FACTOR);
        this.data = new long[capacity];
        Arrays.fill(this.data, EMPTY_VALUE);
    }

    private static int tableSizeFor(int cap) {
        if (cap <= 1) return 1;
        return 1 << (32 - Integer.numberOfLeadingZeros(cap - 1));
    }

    public int get(int key) {
        int mask = this.data.length - 1;
        int index = this.hash(key) & mask;
        long entry;
        while ((entry = this.data[index]) != EMPTY_VALUE) {
            if ((int) (entry & 0xFFFFFFFFL) == key) {
                return (int) (entry >>> 32);
            }
            // collision, advance
            index = (index + 1) & mask;
        }
        return EMPTY_VALUE;
    }

    public void put(int key, int value) {
        if (this.size >= this.threshold) {
            this.resize(this.data.length << 1);
        }
        long[] data = this.data;
        int mask = data.length - 1;
        int index = this.hash(key) & mask;
        long entry;
        while ((entry = data[index]) != EMPTY_VALUE) {
            if ((int) (entry & 0xFFFFFFFFL) == key) {
                // replace existing entry
                data[index] = (entry & 0xFFFFFFFFL) | ((value & 0xFFFFFFFFL) << 32);
                return;
            }
            // collision, advance
            index = (index + 1) & mask;
        }
        // insert new entry at empty index
        data[index] = (key & 0xFFFFFFFFL) | ((value & 0xFFFFFFFFL) << 32);
        this.size++;
    }

    public void putIfAbsent(int key, int value) {
        if (this.size >= this.threshold) {
            this.resize(this.data.length << 1);
        }
        long[] data = this.data;
        int mask = data.length - 1;
        int index = this.hash(key) & mask;
        long entry;
        while ((entry = data[index]) != EMPTY_VALUE) {
            if ((int) (entry & 0xFFFFFFFFL) == key) {
                return; // already present, don't insert
            }
            // collision, advance
            index = (index + 1) & mask;
        }
        // insert new entry at empty index
        data[index] = (key & 0xFFFFFFFFL) | ((value & 0xFFFFFFFFL) << 32);
        this.size++;
    }

    private void resize(int newCapacity) {
        long[] oldData = this.data;

        // construct new table
        this.threshold = (int) (newCapacity * LOAD_FACTOR);
        long[] data = new long[newCapacity];
        Arrays.fill(data, EMPTY_VALUE);
        this.data = data;

        // put old table back into new table
        int mask = newCapacity - 1;
        for (int i = 0, len = oldData.length; i < len; i++) {
            long entry = oldData[i];
            if (entry == EMPTY_VALUE) {
                continue;
            }
            int key = (int) (entry & 0xFFFFFFFFL);
            int index = this.hash(key) & mask;
            // we can skip checking for matches against this same key as
            // this is a fresh table being built and there should be no replacements, only collisions
            while (data[index] != EMPTY_VALUE) {
                index = (index + 1) & mask;
            }
            data[index] = entry;
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
