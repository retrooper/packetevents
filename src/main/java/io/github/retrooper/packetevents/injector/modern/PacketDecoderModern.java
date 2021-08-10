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
import io.github.retrooper.packetevents.event.impl.PacketDecodeEvent;
import io.github.retrooper.packetevents.packettype.PacketState;
import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufModern;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import org.bukkit.entity.Player;

import java.util.List;

public class PacketDecoderModern extends ByteToMessageDecoder {
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        byteBuf.markReaderIndex();

        byte[] array = new byte[3];

        for (int i = 0; i < array.length; ++i) {
            if (!byteBuf.isReadable()) {
                byteBuf.resetReaderIndex();
                return;
            }

            array[i] = byteBuf.readByte();
            if (array[i] >= 0) {
                ByteBuf wrappedBuffer = Unpooled.wrappedBuffer(array);
                PacketWrapper bufferReader = new PacketWrapper(new ByteBufModern(wrappedBuffer));

                try {

                    int length = bufferReader.readVarInt();
                    if (byteBuf.readableBytes() >= length) {
                        list.add(byteBuf.readBytes(length));
                        return;
                    }

                    byteBuf.resetReaderIndex();
                } finally {
                    wrappedBuffer.release();
                }
                return;
            }
        }

        throw new CorruptedFrameException("length wider than 21-bit");
    }
}
