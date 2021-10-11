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

package io.github.retrooper.packetevents.handlers.compression;

import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelHandlerContextAbstract;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.github.retrooper.packetevents.utils.MinecraftReflectionUtil;

import java.util.zip.Deflater;

public class CustomPacketCompressor {
    private static final byte[] COMPRESSED_DATA = new byte[8192];
    private static final Deflater DEFLATER = new Deflater();
    private static final int THRESHOLD = 256;
    private static Class<?> MESSAGE_TO_BYTE_ENCODER;

    public static void recompress(ChannelHandlerContextAbstract ctx, ByteBufAbstract buf) {
        ByteBufAbstract compressed = CustomPacketCompressor.compress(ctx, buf);
        try {
            buf.clear().writeBytes(compressed);
        } finally {
            compressed.release();
        }
    }

    public static ByteBufAbstract compress(ChannelHandlerContextAbstract ctx, ByteBufAbstract byteBuf) {
        if (MESSAGE_TO_BYTE_ENCODER == null) {
            MESSAGE_TO_BYTE_ENCODER = MinecraftReflectionUtil.getNettyClass("handler.codec.MessageToByteEncoder");
        }
        ByteBufAbstract output = ctx.alloc().buffer();
        Object compressHandler = ctx.pipeline().get("compress").rawChannelHandler();
        if (!MESSAGE_TO_BYTE_ENCODER.isInstance(compressHandler)) {
            //ViaRewind replaced the compressor with an empty handler, so we can just skip the compression process.
            return output.writeBytes(byteBuf);
        }
        int dataLength = byteBuf.readableBytes();
        PacketWrapper<?> outputWrapper = PacketWrapper.createUniversalPacketWrapper(output);
        if (dataLength < THRESHOLD) {
            //Set data length to 0
            outputWrapper.writeVarInt(0);
            output.writeBytes(byteBuf);
        } else {
            byte[] decompressedData = new byte[dataLength];
            byteBuf.readBytes(decompressedData);
            outputWrapper.writeVarInt(decompressedData.length);
            DEFLATER.setInput(decompressedData, 0, dataLength);
            DEFLATER.finish();

            while (!DEFLATER.finished()) {
                int deflateResult = DEFLATER.deflate(COMPRESSED_DATA);
                output.writeBytes(COMPRESSED_DATA, 0, deflateResult);
            }

            DEFLATER.reset();
        }
        return output;
    }
}
