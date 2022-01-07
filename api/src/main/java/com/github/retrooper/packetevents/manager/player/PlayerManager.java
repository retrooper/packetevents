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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.GameProfile;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface PlayerManager {
    //Job of implementations, clear these maps
    //Cache game profile independently on late-injections(should only happen on Spigot legacy versions)
    //
    Map<ChannelAbstract, ClientVersion> CLIENT_VERSIONS = new ConcurrentHashMap<>();
    Map<ChannelAbstract, ConnectionState> CONNECTION_STATES = new ConcurrentHashMap<>();
    Map<String, ChannelAbstract> CHANNELS = new ConcurrentHashMap<>();
    Map<ChannelAbstract, GameProfile> GAME_PROFILES = new ConcurrentHashMap<>();
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
        ConnectionState connectionState = CONNECTION_STATES.get(channel);
        if (connectionState == null) {
            connectionState = PacketEvents.getAPI().getInjector().getConnectionState(channel);
            if (connectionState == null) {
                connectionState = ConnectionState.PLAY;
            }
            CONNECTION_STATES.put(channel, connectionState);
        }
        return connectionState;
    }

    default void changeConnectionState(ChannelAbstract channel, ConnectionState connectionState) {
        CONNECTION_STATES.put(channel, connectionState);
        PacketEvents.getAPI().getInjector().changeConnectionState(channel, connectionState);
    }

    default void setClientVersion(ChannelAbstract channel, ClientVersion version) {
        CLIENT_VERSIONS.put(channel, version);
    }

    default void setClientVersion(@NotNull Object player, ClientVersion version) {
        setClientVersion(getChannel(player), version);
    }

    default void sendPacket(ChannelAbstract channel, PacketWrapper<?> wrapper) {
        boolean shouldSend = true;
        if (wrapper instanceof WrapperPlayServerDestroyEntities) {
            WrapperPlayServerDestroyEntities destroyEntities = (WrapperPlayServerDestroyEntities) wrapper;
            if (destroyEntities.getEntityIds().length > 1 && wrapper.getServerVersion() == ServerVersion.V_1_17) {
                //Transform into multiple packets
                for (int entityId : destroyEntities.getEntityIds()) {
                    WrapperPlayServerDestroyEntities newPacket = new WrapperPlayServerDestroyEntities(entityId);
                    newPacket.prepareForSend();
                    sendPacket(channel, newPacket.buffer);
                    shouldSend = false;
                }
            }
        }
        if (shouldSend) {
            wrapper.prepareForSend();
            sendPacket(channel, wrapper.buffer);
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
        boolean shouldSend = true;
        if (wrapper instanceof WrapperPlayServerDestroyEntities) {
            WrapperPlayServerDestroyEntities destroyEntities = (WrapperPlayServerDestroyEntities) wrapper;
            if (destroyEntities.getEntityIds().length > 1 && wrapper.getServerVersion() == ServerVersion.V_1_17) {
                //Transform into multiple packets
                for (int entityId : destroyEntities.getEntityIds()) {
                    WrapperPlayServerDestroyEntities newPacket = new WrapperPlayServerDestroyEntities(entityId);
                    newPacket.prepareForSend();
                    sendPacketSilently(channel, newPacket.buffer);
                    shouldSend = false;
                }
            }
        }
        if (shouldSend) {
            wrapper.prepareForSend();
            sendPacketSilently(channel, wrapper.buffer);
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

    default GameProfile getGameProfile(ChannelAbstract channel) {
        return GAME_PROFILES.get(channel);
    }

    default void setGameProfile(ChannelAbstract channel, GameProfile gameProfile) {
        GAME_PROFILES.put(channel, gameProfile);
    }

    default ChannelAbstract getChannel(String username) {
        return CHANNELS.get(username);
    }

    default void setChannel(String username, ChannelAbstract channel) {
        CHANNELS.put(username, channel);
    }

    void sendPacket(ChannelAbstract channel, ByteBufAbstract byteBuf);

    void sendPacketSilently(ChannelAbstract channel, ByteBufAbstract byteBuf);

    int getPing(@NotNull Object player);

    @NotNull
    ClientVersion getClientVersion(@NotNull Object player);

    ClientVersion getClientVersion(ChannelAbstract channel);

    GameProfile getGameProfile(@NotNull Object player);

    ChannelAbstract getChannel(@NotNull Object player);
}
