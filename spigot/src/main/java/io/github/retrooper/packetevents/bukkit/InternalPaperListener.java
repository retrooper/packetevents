/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package io.github.retrooper.packetevents.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Used on Paper 1.21.7 and 1.21.8 because of changes due to their Configuration API;
 * This is a variant of the pre-1.20.5 {@link InternalBukkitListener}, as Paper
 * no longer creates a {@link Player} object during the configuration phase to align with vanilla.
 */
@NullMarked
@ApiStatus.Internal
public class InternalPaperListener implements Listener {

    private final InternalBukkitListener delegate;

    public InternalPaperListener(Plugin plugin) {
        this.delegate = new InternalBukkitListener(plugin);
    }

    // this may seem like a random event to choose, but this is the first event which
    // is called after the player object has been created; note that we can't extract
    // the reference to the player's connection yet, like before 1.20.5
    @SuppressWarnings("removal")
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSpawnLocation(org.spigotmc.event.player.PlayerSpawnLocationEvent event) {
        this.delegate.onPreJoin(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        this.delegate.onPostJoin(event.getPlayer());
    }
}
