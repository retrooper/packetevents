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

import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.ExceptionUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.github.retrooper.packetevents.util.viaversion.CustomPipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;
import org.bukkit.entity.Player;

@ChannelHandler.Sharable
public class PacketEncoderLatest extends MessageToByteEncoder<Object> {
    public User user;
    public volatile Player player;
    public MessageToByteEncoder<?> vanillaEncoder;

    public PacketEncoderLatest(User user) {
        this.user = user;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, ByteBuf out) throws Exception {
        if (!(o instanceof ByteBuf)) {
            //Convert NMS object to bytes, so we can process it right away.
            if (vanillaEncoder == null) return;
            CustomPipelineUtil.callEncode(vanillaEncoder, ctx, o, out);
            //Failed to translate it into ByteBuf form (which we can process)
            if (!out.isReadable()) return;
        } else {
            ByteBuf in = (ByteBuf) o;
            //Empty packets?
            if (!in.isReadable()) return;
            out.writeBytes(in);
        }
        PacketEventsImplHelper.handleClientBoundPacket(ctx.channel(), user, player, out);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        PacketEventsImplHelper.handleDisconnection(ctx.channel(), null);
        super.close(ctx, promise);
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
}
