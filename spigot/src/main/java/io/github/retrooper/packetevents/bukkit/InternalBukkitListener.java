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

package io.github.retrooper.packetevents.bukkit;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class InternalBukkitListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        SpigotChannelInjector injector = (SpigotChannelInjector) PacketEvents.getAPI().getInjector();
        //By accessing user with the player object, we ensure that a valid user is cached.
        //In the spigot PlayerManager impl, if there was no user cached,
        //we will create one with help of the Player object.
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        if (user == null) return;
        //By accessing the client version with the player object, we should ensure the client version is valid.
        //In the spigot PlayerManager impl, we ask protocol translation dependencies for the version
        //if we failed to catch it.(shouldn't ever happen really)
        //TODO With Via we might have to run this a tick AFTER join event (atleast when via & viabackwards & rewind are present)
        ClientVersion version = PacketEvents.getAPI().getPlayerManager().getClientVersion(player);
        injector.updatePlayer(user, player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Object channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        //When we receive LOGIN_SUCCESS, we cache username with their channel.
        //Here we clean that up.
        PacketEventsImplHelper.handleDisconnection(channel, player.getUniqueId());
    }
}
