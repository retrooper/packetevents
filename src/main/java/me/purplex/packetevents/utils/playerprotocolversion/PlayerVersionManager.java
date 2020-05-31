package me.purplex.packetevents.utils.playerprotocolversion;

import org.bukkit.entity.Player;

import java.util.*;

public class PlayerVersionManager {
    private static HashMap<UUID, Integer> protocolVersions = new HashMap<UUID, Integer>();


    public static void setPlayerProtocolVersion(final Player player, final int version) {
        protocolVersions.put(player.getUniqueId(), version);
    }

    public static void clearPlayerProtocolVersion(final Player player) {
        protocolVersions.remove(player.getUniqueId());
    }

    public static void clearAllProtocolVersions() {
        protocolVersions = new HashMap<>();
    }

    public static int getPlayerProtocolVersion(final Player player) {
        return protocolVersions.get(player.getUniqueId());
    }

}
