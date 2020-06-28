package io.github.retrooper.packetevents.utils.api;

import org.bukkit.entity.Player;
import protocolsupport.api.ProtocolSupportAPI;

class ProtocolSupportAPIAccessor {
    public static int getProtocolVersion(final Player player) {
        return ProtocolSupportAPI.getProtocolVersion(player).getId();
    }
}
