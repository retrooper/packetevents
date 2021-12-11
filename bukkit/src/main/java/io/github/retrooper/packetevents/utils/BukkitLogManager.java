package io.github.retrooper.packetevents.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.LogManager;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class BukkitLogManager extends LogManager {
    private final String prefixText = "§b[PacketEvents] §f";;

    @Override
    public void log(Level level, final String message) {
        String afterPrefixSuffix = "§f";
        if (level == Level.INFO) {
            afterPrefixSuffix = "§a";

        } else if (level == Level.WARNING) {
            afterPrefixSuffix = "§e";
        } else if (level == Level.SEVERE) {
            afterPrefixSuffix = "§c";
        } else if (level == Level.FINE) {
            afterPrefixSuffix = "§7";
            level = Level.INFO;
        }


        Bukkit.getConsoleSender().sendMessage(prefixText + afterPrefixSuffix + message);
    }

    @Override
    public void log(Level level, final String message, Throwable thrown) {
        String afterPrefixSuffix = "§f";
        if (level == Level.INFO) {
            afterPrefixSuffix = "§a";

        } else if (level == Level.WARNING) {
            afterPrefixSuffix = "§e";
        } else if (level == Level.SEVERE) {
            afterPrefixSuffix = "§c";
        } else if (level == Level.FINE) {
            afterPrefixSuffix = "§7";
            level = Level.INFO;
        }

        Bukkit.getConsoleSender().sendMessage(prefixText + afterPrefixSuffix + message);
        thrown.printStackTrace();
    }
}
