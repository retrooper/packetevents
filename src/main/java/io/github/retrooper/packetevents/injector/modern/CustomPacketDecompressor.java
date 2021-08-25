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

import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufUtil;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelHandlerContextAbstract;
import io.github.retrooper.packetevents.utils.nms.MinecraftReflection;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class CustomPacketDecompressor {
    private static Class<?> BYTE_TO_MESSAGE_DECODER;
    private static final Inflater INFLATER = new Inflater();
    private static final int THRESHOLD = 256;

    public static ByteBufAbstract decompress(ChannelHandlerContextAbstract ctx, ByteBufAbstract byteBuf) {
        if (BYTE_TO_MESSAGE_DECODER == null) {
            BYTE_TO_MESSAGE_DECODER = MinecraftReflection.getNettyClass("handler.codec.ByteToMessageDecoder");
        }
        Object decompressHandler = ctx.pipeline().get("decompress").rawChannelHandler();
        //If the decompress decoder is not an instance of the ByteToMessageDecoder
        if (!BYTE_TO_MESSAGE_DECODER.isInstance(decompressHandler)) {
            //ViaRewind replaced the decompressor with an empty handler, so we can just skip the decompression process.
            return ctx.alloc().buffer().writeBytes(byteBuf);
        }
        if (byteBuf.readableBytes() != 0) {
            PacketWrapper wrapper = PacketWrapper.createUniversalPacketWrapper(ByteBufAbstract.generate(byteBuf));
            int dataLength = wrapper.readVarInt();
            if (dataLength == 0) {
                return byteBuf.readBytes(byteBuf.readableBytes());
            } else {
                if (dataLength < THRESHOLD) {
                    throw new RuntimeException("Badly compressed packet - size of " + dataLength + " is below server threshold of " + THRESHOLD);
                }

                if (dataLength > 2097152) {
                    throw new RuntimeException("Badly compressed packet - size of " + dataLength + " is larger than protocol maximum of " + 2097152);
                }

                byte[] compressedData = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(compressedData);
                INFLATER.setInput(compressedData);
                byte[] decompressedData = new byte[dataLength];
                try {
                    INFLATER.inflate(decompressedData);
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
                ByteBufAbstract output = ByteBufUtil.wrappedBuffer(decompressedData);
                INFLATER.reset();
                return output;
            }
        } else {
            return null;
        }
    }
}
