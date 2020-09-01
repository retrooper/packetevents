/**
MIT License

Copyright (c) 2020 retrooper

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package io.github.retrooper.packetevents.utils.api;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.enums.ClientVersion;
import io.github.retrooper.packetevents.handler.NettyPacketHandler;
import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.utils.NMSUtils;
import io.github.retrooper.packetevents.utils.versionlookup.VersionLookupUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public final class PlayerUtils {

    private final HashMap<UUID, ClientVersion> clientVersionsMap = new HashMap<UUID, ClientVersion>();

    public int getPing(final Player player) {
        return NMSUtils.getPlayerPing(player);
    }

    public ClientVersion getClientVersion(final UUID uuid) {
        return clientVersionsMap.get(uuid);
    }

    public int getProtocolVersion(Player player) {
        return VersionLookupUtils.getProtocolVersion(player);
    }

    public ClientVersion getClientVersion(final Player player) {
        return getClientVersion(player.getUniqueId());
    }

    public void setClientVersion(final UUID uuid, final ClientVersion version) {
        clientVersionsMap.put(uuid, version);
    }

    public void setClientVersion(final Player player, final ClientVersion version) {
        setClientVersion(player.getUniqueId(), version);
    }

    public void clearClientVersion(final UUID uuid) {
        clientVersionsMap.remove(uuid);
    }

    public void clearClientVersion(final Player player) {
        clearClientVersion(player.getUniqueId());
    }

    public void injectPlayer(final Player player) {
        if (PacketEvents.getSettings().isInjectAsync()) {
            NettyPacketHandler.injectPlayerAsync(player);
        } else {
            NettyPacketHandler.injectPlayer(player);
        }
    }

    public void uninjectPlayer(final Player player) {
        if (PacketEvents.getSettings().isUninjectAsync()) {
            NettyPacketHandler.uninjectPlayerAsync(player);
        } else {
            NettyPacketHandler.uninjectPlayer(player);
        }
    }

    public void sendPacket(final Player player, final Sendable sendable) {
        NMSUtils.sendSendableWrapper(player, sendable);
    }

    public void sendNMSPacket(final Player player, Object nmsPacket) {
        NMSUtils.sendNMSPacket(player, nmsPacket);
    }
}