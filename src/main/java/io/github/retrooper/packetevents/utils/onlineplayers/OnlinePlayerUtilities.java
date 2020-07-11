package io.github.retrooper.packetevents.utils.onlineplayers;

import io.github.retrooper.packetevents.enums.ServerVersion;
import org.bukkit.entity.Player;

public class OnlinePlayerUtilities {
    private static final ServerVersion version = ServerVersion.getVersion();

    public static Player[] getOnlinePlayers() {
        if (version.isLowerThan(ServerVersion.v_1_9)) {
            return OnlinePlayerUtils_8.getOnlinePlayers();
        } else {
            return OnlinePlayerUtils_9.getOnlinePlayers();
        }
    }
}
