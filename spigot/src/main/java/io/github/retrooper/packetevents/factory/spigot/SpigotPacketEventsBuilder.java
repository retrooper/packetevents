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

package io.github.retrooper.packetevents.factory.spigot;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PostPlayerInjectEvent;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.injector.InternalPacketListener;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.LogManager;
import io.github.retrooper.packetevents.bstats.Metrics;
import io.github.retrooper.packetevents.handlers.SpigotChannelInjector;
import io.github.retrooper.packetevents.manager.player.PlayerManagerImpl;
import io.github.retrooper.packetevents.manager.server.ServerManagerImpl;
import io.github.retrooper.packetevents.processor.InternalBukkitListener;
import io.github.retrooper.packetevents.utils.BukkitLogManager;
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.CustomPipelineUtil;
import io.github.retrooper.packetevents.utils.netty.NettyManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotPacketEventsBuilder {
    private static PacketEventsAPI<Plugin> API_INSTANCE;

    public static void clearBuildCache() {
        API_INSTANCE = null;
    }

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
            private final PacketEventsSettings settings = inSettings;
            private final ServerManager serverManager = new ServerManagerImpl();
            private final PlayerManager playerManager = new PlayerManagerImpl();
            private final NettyManager nettyManager = new NettyManagerImpl();
            private final SpigotChannelInjector injector = new SpigotChannelInjector();
            private final InternalBukkitListener internalBukkitListener = new InternalBukkitListener();
            private final LogManager logManager = new BukkitLogManager();
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
                        SpigotReflectionUtil.init();
                        CustomPipelineUtil.init();
                    } catch (Exception ex) {
                        throw new IllegalStateException(ex);
                    }

                    injector.load();
                    lateBind = !injector.isBound();
                    //If late-bind is enabled, we will inject a bit later.
                    if (!lateBind) {
                        injector.inject();
                    }

                    loaded = true;

                    //Register internal packet listener (should be the first listener)
                    //This listener doesn't do any modifications to the packets, just reads data
                    getEventManager().registerListener(new InternalPacketListener(),
                            PacketListenerPriority.LOWEST, true);
                }
            }

            @Override
            public boolean isLoaded() {
                return loaded;
            }

            @Override
            public void init() {
                //Load if we haven't loaded already
                load();
                if (!initialized) {
                    if (settings.shouldCheckForUpdates()) {
                        getUpdateChecker().handleUpdateCheck();
                    }

                    if (settings.isbStatsEnabled()) {
                        Metrics metrics = new Metrics((JavaPlugin) plugin, 11327);
                        //Just to have an idea what versions of packetevents people use
                        metrics.addCustomChart(new Metrics.SimplePie("packetevents_version", () -> {
                            return getVersion().toString() + "-beta";//TODO Cut off "-beta" once 2.0 releases
                        }));
                    }

                    PacketType.Play.Client.load();
                    PacketType.Play.Server.load();

                    Runnable postInjectTask = () -> {
                        Bukkit.getPluginManager().registerEvents(internalBukkitListener, plugin);
                        for (final Player p : Bukkit.getOnlinePlayers()) {
                            try {
                                injector.injectPlayer(p, ConnectionState.PLAY);
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
            public boolean isInitialized() {
                return initialized;
            }

            @Override
            public void terminate() {
                if (initialized) {
                    //Eject all players
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        injector.ejectPlayer(p);
                    }
                    //Eject the injector if needed(depends on the injector implementation)
                    injector.eject();
                    //Unregister all our listeners
                    getEventManager().unregisterAllListeners();
                    initialized = false;
                }
            }

            @Override
            public Plugin getPlugin() {
                return plugin;
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
            public PacketEventsSettings getSettings() {
                return settings;
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
            public LogManager getLogManager() {
                return logManager;
            }
        };
    }
}
