/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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
package com.github.retrooper.packetevents.util;

import java.util.Arrays;

public class BinaryNBTCompound {

    private final byte[] data;
    private final int hash;

    public BinaryNBTCompound(byte[] data) {
        this.data = data;
        if (data[0] != 10 || data[data.length - 1] != 0) {
            throw new IllegalArgumentException("Data is not a valid NBT compound");
        }
        this.hash = calculateHash(data);
    }

    public byte[] getData() {
        return data;
    }

    private int calculateHash(byte[] data) {
        return Arrays.hashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinaryNBTCompound) {
            BinaryNBTCompound other = (BinaryNBTCompound) obj;
            byte[] otherData = other.data;

            if (otherData.length != data.length) return false;

            // skip first and last bits as they will always be 10 and 0 respectively
            for (int i = 1; i < data.length - 1; i++) {
                if (data[i] != otherData[i]) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        return Arrays.toString(data) + " hash: " + hash;
    }
}
