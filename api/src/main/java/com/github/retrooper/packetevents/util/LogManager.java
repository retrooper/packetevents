package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.chat.Color;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class LogManager {

    protected void log(Level level, @Nullable Color color, String message) {
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
        if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
            log(Level.FINE, null, message);
        }
    }
}
