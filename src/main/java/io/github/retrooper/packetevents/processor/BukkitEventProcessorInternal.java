package io.github.retrooper.packetevents.processor;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PostPlayerInjectEvent;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import io.github.retrooper.packetevents.utils.versionlookup.VersionLookupUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.net.InetSocketAddress;
import java.util.UUID;

public class BukkitEventProcessorInternal implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent e) {
        final Player player = e.getPlayer();
        if (!PacketEvents.get().getSettings().shouldUseCompatibilityInjector()) {
            PacketEvents.get().injector.injectPlayer(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        InetSocketAddress address = player.getAddress();

        boolean shouldInject = PacketEvents.get().getSettings().shouldUseCompatibilityInjector() || !(PacketEvents.get().injector.hasInjected(e.getPlayer()));
        //Inject now if we are using the compatibility-injector or inject if the early injector failed to inject them.
        if (shouldInject) {
            PacketEvents.get().injector.injectPlayer(player);
        }

        boolean dependencyAvailable = VersionLookupUtils.isDependencyAvailable();
        PacketEvents.get().getPlayerUtils().loginTime.put(player.getUniqueId(), System.currentTimeMillis());
        //A supported dependency is available, we need to first ask the dependency for the client version.
        if (dependencyAvailable) {
            //We are resolving version one tick later for extra safety. Some dependencies throw exceptions if we throw too early.
            Bukkit.getScheduler().runTaskLaterAsynchronously(PacketEvents.get().getPlugin(), () -> {
                try {
                    int protocolVersion = VersionLookupUtils.getProtocolVersion(player);
                    ClientVersion version = ClientVersion.getClientVersion(protocolVersion);
                    PacketEvents.get().getPlayerUtils().clientVersionsMap.put(address, version);
                } catch (Exception ignored) {

                }
                PacketEvents.get().getEventManager().callEvent(new PostPlayerInjectEvent(player, true));
            }, 1L);
        } else {
            //Dependency isn't available, we can already call the post player inject event.
            PacketEvents.get().getEventManager().callEvent(new PostPlayerInjectEvent(e.getPlayer(), false));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        InetSocketAddress address = player.getAddress();
        //Cleanup user data
        PacketEvents.get().getPlayerUtils().loginTime.remove(uuid);
        PacketEvents.get().getPlayerUtils().playerPingMap.remove(uuid);
        PacketEvents.get().getPlayerUtils().playerSmoothedPingMap.remove(uuid);
        PacketEvents.get().getPlayerUtils().clientVersionsMap.remove(address);
        PacketEvents.get().getPlayerUtils().tempClientVersionMap.remove(address);

        PacketEvents.get().packetProcessorInternal.keepAliveMap.remove(uuid);
        PacketEvents.get().packetProcessorInternal.channelMap.remove(player.getName());

    }
}
