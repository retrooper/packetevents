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

package io.github.retrooper.packetevents.injector;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import io.github.retrooper.packetevents.handlers.PacketDecoder;
import io.github.retrooper.packetevents.handlers.PacketEncoder;
import io.netty.channel.Channel;

public class ServerConnectionInitializer {
    public static void addChannelHandlers(Channel channel, PacketDecoder decoder, PacketEncoder encoder) {
        channel.pipeline().addBefore("minecraft-decoder", PacketEvents.DECODER_NAME, decoder);
        channel.pipeline().addBefore("minecraft-encoder", PacketEvents.ENCODER_NAME, encoder);
    }

    public static void initChannel(Channel channel, ConnectionState state) {
        User user = new User(channel, state, null, new UserProfile(null, null));
        UserConnectEvent connectEvent = new UserConnectEvent(user);
        PacketEvents.getAPI().getEventManager().callEvent(connectEvent);
        if (connectEvent.isCancelled()) {
            channel.unsafe().closeForcibly();
            return;
        }
        ProtocolManager.USERS.put(channel, user);
        PacketDecoder decoder = new PacketDecoder(user);
        PacketEncoder encoder = new PacketEncoder(user);
        addChannelHandlers(channel, decoder, encoder);
    }

    public static void destroyChannel(Channel channel) {
        User user = ProtocolManager.USERS.get(channel);
        UserDisconnectEvent disconnectEvent = new UserDisconnectEvent(user);
        PacketEvents.getAPI().getEventManager().callEvent(disconnectEvent);
        channel.pipeline().remove(PacketEvents.DECODER_NAME);
        channel.pipeline().remove(PacketEvents.ENCODER_NAME);
    }

    public static void reloadChannel(Channel channel) {
        PacketDecoder decoder = (PacketDecoder) channel.pipeline().remove(PacketEvents.DECODER_NAME);
        PacketEncoder encoder = (PacketEncoder) channel.pipeline().remove(PacketEvents.ENCODER_NAME);
        addChannelHandlers(channel, decoder, encoder);
    }
}
