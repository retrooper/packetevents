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

package io.github.retrooper.packetevents.impl.netty.manager.protocol;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ProtocolVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import io.netty.buffer.ByteBuf;

public abstract class ProtocolManagerAbstract implements ProtocolManager {
    @Override
    public abstract ProtocolVersion getPlatformVersion();

    @Override
    public void sendPacket(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            ChannelHelper.writeAndFlush(channel, byteBuf);
        } else {
            ((ByteBuf) byteBuf).release();
        }
    }

    @Override
    public void sendPacketSilently(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            ChannelHelper.writeAndFlushInContext(channel, PacketEvents.ENCODER_NAME, byteBuf);
        } else {
            ((ByteBuf) byteBuf).release();
        }
    }

    @Override
    public void writePacket(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            ChannelHelper.write(channel, byteBuf);
        } else {
            ((ByteBuf) byteBuf).release();
        }
    }

    @Override
    public void writePacketSilently(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            ChannelHelper.writeInContext(channel, PacketEvents.ENCODER_NAME, byteBuf);
        } else {
            ((ByteBuf) byteBuf).release();
        }
    }

    @Override
    public void receivePacket(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            ChannelHelper.fireChannelRead(channel, byteBuf);
        } else {
            ((ByteBuf) byteBuf).release();
        }
    }

    @Override
    public void receivePacketSilently(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            ChannelHelper.fireChannelReadInContext(channel, PacketEvents.ENCODER_NAME, byteBuf);
        } else {
            ((ByteBuf) byteBuf).release();
        }
    }

    @Override
    public ClientVersion getClientVersion(Object channel) {
        User user = getUser(channel);
        ClientVersion version = user.getClientVersion();
        if (version == null) {
            //TODO Change to getPlatformVersion()
            //Assume client version is same as the server version
            version = ClientVersion.getById(PacketEvents.getAPI().getServerManager().getVersion().getProtocolVersion());
        }
        return version;
    }
}
