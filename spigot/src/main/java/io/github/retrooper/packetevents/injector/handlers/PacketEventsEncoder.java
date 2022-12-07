/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package io.github.retrooper.packetevents.injector.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.ExceptionUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.github.retrooper.packetevents.util.viaversion.CustomPipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@ChannelHandler.Sharable
public class PacketEventsEncoder extends MessageToMessageEncoder<ByteBuf> {
    public User user;
    public volatile Player player;
    private ChannelPromise promise;
    public boolean handledCompression;

    public PacketEventsEncoder(User user) {
        this.user = user;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        boolean needsRecompression = !handledCompression && handleCompression(ctx, byteBuf);

        PacketSendEvent sendEvent = PacketEventsImplHelper.handleClientBoundPacket(ctx.channel(), user, player, byteBuf, false, false);

        if (needsRecompression) {
            compress(ctx, byteBuf);
        }

        list.add(byteBuf.retain());

        if (sendEvent.hasPostTasks()) {
            promise.addListener(p -> {
                for (Runnable runnable : sendEvent.getPostTasks()) {
                    runnable.run();
                }
            });
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        this.promise = promise;
        super.write(ctx, msg, promise);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        //Check if the minecraft server will already print our exception for us.
        //Don't print errors during handshake
        if (ExceptionUtil.isException(cause, PacketProcessException.class)
                && !SpigotReflectionUtil.isMinecraftServerInstanceDebugging()
                && (user != null && user.getConnectionState() != ConnectionState.HANDSHAKING)) {
            cause.printStackTrace();
        }
    }

    private void compress(ChannelHandlerContext ctx, ByteBuf input) throws InvocationTargetException {
        ChannelHandler compressor = ctx.pipeline().get("compress");
        ByteBuf temp = ctx.alloc().buffer();
        try {
            if (compressor != null) {
                CustomPipelineUtil.callEncode(compressor, ctx, input, temp);
            }
        } finally {
            input.clear().writeBytes(temp);
            temp.release();
        }
    }

    private void decompress(ChannelHandlerContext ctx, ByteBuf input, ByteBuf output) throws InvocationTargetException {
        ChannelHandler decompressor = ctx.pipeline().get("decompress");
        if (decompressor != null) {
            ByteBuf temp = (ByteBuf) CustomPipelineUtil.callDecode(decompressor, ctx, input).get(0);
            try {
                output.clear().writeBytes(temp);
            } finally {
                temp.release();
            }
        }
    }

    private boolean handleCompression(ChannelHandlerContext ctx, ByteBuf buffer) throws InvocationTargetException {
        if (handledCompression) return false;
        int decompressIndex = ctx.pipeline().names().indexOf("decompress");
        if (decompressIndex == -1) return false;
        handledCompression = true;
        int peDecoderIndex = ctx.pipeline().names().indexOf(PacketEvents.DECODER_NAME);
        if (peDecoderIndex == -1) return false;
        if (decompressIndex > peDecoderIndex) {
            //We are ahead of the decompression handler (they are added dynamically) so let us relocate.
            //But first we need to compress the data and re-compress it after we do all our processing to avoid issues.
            decompress(ctx, buffer, buffer);
            //Let us relocate and no longer deal with compression.
            ServerConnectionInitializer.relocateHandlers(ctx.channel(), null);
            return true;
        }
        return false;
    }
}
