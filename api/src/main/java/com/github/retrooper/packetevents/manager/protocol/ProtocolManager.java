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

package com.github.retrooper.packetevents.manager.protocol;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ProtocolVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.PacketTransformationUtil;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface ProtocolManager {
    Map<UUID, Object> CHANNELS = new ConcurrentHashMap<>();
    // Use SocketAddress because ProtocolLib wraps Channels with NettyChannelProxy class
    Map<Object, User> USERS = new ConcurrentHashMap<>();

    default Collection<User> getUsers() {
        return USERS.values();
    }

    default Collection<Object> getChannels() {
        return CHANNELS.values();
    }

    //Methods to implement
    ProtocolVersion getPlatformVersion();
    void sendPacket(Object channel, Object byteBuf);
    void sendPacketSilently(Object channel, Object byteBuf);
    void writePacket(Object channel, Object byteBuf);
    void writePacketSilently(Object channel, Object byteBuf);
    void receivePacket(Object channel, Object byteBuf);
    void receivePacketSilently(Object channel, Object byteBuf);
    ClientVersion getClientVersion(Object channel);
    //TODO Define method that accepts an array of channels/set/list of channels.

    default void sendPackets(Object channel, Object... byteBuf) {
        for (Object buf : byteBuf) {
            sendPacket(channel, buf);
        }
    }

    default void sendPacketsSilently(Object channel, Object... byteBuf) {
        for (Object buf : byteBuf) {
            sendPacketSilently(channel, buf);
        }
    }

    default void writePackets(Object channel, Object... byteBuf) {
        for (Object buf : byteBuf) {
            writePacket(channel, buf);
        }
    }

    default void writePacketsSilently(Object channel, Object... byteBuf) {
        for (Object buf : byteBuf) {
            writePacketSilently(channel, buf);
        }
    }

    default void receivePackets(Object channel, Object... byteBuf) {
        for (Object buf : byteBuf) {
            receivePacket(channel, buf);
        }
    }

    default void receivePacketsSilently(Object channel, Object... byteBuf) {
        for (Object buf : byteBuf) {
            receivePacketSilently(channel, buf);
        }
    }


    default void setClientVersion(Object channel, ClientVersion version) {
        getUser(channel).setClientVersion(version);
    }

    @ApiStatus.Internal
    default Object[] transformWrappers(PacketWrapper<?> wrapper, Object channel, boolean outgoing) {
        //It is possible that our packet transformer util decides to transform one wrapper into multiple packets.
        //(Correcting some mistakes on your end)
        PacketWrapper<?>[] wrappers = PacketTransformationUtil.transform(wrapper);
        Object[] buffers = new Object[wrappers.length];
        for (int i = 0; i < wrappers.length; i++) {
            PacketWrapper<?> wrappper = wrappers[i];
            synchronized (wrappper.bufferLock) {
                wrappper.prepareForSend(channel, outgoing);
                buffers[i] = wrappper.buffer;
                // Fix race condition when sending packets to multiple people (due to when the buffer is freed)
                wrappper.buffer = null;
            }
        }
        return buffers;
    }

    default void sendPacket(Object channel, PacketWrapper<?> wrapper) {
        Object[] transformed = transformWrappers(wrapper, channel, true);
        sendPackets(channel, transformed);
    }

    default void sendPacketSilently(Object channel, PacketWrapper<?> wrapper) {
        Object[] transformed = transformWrappers(wrapper, channel, true);
        sendPacketsSilently(channel, transformed);
    }

    default void writePacket(Object channel, PacketWrapper<?> wrapper) {
        Object[] transformed = transformWrappers(wrapper, channel, true);
        writePackets(channel, transformed);
    }

    default void writePacketSilently(Object channel, PacketWrapper<?> wrapper) {
        Object[] transformed = transformWrappers(wrapper, channel, true);
        writePacketsSilently(channel, transformed);
    }

    default void receivePacket(Object channel, PacketWrapper<?> wrapper) {
        Object[] transformed = transformWrappers(wrapper, channel, false);
        receivePackets(channel, transformed);
    }

    default void receivePacketSilently(Object channel, PacketWrapper<?> wrapper) {
        Object[] transformed = transformWrappers(wrapper, channel, false);
        receivePacketsSilently(channel, transformed);
    }

    default User getUser(Object channel) {
        Object pipeline = ChannelHelper.getPipeline(channel);
        return USERS.get(pipeline);
    }

    default User removeUser(Object channel) {
        Object pipeline = ChannelHelper.getPipeline(channel);
        return USERS.remove(pipeline);
    }

    default void setUser(Object channel, User user) {
        synchronized (channel) {
            Object pipeline = ChannelHelper.getPipeline(channel);
            USERS.put(pipeline, user);
        }
        PacketEvents.getAPI().getInjector().updateUser(channel, user);
    }

    default Object getChannel(UUID uuid) {
        return CHANNELS.get(uuid);
    }
}
