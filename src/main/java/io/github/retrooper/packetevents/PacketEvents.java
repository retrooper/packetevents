package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.event.impl.ServerTickEvent;
import io.github.retrooper.packetevents.event.manager.EventManager;
import io.github.retrooper.packetevents.injector.PacketHandler;
import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PacketEvents{
    private static final ServerVersion version = ServerVersion.getVersion();
    private static PacketEvents instance;
    public static JavaPlugin plugin;
    private static PacketHandler packetInjector;
    public static ExecutorService executor = Executors.newCachedThreadPool();

    private static EventManager eventManager = new EventManager();

    private static int currentTick;

    private static BukkitTask serverTickTask;

    public static EventManager getEventManager() {
        return eventManager;
    }


    public static void setup(final JavaPlugin plugin, final boolean serverTickEventEnabled) {
        PacketEvents.plugin = plugin;
        packetInjector = new PacketHandler(plugin);
        packetInjector.initTinyProtocol();

        if (serverTickEventEnabled) {
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    getEventManager().callEvent(new ServerTickEvent(currentTick++, PacketEvents.currentTimeMS()));
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


    public static ServerVersion getServerVersion() {
        return version;
    }

    public static PacketEvents getInstance() {
        return instance == null ? instance = new PacketEvents() : instance;
    }

    public static boolean isServerTickTaskEnabled() {
        return serverTickTask != null;
    }


    public static double[] getRecentServerTPS() {
        double[] tpsArray = new double[0];
        try {
            tpsArray = NMSUtils.recentTPS();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
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
        return System.currentTimeMillis();
    }

    public static void sendPacket(final Player player, final Sendable sendable) {
        NMSUtils.sendSendableWrapper(player, sendable);
    }
}
