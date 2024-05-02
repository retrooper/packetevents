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
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.FakeChannelUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.injector.handlers.PacketEventsDecoder;
import io.github.retrooper.packetevents.injector.handlers.PacketEventsEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;

import java.util.NoSuchElementException;


public class ServerConnectionInitializer {

    public static void initChannel(Object ch, ConnectionState connectionState) {
        Channel channel = (Channel) ch;
        if (FakeChannelUtil.isFakeChannel(channel)) {
            return;
        }
        User user = new User(channel, connectionState, null, new UserProfile(null, null));

        if (connectionState == ConnectionState.PLAY) {
            // Player connected before ViaVersion init, therefore the player is server version (mostly true except 1.7 servers)
            user.setClientVersion(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
            PacketEvents.getAPI().getLogManager().warn("Late injection detected, we missed packets so some functionality may break!");
        }

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

            relocateHandlers(channel, null, user);

            channel.closeFuture().addListener((ChannelFutureListener) future -> PacketEventsImplHelper.handleDisconnection(user.getChannel(), user.getUUID()));
            PacketEvents.getAPI().getProtocolManager().setUser(channel, user);
        }
    }

    public static void destroyHandlers(Object ch) {
        Channel channel = (Channel) ch;
        if (channel.pipeline().get(PacketEvents.DECODER_NAME) != null) {
            channel.pipeline().remove(PacketEvents.DECODER_NAME);
        } else {
            PacketEvents.getAPI().getLogger().warning("Could not find decoder handler in channel pipeline!");
        }

        if (channel.pipeline().get(PacketEvents.ENCODER_NAME) != null) {
            channel.pipeline().remove(PacketEvents.ENCODER_NAME);
        } else {
            PacketEvents.getAPI().getLogger().warning("Could not find encoder handler in channel pipeline!");
        }
    }

    public static void relocateHandlers(Channel ctx, PacketEventsDecoder decoder, User user) {
        // Decoder == null means we haven't made handlers for the user yet
        try {
            ChannelHandler encoder;
            if (decoder != null) {
                // This patches a bug where PE 2.0 handlers keep jumping behind one another causing a stackoverflow
                if (decoder.hasBeenRelocated) return;
                // Make sure we only relocate because of compression once
                decoder.hasBeenRelocated = true;
                decoder = (PacketEventsDecoder) ctx.pipeline().remove(PacketEvents.DECODER_NAME);
                encoder = ctx.pipeline().remove(PacketEvents.ENCODER_NAME);
                decoder = new PacketEventsDecoder(decoder);
                encoder = new PacketEventsEncoder(encoder);
            } else {
                encoder = new PacketEventsEncoder(user);
                decoder = new PacketEventsDecoder(user);
            }
            // We are targeting the encoder and decoder since we don't want to target specific plugins
            // (ProtocolSupport has changed its handler name in the past)
            // I don't like the hacks required for compression but that's on vanilla, we can't fix it.
            // TODO: i think this will only work for server-side packetevents?
            String decoderName = ctx.pipeline().names().contains("inbound_config") ? "inbound_config" : "decoder";
            ctx.pipeline().addBefore(decoderName, PacketEvents.DECODER_NAME, decoder);
            String encoderName = ctx.pipeline().names().contains("outbound_config") ? "outbound_config" : "encoder";
            ctx.pipeline().addBefore(encoderName, PacketEvents.ENCODER_NAME, encoder);
        } catch (NoSuchElementException ex) {
            String handlers = ChannelHelper.pipelineHandlerNamesAsString(ctx);
            throw new IllegalStateException("PacketEvents failed to add a decoder to the netty pipeline. Pipeline handlers: " + handlers, ex);
        }
    }
}
