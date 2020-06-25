package io.github.retrooper.packetevents.utils;

import org.bukkit.entity.Player;

public class VersionLookupUtils {
    public static int getProtocolVersion(final Player player) {
        if (ViaUtils.isViaAPIAvaialable()) {
            return ViaUtils.getProtocolVersion(player);
        } else if (ProtocolSupportUtils.isProtocolSupportAvailable()) {
            return ProtocolSupportUtils.getProtocolVersion(player);
        }
        return -1;
    }
}
