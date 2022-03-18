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

package io.github.retrooper.packetevents.injector.latest.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.ExceptionUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.injector.PacketCompressionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.github.retrooper.packetevents.util.viaversion.CustomPipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PacketDecoderLatest extends ByteToMessageDecoder {
    public ByteToMessageDecoder mcDecoder = null;
    public List<ByteToMessageDecoder> decoders = new ArrayList<>();
    public User user;
    public volatile Player player;
    public boolean handledCompression;
    public boolean skipDoubleTransform;

    public PacketDecoderLatest(User user) {
        this.user = user;
    }

    public PacketDecoderLatest(PacketDecoderLatest decoder) {
        mcDecoder = decoder.mcDecoder;
        decoders = decoder.decoders;
        user = decoder.user;
        player = decoder.player;
        handledCompression = decoder.handledCompression;
        skipDoubleTransform = decoder.skipDoubleTransform;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) throws Exception {
        ByteBuf transformed = ctx.alloc().buffer().writeBytes(input);
        try {
            if (skipDoubleTransform) {
                skipDoubleTransform = false;
                out.add(transformed.retain());
            }
            boolean doRecompression =
                    handleCompressionOrder(ctx, transformed);
            PacketEventsImplHelper.handleServerBoundPacket(ctx.channel(), user, player, transformed);
            if (transformed.isReadable()) {
                if (doRecompression) {
                    PacketCompressionUtil.recompress(ctx, transformed);
                    skipDoubleTransform = true;
                }
                out.add(transformed.retain());
            }
            if (doRecompression && transformed.isReadable()) {
                PacketCompressionUtil.recompress(ctx, transformed);
                skipDoubleTransform = true;
            }
        }
        finally {
            transformed.release();
        }
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        if (buffer.isReadable()) {
            read(ctx, buffer, out);
            for (ByteToMessageDecoder decoder : decoders) {
                //Only support one output object
                if (!out.isEmpty()) {
                    Object input = out.get(0);
                    out.clear();
                    out.addAll(CustomPipelineUtil.callDecode(decoder, ctx, input));
                }
            }
            if (mcDecoder != null) {
                //Call minecraft decoder to convert the ByteBuf to an NMS object for the next handlers
                try {
                    if (!out.isEmpty()) {
                        Object input = out.get(0);
                        out.clear();
                        out.addAll(CustomPipelineUtil.callDecode(mcDecoder, ctx, input));
                    }
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //if (!ExceptionUtil.isExceptionContainedIn(cause, PacketEvents.getAPI().getNettyManager().getChannelOperator().getIgnoredHandlerExceptions())) {
        super.exceptionCaught(ctx, cause);
        //}
        //Check if the minecraft server will already print our exception for us.
        if (ExceptionUtil.isException(cause, PacketProcessException.class)
                && !SpigotReflectionUtil.isMinecraftServerInstanceDebugging()
                && (user == null || user.getConnectionState() != ConnectionState.HANDSHAKING)) {
            cause.printStackTrace();
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
