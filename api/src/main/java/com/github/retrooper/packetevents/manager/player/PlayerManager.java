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

package com.github.retrooper.packetevents.manager.player;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.NotNull;

public interface PlayerManager {
    int getPing(@NotNull Object player);

    @NotNull ClientVersion getClientVersion(@NotNull Object player);

    Object getChannel(@NotNull Object player);

    User getUser(@NotNull Object player);

    default ConnectionState getConnectionState(@NotNull Object player) {
        return getUser(player).getConnectionState();
    }

    default void sendPacket(@NotNull Object player, @NotNull Object byteBuf) {
        PacketEvents.getAPI().getProtocolManager().sendPacket(getChannel(player), byteBuf);
    }
    default void sendPacket(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().sendPacket(getChannel(player), wrapper);
    }

    default void sendPacketSilently(@NotNull Object player, @NotNull Object byteBuf) {
        PacketEvents.getAPI().getProtocolManager().sendPacketSilently(getChannel(player), byteBuf);
    }

    default void sendPacketSilently(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().sendPacketSilently(getChannel(player), wrapper);
    }

    default void writePacket(@NotNull Object player, @NotNull Object byteBuf) {
        PacketEvents.getAPI().getProtocolManager().writePacket(getChannel(player), byteBuf);
    }

    default void writePacket(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().writePacket(getChannel(player), wrapper);
    }

    default void writePacketSilently(@NotNull Object player, @NotNull Object byteBuf) {
        PacketEvents.getAPI().getProtocolManager().writePacketSilently(getChannel(player), byteBuf);
    }

    default void writePacketSilently(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().writePacketSilently(getChannel(player), wrapper);
    }

    default void receivePacket(Object player, Object byteBuf) {
        PacketEvents.getAPI().getProtocolManager().receivePacket(getChannel(player), byteBuf);
    }

    default void receivePacket(Object player, PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().receivePacket(getChannel(player), wrapper);
    }

    default void receivePacketSilently(Object player, Object byteBuf) {
        PacketEvents.getAPI().getProtocolManager().receivePacketSilently(getChannel(player), byteBuf);
    }

    default void receivePacketSilently(Object player, PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().receivePacketSilently(getChannel(player), wrapper);
    }
}
