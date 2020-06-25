package io.github.retrooper.packetevents.utils;

import org.bukkit.entity.Player;
import us.myles.ViaVersion.api.Via;

import java.util.UUID;

class ViaAPIAccessor {
    public static int getProtocolVersion(final Player player) {
        return getProtocolVersion(player.getUniqueId());
    }

    public static int getProtocolVersion(final UUID uuid) {
        return Via.getAPI().getPlayerVersion(uuid);
    }
}
