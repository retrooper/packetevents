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
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PlayerManager {
    int getPing(@NotNull Object player);
    @NotNull ClientVersion getClientVersion(@NotNull Object player);
    @NotNull ChannelAbstract getChannel(@NotNull Object player);
    @NotNull User getUser(@NotNull Object player);

    default void setClientVersion(@NotNull Object player, @NotNull ClientVersion version) {
        setClientVersion(getChannel(player), version);
    }

    default ConnectionState getConnectionState(@NotNull Object player) {
        return getConnectionState(getChannel(player));
    }

    default void sendPacket(@NotNull Object player, @NotNull ByteBufAbstract byteBuf) {
        sendPacket(getChannel(player), byteBuf);
    }
    default void sendPacket(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
        sendPacket(getChannel(player), wrapper);
    }

    default void sendPacketSilently(@NotNull Object player, @NotNull ByteBufAbstract byteBuf) {
        sendPacketSilently(getChannel(player), byteBuf);
    }
    default void sendPacketSilently(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
        sendPacketSilently(getChannel(player), wrapper);
    }
    default void receivePacket(Object player, ByteBufAbstract byteBuf) {
        PacketEvents.getAPI().getProtocolManager().receivePacket(getChannel(player), byteBuf);
    }

    default void receivePacket(Object player, PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().receivePacket(getChannel(player), wrapper);
    }
    default void receivePacketSilently(Object player, ByteBufAbstract byteBuf) {
        PacketEvents.getAPI().getProtocolManager().receivePacketSilently(getChannel(player), byteBuf);
    }
    default void receivePacketSilently(Object player, PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().receivePacketSilently(getChannel(player), wrapper);
    }

    @Deprecated
    default ConnectionState getConnectionState(ChannelAbstract channel) {
        return PacketEvents.getAPI().getProtocolManager().getConnectionState(channel);
    }

    @Deprecated
    default void changeConnectionState(ChannelAbstract channel, ConnectionState connectionState) {
        PacketEvents.getAPI().getProtocolManager().changeConnectionState(channel, connectionState);
    }

    @Deprecated
    default void setClientVersion(ChannelAbstract channel, ClientVersion version) {
        PacketEvents.getAPI().getProtocolManager().setClientVersion(channel, version);
    }

    @Deprecated
    default void sendPacket(ChannelAbstract channel, PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, wrapper);
    }

    @Deprecated
    default void sendPacketSilently(ChannelAbstract channel, PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().sendPacketSilently(channel, wrapper);
    }

    @Deprecated
    default User getUser(ChannelAbstract channel) {
        return PacketEvents.getAPI().getProtocolManager().getUser(channel);
    }

    @Deprecated
    default void setUser(ChannelAbstract channel, User user) {
        PacketEvents.getAPI().getProtocolManager().setUser(channel, user);
    }

    @Deprecated
    default ChannelAbstract getChannel(String username) {
        return PacketEvents.getAPI().getProtocolManager().getChannel(username);
    }

    @Deprecated
    default void setChannel(String username, ChannelAbstract channel) {
        PacketEvents.getAPI().getProtocolManager().setChannel(username, channel);
    }

    @Deprecated
    default void clearUserData(ChannelAbstract channel, @Nullable String name) {
        PacketEvents.getAPI().getProtocolManager().clearUserData(channel, name);
    }

    @Deprecated
    default void sendPacket(ChannelAbstract channel, ByteBufAbstract byteBuf) {
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, byteBuf);
    }

    @Deprecated
    default void sendPacketSilently(ChannelAbstract channel, ByteBufAbstract byteBuf) {
        PacketEvents.getAPI().getProtocolManager().sendPacketSilently(channel, byteBuf);
    }


    @Deprecated
    default ClientVersion getClientVersion(ChannelAbstract channel) {
        return PacketEvents.getAPI().getProtocolManager().getClientVersion(channel);
    }

}
