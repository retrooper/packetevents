/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.utils.netty.bytebuf;

import io.netty.buffer.Unpooled;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.util.internal.EmptyArrays;

public final class ByteBufUtil_7 implements ByteBufUtil {

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
