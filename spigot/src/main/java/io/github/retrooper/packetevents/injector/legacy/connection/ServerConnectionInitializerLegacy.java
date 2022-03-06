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

package io.github.retrooper.packetevents.injector.legacy.connection;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import io.github.retrooper.packetevents.injector.legacy.handlers.PacketDecoderLegacy;
import io.github.retrooper.packetevents.injector.legacy.handlers.PacketEncoderLegacy;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.socket.nio.NioSocketChannel;

import java.util.NoSuchElementException;

public class ServerConnectionInitializerLegacy {
    public static void initChannel(Object ch, ConnectionState connectionState) {
        Channel channel = (Channel) ch;
         if (!(channel instanceof NioSocketChannel)) {
            return;
        }
        User user = new User(channel, connectionState, null, new UserProfile(null, null));
        ProtocolManager.USERS.put(channel, user);
        try {
            channel.pipeline().addAfter("splitter", PacketEvents.DECODER_NAME, new PacketDecoderLegacy(user));
        } catch (NoSuchElementException ex) {
            String handlers = ChannelHelper.pipelineHandlerNamesAsString(channel);
            throw new IllegalStateException("PacketEvents failed to add a decoder to the netty pipeline. Pipeline handlers: " + handlers, ex);
        }
        //No need to account for ViaVersion, as they don't support 1.7.10
        channel.pipeline().addBefore("encoder", PacketEvents.ENCODER_NAME, new PacketEncoderLegacy(user));
    }

    public static void destroyChannel(Object ch) {
        Channel channel = (Channel) ch;
        if (!(channel instanceof NioSocketChannel)) {
            return;
        }
        channel.pipeline().remove(PacketEvents.DECODER_NAME);
        channel.pipeline().remove(PacketEvents.ENCODER_NAME);
        ProtocolManager.USERS.remove(channel);
    }
}
