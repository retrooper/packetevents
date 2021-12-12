package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;

import java.util.logging.Level;

public class LogManager {

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

        PacketEvents.getAPI().getLogger().log(level, afterPrefixSuffix + message);
    }

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

        PacketEvents.getAPI().getLogger().log(level, afterPrefixSuffix + message, thrown);
    }


    public void info(final String message) {
        log(Level.INFO, message);
    }

    public void warn(final String message) {
        log(Level.WARNING, message);
    }

    public void severe(final String message) {
        log(Level.SEVERE, message);
    }

    public void severe(final String message, final Throwable throwable) {
        log(Level.SEVERE, message, throwable);
    }

    public void debug(final String message) {
        if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
            log(Level.FINE, message);
        }

    }
}
