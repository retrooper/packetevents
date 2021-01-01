/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.event.manager.EventManager;
import io.github.retrooper.packetevents.event.manager.PEEventManager;
import io.github.retrooper.packetevents.exceptions.PacketEventsLoadFailureException;
import io.github.retrooper.packetevents.handler.PacketHandlerInternal;
import io.github.retrooper.packetevents.injector.lateinjector.LateChannelInjector;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.updatechecker.UpdateChecker;
import io.github.retrooper.packetevents.utils.entityfinder.EntityFinderUtils;
import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufUtil;
import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufUtil_7;
import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufUtil_8;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import io.github.retrooper.packetevents.utils.player.PlayerUtils;
import io.github.retrooper.packetevents.utils.server.ServerUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.version.PEVersion;
import io.github.retrooper.packetevents.utils.versionlookup.VersionLookupUtils;
import io.github.retrooper.packetevents.utils.versionlookup.v_1_7_10.ProtocolVersionAccessor_v_1_7;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class PacketEvents implements Listener, EventManager {

    private static PacketEvents instance;
    private final PEVersion version = new PEVersion(1, 7, 9);
    private final EventManager eventManager = new PEEventManager();
    private final PlayerUtils playerUtils = new PlayerUtils();
    private final ServerUtils serverUtils = new ServerUtils();
    /**
     * General executor service, basically for anything that the packet executor service doesn't do.
     * For example update checking when you initialize PacketEvents.
     */
    public ExecutorService generalExecutorService = Executors.newSingleThreadExecutor(r -> new Thread(r, "PacketEvents-general"));

    //Executor used for player injecting/ejecting.
    public ExecutorService injectAndEjectExecutorService;//Initiated in init method

    public PacketHandlerInternal packetHandlerInternal;
    private boolean loading, loaded, initialized, initializing, stopping;
    private PacketEventsSettings settings = new PacketEventsSettings();
    private final ByteBufUtil byteBufUtil = LateChannelInjector.v1_7_nettyMode ? new ByteBufUtil_7() : new ByteBufUtil_8();


    public static PacketEvents create(final Plugin plugin) {
        if (!Bukkit.getServicesManager().isProvidedFor(PacketEvents.class)) {
            instance = new PacketEvents();
            Bukkit.getServicesManager().register(PacketEvents.class, instance,
                    plugin, ServicePriority.Normal);
            return instance;
        }
        return instance = Bukkit.getServicesManager().load(PacketEvents.class);
    }

    public static PacketEvents get() {
        return instance;
    }


    public boolean load() {
        if (!loaded && !loading) {
            loading = true;
            ServerVersion version = ServerVersion.getVersion();
            WrappedPacket.version = version;
            NMSUtils.version = version;
            EntityFinderUtils.version = version;

            try {
                NMSUtils.load();

                PacketTypeClasses.Play.Client.load();
                PacketTypeClasses.Play.Server.load();
                PacketTypeClasses.Login.Client.load();
                PacketTypeClasses.Status.Server.load();

                EntityFinderUtils.load();
            } catch (Exception ex) {
                loading = false;
                throw new PacketEventsLoadFailureException(ex);
            }
            loaded = true;
            loading = false;
            return true;
        }
        return false;
    }

    public void loadSettings(PacketEventsSettings settings) {
        this.settings = settings;
    }

    public boolean init(final Plugin plugin) {
        return init(plugin, settings);
    }

    public boolean init(final Plugin pl, PacketEventsSettings packetEventsSettings) {
        load();
        if (!initialized && !initializing) {
            initializing = true;
            settings = packetEventsSettings;
            if (settings.getInjectAndEjectThreadCount() < 1) {
                settings.injectAndEjectThreadCount(1);
            }
            settings.lock();

            int injectAndEjectThreadCount = settings.getInjectAndEjectThreadCount();
            injectAndEjectExecutorService = Executors.newFixedThreadPool(injectAndEjectThreadCount, new ThreadFactory() {

                private final AtomicInteger id = new AtomicInteger();

                @Override
                public Thread newThread(@NotNull Runnable r) {
                    return new Thread(r, "PacketEvents-inject-eject #" + id.getAndIncrement());
                }
            });

            //Register Bukkit listener
            final Plugin plugin = PacketEventsPlugin.getInstance();
            Bukkit.getPluginManager().registerEvents(this, plugin);
            packetHandlerInternal = new PacketHandlerInternal(plugin, settings.shouldInjectEarly());
            for (final Player p : Bukkit.getOnlinePlayers()) {
                try {
                    getPlayerUtils().injectPlayer(p);
                } catch (Exception ex) {
                    p.kickPlayer(getSettings().getInjectionFailureMessage());
                }
            }

            if (settings.shouldCheckForUpdates()) {
                generalExecutorService.execute(() -> new UpdateChecker().handleUpdate());

            }
            initialized = true;
            initializing = false;
            return true;
        }
        return false;
    }

    /**
     *
     */
    public void stop() {
        if (initialized && !stopping) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    packetHandlerInternal.ejectPlayer(player);
                }
            packetHandlerInternal.close();

            getEventManager().unregisterAllListeners();
            generalExecutorService.shutdownNow();
            injectAndEjectExecutorService.shutdownNow();
            initialized = false;
            stopping = false;
        }
    }

    public boolean hasLoaded() {
        return loaded;
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean isInitializing() {
        return initializing;
    }

    public boolean isStopping() {
        return stopping;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public Plugin getPlugin() {
        return PacketEventsPlugin.getInstance();
    }

    /**
     * Get the PacketEvents settings.
     *
     * @return Configure some settings for the API
     */
    public PacketEventsSettings getSettings() {
        return settings;
    }

    public PEVersion getVersion() {
        return version;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public PlayerUtils getPlayerUtils() {
        return playerUtils;
    }

    public ServerUtils getServerUtils() {
        return serverUtils;
    }

    public ByteBufUtil getByteBufUtil() {
        return byteBufUtil;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLogin(final PlayerLoginEvent e) {
        if (getSettings().shouldInjectEarly()) {
            try {
                packetHandlerInternal.injectPlayer(e.getPlayer());
            } catch (Exception ex) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, getSettings().getInjectionFailureMessage());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(final PlayerJoinEvent e) {
        InetSocketAddress socketAddress = e.getPlayer().getAddress();
        if (VersionLookupUtils.isDependencyAvailable()) {
            if (Bukkit.getPluginManager().isPluginEnabled("ViaBackwards") || Bukkit.getPluginManager().isPluginEnabled("ViaRewind")) {
                packetHandlerInternal.minimumPostPlayerInjectDeltaTime = 100L;
            }
        }
        else if (getServerUtils().getVersion() == ServerVersion.v_1_7_10) {
            ClientVersion version = ClientVersion.getClientVersion(ProtocolVersionAccessor_v_1_7.getProtocolVersion(e.getPlayer()));
            if(version == ClientVersion.UNRESOLVED) {
                version = getPlayerUtils().tempClientVersionMap.get(socketAddress);
                if(version == null) {
                    version = ClientVersion.UNRESOLVED;
                }
            }
            getPlayerUtils().clientVersionsMap.put(socketAddress, version);
        }
        packetHandlerInternal.channelTimePassed.put(packetHandlerInternal.getChannel(e.getPlayer().getName()),
                System.currentTimeMillis());
        if (!getSettings().shouldInjectEarly()) {
            try {
                packetHandlerInternal.injectPlayer(e.getPlayer());
            } catch (Exception ex) {
                e.getPlayer().kickPlayer(getSettings().getInjectionFailureMessage());
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(final PlayerQuitEvent e) {
        packetHandlerInternal.ejectPlayer(e.getPlayer());
    }

}
