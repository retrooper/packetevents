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

import io.github.retrooper.packetevents.bungee.BungeePluginMessageListener;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.packetmanager.PacketManager;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.updatechecker.UpdateChecker;
import io.github.retrooper.packetevents.utils.entityfinder.EntityFinderUtils;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import io.github.retrooper.packetevents.utils.server.PEVersion;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.v_1_7_10.ProtocolVersionAccessor_v_1_7;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class PacketEvents implements Listener {
    private static final PacketEventsAPI packetEventsAPI = new PacketEventsAPI();
    private static final PacketEvents instance = new PacketEvents();
    private static final ArrayList<Plugin> plugins = new ArrayList<>(1);
    private static boolean loaded, initialized;
    private static final PEVersion version = new PEVersion(1, 7, 5);

    private static PacketEventsSettings settings = new PacketEventsSettings();
    /** General executor service, basically for anything that the packet executor service doesn't do.
     */
    public static ExecutorService generalExecutorService = Executors.newCachedThreadPool();
    //Executor used for player injecting/ejecting and for packet processing/event calling
    public static ExecutorService packetHandlingExecutorService = Executors.newSingleThreadExecutor();

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
    public static void load() {
        if (!loaded) {
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
                throw new IllegalStateException("PacketEvents failed to successfully load...", ex);
            }
            loaded = true;
        }

    }

    public static void loadSettings(PacketEventsSettings settings) {
        PacketEvents.settings = settings;
    }

    public static void init(final Plugin plugin) {
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
    public static void init(final Plugin pl, PacketEventsSettings packetEventsSettings) {
        load();
        if (!initialized) {
            settings = packetEventsSettings;
            int packetHandlingThreadCount = settings.getPacketHandlingThreadCount();
            //if the count is 1 or is invalid
            if(packetHandlingThreadCount == 1 || packetHandlingThreadCount < 0) {
                packetHandlingExecutorService = Executors.newSingleThreadExecutor();
            }
            else {
                packetHandlingExecutorService = Executors.newFixedThreadPool(packetHandlingThreadCount);
            }
            plugins.add(pl);

            //Register Bukkit listener
            Bukkit.getPluginManager().registerEvents(instance, plugins.get(0));
            PacketEvents.getAPI().packetManager = new PacketManager(plugins.get(0), settings.shouldInjectEarly());

            for (final Player p : Bukkit.getOnlinePlayers()) {
                getAPI().getPlayerUtils().injectPlayer(p);
            }

            if (settings.shouldCheckForUpdates()) {
               PacketEvents.generalExecutorService.execute(() -> new UpdateChecker().handleUpdate());
            }

            if (getAPI().getServerUtils().isBungeeCordEnabled()) {
                Bukkit.getMessenger().registerOutgoingPluginChannel(plugins.get(0), "BungeeCord");
                Bukkit.getServer().getMessenger().registerIncomingPluginChannel(plugins.get(0), BungeePluginMessageListener.tagName, new BungeePluginMessageListener());
            }

            initialized = true;
        }
    }

    /**
     *
     */
    public static void stop() {
        if (initialized) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PacketEvents.getAPI().packetManager.ejectPlayer(player);
            }

            if (PacketEvents.getAPI().packetManager.tinyProtocol != null) {
                PacketEvents.getAPI().packetManager.tinyProtocol.unregisterChannelHandler();
            }
            getAPI().getEventManager().unregisterAllListeners();
            PacketEvents.generalExecutorService.shutdownNow();
            PacketEvents.packetHandlingExecutorService.shutdownNow();
            initialized = false;
        }
    }

    public static boolean hasLoaded() {
        return loaded;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static PacketEventsAPI getAPI() {
        return packetEventsAPI;
    }

    public static ArrayList<Plugin> getPlugins() {
        return plugins;
    }

    /**
     * Get the PacketEvents settings.
     *
     * @return Configure some settings for the API
     */
    public static PacketEventsSettings getSettings() {
        return settings;
    }

    public static PEVersion getVersion() {
        return version;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLogin(PlayerLoginEvent e) {
        if (PacketEvents.getSettings().shouldInjectEarly()) {
            assert getAPI().packetManager.tinyProtocol != null;
            try {
                if (getAPI().packetManager.canInject(e.getPlayer())) {
                    getAPI().packetManager.injectPlayer(e.getPlayer());
                }
            } catch (Exception ex) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "We are unable to inject you. Please try again!");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(final PlayerJoinEvent e) {
        if(PacketEvents.getAPI().getServerUtils().getVersion() == ServerVersion.v_1_7_10) {
            Object channel = NMSUtils.getChannel(e.getPlayer());
            ClientVersion version = ClientVersion.getClientVersion(ProtocolVersionAccessor_v_1_7.getProtocolVersion(e.getPlayer()));
            PacketEvents.getAPI().getPlayerUtils().clientVersionsMap.put(channel, version);
        }
        //Waiting for the BungeeCord server to send their plugin message with your version,
        if (!PacketEvents.getSettings().shouldInjectEarly()) {
            if (getAPI().packetManager.canInject(e.getPlayer())) {
                PacketEvents.getAPI().packetManager.injectPlayer(e.getPlayer());
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(final PlayerQuitEvent e) {
        PacketEvents.getAPI().getPlayerUtils().clientVersionsMap.remove(e.getPlayer().getUniqueId());
    }
}
