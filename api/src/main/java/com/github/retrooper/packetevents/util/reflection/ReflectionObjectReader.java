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

public interface ReflectionObjectReader {

    boolean readBoolean(int index);

    byte readByte(int index);

    short readShort(int index);

    int readInt(int index);

    long readLong(int index);

    float readFloat(int index);

    double readDouble(int index);

    boolean[] readBooleanArray(int index);

    byte[] readByteArray(int index);

    short[] readShortArray(int index);

    int[] readIntArray(int index);

    long[] readLongArray(int index);

    float[] readFloatArray(int index);

    double[] readDoubleArray(int index);

    String[] readStringArray(int index);

    String readString(int index);

    <T> T readObject(int index, Class<? extends T> type);

    <T> T[] readObjectArray(int index, Class<? extends T> type);

    Enum<?> readEnumConstant(int index, Class<? extends Enum<?>> type);

    Object readAnyObject(int index);
}