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

package io.github.retrooper.packetevents.handlers.modern;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.handlers.compression.PacketCompressionManager;
import io.github.retrooper.packetevents.handlers.compression.PacketCompressionUtil;
import io.github.retrooper.packetevents.utils.netty.RawNettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.zip.DataFormatException;

public class PacketCompressionModern implements PacketCompressionManager {
    @Override
    public void decompress(Object rawPipeline, Object rawBuffer, Object rawOutput) {
        ChannelPipeline pipeline = (ChannelPipeline) rawPipeline;
        ByteBuf buffer = (ByteBuf) rawBuffer;
        if (!buffer.isReadable()) return;
        ByteBuf output = (ByteBuf) rawOutput;
        ChannelHandler decompressionHandler = pipeline.get("decompress");
        if (decompressionHandler instanceof ByteToMessageDecoder) {
            int dataLength = RawNettyUtil.readVarInt(buffer);
            if (dataLength == 0) {
                output.writeBytes(buffer.readBytes(buffer.readableBytes()));
            } else {
                if (dataLength < PacketCompressionUtil.THRESHOLD) {
                    throw new RuntimeException("Badly compressed packet - size of " + dataLength + " is below server threshold of " + PacketCompressionUtil.THRESHOLD);
                }

                if (dataLength > PacketCompressionUtil.MAXIMUM) {
                    throw new RuntimeException("Badly compressed packet - size of " + dataLength + " is larger than protocol maximum of " + PacketCompressionUtil.MAXIMUM);
                }

                byte[] compressedData = new byte[buffer.readableBytes()];
                buffer.readBytes(compressedData);
                PacketCompressionUtil.INFLATER.setInput(compressedData);
                byte[] decompressedData = new byte[dataLength];
                try {
                    PacketCompressionUtil.INFLATER.inflate(decompressedData);
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
                output.writeBytes(decompressedData);
                PacketCompressionUtil.INFLATER.reset();
            }
        } else {
            //ViaRewind might have replaced the decompressor with an empty handler, so we can just skip the decompression process.
            output.writeBytes(buffer);
        }
    }

    @Override
    public void compress(Object rawPipeline, Object rawBuffer, Object rawOutput) {
        ChannelPipeline pipeline = (ChannelPipeline) rawPipeline;
        ByteBuf buffer = (ByteBuf) rawBuffer;
        if (!buffer.isReadable()) return;
        ByteBuf output = (ByteBuf) rawOutput;
        ChannelHandler compressionHandler = pipeline.get("compress");
        if (compressionHandler instanceof MessageToByteEncoder) {
            int dataLength = buffer.readableBytes();
            if (dataLength < PacketCompressionUtil.THRESHOLD) {
                RawNettyUtil.writeVarInt(output, dataLength);
                output.writeBytes(buffer);
            } else {
                byte[] decompressedData = new byte[dataLength];
                buffer.readBytes(decompressedData);
                RawNettyUtil.writeVarInt(output, decompressedData.length);
                PacketCompressionUtil.DEFLATER.setInput(decompressedData, 0, decompressedData.length);
                PacketCompressionUtil.DEFLATER.finish();

                while (!PacketCompressionUtil.DEFLATER.finished()) {
                    int deflateResult = PacketCompressionUtil.DEFLATER.deflate(PacketCompressionUtil.COMPRESSED_DATA);
                    output.writeBytes(PacketCompressionUtil.COMPRESSED_DATA, 0, deflateResult);
                }
                PacketCompressionUtil.DEFLATER.reset();
            }
        }
    }

    @Override
    public void recompress(Object rawCtx, Object rawBuffer) {
        ChannelHandlerContext ctx = (ChannelHandlerContext) rawCtx;
        ByteBuf buffer = (ByteBuf) rawBuffer;
        ByteBuf compressed = ctx.alloc().buffer();
        compress(ctx.pipeline(), buffer, compressed);
        try {
            buffer.clear().writeBytes(compressed);
        }
        finally {
            compressed.release();
        }
    }


    @Override
    public void relocateHandlers(Object rawPipeline, Object rawBuffer, Object rawDecompressed) {
        ChannelPipeline pipeline = (ChannelPipeline) rawPipeline;
        ByteBuf buffer = (ByteBuf) rawBuffer;
        ByteBuf decompressed = (ByteBuf) rawDecompressed;
        try {
            buffer.clear().writeBytes(decompressed);
        } finally {
            decompressed.release();
        }
        ChannelHandler encoder = pipeline.remove(PacketEvents.ENCODER_NAME);
        pipeline.addAfter("compress", PacketEvents.ENCODER_NAME, encoder);

        PacketDecoderModern decoder = (PacketDecoderModern) pipeline.remove(PacketEvents.DECODER_NAME);
        pipeline.addAfter("decompress", PacketEvents.DECODER_NAME, new PacketDecoderModern(decoder));
    }
}
