package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.api.PacketEventsAPI;
import io.github.retrooper.packetevents.enums.ClientVersion;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.BukkitMoveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.versionlookup.VersionLookupUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
     */
    public static void load() {
        //SERVER VERSION LOADING

        //Server Version reversed values caching
        ServerVersion.reversedValues = ServerVersion.reverse(ServerVersion.values());

        PacketTypeClasses.Client.load();
        PacketTypeClasses.Server.load();
        ClientVersion.prepareLookUp();
        hasLoaded = true;
    }

    /**
     * Loads PacketEvents if you haven't already, Sets everything up, injects all players
     */
    public static void start(final Plugin pl) {
        if (!hasLoaded) {
            load();
        }
        if (!hasStarted) {
            plugin = pl;
            //Register Bukkit and PacketListener
            getAPI().getEventManager().registerListener(instance);

            Bukkit.getPluginManager().registerEvents(instance, plugin);

            for (final Player p : Bukkit.getOnlinePlayers()) {
                getAPI().getPlayerUtils().injectPlayer(p);
            }
            hasStarted = true;
        }
    }

    /**
     * Stop all tasks and unregisters all PacketEvents' listeners
     */
    public static void stop() {
        if (hasStarted) {
            for (final Player p : Bukkit.getOnlinePlayers()) {

                getAPI().getPlayerUtils().uninjectPlayerNow(p);
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
        return "pe-" + getSettings().getIdentifier() + "-" + name;
    }

    public static PacketEventsSettings getSettings() {
        return settings;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        getAPI().getPlayerUtils().injectPlayer(e.getPlayer());
        getAPI().getPlayerUtils().setClientVersion(e.getPlayer(), ClientVersion.fromProtocolVersion(VersionLookupUtils.getProtocolVersion(e.getPlayer())));
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        getAPI().getPlayerUtils().clearClientVersion(e.getPlayer());
        getAPI().getPlayerUtils().uninjectPlayerNow(e.getPlayer());
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        BukkitMoveEvent moveEvent = new BukkitMoveEvent(e);
        getAPI().getEventManager().callEvent(moveEvent);
        e.setCancelled(moveEvent.isCancelled());
    }
}
