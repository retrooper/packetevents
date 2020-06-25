package io.github.retrooper.packetevents.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import protocolsupport.api.ProtocolSupportAPI;

public class ProtocolSupportUtils {
    public static boolean isProtocolSupportAvailable() {
        return Bukkit.getPluginManager().getPlugin("ProtocolSupport") != null;
    }

    public static int getProtocolVersion(final Player player) {
        return ProtocolSupportAPI.getProtocolVersion(player).getId();
    }
}
