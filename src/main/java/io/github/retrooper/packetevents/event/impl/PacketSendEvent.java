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

package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.ProtocolPacketEvent;
import io.github.retrooper.packetevents.protocol.ConnectionState;
import io.github.retrooper.packetevents.protocol.PacketSide;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelAbstract;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.bukkit.entity.Player;

public class PacketSendEvent extends ProtocolPacketEvent {
    private Runnable postTask = null;
    private PacketWrapper currentPacketWrapper;

    public PacketSendEvent(ChannelAbstract channel, Player player, ByteBufAbstract byteBuf, Runnable postTask) {
        super(PacketSide.SERVER, channel, player, byteBuf);
        this.postTask = postTask;
    }

    public PacketSendEvent(ConnectionState connectionState, ChannelAbstract channel, Player player, ByteBufAbstract byteBuf, Runnable postTask) {
        super(PacketSide.SERVER, connectionState, channel, player, byteBuf);
        this.postTask = postTask;
    }

    public PacketSendEvent(Object channel, Player player, Object rawByteBuf, Runnable postTask) {
        super(PacketSide.SERVER, channel, player, rawByteBuf);
        this.postTask = postTask;
    }

    public PacketSendEvent(ConnectionState connectionState, Object channel, Player player, Object rawByteBuf, Runnable postTask) {
        super(PacketSide.SERVER, connectionState, channel, player, rawByteBuf);
        this.postTask = postTask;
    }

    public PacketSendEvent(ChannelAbstract channel, Player player, ByteBufAbstract byteBuf) {
        super(PacketSide.SERVER, channel, player, byteBuf);
        this.postTask = null;
    }

    public PacketSendEvent(ConnectionState connectionState, ChannelAbstract channel, Player player, ByteBufAbstract byteBuf) {
        super(PacketSide.SERVER, connectionState, channel, player, byteBuf);
        this.postTask = null;
    }

    public PacketSendEvent(Object channel, Player player, Object rawByteBuf) {
        super(PacketSide.SERVER, channel, player, rawByteBuf);
        this.postTask = null;
    }

    public PacketSendEvent(ConnectionState connectionState, Object channel, Player player, Object rawByteBuf) {
        super(PacketSide.SERVER, connectionState, channel, player, rawByteBuf);
        this.postTask = null;
    }

    public Runnable getPostTask() {
        return postTask;
    }

    public void setPostTask(Runnable postTask) {
        this.postTask = postTask;
    }

    @Deprecated
    public PacketWrapper getCurrentPacketWrapper() {
        return currentPacketWrapper;
    }

    @Deprecated
    public void setCurrentPacketWrapper(PacketWrapper currentPacketWrapper) {
        this.currentPacketWrapper = currentPacketWrapper;
    }

    @Override
    public void call(PacketListenerAbstract listener) {
        listener.onPacketSend(this);
    }
}
