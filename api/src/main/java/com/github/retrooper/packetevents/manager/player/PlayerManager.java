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
import com.github.retrooper.packetevents.protocol.data.gameprofile.WrappedGameProfile;
import com.github.retrooper.packetevents.protocol.data.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface PlayerManager {
    Map<ChannelAbstract, ClientVersion> CLIENT_VERSIONS = new ConcurrentHashMap<>();
    Map<ChannelAbstract, ConnectionState> CONNECTION_STATES = new ConcurrentHashMap<>();
    Map<String, ChannelAbstract> CHANNELS = new ConcurrentHashMap<>();

    ConnectionState getConnectionState(Object player);

    ConnectionState getConnectionState(ChannelAbstract channel);

    void changeConnectionState(ChannelAbstract channel, ConnectionState connectionState);

    int getPing(Object player);

    @NotNull
    ClientVersion getClientVersion(Object player);

    ClientVersion getClientVersion(ChannelAbstract channel);

    void setClientVersion(ChannelAbstract channel, ClientVersion version);

    void setClientVersion(Object player, ClientVersion version);

    void sendPacket(ChannelAbstract channel, ByteBufAbstract byteBuf);

    void sendPacket(ChannelAbstract channel, PacketWrapper<?> wrapper);

    void sendPacket(Object player, ByteBufAbstract byteBuf);

    void sendPacket(Object player, PacketWrapper<?> wrapper);

    WrappedGameProfile getGameProfile(Object player);

    boolean isGeyserPlayer(Object player);

    boolean isGeyserPlayer(UUID uuid);

    ChannelAbstract getChannel(Object player);

    ChannelAbstract getChannel(String username);

    void setChannel(String username, ChannelAbstract channel);

    void setChannel(Object player, ChannelAbstract channel);
}
