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

package io.github.retrooper.packetevents.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.EventCreationUtil;
import com.github.retrooper.packetevents.util.ExceptionUtil;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.velocitypowered.api.proxy.Player;
import io.github.retrooper.packetevents.impl.netty.util.WrapperUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.kyori.adventure.text.Component;

@ChannelHandler.Sharable
public class PacketEventsEncoder extends MessageToByteEncoder<ByteBuf> {
    public Player player;
    public User user;

    public PacketEventsEncoder(User user) {
        this.user = user;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        int firstReaderIndex = buffer.readerIndex();
        PacketSendEvent packetSendEvent = EventCreationUtil.createSendEvent(ctx.channel(), user, player, buffer,
                false);
        int readerIndex = buffer.readerIndex();
        PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent, () -> buffer.readerIndex(readerIndex));
        if (!packetSendEvent.isCancelled()) {
            if (packetSendEvent.getLastUsedWrapper() != null) {
                ByteBufHelper.clear(packetSendEvent.getByteBuf());
                packetSendEvent.getLastUsedWrapper().writeVarInt(packetSendEvent.getPacketId());
                packetSendEvent.getLastUsedWrapper().write();
            }
            buffer.readerIndex(firstReaderIndex);
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
        if (!msg.isReadable()) return;

        ByteBuf transformed = ctx.alloc().buffer().writeBytes(msg);
        try {
            read(ctx, transformed);
            out.writeBytes(transformed);
        } finally {
            transformed.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        boolean didWeCauseThis = ExceptionUtil.isException(cause, PacketProcessException.class);
        User user = this.user;
        if (didWeCauseThis
            && (user == null || user.getEncoderState() != ConnectionState.HANDSHAKING)) {
            if (PacketEvents.getAPI().getSettings().isFullStackTraceEnabled()) {
                cause.printStackTrace();
            } else {
                PacketEvents.getAPI().getLogManager().warn(cause.getMessage());
            }

            if (PacketEvents.getAPI().getSettings().isKickOnPacketExceptionEnabled()) {
                Channel channel = ctx.channel();
                if (user != null) {
                    PacketWrapper<?> wrapper = WrapperUtil.disconnectWrapper(user.getEncoderState(), Component.text("Invalid packet"));

                    channel.eventLoop().execute(() -> {
                        if (wrapper != null) user.sendPacket(wrapper);

                        channel.close();
                    });
                } else {
                    channel.close();
                }

                super.exceptionCaught(ctx, cause);
            }
        }
    }
}
