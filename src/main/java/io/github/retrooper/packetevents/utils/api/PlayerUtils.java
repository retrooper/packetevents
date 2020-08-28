/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.utils.api;

import io.github.retrooper.packetevents.enums.ClientVersion;
import io.github.retrooper.packetevents.handler.NettyPacketHandler;
import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.NMSUtils;
import io.github.retrooper.packetevents.utils.versionlookup.VersionLookupUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public final class PlayerUtils {

    private final HashMap<UUID, ClientVersion> clientVersionsMap = new HashMap<UUID, ClientVersion>();
    private final PacketEventsSettings settings = new PacketEventsSettings();

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
        NettyPacketHandler.injectPlayer(player);
    }

    public void uninjectPlayer(final Player player) {
        if (settings.isUninjectAsync()) {
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