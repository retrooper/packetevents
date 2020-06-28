package io.github.retrooper.packetevents.utils.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ProtocolLibUtils {
    public static boolean isAvailable() {
        return Bukkit.getPluginManager().isPluginEnabled("ProtocolLib");
    }

    public static int getProtocolVersion(final Player player) {
        return ProtocolLibAPIAccessor.getProtocolVersion(player);
    }
}
