/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2016-2021 retrooper and contributors
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
import io.github.retrooper.packetevents.event.impl.PostPlayerInjectEvent;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import io.github.retrooper.packetevents.utils.versionlookup.VersionLookupUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.net.InetSocketAddress;
import java.util.UUID;

public class BukkitEventProcessorInternal implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent e) {
        final Player player = e.getPlayer();
        if (!PacketEvents.get().getSettings().shouldUseCompatibilityInjector()) {
            PacketEvents.get().getInjector().injectPlayer(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        InetSocketAddress address = player.getAddress();

        boolean shouldInject = PacketEvents.get().getSettings().shouldUseCompatibilityInjector() || !(PacketEvents.get().getInjector().hasInjected(e.getPlayer()));
        //Inject now if we are using the compatibility-injector or inject if the early injector failed to inject them.
        if (shouldInject) {
            PacketEvents.get().getInjector().injectPlayer(player);
        }

        boolean dependencyAvailable = VersionLookupUtils.isDependencyAvailable();
        PacketEvents.get().getPlayerUtils().loginTime.put(player.getUniqueId(), System.currentTimeMillis());
        //A supported dependency is available, we need to first ask the dependency for the client version.
        if (dependencyAvailable) {
            //We are resolving version one tick later for extra safety. Some dependencies throw exceptions if we throw too early.
            Bukkit.getScheduler().runTaskLaterAsynchronously(PacketEvents.get().getPlugin(), () -> {
                try {
                    int protocolVersion = VersionLookupUtils.getProtocolVersion(player);
                    ClientVersion version = ClientVersion.getClientVersion(protocolVersion);
                    PacketEvents.get().getPlayerUtils().clientVersionsMap.put(address, version);
                } catch (Exception ignored) {

                }
                PacketEvents.get().getEventManager().callEvent(new PostPlayerInjectEvent(player, true));
            }, 1L);
        } else {
            //Dependency isn't available, we can already call the post player inject event.
            PacketEvents.get().getEventManager().callEvent(new PostPlayerInjectEvent(e.getPlayer(), false));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        InetSocketAddress address = player.getAddress();
        //Cleanup user data
        PacketEvents.get().getPlayerUtils().loginTime.remove(uuid);
        PacketEvents.get().getPlayerUtils().playerPingMap.remove(uuid);
        PacketEvents.get().getPlayerUtils().playerSmoothedPingMap.remove(uuid);
        PacketEvents.get().getPlayerUtils().clientVersionsMap.remove(address);
        PacketEvents.get().getPlayerUtils().tempClientVersionMap.remove(address);
        PacketEvents.get().getPlayerUtils().keepAliveMap.remove(uuid);
        PacketEvents.get().getPlayerUtils().channels.remove(player.getName());

    }
}
