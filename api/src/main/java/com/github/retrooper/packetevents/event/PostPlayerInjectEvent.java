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

package com.github.retrooper.packetevents.event;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
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
    private final Object player;
    private final Object channel;

    public PostPlayerInjectEvent(Object player) {
        this.player = player;
        this.channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
    }

    @NotNull
    @Override
    public Object getPlayer() {
        return player;
    }

    @NotNull
    public Object getChannel() {
        return channel;
    }

    @NotNull
    public InetSocketAddress getSocketAddress() {
        return (InetSocketAddress) ChannelHelper.remoteAddress(channel);
    }

    /**
     * This method returns the ClientVersion of the injected player.
     *
     * @return ClientVersion of injected player.
     * @see ClientVersion
     */
    @NotNull
    public ClientVersion getClientVersion() {
        return PacketEvents.getAPI().getPlayerManager().getClientVersion(player);
    }

    @Override
    public void call(PacketListenerCommon listener) {
        listener.onPostPlayerInject(this);
    }

    @Override
    public boolean isInbuilt() {
        return true;
    }
}
