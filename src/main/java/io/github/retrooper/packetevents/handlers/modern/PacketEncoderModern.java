/*
 * This file is part of ViaVersion - https://github.com/ViaVersion/ViaVersion
 * Copyright (C) 2016-2021 ViaVersion and contributors
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
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.handlers.compression.CompressionManager;
import io.github.retrooper.packetevents.handlers.compression.CustomPacketCompressor;
import io.github.retrooper.packetevents.handlers.compression.CustomPacketDecompressor;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelHandlerContextAbstract;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.bukkit.entity.Player;

import java.util.List;

@ChannelHandler.Sharable
public class PacketEncoderModern extends MessageToMessageEncoder<ByteBuf> {
    public volatile Player player;
    private boolean handledCompression;

    public void handle(ChannelHandlerContextAbstract ctx, ByteBufAbstract byteBuf, List<Object> output) {
        ByteBufAbstract transformedBuf = ctx.alloc().buffer().writeBytes(byteBuf);
        try {
            boolean needsCompress = handleCompressionOrder(ctx, transformedBuf);

            int firstReaderIndex = transformedBuf.readerIndex();
            PacketSendEvent packetSendEvent = new PacketSendEvent(ctx.channel(), player, transformedBuf);
            int readerIndex = transformedBuf.readerIndex();
            PacketEvents.get().getEventManager().callEvent(packetSendEvent, () -> {
                transformedBuf.readerIndex(readerIndex);
            });
            if (!packetSendEvent.isCancelled()) {
                if (packetSendEvent.getCurrentPacketWrapper() != null) {
                    packetSendEvent.getByteBuf().clear();
                    packetSendEvent.getCurrentPacketWrapper().writeVarInt(packetSendEvent.getPacketID());
                    packetSendEvent.getCurrentPacketWrapper().writeData();
                }
                transformedBuf.readerIndex(firstReaderIndex);
                if (needsCompress) {
                    recompress(ctx, transformedBuf);
                }
                output.add(transformedBuf.retain().rawByteBuf());
                if (packetSendEvent.getPostTask() != null) {
                    ((ChannelHandlerContext) ctx.rawChannelHandlerContext()).newPromise().addListener(f -> {
                        packetSendEvent.getPostTask().run();
                    });
                }
            }
        } finally {
            transformedBuf.release();
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        handle(ChannelHandlerContextAbstract.generate(ctx), ByteBufAbstract.generate(byteBuf), out);
    }

    private boolean handleCompressionOrder(ChannelHandlerContextAbstract ctx, ByteBufAbstract buf) {
        if (handledCompression) return false;

        int encoderIndex = ctx.pipeline().names().indexOf("compress");
        if (encoderIndex == -1) return false;
        handledCompression = true;
        if (encoderIndex > ctx.pipeline().names().indexOf(PacketEvents.get().encoderName)) {
            // Need to decompress this packet due to bad order
            ByteBufAbstract decompressed = CustomPacketDecompressor.decompress(ctx, buf);
            return CompressionManager.refactorHandlers(ctx, buf, decompressed);
        }
        return false;
    }

    private void recompress(ChannelHandlerContextAbstract ctx, ByteBufAbstract buf) {
        ByteBufAbstract compressed = CustomPacketCompressor.compress(ctx, buf);
        try {
            buf.clear().writeBytes(compressed);
        } finally {
            compressed.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}
