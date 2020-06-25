package io.github.retrooper.packetevents.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class ViaUtils {
    public static int getProtocolVersion(final Player player) {
        return ViaAPIAccessor.getProtocolVersion(player);
    }

    public static boolean isViaAPIAvaialable() {
        return Bukkit.getPluginManager().getPlugin("ViaVersion") != null;
    }
}
