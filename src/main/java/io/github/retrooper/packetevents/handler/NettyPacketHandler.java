package io.github.retrooper.packetevents.handler;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import io.github.retrooper.packetevents.event.impl.PlayerUninjectEvent;
import org.bukkit.entity.Player;

import java.util.concurrent.Future;

public class NettyPacketHandler {
    public static final String handlerName = "packet_handler";
    private static final ServerVersion version = PacketEvents.getAPI().getServerUtilities().getServerVersion();

    public static void injectPlayer(final Player player) {
        final PlayerInjectEvent injectEvent = new PlayerInjectEvent(player);
        PacketEvents.getAPI().getEventManager().callEvent(injectEvent);
        if (!injectEvent.isCancelled()) {
            if (version.isLowerThan(ServerVersion.v_1_8)) {
                NettyPacketHandler_7.injectPlayer(player);
            } else {
                NettyPacketHandler_8.injectPlayer(player);
            }
        }
    }

    public static Future<?> uninjectPlayer(final Player player) {
        final PlayerUninjectEvent uninjectEvent = new PlayerUninjectEvent(player);
        PacketEvents.getAPI().getEventManager().callEvent(uninjectEvent);
        if (!uninjectEvent.isCancelled()) {
            if (version.isLowerThan(ServerVersion.v_1_8)) {
                return NettyPacketHandler_7.uninjectPlayer(player);
            } else {
                return NettyPacketHandler_8.uninjectPlayer(player);
            }
        }
        return null;
    }

    public static Object write(final Player sender, final Object packet) {
        final String packetName = packet.getClass().getSimpleName();
        final PacketSendEvent packetSendEvent = new PacketSendEvent(sender, packetName, packet);
        PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent);
        if (!packetSendEvent.isCancelled()) {
            return packet;
        }
        return null;
    }

    public static Object read(final Player receiver, final Object packet) {
        final String packetName = packet.getClass().getSimpleName();
        final PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(receiver, packetName, packet);
        PacketEvents.getAPI().getEventManager().callEvent(packetReceiveEvent);
        if (!packetReceiveEvent.isCancelled()) {
            return packet;
        }
        return null;
    }


}
