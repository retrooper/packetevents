package io.github.retrooper.packetevents.utils.api;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.entity.Player;

class ProtocolLibAPIAccessor {
    public static int getProtocolVersion(final Player player) {
        return ProtocolLibrary.getProtocolManager().getProtocolVersion(player);
    }
}
