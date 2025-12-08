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

package com.github.retrooper.packetevents.protocol.world.chunk.palette;

import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Arrays;

/**
 * A palette backed by a map.
 */
public class MapPalette implements Palette {

    private final int bits;
    private final int[] idToState;
    private final Int2IntMap stateToId;
    private int nextId = 0;

    public MapPalette(int bitsPerEntry) {
        this.bits = bitsPerEntry;
        this.idToState = new int[1 << bitsPerEntry];
        this.stateToId = new Int2IntMap(1 << bitsPerEntry);
    }

    @Deprecated
    public MapPalette(int bitsPerEntry, NetStreamInput in) {
        this(bitsPerEntry);

        int paletteLength = in.readVarInt();
        for (int i = 0; i < paletteLength; i++) {
            int state = in.readVarInt();
            this.idToState[i] = state;
            this.stateToId.putIfAbsent(state, i);
        }
        this.nextId = paletteLength;
    }

    public MapPalette(int bitsPerEntry, PacketWrapper<?> wrapper) {
        this(bitsPerEntry);

        int paletteLength = wrapper.readVarInt();
        for (int i = 0; i < paletteLength; i++) {
            int state = wrapper.readVarInt();
            this.idToState[i] = state;
            this.stateToId.putIfAbsent(state, i);
        }
        this.nextId = paletteLength;
    }

    @Override
    public int size() {
        return this.nextId;
    }

    @Override
    public int stateToId(int state) {
        int id = this.stateToId.get(state);
        if (id == -1 && this.size() < this.idToState.length) {
            id = this.nextId++;
            this.idToState[id] = state;
            this.stateToId.put(state, id);
        }
        return id;
    }

    @Override
    public int idToState(int id) {
        if (id >= 0 && id < this.size()) {
            return this.idToState[id];
        } else {
            return 0;
        }
    }

    @Override
    public int getBits() {
        return this.bits;
    }

    /**
     * A simple open-addressing hash map optimized for int keys and values.
     * It uses linear probing and supports a default return value of -1.
     */
    private static class Int2IntMap {

        private static final float LOAD_FACTOR = 0.75f;

        private int[] data;
        private int size;
        private int threshold;

        public Int2IntMap(int expectedSize) {
            int capacity = tableSizeFor(expectedSize);
            this.threshold = (int) (capacity * LOAD_FACTOR);
            this.data = new int[capacity * 2];
            Arrays.fill(this.data, -1);
        }

        private static int tableSizeFor(int cap) {
            int highestOneBit = Integer.highestOneBit(cap - 1);
            return (cap <= 1) ? 1 : (highestOneBit >= 1 << 30) ? 1 << 30 : highestOneBit << 1;
        }

        public int get(int key) {
            int capacity = data.length >> 1;
            int mask = capacity - 1;
            int index = hash(key) & mask;
            while (data[index * 2] != -1) {
                if (data[index * 2] == key) {
                    return data[index * 2 + 1];
                }
                index = (index + 1) & mask;
            }
            return -1;
        }

        public void put(int key, int value) {
            if (size >= threshold) {
                resize((data.length >> 1) * 2);
            }
            int capacity = data.length >> 1;
            int mask = capacity - 1;
            int index = hash(key) & mask;
            while (data[index * 2] != -1) {
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
            while (data[index * 2] != -1) {
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
            Arrays.fill(data, -1);
            size = 0;

            for (int i = 0; i < oldCapacity; i++) {
                int key = oldData[i * 2];
                if (key != -1) {
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
}
