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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.impl.PostPlayerInjectEvent;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.PEVersion;
import com.github.retrooper.packetevents.util.updatechecker.UpdateChecker;
import io.github.retrooper.packetevents.bstats.Metrics;
import io.github.retrooper.packetevents.handlers.GlobalChannelInjector;
import io.github.retrooper.packetevents.manager.player.PlayerManagerImpl;
import io.github.retrooper.packetevents.manager.server.ServerManagerImpl;
import io.github.retrooper.packetevents.processor.InternalBukkitListener;
import io.github.retrooper.packetevents.processor.InternalPacketListener;
import io.github.retrooper.packetevents.utils.MinecraftReflectionUtil;
import io.github.retrooper.packetevents.utils.netty.NettyManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PacketEventsBuilder {
    private static PacketEventsAPI<Plugin> API_INSTANCE;

    public static PacketEventsAPI<Plugin> build(Plugin plugin) {
        if (API_INSTANCE == null) {
            API_INSTANCE = buildNoCache(plugin);
        }
        return API_INSTANCE;
    }

    public static PacketEventsAPI<Plugin> build(Plugin plugin, PacketEventsSettings settings) {
        if (API_INSTANCE == null) {
            API_INSTANCE = buildNoCache(plugin, settings);
        }
        return API_INSTANCE;
    }

    public static PacketEventsAPI<Plugin> buildNoCache(Plugin plugin) {
        return buildNoCache(plugin, new PacketEventsSettings());
    }

    public static PacketEventsAPI<Plugin> buildNoCache(Plugin plugin, PacketEventsSettings inSettings) {
        return new PacketEventsAPI<Plugin>() {
            private final EventManager eventManager = new EventManager();
            private final PacketEventsSettings settings = inSettings;
            private final Logger logger = Logger.getLogger(PacketEventsAPI.class.getName());
            private final ServerManager serverManager = new ServerManagerImpl();
            private final PlayerManager playerManager = new PlayerManagerImpl();
            private final NettyManager nettyManager = new NettyManagerImpl();
            private final GlobalChannelInjector injector = new GlobalChannelInjector();
            private final UpdateChecker updateChecker = new UpdateChecker();
            private final InternalBukkitListener internalBukkitListener = new InternalBukkitListener();
            private boolean loaded;
            private boolean initialized;
            private boolean lateBind = false;

            @Override
            public void load() {
                if (!loaded) {
                    //Resolve server version and cache
                    PacketEvents.IDENTIFIER = "pe-" + plugin.getName().toLowerCase();
                    PacketEvents.ENCODER_NAME = "pe-encoder-" + plugin.getName().toLowerCase();
                    PacketEvents.DECODER_NAME = "pe-decoder-" + plugin.getName().toLowerCase();
                    PacketEvents.CONNECTION_NAME = "pe-connection-handler-" + plugin.getName().toLowerCase();
                    PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-" + plugin.getName().toLowerCase();
                    try {
                        MinecraftReflectionUtil.init();
                    } catch (Exception ex) {
                        throw new IllegalStateException(ex);
                    }


                    injector.load();
                    lateBind = !injector.isBound();
                    //If late-bind is enabled or ViaVersion is present, we will inject a bit later.
                    if (!lateBind) {
                        injector.inject();
                    }

                    loaded = true;

                    //Register internal packet listener (should be the first listener)
                    getEventManager().registerListener(new InternalPacketListener(), PacketListenerPriority.LOWEST);
                }
            }

            @Override
            public void init() {
                //Load if we haven't loaded already
                load();
                if (!initialized) {
                    if (settings.shouldCheckForUpdates()) {
                        updateChecker.handleUpdateCheck();
                    }

                    if (settings.isbStatsEnabled()) {
                        Metrics metrics = new Metrics((JavaPlugin) plugin, 11327);
                    }

                    PacketType.Play.Client.load();
                    PacketType.Play.Server.load();

                    Runnable postInjectTask = () -> {
                        Bukkit.getPluginManager().registerEvents(internalBukkitListener, plugin);
                        for (final Player p : Bukkit.getOnlinePlayers()) {
                            try {
                                injector.injectPlayer(p);
                                getEventManager().callEvent(new PostPlayerInjectEvent(p));
                            } catch (Exception ex) {
                                p.kickPlayer("Failed to inject... Please rejoin!");
                            }
                        }
                    };

                    if (lateBind) {
                        //If late-bind is enabled, we still need to inject (after all plugins enabled).
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, injector::inject);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, postInjectTask);
                    } else {
                        postInjectTask.run();
                    }

                    initialized = true;
                }
            }

            @Override
            public void terminate() {
                if (initialized) {
                    //Eject all players
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        injector.ejectPlayer(p);
                    }
                    //Eject the injector if needed
                    injector.eject();
                    //Unregister all our listeners
                    getEventManager().unregisterAllListeners();
                    initialized = false;
                }
            }

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
                return PacketEvents.VERSION;
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

            @Override
            public UpdateChecker getUpdateChecker() {
                return updateChecker;
            }
        };
    }
}
