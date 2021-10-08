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
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;

public class PacketReceiveEvent extends ProtocolPacketEvent {
    private PacketWrapper<?> lastUsedWrapper;

    public PacketReceiveEvent(ChannelAbstract channel, Object player, ByteBufAbstract byteBuf) {
        super(PacketSide.CLIENT, channel, player, byteBuf);
    }

    public PacketReceiveEvent(ConnectionState connectionState, ChannelAbstract channel, Object player, ByteBufAbstract byteBuf) {
        super(PacketSide.CLIENT, connectionState, channel, player, byteBuf);
    }

    public PacketReceiveEvent(Object channel, Object player, Object rawByteBuf) {
        super(PacketSide.CLIENT, channel, player, rawByteBuf);
    }

    public PacketReceiveEvent(ConnectionState connectionState, Object channel, Object player, Object rawByteBuf) {
        super(PacketSide.CLIENT, connectionState, channel, player, rawByteBuf);
    }

    @Override
    public void call(PacketListenerAbstract listener) {
        listener.onPacketReceive(this);
    }

    @Deprecated
    public PacketWrapper<?> getLastUsedWrapper() {
        return lastUsedWrapper;
    }

    @Deprecated
    public void setLastUsedWrapper(PacketWrapper<?> currentPacketWrapper) {
        this.lastUsedWrapper = currentPacketWrapper;
    }
}
