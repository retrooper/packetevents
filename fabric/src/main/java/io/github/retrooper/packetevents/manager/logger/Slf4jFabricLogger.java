package io.github.retrooper.packetevents.manager.logger;

import net.kyori.adventure.text.format.NamedTextColor;

import java.util.logging.Level;

public class Slf4jFabricLogger extends AbstractFabricLogger {
    private final org.slf4j.Logger logger;

    public Slf4jFabricLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void log(Level level, NamedTextColor color, String message) {
        String msg = stripColorCodes(message);
        switch (level.getName()) {
            case "SEVERE":
                logger.error(msg);
                break;
            case "WARNING":
                logger.warn(msg);
                break;
            case "INFO":
                logger.info(msg);
                break;
            case "CONFIG":
            case "FINE":
                logger.debug(msg);
                break;
            case "FINER":
            case "FINEST":
                logger.trace(msg);
                break;
            default:
                logger.info(msg);
                break;
        }
    }
}