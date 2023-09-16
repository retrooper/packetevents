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

package io.github.retrooper.packetevents.injector.modern.early;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.injector.modern.PlayerChannelHandlerModern;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

import java.lang.reflect.Method;

public class PEChannelInitializerModern extends ChannelInitializer<Channel> {
    private final ChannelInitializer<?> oldChannelInitializer;
    private Method initChannelMethod;

    public PEChannelInitializerModern(ChannelInitializer<?> oldChannelInitializer) {
        this.oldChannelInitializer = oldChannelInitializer;
        load();
    }

    public static void postInitChannel(Channel channel) {
        if (channel == null || ClassUtil.getClassSimpleName(channel.getClass()).equals("FakeChannel")
                || ClassUtil.getClassSimpleName(channel.getClass()).equals("SpoofedChannel")) {
            return;
        }
        PlayerChannelHandlerModern channelHandler = new PlayerChannelHandlerModern();
        if (channel.pipeline().get("packet_handler") != null) {
            String handlerName = PacketEvents.get().getHandlerName();
            if (channel.pipeline().get(handlerName) != null) {
                //Just ignore this to support latest ProtocolLib snapshots.
                //PacketEvents.get().getPlugin().getLogger().warning("[PacketEvents] Attempted to initialize a channel twice!");
            } else {
                channel.pipeline().addBefore("packet_handler", handlerName, channelHandler);
            }
        }
    }

    private void load() {
        initChannelMethod = Reflection.getMethod(oldChannelInitializer.getClass(), "initChannel", 0);
    }

    public ChannelInitializer<?> getOldChannelInitializer() {
        return oldChannelInitializer;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        initChannelMethod.invoke(oldChannelInitializer, channel);
        postInitChannel(channel);
    }
}
