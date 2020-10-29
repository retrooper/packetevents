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
import io.github.retrooper.packetevents.event.impl.*;
import io.github.retrooper.packetevents.nettyhandler.tinyprotocol.TinyProtocol_7;
import io.github.retrooper.packetevents.nettyhandler.tinyprotocol.TinyProtocol_8;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyPacketManager {
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
        if (TinyProtocolManager.tinyProtocol == null) {
            try {
                final PlayerInjectEvent injectEvent = new PlayerInjectEvent(player);
                PacketEvents.getAPI().getEventManager().callEvent(injectEvent);
                if (!injectEvent.isCancelled()) {
                    if (v1_7_nettyMode) {
                        NettyPacketManager_7.injectPlayer(player);
                    } else {
                        NettyPacketManager_8.injectPlayer(player);
                    }
                }
            } catch (Exception ignored) {

            }
        } else {
            if (TinyProtocolManager.tinyProtocol instanceof TinyProtocol_7) {
                TinyProtocol_7 tinyProt = (TinyProtocol_7) TinyProtocolManager.tinyProtocol;
                tinyProt.injectPlayer(player);
            } else if (TinyProtocolManager.tinyProtocol instanceof TinyProtocol_8) {
                TinyProtocol_8 tinyProt = (TinyProtocol_8) TinyProtocolManager.tinyProtocol;
                tinyProt.injectPlayer(player);
            }
        }
    }

    /**
     * Asynchronously inject a player
     *
     * @param player
     * @return {@link java.util.concurrent.Future}
     */
    public static void injectPlayerAsync(final Player player) {
        if (TinyProtocolManager.tinyProtocol == null) {
            Object channel = NMSUtils.getChannel(player);
            executorService.execute(() -> {
                try {
                    final PlayerInjectEvent injectEvent = new PlayerInjectEvent(player, true);
                    PacketEvents.getAPI().getEventManager().callEvent(injectEvent);
                    if (!injectEvent.isCancelled()) {
                        if (v1_7_nettyMode) {
                            NettyPacketManager_7.injectPlayer(player);
                        } else {
                            NettyPacketManager_8.injectPlayer(player);
                        }
                    }
                } catch (Exception ignored) {

                }
            });
        } else {
            if (TinyProtocolManager.tinyProtocol instanceof TinyProtocol_7) {
                TinyProtocol_7 tinyProt = (TinyProtocol_7) TinyProtocolManager.tinyProtocol;
                tinyProt.injectPlayerAsync(player);
            } else if (TinyProtocolManager.tinyProtocol instanceof TinyProtocol_8) {
                TinyProtocol_8 tinyProt = (TinyProtocol_8) TinyProtocolManager.tinyProtocol;
                tinyProt.injectPlayerAsync(player);
            }
        }
    }

    /**
     * Synchronously eject a player.
     *
     * @param player
     */
    public static void ejectPlayer(final Player player) {
        keepAliveMap.remove(player.getUniqueId());
        if (TinyProtocolManager.tinyProtocol == null) {
            NMSUtils.channelCache.remove(player.getUniqueId());

            try {
                final PlayerEjectEvent uninjectEvent = new PlayerEjectEvent(player);
                PacketEvents.getAPI().getEventManager().callEvent(uninjectEvent);
                if (!uninjectEvent.isCancelled()) {
                    if (v1_7_nettyMode) {
                        NettyPacketManager_7.ejectPlayer(player);
                    } else {
                        NettyPacketManager_8.ejectPlayer(player);
                    }
                }
            } catch (Exception ignored) {

            }
        } else {
            if (TinyProtocolManager.tinyProtocol instanceof TinyProtocol_7) {
                TinyProtocol_7 tinyProt = (TinyProtocol_7) TinyProtocolManager.tinyProtocol;
                tinyProt.uninjectPlayer(player);
            } else if (TinyProtocolManager.tinyProtocol instanceof TinyProtocol_8) {
                TinyProtocol_8 tinyProt = (TinyProtocol_8) TinyProtocolManager.tinyProtocol;
                tinyProt.uninjectPlayer(player);
            }
        }
    }

    /**
     * Asynchronously eject a player
     *
     * @param player
     * @return {@link java.util.concurrent.Future}
     */
    public static void ejectPlayerAsync(final Player player) {
        keepAliveMap.remove(player.getUniqueId());
        if (TinyProtocolManager.tinyProtocol == null) {
            NMSUtils.channelCache.remove(player.getUniqueId());

            executorService.execute(() -> {
                try {
                    final PlayerEjectEvent uninjectEvent = new PlayerEjectEvent(player, true);
                    PacketEvents.getAPI().getEventManager().callEvent(uninjectEvent);
                    if (!uninjectEvent.isCancelled()) {
                        if (v1_7_nettyMode) {
                            NettyPacketManager_7.ejectPlayer(player);
                        } else {
                            NettyPacketManager_8.ejectPlayer(player);
                        }
                    }
                } catch (Exception ignored) {

                }
            });
        } else {
            if (TinyProtocolManager.tinyProtocol instanceof TinyProtocol_7) {
                TinyProtocol_7 tinyProt = (TinyProtocol_7) TinyProtocolManager.tinyProtocol;
                tinyProt.uninjectPlayerAsync(player);
            } else if (TinyProtocolManager.tinyProtocol instanceof TinyProtocol_8) {
                TinyProtocol_8 tinyProt = (TinyProtocol_8) TinyProtocolManager.tinyProtocol;
                tinyProt.uninjectPlayerAsync(player);
            }
        }
    }

    /**
     * This function is called each time the server plans to send a packet to the client.
     *
     * @param player
     * @param packet
     * @return
     */
    public static Object write(final Player player, Object packet) {
        switch (ClassUtil.getClassSimpleName(packet.getClass())) {
            case "PacketLoginOutDisconnect":
            case "PacketLoginOutEncryptionBegin":
            case "PacketLoginOutSetCompression":
            case "PacketLoginOutSuccess":
                final PacketLoginEvent packetLoginEvent = new PacketLoginEvent(TinyProtocolManager.getChannel(player), packet);
                PacketEvents.getAPI().getEventManager().callEvent(packetLoginEvent);
                interceptLoginEvent(packetLoginEvent);
                if (packetLoginEvent.isCancelled()) {
                    packet = null;
                }
                break;
            case "PacketStatusOutPong":
                final PacketStatusEvent packetStatusEvent = new PacketStatusEvent(TinyProtocolManager.getChannel(player), packet);
                PacketEvents.getAPI().getEventManager().callEvent(packetStatusEvent);
                interceptStatusEvent(packetStatusEvent);
                if (packetStatusEvent.isCancelled()) {
                    packet = null;
                }
                break;
            default:
                final PacketSendEvent packetSendEvent = new PacketSendEvent(player, packet);
                PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent);
                interceptSendEvent(packetSendEvent);
                if (packetSendEvent.isCancelled()) {
                    packet = null;
                }
                break;
        }

        return packet;
    }


    /**
     * This function is called each time the server receives a packet from the client.
     *
     * @param player
     * @param packet
     * @return
     */
    public static Object read(final Player player, Object packet) {
        switch (ClassUtil.getClassSimpleName(packet.getClass())) {
            case "PacketHandshakingInSetProtocol":
            case "PacketLoginInCustomPayload":
            case "PacketLoginInStart":
            case "PacketLoginInEncryptionBegin":
                final PacketLoginEvent packetLoginEvent = new PacketLoginEvent(TinyProtocolManager.getChannel(player), packet);
                PacketEvents.getAPI().getEventManager().callEvent(packetLoginEvent);
                interceptLoginEvent(packetLoginEvent);
                if (packetLoginEvent.isCancelled()) {
                    packet = null;
                }
                break;
            case "PacketStatusInPing":
                final PacketStatusEvent packetStatusEvent = new PacketStatusEvent(TinyProtocolManager.getChannel(player), packet);
                PacketEvents.getAPI().getEventManager().callEvent(packetStatusEvent);
                interceptStatusEvent(packetStatusEvent);
                if (packetStatusEvent.isCancelled()) {
                    packet = null;
                }
                break;
            default:
                final PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(player, packet);
                PacketEvents.getAPI().getEventManager().callEvent(packetReceiveEvent);
                interceptReceiveEvent(packetReceiveEvent);
                if (packetReceiveEvent.isCancelled()) {
                    packet = null;
                }
                break;
        }
        return packet;
    }

    public static void postRead(Player player, final Object packet) {
        switch (ClassUtil.getClassSimpleName(packet.getClass())) {
            case "PacketHandshakingInSetProtocol":
            case "PacketLoginInCustomPayload":
            case "PacketLoginInStart":
            case "PacketLoginInEncryptionBegin":
            case "PacketStatusInPing":
                break;
            default:
                PacketEvents.getAPI().getEventManager().callEvent(new PostPacketReceiveEvent(player, packet));
                break;
        }
    }

    public static void postSend(Player player, final Object packet) {
        switch(ClassUtil.getClassSimpleName(packet.getClass())) {
            case "PacketLoginOutDisconnect":
            case "PacketLoginOutEncryptionBegin":
            case "PacketLoginOutSetCompression":
            case "PacketLoginOutSuccess":
            case "PacketStatusOutPong":
                break;
            default:
                PacketEvents.getAPI().getEventManager().callEvent(new PostPacketSendEvent(player, packet));
                break;
        }

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

    private static void interceptLoginEvent(PacketLoginEvent event) {
        if (event.getPacketId() == PacketType.Login.HANDSHAKE) {

        }
    }

    private static void interceptStatusEvent(PacketStatusEvent event) {

    }

    public static void sendPacket(Object channel, Object packet) {
        if (v1_7_nettyMode) {
            if (TinyProtocolManager.tinyProtocol == null) {
                NettyPacketManager_7.sendPacket(channel, packet);
            } else {
                TinyProtocol_7 tinyProt = (TinyProtocol_7) TinyProtocolManager.tinyProtocol;
                tinyProt.sendPacket(channel, packet);
            }
        } else {
            if (TinyProtocolManager.tinyProtocol == null) {
                NettyPacketManager_8.sendPacket(channel, packet);
            } else {
                TinyProtocol_8 tinyProt = (TinyProtocol_8) TinyProtocolManager.tinyProtocol;
                tinyProt.sendPacket(channel, packet);
            }
        }
    }
}