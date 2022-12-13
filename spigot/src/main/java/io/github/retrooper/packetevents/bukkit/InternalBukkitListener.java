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
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import org.bukkit.Bukkit;
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
            Object channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);

            // Due to how processors work, we aren't guaranteed to have a channel
            // This is called on the bukkit thread instead of the netty thread
            // Even if the netty thread set a channel in the map in real time, caches
            // mean that we may not see it.  Therefore, use synchronized to fix cache issues.
            //
            // Just fall back to synchronized and reflection for performance reasons.
            synchronized (channel) {
                if (!ChannelHelper.isOpen(channel)) {
                    user = PacketEvents.getAPI().getPlayerManager().getUser(player);
                    PacketEvents.getAPI().getLogManager().warn("If you see this message tell DefineOutside so we know to keep this logic in.");
                }

                // We still don't have it
                if (user == null) {
                    Bukkit.getScheduler().runTask(plugin, () -> player.kickPlayer("PacketEvents 2.0 failed to inject"));
                    return;
                }
            }
        }

        // Set bukkit player object in the injectors
        injector.updatePlayer(user, player);
    }
}
