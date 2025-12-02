/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package io.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.ColorUtil;
import com.github.retrooper.packetevents.util.LogManager;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class BukkitLogManager extends LogManager {
    private final String prefixText = ColorUtil.toString(NamedTextColor.AQUA) + "[packetevents] " + ColorUtil.toString(NamedTextColor.WHITE);

    @Override
    protected void log(Level level, @Nullable NamedTextColor color, String message) {
        Bukkit.getConsoleSender().sendMessage(prefixText + ColorUtil.toString(color) + message);
    }

    @Override
    public void info(String message) {
        log(Level.INFO, NamedTextColor.WHITE, message);
    }

    @Override
    public void warn(final String message) {
        log(Level.WARNING, NamedTextColor.YELLOW, message);
    }

    @Override
    public void severe(String message) {
        log(Level.SEVERE, NamedTextColor.RED, message);
    }

    @Override
    public void debug(String message) {
        if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
            log(Level.FINE, NamedTextColor.GRAY, message);
        }
    }
}
