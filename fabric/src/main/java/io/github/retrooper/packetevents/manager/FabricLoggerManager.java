/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.manager;

import com.github.retrooper.packetevents.util.LogManager;
import net.kyori.adventure.text.format.NamedTextColor;
import java.util.logging.Logger;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class FabricLoggerManager extends LogManager {
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("§[0-9A-FK-ORa-fk-or]"); // Example pattern to strip Minecraft color codes

    private final Object logger;
    private final LoggerType loggerType;

    // Enum to represent the type of logger being used
    private enum LoggerType {
        SLF4J, LOG4J, JDK
    }

    private FabricLoggerManager(Object logger, LoggerType loggerType) {
        this.logger = logger;
        this.loggerType = loggerType;
    }

    @Override
    public void log(Level level, NamedTextColor color, String message) {
        // Strip color codes from the message (if any)
        String plainMessage = STRIP_COLOR_PATTERN.matcher(message).replaceAll("");

        switch (loggerType) {
            case SLF4J:
                logWithSlf4j(level, plainMessage);
                break;
            case LOG4J:
                logWithLog4j(level, plainMessage);
                break;
            case JDK:
                logWithJdk(level, plainMessage);
                break;
        }
    }

    private void logWithSlf4j(Level level, String message) {
        try {
            Object slf4jLogger = logger;
            Class<?> slf4jLoggerClass = Class.forName("org.slf4j.Logger");
            Method logMethod;
            switch (level.getName()) {
                case "SEVERE":
                    logMethod = slf4jLoggerClass.getMethod("error", String.class);
                    break;
                case "WARNING":
                    logMethod = slf4jLoggerClass.getMethod("warn", String.class);
                    break;
                case "INFO":
                    logMethod = slf4jLoggerClass.getMethod("info", String.class);
                    break;
                case "CONFIG":
                case "FINE":
                    logMethod = slf4jLoggerClass.getMethod("debug", String.class);
                    break;
                case "FINER":
                case "FINEST":
                    logMethod = slf4jLoggerClass.getMethod("trace", String.class);
                    break;
                default:
                    logMethod = slf4jLoggerClass.getMethod("info", String.class); // Default to info
                    break;
            }
            logMethod.invoke(slf4jLogger, message);
        } catch (Exception e) {
            // Fallback to println if reflection fails (shouldn't happen)
            System.out.println("[" + level + "] " + message);
        }
    }

    private void logWithLog4j(Level level, String message) {
        try {
            Object log4jLogger = logger;
            Class<?> log4jLoggerClass = Class.forName("org.apache.logging.log4j.Logger");
            Class<?> log4jLevelClass = Class.forName("org.apache.logging.log4j.Level");
            Method logMethod = log4jLoggerClass.getMethod("log", log4jLevelClass, String.class);

            // Map java.util.logging.Level to Log4j Level
            Object log4jLevel;
            switch (level.getName()) {
                case "SEVERE":
                    log4jLevel = log4jLevelClass.getField("ERROR").get(null);
                    break;
                case "WARNING":
                    log4jLevel = log4jLevelClass.getField("WARN").get(null);
                    break;
                case "INFO":
                    log4jLevel = log4jLevelClass.getField("INFO").get(null);
                    break;
                case "CONFIG":
                case "FINE":
                    log4jLevel = log4jLevelClass.getField("DEBUG").get(null);
                    break;
                case "FINER":
                case "FINEST":
                    log4jLevel = log4jLevelClass.getField("TRACE").get(null);
                    break;
                default:
                    log4jLevel = log4jLevelClass.getField("INFO").get(null); // Default to INFO
                    break;
            }

            logMethod.invoke(log4jLogger, log4jLevel, message);
        } catch (Exception e) {
            // Fallback to println if reflection fails (shouldn't happen)
            System.out.println("[" + level + "] " + message);
        }
    }

    private void logWithJdk(Level level, String message) {
        Logger jdkLogger = (Logger) logger;
        jdkLogger.log(level, message);
    }

    // Factory method to create a mod-specific logger with fallback
    public static FabricLoggerManager createModLogger(String modId) {
        // Try SLF4J first
        try {
            Class<?> slf4jLoggerClass = Class.forName("org.slf4j.Logger");
            Class<?> slf4jLoggerFactoryClass = Class.forName("org.slf4j.LoggerFactory");
            Method getLoggerMethod = slf4jLoggerFactoryClass.getMethod("getLogger", String.class);
            Object slf4jLogger = getLoggerMethod.invoke(null, modId);
            return new FabricLoggerManager(slf4jLogger, LoggerType.SLF4J);
        } catch (ClassNotFoundException e) {
            // SLF4J not found, try Log4j next
        } catch (Exception e) {
            // Reflection failed, try Log4j next
        }

        // Try Log4j
        try {
            Class<?> log4jLogManagerClass = Class.forName("org.apache.logging.log4j.LogManager");
            Method getLoggerMethod = log4jLogManagerClass.getMethod("getLogger", String.class);
            Object log4jLogger = getLoggerMethod.invoke(null, modId);
            return new FabricLoggerManager(log4jLogger, LoggerType.LOG4J);
        } catch (ClassNotFoundException e) {
            // Log4j not found, fall back to JDK Logger
        } catch (Exception e) {
            // Reflection failed, fall back to JDK Logger
        }

        // Fall back to JDK Logger
        Logger jdkLogger = Logger.getLogger(modId);
        return new FabricLoggerManager(jdkLogger, LoggerType.JDK);
    }
}
