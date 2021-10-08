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

package com.github.retrooper.packetevents.factory.bukkit;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.PEVersion;
import io.github.retrooper.packetevents.handlers.GlobalChannelInjector;
import io.github.retrooper.packetevents.manager.player.PlayerManagerImpl;
import io.github.retrooper.packetevents.manager.server.ServerManagerImpl;
import io.github.retrooper.packetevents.utils.netty.NettyManagerImpl;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

public class PacketEventsBuilder {

    public static PacketEventsAPI<Plugin> build(Plugin plugin) {
        return new PacketEventsAPI<Plugin>() {
            private final EventManager eventManager = new EventManager();
            private final PEVersion version = new PEVersion(2, 0, 0);
            private final PacketEventsSettings settings = new PacketEventsSettings();
            private final Logger logger = Logger.getLogger(PacketEventsAPI.class.getName());
            private final ServerManager serverManager = new ServerManagerImpl();
            private final PlayerManager playerManager = new PlayerManagerImpl();
            private final NettyManager nettyManager = new NettyManagerImpl();
            private final ChannelInjector injector = new GlobalChannelInjector();

            @Override
            public Plugin getPlugin() {
                return (Plugin) plugin;
            }

            @Override
            public ServerManager getServerManager() {
                return serverManager;
            }

            @Override
            public PlayerManager getPlayerManager() {
                return playerManager;
            }

            @Override
            public EventManager getEventManager() {
                return eventManager;
            }

            @Override
            public PacketEventsSettings getSettings() {
                return settings;
            }

            @Override
            public PEVersion getVersion() {
                return version;
            }

            @Override
            public Logger getLogger() {
                return logger;
            }

            @Override
            public NettyManager getNettyManager() {
                return nettyManager;
            }

            @Override
            public ChannelInjector getInjector() {
                return injector;
            }
        };
    }
}
