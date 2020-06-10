package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.enums.ClientVersion;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.event.PacketHandler;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketLoginEvent;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import io.github.retrooper.packetevents.event.impl.ServerTickEvent;
import io.github.retrooper.packetevents.event.manager.EventManager;
import io.github.retrooper.packetevents.handler.TinyProtocolHandler;
import io.github.retrooper.packetevents.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.packetwrappers.login.WrappedPacketLoginHandshake;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class PacketEvents implements PacketListener{

    private static boolean hasRegistered = false;

    private static final ServerVersion version = ServerVersion.getVersion();
    private static PacketEvents instance;
    private static EventManager eventManager = new EventManager();

    private static int currentTick;

    private static BukkitTask serverTickTask;

    private static final HashMap<Object, ClientVersion> clientVersionLookup = new HashMap<Object, ClientVersion>();

    public static EventManager getEventManager() {
        return eventManager;
    }


    public static void start(final JavaPlugin plugin, final boolean serverTickEventEnabled) {
        if (!hasRegistered) {
            getEventManager().registerListener(getInstance());
            hasRegistered = true;
        }
        final TinyProtocolHandler packetHandler = new TinyProtocolHandler(plugin);
        packetHandler.initTinyProtocol();

        if (serverTickEventEnabled) {
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    getEventManager().callEvent(new ServerTickEvent(currentTick++, PacketEvents.currentCalculatedMS()));
                }
            };
            serverTickTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, 0L, 1L);
        }
    }

    public static void stop() {
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
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return tpsArray;
    }

    public static double getCurrentServerTPS() {
        return getRecentServerTPS()[0];
    }

    public static int getPing(final Player player) {
        return NMSUtils.getPlayerPing(player);
    }

    /**
     * This function returns nanoTime / 1 million,
     *
     * Why use this instead of System.currentTimeMillis()?
     *
     * System.currentTimeMillis() isn't supported on all machines,
     * This function is also more accurate, although it is slower on
     * Windows machines, it is faster than System.currentTimeMillis() on other Operating Systems!
     * @return
     */
    public static long currentCalculatedMS() {
        return System.nanoTime() / 1000000;
    }

    /**
     * Get the player's clients' version.
     *
     * Do not call this method in the PlayerInjectEvent or before the player is injected.
     * The EntityPlayer object is null at that time, resulting in the version lookup to fail.
     * @param player
     * @return
     */
    @Nullable
    public static ClientVersion getClientVersion(final Player player) {
        final Object channel = TinyProtocolHandler.getPlayerChannel(player);
        return clientVersionLookup.get(channel);
    }

    @Nullable
    public static ClientVersion getClientVersion(final Object channel) {
        return clientVersionLookup.get(channel);
    }

    @PacketHandler
    public void onLogin(final PacketLoginEvent e) {
        if (e.getPacketName().equals(Packet.Login.HANDSHAKE)) {
            final WrappedPacketLoginHandshake handshake = new WrappedPacketLoginHandshake(e.getPacket());
            final ClientVersion clientVersion = ClientVersion.fromProtocolVersion(handshake.getProtocolVersion());
            clientVersionLookup.put(e.getNettyChannel(), clientVersion);
        }
    }

    /**
     * Do not check the client version in or before the PlayerInjectEvent,
     * it will cause ERRORSS
     * @param e
     */
    @PacketHandler
    public void onInject(final PlayerInjectEvent e) {
        final String username = e.getPlayer().getName();

    }

    /**
     * Send a wrapped sendable packet to a player
     * @param player
     * @param sendable
     */
    public static void sendPacket(final Player player, final Sendable sendable) {
        NMSUtils.sendSendableWrapper(player, sendable);
    }
}
