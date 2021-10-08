/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.data.nbt;

public class NBTFloat extends NBTNumber {

    protected final float value;

    public NBTFloat(float value) {
        this.value = value;
    }

    @Override
    public NBTType<NBTFloat> getType() {
        return NBTType.FLOAT;
    }

    @Override
    public byte getAsByte() {
        return (byte) value;
    }

    @Override
    public short getAsShort() {
        return (short) value;
    }

    @Override
    public int getAsInt() {
        return (int) value;
    }

    @Override
    public long getAsLong() {
        return (long) value;
    }

    @Override
    public float getAsFloat() {
        return value;
    }

    @Override
    public double getAsDouble() {
        return value;
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
        NBTFloat other = (NBTFloat) obj;
        return Float.floatToIntBits(value) == Float.floatToIntBits(other.value);
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }

}
