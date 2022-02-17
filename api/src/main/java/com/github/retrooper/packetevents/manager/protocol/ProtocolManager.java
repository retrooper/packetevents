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

package com.github.retrooper.packetevents.manager.protocol;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.ProtocolVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.PacketTransformationUtil;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface ProtocolManager {
    Map<String, Object> CHANNELS = new ConcurrentHashMap<>();
    Map<Object, User> USERS = new ConcurrentHashMap<>();

    //Methods to implement
    ProtocolVersion getPlatformVersion();

    void sendPacket(Object channel, Object byteBuf);

    void sendPacketSilently(Object channel, Object byteBuf);

    void receivePacket(Object channel, Object byteBuf);

    void receivePacketSilently(Object channel, Object byteBuf);

    ClientVersion getClientVersion(Object channel);


    //Methods below don't need to be implemented. A lot of these functions depend on each other.
    default ConnectionState getConnectionState(Object channel) {
        return getUser(channel).getConnectionState();
    }

    //TODO Make it clear that this only updates the connection state in our user.
    //Sometimes you should use getInjector().changeConnectionState because that can allow the injector to make adjustments.
    //This is very important, especially on Spigot.
    //As soon as we switch to the play state on spigot, our injector makes some adjustments.
    default void changeConnectionState(Object channel, ConnectionState connectionState) {
        getUser(channel).setConnectionState(connectionState);
    }

    default void setClientVersion(Object channel, ClientVersion version) {
        getUser(channel).setClientVersion(version);
    }

    default void sendPacket(Object channel, PacketWrapper<?> wrapper) {
        PacketWrapper<?>[] wrappers = PacketTransformationUtil.transform(wrapper);
        for (PacketWrapper<?> packet : wrappers) {
            packet.prepareForSend();
            sendPacket(channel, packet.buffer);
        }
    }

    default void sendPacketSilently(Object channel, PacketWrapper<?> wrapper) {
        PacketWrapper<?>[] wrappers = PacketTransformationUtil.transform(wrapper);
        for (PacketWrapper<?> packet : wrappers) {
            packet.prepareForSend();
            sendPacketSilently(channel, packet.buffer);
        }
    }

    default void receivePacket(Object channel, PacketWrapper<?> wrapper) {
        PacketWrapper<?>[] wrappers = PacketTransformationUtil.transform(wrapper);
        for (PacketWrapper<?> packet : wrappers) {
            packet.prepareForSend();
            receivePacket(channel, packet.buffer);
        }
    }

    default void receivePacketSilently(Object channel, PacketWrapper<?> wrapper) {
        PacketWrapper<?>[] wrappers = PacketTransformationUtil.transform(wrapper);
        for (PacketWrapper<?> packet : wrappers) {
            packet.prepareForSend();
            receivePacketSilently(channel, packet.buffer);
        }
    }

    default User getUser(Object channel) {
        return USERS.get(channel);
    }

    default void setUser(Object channel, User user) {
        USERS.put(channel, user);
        PacketEvents.getAPI().getInjector().updateUser(channel, user);
    }

    default Object getChannel(String username) {
        return CHANNELS.get(username);
    }

    default void clearUserData(Object channel, @Nullable String name) {
        USERS.remove(channel);
        //Name is only accessible if the server sends the LOGIN_SUCCESS packet which contains name and UUID
        if (name != null) {
            CHANNELS.remove(name);
        }
    }
}
