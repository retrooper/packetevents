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
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.PacketTransformationUtil;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface PlayerManager {
    //Job of implementations, clear these maps
    //Cache game profile independently on late-injections(should only happen on Spigot legacy versions)
    //
    Map<String, ChannelAbstract> CHANNELS = new ConcurrentHashMap<>();
    Map<ChannelAbstract, User> USERS = new ConcurrentHashMap<>();
    Map<UUID, Map<Class<? extends PlayerAttributeObject>, PlayerAttributeObject>> PLAYER_ATTRIBUTES = new ConcurrentHashMap<>();

    default <T extends PlayerAttributeObject> T getAttributeOrDefault(UUID uuid, Class<T> clazz, T defaultReturnValue) {
        Map<Class<? extends PlayerAttributeObject>, PlayerAttributeObject> attributes = PLAYER_ATTRIBUTES.get(uuid);
        if (attributes != null) {
            return (T) attributes.get(clazz);
        } else {
            attributes = new HashMap<>();
            attributes.put(defaultReturnValue.getClass(), defaultReturnValue);
            PLAYER_ATTRIBUTES.put(uuid, attributes);
            return defaultReturnValue;
        }
    }

    default <T extends PlayerAttributeObject> T getAttribute(UUID uuid, Class<T> clazz) {
        Map<Class<? extends PlayerAttributeObject>, PlayerAttributeObject> attributes = PLAYER_ATTRIBUTES.get(uuid);
        if (attributes != null) {
            return (T) attributes.get(clazz);
        } else {
            PLAYER_ATTRIBUTES.put(uuid, new HashMap<>());
            return null;
        }
    }

    default <T extends PlayerAttributeObject> void setAttribute(UUID uuid, T attribute) {
        Map<Class<? extends PlayerAttributeObject>, PlayerAttributeObject> attributes = PLAYER_ATTRIBUTES.computeIfAbsent(uuid, k -> new HashMap<>());
        attributes.put(attribute.getClass(), attribute);
    }

    default ConnectionState getConnectionState(@NotNull Object player) {
        return getConnectionState(getChannel(player));
    }

    default ConnectionState getConnectionState(ChannelAbstract channel) {
        return getUser(channel).getConnectionState();
    }

    default void changeConnectionState(ChannelAbstract channel, ConnectionState connectionState) {
        getUser(channel).setConnectionState(connectionState);
    }

    default void setClientVersion(ChannelAbstract channel, ClientVersion version) {
        getUser(channel).setClientVersion(version);
    }

    default void setClientVersion(@NotNull Object player, ClientVersion version) {
        setClientVersion(getChannel(player), version);
    }

    default void sendPacket(ChannelAbstract channel, PacketWrapper<?> wrapper) {
        PacketWrapper<?>[] wrappers = PacketTransformationUtil.transform(wrapper);
        for (PacketWrapper<?> packet : wrappers) {
            packet.prepareForSend();
            sendPacket(channel, packet.buffer);
        }
    }

    default void sendPacket(@NotNull Object player, ByteBufAbstract byteBuf) {
        ChannelAbstract channel = getChannel(player);
        sendPacket(channel, byteBuf);
    }

    default void sendPacket(@NotNull Object player, PacketWrapper<?> wrapper) {
        ChannelAbstract channel = getChannel(player);
        sendPacket(channel, wrapper);
    }

    default void sendPacketSilently(ChannelAbstract channel, PacketWrapper<?> wrapper) {
        PacketWrapper<?>[] wrappers = PacketTransformationUtil.transform(wrapper);
        for (PacketWrapper<?> packet : wrappers) {
            packet.prepareForSend();
            sendPacketSilently(channel, packet.buffer);
        }
    }

    default void sendPacketSilently(@NotNull Object player, ByteBufAbstract byteBuf) {
        ChannelAbstract channel = getChannel(player);
        sendPacketSilently(channel, byteBuf);
    }

    default void sendPacketSilently(@NotNull Object player, PacketWrapper<?> wrapper) {
        ChannelAbstract channel = getChannel(player);
        sendPacketSilently(channel, wrapper);
    }

    default User getUser(ChannelAbstract channel) {
        return USERS.get(channel);
    }

    default void setUser(ChannelAbstract channel, User user) {
        USERS.put(channel, user);
        PacketEvents.getAPI().getInjector().updateUser(channel, user);
    }

    default ChannelAbstract getChannel(String username) {
        return CHANNELS.get(username);
    }

    default void setChannel(String username, ChannelAbstract channel) {
        CHANNELS.put(username, channel);
    }

    default void clearUserData(ChannelAbstract channel, String name, UUID uuid) {
        NettyManager.CHANNEL_MAP.remove(channel.rawChannel());
        USERS.remove(channel);
        CHANNELS.remove(name);
        PLAYER_ATTRIBUTES.remove(uuid);
    }

    void sendPacket(ChannelAbstract channel, ByteBufAbstract byteBuf);

    void sendPacketSilently(ChannelAbstract channel, ByteBufAbstract byteBuf);

    int getPing(@NotNull Object player);

    @NotNull
    ClientVersion getClientVersion(@NotNull Object player);

    ClientVersion getClientVersion(ChannelAbstract channel);

    User getUser(@NotNull Object player);

    ChannelAbstract getChannel(@NotNull Object player);
}
