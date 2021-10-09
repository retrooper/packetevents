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
import com.github.retrooper.packetevents.event.type.CancellableEvent;
import com.github.retrooper.packetevents.event.type.PlayerEvent;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;

/**
 * The {@code PlayerInjectEvent} event is fired whenever a player is injected.
 * A player is injected by PacketEvents whenever they join the server.
 * This class implements {@link CancellableEvent} and {@link PlayerEvent}.
 *
 * @author retrooper
 * @see <a href="https://github.com/retrooper/packetevents/blob/dev/src/main/java/io/github/retrooper/packetevents/handler/PacketHandlerInternal.java">https://github.com/retrooper/packetevents/blob/dev/src/main/java/io/github/retrooper/packetevents/handler/PacketHandlerInternal.java</a>
 * @since 1.6.9
 */
//TODO Recode this event, looks questionable :0
public final class PlayerInjectEvent extends PacketEvent implements CancellableEvent, PlayerEvent<Object> {
    private final Object player;
    private final InetSocketAddress address;
    private boolean cancelled;

    public PlayerInjectEvent(Object player) {
        this.player = player;
        ChannelAbstract channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        this.address = (InetSocketAddress) channel.remoteAddress();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean value) {
        cancelled = value;
    }

    /**
     * This method returns the bukkit player object of the player being injected.
     * This player might not be fully initialized.
     *
     * @return Injected Player.
     */
    @Nullable
    @Override
    public Object getPlayer() {
        return player;
    }

    /**
     * This method returns the socket address injecting player.
     *
     * @return Socket address of the injecting player.
     */
    @Nullable
    public InetSocketAddress getSocketAddress() {
        return address;
    }

    @Override
    public void call(PacketListenerAbstract listener) {
        listener.onPlayerInject(this);
    }

    @Override
    public boolean isInbuilt() {
        return true;
    }
}
