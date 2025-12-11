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

package io.github.retrooper.packetevents.sponge.util;

import com.github.retrooper.packetevents.util.LogManager;
import io.github.retrooper.packetevents.sponge.PacketEventsPlugin;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.plugin.PluginContainer;

import java.util.Map;
import java.util.logging.Level;

public class SpongeLogManager extends LogManager {

    private static final Map<java.util.logging.Level, org.apache.logging.log4j.Level> LEVEL_CONVERSION = Map.of(
            java.util.logging.Level.FINEST, org.apache.logging.log4j.Level.TRACE,
            java.util.logging.Level.FINER, org.apache.logging.log4j.Level.TRACE,
            java.util.logging.Level.FINE, org.apache.logging.log4j.Level.DEBUG,
            java.util.logging.Level.INFO, org.apache.logging.log4j.Level.INFO,
            java.util.logging.Level.WARNING, org.apache.logging.log4j.Level.WARN,
            java.util.logging.Level.SEVERE, org.apache.logging.log4j.Level.ERROR
    );

    private final Logger logger;
    // If this is true, then the logger will not add the [packetevents] prefix
    private final boolean isPacketEvents;

    public SpongeLogManager(PluginContainer pluginContainer) {
        this.logger = pluginContainer.logger();
        this.isPacketEvents = pluginContainer.instance() instanceof PacketEventsPlugin;
    }

    @Override
    protected void log(Level level, @Nullable NamedTextColor color, String message) {
        String plainMessage = STRIP_COLOR_PATTERN.matcher(message).replaceAll("");
        logger.log(LEVEL_CONVERSION.getOrDefault(level, org.apache.logging.log4j.Level.INFO), isPacketEvents ? plainMessage : "[packetevents] " + plainMessage);
    }
}
