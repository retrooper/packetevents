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
import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelHandlerContextAbstract;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.handlers.compression.CustomPacketCompressor;
import io.github.retrooper.packetevents.handlers.compression.CustomPacketDecompressor;
import io.github.retrooper.packetevents.handlers.compression.PacketCompressionUtil;
import io.github.retrooper.packetevents.handlers.modern.early.CompressionManagerModern;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.CustomPipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@ChannelHandler.Sharable
public class PacketEncoderModern extends MessageToByteEncoder<Object> {
    public User user;
    public volatile Player player;
    public boolean handledCompression;
    public MessageToByteEncoder<?> mcEncoder;
    private final List<Runnable> postTasks = new ArrayList<>();

    public PacketEncoderModern(User user) {
        this.user = user;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf buffer) {
        boolean doCompression = handleCompressionOrder(ctx, buffer);

        int preProcessIndex = buffer.readerIndex();
        PacketSendEvent packetSendEvent =
                new PacketSendEvent(user.getConnectionState(), ctx.channel(), user, player, buffer);
        int processIndex = buffer.readerIndex();
        PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent, () -> {
            buffer.readerIndex(processIndex);
        });
        if (!packetSendEvent.isCancelled()) {
            if (packetSendEvent.getLastUsedWrapper() != null) {
                packetSendEvent.getByteBuf().clear();
                packetSendEvent.getLastUsedWrapper().writeVarInt(packetSendEvent.getPacketId());
                packetSendEvent.getLastUsedWrapper().writeData();
            }
            buffer.readerIndex(preProcessIndex);
            if (doCompression) {
                PacketCompressionUtil.recompress(ctx, buffer);
            }
            postTasks.addAll(packetSendEvent.getPostTasks());
        } else {
            //Make the buffer unreadable for the next handlers
            buffer.clear();
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!postTasks.isEmpty()) {
            List<Runnable> postTasks = new ArrayList<>(this.postTasks);
            this.postTasks.clear();
            promise.addListener(f -> {
                for (Runnable task : postTasks) {
                    task.run();
                }
            });
        }
        super.write(ctx, msg, promise);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, ByteBuf out) {
        if (!(o instanceof ByteBuf)) {
            //Call mc encoder
            if (mcEncoder == null) return;
            try {
                CustomPipelineUtil.callEncode(mcEncoder, ctx, o, out);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            ByteBuf in = (ByteBuf) o;
            if (!in.isReadable()) return;
            out.writeBytes(in);
        }
        read(ctx, out);
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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}
