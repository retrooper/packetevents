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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelHandlerContextAbstract;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.handlers.compression.CustomPacketCompressor;
import io.github.retrooper.packetevents.handlers.compression.CustomPacketDecompressor;
import io.github.retrooper.packetevents.handlers.compression.PacketCompressionUtil;
import io.github.retrooper.packetevents.handlers.modern.early.CompressionManagerModern;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.CustomPipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PacketDecoderModern extends ByteToMessageDecoder {
    public ByteToMessageDecoder mcDecoder = null;
    public List<ByteToMessageDecoder> decoders = new ArrayList<>();
    public User user;
    public volatile Player player;
    public boolean bypassCompression = false;
    public boolean handledCompression;
    public boolean skipDoubleTransform;

    public PacketDecoderModern(User user) {
        this.user = user;
    }

    public PacketDecoderModern(PacketDecoderModern decoder) {
        mcDecoder = decoder.mcDecoder;
        decoders = decoder.decoders;
        user = decoder.user;
        player = decoder.player;
        bypassCompression = decoder.bypassCompression;
        handledCompression = decoder.handledCompression;
        skipDoubleTransform = decoder.skipDoubleTransform;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf input, List<Object> output) {
        if (skipDoubleTransform) {
            skipDoubleTransform = false;
            output.add(input.retain());
        }
        ByteBuf transformed = ctx.alloc().buffer().writeBytes(input);
        try {
            boolean doCompression =
                    !bypassCompression && handleCompressionOrder(ctx, transformed);
            int preProcessIndex = transformed.readerIndex();
            PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(user.getConnectionState(),
                    ctx.channel(), user, player, transformed);
            int readerIndex = transformed.readerIndex();
            PacketEvents.getAPI().getEventManager().callEvent(packetReceiveEvent, () -> {
                transformed.readerIndex(readerIndex);
            });
            if (!packetReceiveEvent.isCancelled()) {
                if (packetReceiveEvent.getLastUsedWrapper() != null) {
                    packetReceiveEvent.getByteBuf().clear();
                    packetReceiveEvent.getLastUsedWrapper().writeVarInt(packetReceiveEvent.getPacketId());
                    packetReceiveEvent.getLastUsedWrapper().writeData();
                }
                transformed.readerIndex(preProcessIndex);
                if (doCompression) {
                    PacketCompressionUtil.recompress(ctx, transformed);
                    skipDoubleTransform = true;
                }
                output.add(transformed.retain());
            }

        } finally {
            transformed.release();
        }
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        if (buffer.isReadable()) {
            read(ctx, buffer, out);
            if (!decoders.isEmpty()) {
                //Call custom decoders
                try {
                    for (ByteToMessageDecoder decoder : decoders) {
                        //Only support one output object
                        Object input = out.get(0);
                        out.clear();
                        out.addAll(CustomPipelineUtil.callDecode(decoder, ctx, input));
                    }
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            if (mcDecoder != null) {
                //Call minecraft decoder to convert the ByteBuf to an NMS object for the next handlers
                try {
                    Object input = out.get(0);
                    out.clear();
                    out.addAll(CustomPipelineUtil.callDecode(mcDecoder, ctx, input));
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean handleCompressionOrder(ChannelHandlerContext ctx, ByteBuf buffer) {
        if (handledCompression) return false;

        int decoderIndex = ctx.pipeline().names().indexOf("decompress");
        if (decoderIndex == -1) return false;
        handledCompression = true;
        if (decoderIndex > ctx.pipeline().names().indexOf(PacketEvents.DECODER_NAME)) {
            // Need to decompress this packet due to bad order
            ByteBuf decompressed = ctx.alloc().buffer();
            PacketCompressionUtil.decompress(ctx.pipeline(), buffer, decompressed);

            PacketCompressionUtil.relocateHandlers(ctx.pipeline(), buffer, decompressed);
            return true;
        }
        return false;
    }
}
