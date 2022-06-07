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
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.InternalPacketListener;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.LogManager;
import com.github.retrooper.packetevents.util.PEVersion;
import io.github.retrooper.packetevents.bstats.Metrics;
import io.github.retrooper.packetevents.bukkit.InternalBukkitListener;
import io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import io.github.retrooper.packetevents.manager.player.PlayerManagerImpl;
import io.github.retrooper.packetevents.manager.protocol.ProtocolManagerImpl;
import io.github.retrooper.packetevents.manager.server.ServerManagerImpl;
import io.github.retrooper.packetevents.netty.NettyManagerImpl;
import io.github.retrooper.packetevents.util.BukkitLogManager;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportUtil;
import io.github.retrooper.packetevents.util.viaversion.CustomPipelineUtil;
import io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicBoolean;

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
            private final ProtocolManager protocolManager = new ProtocolManagerImpl();
            private final ServerManager serverManager = new ServerManagerImpl();
            private final PlayerManager playerManager = new PlayerManagerImpl();
            private final NettyManager nettyManager = new NettyManagerImpl();
            private final SpigotChannelInjector injector = new SpigotChannelInjector();
            private final LogManager logManager = new BukkitLogManager();
            private final AtomicBoolean loaded = new AtomicBoolean(false);
            private final AtomicBoolean initialized = new AtomicBoolean(false);
            private boolean lateBind = false;

            @Override
            public void load() {
                if (!loaded.getAndSet(true)) {
                    //Resolve server version and cache
                    String id = plugin.getName().toLowerCase();
                    PacketEvents.IDENTIFIER = "pe-" + id;
                    PacketEvents.ENCODER_NAME = "pe-encoder-" + id;
                    PacketEvents.DECODER_NAME = "pe-decoder-" + id;
                    PacketEvents.CONNECTION_HANDLER_NAME = "pe-connection-handler-" + id;
                    PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-" + id;
                    PacketEvents.TIMEOUT_HANDLER_NAME = "pe-timeout-handler-" + id;
                    try {
                        SpigotReflectionUtil.init();
                        CustomPipelineUtil.init();
                    } catch (Exception ex) {
                        throw new IllegalStateException(ex);
                    }

                    //Server hasn't bound to the port yet.
                    lateBind = !injector.isServerBound();
                    //If late-bind is enabled, we will inject a bit later.
                    if (!lateBind) {
                        injector.inject();
                    }

                    //Register internal packet listener (should be the first listener)
                    //This listener doesn't do any modifications to the packets, just reads data
                    getEventManager().registerListener(new InternalPacketListener());
                }
            }

            @Override
            public boolean isLoaded() {
                return loaded.get();
            }

            @Override
            public void init() {
                //Load if we haven't loaded already
                load();
                if (!initialized.getAndSet(true)) {
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
                    if (!PacketType.isPrepared()) {
                        PacketType.prepare();
                    }
                    Bukkit.getPluginManager().registerEvents(new InternalBukkitListener(),
                            plugin);
                    //TODO Clean up and remove redundant post inject task
                    Runnable postInjectTask = () -> {
                        /*for (final Player p : Bukkit.getOnlinePlayers()) {
                            try {
                                Object channel = PacketEvents.getAPI().getPlayerManager().getChannel(p);
                                System.out.println("Pipe: " + ChannelHelper.pipelineHandlerNamesAsString(channel));
                                User user = PacketEvents.getAPI().getPlayerManager().getUser(p);
                                injector.updatePlayer(user, p);
                                getEventManager().callEvent(new UserLoginEvent(user, p));
                            } catch (Exception ex) {
                                p.kickPlayer("Failed to inject... Please rejoin!");
                                ex.printStackTrace();
                            }
                        }*/
                    };

                    if (lateBind) {
                        //If late-bind is enabled, we still need to inject (after all plugins enabled).
                        Runnable lateBindTask = () -> {
                            if (injector.isServerBound()) {
                                injector.inject();
                                postInjectTask.run();
                            }
                        };
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, lateBindTask);
                    } else {
                        postInjectTask.run();
                    }

                    checkCompatibility();
                }
            }

            private void checkCompatibility() {
                // PacketEvents is now enabled, we can now check
                ViaVersionUtil.checkIfViaIsPresent();
                ProtocolSupportUtil.checkIfProtocolSupportIsPresent();
                //If ProtocolSupport is present, it needs to be x or newer
                Plugin protocolSupportPlugin = Bukkit.getPluginManager().getPlugin("ProtocolSupport");
                if (protocolSupportPlugin != null) {
                    String psVersionString = protocolSupportPlugin.getDescription().getVersion().split("-")[0];
                    PEVersion psVersion;
                    try {
                        psVersion = new PEVersion(psVersionString);
                    }
                    catch (Exception ex) {
                        PacketEvents.getAPI().getLogManager().severe("You are attempting to combine 2.0 PacketEvents with a " +
                                "ProtocolSupport version older than v1.18.1-1. (Failed to parse the ProtocolSupport version)" +
                                "This is no longer works, please update to a newer build. " +
                                "https://ci.dmulloy2.net/job/ProtocolLib/lastBuild/");
                        Plugin ourPlugin = getPlugin();
                        Bukkit.getPluginManager().disablePlugin(ourPlugin);
                        throw new IllegalStateException("ProtocolSupport incompatibility! Update to v1.18.1-1 or newer!");
                    }
                    PEVersion minimumPSVersion = new PEVersion(1, 18, 1);
                    if (psVersion.isOlderThan(minimumPSVersion)) {
                        PacketEvents.getAPI().getLogManager().severe("You are attempting to combine 2.0 PacketEvents with a " +
                                "ProtocolSupport version older than v1.18.1-1. (" + psVersion.toString() + ") " +
                                "This is no longer works, please update to a newer build. " +
                                "https://ci.dmulloy2.net/job/ProtocolLib/lastBuild/");
                        Plugin ourPlugin = getPlugin();
                        Bukkit.getPluginManager().disablePlugin(ourPlugin);
                        throw new IllegalStateException("ProtocolSupport incompatibility! Update to v1.18.1-1 or newer!");
                    }
                }
                //If ProtocolLib is present, it needs to be v5.0.0 or newer
                Plugin protocolLibPlugin = Bukkit.getPluginManager().getPlugin("ProtocolLib");
                if (protocolLibPlugin != null) {
                    int majorVersion = Integer.parseInt(protocolLibPlugin.getDescription().getVersion().split("\\.", 2)[0]);
                    if (majorVersion < 5) {
                        PacketEvents.getAPI().getLogManager().severe("You are attempting to combine 2.0 PacketEvents with a " +
                                "ProtocolLib version older than v5.0.0. " +
                                "This is no longer works, please update to their dev builds. " +
                                "https://ci.dmulloy2.net/job/ProtocolLib/lastBuild/");
                        Plugin ourPlugin = getPlugin();
                        Bukkit.getPluginManager().disablePlugin(ourPlugin);
                        throw new IllegalStateException("ProtocolLib incompatibility! Update to v5.0.0 or newer!");
                    }
                }
            }

            @Override
            public boolean isInitialized() {
                return initialized.get();
            }

            @Override
            public void terminate() {
                if (initialized.getAndSet(false)) {
                    //Uninject the injector if needed(depends on the injector implementation)
                    injector.uninject();
                }
            }

            @Override
            public Plugin getPlugin() {
                return plugin;
            }

            @Override
            public ProtocolManager getProtocolManager() {
                return protocolManager;
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
