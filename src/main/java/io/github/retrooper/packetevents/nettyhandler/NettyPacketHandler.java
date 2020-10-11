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

package io.github.retrooper.packetevents.nettyhandler;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.event.impl.PlayerEjectEvent;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NettyPacketHandler {
    public static boolean v1_7_nettyMode = false;
    public static final ExecutorService
            executorService =
            Executors.newFixedThreadPool(Math.max(Runtime.getRuntime().availableProcessors(), 64));
    private static final HashMap<UUID, Long> keepAliveMap = new HashMap<>();

    static {
        try {
            Class.forName("io.netty.channel.Channel");
        } catch (ClassNotFoundException e) {
            v1_7_nettyMode = true;
        }
    }

    /**
     * Synchronously inject a player
     *
     * @param player Target player to inject
     */
    public static void injectPlayer(final Player player) {
        try {
            final PlayerInjectEvent injectEvent = new PlayerInjectEvent(player);
            PacketEvents.getAPI().getEventManager().callEvent(injectEvent);
            if (!injectEvent.isCancelled()) {
                if (v1_7_nettyMode) {
                    NettyPacketHandler_7.injectPlayer(player);
                } else {
                    NettyPacketHandler_8.injectPlayer(player);
                }
            }
        } catch (Exception ignored) {

        }
    }

    /**
     * Asynchronously inject a player
     *
     * @param player
     * @return {@link java.util.concurrent.Future}
     */
    public static Future<?> injectPlayerAsync(final Player player) {
        Object channel = NMSUtils.getChannel(player);
        Future<?> future = executorService.submit(() -> {
            try {
                final PlayerInjectEvent injectEvent = new PlayerInjectEvent(player, true);
                PacketEvents.getAPI().getEventManager().callEvent(injectEvent);
                if (!injectEvent.isCancelled()) {
                    if (v1_7_nettyMode) {
                        NettyPacketHandler_7.injectPlayer(player);
                    } else {
                        NettyPacketHandler_8.injectPlayer(player);
                    }
                }
            } catch (Exception ignored) {

            }
        });
        return future;
    }

    /**
     * Synchronously eject a player.
     *
     * @param player
     */
    public static void ejectPlayer(final Player player) {
        NMSUtils.channelCache.remove(player.getUniqueId());
        try {
            final PlayerEjectEvent uninjectEvent = new PlayerEjectEvent(player);
            PacketEvents.getAPI().getEventManager().callEvent(uninjectEvent);
            if (!uninjectEvent.isCancelled()) {
                if (v1_7_nettyMode) {
                    NettyPacketHandler_7.ejectPlayer(player);
                } else {
                    NettyPacketHandler_8.ejectPlayer(player);
                }
            }
        } catch (Exception ignored) {

        }
    }

    /**
     * Asynchronously eject a player
     *
     * @param player
     * @return {@link java.util.concurrent.Future}
     */
    public static Future<?> ejectPlayerAsync(final Player player) {
        NMSUtils.channelCache.remove(player.getUniqueId());
        Future<?> f = executorService.submit(() -> {
            try {
                final PlayerEjectEvent uninjectEvent = new PlayerEjectEvent(player, true);
                PacketEvents.getAPI().getEventManager().callEvent(uninjectEvent);
                if (!uninjectEvent.isCancelled()) {
                    if (v1_7_nettyMode) {
                        NettyPacketHandler_7.ejectPlayer(player);
                    } else {
                        NettyPacketHandler_8.ejectPlayer(player);
                    }
                }
            } catch (Exception ignored) {

            }
        });
        return f;
    }

    /**
     * This function is called each time the server plans to send a packet to the client.
     *
     * @param sender
     * @param packet
     * @return
     */
    public static Object write(final Player sender, final Object packet) {
        final PacketSendEvent packetSendEvent = new PacketSendEvent(sender, packet);
        PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent);
        interceptSendEvent(packetSendEvent);
        if (!packetSendEvent.isCancelled()) {
            return packet;
        }
        return null;
    }


    /**
     * This function is called each time the server receives a packet from the client.
     *
     * @param receiver
     * @param packet
     * @return
     */
    public static Object read(final Player receiver, final Object packet) {
        final PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(receiver, packet);
        PacketEvents.getAPI().getEventManager().callEvent(packetReceiveEvent);
        interceptReceiveEvent(packetReceiveEvent);
        if (!packetReceiveEvent.isCancelled()) {
            return packet;
        }
        return null;
    }

    private static void interceptSendEvent(PacketSendEvent event) {
        if (event.getPacketId() == PacketType.Server.KEEP_ALIVE) {
            keepAliveMap.put(event.getPlayer().getUniqueId(), event.getTimestamp());
        }
    }

    private static void interceptReceiveEvent(PacketReceiveEvent event) {
        if (event.getPacketId() == PacketType.Client.KEEP_ALIVE) {
            UUID uuid = event.getPlayer().getUniqueId();
            long timestamp = keepAliveMap.getOrDefault(uuid, event.getTimestamp());
            long currentTime = event.getTimestamp();
            long ping = currentTime - timestamp;
            long smoothedPing = ((PacketEvents.getAPI().getPlayerUtils().getSmoothedPing(event.getPlayer()) * 3) + ping) / 4;
            PacketEvents.getAPI().getPlayerUtils().playerPingMap.put(uuid, (short) ping);
            PacketEvents.getAPI().getPlayerUtils().playerSmoothedPingMap.put(uuid, (short) smoothedPing);
        }
    }

    public static void sendPacket(Object channel, Object packet) {
        if (v1_7_nettyMode) {
            NettyPacketHandler_7.sendPacket(channel, packet);
        } else {
            NettyPacketHandler_8.sendPacket(channel, packet);
        }
    }
}