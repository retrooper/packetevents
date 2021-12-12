package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.chat.Color;

import java.util.logging.Level;

public class LogManager {

    protected void log(Level level, Color color, String message) {
        PacketEvents.getAPI().getLogger().log(level, color.toString() + message);
    }

    public void info(String message) {
        log(Level.INFO, Color.BRIGHT_GREEN, message);
    }

    public void warn(final String message) {
        log(Level.WARNING, Color.YELLOW, message);
    }

    public void severe(final String message) {
        log(Level.SEVERE, Color.RED, message);
    }

    public void debug(final String message) {
        if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
            log(Level.FINE, Color.GRAY, message);
        }
    }
}
