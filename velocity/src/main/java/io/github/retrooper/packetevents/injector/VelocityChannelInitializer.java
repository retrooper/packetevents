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

package io.github.retrooper.packetevents.injector;

import com.github.retrooper.packetevents.protocol.ConnectionState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class VelocityChannelInitializer extends ChannelInitializer<Channel> {
    private static Method INIT_CHANNEL;
    private final ChannelInitializer<Channel> wrappedInitializer;
    //public final List<ChannelInitializer<?>> initializers = new ArrayList<>();

    public VelocityChannelInitializer(ChannelInitializer<Channel> wrappedInitializer) {
        this.wrappedInitializer = wrappedInitializer;
    }

    @Override
    protected void initChannel(@NotNull Channel channel) throws Exception {
        if (INIT_CHANNEL == null) {
            INIT_CHANNEL = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class);
            INIT_CHANNEL.setAccessible(true);
        }
        INIT_CHANNEL.invoke(wrappedInitializer, channel);

        ServerConnectionInitializer.initChannel(channel, ConnectionState.HANDSHAKING);
    }
}
