package io.github.retrooper.packetevents.handler;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.Future;

public class NettyPacketHandler {
    public static final String handlerName = "PacketEventsListener";
    private static final ServerVersion version = PacketEvents.getServerVersion();

    public static final HashSet<UUID> injectedList = new HashSet<UUID>();

    public static void injectPlayer(final Player player) {
        if (!hasInjected(player)) {
            if (version.isLowerThan(ServerVersion.v_1_8)) {
                NettyPacketHandler_7.injectPlayer(player);
            } else {
                NettyPacketHandler_8.injectPlayer(player);
            }
            injectedList.add(player.getUniqueId());
        }
    }

    public static Future<?> uninjectPlayer(final Player player) {
        if (hasInjected(player)) {
            injectedList.remove(player.getUniqueId());
            if (version.isLowerThan(ServerVersion.v_1_8)) {
                return NettyPacketHandler_7.uninjectPlayer(player);
            } else {
                return NettyPacketHandler_8.uninjectPlayer(player);
            }
        }
        return null;
    }

    public static boolean hasInjected(final Player player) {
        return injectedList.contains(player.getUniqueId());
    }

    public static Object read(final Player receiver, final Object packet) {
        final String packetName = packet.getClass().getSimpleName();
        final PacketSendEvent packetSendEvent = new PacketSendEvent(receiver, packetName, packet);
        PacketEvents.getEventManager().callEvent(packetSendEvent);
        if (!packetSendEvent.isCancelled()) {
            return packet;
        }
        return null;
    }

    public static Object write(final Player sender, final Object packet) {
        final String packetName = packet.getClass().getSimpleName();
        final PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(sender, packetName, packet);
        PacketEvents.getEventManager().callEvent(packetReceiveEvent);
        if (!packetReceiveEvent.isCancelled()) {
            return packet;
        }
        return null;
    }


}
