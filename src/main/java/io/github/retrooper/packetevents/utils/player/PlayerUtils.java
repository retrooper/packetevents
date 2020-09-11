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
import io.github.retrooper.packetevents.enums.ClientVersion;
import io.github.retrooper.packetevents.nettyhandler.NettyPacketHandler;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.utils.NMSUtils;
import io.github.retrooper.packetevents.utils.versionlookup.VersionLookupUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public final class PlayerUtils {

    /**
     * This map stores each player's client version.
     */
    public final HashMap<UUID, ClientVersion> clientVersionsMap = new HashMap<UUID, ClientVersion>();

    /**
     * Get the player's ping.
     * @param player
     * @return Get Player Ping
     */
    public int getPing(final Player player) {
        return NMSUtils.getPlayerPing(player);
    }

    /**
     * Get the client version by a UUID.
     * @param uuid
     * @return Get Client Version
     */
    public ClientVersion getClientVersion(final UUID uuid) {
        return clientVersionsMap.get(uuid);
    }

    /**
     * Get the client version by a player object.
     * @param player
     * @return Get Client Version
     */
    public ClientVersion getClientVersion(final Player player) {
        return clientVersionsMap.get(player.getUniqueId());
    }

    /**
     * Get the protocol version.
     * @param player
     * @return Get Protocol Version
     */
    public int getProtocolVersion(Player player) {
        return VersionLookupUtils.getProtocolVersion(player);
    }

    /**
     * Inject a player.
     *
     * (This also calls the {@link io.github.retrooper.packetevents.event.impl.PlayerInjectEvent})
     * @param player
     */
    public void injectPlayer(final Player player) {
        if (PacketEvents.getSettings().isInjectAsync()) {
            NettyPacketHandler.injectPlayerAsync(player);
        } else {
            NettyPacketHandler.injectPlayer(player);
        }
    }

    /**
     * Use {@link #ejectPlayer(Player)}
     * @param player
     */
    @Deprecated
    public void uninjectPlayer(final Player player) {
        if (PacketEvents.getSettings().isUninjectAsync()) {
            NettyPacketHandler.ejectPlayerAsync(player);
        } else {
            NettyPacketHandler.ejectPlayer(player);
        }
    }

    /**
     * Eject a player.
     * (This also calls the {@link io.github.retrooper.packetevents.event.impl.PlayerUninjectEvent})
     * @param player
     */
    public void ejectPlayer(final Player player) {
        if (PacketEvents.getSettings().isUninjectAsync()) {
            NettyPacketHandler.ejectPlayerAsync(player);
        } else {
            NettyPacketHandler.ejectPlayer(player);
        }
    }

    /**
     * Send a {@link SendableWrapper} wrapper to a player.
     * @param player
     * @param sendableWrapper
     */
    public void sendPacket(final Player player, final SendableWrapper sendableWrapper) {
        NMSUtils.sendSendableWrapper(player, sendableWrapper);
    }

    /**
     * Send a raw NMS packet to a player.
     * @param player
     * @param nmsPacket
     */
    public void sendNMSPacket(final Player player, Object nmsPacket) {
        NMSUtils.sendNMSPacket(player, nmsPacket);
    }
}