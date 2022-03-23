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

package io.github.retrooper.packetevents.injector.legacy.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.ExceptionUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.injector.PacketCompressionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.channel.ChannelHandler;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.handler.codec.MessageToMessageDecoder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@ChannelHandler.Sharable
public class PacketDecoderLegacy extends MessageToMessageDecoder<ByteBuf> {
    public volatile Player player;
    public User user;
    public boolean handledCompression;
    private boolean skipDoubleTransform;
    private final List<Runnable> postTasks = new ArrayList<>();

    public PacketDecoderLegacy(User user) {
        this.user = user;
    }

    public PacketDecoderLegacy(PacketDecoderLegacy decoder) {
        this.player = decoder.player;
        this.user = decoder.user;
        this.handledCompression = decoder.handledCompression;
        this.skipDoubleTransform = decoder.skipDoubleTransform;
        postTasks.clear();
        postTasks.addAll(decoder.postTasks);
    }

    public void read(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) throws Exception {
        if (skipDoubleTransform) {
            skipDoubleTransform = false;
            out.add(input.retain());
        }
        ByteBuf outputBuffer = ctx.alloc().buffer().writeBytes(input);
        try {
            boolean doRecompression =
                    handleCompressionOrder(ctx, outputBuffer);
            PacketEventsImplHelper.handleServerBoundPacket(ctx.channel(), user, player, outputBuffer);
            if (outputBuffer.isReadable()) {
                if (doRecompression) {
                    PacketCompressionUtil.recompress(ctx, outputBuffer);
                    skipDoubleTransform = true;
                }
                out.add(outputBuffer.retain());
            }
        } finally {
            outputBuffer.release();
        }
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        read(ctx, byteBuf, out);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        //Check if the minecraft server will already print our exception for us.
        if (!SpigotReflectionUtil.isMinecraftServerInstanceDebugging()
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
