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

package io.github.retrooper.packetevents.utils.netty.buffer;

import com.github.retrooper.packetevents.netty.buffer.ByteBufHandler;
import net.minecraft.util.io.netty.buffer.ByteBuf;

public class ByteBufHandlerLegacyImpl implements ByteBufHandler {
    @Override
    public int readerIndex(Object buffer) {
        return ((ByteBuf)buffer).readerIndex();
    }

    @Override
    public Object readerIndex(Object buffer, int readerIndex) {
        return ((ByteBuf)buffer).readerIndex(readerIndex);
    }

    @Override
    public int writerIndex(Object buffer) {
        return ((ByteBuf)buffer).writerIndex();
    }

    @Override
    public Object writerIndex(Object buffer, int writerIndex) {
        return ((ByteBuf)buffer).writerIndex(writerIndex);
    }

    @Override
    public int readableBytes(Object buffer) {
        return ((ByteBuf)buffer).readableBytes();
    }

    @Override
    public int writableBytes(Object buffer) {
        return ((ByteBuf)buffer).writableBytes();
    }

    @Override
    public Object clear(Object buffer) {
        return ((ByteBuf)buffer).clear();
    }

    @Override
    public byte readByte(Object buffer) {
        return ((ByteBuf)buffer).readByte();
    }

    @Override
    public short readShort(Object buffer) {
        return ((ByteBuf)buffer).readShort();
    }

    @Override
    public int readInt(Object buffer) {
        return ((ByteBuf)buffer).readInt();
    }

    @Override
    public long readUnsignedInt(Object buffer) {
        return ((ByteBuf)buffer).readUnsignedInt();
    }

    @Override
    public long readLong(Object buffer) {
        return ((ByteBuf)buffer).readLong();
    }

    @Override
    public Object copy(Object buffer) {
        return ((ByteBuf)buffer).copy();
    }

    @Override
    public Object duplicate(Object buffer) {
        return ((ByteBuf)buffer).duplicate();
    }

    @Override
    public boolean hasArray(Object buffer) {
        return ((ByteBuf)buffer).hasArray();
    }

    @Override
    public byte[] array(Object buffer) {
        return ((ByteBuf)buffer).array();
    }

    @Override
    public Object retain(Object buffer) {
        return ((ByteBuf)buffer).retain();
    }

    @Override
    public Object readBytes(Object buffer, int length) {
        return ((ByteBuf)buffer).readBytes(length);
    }

    @Override
    public void readBytes(Object buffer, byte[] bytes) {
         ((ByteBuf)buffer).readBytes(bytes);
    }

    @Override
    public boolean release(Object buffer) {
        return ((ByteBuf)buffer).release();
    }

    @Override
    public int refCnt(Object buffer) {
        return ((ByteBuf)buffer).refCnt();
    }

    @Override
    public Object skipBytes(Object buffer, int length) {
        return ((ByteBuf)buffer).skipBytes(length);
    }
}
