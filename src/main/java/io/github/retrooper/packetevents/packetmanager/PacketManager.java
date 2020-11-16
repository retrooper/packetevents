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

package io.github.retrooper.packetevents.packetmanager;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.*;
import io.github.retrooper.packetevents.packetmanager.netty.NettyPacketManager;
import io.github.retrooper.packetevents.packetmanager.tinyprotocol.TinyProtocol;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.login.in.WrappedPacketLoginHandshake;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketManager {

    private static final AtomicInteger handlerID = new AtomicInteger();
    private final int handlerNumber = handlerID.getAndIncrement();
    private final Plugin plugin;
    private final boolean tinyProtocolMode;
    public final TinyProtocol tinyProtocol;
    public final NettyPacketManager nettyProtocol;
    private final HashMap<UUID, Long> keepAliveMap = new HashMap<>();

    public PacketManager(Plugin plugin, boolean tinyProtocolMode) {
        this.plugin = plugin;
        this.tinyProtocolMode = tinyProtocolMode;
        if (tinyProtocolMode) {
            tinyProtocol = new TinyProtocol(plugin);
            nettyProtocol = null;
        } else {
            tinyProtocol = null;
            nettyProtocol = new NettyPacketManager(plugin);
        }
    }

    public void injectPlayer(Player player) {
        if (PacketEvents.getSettings().shouldInjectAsync()) {
            injectPlayerAsync(player);
        } else {
            injectPlayerSync(player);
        }
    }

    public void ejectPlayer(Player player) {
        if (PacketEvents.getSettings().shouldEjectAsync()) {
            ejectPlayerAsync(player);
        } else {
            ejectPlayerSync(player);
        }
    }

    public void injectPlayerSync(Player player) {
        PlayerInjectEvent injectEvent = new PlayerInjectEvent(player, false);
        PacketEvents.getAPI().getEventManager().callEvent(injectEvent);
        if (!injectEvent.isCancelled()) {
            if (tinyProtocolMode) {
                tinyProtocol.injectPlayer(player);
            } else {
                nettyProtocol.injectPlayer(player);
            }
        }
    }

    public void injectPlayerAsync(Player player) {
        PlayerInjectEvent injectEvent = new PlayerInjectEvent(player, true);
        PacketEvents.getAPI().getEventManager().callEvent(injectEvent);
        if (!injectEvent.isCancelled()) {
            if (tinyProtocolMode) {
                assert tinyProtocol != null;
                tinyProtocol.injectPlayerAsync(player);
            } else {
                assert nettyProtocol != null;
                nettyProtocol.injectPlayerAsync(player);
            }
        }
    }

    public void ejectPlayerSync(Player player) {
        PlayerEjectEvent ejectEvent = new PlayerEjectEvent(player, false);
        PacketEvents.getAPI().getEventManager().callEvent(ejectEvent);
        if (!ejectEvent.isCancelled()) {
            keepAliveMap.remove(player.getUniqueId());
            if (tinyProtocolMode) {
                tinyProtocol.ejectPlayer(player);
            } else {
                nettyProtocol.ejectPlayer(player);
            }
        }
    }

    public void ejectPlayerAsync(Player player) {
        PlayerEjectEvent ejectEvent = new PlayerEjectEvent(player, true);
        PacketEvents.getAPI().getEventManager().callEvent(ejectEvent);
        if (!ejectEvent.isCancelled()) {
            keepAliveMap.remove(player.getUniqueId());
            if (tinyProtocolMode) {
                tinyProtocol.ejectPlayerAsync(player);
            } else {
                nettyProtocol.ejectPlayerAsync(player);
            }
        }
    }

    public void ejectChannel(Object channel) {
        if (!PacketEvents.getSettings().shouldEjectAsync()) {
            ejectChannelSync(channel);
        } else {
            ejectChannelAsync(channel);
        }
    }

    public void ejectChannelSync(Object channel) {
        if (tinyProtocolMode) {
            tinyProtocol.ejectChannelSync(channel);
        } else {
            nettyProtocol.ejectChannelSync(channel);
        }
    }

    public void ejectChannelAsync(Object channel) {
        if (tinyProtocolMode) {
            tinyProtocol.ejectChannelAsync(channel);
        } else {
            nettyProtocol.ejectChannelAsync(channel);
        }
    }

    public void sendPacket(Object channel, Object packet) {
        if (tinyProtocolMode) {
            tinyProtocol.sendPacket(channel, packet);
        } else {
            nettyProtocol.sendPacket(channel, packet);
        }
    }

    public String getNettyHandlerName() {
        return "pe-" + plugin.getName() + "-" + handlerNumber;
    }

    public Object read(Player player, Object channel, Object packet) {
        if (player == null) {
            String simpleClassName = ClassUtil.getClassSimpleName(packet.getClass());
            //Status packet
            if (simpleClassName.startsWith("PacketS")) {
                final PacketStatusEvent packetStatusEvent = new PacketStatusEvent(channel, packet);
                PacketEvents.getAPI().getEventManager().callEvent(packetStatusEvent);
                interceptStatus(packetStatusEvent);
                if (packetStatusEvent.isCancelled()) {
                    packet = null;
                }
            } else {
                //Login packet
                final PacketLoginEvent packetLoginEvent = new PacketLoginEvent(channel, packet);
                PacketEvents.getAPI().getEventManager().callEvent(packetLoginEvent);
                interceptLogin(packetLoginEvent);
                if (packetLoginEvent.isCancelled()) {
                    packet = null;
                }
            }
        } else {
            final PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(player, packet);
            PacketEvents.getAPI().getEventManager().callEvent(packetReceiveEvent);
            interceptRead(packetReceiveEvent);
            if (packetReceiveEvent.isCancelled()) {
                packet = null;
            }
        }
        return packet;
    }

    public Object write(Player player, Object channel, Object packet) {
        if (player == null) {
            String simpleClassName = ClassUtil.getClassSimpleName(packet.getClass());
            //Status packet
            if (simpleClassName.startsWith("PacketS")) {
                final PacketStatusEvent packetStatusEvent = new PacketStatusEvent(channel, packet);
                PacketEvents.getAPI().getEventManager().callEvent(packetStatusEvent);
                interceptStatus(packetStatusEvent);
                if (packetStatusEvent.isCancelled()) {
                    packet = null;
                }
            }
            //Login packet
            else {
                final PacketLoginEvent packetLoginEvent = new PacketLoginEvent(channel, packet);
                PacketEvents.getAPI().getEventManager().callEvent(packetLoginEvent);
                interceptLogin(packetLoginEvent);
                if (packetLoginEvent.isCancelled()) {
                    packet = null;
                }
            }
        } else {
            final PacketSendEvent packetSendEvent = new PacketSendEvent(player, packet);
            PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent);
            interceptWrite(packetSendEvent);
            if (packetSendEvent.isCancelled()) {
                packet = null;
            }
        }
        return packet;
    }

    public void postRead(Player player, Object packet) {
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

    public void postWrite(Player player, Object packet) {
        switch (ClassUtil.getClassSimpleName(packet.getClass())) {
            case "PacketLoginOutDisconnect":
            case "PacketLoginOutEncryptionBegin":
            case "PacketLoginOutSetCompression":
            case "PacketLoginOutSuccess":
            case "PacketStatusOutPong":
            case "PacketStatusOutServerInfo":
                break;
            default:
                PacketEvents.getAPI().getEventManager().callEvent(new PostPacketSendEvent(player, packet));
                break;
        }
    }


    private void interceptRead(PacketReceiveEvent event) {
        if (event.getPacketId() == PacketType.Client.KEEP_ALIVE) {
            UUID uuid = event.getPlayer().getUniqueId();
            long timestamp = keepAliveMap.getOrDefault(uuid, event.getTimestamp());
            long currentTime = event.getTimestamp();
            long ping = currentTime - timestamp;
            long smoothedPing = (PacketEvents.getAPI().getPlayerUtils().getSmoothedPing(event.getPlayer()) * 3 + ping) / 4;
            PacketEvents.getAPI().getPlayerUtils().playerPingMap.put(uuid, (short) ping);
            PacketEvents.getAPI().getPlayerUtils().playerSmoothedPingMap.put(uuid, (short) smoothedPing);
        }
    }

    private void interceptWrite(PacketSendEvent event) {
        if (event.getPacketId() == PacketType.Server.KEEP_ALIVE) {
            keepAliveMap.put(event.getPlayer().getUniqueId(), event.getTimestamp());
        }
    }

    private void interceptLogin(PacketLoginEvent event) {
        if (event.getPacketId() == PacketType.Login.HANDSHAKE &&
        PacketEvents.getAPI().getServerUtils().getVersion() != ServerVersion.v_1_7_10) {
                WrappedPacketLoginHandshake handshake = new WrappedPacketLoginHandshake(event.getNMSPacket());
                int protocolVersion = handshake.getProtocolVersion();
                ClientVersion version = ClientVersion.getClientVersion(protocolVersion);
                PacketEvents.getAPI().getPlayerUtils().clientVersionsMap.put(event.getChannel(), version);
        }
    }

    private void interceptStatus(PacketStatusEvent event) {

    }

}
