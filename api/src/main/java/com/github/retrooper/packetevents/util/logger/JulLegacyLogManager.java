package com.github.retrooper.packetevents.util.logger;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.util.LogManager;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;

import static net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection;

@NullMarked
@ApiStatus.Internal
public final class JulLegacyLogManager extends LogManager {

    private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);

    public JulLegacyLogManager(PacketEventsAPI<?> packetevents) {
        super(packetevents);
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void log(Level level, ComponentLike component, @Nullable Throwable error) {
        LOGGER.log(level, legacySection().serialize(component.asComponent()), error);
    }
}
