package me.purplex.packetevents.utils.playerprotocolversion;

import org.bukkit.entity.Player;

import java.util.*;

public class PlayerVersionManager {
    private static HashMap<UUID, Integer> protocolVersion = new HashMap<UUID, Integer>();

    public static int getPlayerProtocolVersion(final Player player) {
        return protocolVersion.get(player.getUniqueId());
    }

    public static void setPlayerProtocolVersion(final Player player, final int version) {
        protocolVersion.put(player.getUniqueId(), version);
    }

    public static void clearPlayer(final Player player) {
        protocolVersion.remove(player.getUniqueId());
    }


    public static int getPlayerProtocolVersion(final UUID uuid) {
        return protocolVersion.get(uuid);
    }

    public static void setPlayerProtocolVersion(final UUID uuid, final int version) {
        protocolVersion.put(uuid, version);
    }

    public static void clearPlayer(final UUID uuid) {
        protocolVersion.remove(uuid);
    }
}
