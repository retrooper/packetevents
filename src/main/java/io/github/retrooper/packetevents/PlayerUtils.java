package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.enums.ClientVersion;
import io.github.retrooper.packetevents.handler.NettyPacketHandler;
import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerUtils {
    protected final HashMap<UUID, ClientVersion> clientVersionsMap = new HashMap<UUID, ClientVersion>();

    public int getPlayerPing(final Player player) {
        return NMSUtils.getPlayerPing(player);
    }

    public ClientVersion getClientVersion(final UUID uuid) {
        return clientVersionsMap.get(uuid);
    }

    public ClientVersion getClientVersion(final Player player) {
        return getClientVersion(player.getUniqueId());
    }

    public void injectPlayer(final Player player) {
        NettyPacketHandler.injectPlayer(player);
    }

    public void uninjectPlayer(final Player player) {
        NettyPacketHandler.uninjectPlayer(player);
    }

    public void sendPacket(final Player player, final Sendable sendable) {
        NMSUtils.sendSendableWrapper(player, sendable);
    }


}
