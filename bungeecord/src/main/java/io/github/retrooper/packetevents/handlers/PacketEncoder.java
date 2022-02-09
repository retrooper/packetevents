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

package io.github.retrooper.packetevents.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.EventCreationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
@ChannelHandler.Sharable
public class PacketEncoder extends MessageToByteEncoder<ByteBuf> {
    public ProxiedPlayer player;
    public User user;
    private final List<Runnable> promisedTasks = new ArrayList<>();

    public PacketEncoder(User user, ProxiedPlayer player) {
        this.user = user;
        this.player = player;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        int firstReaderIndex = byteBuf.readerIndex();
        PacketSendEvent packetSendEvent = EventCreationUtil.createSendEvent(ctx.channel(), user, player, byteBuf);
        int readerIndex = byteBuf.readerIndex();
        PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent, () -> byteBuf.readerIndex(readerIndex));
        if (!packetSendEvent.isCancelled()) {
            if (packetSendEvent.getLastUsedWrapper() != null) {
                packetSendEvent.getByteBuf().clear();
                packetSendEvent.getLastUsedWrapper().writeVarInt(packetSendEvent.getPacketId());
                packetSendEvent.getLastUsedWrapper().writeData();
            }
            byteBuf.readerIndex(firstReaderIndex);
            if (packetSendEvent.hasPromisedTasks()) {
                promisedTasks.addAll(packetSendEvent.getPromisedTasks());
            }
        }
        if (packetSendEvent.hasPostTasks()) {
            for (Runnable task : packetSendEvent.getPostTasks()) {
                task.run();
            }
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!promisedTasks.isEmpty()) {
            List<Runnable> postTasks = new ArrayList<>(this.promisedTasks);
            this.promisedTasks.clear();
            promise.addListener(f -> {
                for (Runnable task : postTasks) {
                    task.run();
                }
            });
        }
        super.write(ctx, msg, promise);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
        if (msg.readableBytes() == 0) return;
        out.writeBytes(msg);
        read(ctx, out);
    }
}
