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

package io.github.retrooper.packetevents.injector.connection;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import com.github.retrooper.packetevents.util.reflection.ClassUtil;
import io.github.retrooper.packetevents.injector.handlers.PacketEventsDecoder;
import io.github.retrooper.packetevents.injector.handlers.PacketEventsEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;

import java.util.NoSuchElementException;


public class ServerConnectionInitializer {

    public static void initChannel(Object ch, ConnectionState connectionState) {
        Channel channel = (Channel) ch;
        if (ClassUtil.getClassSimpleName(channel.getClass()).equals("FakeChannel")) {
            return;
        }
        User user = new User(channel, connectionState, null, new UserProfile(null, null));

        synchronized (channel) {
            /*
             * This is a rather rare one, BUT!
             * If the plugin takes a while to initialize and handshakes/pings pile up,
             * some may not be handled completely, thus, not having a 'splitter' ChannelHandler.
             * We can, of course, wait for them to be handled, but this complexes the algorithm.
             * Taken the above into account, here we just drop all unhandled connections.
             */
            if (channel.pipeline().get("splitter") == null) {
                channel.close();
                return;
            }

            UserConnectEvent connectEvent = new UserConnectEvent(user);
            PacketEvents.getAPI().getEventManager().callEvent(connectEvent);
            if (connectEvent.isCancelled()) {
                channel.unsafe().closeForcibly();
                return;
            }

            relocateHandlers(channel, user);

            channel.closeFuture().addListener((ChannelFutureListener) future -> PacketEventsImplHelper.handleDisconnection(user.getChannel(), user.getUUID()));
            ProtocolManager.USERS.put(channel, user);
        }
    }

    public static void relocateHandlers(Channel ctx, User user) {
        // User == null means we already have handlers
        try {
            ChannelHandler decoder;
            ChannelHandler encoder;

            if (user == null) {
                encoder = ctx.pipeline().remove(PacketEvents.ENCODER_NAME);
            } else {
                encoder = new PacketEventsEncoder(user);
            }

            if (user == null) {
                decoder = ctx.pipeline().remove(PacketEvents.DECODER_NAME);
            } else {
                decoder = new PacketEventsDecoder(user);
            }

            ctx.pipeline().addBefore("decoder", PacketEvents.DECODER_NAME, decoder);
            ctx.pipeline().addBefore("encoder", PacketEvents.ENCODER_NAME, encoder);
        } catch (NoSuchElementException ex) {
            String handlers = ChannelHelper.pipelineHandlerNamesAsString(ctx);
            throw new IllegalStateException("PacketEvents failed to add a decoder to the netty pipeline. Pipeline handlers: " + handlers, ex);
        }
    }
}
