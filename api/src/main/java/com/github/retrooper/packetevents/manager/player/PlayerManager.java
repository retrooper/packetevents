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

import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.gameprofile.WrappedGameProfile;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface PlayerManager {
    Map<ChannelAbstract, ClientVersion> CLIENT_VERSIONS = new ConcurrentHashMap<>();
    Map<ChannelAbstract, ConnectionState> CONNECTION_STATES = new ConcurrentHashMap<>();
    Map<String, ChannelAbstract> CHANNELS = new ConcurrentHashMap<>();
    Map<UUID, Map<Class<? extends PlayerAttributeObject>, PlayerAttributeObject>> PLAYER_ATTRIBUTES = new ConcurrentHashMap<>();

    <T extends PlayerAttributeObject> T getAttributeOrDefault(UUID uuid, Class<T> clazz, T defaultReturnValue);
    <T extends PlayerAttributeObject> T getAttribute(UUID uuid, Class<T> clazz);
    <T extends PlayerAttributeObject> void setAttribute(UUID uuid, T attribute);

    ConnectionState getConnectionState(@NotNull Object player);

    ConnectionState getConnectionState(ChannelAbstract channel);

    void changeConnectionState(ChannelAbstract channel, ConnectionState connectionState);

    int getPing(@NotNull Object player);

    @NotNull
    ClientVersion getClientVersion(@NotNull Object player);

    ClientVersion getClientVersion(ChannelAbstract channel);

    void setClientVersion(ChannelAbstract channel, ClientVersion version);

    void setClientVersion(@NotNull Object player, ClientVersion version);

    void sendPacket(ChannelAbstract channel, ByteBufAbstract byteBuf);

    void sendPacket(ChannelAbstract channel, PacketWrapper<?> wrapper);

    void sendPacket(@NotNull Object player, ByteBufAbstract byteBuf);

    void sendPacket(@NotNull Object player, PacketWrapper<?> wrapper);

    WrappedGameProfile getGameProfile(@NotNull Object player);

    boolean isGeyserPlayer(@NotNull Object player);

    boolean isGeyserPlayer(UUID uuid);

    ChannelAbstract getChannel(@NotNull Object player);

    ChannelAbstract getChannel(String username);

    void setChannel(String username, ChannelAbstract channel);

    void setChannel(@NotNull Object player, ChannelAbstract channel);
}
