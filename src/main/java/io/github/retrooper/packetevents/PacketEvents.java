package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.BukkitMoveEvent;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public final class PacketEvents implements PacketListener, Listener {
    public static HashMap<Plugin, Integer> plugins = new HashMap<Plugin, Integer>();
    public PacketEvents(final Plugin plugin) {
        this.plugin = plugin;
        plugins.put(plugin, plugins.size());
    }
    private final Plugin plugin;
    private final PacketEventsAPI packetEventsAPI = new PacketEventsAPI();
    private static boolean hasLoaded;
    private static final Settings settings = new Settings();

    /**
     * Call this before start()
     */
    public static void load() {
        PacketTypeClasses.Client.load();
        PacketTypeClasses.Server.load();
        hasLoaded = true;
    }

    /**
     *Loads PacketEvents if you haven't already, Sets everything up, injects all players
     */
    public void start() {
        if (!hasLoaded) {
            load();
        }
            //Register Bukkit and PacketListener
            getAPI().getEventManager().registerListener(this);

            Bukkit.getPluginManager().registerEvents(this, plugin);

            for(final Player p : Bukkit.getServer().getOnlinePlayers()) {
                getAPI().getPlayerUtilities().injectPlayer(p, this);
            }

    }

    /**
     * Stop all tasks and unregisters all PacketEvents' listeners
     */
    public void stop() {
        getAPI().getEventManager().unregisterAllListeners();

        PacketType.Client.packetIds.clear();
        PacketType.Server.packetIds.clear();
        for(final Player p : Bukkit.getServer().getOnlinePlayers()) {
            getAPI().getPlayerUtilities().uninjectPlayer(p, this);
        }
    }

    public PacketEventsAPI getAPI() {
        return packetEventsAPI;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getHandlerName() {
        return "packetevents_" + plugins.get(plugin);
    }
    public static Settings getSettings() {
        return settings;
    }

    @PacketHandler
    public void onInject(final PlayerInjectEvent e) {
        getAPI().getPlayerUtilities().setClientVersion(e.getPlayer(), e.getClientVersion());
    }

    @PacketHandler
    public void onReceive(final PacketReceiveEvent e) {
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        getAPI().getPlayerUtilities().injectPlayer(e.getPlayer(), this);
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        getAPI().getPlayerUtilities().clearClientVersion(e.getPlayer());
        getAPI().getPlayerUtilities().uninjectPlayer(e.getPlayer(), this);
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        getAPI().getEventManager().callEvent(new BukkitMoveEvent(e), getPlugin());
    }
}
