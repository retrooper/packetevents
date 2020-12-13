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
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
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

/**
 * Internal manager of everything related to packets.
 * For example injecting, channel caching, internal processing, packet sending....
 * This class is only meant to be used internally.
 * This could end up being refactored or renamed without a deprecation as it is only meant to be used internally.
 * @see <a href="http://netty.io">http://netty.io</a>
 * @author retrooper
 * @since 1.7.9
 */
public class PacketHandlerInternal {
    //TODO document all classes below this package ordered alphabetically
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

    /**
     * Get a player's netty channel object with their name.
     * This netty channel object is cached in a {@link ConcurrentHashMap} as the value and
     * the name is the key.
     * We cache the name and the channel quite early infact,
     * once we receive the {@link PacketType.Login.Client#START} packet(which contains the game profile)
     * and the game profile contains the player name.
     * The wrapper for that packet is {@link WrappedPacketLoginInStart}.
     * If PacketEvents couldn't cache(when you have {@link PacketEventsSettings#shouldInjectEarly()} set to false),
     * PacketEvents will use some reflection to see if CraftBukkit has the netty channel.
     * If you access this before the START login packet was sent, you will for sure experience issues.
     * @see <a href="https://wiki.vg/Protocol#Login_Start">https://wiki.vg/Protocol#Login_Start</a>
     * @see Player#getName()
     * @param name Target player name.
     * @return Netty channel of a player by their name.
     */
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

    /**
     * Inject a player.
     * Executed synchronously or asynchronously depending on what you have the associated setting set to.
     * Injecting a player is basically pairing the cached netty channel
     * {@link #getChannel(String)} to a bukkit player object.
     * Bukkit initializes the {@link Player} object some time after
     * we inject the netty channel, so we need to pair the two.
     * PacketEvents already injects a player when the bukkit
     * {@link org.bukkit.event.player.PlayerJoinEvent} or {@link org.bukkit.event.player.PlayerLoginEvent}
     * is called, depending on your {@link PacketEventsSettings#shouldInjectEarly()} setting is set to.
     * @param player Target bukkit player.
     */
    public void injectPlayer(Player player) {
        if (PacketEvents.get().getSettings().shouldInjectAsync()) {
            injectPlayerAsync(player);
        } else {
            injectPlayerSync(player);
        }
    }

    /**
     * Eject a player.
     * Executed synchronously or asynchronously depending on what you have the associated setting set to.
     * Ejecting a player is basically unpairing the cached netty channel
     * {@link #getChannel(String)} from the bukkit player.
     * Do this if you want to stop listening to a user's packets.
     * PacketEvents already ejects a player when the bukkit
     * {@link org.bukkit.event.player.PlayerQuitEvent} is called.
     * @param player
     */
    public void ejectPlayer(Player player) {
        if (PacketEvents.get().getSettings().shouldEjectAsync()) {
            ejectPlayerAsync(player);
        } else {
            ejectPlayerSync(player);
        }
    }

    /**
     * Synchronously inject a player.
     * @see #injectPlayer(Player)
     * @param player Target player.
     */
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

    /**
     * Asynchronously inject a player.
     * @see #injectPlayer(Player)
     * @param player Target player.
     */
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

    /**
     * Synchronously eject a player.
     * @see #ejectPlayer(Player) 
     * @param player Target player.
     */
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

    /**
     * Asynchronously eject a player.
     * @see #ejectPlayer(Player)
     * @param player Target player.
     */
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

    /**
     * Write and flush a packet to a netty channel.
     * This netty channel is an object as 1.7.10 and 1.8 and above
     * have different netty package locations.
     * @param channel Netty channel.
     * @param packet NMS Packet.
     */
    public void sendPacket(Object channel, Object packet) {
        if (earlyInjectMode) {
            earlyInjector.sendPacket(channel, packet);
        } else {
            lateInjector.sendPacket(channel, packet);
        }
    }

    /**
     * Make PacketEvents process an incoming packet.
     * @param player Packet sender.
     * @param channel Packet sender's netty channel.
     * @param packet NMS Packet.
     * @return Object array, first index is the NMS Packet and the second index is the protocol state(STATUS=0,LOGIN=1,PLAY=2).
     */
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
                    channelTimePassed.put(channel, -1L);
                    PacketEvents.get().getEventManager().callEvent(new PostPlayerInjectEvent(player));
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

    /**
     * Make PacketEvents process an outgoing packet.
     * @param player Packet receiver.
     * @param channel Packet receiver's netty channel.
     * @param packet NMS Packet.
     * @return Object array, first index is the NMS Packet and the second index is the protocol state(STATUS=0,LOGIN=1,PLAY=2).
     */
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
                if (deltaTime >= minimumPostPlayerInjectDeltaTime) {
                    channelTimePassed.put(channel, -1L);
                    PacketEvents.get().getEventManager().callEvent(new PostPlayerInjectEvent(player));
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

    /**
     * Make PacketEvents process an incoming PLAY packet after minecraft has processed it.
     * As minecraft has already processed the packet, we cannot cancel the action, nor the event.
     * @param player Packet sender.
     * @param channel Netty channel of the packet sender.
     * @param packet NMS Packet.
     */
    public void postRead(Player player, Object channel, Object packet) {
        if (player != null) {
            PostPacketPlayReceiveEvent event = new PostPacketPlayReceiveEvent(player, channel, packet);
            PacketEvents.get().getEventManager().callEvent(event);
            interceptPostPlayReceive(event);
        }
    }

    /**
     * Make PacketEvents process an outgoing PLAY packet after minecraft has already sent the packet.
     * This doesn't necessarily mean the client already received the packet,
     * but the server has sent it for sure by this time.
     * As minecraft has already processed the packet, we cannot cancel the action, nor the event.
     * @param player Packet receiver.
     * @param channel Netty channel of the packet receiver.
     * @param packet NMS Packet.
     */
    public void postWrite(Player player, Object channel, Object packet) {
        if (player != null) {
            PostPacketPlaySendEvent event = new PostPacketPlaySendEvent(player, channel, packet);
            PacketEvents.get().getEventManager().callEvent(event);
            interceptPostPlaySend(event);
        }
    }

    /**
     * Internal processing of an incoming PLAY packet.
     * @param event PLAY server-bound packet event.
     */
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

    /**
     * Internal processing of an outgoing PLAY packet.
     * @param event PLAY client-bound packet event.
     */
    private void interceptWrite(PacketPlaySendEvent event) {

    }

    /**
     * Internal processing of an incoming LOGIN packet.
     * @param event LOGIN server-bound packet event.
     */
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

    /**
     * Internal processing of an outgoing LOGIN packet.
     * @param event client-bound LOGIN packet event.
     */
    private void interceptLoginSend(PacketLoginSendEvent event) {

    }

    /**
     * Internal processing of an incoming STATUS packet.
     * @param event server-bound STATUS packet event.
     */
    private void interceptStatusReceive(PacketStatusReceiveEvent event) {

    }

    /**
     * Internal processing of an outgoing STATUS packet.
     * @param event client-bound STATUS packet event.
     */
    private void interceptStatusSend(PacketStatusSendEvent event) {

    }

    /**
     * Internal processing of a packet that has already been processed by minecraft.
     * @param event post server-bound play packet event.
     */
    private void interceptPostPlayReceive(PostPacketPlayReceiveEvent event) {

    }

    /**
     * Internal processing of a packet that has already been sent to a client.
     * Doesn't necessarily mean the client has received it yet.
     * @param event post client-bound play packet event.
     */
    private void interceptPostPlaySend(PostPacketPlaySendEvent event) {
        if (event.getPacketId() == PacketType.Play.Server.KEEP_ALIVE) {
            if (event.getPlayer() != null) {
                keepAliveMap.put(event.getPlayer().getUniqueId(), event.getTimestamp());
            }
        }
    }

    /**
     * If you are using the EarlyInjector,
     * calling this close method will unregister the channel handlers it registered when the plugin enabled.
     * PacketEvents already unregisters them in the {@link PacketEvents#stop()} method.
     */
    public void close() {
        if (earlyInjectMode) {
            earlyInjector.close();
        }
    }

    /**
     * If you are using the EarlyInjector,
     * calling this close method will unregister the channel handlers it registered when the plugin enabled
     * ASYNCHRONOUSLY.
     * PacketEvents already unregisters them in the {@link PacketEvents#stop()} method.
     */
    public void closeAsync() {
        if(earlyInjectMode) {
            earlyInjector.closeAsync();
        }
    }
}
