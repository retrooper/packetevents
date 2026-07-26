/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package io.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.protocol.ConnectionState;
import io.netty.channel.Channel;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.network.player.PlayerSocketConnection;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Conversion helpers between nettystom's networking types and packetevents' own
 * protocol abstractions.
 */
@ApiStatus.Internal
public final class NettystomConversionUtil {

    private NettystomConversionUtil() {
    }

    /**
     * Converts a nettystom {@link net.minestom.server.network.ConnectionState} into the
     * packetevents {@link ConnectionState}.
     */
    public static ConnectionState fromNettystom(net.minestom.server.network.ConnectionState state) {
        return switch (state) {
            case HANDSHAKE -> ConnectionState.HANDSHAKING;
            case STATUS -> ConnectionState.STATUS;
            case LOGIN -> ConnectionState.LOGIN;
            case CONFIGURATION -> ConnectionState.CONFIGURATION;
            case PLAY -> ConnectionState.PLAY;
        };
    }

    /**
     * Returns the underlying Netty {@link Channel} for a nettystom player, or {@code null}
     * if the player is not backed by a socket connection (e.g. a fake player).
     */
    public static @Nullable Channel getChannel(Player player) {
        return getChannel(player.getPlayerConnection());
    }

    /**
     * Returns the underlying Netty {@link Channel} for a nettystom connection, or
     * {@code null} if the connection is not backed by a socket.
     */
    public static @Nullable Channel getChannel(@Nullable PlayerConnection connection) {
        if (connection instanceof PlayerSocketConnection socket) {
            return socket.getChannel();
        }
        return null;
    }
}
