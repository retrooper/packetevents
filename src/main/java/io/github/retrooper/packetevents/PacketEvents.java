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

package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.bstats.Metrics;
import io.github.retrooper.packetevents.event.EventManager;
import io.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.event.impl.PostPlayerInjectEvent;
import io.github.retrooper.packetevents.exceptions.PacketEventsLoadFailureException;
import io.github.retrooper.packetevents.handlers.GlobalChannelInjector;
import io.github.retrooper.packetevents.manager.player.PlayerManager;
import io.github.retrooper.packetevents.manager.server.ServerManager;
import io.github.retrooper.packetevents.processor.InternalBukkitListener;
import io.github.retrooper.packetevents.processor.InternalPacketListener;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.updatechecker.UpdateChecker;
import io.github.retrooper.packetevents.utils.guava.GuavaUtils;
import io.github.retrooper.packetevents.utils.nms.MinecraftReflection;
import io.github.retrooper.packetevents.utils.PEVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class PacketEvents implements Listener {
    private static final PacketEvents instance = new PacketEvents();
    private final PEVersion version = new PEVersion(2, 0, 0);
    private final EventManager eventManager = new EventManager();
    private final PlayerManager playerManager = new PlayerManager();
    private final ServerManager serverManager = new ServerManager();
    private final InternalBukkitListener internalBukkitListener = new InternalBukkitListener();
    private final GlobalChannelInjector injector = new GlobalChannelInjector();
    public String identifier, encoderName, decoderName, connectionName, serverChannelHandlerName;
    private PacketEventsSettings settings = new PacketEventsSettings();
    private UpdateChecker updateChecker;
    private boolean loaded;
    private boolean initialized;
    private boolean lateBind = false;
    private Plugin plugin;


    public static PacketEvents get() {
        return instance;
    }

    public void load(Plugin plugin) {
        this.plugin = plugin;
        if (!loaded) {
            //Resolve server version and cache
            identifier = "pe-" + plugin.getName().toLowerCase();
            encoderName = "pe-encoder-" + plugin.getName().toLowerCase();
            decoderName = "pe-decoder-" + plugin.getName().toLowerCase();
            connectionName = "pe-connection-handler-" + plugin.getName().toLowerCase();
            serverChannelHandlerName = "pe-connection-initializer-" + plugin.getName().toLowerCase();
            try {
                MinecraftReflection.init();

                serverManager.entityCache = GuavaUtils.makeMap();
            } catch (Exception ex) {
                throw new PacketEventsLoadFailureException(ex);
            }

            updateChecker = new UpdateChecker();

            injector.load();
            lateBind = !injector.isBound();
            //If late-bind is enabled or ViaVersion is present, we will inject a bit later.
            if (!lateBind) {
                injector.inject();
            }

            loaded = true;

            //Register internal packet listener
            getEventManager().registerListener(new InternalPacketListener(), PacketListenerPriority.LOWEST);
        }
    }

    public void init() {
        init(getSettings());
    }

    public void init(PacketEventsSettings packetEventsSettings) {
        //Load if we haven't loaded already
        load(plugin);
        if (!initialized) {
            settings = packetEventsSettings;

            if (settings.shouldCheckForUpdates()) {
                handleUpdateCheck(plugin);
            }

            if (settings.isbStatsEnabled()) {
                Metrics metrics = new Metrics((JavaPlugin) plugin, 11327);
            }

            PacketType.Game.Client.load();
            PacketType.Game.Server.load();

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

    public boolean hasLoaded() {
        return loaded;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public GlobalChannelInjector getInjector() {
        return injector;
    }

    public PacketEventsSettings getSettings() {
        return settings;
    }

    public PEVersion getVersion() {
        return version;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    private void handleUpdateCheck(Plugin plugin) {
        if (updateChecker == null) {
            updateChecker = new UpdateChecker();
        }
        Thread thread = new Thread(() -> {
            plugin.getLogger().info("[packetevents] Checking for an update, please wait...");
            UpdateChecker.UpdateCheckerStatus status = updateChecker.checkForUpdate();
            int waitTimeInSeconds = 5;
            int maxRetryCount = 5;
            int retries = 0;
            while (retries < maxRetryCount) {
                if (status != UpdateChecker.UpdateCheckerStatus.FAILED) {
                    break;
                }
                plugin.getLogger().severe("[packetevents] Checking for an update again in " + waitTimeInSeconds + " seconds...");
                try {
                    Thread.sleep(waitTimeInSeconds * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                waitTimeInSeconds *= 2;

                status = updateChecker.checkForUpdate();

                if (retries == (maxRetryCount - 1)) {
                    plugin.getLogger().severe("[packetevents] PacketEvents failed to check for an update. No longer retrying.");
                    break;
                }

                retries++;
            }

        }, "packetevents-update-check-thread");
        thread.start();
    }

}
