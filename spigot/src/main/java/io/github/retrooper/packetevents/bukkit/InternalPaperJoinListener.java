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
import io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import io.github.retrooper.packetevents.injector.handlers.PacketEventsEncoder;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.netty.channel.Channel;
import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.event.connection.PlayerConnectionValidateLoginEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * We need to save the player instance before the first play-phase packets get sent by the server. Before 1.21.9,
 * we used the {@link org.spigotmc.event.player.PlayerSpawnLocationEvent}, which was called at the perfect spot.
 * As of 1.21.9, this event is now called during the configuration phase, without a player instance being available.<br/>
 * So, to still get the player instance in time, we will now pause processing packets after the configuration phase
 * ends and wait for the {@link PlayerJoinEvent}. When the {@link PlayerJoinEvent} gets called, we are able
 * to save the player instance and resume processing packets.<br/>
 * This is not a great solution, but I don't think there is a better one at the moment.
 */
@NullMarked
@ApiStatus.Internal
public class InternalPaperJoinListener implements Listener {

    private final InternalBukkitListener delegate;

    public InternalPaperJoinListener(Plugin plugin) {
        this.delegate = new InternalBukkitListener(plugin);
    }

    private void setChannelFreeze(Channel channel, boolean freeze) {
        channel.eventLoop().execute(() -> {
            try {
                SpigotChannelInjector injector = (SpigotChannelInjector) PacketEvents.getAPI().getInjector();
                PacketEventsEncoder encoder = injector.getEncoder(channel);
                if (encoder != null) {
                    encoder.setHold(channel, freeze);
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerConnectionValidateLoginEvent event) {
        if (!event.isAllowed()) {
            return; // player will be kicked
        }
        if (!(event.getConnection() instanceof PlayerConfigurationConnection)) {
            return; // player isn't exiting configuration phase, skip
        }
        Channel channel = (Channel) SpigotReflectionUtil.getChannelFromPaperConnection(event.getConnection());
        this.setChannelFreeze(channel, true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        this.delegate.onPostJoin(event.getPlayer());

        Channel channel = (Channel) SpigotReflectionUtil.getChannel(event.getPlayer());
        if (channel != null) {
            this.setChannelFreeze(channel, false);
        }
    }
}
