/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.util;

import net.kyori.adventure.text.format.NamedTextColor;

public class ColorUtil {
    public static String toString(NamedTextColor color) {
        String prefix = "\u00A7";
        if (color == null) {
            return prefix + "f";
        }
        switch (color.toString()) {
            case "black":
                return prefix + "0";
            case "dark_blue":
                return prefix + "1";
            case "dark_green":
                return prefix + "2";
            case "dark_aqua":
                return prefix + "3";
            case "dark_red":
                return prefix + "4";
            case "dark_purple":
                return prefix + "5";
            case "gold":
                return prefix + "6";
            case "gray":
                return prefix + "7";
            case "dark_gray":
                return prefix + "8";
            case "blue":
                return prefix + "9";
            case "green":
                return prefix + "a";
            case "aqua":
                return prefix + "b";
            case "red":
                return prefix + "c";
            case "light_purple":
                return prefix + "d";
            case "yellow":
                return prefix + "e";
            case "white":
                return prefix + "f";
            default:
                return prefix + "f";
        }
    }
}
