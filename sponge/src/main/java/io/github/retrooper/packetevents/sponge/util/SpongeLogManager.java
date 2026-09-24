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
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import io.github.retrooper.packetevents.sponge.PacketEventsPlugin;
import net.kyori.adventure.text.ComponentLike;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.plugin.PluginContainer;

import java.util.Map;
import java.util.logging.Level;

@NullMarked
@Deprecated(forRemoval = true)
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
    public void log(Level level, ComponentLike component, @Nullable Throwable error) {
        String plainMessage = AdventureSerializer.stringify(component);
        org.apache.logging.log4j.Level log4jLevel = LEVEL_CONVERSION.getOrDefault(level, org.apache.logging.log4j.Level.INFO);
        logger.log(log4jLevel, isPacketEvents ? plainMessage : "[packetevents] " + plainMessage, error);
    }
}
