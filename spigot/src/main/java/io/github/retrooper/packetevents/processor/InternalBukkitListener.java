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

package io.github.retrooper.packetevents.processor;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.impl.PostPlayerInjectEvent;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.handlers.SpigotChannelInjector;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class InternalBukkitListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent e) {
        final Player player = e.getPlayer();
        SpigotChannelInjector injector = (SpigotChannelInjector) PacketEvents.getAPI().getInjector();
        if (injector.shouldInjectEarly()) {
            PacketEvents.getAPI().getInjector().injectPlayer(player, null);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        SpigotChannelInjector injector = (SpigotChannelInjector) PacketEvents.getAPI().getInjector();

        boolean injectEarly = injector.shouldInjectEarly();
        boolean shouldInject = !injectEarly || !injector.hasInjected(e.getPlayer());
        //By accessing user with the player object, we ensure that a valid user is cached.
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);

        //Inject now if we are using the compatibility-injector or inject if the early injector failed to inject them.
        if (shouldInject) {
            //Late injection
            injector.injectPlayer(player, ConnectionState.PLAY);
        }

        PacketEvents.getAPI().getEventManager().callEvent(new PostPlayerInjectEvent(e.getPlayer()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        ChannelAbstract channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        //Cleanup user data
        PacketEvents.getAPI().getPlayerManager().USERS.remove(channel);
        PacketEvents.getAPI().getPlayerManager().CHANNELS.remove(player.getName());
        PacketEvents.getAPI().getPlayerManager().PLAYER_ATTRIBUTES.remove(player.getUniqueId());
    }
}
