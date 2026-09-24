package com.github.retrooper.packetevents.util.logger;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.util.LogManager;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;

/**
 * Only works on newer platforms which include slf4j.
 */
@NullMarked
@ApiStatus.Internal
public final class Slf4jLogManager extends LogManager {

    private static final boolean EXISTS = Reflection.getClassByNameWithoutException("org.slf4j.Logger") != null;

    private final Logger logger;

    public Slf4jLogManager(PacketEventsAPI<?> packetevents) {
        super(packetevents);
        this.logger = LoggerFactory.getLogger(LOGGER_NAME);
    }

    public static boolean exists() {
        return EXISTS;
    }

    @Override
    public void log(Level level, ComponentLike component, @Nullable Throwable error) {
        String message = AdventureSerializer.stringify(component.asComponent());
        if (level == Level.FINEST || level == Level.FINER) {
            this.logger.trace(message, error);
        } else if (level == Level.FINE) {
            this.logger.debug(message, error);
        } else if (level == Level.INFO) {
            this.logger.info(message, error);
        } else if (level == Level.WARNING) {
            this.logger.warn(message, error);
        } else if (level == Level.SEVERE) {
            this.logger.error(message, error);
        } else {
            throw new UnsupportedOperationException(level + " is unsupported (" + component + ")");
        }
    }
}
