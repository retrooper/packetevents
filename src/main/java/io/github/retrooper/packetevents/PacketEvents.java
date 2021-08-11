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
import io.github.retrooper.packetevents.event.impl.PostPlayerInjectEvent;
import io.github.retrooper.packetevents.event.manager.EventManager;
import io.github.retrooper.packetevents.event.manager.PEEventManager;
import io.github.retrooper.packetevents.exceptions.PacketEventsLoadFailureException;
import io.github.retrooper.packetevents.injector.GlobalChannelInjector;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.processor.BukkitEventProcessorInternal;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.updatechecker.UpdateChecker;
import io.github.retrooper.packetevents.utils.entityfinder.EntityFinderUtils;
import io.github.retrooper.packetevents.utils.guava.GuavaUtils;
import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufUtil;
import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufUtil_7;
import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufUtil_8;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.PlayerUtils;
import io.github.retrooper.packetevents.utils.reflection.ReflectionObject;
import io.github.retrooper.packetevents.utils.server.ServerUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.version.PEVersion;
import io.github.retrooper.packetevents.utils.versionlookup.viaversion.ViaVersionLookupUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class PacketEvents implements Listener, EventManager {
    private static PacketEvents instance;
    private static Plugin plugin;
    private final PEVersion version = new PEVersion(2, 0, 0);
    private final EventManager eventManager = new PEEventManager();
    private final PlayerUtils playerUtils = new PlayerUtils();
    private final ServerUtils serverUtils = new ServerUtils();
    private final BukkitEventProcessorInternal bukkitEventProcessorInternal = new BukkitEventProcessorInternal();
    private final GlobalChannelInjector injector = new GlobalChannelInjector();
    public String handlerName;
    private PacketEventsSettings settings = new PacketEventsSettings();
    private ByteBufUtil byteBufUtil;
    private UpdateChecker updateChecker;
    private volatile boolean loading, loaded;
    private boolean initialized, initializing, terminating;
    private boolean lateBind = false;

    public static PacketEvents create(final Plugin plugin) {
        if (Bukkit.isPrimaryThread()) {
            //We are on the main thread
            if (!Bukkit.getServicesManager().isProvidedFor(PacketEvents.class)) {
                //We can register in the service manager.
                instance = new PacketEvents();
                Bukkit.getServicesManager().register(PacketEvents.class, instance,
                        plugin, ServicePriority.Normal);
                PacketEvents.plugin = plugin;
                return instance;
            } else {
                //We have already registered. Let us load what was registered.
                return instance = Bukkit.getServicesManager().load(PacketEvents.class);
            }
        } else {
            //We are off thread, we cannot use the service manager.
            if (instance == null) {
                PacketEvents.plugin = plugin;
                instance = new PacketEvents();
            }
            return instance;
        }
    }

    public static PacketEvents get() {
        return instance;
    }

    public void load() {
        if (!loaded && !loading) {
            loading = true;
            //Resolve server version and cache
            ServerVersion version = ServerVersion.getVersion();
            ReflectionObject.version = version;
            NMSUtils.version = version;
            EntityFinderUtils.version = version;
            handlerName = "pe-" + plugin.getName();
            try {
                NMSUtils.load();

                EntityFinderUtils.load();

                getServerUtils().entityCache = GuavaUtils.makeMap();
            } catch (Exception ex) {
                loading = false;
                throw new PacketEventsLoadFailureException(ex);
            }

            byteBufUtil = NMSUtils.legacyNettyImportMode ? new ByteBufUtil_7() : new ByteBufUtil_8();
            updateChecker = new UpdateChecker();

            injector.load();
            lateBind = !injector.isBound() || ViaVersionLookupUtils.isAvailable();
            //If late-bind is enabled or ViaVersion is present, we will inject a bit later.
            if (!lateBind) {
                injector.inject();
            }

            loaded = true;
            loading = false;
        }
    }

    public void init() {
        init(getSettings());
    }

    public void init(PacketEventsSettings packetEventsSettings) {
        //Load if we haven't loaded already
        load();
        if (!initialized && !initializing) {
            initializing = true;
            settings = packetEventsSettings;
            settings.lock();

            if (settings.shouldCheckForUpdates()) {
                handleUpdateCheck();
            }

            if (settings.isbStatsEnabled()) {
                Metrics metrics = new Metrics((JavaPlugin) getPlugin(), 11327);
            }

            //TODO Load other ones
            PacketType.Play.Client.load();

            Runnable postInjectTask = () -> {
                Bukkit.getPluginManager().registerEvents(bukkitEventProcessorInternal, plugin);
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    try {
                        injector.injectPlayer(p);
                        getEventManager().callEvent(new PostPlayerInjectEvent(p, false));
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
            initializing = false;
        }
    }

    public void terminate() {
        if (initialized && !terminating) {
            //Eject all players
            for (Player p : Bukkit.getOnlinePlayers()) {
                injector.ejectPlayer(p);
            }
            //Eject the injector if needed
            injector.eject();
            //Unregister all our listeners
            getEventManager().unregisterAllListeners();
            initialized = false;
            terminating = false;
        }
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean hasLoaded() {
        return loaded;
    }

    public boolean isTerminating() {
        return terminating;
    }

    public boolean isInitializing() {
        return initializing;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public Plugin getPlugin() {
        return plugin;
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

    public PlayerUtils getPlayerUtils() {
        return playerUtils;
    }

    public ServerUtils getServerUtils() {
        return serverUtils;
    }

    public ByteBufUtil getByteBufUtil() {
        return byteBufUtil;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    private void handleUpdateCheck() {
        if (updateChecker == null) {
            updateChecker = new UpdateChecker();
        }
        Thread thread = new Thread(() -> {
            getPlugin().getLogger().info("[packetevents] Checking for an update, please wait...");
            UpdateChecker.UpdateCheckerStatus status = updateChecker.checkForUpdate();
            int seconds = 5;
            int retryCount = 5;
            for (int i = 0; i < retryCount; i++) {
                if (status != UpdateChecker.UpdateCheckerStatus.FAILED) {
                    break;
                }
                getPlugin().getLogger().severe("[packetevents] Checking for an update again in " + seconds + " seconds...");
                try {
                    Thread.sleep(seconds * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                seconds *= 2;

                status = updateChecker.checkForUpdate();

                if (i == (retryCount - 1)) {
                    getPlugin().getLogger().severe("[packetevents] PacketEvents failed to check for an update. No longer retrying.");
                    break;
                }

            }

        }, "packetevents-update-check-thread");
        thread.start();
    }

}
