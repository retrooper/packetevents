package io.github.retrooper.packetevents.utils.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class ViaUtils {
    public static int getProtocolVersion(final Player player) {
        return ViaAPIAccessor.getProtocolVersion(player);
    }

    public static boolean isAvailable() {
        return Bukkit.getPluginManager().isPluginEnabled("ViaVersion");
    }
}
