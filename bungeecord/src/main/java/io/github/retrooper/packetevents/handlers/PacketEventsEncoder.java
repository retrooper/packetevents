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

package io.github.retrooper.packetevents.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.EventCreationUtil;
import io.github.retrooper.packetevents.injector.CustomPipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToByteEncoder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.reflect.InvocationTargetException;

// Thanks to ViaVersion for the compression method.
@ChannelHandler.Sharable
public class PacketEventsEncoder extends MessageToByteEncoder<ByteBuf> {
    public ProxiedPlayer player;
    public User user;
    public boolean handledCompression;

    public PacketEventsEncoder(User user) {
        this.user = user;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        boolean doCompression = handleCompressionOrder(ctx, buffer);
        int firstReaderIndex = buffer.readerIndex();
        PacketSendEvent packetSendEvent = EventCreationUtil.createSendEvent(ctx.channel(), user, player,
                buffer, false);
        int readerIndex = buffer.readerIndex();
        PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent, () -> buffer.readerIndex(readerIndex));
        if (!packetSendEvent.isCancelled()) {
            if (packetSendEvent.getLastUsedWrapper() != null) {
                ByteBufHelper.clear(packetSendEvent.getByteBuf());
                packetSendEvent.getLastUsedWrapper().writeVarInt(packetSendEvent.getPacketId());
                packetSendEvent.getLastUsedWrapper().write();
            }
            else {
                buffer.readerIndex(firstReaderIndex);
            }
            if (doCompression) {
                recompress(ctx, buffer);
            }
        } else {
            ByteBufHelper.clear(packetSendEvent.getByteBuf());
        }
        if (packetSendEvent.hasPostTasks()) {
            for (Runnable task : packetSendEvent.getPostTasks()) {
                task.run();
            }
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        if (!msg.isReadable()) {
            return;
        }
        read(ctx, msg);
        out.writeBytes(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    private boolean handleCompressionOrder(ChannelHandlerContext ctx, ByteBuf buffer) {
        ChannelPipeline pipe = ctx.pipeline();
        if (handledCompression) {
            return false;
        }
        int encoderIndex = pipe.names().indexOf("compress");
        if (encoderIndex == -1) {
            return false;
        }
        if (encoderIndex > pipe.names().indexOf(PacketEvents.ENCODER_NAME)) {
            // Need to decompress this packet due to bad order
            ChannelHandler decompressor = pipe.get("decompress");
            try {
                ByteBuf decompressed = (ByteBuf) CustomPipelineUtil.callPacketDecodeByteBuf(decompressor, ctx, buffer).get(0);
                if (buffer != decompressed) {
                    try {
                        buffer.clear().writeBytes(decompressed);
                    } finally {
                        decompressed.release();
                    }
                }
                //Relocate handlers
                PacketEventsDecoder decoder = (PacketEventsDecoder) pipe.remove(PacketEvents.DECODER_NAME);
                PacketEventsEncoder encoder = (PacketEventsEncoder) pipe.remove(PacketEvents.ENCODER_NAME);
                pipe.addAfter("decompress", PacketEvents.DECODER_NAME, decoder);
                pipe.addAfter("compress", PacketEvents.ENCODER_NAME, encoder);
                //System.out.println("Pipe: " + ChannelHelper.pipelineHandlerNamesAsString(ctx.channel()));
                handledCompression = true;
                return true;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void recompress(ChannelHandlerContext ctx, ByteBuf buffer) {
        ChannelHandler compressor = ctx.pipeline().get("compress");
        ByteBuf compressed = ctx.alloc().buffer();
        try {
            CustomPipelineUtil.callPacketEncodeByteBuf(compressor, ctx, buffer, compressed);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            buffer.clear().writeBytes(compressed);
        } finally {
            compressed.release();
        }
    }
}
