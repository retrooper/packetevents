/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.impl.PostPlayerInjectEvent;
import io.github.retrooper.packetevents.event.manager.EventManager;
import io.github.retrooper.packetevents.event.manager.PEEventManager;
import io.github.retrooper.packetevents.exceptions.PacketEventsLoadFailureException;
import io.github.retrooper.packetevents.packetmanager.PacketManager;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.updatechecker.UpdateChecker;
import io.github.retrooper.packetevents.utils.entityfinder.EntityFinderUtils;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import io.github.retrooper.packetevents.utils.player.PlayerUtils;
import io.github.retrooper.packetevents.utils.server.ServerUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.version.PEVersion;
import io.github.retrooper.packetevents.utils.versionlookup.VersionLookupUtils;
import io.github.retrooper.packetevents.utils.versionlookup.v_1_7_10.ProtocolVersionAccessor_v_1_7;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class PacketEvents implements Listener, EventManager {
    private static PacketEvents instance;
    private final ArrayList<Plugin> plugins = new ArrayList<>(1);
    private final PEVersion version = new PEVersion(1, 7, 9);
    private final EventManager eventManager = new PEEventManager();
    private final PlayerUtils playerUtils = new PlayerUtils();
    private final ServerUtils serverUtils = new ServerUtils();
    /**
     * General executor service, basically for anything that the packet executor service doesn't do.
     */
    public ExecutorService generalExecutorService = Executors.newSingleThreadExecutor();
    //Executor used for player injecting/ejecting and for packet processing/event calling
    public ExecutorService packetHandlingExecutorService = Executors.newSingleThreadExecutor();
    public PacketManager packetManager = null;
    private boolean loading, loaded, initialized, initializing, stopping;
    private PacketEventsSettings settings = new PacketEventsSettings();

    public static PacketEvents create() {
        return PacketEvents.instance = new PacketEvents();
    }

    public static PacketEvents get() {
        return instance;
    }

    /**
     * This loads the PacketEvents API.
     * <p>
     * ServerVersion:
     * In this method we detect and cache the server version.
     * <p>
     * NMSUtils:
     * We setup some NMS utilities.
     * <p>
     * Packet ID System:
     * All the packet classes we will be needing are cached in a Map with an integer ID.
     * <p>
     * Version Lookup Utils:
     * We setup the client protocol version system.
     * We check if ViaVersion, ProtocolSupport or ProtocolLib is present.
     * <p>
     * Wrappers:
     * All PacketEvents' wrappers are setup and do all loading they need to do.
     */
    public void load() {
        if (!loaded && !loading) {
            loading = true;
            ServerVersion version = ServerVersion.getVersion();
            WrappedPacket.version = version;
            PacketEvent.version = version;
            NMSUtils.version = version;
            EntityFinderUtils.version = version;

            try {
                NMSUtils.load();

                PacketTypeClasses.Client.load();
                PacketTypeClasses.Server.load();
                PacketTypeClasses.Login.load();
                PacketTypeClasses.Status.load();

                EntityFinderUtils.load();

                WrappedPacket.loadAllWrappers();
            } catch (Exception ex) {
                loading = false;
                throw new PacketEventsLoadFailureException(ex);
            }
            loaded = true;
            loading = false;
        }

    }

    public void loadSettings(PacketEventsSettings settings) {
        this.settings = settings;
    }

    public void init(final Plugin plugin) {
        init(plugin, settings);
    }

    /**
     * Initiates PacketEvents
     * <p>
     * Loading:
     * Loads PacketEvents if you haven't already.
     * <p>
     * Registering:
     * Registers this class as a Bukkit listener to inject/eject players.
     *
     * @param pl JavaPlugin instance
     */
    public void init(final Plugin pl, PacketEventsSettings packetEventsSettings) {
        load();
        if (!initialized && !initializing) {
            initializing = true;
            settings = packetEventsSettings;
            int packetHandlingThreadCount = settings.getPacketHandlingThreadCount();
            //if the count is 1 or is invalid
            if (packetHandlingThreadCount == 1 || packetHandlingThreadCount < 0) {
                packetHandlingExecutorService = Executors.newSingleThreadExecutor();
            } else {
                packetHandlingExecutorService = Executors.newFixedThreadPool(packetHandlingThreadCount);
            }
            plugins.add(pl);

            //Register Bukkit listener
            Bukkit.getPluginManager().registerEvents(this, plugins.get(0));
            packetManager = new PacketManager(plugins.get(0), settings.shouldInjectEarly());

            for (final Player p : Bukkit.getOnlinePlayers()) {
                try {
                    getPlayerUtils().injectPlayer(p);
                } catch (Exception ex) {
                    p.kickPlayer(getSettings().getInjectionFailureMessage());
                }
            }

            if (settings.shouldCheckForUpdates()) {
                generalExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        new UpdateChecker().handleUpdate();
                    }
                });

            }
            initialized = true;
            initializing = false;
        }
    }

    /**
     *
     */
    public void stop() {
        if (initialized && !stopping) {
            stopping = true;
            for (Player player : Bukkit.getOnlinePlayers()) {
                packetManager.ejectPlayer(player);
            }

            if (packetManager.tinyProtocol != null) {
                packetManager.tinyProtocol.unregisterChannelHandler();
            }

            getEventManager().unregisterAllListeners();
            generalExecutorService.shutdownNow();
            packetHandlingExecutorService.shutdownNow();
            initialized = false;
            stopping = true;
        }
    }

    public boolean hasLoaded() {
        return loaded;
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean isInitializing() {
        return initializing;
    }

    public boolean isStopping() {
        return stopping;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public ArrayList<Plugin> getPlugins() {
        return plugins;
    }

    /**
     * Get the PacketEvents settings.
     *
     * @return Configure some settings for the API
     */
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


    @EventHandler(priority = EventPriority.LOW)
    public void onLogin(PlayerLoginEvent e) {
        if (getSettings().shouldInjectEarly()) {
            try {
                packetManager.injectPlayer(e.getPlayer());
            } catch (Exception ex) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, getSettings().getInjectionFailureMessage());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(final PlayerJoinEvent e) {
        Object channel = NMSUtils.getChannel(e.getPlayer());
        if (getServerUtils().getVersion() == ServerVersion.v_1_7_10) {
            ClientVersion version = ClientVersion.getClientVersion(ProtocolVersionAccessor_v_1_7.getProtocolVersion(e.getPlayer()));
            getPlayerUtils().clientVersionsMap.put(channel, version);
        } else if (VersionLookupUtils.isDependencyAvailable()) {
            int protocolVersion = VersionLookupUtils.getProtocolVersion(e.getPlayer());
            ClientVersion version = ClientVersion.getClientVersion(protocolVersion);
            getPlayerUtils().clientVersionsMap.put(channel, version);
        }

        if (!getSettings().shouldInjectEarly()) {
            try {
                packetManager.injectPlayer(e.getPlayer());
                //The injection has succeeded if we reach to this point.
                getEventManager().callEvent(new PostPlayerInjectEvent(e.getPlayer()));
            } catch (Exception ex) {
                e.getPlayer().kickPlayer(getSettings().getInjectionFailureMessage());
            }
        } else {
            //We have already injected them.
            getEventManager().callEvent(new PostPlayerInjectEvent(e.getPlayer()));
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(final PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        Object channel = NMSUtils.getChannelNoCache(e.getPlayer());
        getPlayerUtils().clientVersionsMap.remove(channel);
        getPlayerUtils().playerPingMap.remove(uuid);
        getPlayerUtils().playerSmoothedPingMap.remove(uuid);
    }
}