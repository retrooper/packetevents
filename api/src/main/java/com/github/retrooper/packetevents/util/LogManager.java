package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.regex.Pattern;

public class LogManager {

    protected static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + '\u00A7' + "[0-9A-FK-ORX]");

    protected void log(Level level, @Nullable NamedTextColor color, String message) {
        //First we must strip away the color codes that might be in this message
        message = STRIP_COLOR_PATTERN.matcher(message).replaceAll("");
        PacketEvents.getAPI().getLogger().log(level, color != null ? (color.toString()) : "" + message);
    }

    public void info(String message) {
        log(Level.INFO, null, message);
    }

    public void warn(final String message) {
        log(Level.WARNING, null, message);
    }

    public void severe(String message) {
        log(Level.SEVERE, null, message);
    }

    public void debug(String message) {
        if (this.isDebug()) {
            log(Level.FINE, null, message);
        }
    }

    public boolean isDebug() {
        return PacketEvents.getAPI().getSettings().isDebugEnabled();
    }
}
