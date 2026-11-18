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
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.FakeChannelUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import io.github.retrooper.packetevents.injector.connection.PacketHandlerRelocation.Slot;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
public final class ServerConnectionInitializer {

    private ServerConnectionInitializer() {
    }

    @Deprecated
    public static void initChannel(Object ch, ConnectionState state) {
        initChannel((Channel) ch, state);
    }

    public static void initChannel(Channel channel, ConnectionState state) {
        if (FakeChannelUtil.isFakeChannel(channel)) {
            return;
        }
        User user = new User(channel, state, null, new UserProfile(null, null));

        PacketEventsAPI<?> packetevents = PacketEvents.getAPI();
        if (state == ConnectionState.PLAY) {
            // Player connected before ViaVersion init, therefore the player is server version (mostly true except 1.7 servers)
            user.setClientVersion(packetevents.getServerManager().getVersion().toClientVersion());
            packetevents.getLogManager().warn("Late injection detected for " + user.getUUID() + ", we missed packets so some functionality may break!");
        }

        synchronized (channel) {
            /*
             * This is a rather rare one, BUT!
             * If the plugin takes a while to initialize and handshakes/pings pile up,
             * some may not be handled completely, thus, not having a 'splitter' ChannelHandler.
             * We can, of course, wait for them to be handled, but this complexes the algorithm.
             * Taken the above into account, here we just drop all unhandled connections.
             */
            if (channel.pipeline().get(PacketHandlerRelocation.SPLITTER_NAME) == null) {
                channel.close();
                return;
            }

            UserConnectEvent connectEvent = new UserConnectEvent(user);
            packetevents.getEventManager().callEvent(connectEvent);
            if (connectEvent.isCancelled()) {
                channel.unsafe().closeForcibly();
                return;
            }

            PacketHandlerRelocation.inject(channel, user, Slot.NORMAL);
            SpigotChannelInjector injector = (SpigotChannelInjector) packetevents.getInjector();
            if (injector.isPreViaVersionActivated()) {
                PacketHandlerRelocation.inject(channel, user, Slot.PRE_VIA);
            }

            packetevents.getProtocolManager().setUser(channel, user);
            // Register the user before the close listener: if the channel is already closed, the listener runs
            // immediately and must find the user, or the entry stays in the user map forever.
            channel.closeFuture().addListener((ChannelFutureListener) future -> PacketEventsImplHelper.handleDisconnection(user.getChannel(), user.getUUID()));
        }
    }

    public static void destroyHandlers(Object ch) {
        Channel channel = (Channel) ch;
        removeHandler(channel, Slot.NORMAL.decoderName(), "decoder");
        removeHandler(channel, Slot.NORMAL.encoderName(), "encoder");
        // Only injected when client-versioned listeners were registered, so silently ignore absence.
        PacketHandlerRelocation.remove(channel, Slot.PRE_VIA);
    }

    private static void removeHandler(Channel channel, String handlerName, String description) {
        if (channel.pipeline().get(handlerName) != null) {
            channel.pipeline().remove(handlerName);
        } else {
            PacketEvents.getAPI().getLogManager().warn(
                    "Could not find " + description + " handler in channel pipeline!");
        }
    }
}
