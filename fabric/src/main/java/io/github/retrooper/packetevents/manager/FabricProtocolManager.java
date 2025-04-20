/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ProtocolVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.impl.netty.manager.protocol.ProtocolManagerAbstract;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FabricProtocolManager extends ProtocolManagerAbstract {

    private final Map<UUID, Object> channels = new ConcurrentHashMap<>();
    private final Map<Object, User> users = new ConcurrentHashMap<>();
    private final boolean invert;
    private final PacketEventsAPI<?> packetEventsAPI;

    public FabricProtocolManager(PacketEventsAPI<?> packetEventsAPI, EnvType environment) {
        this.packetEventsAPI = packetEventsAPI;
        this.invert = environment == EnvType.CLIENT;
    }

    private void receivePacket0(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            if (ChannelHelper.pipelineHandlerNames(channel).contains("decompress")) {
                ChannelHelper.fireChannelReadInContext(channel, "decompress", byteBuf);
            } else {
                ChannelHelper.fireChannelRead(channel, byteBuf);
            }
        } else {
            ((ByteBuf) byteBuf).release();
        }
    }

    @Override
    public ProtocolVersion getPlatformVersion() {
        return ProtocolVersion.UNKNOWN; // TODO implement platform version
    }

    @Override
    public void sendPacket(Object channel, Object byteBuf) {
        if (this.invert) {
            this.receivePacket0(channel, byteBuf);
        } else {
            super.sendPacket(channel, byteBuf);
        }
    }

    @Override
    public void sendPacketSilently(Object channel, Object byteBuf) {
        if (this.invert) {
            super.receivePacketSilently(channel, byteBuf);
        } else {
            super.sendPacketSilently(channel, byteBuf);
        }
    }

    @Override
    public void writePacket(Object channel, Object byteBuf) {
        if (this.invert) {
            this.receivePacket0(channel, byteBuf);
        } else {
            super.writePacket(channel, byteBuf);
        }
    }

    @Override
    public void writePacketSilently(Object channel, Object byteBuf) {
        if (this.invert) {
            super.receivePacketSilently(channel, byteBuf);
        } else {
            super.writePacketSilently(channel, byteBuf);
        }
    }

    @Override
    public void receivePacket(Object channel, Object byteBuf) {
        if (this.invert) {
            // no way to specify whether to flush or not, so just don't
            super.writePacket(channel, byteBuf);
        } else {
            this.receivePacket0(channel, byteBuf);
        }
    }

    @Override
    public void receivePacketSilently(Object channel, Object byteBuf) {
        if (this.invert) {
            // no way to specify whether to flush or not, so just don't
            super.writePacketSilently(channel, byteBuf);
        } else {
            super.receivePacketSilently(channel, byteBuf);
        }
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public Collection<Object> getChannels() {
        return channels.values();
    }

    @Override 
    public User getUser(Object channel) {
        Object pipeline = ChannelHelper.getPipeline(channel);
        return users.get(pipeline);
    }

    @Override 
    public User removeUser(Object channel) {
        Object pipeline = ChannelHelper.getPipeline(channel);
        return users.remove(pipeline);
    }

    @Override
    public void setUser(Object channel, User user) {
        synchronized (channel) {
            Object pipeline = ChannelHelper.getPipeline(channel);
            users.put(pipeline, user);
        }
        packetEventsAPI.getInjector().updateUser(channel, user);
    }

    @Override 
    public Object getChannel(UUID uuid) {
        return channels.get(uuid);
    }

    @Override
    public void setChannel(UUID uuid, Object channel) {
        channels.put(uuid, channel);
    }

    @Override
    public boolean hasChannel(Object channel) {
        return channels.containsValue(channel);
    }
}
