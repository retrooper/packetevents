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

package io.github.retrooper.packetevents.manager.player;

import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.data.gameprofile.WrappedGameProfile;
import com.github.retrooper.packetevents.protocol.data.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.github.retrooper.packetevents.utils.MinecraftReflectionUtil;
import io.github.retrooper.packetevents.utils.PlayerPingAccessorModern;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManagerImpl implements PlayerManager {
    public final Map<Object, ClientVersion> clientVersions = new ConcurrentHashMap<>();
    public final Map<Object, ConnectionState> connectionStates = new ConcurrentHashMap<>();
    public final Map<String, Object> channels = new ConcurrentHashMap<>();
    @Override
    public ConnectionState getConnectionState(Object player) {
        return null;
    }

    @Override
    public ConnectionState getConnectionState(ChannelAbstract channel) {
        return null;
    }

    @Override
    public ConnectionState changeConnectionState(ChannelAbstract channel, ConnectionState connectionState) {
        return null;
    }

    @Override
    public int getPing(Object player) {
        if (MinecraftReflectionUtil.V_1_17_OR_HIGHER) {
            return PlayerPingAccessorModern.getPing((Player)player);
        }
        else {
            return MinecraftReflectionUtil.getPlayerPing((Player)player);
        }
    }

    @Override
    public @NotNull ClientVersion getClientVersion(Object player) {
        return null;
    }

    @Override
    public ClientVersion getClientVersion(ChannelAbstract channel) {
        return null;
    }

    @Override
    public void sendPacket(ChannelAbstract channel, ByteBufAbstract byteBuf) {

    }

    @Override
    public void sendPacket(ChannelAbstract channel, PacketWrapper<?> wrapper) {

    }

    @Override
    public void sendPacket(Object player, ByteBufAbstract byteBuf) {

    }

    @Override
    public void sendPacket(Object player, PacketWrapper<?> wrapper) {

    }

    @Override
    public WrappedGameProfile getGameProfile(Object player) {
        return null;
    }

    @Override
    public boolean isGeyserPlayer(Object player) {
        return false;
    }

    @Override
    public boolean isGeyserPlayer(UUID uuid) {
        return false;
    }

    @Override
    public ChannelAbstract getChannel(Object player) {
        return null;
    }
}
