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

import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;
import io.github.retrooper.packetevents.event.eventtypes.PlayerEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code PlayerEjectEvent} event is fired whenever a player is ejected.
 * This class implements {@link CancellableEvent} and {@link PlayerEvent}.
 *
 * @author retrooper
 * @see <a href="https://github.com/retrooper/packetevents/blob/dev/src/main/java/io/github/retrooper/packetevents/handler/PacketHandlerInternal.java">https://github.com/retrooper/packetevents/blob/dev/src/main/java/io/github/retrooper/packetevents/handler/PacketHandlerInternal.java</a>
 * @since 1.6.9
 */
public final class PlayerEjectEvent extends PacketEvent implements CancellableEvent, PlayerEvent {
    private final Player player;
    private boolean cancelled;

    public PlayerEjectEvent(final Player player) {
        this.player = player;
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
     * This method returns the bukkit player object of the player being ejected.
     * The player object is guaranteed to NOT be null.
     *
     * @return Ejected player.
     */
    @NotNull
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * @return Is the event called async?
     * @deprecated Now always false
     */
    @Deprecated
    public boolean isAsync() {
        return false;
    }

    @Override
    public void call(PacketListenerAbstract listener) {
        listener.onPlayerEject(this);
    }

    @Override
    public boolean isInbuilt() {
        return true;
    }
}
