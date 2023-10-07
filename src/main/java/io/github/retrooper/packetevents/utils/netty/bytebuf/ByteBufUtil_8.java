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
import io.netty.buffer.Unpooled;
import io.netty.util.internal.EmptyArrays;

public final class ByteBufUtil_8 implements ByteBufUtil {

    @Override
    public Object buffer() {
        return Unpooled.buffer();
    }

    @Override
    public Object newByteBuf(byte[] data) {
        return Unpooled.wrappedBuffer(data);
    }

    @Override
    public void retain(Object byteBuf) {
        ((ByteBuf) byteBuf).retain();
    }

    @Override
    public void release(Object byteBuf) {
        ((ByteBuf) byteBuf).release();
    }

    @Override
    public byte[] getBytes(Object byteBuf) {
        final ByteBuf bb = (ByteBuf) byteBuf;
        if (bb.refCnt() < 1) {
            return EmptyArrays.EMPTY_BYTES;
        }
        byte[] bytes;
        if (bb.hasArray()) {
            bytes = bb.array();
        } else {
            bytes = new byte[bb.readableBytes()];
            bb.getBytes(bb.readerIndex(), bytes);
        }
        return bytes;
    }

    @Override
    public void setBytes(Object byteBuf, byte[] bytes) {
        final ByteBuf bb = (ByteBuf) byteBuf;
        if (bb.refCnt() < 1) {
            return;
        }
        final int bytesLength = bytes.length;
        if (bb.capacity() < bytesLength) {
            bb.capacity(bytesLength);
        }
        bb.setBytes(0, bytes);
    }


}