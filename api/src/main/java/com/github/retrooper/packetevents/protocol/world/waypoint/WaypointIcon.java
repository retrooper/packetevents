/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.waypoint;

import com.github.retrooper.packetevents.protocol.color.Color;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class WaypointIcon {

    public static final ResourceLocation ICON_STYLE_DEFAULT = new ResourceLocation("default");
    public static final ResourceLocation ICON_STYLE_BOWTIE = new ResourceLocation("bowtie");

    private final ResourceLocation style;
    private final @Nullable Color color;

    public WaypointIcon(ResourceLocation style, @Nullable Color color) {
        this.style = style;
        this.color = color;
    }

    public static WaypointIcon read(PacketWrapper<?> wrapper) {
        ResourceLocation style = wrapper.readIdentifier();
        Color color = wrapper.readOptional(Color::readShort);
        return new WaypointIcon(style, color);
    }

    public static void write(PacketWrapper<?> wrapper, WaypointIcon icon) {
        wrapper.writeIdentifier(icon.style);
        wrapper.writeOptional(icon.color, Color::writeShort);
    }

    public ResourceLocation getStyle() {
        return this.style;
    }

    public @Nullable Color getColor() {
        return this.color;
    }
}
