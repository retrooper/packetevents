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

import io.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.impl.PostPlayerInjectEvent;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class InternalBukkitListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent e) {
        final Player player = e.getPlayer();
        if (PacketEvents.get().getInjector().shouldInjectEarly()) {
            PacketEvents.get().getInjector().injectPlayer(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        boolean shouldInject = !PacketEvents.get().getInjector().shouldInjectEarly() || !PacketEvents.get().getInjector().hasInjected(e.getPlayer());
        //Inject now if we are using the compatibility-injector or inject if the early injector failed to inject them.
        if (shouldInject) {
            PacketEvents.get().getInjector().injectPlayer(player);
        }

        PacketEvents.get().getEventManager().callEvent(new PostPlayerInjectEvent(e.getPlayer()));
        ServerManager.ENTITY_ID_CACHE.putIfAbsent(e.getPlayer().getEntityId(), e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Object channel = PacketEvents.get().getPlayerManager().getChannel(player);
        //Cleanup user data
        PacketEvents.get().getPlayerManager().clientVersions.remove(channel);
        PacketEvents.get().getPlayerManager().connectionStates.remove(channel);
        PacketEvents.get().getPlayerManager().channels.remove(player.getName());
        ServerManager.ENTITY_ID_CACHE.remove(player.getEntityId());
    }


    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        ServerManager.ENTITY_ID_CACHE.putIfAbsent(entity.getEntityId(), entity);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        ServerManager.ENTITY_ID_CACHE.remove(event.getEntity().getEntityId());
    }
}
