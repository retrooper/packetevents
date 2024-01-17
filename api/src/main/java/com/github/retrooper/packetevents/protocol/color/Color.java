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

package com.github.retrooper.packetevents.protocol.color;

import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public final class Color implements RGBLike {

    private static final int BIT_MASK = 0xFF;

    private final int red, green, blue;

    public Color(@Range(from = 0L, to = 255L) int red, @Range(from = 0L, to = 255L) int green, @Range(from = 0L, to = 255L) int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color(int rgb) {
        this((rgb >> 16) & BIT_MASK, (rgb >> 8) & BIT_MASK, rgb & BIT_MASK);
    }

    public @NotNull Color withRed(@Range(from = 0L, to = 255L) int red) {
        return new Color(red, green, blue);
    }

    public @NotNull Color withGreen(@Range(from = 0L, to = 255L) int green) {
        return new Color(red, green, blue);
    }

    public @NotNull Color withBlue(@Range(from = 0L, to = 255L) int blue) {
        return new Color(red, green, blue);
    }

    public int asRGB() {
        int rgb = red;
        rgb = (rgb << 8) + green;
        return (rgb << 8) + blue;
    }

    @Override
    public @Range(from = 0L, to = 255L) int red() {
        return red;
    }

    @Override
    public @Range(from = 0L, to = 255L) int green() {
        return green;
    }

    @Override
    public @Range(from = 0L, to = 255L) int blue() {
        return blue;
    }
}