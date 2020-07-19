package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.api.PacketEventsAPI;
import io.github.retrooper.packetevents.enums.ClientVersion;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.BukkitMoveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.settings.Settings;
import io.github.retrooper.packetevents.utils.onlineplayers.OnlinePlayerUtilities;
import io.github.retrooper.packetevents.utils.versionlookup.VersionLookupUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public final class PacketEvents implements PacketListener, Listener {
    private static Plugin plugin;
    private static final PacketEventsAPI packetEventsAPI = new PacketEventsAPI();
    private static final PacketEvents instance = new PacketEvents();
    private static boolean hasLoaded;
    private static final Settings settings = new Settings();
    public static final List<Player> pendingUninjections = new ArrayList<Player>();

    /**
     * Call this before start()
     */
    public static void load() {
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
        plugin = pl;
        //Register Bukkit and PacketListener
        getAPI().getEventManager().registerListener(instance);

        Bukkit.getPluginManager().registerEvents(instance, plugin);

        for (final Player p : OnlinePlayerUtilities.getOnlinePlayers()) {
            getAPI().getPlayerUtils().injectPlayer(p);
        }
    }

    /**
     * Stop all tasks and unregisters all PacketEvents' listeners
     */
    public static void stop() {
        for (final Player p : OnlinePlayerUtilities.getOnlinePlayers()) {
            if(!pendingUninjections.contains(p)) {
            getAPI().getPlayerUtils().uninjectPlayerNow(p);}
        }
        getAPI().getEventManager().unregisterAllListeners();

        PacketType.Client.packetIds.clear();
        PacketType.Server.packetIds.clear();
        int i = 0;
        long init = System.currentTimeMillis();
        while(pendingUninjections.size() > 0) {
            if(i++ == 0) {
                System.out.println("PacketEvents is attempting to uninject some players.");
            }
            else if(System.currentTimeMillis() - init > 1000L) {
                init = System.currentTimeMillis();
                System.out.println("PacketEvents is still attempting to uninject some players...");
            }
        }
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

    public static Settings getSettings() {
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
        pendingUninjections.add(e.getPlayer());
        getAPI().getPlayerUtils().uninjectPlayer(e.getPlayer());

    }

    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        BukkitMoveEvent moveEvent = new BukkitMoveEvent(e);
        getAPI().getEventManager().callEvent(moveEvent);
        e.setCancelled(moveEvent.isCancelled());
    }
}
