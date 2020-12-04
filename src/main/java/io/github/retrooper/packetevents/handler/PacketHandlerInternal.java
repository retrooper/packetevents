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

package io.github.retrooper.packetevents.handler;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.*;
import io.github.retrooper.packetevents.injector.earlyinjector.EarlyChannelInjector;
import io.github.retrooper.packetevents.injector.lateinjector.LateChannelInjector;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.login.in.handshake.WrappedPacketLoginInHandshake;
import io.github.retrooper.packetevents.packetwrappers.login.in.start.WrappedPacketLoginInStart;
import io.github.retrooper.packetevents.utils.gameprofile.WrappedGameProfile;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PacketHandlerInternal {
    public final EarlyChannelInjector earlyInjector;
    public final LateChannelInjector lateInjector;
    private final Plugin plugin;
    private final boolean earlyInjectMode;
    private final HashMap<UUID, Long> keepAliveMap = new HashMap<>();
    private final Map<String, Object> channelMap = new ConcurrentHashMap<>();
    private final Map<Object, Boolean> firstPacketCache;

    public PacketHandlerInternal(Plugin plugin, boolean earlyInjectMode) {
        this.plugin = plugin;
        this.earlyInjectMode = earlyInjectMode;
        if (earlyInjectMode) {
            earlyInjector = new EarlyChannelInjector(plugin);
            earlyInjector.startup();
            lateInjector = null;
        } else {
            lateInjector = new LateChannelInjector(plugin);
            earlyInjector = null;
        }
        if (PacketEvents.get().getSettings().getPacketHandlingThreadCount() == 1) {
            firstPacketCache = new HashMap<>();
        } else {
            firstPacketCache = new ConcurrentHashMap<>();
        }
    }

    public Object getChannel(String name) {
        Object channel = channelMap.get(name);
        if (channel == null) {
            channel = NMSUtils.getChannel(Bukkit.getPlayer(name));
            channelMap.put(name, channel);
            return channel;
        }
        return channel;
    }

    public void injectPlayer(Player player) {
        if (PacketEvents.get().getSettings().shouldInjectAsync()) {
            injectPlayerAsync(player);
        } else {
            injectPlayerSync(player);
        }
    }

    public void ejectPlayer(Player player) {
        if (PacketEvents.get().getSettings().shouldEjectAsync()) {
            ejectPlayerAsync(player);
        } else {
            ejectPlayerSync(player);
        }
    }

    public void injectPlayerSync(Player player) {
        PlayerInjectEvent injectEvent = new PlayerInjectEvent(player, false);
        PacketEvents.get().getEventManager().callEvent(injectEvent);
        if (!injectEvent.isCancelled()) {
            if (earlyInjectMode) {
                earlyInjector.injectPlayerSync(player);
            } else {
                lateInjector.injectPlayerSync(player);
            }
        }
    }

    public void injectPlayerAsync(Player player) {
        PlayerInjectEvent injectEvent = new PlayerInjectEvent(player, true);
        PacketEvents.get().getEventManager().callEvent(injectEvent);
        if (!injectEvent.isCancelled()) {
            if (earlyInjectMode) {
                Objects.requireNonNull(earlyInjector).injectPlayerAsync(player);
            } else {
                Objects.requireNonNull(lateInjector).injectPlayerAsync(player);
            }
        }
    }

    public void ejectPlayerSync(Player player) {
        PlayerEjectEvent ejectEvent = new PlayerEjectEvent(player, false);
        PacketEvents.get().getEventManager().callEvent(ejectEvent);
        if (!ejectEvent.isCancelled()) {
            keepAliveMap.remove(player.getUniqueId());
            if (earlyInjectMode) {
                Objects.requireNonNull(earlyInjector).ejectPlayerSync(player);
            } else {
                Objects.requireNonNull(lateInjector).ejectPlayerSync(player);
            }
            firstPacketCache.remove(getChannel(player.getName()));
            channelMap.remove(player.getName());
        }
    }

    public void ejectPlayerAsync(Player player) {
        PlayerEjectEvent ejectEvent = new PlayerEjectEvent(player, true);
        PacketEvents.get().getEventManager().callEvent(ejectEvent);
        if (!ejectEvent.isCancelled()) {
            keepAliveMap.remove(player.getUniqueId());
            if (earlyInjectMode) {
                Objects.requireNonNull(earlyInjector).ejectPlayerAsync(player);
            } else {
                Objects.requireNonNull(lateInjector).ejectPlayerAsync(player);
            }
            firstPacketCache.remove(getChannel(player.getName()));
            channelMap.remove(player.getName());
        }
    }

    public void sendPacket(Object channel, Object packet) {
        if (earlyInjectMode) {
            earlyInjector.sendPacket(channel, packet);
        } else {
            lateInjector.sendPacket(channel, packet);
        }
    }

    public Object read(Player player, Object channel, Object packet) {
        if (player == null) {
            String simpleClassName = ClassUtil.getClassSimpleName(packet.getClass());
            //Status packet
            if (simpleClassName.startsWith("PacketS")) {
                final PacketStatusEvent packetStatusEvent = new PacketStatusEvent(channel, packet);
                PacketEvents.get().getEventManager().callEvent(packetStatusEvent);
                interceptStatus(packetStatusEvent);
                if (packetStatusEvent.isCancelled()) {
                    packet = null;
                }
            } else {
                //Login packet
                final PacketLoginEvent packetLoginEvent = new PacketLoginEvent(channel, packet);
                PacketEvents.get().getEventManager().callEvent(packetLoginEvent);
                interceptLogin(packetLoginEvent);
                if (packetLoginEvent.isCancelled()) {
                    packet = null;
                } else {
                    //Cache the channel
                    if (packetLoginEvent.getPacketId() == PacketType.Login.Client.START) {
                        WrappedPacketLoginInStart startWrapper = new WrappedPacketLoginInStart(packetLoginEvent.getNMSPacket());
                        WrappedGameProfile gameProfile = startWrapper.getGameProfile();
                        channelMap.put(gameProfile.name, channel);
                    }
                }
            }
        } else {
            Boolean firstPacket = firstPacketCache.get(channel);
            if (firstPacket == null) {
                firstPacketCache.put(channel, true);
                PacketEvents.get().getEventManager().callEvent(new PostPlayerInjectEvent(player));
            }
            final PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(player, packet);
            PacketEvents.get().getEventManager().callEvent(packetReceiveEvent);
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
                PacketEvents.get().getEventManager().callEvent(packetStatusEvent);
                interceptStatus(packetStatusEvent);
                if (packetStatusEvent.isCancelled()) {
                    packet = null;
                }
            }
            //Login packet
            else {
                final PacketLoginEvent packetLoginEvent = new PacketLoginEvent(channel, packet);
                PacketEvents.get().getEventManager().callEvent(packetLoginEvent);
                interceptLogin(packetLoginEvent);
                if (packetLoginEvent.isCancelled()) {
                    packet = null;
                }
            }
        } else {
            Boolean firstPacket = firstPacketCache.get(channel);
            if (firstPacket == null) {
                firstPacketCache.put(channel, true);
                PacketEvents.get().getEventManager().callEvent(new PostPlayerInjectEvent(player));
            }
            final PacketSendEvent packetSendEvent = new PacketSendEvent(player, packet);
            PacketEvents.get().getEventManager().callEvent(packetSendEvent);
            interceptWrite(packetSendEvent);
            if (packetSendEvent.isCancelled()) {
                packet = null;
            }
        }
        return packet;
    }

    public void postRead(Player player, Object packet) {
        if (player != null) {
            PostPacketReceiveEvent event = new PostPacketReceiveEvent(player, packet);
            PacketEvents.get().getEventManager().callEvent(event);
            interceptPostRead(event);
        }
    }

    public void postWrite(Player player, Object packet) {
        if (player != null) {
            PostPacketSendEvent event = new PostPacketSendEvent(player, packet);
            PacketEvents.get().getEventManager().callEvent(event);
            interceptPostSend(event);
        }
    }


    private void interceptRead(PacketReceiveEvent event) {
        if (event.getPacketId() == PacketType.Play.Client.KEEP_ALIVE) {
            UUID uuid = event.getPlayer().getUniqueId();
            long timestamp = keepAliveMap.getOrDefault(uuid, event.getTimestamp());
            long currentTime = event.getTimestamp();
            long ping = currentTime - timestamp;
            long smoothedPing = (PacketEvents.get().getPlayerUtils().getSmoothedPing(event.getPlayer()) * 3 + ping) / 4;
            PacketEvents.get().getPlayerUtils().playerPingMap.put(uuid, (short) ping);
            PacketEvents.get().getPlayerUtils().playerSmoothedPingMap.put(uuid, (short) smoothedPing);
        }
    }

    private void interceptWrite(PacketSendEvent event) {

    }

    private void interceptLogin(PacketLoginEvent event) {
        if (event.getPacketId() == PacketType.Login.Client.HANDSHAKE
                && PacketEvents.get().getServerUtils().getVersion() != ServerVersion.v_1_7_10
                && !PacketEvents.get().getServerUtils().isBungeeCordEnabled() &&
                !PacketEvents.get().getPlayerUtils().clientVersionsMap.containsKey(event.getChannel())) {
            WrappedPacketLoginInHandshake handshake = new WrappedPacketLoginInHandshake(event.getNMSPacket());
            int protocolVersion = handshake.getProtocolVersion();
            ClientVersion version = ClientVersion.getClientVersion(protocolVersion);
            PacketEvents.get().getPlayerUtils().clientVersionsMap.put(event.getChannel(), version);
        }
    }

    private void interceptStatus(PacketStatusEvent event) {

    }

    private void interceptPostRead(PostPacketReceiveEvent event) {

    }

    private void interceptPostSend(PostPacketSendEvent event) {
        if (event.getPacketId() == PacketType.Play.Server.KEEP_ALIVE) {
            keepAliveMap.put(event.getPlayer().getUniqueId(), event.getTimestamp());
        }
    }

    public void close() {
        if (earlyInjectMode) {
            earlyInjector.close();
        }
    }
}
