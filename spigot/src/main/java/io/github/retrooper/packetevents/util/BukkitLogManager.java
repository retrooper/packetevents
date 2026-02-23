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

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.util.LogManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection;

@NullMarked
@ApiStatus.Internal
public final class BukkitLogManager extends LogManager {

    private static final Component PREFIX = text("[" + LOGGER_NAME + "] ").color(AQUA);

    public BukkitLogManager(PacketEventsAPI<?> packetevents) {
        super(packetevents);
    }

    @Override
    public void log(Level level, ComponentLike component, @Nullable Throwable error) {
        ComponentLike line = text().append(PREFIX).append(component);
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        if (sender instanceof Audience) {
            sender.sendMessage(line);
        } else {
            // either spigot or old paper
            sender.sendMessage(legacySection().serialize(line.asComponent()));
        }
        if (error != null) {
            // I don't know if there is a better way to do this properly
            Bukkit.getLogger().log(level, "", error);
        }
    }
}
