/**
MIT License

Copyright (c) 2020 retrooper

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.api.PacketEventsAPI;
import io.github.retrooper.packetevents.enums.ClientVersion;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.BukkitMoveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.NMSUtils;
import io.github.retrooper.packetevents.utils.nms_entityfinder.EntityFinderUtils;
import io.github.retrooper.packetevents.utils.versionlookup.VersionLookupUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public final class PacketEvents implements PacketListener, Listener {

    private static final PacketEventsAPI packetEventsAPI = new PacketEventsAPI();
    private static final PacketEvents instance = new PacketEvents();
    private static final PacketEventsSettings settings = new PacketEventsSettings();
    private static Plugin plugin;
    private static boolean hasLoaded, hasStarted;

    /**
     * Call this before start()
     *
     * Recommended on your onLoad() Bukkit Method
     */
    public static void load() {
        //SERVER VERSION LOADING

        //Server Version loading
        ServerVersion.load();

        ServerVersion version = ServerVersion.getVersion();
        WrappedPacket.version = version;
        PacketEvent.version = version;
        NMSUtils.version = version;
        EntityFinderUtils.version = version;

        NMSUtils.load();

        PacketTypeClasses.Client.load();
        PacketTypeClasses.Server.load();

        EntityFinderUtils.load();



        VersionLookupUtils.load();
        ClientVersion.prepareLookUp();

        WrappedPacket.loadAllWrappers();
        hasLoaded = true;
    }

    /**
     * Loads PacketEvents if you haven't already, Sets everything up, injects all players
     * @param pl JavaPlugin instance
     */
    public static void init(final Plugin pl) {
        if (!hasLoaded) {
            load();
        }
        if (!hasStarted) {
            plugin = pl;
            //Register Bukkit and PacketListener
            PacketEvents.getAPI().getEventManager().registerListener(instance);

            Bukkit.getPluginManager().registerEvents(instance, plugin);

            for (final Player p : Bukkit.getOnlinePlayers()) {
                getAPI().getPlayerUtils().injectPlayer(p);
            }
            hasStarted = true;
        }
    }

    /**
     * Loads PacketEvents if you haven't already, Sets everything up, injects all players
     * @param pl Plugin instance
     * @deprecated Use {@link #init(Plugin)}
     */
    @Deprecated
    public static void start(final Plugin pl) {
        init(pl);
    }

    /**
     * Stop all tasks and unregisters all PacketEvents' listeners
     */
    public static void stop() {
        if (hasStarted) {
            for (final Player p : Bukkit.getOnlinePlayers()) {
                getAPI().getPlayerUtils().uninjectPlayer(p);
            }
            getAPI().getEventManager().unregisterAllListeners();

            PacketType.Client.packetIds.clear();
            PacketType.Server.packetIds.clear();
        }
    }

    public static boolean hasLoaded() {
        return hasLoaded;
    }

    public static boolean hasStarted() {
        return hasStarted;
    }

    public static PacketEventsAPI getAPI() {
        return packetEventsAPI;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static String getHandlerName(final String name) {
        return "pe-" + settings.getIdentifier() + "-" + name;
    }

    public static PacketEventsSettings getSettings() {
        return settings;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onJoin(final PlayerJoinEvent e) {
        getAPI().getPlayerUtils().injectPlayer(e.getPlayer());
        getAPI().getPlayerUtils().setClientVersion(e.getPlayer(), ClientVersion.fromProtocolVersion((short) VersionLookupUtils.getProtocolVersion(e.getPlayer())));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onQuit(final PlayerQuitEvent e) {
        getAPI().getPlayerUtils().clearClientVersion(e.getPlayer());
        getAPI().getPlayerUtils().uninjectPlayer(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMove(final PlayerMoveEvent e) {
        BukkitMoveEvent moveEvent = new BukkitMoveEvent(e);
        getAPI().getEventManager().callEvent(moveEvent);
        e.setCancelled(moveEvent.isCancelled());
    }

}