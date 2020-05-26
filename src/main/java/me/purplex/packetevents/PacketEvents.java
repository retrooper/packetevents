package me.purplex.packetevents;

import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.injector.PacketInjector;
import me.purplex.packetevents.event.impl.ServerTickEvent;
import me.purplex.packetevents.event.manager.PacketManager;
import me.purplex.packetevents.utils.tps.*; //All tps' util classes
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.concurrent.*;

public class PacketEvents implements Listener {
    private static final ServerVersion version = ServerVersion.getVersion();
    private static PacketEvents instance;
    private static final PacketInjector packetInjector = new PacketInjector();
    public static ExecutorService executor = Executors.newCachedThreadPool();
    private static final PacketManager packetManager = new PacketManager();

    private static int currentTick;

    private static int serverTickTask;

    private static boolean serverTickEventActive = false;

    public static JavaPlugin plugin;

    public static PacketManager getPacketManager() {
        return packetManager;
    }

    @Deprecated
    public static void setup(final JavaPlugin plugin, final boolean serverTickEventEnabled) {
        PacketEvents.plugin = plugin;
        serverTickEventActive = serverTickEventEnabled;
        Bukkit.getPluginManager().registerEvents(getInstance(), plugin);

        if (serverTickEventEnabled) {
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    PacketEvents.getPacketManager().callEvent(new ServerTickEvent(currentTick++, PacketEvents.currentTimeMS()));
                }
            };
            serverTickTask = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, runnable, 0L, 1L);
        }
    }

    @Deprecated
    public static void cleanup() {
        if (serverTickEventActive) {
            Bukkit.getScheduler().cancelTask(serverTickTask);
        }
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


    public static double[] getRecentServerTPS() {
        final double[] tpsArray;
        if (version == ServerVersion.v_1_7_10) {
            tpsArray = TPS_1_7_10.getTPS();
        } else if (version == ServerVersion.v_1_8) {
            tpsArray = TPS_1_8.getTPS();
        } else if (version == ServerVersion.v_1_8_3) {
            tpsArray = TPS_1_8_3.getTPS();
        } else if (version == ServerVersion.v_1_8_8) {
            tpsArray = TPS_1_8_8.getTPS();
        } else if (version == ServerVersion.v_1_9) {
            tpsArray = TPS_1_9.getTPS();
        } else if (version == ServerVersion.v_1_9_4) {
            tpsArray = TPS_1_9_4.getTPS();
        } else if (version == ServerVersion.v_1_10) {
            tpsArray = TPS_1_10.getTPS();
        } else if (version == ServerVersion.v_1_11) {
            tpsArray = TPS_1_11.getTPS();
        } else if (version == ServerVersion.v_1_12) {
            tpsArray = TPS_1_12.getTPS();
        } else if (version == ServerVersion.v_1_13) {
            tpsArray = TPS_1_13.getTPS();
        } else if (version == ServerVersion.v_1_13_2) {
            tpsArray = TPS_1_13_2.getTPS();
        } else if (version == ServerVersion.v_1_14) {
            tpsArray = TPS_1_14.getTPS();
        } else if (version == ServerVersion.v_1_15) {
            tpsArray = TPS_1_15.getTPS();
        } else {
            throw new IllegalStateException("Unable to calculate your the server's TPS, this version is not supported! Make sure you are using Spigot!");
        }
        return tpsArray;
    }

    public static double getCurrentServerTPS() {
        return getRecentServerTPS()[0];
    }

    public static long currentTimeMS() {
        return System.nanoTime() / 1000000;
    }
}
