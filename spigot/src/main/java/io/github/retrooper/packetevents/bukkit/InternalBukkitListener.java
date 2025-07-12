/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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
import io.github.retrooper.packetevents.manager.player.PlayerManagerImpl;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.UUID;

/**
 * Used on Paper/Spigot before 1.20.5
 */
@NullMarked
@ApiStatus.Internal
public class InternalBukkitListener implements Listener {

    static final String KICK_MESSAGE = "PacketEvents failed to inject into a channel";

    private final Plugin plugin;

    public InternalBukkitListener(Plugin plugin) {
        this.plugin = plugin;
    }

    // this is the first event which is called after the player object has been created;
    // note that we can't extract the reference to the player's connection yet
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            this.onPreJoin(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        this.onPostJoin(event.getPlayer());
    }

    void onPreJoin(Player player) {
        // save player in map for packet handler to consume
        PacketEventsAPI<?> api = PacketEvents.getAPI();
        Map<UUID, WeakReference<Player>> map = ((PlayerManagerImpl) api.getPlayerManager()).joiningPlayers;
        map.put(player.getUniqueId(), new WeakReference<>(player));
    }

    void onPostJoin(Player player) {
        PacketEventsAPI<?> api = PacketEvents.getAPI();
        User user = api.getPlayerManager().getUser(player);
        if (user != null) {
            // update player reference in encoder/decoder (doesn't matter if this was already done)
            SpigotChannelInjector injector = (SpigotChannelInjector) PacketEvents.getAPI().getInjector();
            injector.setPlayer(user.getChannel(), player);
            // remove from map; this is probably already removed anyway, but just make sure
            ((PlayerManagerImpl) api.getPlayerManager()).joiningPlayers.remove(player.getUniqueId());
            return;
        }

        // if this case occurs, we can't extract the connection from a fully joined player
        // or have internal connection for this player; this should not occur, kick them

        // remove from map just to be sure
        ((PlayerManagerImpl) api.getPlayerManager()).joiningPlayers.remove(player.getUniqueId());

        Object channel = api.getPlayerManager().getChannel(player);
        if (channel != null && FakeChannelUtil.isFakeChannel(channel)
                || (api.isTerminated() && !api.getSettings().isKickIfTerminated())) {
            // either fake channel or api terminated (and we don't kick)
            return;
        }

        // delay by a tick
        FoliaScheduler.getEntityScheduler().runDelayed(player, this.plugin, __ -> {
            // only kick if the player is actually still connected
            if (player.isConnected()) {
                player.kickPlayer(KICK_MESSAGE);
            }
        }, null, 0);
    }
}
