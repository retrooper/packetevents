/*
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.utils.versionlookup;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.enums.ServerVersion;
import org.bukkit.entity.Player;

public class VersionLookupUtils {

    private static byte protocolAccessMode;

    public static void load() {
        protocolAccessMode = (byte) (ViaUtils.isAvailable() ?
                0 : ProtocolSupportUtils.isAvailable() ?
                1 : ProtocolLibUtils.isAvailable() ?
                2 : PacketEvents.getAPI().getServerUtils().getVersion() == ServerVersion.v_1_7_10 ?
                3 : -1);

    }

    /**
     * If ViaVersion is present, we get the protocol version with the ViaVersion API.
     * If ProtocolSupport is present, we get the protocol version with the ProtocolSupport API.
     * If ProtocolLib is present, we get the protocol version with the ProtocolLib API.
     * Otherwise return -1
     *
     * @param player target player
     * @return protocol version of the player if ViaVersion or ProtocolSupport or ProtocolLib is present. Otherwise -1
     */
    public static int getProtocolVersion(final Player player) {
        switch(protocolAccessMode) {
            case 0:
                return ViaUtils.getProtocolVersion(player);
            case 1:
                return ProtocolSupportUtils.getProtocolVersion(player);
            case 2:
                return ProtocolLibUtils.getProtocolVersion(player);
            case 3:
                return ProtocolVersionAccessor_v_1_7.getProtocolVersion(player);
            default:
                return -1;
        }
    }
}
