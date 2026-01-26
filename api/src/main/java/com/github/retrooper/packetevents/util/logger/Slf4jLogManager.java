package com.github.retrooper.packetevents.util.logger;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.util.LogManager;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * Only works on newer platforms which include slf4j.
 */
@NullMarked
@ApiStatus.Internal
public final class Slf4jLogManager extends LogManager {

    private static final @Nullable Class<?> LOGGER_CLASS = Reflection.getClassByNameWithoutException("org.slf4j.Logger");

    private final Object logger;
    private final Method trace, debug, info, warn, error;

    public Slf4jLogManager(PacketEventsAPI<?> packetevents) {
        super(packetevents);

        if (LOGGER_CLASS == null) {
            throw new UnsupportedOperationException("Can't find slf4j logger class");
        }
        try {
            this.logger = LOGGER_CLASS.getMethod("logger", String.class)
                    .invoke(null, LOGGER_NAME);
            this.trace = LOGGER_CLASS.getMethod("trace", String.class, Throwable.class);
            this.debug = LOGGER_CLASS.getMethod("debug", String.class, Throwable.class);
            this.info = LOGGER_CLASS.getMethod("info", String.class, Throwable.class);
            this.warn = LOGGER_CLASS.getMethod("warn", String.class, Throwable.class);
            this.error = LOGGER_CLASS.getMethod("error", String.class, Throwable.class);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }

    public static boolean exists() {
        return LOGGER_CLASS != null;
    }

    @Override
    public void log(Level level, ComponentLike component, @Nullable Throwable error) {
        String message = AdventureSerializer.stringify(component.asComponent());
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
