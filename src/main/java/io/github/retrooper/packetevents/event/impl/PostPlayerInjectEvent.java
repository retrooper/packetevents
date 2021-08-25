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

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.type.PlayerEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelAbstract;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

/**
 * The {@code PostPlayerInjectEvent} event is fired after a successful injection.
 * Use the {@link #isAsync()} method to figure out if is being called sync or async.
 * A player is injected by PacketEvents each time they join the server.
 *
 * @author retrooper
 * @see <a href="https://github.com/retrooper/packetevents/blob/dev/src/main/java/io/github/retrooper/packetevents/handler/PacketHandlerInternal.java">https://github.com/retrooper/packetevents/blob/dev/src/main/java/io/github/retrooper/packetevents/handler/PacketHandlerInternal.java</a>
 * @since 1.3
 */
public class PostPlayerInjectEvent extends PacketEvent implements PlayerEvent {
    //TODO Rethink this event
    private final Player player;
    private final boolean async;

    public PostPlayerInjectEvent(Player player, boolean async) {
        this.player = player;
        this.async = async;
    }

    /**
     * This method returns the bukkit player object of the player that has been injected.
     * The player is guaranteed to not be null.
     *
     * @return Injected Player.
     */
    @NotNull
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * This method returns the cached netty channel of the player.
     *
     * @return Netty channel of the injected player.
     */
    @NotNull
    public Object getChannel() {
        return PacketEvents.get().getPlayerManager().getChannel(player);
    }

    @NotNull
    public InetSocketAddress getSocketAddress() {
        ChannelAbstract channelAbstract = ChannelAbstract.generate(getChannel());
        return (InetSocketAddress) channelAbstract.remoteAddress();
    }

    /**
     * This method returns the ClientVersion of the injected player.
     *
     * @return ClientVersion of injected player.
     * @see ClientVersion
     */
    @NotNull
    public ClientVersion getClientVersion() {
        return PacketEvents.get().getPlayerManager().getClientVersion(player);
    }

    /**
     * Has the event been called async or sync?
     *
     * @return Was the event call in an async context?
     */
    public boolean isAsync() {
        return async;
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
