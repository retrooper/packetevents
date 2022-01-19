package io.github.retrooper.packetevents.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.ColorUtil;
import com.github.retrooper.packetevents.util.LogManager;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class BukkitLogManager extends LogManager {
    private final String prefixText = ColorUtil.toString(NamedTextColor.AQUA) + "[packetevents] " + ColorUtil.toString(NamedTextColor.WHITE);

    @Override
    protected void log(Level level, @Nullable NamedTextColor color, String message) {
        Bukkit.getConsoleSender().sendMessage(prefixText + ColorUtil.toString(color) + message);
    }

    @Override
    public void info(String message) {
        log(Level.INFO, NamedTextColor.WHITE, message);
    }

    @Override
    public void warn(final String message) {
        log(Level.WARNING, NamedTextColor.YELLOW, message);
    }

    @Override
    public void severe(String message) {
        log(Level.SEVERE, NamedTextColor.RED, message);
    }

    @Override
    public void debug(String message) {
        if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
            log(Level.FINE, NamedTextColor.GRAY, message);
        }
    }
}
