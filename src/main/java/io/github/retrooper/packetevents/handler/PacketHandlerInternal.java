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
    private final boolean earlyInjectMode;
    public final HashMap<UUID, Long> keepAliveMap = new HashMap<>();
    public final Map<String, Object> channelMap = new ConcurrentHashMap<>();
    public final Map<Object, Long> channelTimePassed = new ConcurrentHashMap<>();
    public volatile long minimumPostPlayerInjectDeltaTime = 0L;
    public PacketHandlerInternal(Plugin plugin, boolean earlyInjectMode) {
        this.earlyInjectMode = earlyInjectMode;
        if (earlyInjectMode) {
            earlyInjector = new EarlyChannelInjector(plugin);
            earlyInjector.startup();
            lateInjector = null;
        } else {
            lateInjector = new LateChannelInjector(plugin);
            earlyInjector = null;
        }
    }

    public Object getChannel(String name) {
        Object channel = channelMap.get(name);
        if (channel == null) {
            Player player = Bukkit.getPlayer(name);
            channel = NMSUtils.getChannel(player);
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
        Object channel = PacketEvents.get().packetHandlerInternal.getChannel(player.getName());
        PlayerInjectEvent injectEvent = new PlayerInjectEvent(player, channel,false);
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
        Object channel = PacketEvents.get().packetHandlerInternal.getChannel(player.getName());
        PlayerInjectEvent injectEvent = new PlayerInjectEvent(player, channel, true);
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
            if (earlyInjectMode) {
                Objects.requireNonNull(earlyInjector).ejectPlayerSync(player);
            } else {
                Objects.requireNonNull(lateInjector).ejectPlayerSync(player);
            }
            keepAliveMap.remove(player.getUniqueId());
            channelTimePassed.remove(getChannel(player.getName()));
            channelMap.remove(player.getName());
        }
    }

    public void ejectPlayerAsync(Player player) {
        PlayerEjectEvent ejectEvent = new PlayerEjectEvent(player, true);
        PacketEvents.get().getEventManager().callEvent(ejectEvent);
        if (!ejectEvent.isCancelled()) {
            if (earlyInjectMode) {
                Objects.requireNonNull(earlyInjector).ejectPlayerAsync(player);
            } else {
                Objects.requireNonNull(lateInjector).ejectPlayerAsync(player);
            }
        }
    }

    public void sendPacket(Object channel, Object packet) {
        if (earlyInjectMode) {
            earlyInjector.sendPacket(channel, packet);
        } else {
            lateInjector.sendPacket(channel, packet);
        }
    }

    public Object[] read(Player player, Object channel, Object packet) {
        final byte protocolState;
        if (player == null) {
            String simpleClassName = ClassUtil.getClassSimpleName(packet.getClass());
            //Status packet
            if (simpleClassName.startsWith("PacketS")) {
                protocolState = 0;
                final PacketStatusReceiveEvent event = new PacketStatusReceiveEvent(channel, packet);
                PacketEvents.get().getEventManager().callEvent(event);
                packet = event.getNMSPacket();
                interceptStatusReceive(event);
                if (event.isCancelled()) {
                    packet = null;
                }
            } else {
                protocolState = 1;
                //Login packet
                final PacketLoginReceiveEvent event = new PacketLoginReceiveEvent(channel, packet);
                PacketEvents.get().getEventManager().callEvent(event);
                packet = event.getNMSPacket();
                interceptLoginReceive(event);
                if (event.isCancelled()) {
                    packet = null;
                } else {
                    //Cache the channel
                    if (event.getPacketId() == PacketType.Login.Client.START) {
                        WrappedPacketLoginInStart startWrapper = new WrappedPacketLoginInStart(event.getNMSPacket());
                        WrappedGameProfile gameProfile = startWrapper.getGameProfile();
                        channelMap.put(gameProfile.name, channel);
                    }
                }
            }
        } else {
            Long minTicksPassed = channelTimePassed.get(channel);
            if(minTicksPassed != null && minTicksPassed != -1L) {
                long deltaTime = System.currentTimeMillis() - minTicksPassed;
                if(deltaTime >= minimumPostPlayerInjectDeltaTime) {
                    PacketEvents.get().getEventManager().callEvent(new PostPlayerInjectEvent(player));
                    channelTimePassed.put(channel, -1L);
                }
            }
            protocolState = 2;
            final PacketPlayReceiveEvent event = new PacketPlayReceiveEvent(player, channel, packet);
            PacketEvents.get().getEventManager().callEvent(event);
            packet = event.getNMSPacket();
            interceptRead(event);
            if (event.isCancelled()) {
                packet = null;
            }
        }
        return new Object[]{packet, protocolState};
    }

    public Object[] write(Player player, Object channel, Object packet) {
        final byte protocolState;
        if (player == null) {
            String simpleClassName = ClassUtil.getClassSimpleName(packet.getClass());
            //Status packet
            if (simpleClassName.startsWith("PacketS")) {
                protocolState = 0;
                final PacketStatusSendEvent event = new PacketStatusSendEvent(channel, packet);
                PacketEvents.get().getEventManager().callEvent(event);
                packet = event.getNMSPacket();
                interceptStatusSend(event);
                if (event.isCancelled()) {
                    packet = null;
                }
            }
            //Login packet
            else {
                protocolState = 1;
                final PacketLoginSendEvent event = new PacketLoginSendEvent(channel, packet);
                PacketEvents.get().getEventManager().callEvent(event);
                packet = event.getNMSPacket();
                interceptLoginSend(event);
                if (event.isCancelled()) {
                    packet = null;
                }
            }
        } else {
            Long minTicksPassed = channelTimePassed.get(channel);
            if(minTicksPassed != null && minTicksPassed != -1L) {
                long deltaTime = System.currentTimeMillis() - minTicksPassed;
                if(deltaTime >= minimumPostPlayerInjectDeltaTime) {
                    PacketEvents.get().getEventManager().callEvent(new PostPlayerInjectEvent(player));
                    channelTimePassed.put(channel, -1L);
                }
            }
            protocolState = 2;
            final PacketPlaySendEvent event = new PacketPlaySendEvent(player, channel, packet);
            PacketEvents.get().getEventManager().callEvent(event);
            packet = event.getNMSPacket();
            interceptWrite(event);
            if (event.isCancelled()) {
                packet = null;
            }
        }
        return new Object[] {packet, protocolState};
    }

    public void postRead(Player player, Object channel, Object packet) {
        if (player != null) {
            PostPacketPlayReceiveEvent event = new PostPacketPlayReceiveEvent(player, channel, packet);
            PacketEvents.get().getEventManager().callEvent(event);
            interceptPostPlayReceive(event);
        }
    }

    public void postWrite(Player player, Object channel, Object packet) {
        if (player != null) {
            PostPacketPlaySendEvent event = new PostPacketPlaySendEvent(player, channel, packet);
            PacketEvents.get().getEventManager().callEvent(event);
            interceptPostPlaySend(event);
        }
    }


    private void interceptRead(PacketPlayReceiveEvent event) {
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

    private void interceptWrite(PacketPlaySendEvent event) {

    }

    private void interceptLoginReceive(PacketLoginReceiveEvent event) {
        if (event.getPacketId() == PacketType.Login.Client.HANDSHAKE
                && PacketEvents.get().getServerUtils().getVersion() != ServerVersion.v_1_7_10
                && !PacketEvents.get().getServerUtils().isBungeeCordEnabled() &&
                !PacketEvents.get().getPlayerUtils().clientVersionsMap.containsKey(event.getSocketAddress())) {
            WrappedPacketLoginInHandshake handshake = new WrappedPacketLoginInHandshake(event.getNMSPacket());
            int protocolVersion = handshake.getProtocolVersion();
            ClientVersion version = ClientVersion.getClientVersion(protocolVersion);
            PacketEvents.get().getPlayerUtils().clientVersionsMap.put(event.getSocketAddress(), version);
        }
    }

    private void interceptLoginSend(PacketLoginSendEvent event) {

    }

    private void interceptStatusReceive(PacketStatusReceiveEvent event) {

    }

    private void interceptStatusSend(PacketStatusSendEvent event) {

    }

    private void interceptPostPlayReceive(PostPacketPlayReceiveEvent event) {

    }

    private void interceptPostPlaySend(PostPacketPlaySendEvent event) {
        if (event.getPacketId() == PacketType.Play.Server.KEEP_ALIVE) {
            if (event.getPlayer() != null) {
                keepAliveMap.put(event.getPlayer().getUniqueId(), event.getTimestamp());
            }
        }
    }

    public void close() {
        if (earlyInjectMode) {
            earlyInjector.close();
        }
    }

    public void closeAsync() {
        if(earlyInjectMode) {
            earlyInjector.closeAsync();
        }
    }
}
