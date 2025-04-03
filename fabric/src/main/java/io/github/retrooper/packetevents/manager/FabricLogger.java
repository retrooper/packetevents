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

package io.github.retrooper.packetevents.manager;

import com.github.retrooper.packetevents.util.LogManager;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Nullable;
import java.util.logging.Logger;

public class FabricLogger extends LogManager {

    private final Logger logger;

    public FabricLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void log(java.util.logging.Level level, @Nullable NamedTextColor color, String message) {
        String plainMessage = STRIP_COLOR_PATTERN.matcher(message).replaceAll("");
        this.logger.log(level, plainMessage);
    }
}
