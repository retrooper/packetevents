package com.github.retrooper.packetevents.util.logger;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.util.LogManager;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.logging.Level;

/**
 * Only works on newer platforms which include adventure-slf4j's ComponentLogger.
 */
@NullMarked
@ApiStatus.Internal
public final class ComponentLogManager extends LogManager {

    private static final boolean EXISTS = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.logger.slf4j.ComponentLogger") != null;

    private final ComponentLogger logger;

    public ComponentLogManager(PacketEventsAPI<?> packetevents) {
        super(packetevents);
        this.logger = ComponentLogger.logger(LOGGER_NAME);
    }

    public static boolean exists() {
        return EXISTS;
    }

    @Override
    public void log(Level level, ComponentLike component, @Nullable Throwable error) {
        Component message = component.asComponent();
        if (level == Level.FINEST || level == Level.FINER) {
            this.logger.trace(message, error);
        } else if (level == Level.FINE) {
            this.logger.debug(message, error);
        } else if (level == Level.INFO) {
            this.logger.info(message, error);
        } else if (level == Level.WARNING) {
            if (error != null) {
                this.logger.warn(message, error);
            } else {
                this.logger.warn(message);
            }
        } else if (level == Level.SEVERE) {
            if (error != null) {
                this.logger.error(message, error);
            } else {
                this.logger.error(message);
            }
        } else {
            throw new UnsupportedOperationException(level + " is unsupported (" + component + ")");
        }
    }
}
