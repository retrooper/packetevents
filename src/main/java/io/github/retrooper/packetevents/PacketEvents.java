package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.event.impl.ServerTickEvent;
import io.github.retrooper.packetevents.event.manager.EventManager;
import io.github.retrooper.packetevents.injector.PacketInjector;
import io.github.retrooper.packetevents.utils.NMSUtils;
import io.github.retrooper.packetevents.utils.TPSUtils;
import io.github.retrooper.packetevents.enums.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import javax.annotation.Nullable;
import java.util.concurrent.*;

public class PacketEvents implements Listener {
    private static final ServerVersion version = ServerVersion.getVersion();
    private static PacketEvents instance;
    private static final PacketInjector packetInjector = new PacketInjector();
    public static ExecutorService executor = Executors.newCachedThreadPool();
    private static final EventManager eventManager = new EventManager();

    private static int currentTick;

    private static BukkitTask serverTickTask;

    public static JavaPlugin plugin;

    public static EventManager getEventManager() {
        return eventManager;
    }

    public static void setup(final JavaPlugin plugin, final boolean serverTickEventEnabled) {
        PacketEvents.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(getInstance(), plugin);

        if (serverTickEventEnabled) {
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    PacketEvents.getEventManager().callEvent(new ServerTickEvent(currentTick++, PacketEvents.currentTimeMS()));
                }
            };
            serverTickTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, 0L, 1L);
        }
    }

    public static void cleanup() {
        if (serverTickTask != null) {
            serverTickTask.cancel();
        }
        getEventManager().unregisterAllListeners();
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (executor == null) {
            executor = Executors.newCachedThreadPool();
            packetInjector.injectPlayer(e.getPlayer());
            System.out.println("Failed to inject " + e.getPlayer().getName() + " asynchronously!");
        }

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                packetInjector.injectPlayer(e.getPlayer());
            }
        };
        Future<?> future = executor.submit(runnable);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        packetInjector.uninjectPlayer(e.getPlayer());
    }

    public static ServerVersion getServerVersion() {
        return version;
    }

    public static PacketEvents getInstance() {
        return instance == null ? instance = new PacketEvents() : instance;
    }

    public static boolean isServerTickTaskEnabled() {
        return serverTickTask != null;
    }

    @Nullable
    public static double[] getRecentServerTPS() {
        final double[] tpsArray = TPSUtils.getRecentTPS();
        final int size = tpsArray.length;
        for (int i = 0; i < size; i++) {
            if (tpsArray[i] > 20.0) {
                tpsArray[i] = 20.0;
            }
        }
        return tpsArray;
    }

    public static double getCurrentServerTPS() {
        return getRecentServerTPS()[0];
    }

    public static int getPing(final Player player) {
        return NMSUtils.getPlayerPing(player);
    }

    public static long currentTimeMS() {
        return System.nanoTime() / 1000000;
    }
}
