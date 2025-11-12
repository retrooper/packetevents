/*
 * This file is part of ProtocolSupport - https://github.com/ProtocolSupport/ProtocolSupport
 * Copyright (C) 2021 ProtocolSupport
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.protocol.nbt;

import java.util.Arrays;

public class NBTIntArray extends NBT {

    protected final int[] array;

    public NBTIntArray(int[] array) {
        this.array = array;
    }

    @Override
    public NBTType<NBTIntArray> getType() {
        return NBTType.INT_ARRAY;
    }

    public int[] getValue() {
        return array;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NBTIntArray other = (NBTIntArray) obj;
        return Arrays.equals(array, other.array);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }

    @Override
    public NBTIntArray copy() {
        int[] aint = new int[this.array.length];
        System.arraycopy(this.array, 0, aint, 0, this.array.length);
        return new NBTIntArray(aint);
    }

    @Override
    public String toString() {
        return "IntArray(" + Arrays.toString(array) + ")";
    }
}
