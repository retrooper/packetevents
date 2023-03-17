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

package com.github.retrooper.packetevents.protocol.world.chunk.storage;

import java.util.Arrays;

public class LegacyFlexibleStorage extends BaseStorage {
    private final long[] data;
    private final int bitsPerEntry;
    private final int size;
    private final long maxEntryValue;

    public LegacyFlexibleStorage(int bitsPerEntry, int size) {
        this(bitsPerEntry, new long[roundToNearest(size * bitsPerEntry, 64) / 64]);
    }

    public LegacyFlexibleStorage(int bitsPerEntry, long[] data) {
        if (bitsPerEntry < 4) {
            bitsPerEntry = 4;
        }

        this.bitsPerEntry = bitsPerEntry;
        this.data = Arrays.copyOf(data, data.length);

        this.size = this.data.length * 64 / this.bitsPerEntry;
        this.maxEntryValue = (1L << this.bitsPerEntry) - 1;
    }

    private static int roundToNearest(int value, int roundTo) {
        if (roundTo == 0) {
            return 0;
        } else if (value == 0) {
            return roundTo;
        } else {
            if (value < 0) {
                roundTo *= -1;
            }

            int remainder = value % roundTo;
            return remainder != 0 ? value + roundTo - remainder : value;
        }
    }

    @Override
    public int get(int index) {
        int bitIndex = index * this.bitsPerEntry;
        int startIndex = bitIndex / 64;
        int endIndex = ((index + 1) * this.bitsPerEntry - 1) / 64;
        int startBitSubIndex = bitIndex % 64;
        if (startIndex == endIndex) {
            return (int) (this.data[startIndex] >>> startBitSubIndex & this.maxEntryValue);
        } else {
            int endBitSubIndex = 64 - startBitSubIndex;
            return (int) ((this.data[startIndex] >>> startBitSubIndex | this.data[endIndex] << endBitSubIndex) & this.maxEntryValue);
        }
    }

    @Override
    public void set(int index, int value) {
        int bitIndex = index * this.bitsPerEntry;
        int startIndex = bitIndex / 64;
        int endIndex = ((index + 1) * this.bitsPerEntry - 1) / 64;
        int startBitSubIndex = bitIndex % 64;
        this.data[startIndex] = this.data[startIndex] & ~(this.maxEntryValue << startBitSubIndex) | ((long) value & this.maxEntryValue) << startBitSubIndex;
        if (startIndex != endIndex) {
            int endBitSubIndex = 64 - startBitSubIndex;
            int j1 = this.bitsPerEntry - endBitSubIndex;
            this.data[endIndex] = this.data[endIndex] >>> j1 << j1 | ((long) value & this.maxEntryValue) >> endBitSubIndex;
        }
    }

    @Override
    public long[] getData() {
        return data;
    }

    @Override
    public int getBitsPerEntry() {
        return bitsPerEntry;
    }

    @Override
    public int getSize() {
        return size;
    }

    public long getMaxEntryValue() {
        return maxEntryValue;
    }
}
