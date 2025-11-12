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

package com.github.retrooper.packetevents.util.reflection;

public interface ReflectionObjectWriter {
    void writeBoolean(int index, boolean value);

    void writeByte(int index, byte value);

    void writeShort(int index, short value);

    void writeInt(int index, int value);

    void writeLong(int index, long value);

    void writeFloat(int index, float value);

    void writeDouble(int index, double value);

    void writeString(int index, String value);

    //ARRAYS
    void writeBooleanArray(int index, boolean[] array);

    void writeByteArray(int index, byte[] value);

    void writeShortArray(int index, short[] value);

    void writeIntArray(int index, int[] value);

    void writeLongArray(int index, long[] value);

    void writeFloatArray(int index, float[] value);

    void writeDoubleArray(int index, double[] value);

    void writeStringArray(int index, String[] value);

    void writeObject(int index, Object object);

    void writeAnyObject(int index, Object value);

    void writeEnumConstant(int index, Enum<?> enumConstant);
}