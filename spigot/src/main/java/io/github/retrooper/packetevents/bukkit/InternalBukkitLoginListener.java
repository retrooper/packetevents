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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.FakeChannelUtil;
import io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import static io.github.retrooper.packetevents.bukkit.InternalBukkitListener.KICK_MESSAGE;

/**
 * Used on Paper 1.20.5-1.21.6 or Spigot 1.20.5+
 */
@ApiStatus.Internal
@NullMarked
public class InternalBukkitLoginListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        PacketEventsAPI<?> api = PacketEvents.getAPI();
        User user = api.getPlayerManager().getUser(event.getPlayer());
        if (user != null) {
            // if the user can be resolved from this player, save in encoder/decoder
            SpigotChannelInjector injector = (SpigotChannelInjector) api.getInjector();
            injector.updatePlayer(user, event.getPlayer());
            return; // we're done
        }
        Object channel = api.getPlayerManager().getChannel(event.getPlayer());
        if (channel != null && FakeChannelUtil.isFakeChannel(channel)
                || (api.isTerminated() && !api.getSettings().isKickIfTerminated())) {
            // either fake channel or api terminated (and we don't kick)
            return;
        }
        // since 1.20.5 and cookie packets, CraftBukkit associates the login listener with the player
        // before calling the login event; if this fails on 1.20.5+, something broke a lot
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, KICK_MESSAGE);
    }
}
