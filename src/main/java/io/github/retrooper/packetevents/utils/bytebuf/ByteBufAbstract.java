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

package io.github.retrooper.packetevents.utils.bytebuf;


import io.github.retrooper.packetevents.utils.nms.MinecraftReflection;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public interface ByteBufAbstract {
    static ByteBufAbstract generate(@NotNull Object byteBuf) {
        if (MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            return new ByteBufModern(byteBuf);
        }
        else {
            return new ByteBufLegacy(byteBuf);
        }
    }

    Object rawByteBuf();

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

    int getUnsignedShort(int i);

    int getMedium(int i);
    

    int getUnsignedMedium(int i);

    int getInt(int i);

    long getUnsignedInt(int i);

    long getLong(int i);

    char getChar(int i);

    float getFloat(int i);

    double getDouble(int i);

    boolean readBoolean();

    byte readByte();

    short readUnsignedByte();

    short readShort();

    int readUnsignedShort();

    int readMedium();

    int readUnsignedMedium();

    int readInt();

    long readUnsignedInt();

    long readLong();

    char readChar();

    float readFloat();

    double readDouble();

     ByteBufAbstract writeBoolean(boolean a);

    ByteBufAbstract writeByte(int a);

    ByteBufAbstract writeShort(int a);

    ByteBufAbstract writeMedium(int a);

    ByteBufAbstract writeInt(int a);

    ByteBufAbstract writeLong(long a);

    ByteBufAbstract writeChar(int a);

    ByteBufAbstract writeFloat(float a);

    ByteBufAbstract writeDouble(double a);

     ByteBufAbstract copy();

     ByteBufAbstract copy(int a, int b);

     ByteBufAbstract slice();

     ByteBufAbstract slice(int a, int b);

     ByteBufAbstract duplicate();

     int nioBufferCount();

     ByteBuffer nioBuffer();

     ByteBuffer nioBuffer(int a, int b);

     ByteBuffer internalNioBuffer(int a, int b);

     ByteBuffer[] nioBuffers();

     ByteBuffer[] nioBuffers(int a, int b);

     boolean hasArray();

     byte[] array();

     int arrayOffset();

     boolean hasMemoryAddress();

     long memoryAddress();

     int hashCode();

     boolean equals(Object a);

     int compareTo(ByteBufAbstract a);

     ByteBufAbstract retain(int a);

     ByteBufAbstract retain();

     ByteBufAbstract readBytes(int i);

     void readBytes(byte[] bytes);

    ByteBufAbstract getBytes(int readerIndex, byte[] bytes);

    boolean release();

    int refCnt();

    ByteBufAbstract setBytes(int i, byte[] bytes);

    String toString(int i, int j, Charset charset);
}
