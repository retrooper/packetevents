package io.github.retrooper.packetevents.handler;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import io.github.retrooper.packetevents.event.impl.PlayerUninjectEvent;
import org.bukkit.entity.Player;

import java.util.concurrent.Future;

public class NettyPacketHandler {
    private static boolean v1_7_nettyMode = false;

    static {
        try {
            Class.forName("io.netty.channel.Channel");
        } catch (ClassNotFoundException e) {
            v1_7_nettyMode = true;
        }
    }

    public static void injectPlayer(final Player player) {
        final PlayerInjectEvent injectEvent = new PlayerInjectEvent(player);
        PacketEvents.getAPI().getEventManager().callEvent(injectEvent);
        if (!injectEvent.isCancelled()) {
            if (v1_7_nettyMode) {
                NettyPacketHandler_7.injectPlayer(player);
            } else {
                NettyPacketHandler_8.injectPlayer(player);
            }
        }
    }

    public static Future<?> uninjectPlayer(final Player player) {
        try {
            final PlayerUninjectEvent uninjectEvent = new PlayerUninjectEvent(player, false);
            PacketEvents.getAPI().getEventManager().callEvent(uninjectEvent);
            if (!uninjectEvent.isCancelled()) {
                if (v1_7_nettyMode) {
                    return NettyPacketHandler_7.uninjectPlayer(player);
                } else {
                    return NettyPacketHandler_8.uninjectPlayer(player);
                }
            }
        } catch (Exception e) {

        }
        return null;
    }

    public static void uninjectPlayerNow(final Player player) {
        try {
            final PlayerUninjectEvent uninjectEvent = new PlayerUninjectEvent(player, true);
            PacketEvents.getAPI().getEventManager().callEvent(uninjectEvent);
            if (!uninjectEvent.isCancelled()) {
                if (v1_7_nettyMode) {
                    NettyPacketHandler_7.uninjectPlayerNow(player);
                } else {
                    NettyPacketHandler_8.uninjectPlayerNow(player);
                }
            }
        } catch (Exception e) {
        }
    }

    public static Object write(final Player sender, final Object packet) {
        final PacketSendEvent packetSendEvent = new PacketSendEvent(sender, packet);
        PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent);
        if (!packetSendEvent.isCancelled()) {
            return packet;
        }
        return null;
    }

    public static Object read(final Player receiver, final Object packet) {
        final PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(receiver, packet);
        PacketEvents.getAPI().getEventManager().callEvent(packetReceiveEvent);
        if (!packetReceiveEvent.isCancelled()) {
            return packet;
        }
        return null;
    }


}
