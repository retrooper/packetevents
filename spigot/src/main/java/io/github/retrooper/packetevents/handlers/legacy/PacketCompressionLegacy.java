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

package io.github.retrooper.packetevents.handlers.legacy;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import io.github.retrooper.packetevents.handlers.compression.PacketCompressionManager;
import io.github.retrooper.packetevents.handlers.compression.PacketCompressionUtil;
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.channel.ChannelHandler;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.channel.ChannelPipeline;

import java.util.zip.DataFormatException;

public class PacketCompressionLegacy implements PacketCompressionManager {
    @Override
    public void decompress(Object rawPipeline, Object rawBuffer, Object rawOutput) {
        ChannelPipeline pipeline = (ChannelPipeline) rawPipeline;
        ByteBuf buffer = (ByteBuf) rawBuffer;
        if (buffer.readableBytes() == 0) return;
        ByteBuf output = (ByteBuf) rawOutput;
        ChannelHandler decompressionHandler = pipeline.get("decompress");
        if (!SpigotReflectionUtil.BYTE_TO_MESSAGE_DECODER.isInstance(decompressionHandler)) {
            //ViaRewind might have replaced the decompressor with an empty handler, so we can just skip the decompression process.
            output.writeBytes(buffer);
            return;
        }
        int dataLength = ByteBufHelper.readVarInt(buffer);
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
    }

    @Override
    public void compress(Object rawPipeline, Object rawBuffer, Object rawOutput) {
        ChannelPipeline pipeline = (ChannelPipeline) rawPipeline;
        ByteBuf buffer = (ByteBuf) rawBuffer;
        if (buffer.readableBytes() == 0) return;
        ByteBuf output = (ByteBuf) rawOutput;
        ChannelHandler compressionHandler = pipeline.get("compress");
        if (!SpigotReflectionUtil.MESSAGE_TO_BYTE_ENCODER.isInstance(compressionHandler)) {
            //ViaRewind might have replaced the compressor with an empty handler, so we can just skip the compression process.
            output.writeBytes(buffer);
            return;
        }
        int dataLength = buffer.readableBytes();
        if (dataLength < PacketCompressionUtil.THRESHOLD) {
            ByteBufHelper.writeVarInt(output, 0);
            output.writeBytes(buffer);
        } else {
            byte[] decompressedData = new byte[dataLength];
            buffer.readBytes(decompressedData);
            ByteBufHelper.writeVarInt(output, decompressedData.length);
            PacketCompressionUtil.DEFLATER.setInput(decompressedData, 0, decompressedData.length);
            PacketCompressionUtil.DEFLATER.finish();

            while (!PacketCompressionUtil.DEFLATER.finished()) {
                int deflateResult = PacketCompressionUtil.DEFLATER.deflate(PacketCompressionUtil.COMPRESSED_DATA);
                output.writeBytes(PacketCompressionUtil.COMPRESSED_DATA, 0, deflateResult);
            }
            PacketCompressionUtil.DEFLATER.reset();
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
        } finally {
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

        PacketDecoderLegacy decoder = (PacketDecoderLegacy) pipeline.remove(PacketEvents.DECODER_NAME);
        pipeline.addAfter("decompress", PacketEvents.DECODER_NAME, new PacketDecoderLegacy(decoder));
    }
}
