package io.github.retrooper.packetevents.utils.versionlookup.protocollib;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ProtocolLibVersionLookupUtils {
    public static boolean isAvailable() {
        return Bukkit.getPluginManager().isPluginEnabled("ProtocolLib");
    }

    public static int getProtocolVersion(Player player) {
        return ProtocolLibrary.getProtocolManager().getProtocolVersion(player);
    }
}
