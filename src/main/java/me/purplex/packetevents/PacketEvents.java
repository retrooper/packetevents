package me.purplex.packetevents;

import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.injector.PacketInjector;
import me.purplex.packetevents.packetevent.impl.ServerTickEvent;
import me.purplex.packetevents.packetevent.manager.PacketManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.*;

public class PacketEvents implements Listener {
    private static PacketEvents instance;
    private static PacketInjector packetInjector = new PacketInjector();
    public static ExecutorService executor = Executors.newCachedThreadPool();
    private static final PacketManager packetManager = new PacketManager();

    public static ServerVersion getServerVersion() {
        return ServerVersion.getVersion();
    }

    private static int asyncRepeatingTask;

    private static boolean serverTickEventActive = false;


    public static PacketManager getPacketManager() {
        return packetManager;
    }

    public static void setup(JavaPlugin plugin, boolean serverTickEventEnabled) {
        serverTickEventActive = serverTickEventEnabled;
        Bukkit.getPluginManager().registerEvents(getInstance(), plugin);

        if (serverTickEventEnabled) {
            asyncRepeatingTask = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    long now = System.currentTimeMillis();
                    PacketEvents.getPacketManager().callEvent(new ServerTickEvent(now));
                }
            }, 0L, 1L);
        }
    }

    public static void cleanup(JavaPlugin plugin) {
        if (serverTickEventActive) {
            Bukkit.getScheduler().cancelTasks(plugin);
        }
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (executor == null) {
            executor = Executors.newCachedThreadPool();
            packetInjector.injectPlayer(e.getPlayer());
            System.out.println("Failed to inject " + e.getPlayer().getName() + " asynchronously!");
        }
        executor.submit(new Runnable() {
            @Override
            public void run() {
                packetInjector.injectPlayer(e.getPlayer());
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        packetInjector.uninjectPlayer(e.getPlayer());
    }

    public static PacketEvents getInstance() {
        return instance == null ? instance = new PacketEvents() : instance;
    }
}
