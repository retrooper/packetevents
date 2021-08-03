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

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

public class ByteBufModern implements ByteBufAbstract {
    private final ByteBuf byteBuf;

    public ByteBufModern(Object byteBuf) {
        this.byteBuf = (ByteBuf) byteBuf;
    }

    @Override
    public Object rawByteBuf() {
        return byteBuf;
    }

    @Override
    public int capacity() {
        return byteBuf.capacity();
    }

    @Override
    public ByteBufAbstract capacity(int capacity) {
        return new ByteBufModern(byteBuf.capacity(capacity));
    }

    @Override
    public int maxCapacity() {
        return byteBuf.maxCapacity();
    }

    @Override
    public boolean isDirect() {
        return byteBuf.isDirect();
    }

    @Override
    public int readerIndex() {
        return byteBuf.readerIndex();
    }

    @Override
    public ByteBufAbstract readerIndex(int readerIndex) {
        return new ByteBufModern(byteBuf.readerIndex(readerIndex));
    }

    @Override
    public int writerIndex() {
        return byteBuf.writerIndex();
    }

    @Override
    public ByteBufAbstract writerIndex(int writerIndex) {
        return new ByteBufModern(byteBuf.writerIndex(writerIndex));
    }

    @Override
    public ByteBufAbstract setIndex(int a, int b) {
        return new ByteBufModern(byteBuf.setIndex(a, b));
    }

    @Override
    public int readableBytes() {
        return byteBuf.readableBytes();
    }

    @Override
    public int writableBytes() {
        return byteBuf.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return byteBuf.maxWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return byteBuf.isReadable();
    }

    @Override
    public boolean isReadable(int a) {
        return byteBuf.isReadable(a);
    }

    @Override
    public boolean isWritable() {
        return byteBuf.isWritable();
    }

    @Override
    public boolean isWritable(int var1) {
        return byteBuf.isWritable(var1);
    }

    @Override
    public ByteBufAbstract clear() {
        return new ByteBufModern(byteBuf.clear());
    }

    @Override
    public boolean getBoolean(int i) {
        return byteBuf.getBoolean(i);
    }

    @Override
    public byte getByte(int i) {
        return byteBuf.getByte(i);
    }

    @Override
    public short getUnsignedByte(int i) {
        return byteBuf.getUnsignedByte(i);
    }

    @Override
    public short getShort(int i) {
        return byteBuf.getShort(i);
    }


    @Override
    public int getUnsignedShort(int i) {
        return byteBuf.getUnsignedShort(i);
    }

    @Override
    public int getMedium(int i) {
        return byteBuf.getMedium(i);
    }

    @Override
    public int getUnsignedMedium(int i) {
        return byteBuf.getUnsignedMedium(i);
    }

    @Override
    public int getInt(int i) {
        return byteBuf.getInt(i);
    }

    @Override
    public long getUnsignedInt(int i) {
        return byteBuf.getUnsignedInt(i);
    }

    @Override
    public long getLong(int i) {
        return byteBuf.getLong(i);
    }


    @Override
    public char getChar(int i) {
        return byteBuf.getChar(i);
    }

    @Override
    public float getFloat(int i) {
        return byteBuf.getFloat(i);
    }

    @Override
    public double getDouble(int i) {
        return byteBuf.getDouble(i);
    }

    @Override
    public boolean readBoolean() {
        return byteBuf.readBoolean();
    }

    @Override
    public byte readByte() {
        return byteBuf.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return byteBuf.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return byteBuf.readShort();
    }

    @Override
    public int readUnsignedShort() {
        return byteBuf.readUnsignedShort();
    }


    @Override
    public int readMedium() {
        return byteBuf.readMedium();
    }

    @Override
    public int readUnsignedMedium() {
        return byteBuf.readUnsignedMedium();
    }

    @Override
    public int readInt() {
        return byteBuf.readInt();
    }

    @Override
    public long readUnsignedInt() {
        return byteBuf.readUnsignedInt();
    }

    @Override
    public long readLong() {
        return byteBuf.readLong();
    }

    @Override
    public char readChar() {
        return byteBuf.readChar();
    }

    @Override
    public float readFloat() {
        return byteBuf.readFloat();
    }

    @Override
    public double readDouble() {
        return byteBuf.readDouble();
    }

    @Override
    public ByteBufAbstract writeBoolean(boolean a) {
        return new ByteBufModern(byteBuf.writeBoolean(a));
    }

    @Override
    public ByteBufAbstract writeByte(int a) {
        return new ByteBufModern(byteBuf.writeByte(a));
    }

    @Override
    public ByteBufAbstract writeShort(int a) {
        return new ByteBufModern(byteBuf.writeShort(a));
    }

    @Override
    public ByteBufAbstract writeMedium(int a) {
        return new ByteBufModern(byteBuf.writeMedium(a));
    }

    @Override
    public ByteBufAbstract writeInt(int a) {
        return new ByteBufModern(byteBuf.writeInt(a));
    }

    @Override
    public ByteBufAbstract writeLong(long a) {
        return new ByteBufModern(byteBuf.writeLong(a));
    }

    @Override
    public ByteBufAbstract writeChar(int a) {
        return new ByteBufModern(byteBuf.writeChar(a));
    }

    @Override
    public ByteBufAbstract writeFloat(float a) {
        return new ByteBufModern(byteBuf.writeFloat(a));
    }

    @Override
    public ByteBufAbstract writeDouble(double a) {
        return new ByteBufModern(byteBuf.writeDouble(a));
    }

    @Override
    public ByteBufAbstract copy() {
        return new ByteBufModern(byteBuf.copy());
    }

    @Override
    public ByteBufAbstract copy(int a, int b) {
        return new ByteBufModern(byteBuf.copy(a, b));
    }

    @Override
    public ByteBufAbstract slice() {
        return new ByteBufModern(byteBuf.slice());
    }

    @Override
    public ByteBufAbstract slice(int a, int b) {
        return new ByteBufModern(byteBuf.slice(a, b));
    }

    @Override
    public ByteBufAbstract duplicate() {
        return new ByteBufModern(byteBuf.duplicate());
    }

    @Override
    public int nioBufferCount() {
        return byteBuf.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return byteBuf.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int a, int b) {
        return byteBuf.nioBuffer(a, b);
    }

    @Override
    public ByteBuffer internalNioBuffer(int a, int b) {
        return byteBuf.internalNioBuffer(a, b);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return byteBuf.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int a, int b) {
        return byteBuf.nioBuffers(a, b);
    }

    @Override
    public boolean hasArray() {
        return byteBuf.hasArray();
    }

    @Override
    public byte[] array() {
        return byteBuf.array();
    }

    @Override
    public int arrayOffset() {
        return byteBuf.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return byteBuf.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return byteBuf.memoryAddress();
    }

    @Override
    public int compareTo(ByteBufAbstract a) {
        return byteBuf.compareTo((ByteBuf) a.rawByteBuf());
    }

    @Override
    public ByteBufAbstract retain(int a) {
        return new ByteBufModern(byteBuf.retain(a));
    }

    @Override
    public ByteBufAbstract retain() {
        return new ByteBufModern(byteBuf.retain());
    }
}
