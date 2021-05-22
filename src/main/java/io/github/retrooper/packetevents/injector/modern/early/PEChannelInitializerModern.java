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

package io.github.retrooper.packetevents.injector.modern.early;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.injector.modern.PlayerChannelHandlerModern;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.lang.reflect.Method;

public class PEChannelInitializerModern extends ChannelInitializer<SocketChannel> {
    private final ChannelInitializer<?> oldChannelInitializer;
    private Method initChannelMethod;

    public PEChannelInitializerModern(ChannelInitializer<?> oldChannelInitializer) {
        this.oldChannelInitializer = oldChannelInitializer;
        load();
    }

    private void load() {
        initChannelMethod = Reflection.getMethod(oldChannelInitializer.getClass(), "initChannel", 0);
    }

    public ChannelInitializer<?> getOldChannelInitializer() {
        return oldChannelInitializer;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        initChannelMethod.invoke(oldChannelInitializer, socketChannel);
        postInitChannel(socketChannel);
    }

    public static void postInitChannel(Channel channel) {
        PlayerChannelHandlerModern channelHandler = new PlayerChannelHandlerModern();
        if (channel.pipeline().get("packet_handler") != null) {
            channel.pipeline().addBefore("packet_handler", PacketEvents.get().getHandlerName(), channelHandler);
        }
    }
}
