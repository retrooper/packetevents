package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.util.logger.ComponentLogManager;
import com.github.retrooper.packetevents.util.logger.JulLegacyLogManager;
import com.github.retrooper.packetevents.util.logger.Slf4jLogManager;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

@NullMarked
@ApiStatus.NonExtendable
public abstract class LogManager {

    protected static final String LOGGER_NAME = "packetevents";
    private static final char LEGACY_COLOR_CHAR = '§';

    protected final PacketEventsAPI<?> packetevents;

    public LogManager(PacketEventsAPI<?> packetevents) {
        this.packetevents = packetevents;
    }

    @Deprecated
    public LogManager() {
        this(PacketEvents.getAPI());
    }

    /**
     * Legacy method for ensuring backwards compatibility with
     * plugins which do try to use our {@link LogManager} with legacy
     * color log messages.
     */
    private static String stripLegacyColors(String message) {
        int sectionIndex;
        while ((sectionIndex = message.indexOf(LEGACY_COLOR_CHAR)) != -1) {
            if (message.length() < sectionIndex + 2) {
                break; // out of bounds
            }
            // cut away the legacy color code
            message = message.substring(0, sectionIndex) + message.substring(sectionIndex + 2);
        }
        return message;
    }

    @ApiStatus.Internal
    public static LogManager construct(PacketEventsAPI<?> packetevents) {
        if (ComponentLogManager.exists()) {
            return new ComponentLogManager(packetevents);
        } else if (Slf4jLogManager.exists()) {
            return new Slf4jLogManager(packetevents);
        } else {
            return new JulLegacyLogManager(packetevents);
        }
    }

    protected void log(Level level, @Nullable NamedTextColor color, String message) {
        this.log(level, color, message, null);
    }

    protected void log(Level level, @Nullable NamedTextColor color, String message, @Nullable Throwable error) {
        ComponentLike component = text(stripLegacyColors(message));
        if (color != null) {
            component = text().append(component).color(color);
        }
        this.log(level, component, error);
    }

    public abstract void log(Level level, ComponentLike component, @Nullable Throwable error);

    public void info(String message) {
        this.log(Level.INFO, WHITE, message);
    }

    public void warn(String message) {
        this.log(Level.WARNING, YELLOW, message);
    }

    public void warn(String message, @Nullable Throwable error) {
        this.log(Level.WARNING, YELLOW, message, error);
    }

    public void severe(String message) {
        this.log(Level.SEVERE, RED, message);
    }

    public void severe(String message, @Nullable Throwable error) {
        this.log(Level.SEVERE, RED, message, error);
    }

    public void debug(String message) {
        if (this.isDebug()) {
            this.log(Level.FINE, GRAY, message);
        }
    }

    public boolean isDebug() {
        return this.packetevents.getSettings().isDebugEnabled();
    }
}
