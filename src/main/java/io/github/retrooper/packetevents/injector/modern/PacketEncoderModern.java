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

package io.github.retrooper.packetevents.injector.modern;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.protocol.ConnectionState;
import io.github.retrooper.packetevents.utils.bytebuf.ByteBufModern;
import io.github.retrooper.packetevents.utils.wrapper.PacketWrapperUtils;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.internal.EmptyArrays;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PacketEncoderModern extends MessageToByteEncoder<ByteBuf> {
    public volatile Player player;

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf bb, ByteBuf byteBuf) throws Exception {
        int firstReaderIndex = bb.readerIndex();
        PacketSendEvent packetSendEvent = new PacketSendEvent(ctx.channel(), player, bb);
        int readerIndex = bb.readerIndex();
        PacketEvents.get().getEventManager().callEvent(packetSendEvent, () -> {
            bb.readerIndex(readerIndex);
        });
        bb.readerIndex(firstReaderIndex);
        byteBuf.writeBytes(bb);
    }

    /*
     @Override
     public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf bb = (ByteBuf) msg;
        int firstReaderIndex = bb.readerIndex();
        PacketSendEvent packetSendEvent = new PacketSendEvent(ctx.channel(), player, bb);
        int readerIndex = bb.readerIndex();
        PacketEvents.get().getEventManager().callEvent(packetSendEvent, () -> {
            bb.readerIndex(readerIndex);
        });

        bb.readerIndex(firstReaderIndex);

        ctx.write(bb);

    }*/




    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    public static void writeBytes(ByteBuf buf, byte[] bytes, int buffer) {
        int length = bytes.length;
        correctSize(buf, length, buffer);
        if (length > 0) {
            buf.writeBytes(bytes);
        }
    }

    public static void correctSize(ByteBuf buf, int bytes, int buffer) {
        int capacity = buf.capacity();
        int available = capacity - (buf.writerIndex() + bytes);

        if (available < 0) {
            buf.capacity(capacity - available + buffer);
        }
    }

    public static byte[] getBytes(ByteBuf buffer, int bytes) {
        if (bytes == 0) {
            return EmptyArrays.EMPTY_BYTES;
        }

        return getBytes0(buffer, bytes);
    }

    public static byte[] getBytesAndRelease(ByteBuf buffer) {
        final byte[] bytes = getBytes(buffer, buffer.readableBytes());
        buffer.release();
        return bytes;
    }

    /**
     * Copied from netty to allow the usage of older netty versions:
     * <p>
     * https://github.com/netty/netty/blob/4.1/buffer/src/main/java/io/netty/buffer/ByteBufUtil.java
     */
    private static byte[] getBytes0(ByteBuf buffer, int length) {
        final int start = buffer.readerIndex();
        final int capacity = buffer.capacity();

        if (isOutOfBounds(start, length, capacity))
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length
                    + ") <= buf.capacity(" + capacity + ')');

        if (buffer.hasArray()) {
            final int baseOffset = buffer.arrayOffset() + start;
            final byte[] bytes = buffer.array();
            if (/*copy || */baseOffset != 0 || length != bytes.length) {
                return Arrays.copyOfRange(bytes, baseOffset, baseOffset + length);
            }
            return bytes;
        }

        final byte[] bytes = new byte[length];
        buffer.getBytes(start, bytes);
        return bytes;
    }

    /**
     * Copied from netty to allow the usage of older netty versions:
     * <p>
     * https://github.com/netty/netty/blob/4.1/common/src/main/java/io/netty/util/internal/MathUtil.java
     */
    private static boolean isOutOfBounds(int index, int length, int capacity) {
        return (index | length | capacity | (index + length) | (capacity - (index + length))) < 0;
    }
}
