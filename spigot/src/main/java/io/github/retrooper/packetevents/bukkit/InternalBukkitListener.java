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
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.FakeChannelUtil;
import io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class InternalBukkitListener implements Listener {

    private final Plugin plugin;

    public InternalBukkitListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        SpigotChannelInjector injector = (SpigotChannelInjector) PacketEvents.getAPI().getInjector();

        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        if (user == null) {
            //We did not inject this user
            Object channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
            //Check if it is a fake connection...
            if (!FakeChannelUtil.isFakeChannel(channel) && (!PacketEvents.getAPI().isTerminated() || PacketEvents.getAPI().getSettings().isKickIfTerminated())) {
                //Kick them, if they are not a fake player.
                FoliaScheduler.getRegionScheduler().runDelayed(plugin, player.getLocation(), (o) -> {
                    player.kickPlayer("PacketEvents 2.0 failed to inject");
                }, 0);
            }
            return;
        }

        // Set bukkit player object in the injectors
        injector.updatePlayer(user, player);
    }
}
