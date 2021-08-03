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

public class ByteBuf_8 implements ByteBufAbstract {
    private ByteBuf byteBuf;
    public ByteBuf_8(Object byteBuf) {
        this.byteBuf = (ByteBuf) byteBuf;
    }

    @Override
    public int capacity() {
        return byteBuf.capacity();
    }

    @Override
    public ByteBufAbstract capacity(int capacity) {
        return new ByteBuf_8(byteBuf.capacity(capacity));
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
        return new ByteBuf_8(byteBuf.readerIndex(readerIndex));
    }

    @Override
    public int writerIndex() {
        return byteBuf.writerIndex();
    }

    @Override
    public ByteBufAbstract writerIndex(int writerIndex) {
        return new ByteBuf_8(byteBuf.writerIndex(writerIndex));
    }

    @Override
    public ByteBufAbstract setIndex(int a, int b) {
        return new ByteBuf_8(byteBuf.setIndex(a, b));
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
        return new ByteBuf_8(byteBuf.clear());
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
    public short getShortLE(int i) {
        return byteBuf.getShortLE(i);
    }

    @Override
    public int getUnsignedShort(int i) {
        return byteBuf.getUnsignedShort(i);
    }

    @Override
    public int getUnsignedShortLE(int i) {
        return byteBuf.getUnsignedShortLE(i);
    }

    @Override
    public int getMedium(int i) {
        return byteBuf.getMedium(i);
    }

    @Override
    public int getMediumLE(int i) {
        return byteBuf.getMediumLE(i);
    }

    @Override
    public int getUnsignedMedium(int i) {
        return byteBuf.getUnsignedMedium(i);
    }

    @Override
    public int getUnsignedMediumLE(int i) {
        return byteBuf.getUnsignedMediumLE(i);
    }

    @Override
    public int getInt(int i) {
        return byteBuf.getInt(i);
    }

    @Override
    public int getIntLE(int i) {
        return byteBuf.getIntLE(i);
    }

    @Override
    public long getUnsignedInt(int i) {
        return byteBuf.getUnsignedInt(i);
    }

    @Override
    public long getUnsignedIntLE(int i) {
        return byteBuf.getUnsignedIntLE(i);
    }

    @Override
    public long getLong(int i) {
        return byteBuf.getLong(i);
    }

    @Override
    public long getLongLE(int i) {
        return byteBuf.getLongLE(i);
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
    public float getFloatLE(int i) {
        return byteBuf.getFloatLE(i);
    }

    @Override
    public double getDouble(int i) {
        return byteBuf.getDouble(i);
    }

    @Override
    public double getDoubleLE(int i) {
        return byteBuf.getDoubleLE(i);
    }
}
