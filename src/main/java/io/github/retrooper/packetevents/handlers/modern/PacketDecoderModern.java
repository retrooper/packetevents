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

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.handlers.compression.CompressionManager;
import io.github.retrooper.packetevents.handlers.compression.CustomPacketDecompressor;
import io.github.retrooper.packetevents.protocol.ConnectionState;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelHandlerContextAbstract;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@ChannelHandler.Sharable
public class PacketDecoderModern extends MessageToMessageDecoder<ByteBuf> {
    public volatile Player player;
    public ConnectionState connectionState = ConnectionState.HANDSHAKING;
    private boolean handledCompression;
    private boolean handledProtocolSupport;
    private boolean skipDoubleTransform;

    public ByteBufAbstract handle(ChannelHandlerContextAbstract ctx, ByteBufAbstract byteBuf) {
        if (skipDoubleTransform) {
            skipDoubleTransform = false;
            return byteBuf.retain();
        }
        ByteBufAbstract transformedBuf = ctx.alloc().buffer().writeBytes(byteBuf);
        try {
            boolean needsCompress = false;
            if (!handledCompression) {
                List<String> handlerNames = ctx.pipeline().names();
                int decompressDecoderIndex = handlerNames.indexOf("decompress");
                if (decompressDecoderIndex != -1) {
                    int decoderIndex = handlerNames.indexOf(PacketEvents.get().decoderName);
                    needsCompress = handleCompressionOrder(ctx, transformedBuf, decompressDecoderIndex, decoderIndex);
                }
            }

            if (!handledProtocolSupport) {
                handleProtocolSupport(ctx);
            }

            int firstReaderIndex = transformedBuf.readerIndex();
            PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(ctx.channel(), player, transformedBuf);
            int readerIndex = transformedBuf.readerIndex();
            PacketEvents.get().getEventManager().callEvent(packetReceiveEvent, () -> {
                transformedBuf.readerIndex(readerIndex);
            });
            transformedBuf.readerIndex(firstReaderIndex);
            if (needsCompress) {
                CompressionManager.recompress(ctx, transformedBuf);
                skipDoubleTransform = true;
            }
            return transformedBuf.retain();
        } finally {
            transformedBuf.release();
        }
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        ByteBuf output = (ByteBuf) handle(ChannelHandlerContextAbstract.generate(ctx), ByteBufAbstract.generate(byteBuf)).rawByteBuf();
        out.add(output);
    }

    private void handleProtocolSupport(ChannelHandlerContextAbstract ctx) {
        List<String> handlerNames = ctx.pipeline().names();
        //ProtocolSupport is before us?
        if (handlerNames.indexOf("ps_raw_capture_receive") < handlerNames.indexOf(PacketEvents.get().decoderName)) {
            if (handlerNames.contains("decompress")) {
                CompressionManager.rearrangeHandlers(ctx, "compress", "decompress");
                handledProtocolSupport = true;
                handlerNames = ctx.pipeline().names();
                System.out.println("RE ARRANGED HANDLERS AFTER COMPRESSION HANDLERS WERE ADDED, HERE IS THE ORDER: " + Arrays.toString(handlerNames.toArray(new String[0])));
            }
            else {
                CompressionManager.rearrangeHandlers(ctx, "prepender", "splitter");
                handlerNames = ctx.pipeline().names();
                System.out.println("RE ARRANGED HANDLERS, HERE IS THE ORDER: " + Arrays.toString(handlerNames.toArray(new String[0])));
            }
        }
    }

    private boolean handleCompressionOrder(ChannelHandlerContextAbstract ctx, ByteBufAbstract buf, int decompressDecoderIndex, int decoderIndex) {
        handledCompression = true;
        if (decompressDecoderIndex > decoderIndex) {
            // Need to decompress this packet due to bad order
            ByteBufAbstract decompressed = CustomPacketDecompressor.decompress(ctx, buf);
            return CompressionManager.rearrangeHandlersAndWriteOutput(ctx, buf, decompressed);
        }
        return false;
    }
}
