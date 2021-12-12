package io.github.retrooper.packetevents.utils;

import com.github.retrooper.packetevents.protocol.chat.Color;
import com.github.retrooper.packetevents.util.LogManager;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class BukkitLogManager extends LogManager {
    private final String prefixText = Color.CYAN + "[packetevents] " + Color.WHITE;

    @Override
    protected void log(Level level, Color color, String message) {
        Bukkit.getConsoleSender().sendMessage(prefixText + color.toString() + message);
    }
}
