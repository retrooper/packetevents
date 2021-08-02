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
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityequipment.WrappedPacketOutEntityEquipment;
import io.github.retrooper.packetevents.processor.BukkitEventProcessorInternal;
import io.github.retrooper.packetevents.processor.PacketProcessorInternal;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.updatechecker.UpdateChecker;
import io.github.retrooper.packetevents.utils.entityfinder.EntityFinderUtils;
import io.github.retrooper.packetevents.utils.guava.GuavaUtils;
import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufUtil;
import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufUtil_7;
import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufUtil_8;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.PlayerUtils;
import io.github.retrooper.packetevents.utils.server.ServerUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.version.PEVersion;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public final class PacketEvents implements Listener, EventManager {
    private static PacketEvents instance;
    private static Plugin plugin;
    private final PEVersion version = new PEVersion(1, 7, 9, 17);
    private final EventManager eventManager = new PEEventManager();
    private final PlayerUtils playerUtils = new PlayerUtils();
    private final ServerUtils serverUtils = new ServerUtils();
    private final PacketProcessorInternal packetProcessorInternal = new PacketProcessorInternal();
    private final BukkitEventProcessorInternal bukkitEventProcessorInternal = new BukkitEventProcessorInternal();
    private final GlobalChannelInjector injector = new GlobalChannelInjector();
    private final AtomicBoolean injectorReady = new AtomicBoolean();
    private String handlerName;
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

    @Deprecated
    public static PacketEvents getAPI() {
        return instance;
    }


    public void load() {
        if (!loaded && !loading) {
            loading = true;
            //Resolve server version and cache
            ServerVersion version = ServerVersion.getVersion();
            WrappedPacket.version = version;
            NMSUtils.version = version;
            EntityFinderUtils.version = version;
            handlerName = "pe-" + plugin.getName();
            try {
                NMSUtils.load();

                PacketTypeClasses.load();

                PacketType.load();

                EntityFinderUtils.load();

                getServerUtils().entityCache = GuavaUtils.makeMap();

                if (version.isNewerThanOrEquals(ServerVersion.v_1_9)) {
                    for (WrappedPacketOutEntityEquipment.EquipmentSlot slot : WrappedPacketOutEntityEquipment.EquipmentSlot.values()) {
                        slot.id = (byte) slot.ordinal();
                    }
                } else {
                    WrappedPacketOutEntityEquipment.EquipmentSlot.MAINHAND.id = 0;
                    WrappedPacketOutEntityEquipment.EquipmentSlot.OFFHAND.id = -1; //Invalid
                    WrappedPacketOutEntityEquipment.EquipmentSlot.BOOTS.id = 1;
                    WrappedPacketOutEntityEquipment.EquipmentSlot.LEGGINGS.id = 2;
                    WrappedPacketOutEntityEquipment.EquipmentSlot.CHESTPLATE.id = 3;
                    WrappedPacketOutEntityEquipment.EquipmentSlot.HELMET.id = 4;
                }
            } catch (Exception ex) {
                loading = false;
                throw new PacketEventsLoadFailureException(ex);
            }

            byteBufUtil = NMSUtils.legacyNettyImportMode ? new ByteBufUtil_7() : new ByteBufUtil_8();
            updateChecker = new UpdateChecker();
            if (!injectorReady.get()) {
                injector.load();
                lateBind = !injector.isBound();
                //If late-bind is enabled, we will inject a bit later.
                if (!lateBind) {
                    injector.inject();
                }
                injectorReady.set(true);
            }

            loaded = true;
            loading = false;
        }
    }

    public void loadAsyncNewThread() {
        new Thread(this::load).start();
    }

    public void loadAsync(ExecutorService executorService) {
        executorService.execute(this::load);
    }

    public void loadSettings(PacketEventsSettings settings) {
        this.settings = settings;
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

            //We must wait for the injector to initialize.

            //Wait for the injector to be ready.
            while (!injectorReady.get()) {
            }

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

    @Deprecated
    public void init(Plugin plugin) {
        init(plugin, settings);
    }

    @Deprecated
    public void init(Plugin pl, PacketEventsSettings packetEventsSettings) {
        init(packetEventsSettings);
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

    /**
     * Use {@link #terminate()}. This is deprecated
     *
     * @deprecated "Stop" might be misleading and "terminate" sounds better I guess...
     */
    @Deprecated
    public void stop() {
        terminate();
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

    @Deprecated
    public boolean isStopping() {
        return isTerminating();
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

    public PacketProcessorInternal getInternalPacketProcessor() {
        return packetProcessorInternal;
    }

    public String getHandlerName() {
        return handlerName;
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
