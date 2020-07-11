package io.github.retrooper.packetevents.utils.onlineplayers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class OnlinePlayerUtils_8 {
    public static Player[] getOnlinePlayers() {

        return Bukkit.getServer().getOnlinePlayers().toArray(new Player[0]);
    }
}
