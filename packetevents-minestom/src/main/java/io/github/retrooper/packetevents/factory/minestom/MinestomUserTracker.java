/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package io.github.retrooper.packetevents.factory.minestom;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.network.player.PlayerSocketConnection;

import java.nio.channels.SocketChannel;

/**
 * Binds a PacketEvents {@link User} to each Minestom connection for its lifetime.
 * <p>
 * The user is created at {@link AsyncPlayerConfigurationEvent} (the earliest hook where a
 * {@link Player} with a resolved protocol version exists) and torn down at
 * {@link PlayerDisconnectEvent}. Keyed on the connection's {@link SocketChannel} — the
 * same key {@link MinestomPacketFeeder} and {@link MinestomProtocolManager} use.
 */
public final class MinestomUserTracker {

    private MinestomUserTracker() {
    }

    /** Registers the lifecycle listeners. Call after PacketEvents is initialized. */
    public static void register() {
        var handler = MinecraftServer.getGlobalEventHandler();
        handler.addListener(AsyncPlayerConfigurationEvent.class, MinestomUserTracker::onConfigure);
        handler.addListener(PlayerDisconnectEvent.class, MinestomUserTracker::onDisconnect);
    }

    private static void onConfigure(AsyncPlayerConfigurationEvent event) {
        Player player = event.getPlayer();
        if (!(player.getPlayerConnection() instanceof PlayerSocketConnection conn)) {
            return; // non-socket (test/fake) connection: nothing to track
        }
        SocketChannel channel = conn.getChannel();
        // NOTE: behind Velocity+ViaVersion this is always the native protocol (Via translates
        // at the proxy). For a player's real client version, forward it from the proxy and
        // resolve that instead — see MinestomProtocolManager#resolveClientVersion.
        var clientVersion = MinestomProtocolManager.resolveClientVersion(conn.getProtocolVersion());
        User user = new User(channel, ConnectionState.CONFIGURATION, clientVersion,
                new UserProfile(player.getUuid(), player.getUsername()));

        PacketEvents.getAPI().getProtocolManager().setUser(channel, user);
        PacketEvents.getAPI().getInjector().setPlayer(channel, player);
    }

    private static void onDisconnect(PlayerDisconnectEvent event) {
        if (!(event.getPlayer().getPlayerConnection() instanceof PlayerSocketConnection conn)) {
            return;
        }
        SocketChannel channel = conn.getChannel();
        PacketEvents.getAPI().getProtocolManager().removeUser(channel);
        ((MinestomChannelInjector) PacketEvents.getAPI().getInjector()).removeChannel(channel);
    }
}
