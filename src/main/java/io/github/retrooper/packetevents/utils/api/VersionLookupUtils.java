package io.github.retrooper.packetevents.utils.api;

import org.bukkit.entity.Player;

public class VersionLookupUtils {
    /**
     * If ViaVersion is present, we get the protocol version with the ViaVersion API.
     * If ProtocolSupport is present, we get the protocol version with the ProtocolSupport API.
     * If ProtocolLib is present, we get the protocol version with the ProtocolLib API.
     * Otherwise return -1
     *
     * @param player
     * @return protocol version of the player if ViaVersion or ProtocolSupport or ProtocolLib is present. Otherwise -1
     */
    public static int getProtocolVersion(final Player player) {
        if (ViaUtils.isAvailable()) {
            return ViaUtils.getProtocolVersion(player);
        } else if (ProtocolSupportUtils.isAvailable()) {
            return ProtocolSupportUtils.getProtocolVersion(player);
        } else if (ProtocolLibUtils.isAvailable()) {
            return ProtocolLibUtils.getProtocolVersion(player);
        }
        return -1;
    }
}
