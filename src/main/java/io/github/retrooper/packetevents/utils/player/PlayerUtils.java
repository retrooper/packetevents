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

package io.github.retrooper.packetevents.utils.player;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PlayerEjectEvent;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.UUID;

/**
 * Useful player utilities.
 *
 * @author retrooper
 * @since 1.6.8
 */
public final class PlayerUtils {
    /**
     * This is where the most recent non-smoothed player ping that PacketEvents calculates is stored.
     */
    public final HashMap<UUID, Short> playerPingMap = new HashMap<>();

    /**
     * This is where the most recent smoothed player ping that PacketEvents calculates is stored.
     * When accessing minecraft for a player's ping, minecraft will return a smoothed value.
     * Use this to receive a smoothed ping value.
     * PacketEvents smooths in the same way minecraft does.
     */
    public final HashMap<UUID, Short> playerSmoothedPingMap = new HashMap<>();

    /**
     * This map stores the client version of a player only when it has been confirmed.
     * Since some plugins like ViaBackwards take around one tick from the PlayerJoinEvent to resolve a player's
     * client version, we need to compare the version we stored from the packet and the one ViaBackwards specifies.
     * Why do we need to do this?
     * Plugins like ViaVersion modify the Handshaking packet containing the protocol version
     * to allow the client to join.
     * Plugins that listen to the handshaking packet will receive the modified protocol version (=server version).
     * So we have to go out of our way and use the API of ViaVersion or such similar plugins
     * to have support for client version resolving.
     * I can't do this for every single plugin like ViaVersion, so for now ViaVersion, ViaBackwards and ViaRewind are
     * guaranteed to work!
     * ProtocolSupport MIGHT work.
     */
    public final HashMap<InetSocketAddress, ClientVersion> clientVersionsMap = new HashMap<>();

    /**
     * This is a temporary client version.
     * This is the client version we receive from the handshaking packet.
     * This might not be the actual client version of the player since plugins like ViaVersion modify the packet to allow
     * users to join servers that aren't usually compatible with their client version.(Described in more detail above)
     * We will compare this version(received from the packet) and the one from the ViaVersion API and the one from the ProtocolSupport API.
     * ProtocolSupport compatibility might not work.
     * If ViaVersion or ProtocolSupport aren't available, we will trust this one.
     */
    public final HashMap<InetSocketAddress, ClientVersion> tempClientVersionMap = new HashMap<>();

    /**
     * Use reflection to read the ping value NMS calculates for the player.
     * NMS smooths the player ping.
     * @param player Target player.
     * @return NMS smoothed ping.
     */
    public int getNMSPing(final Player player) {
        return NMSUtils.getPlayerPing(player);
    }

    /**
     * Use the ping PacketEvents calculates for the player. (Updates every incoming Keep Alive packet)
     * @param player Target player.
     * @return Non-smoothed ping.
     */
    @Deprecated
    public short getPing(final Player player) {
        return playerPingMap.getOrDefault(player.getUniqueId(), (short) 0);
    }

    /**
     * Use the ping PacketEvents calculates and smooths in the same way NMS does (Updates every incoming Keep Alive packet)
     * @param player Target player.
     * @return Smoothed ping.
     */
    @Deprecated
    public short getSmoothedPing(final Player player) {
        return playerSmoothedPingMap.getOrDefault(player.getUniqueId(), (short) 0);
    }

    /**
     * Use the ping PacketEvents calculates for the player. (Updates every incoming Keep Alive packet)
     * @param uuid Target player UUID.
     * @return Non-smoothed ping.
     */
    public short getPing(UUID uuid) {
        return playerPingMap.getOrDefault(uuid, (short) 0);
    }

    /**
     * Use the ping PacketEvents calculates and smooths in the same way NMS does (Updates every incoming Keep Alive packet)
     * @param uuid Target player UUID.
     * @return Smoothed ping.
     */
    public short getSmoothedPing(UUID uuid) {
        return playerSmoothedPingMap.getOrDefault(uuid, (short) 0);
    }

    /**
     * Get a player's client version.
     * @see #clientVersionsMap
     * @param player Target player.
     * @return Client Version.
     */
    @Nullable
    public ClientVersion getClientVersion(final Player player) {
        return getClientVersion(player.getAddress());
    }

    /**
     * Get a player's client version by their socket address.
     * @see #clientVersionsMap
     * @param address Target player address.
     * @return Client Version.
     */
    @Nullable
    public ClientVersion getClientVersion(final InetSocketAddress address) {
        return clientVersionsMap.get(address);
    }

    /**
     * High level player injection method.
     * This method will call the {@link io.github.retrooper.packetevents.event.impl.PlayerInjectEvent}
     * event and if the event gets cancelled, this action will cancel too.
     * The injection action and event call will be async or sync depending on the PacketEvents settings.
     * Recommended to use this as this most likely won't ever change/refactor compared to using our internal utils.
     * @param player Target player.
     */
    public void injectPlayer(final Player player) {
        PacketEvents.get().packetHandlerInternal.injectPlayer(player);
    }

    /**
     * High level player ejection method.
     * This method will call the {@link io.github.retrooper.packetevents.event.impl.PlayerEjectEvent}
     * event and if the event gets cancelled, this action will cancel too.
     * The ejection action and event call will be async or sync depending on the PacketEvents settings.
     * Recommended to use this as this most likely won't ever change/refactor compared to using our internal utils.
     * @param player Target player.
     */
    public void ejectPlayer(final Player player) {
        PacketEvents.get().packetHandlerInternal.ejectPlayer(player);
    }

    /**
     * Send a client-bound(server-sided) wrapper that supports sending to a player.
     * @param player Packet receiver.
     * @param wrapper Client-bound wrapper supporting sending.
     */
    public void sendPacket(final Player player, final SendableWrapper wrapper) {
        PacketEvents.get().packetHandlerInternal.sendPacket(NMSUtils.getChannel(player), wrapper.asNMSPacket());
    }

    /**
     * Send a client-bound(server-sided) raw NMS Packet without any wrapper to a player.
     * @param player Packet receiver.
     * @param packet Client-bound raw NMS packet.
     */
    public void sendNMSPacket(final Player player, final Object packet) {
        PacketEvents.get().packetHandlerInternal.sendPacket(NMSUtils.getChannel(player), packet);
    }

    /**
     * Send a client-bound(server-sided) wrapper that supports sending to a netty channel.
     * @param channel Netty channel as object(due to package changes)
     * @param wrapper Client-bound raw NMS packet.
     */
    public void sendPacket(final Object channel, final SendableWrapper wrapper) {
        PacketEvents.get().packetHandlerInternal.sendPacket(channel, wrapper.asNMSPacket());
    }

    /**
     * Send a client-bound(server-sided) raw NMS Packet without any wrapper to a netty channel.
     * @param channel Netty channel as object(due to package changes)
     * @param packet Client-bound raw NMS packet.
     */
    public void sendNMSPacket(final Object channel, final Object packet) {
        PacketEvents.get().packetHandlerInternal.sendPacket(channel, packet);
    }
}
