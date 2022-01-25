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

package com.github.retrooper.packetevents.event.impl;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;

import java.net.InetSocketAddress;

public class PacketReceiveEvent extends ProtocolPacketEvent<Object> {
    private boolean cloned;

    public PacketReceiveEvent(ChannelAbstract channel, User user, Object player, ByteBufAbstract byteBuf) {
        super(PacketSide.CLIENT, channel, user, player, byteBuf);
    }

    public PacketReceiveEvent(ConnectionState connectionState, ChannelAbstract channel, User user, Object player, ByteBufAbstract byteBuf) {
        super(PacketSide.CLIENT, connectionState, channel, user, player, byteBuf);
    }

    public PacketReceiveEvent(Object channel, User user, Object player, Object rawByteBuf) {
        super(PacketSide.CLIENT, channel, user, player, rawByteBuf);
    }

    public PacketReceiveEvent(ConnectionState connectionState, Object channel, User user, Object player, Object rawByteBuf) {
        super(PacketSide.CLIENT, connectionState, channel, user, player, rawByteBuf);
    }

    public PacketReceiveEvent(boolean cloned, int packetID, PacketTypeCommon packetType,
                              ServerVersion serverVersion, InetSocketAddress socketAddress,
                              ChannelAbstract channel, User user, Object player, ByteBufAbstract byteBuf) {
        super(packetID, packetType, serverVersion, socketAddress,
                channel, user, player, byteBuf);
        this.cloned = cloned;
    }

    public boolean isCloned() {
        return cloned;
    }

    @Override
    public PacketReceiveEvent clone() {
        return new PacketReceiveEvent(true, getPacketId(),
                getPacketType(), getServerVersion(),
                getSocketAddress(), getChannel(),
                getUser(), getPlayer(), getByteBuf().duplicate());
    }

    @Override
    public void call(PacketListenerAbstract listener) {
        listener.onPacketReceive(this);
    }
}
