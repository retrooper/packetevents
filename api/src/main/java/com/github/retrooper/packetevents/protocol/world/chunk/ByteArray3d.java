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

package com.github.retrooper.packetevents.protocol.world.chunk;

import java.util.Arrays;

public class ByteArray3d {
    private final byte[] data;

    public ByteArray3d(int size) {
        this.data = new byte[size];
    }

    public ByteArray3d(byte[] array) {
        this.data = array;
    }

    public byte[] getData() {
        return this.data;
    }

    public int get(int x, int y, int z) {
        return this.data[y << 8 | z << 4 | x] & 0xFF;
    }

    public void set(int x, int y, int z, int val) {
        this.data[y << 8 | z << 4 | x] = (byte) val;
    }

    public void fill(int val) {
        Arrays.fill(this.data, (byte) val);
    }
}