package com.github.retrooper.packetevents.util.logger;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.util.LogManager;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * Only works on newer platforms which include adventure-slf4j's ComponentLogger.
 */
@NullMarked
@ApiStatus.Internal
public final class ComponentLogManager extends LogManager {

    private static final @Nullable Class<?> LOGGER_CLASS = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.logger.slf4j.ComponentLogger");

    private final Object logger;
    private final Method trace, debug, info, warn, error;

    public ComponentLogManager(PacketEventsAPI<?> packetevents) {
        super(packetevents);

        if (LOGGER_CLASS == null) {
            throw new UnsupportedOperationException("Can't find ComponentLogger class");
        }
        try {
            this.logger = LOGGER_CLASS.getMethod("logger", String.class)
                    .invoke(null, LOGGER_NAME);
            this.trace = LOGGER_CLASS.getMethod("trace", Component.class, Throwable.class);
            this.debug = LOGGER_CLASS.getMethod("debug", Component.class, Throwable.class);
            this.info = LOGGER_CLASS.getMethod("info", Component.class, Throwable.class);
            this.warn = LOGGER_CLASS.getMethod("warn", Component.class, Throwable.class);
            this.error = LOGGER_CLASS.getMethod("error", Component.class, Throwable.class);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }

    public static boolean exists() {
        return LOGGER_CLASS != null;
    }

    @Override
    public void log(Level level, ComponentLike component, @Nullable Throwable error) {
        Component message = component.asComponent();
        try {
            if (level == Level.FINEST || level == Level.FINER) {
                this.trace.invoke(this.logger, message, error);
            } else if (level == Level.FINE) {
                this.debug.invoke(this.logger, message, error);
            } else if (level == Level.INFO) {
                this.info.invoke(this.logger, message, error);
            } else if (level == Level.WARNING) {
                this.warn.invoke(this.logger, message, error);
            } else if (level == Level.SEVERE) {
                this.error.invoke(this.logger, message, error);
            } else {
                throw new UnsupportedOperationException(level + " is unsupported (" + component + ")");
            }
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
