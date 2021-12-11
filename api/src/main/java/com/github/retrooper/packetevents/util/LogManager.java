package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;

import java.util.logging.Level;

public class LogManager {
    private final static String prefixText = "§b[PacketEvents] §f";




    public static void log(Level level, final String message) {
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

        if(PacketEvents.getAPI().getLogger() != null){
            PacketEvents.getAPI().getLogger().log(level, afterPrefixSuffix + message);
        }else{
            System.out.println(prefixText + afterPrefixSuffix + message);
        }
    }

    public static void log(Level level, final String message, Throwable thrown) {
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

        if(PacketEvents.getAPI().getLogger() != null){
            PacketEvents.getAPI().getLogger().log(level, afterPrefixSuffix + message, thrown);
        }else{
            System.out.println(prefixText + afterPrefixSuffix + message + thrown.getMessage());
        }
    }


    public static void info(final String message) {
        log(Level.INFO, message);
    }

    public static void warn(final String message) {
        log(Level.WARNING, message);
    }

    public static void severe(final String message) {
        log(Level.SEVERE, message);
    }

    public static void severe(final String message, final Throwable throwable) {
        log(Level.SEVERE, message, throwable);
    }

    public static void debug(final String message) {
        if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
            log(Level.FINE, message);
        }

    }
}
