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

package com.github.retrooper.packetevents.protocol.item.component.builtin;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;

public class FireworkExplosion {

    private final Shape shape;
    private final List<Integer> colors;
    private final List<Integer> fadeColors;
    private final boolean hasTrail;
    private final boolean hasTwinkle;

    public FireworkExplosion(
            Shape shape, List<Integer> colors, List<Integer> fadeColors,
            boolean hasTrail, boolean hasTwinkle
    ) {
        this.shape = shape;
        this.colors = colors;
        this.fadeColors = fadeColors;
        this.hasTrail = hasTrail;
        this.hasTwinkle = hasTwinkle;
    }

    public static FireworkExplosion read(PacketWrapper<?> wrapper) {
        Shape shape = wrapper.readEnum(Shape.values());
        List<Integer> colors = wrapper.readList(PacketWrapper::readInt);
        List<Integer> fadeColors = wrapper.readList(PacketWrapper::readInt);
        boolean hasTrail = wrapper.readBoolean();
        boolean hasTwinkle = wrapper.readBoolean();
        return new FireworkExplosion(shape, colors, fadeColors, hasTrail, hasTwinkle);
    }

    public static void write(PacketWrapper<?> wrapper, FireworkExplosion explosion) {
        wrapper.writeEnum(explosion.shape);
        wrapper.writeList(explosion.colors, PacketWrapper::writeInt);
        wrapper.writeList(explosion.fadeColors, PacketWrapper::writeInt);
        wrapper.writeBoolean(explosion.hasTrail);
        wrapper.writeBoolean(explosion.hasTwinkle);
    }

    public enum Shape {
        SMALL_BALL,
        LARGE_BALL,
        STAR,
        CREEPER,
        BURST,
    }
}
