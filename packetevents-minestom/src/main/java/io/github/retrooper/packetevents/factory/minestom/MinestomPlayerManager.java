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

import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.network.player.PlayerSocketConnection;

/**
 * {@link com.github.retrooper.packetevents.manager.player.PlayerManager} implementation
 * for a Minestom server, keyed off {@code net.minestom.server.entity.Player}.
 */
public class MinestomPlayerManager extends PlayerManagerAbstract {

    @Override
    public int getPing(Object player) {
        if (player instanceof Player minestomPlayer) {
            return minestomPlayer.getLatency();
        }
        throw new UnsupportedOperationException("Unsupported player implementation: " + player);
    }

    @Override
    public Object getChannel(Object player) {
        if (player instanceof Player minestomPlayer) {
            PlayerConnection connection = minestomPlayer.getPlayerConnection();
            if (connection instanceof PlayerSocketConnection socketConnection) {
                return socketConnection.getChannel();
            }
            // Non-socket connections (e.g. test/fake players) have no real channel.
            throw new UnsupportedOperationException(
                    "Unsupported connection implementation: " + connection);
        }
        throw new UnsupportedOperationException("Unsupported player implementation: " + player);
    }
}
