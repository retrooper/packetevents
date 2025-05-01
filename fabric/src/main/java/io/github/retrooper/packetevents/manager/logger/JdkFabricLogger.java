package io.github.retrooper.packetevents.manager.logger;

import net.kyori.adventure.text.format.NamedTextColor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JdkFabricLogger extends AbstractFabricLogger {
    private final Logger logger;

    public JdkFabricLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void log(Level level, NamedTextColor color, String message) {
        String msg = stripColorCodes(message);
        logger.log(level, msg);
    }
}