package io.github.retrooper.packetevents.utils.onlineplayers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OnlinePlayerUtils_9 {
    public static Player[] getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().toArray(new Player[0]);
    }
}
