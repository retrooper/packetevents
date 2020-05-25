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
import org.bukkit.scheduler.BukkitTask;
import java.util.concurrent.*;

public class PacketEvents implements Listener {
    private static PacketEvents instance;
    private static PacketInjector packetInjector = new PacketInjector();
    public static ExecutorService executor = Executors.newCachedThreadPool();
    private static final PacketManager packetManager = new PacketManager();

    private static int currentTick = 0;

    private static BukkitTask serverTickTask;

    private static boolean serverTickEventActive = false;


    public static PacketManager getPacketManager() {
        return packetManager;
    }

    public static void setup(JavaPlugin plugin, boolean serverTickEventEnabled) {
        serverTickEventActive = serverTickEventEnabled;
        Bukkit.getPluginManager().registerEvents(getInstance(), plugin);

        if (serverTickEventEnabled) {
            serverTickTask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    PacketEvents.getPacketManager().callEvent(new ServerTickEvent(currentTick++, System.nanoTime() / 1000000));
                }
            }, 0L, 1L);
        }
    }

    public static void cleanup(JavaPlugin plugin) {
        if (serverTickEventActive) {
            serverTickTask.cancel();
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

    public static ServerVersion getServerVersion() {
        return ServerVersion.getVersion();
    }

    public static PacketEvents getInstance() {
        return instance == null ? instance = new PacketEvents() : instance;
    }
}
