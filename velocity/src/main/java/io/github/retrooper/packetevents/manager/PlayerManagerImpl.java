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

package io.github.retrooper.packetevents.manager;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import com.velocitypowered.api.proxy.Player;
import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract;
import io.netty.channel.Channel;
import org.jetbrains.annotations.NotNull;

public class PlayerManagerImpl extends PlayerManagerAbstract {
    private static Class<?> CONNECTED_PLAYER, MINECRAFT_CONNECTION_CLASS;
    @Override
    public int getPing(@NotNull Object player) {
        return (int) ((Player) player).getPing();
    }

    @Override
    public Object getChannel(@NotNull Object player) {
        Object channel = PacketEvents.getAPI().getProtocolManager().getChannel(((Player) player).getUniqueId());
        if (channel == null) {
            if (CONNECTED_PLAYER == null) {
                CONNECTED_PLAYER = Reflection
                        .getClassByNameWithoutException("com.velocitypowered.proxy.connection.client.ConnectedPlayer");
                MINECRAFT_CONNECTION_CLASS = Reflection
                        .getClassByNameWithoutException("com.velocitypowered.proxy.connection.MinecraftConnection");

            }
            Object connectedPlayer = CONNECTED_PLAYER.cast(player);
            ReflectionObject reflectConnectedPlayer = new ReflectionObject(connectedPlayer);
            Object minecraftConnection = reflectConnectedPlayer.readObject(0, MINECRAFT_CONNECTION_CLASS);
            ReflectionObject reflectConnection = new ReflectionObject(minecraftConnection);
            // In cases where the player has a custom connection, for example, Fake Players, this will not find the channel.
            try {
              channel = reflectConnection.readObject(0, Channel.class);
            } catch (IllegalStateException ignored) {
              return null;
            }

            synchronized (channel) {
                ProtocolManager.CHANNELS.put(((Player) player).getUniqueId(), channel);
            }
        }
        return channel;
    }
}
