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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.type.PlayerEvent;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

/**
 * The {@code PostPlayerInjectEvent} event is fired after a successful injection.
 * A player is injected by PacketEvents each time they join the server.
 *
 * @author retrooper
 * @since 1.3
 */
public class PostPlayerInjectEvent extends PacketEvent implements PlayerEvent<Object> {
    //TODO Rethink this event
    private final Object player;
    private final ChannelAbstract channel;

    public PostPlayerInjectEvent(Object player) {
        this.player = player;
        this.channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
    }

    /**
     * This method returns the bukkit player object of the player that has been injected.
     * The player is guaranteed to not be null.
     *
     * @return Injected Player.
     */
    @NotNull
    @Override
    public Object getPlayer() {
        return player;
    }

    /**
     * This method returns the cached netty channel of the player.
     *
     * @return Netty channel of the injected player.
     */
    @NotNull
    public ChannelAbstract getChannel() {
        return channel;
    }

    @NotNull
    public InetSocketAddress getSocketAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }

    /**
     * This method returns the ClientVersion of the injected player.
     *
     * @return ClientVersion of injected player.
     * @see ClientVersion
     */
    @NotNull
    public ClientVersion getClientVersion() {
        return PacketEvents.getAPI().getPlayerManager().getClientVersion(channel);
    }

    @Override
    public void call(PacketListenerAbstract listener) {
        listener.onPostPlayerInject(this);
    }

    @Override
    public boolean isInbuilt() {
        return true;
    }
}
