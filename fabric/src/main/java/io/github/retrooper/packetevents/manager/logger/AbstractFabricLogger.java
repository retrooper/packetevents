package io.github.retrooper.packetevents.manager.logger;

import com.github.retrooper.packetevents.util.LogManager;

import java.util.logging.Logger;
import java.util.regex.Pattern;

public abstract class AbstractFabricLogger extends LogManager {
    protected static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("§[0-9A-FK-ORa-fk-or]");

    protected String stripColorCodes(String message) {
        return STRIP_COLOR_PATTERN.matcher(message).replaceAll("");
    }

    public static AbstractFabricLogger createModLogger(String modId) {
        // Try SLF4J
        try {
            org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(modId);
            return new Slf4jFabricLogger(logger);
        } catch (Throwable ignored) {}

        // Try Log4j
        try {
            org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(modId);
            return new Log4jFabricLogger(logger);
        } catch (Throwable ignored) {}

        // Fallback to JDK logger
        return new JdkFabricLogger(Logger.getLogger(modId));
    }
}