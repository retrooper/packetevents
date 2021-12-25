package io.github.retrooper.packetevents.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.chat.Color;
import com.github.retrooper.packetevents.util.LogManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class BukkitLogManager extends LogManager {
    private final String prefixText = Color.CYAN + "[packetevents] " + Color.WHITE;

    @Override
    protected void log(Level level, @Nullable Color color, String message) {
        Bukkit.getConsoleSender().sendMessage(prefixText + color.toString() + message);
    }

    @Override
    public void info(String message) {
        log(Level.INFO, Color.WHITE, message);
    }

    @Override
    public void warn(final String message) {
        log(Level.WARNING, Color.YELLOW, message);
    }

    @Override
    public void severe(String message) {
        log(Level.SEVERE, Color.RED, message);
    }

    @Override
    public void debug(String message) {
        if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
            log(Level.FINE, Color.GRAY, message);
        }
    }
}
