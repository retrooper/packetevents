package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.annotations.NotNull;
import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.ClientVersion;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.BukkitMoveEvent;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import io.github.retrooper.packetevents.event.impl.ServerTickEvent;
import io.github.retrooper.packetevents.event.manager.EventManager;
import io.github.retrooper.packetevents.handler.NettyPacketHandler;
import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public final class PacketEvents implements PacketListener, Listener {

    private static final ServerVersion version = ServerVersion.getVersion();
    private static final EventManager eventManager = new EventManager();
    private static final HashMap<UUID, ClientVersion> clientVersionsMap = new HashMap<UUID, ClientVersion>();
    private static boolean hasRegistered;
    private static PacketEvents instance;
    private static int currentTick;
    private static BukkitTask serverTickTask;

    public static EventManager getEventManager() {
        return eventManager;
    }


    /**
     * Starts the server tick task and initiates the TinyProtocolHandler
     *
     * @param plugin
     */
    public static void start(final Plugin plugin) {
        if (!hasRegistered) {
            //Register Bukkit and PacketListener
            getEventManager().registerListener(getInstance());

            Bukkit.getPluginManager().registerEvents(getInstance(), plugin);

            //Start the server tick task
            final Runnable tickRunnable = new Runnable() {
                @Override
                public void run() {
                    getEventManager().callEvent(new ServerTickEvent(currentTick++, PacketEvents.highlyPreciseMillis()));
                }
            };
            serverTickTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, tickRunnable, 0L, 1L);
            hasRegistered = true;
        }
    }

    /**
     * Stop all tasks and unregisters all PacketEvents' listeners
     */
    public static void stop() {
        if (serverTickTask != null) {
            serverTickTask.cancel();
        }
        getEventManager().unregisterAllListeners();
    }

    /**
     * Get the server's version
     *
     * @return version
     */
    public static ServerVersion getServerVersion() {
        return version;
    }

    /**
     * Returns the server tick task, you may cancel it if you wish.
     * To cancel it do PacketEvents.getServerTickTask().cancel();
     * <p>
     * This bukkit task runs asynchronously every tick and is started in the start(plugin) function
     *
     * @return serverTickTask
     */
    public static BukkitTask getServerTickTask() {
        return serverTickTask;
    }

    /**
     * Get an instance of the PacketEvents API class
     *
     * @return instance
     */
    public static PacketEvents getInstance() {
        return instance == null ? instance = new PacketEvents() : instance;
    }


    /**
     * Get the server's recent TPS values
     * TPS stands for ticks per second.
     * See to learn more about TPS<a href="https://apexminecrafthosting.com/what-is-minecraft-tps/">"https://apexminecrafthosting.com/what-is-minecraft-tps/"</a>
     *
     * @return recentTPS[]
     */
    public static double[] getRecentServerTPS() {
        double[] tpsArray = new double[0];
        try {
            tpsArray = NMSUtils.recentTPS();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return tpsArray;
    }

    /**
     * Get the server's current TPS
     * TPS stands for ticks per second.
     * See to learn more about TPS <a href="https://apexminecrafthosting.com/what-is-minecraft-tps/">"https://apexminecrafthosting.com/what-is-minecraft-tps/"</a>
     *
     * @return {@link PacketEvents#getRecentServerTPS()[0]}
     */
    public static double getCurrentServerTPS() {
        return getRecentServerTPS()[0];
    }

    /**
     * Get the player's ping.
     * Learn more about ping <a href="https://en.wikipedia.org/wiki/Ping_(networking_utility)">https://en.wikipedia.org/wiki/Ping_(networking_utility)</a>
     *
     * @param player
     * @return ping
     */
    public static int getPing(@NotNull final Player player) {
        return NMSUtils.getPlayerPing(player);
    }

    /**
     * When to use this(nano / 1 million) vs {@link System#currentTimeMillis()}
     * This is preciser than System.currentTimeMillis(), System.currentTimeMillis() can be up to 50ms off on some operating systems, but using this isn't cheap.
     * Java documentation recommend using nano time if you are measuring elapsed time.
     * In this function nanoTime is divided by 1 million giving us milliseconds.
     * It is also important to mention that using this method doesn't guarantee thread safety.
     *
     * @return nanoTime / 1 million
     */
    public static long highlyPreciseMillis() {
        return System.nanoTime() / 1000000;
    }


    /**
     * Get the player's version.
     * Do not call this method in the PlayerInjectEvent, it is safe to call it in the PostPlayerInjectEvent.
     * The EntityPlayer object is null at that time, resulting in the version lookup to fail.
     *
     * @param player
     * @return ClientVersion
     */

    @Nullable
    public static ClientVersion getClientVersion(@NotNull final Player player) {
        return getClientVersion(player.getUniqueId());
    }

    public static ClientVersion getClientVersion(@NotNull final UUID uuid) {
        return clientVersionsMap.get(uuid);
    }

    /**
     * Version independent player injection
     *
     * @param player
     */
    public static void injectPlayer(final Player player) {
        NettyPacketHandler.injectPlayer(player);
    }

    /**
     * Version independant player injection
     *
     * @param player
     */
    public static void uninjectPlayer(final Player player) {
        NettyPacketHandler.uninjectPlayer(player);
    }


    /**
     * Send a wrapped sendable packet to a player
     *
     * @param player
     * @param sendable
     */
    public static void sendPacket(final Player player, final Sendable sendable) {
        NMSUtils.sendSendableWrapper(player, sendable);
    }

    /**
     * Do not check the client version in or before the PlayerInjectEvent, use the PostPlayerInjectEvent.
     * It is not recommended to do much in the PlayerInjectEvent, as some fields in the Player object are be null.
     * Use the PostPlayerInjectEvent which is only called after the PlayerJoinEvent if the player was injected.
     *
     * @param e
     */
    @PacketHandler
    public void onInject(final PlayerInjectEvent e) {
        clientVersionsMap.put(e.getPlayer().getUniqueId(), e.getClientVersion());
    }

    @PacketHandler
    public void onReceive(final PacketReceiveEvent e) {
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        injectPlayer(e.getPlayer());
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        uninjectPlayer(e.getPlayer());
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        PacketEvents.getEventManager().callEvent(new BukkitMoveEvent(e));
    }
}
