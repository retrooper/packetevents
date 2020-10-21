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
import io.github.retrooper.packetevents.nettyhandler.NettyPacketHandler;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.updatechecker.UpdateChecker;
import io.github.retrooper.packetevents.utils.entityfinder.EntityFinderUtils;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.versionlookup.VersionLookupUtils;
import io.github.retrooper.packetevents.utils.server.PEVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Future;

public final class PacketEvents implements Listener {
    private static final String nettyHandlerIdentifier = generateRandomNettyIdentifier();

    private static final PacketEventsAPI packetEventsAPI = new PacketEventsAPI();
    private static final PacketEvents instance = new PacketEvents();
    private static final ArrayList<Plugin> plugins = new ArrayList<Plugin>(1);
    private static boolean loaded, initialized;
    private static final PEVersion version = new PEVersion(1, 7);

    private static PacketEventsSettings settings = new PacketEventsSettings();

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

            NMSUtils.load();

            PacketTypeClasses.Client.load();
            PacketTypeClasses.Server.load();

            EntityFinderUtils.load();

            WrappedPacket.loadAllWrappers();
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
            plugins.add(pl);
            //Register Bukkit listener
            Bukkit.getPluginManager().registerEvents(instance, plugins.get(0));

            for (final Player p : Bukkit.getOnlinePlayers()) {
                getAPI().getPlayerUtils().injectPlayer(p);
            }

            if (settings.shouldCheckForUpdates()) {
                Future<?> future = NettyPacketHandler.executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        new UpdateChecker().handleUpdate();
                    }
                });
            }

            if(getAPI().getServerUtils().isBungeeCordEnabled()) {
                Bukkit.getServer().getMessenger().registerIncomingPluginChannel(pl, BungeePluginMessageListener.tagName, new BungeePluginMessageListener());
            }
            initialized = true;
        }
    }

    /**
     *
     */
    public static void stop() {
        if (initialized) {
            for (final Player p : Bukkit.getOnlinePlayers()) {
                getAPI().getPlayerUtils().ejectPlayer(p);
            }

            getAPI().getEventManager().unregisterAllListeners();

            initialized = false;
            NettyPacketHandler.executorService.shutdownNow();
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

    public static String getHandlerName(final String name) {
        return "pe-" + nettyHandlerIdentifier + "-" + name;
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
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        if (!VersionLookupUtils.hasHandledLoadedDependencies()) {
            VersionLookupUtils.handleLoadedDependencies();
        }
        //for now we don't support bungee
        if (PacketEvents.getAPI().getServerUtils().isBungeeCordEnabled()) {
            PacketEvents.getAPI().getPlayerUtils().clientVersionsMap.put(e.getPlayer().getUniqueId(), ClientVersion.UNKNOWN_BUNGEE_SERVER);
        } else {
            ClientVersion version = ClientVersion.getClientVersion(VersionLookupUtils.getProtocolVersion(e.getPlayer()));
            PacketEvents.getAPI().getPlayerUtils().clientVersionsMap.put(e.getPlayer().getUniqueId(), version);
        }
        PacketEvents.getAPI().getPlayerUtils().injectPlayer(e.getPlayer());
    }


    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        PacketEvents.getAPI().getPlayerUtils().ejectPlayer(e.getPlayer());
        PacketEvents.getAPI().getPlayerUtils().clientVersionsMap.remove(e.getPlayer().getUniqueId());
    }

    private static String generateRandomNettyIdentifier() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        while (sb.length() < 13) {
            int index = (int) (random.nextFloat() * alphabet.length());
            sb.append(alphabet.charAt(index));
        }
        return sb.toString();
    }

}
