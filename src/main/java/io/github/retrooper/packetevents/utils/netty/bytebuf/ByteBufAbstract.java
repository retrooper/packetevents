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

package io.github.retrooper.packetevents.utils.netty.bytebuf;

public interface ByteBufAbstract {
    int capacity();

    ByteBufAbstract capacity(int capacity);

    int maxCapacity();

    boolean isDirect();

    int readerIndex();

    ByteBufAbstract readerIndex(int readerIndex);

    int writerIndex();

    ByteBufAbstract writerIndex(int writerIndex);

    ByteBufAbstract setIndex(int a, int b);

    int readableBytes();

    int writableBytes();

    int maxWritableBytes();

    boolean isReadable();

    boolean isReadable(int a);

    boolean isWritable();

    boolean isWritable(int var1);

    ByteBufAbstract clear();

    boolean getBoolean(int i);

    byte getByte(int i);

    short getUnsignedByte(int i);

    short getShort(int i);

    short getShortLE(int i);

    int getUnsignedShort(int i);

    int getUnsignedShortLE(int i);

    int getMedium(int i);

    int getMediumLE(int i);

    int getUnsignedMedium(int i);

    int getUnsignedMediumLE(int i);

    int getInt(int i);

    int getIntLE(int i);

    long getUnsignedInt(int i);

    long getUnsignedIntLE(int i);

    long getLong(int i);

    long getLongLE(int i);

    char getChar(int i);

    float getFloat(int i);

    float getFloatLE(int i);

    double getDouble(int i);

    double getDoubleLE(int i);

    //TODO add setters, add reading/writing without index methods, implement them
}
