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

import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.ExceptionUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.bukkit.entity.Player;

import java.util.List;

public class PacketEventsDecoder extends MessageToMessageDecoder<ByteBuf> {
    public User user;
    public Player player;
    public boolean hasBeenRelocated;

    public PacketEventsDecoder(User user) {
        this.user = user;
    }

    public PacketEventsDecoder(PacketEventsDecoder decoder) {
        user = decoder.user;
        player = decoder.player;
        hasBeenRelocated = decoder.hasBeenRelocated;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) throws Exception {
        PacketEventsImplHelper.handleServerBoundPacket(ctx.channel(), user, player, input, true);
        out.add(input.retain());
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        if (buffer.isReadable()) {
            read(ctx, buffer, out);
        }
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

    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object event) throws Exception {
        if (PacketEventsEncoder.COMPRESSION_ENABLED_EVENT == null || event != PacketEventsEncoder.COMPRESSION_ENABLED_EVENT) {
            super.userEventTriggered(ctx, event);
            return;
        }

        // Via changes the order of handlers in this event, so we must respond to Via changing their stuff
        ServerConnectionInitializer.relocateHandlers(ctx.channel(), this, user);
        super.userEventTriggered(ctx, event);
    }

}
