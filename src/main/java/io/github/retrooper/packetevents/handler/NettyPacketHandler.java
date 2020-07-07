package io.github.retrooper.packetevents.handler;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import io.github.retrooper.packetevents.event.impl.PlayerUninjectEvent;
import org.bukkit.entity.Player;

import java.util.concurrent.Future;

public class NettyPacketHandler {
    public static final String handlerName = "packet_handler";
    private static boolean v1_7_nettyMode = false;

    static {
        try {
            Class.forName("io.netty.channel.Channel");
        } catch (ClassNotFoundException e) {
            v1_7_nettyMode = true;
        }
    }

    public static void injectPlayer(final Player player, PacketEvents pe) {
        final PlayerInjectEvent injectEvent = new PlayerInjectEvent(player);
        pe.getAPI().getEventManager().callEvent(injectEvent, pe.getPlugin());
        if (!injectEvent.isCancelled()) {
            if (v1_7_nettyMode) {
                NettyPacketHandler_7.injectPlayer(player, pe);
            } else {
                NettyPacketHandler_8.injectPlayer(player, pe);
            }
        }
    }

    public static Future<?> uninjectPlayer(final Player player, PacketEvents pe) {
        final PlayerUninjectEvent uninjectEvent = new PlayerUninjectEvent(player);
        pe.getAPI().getEventManager().callEvent(uninjectEvent, pe.getPlugin());
        if (!uninjectEvent.isCancelled()) {
            if (v1_7_nettyMode) {
                return NettyPacketHandler_7.uninjectPlayer(player);
            } else {
                return NettyPacketHandler_8.uninjectPlayer(player);
            }
        }
        return null;
    }

    public static Object write(final Player sender, final Object packet, PacketEvents pe) {
        final String packetName = packet.getClass().getSimpleName();
        final PacketSendEvent packetSendEvent = new PacketSendEvent(sender, packetName, packet);
        pe.getAPI().getEventManager().callEvent(packetSendEvent, pe.getPlugin());
        if (!packetSendEvent.isCancelled()) {
            return packet;
        }
        return null;
    }

    public static Object read(final Player receiver, final Object packet, PacketEvents pe) {
        final String packetName = packet.getClass().getSimpleName();
        final PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(receiver, packetName, packet);
        pe.getAPI().getEventManager().callEvent(packetReceiveEvent, pe.getPlugin());
        if (!packetReceiveEvent.isCancelled()) {
            return packet;
        }
        return null;
    }


}
