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

    public static int getId(NamedTextColor color) {
        if (color == null) {
            return -1;
        }
        switch (color.toString()) {
            case "black":
                return 0;
            case "dark_blue":
                return 1;
            case "dark_green":
                return 2;
            case "dark_aqua":
                return 3;
            case "dark_red":
                return 4;
            case "dark_purple":
                return 5;
            case "gold":
                return 6;
            case "gray":
                return 7;
            case "dark_gray":
                return 8;
            case "blue":
                return 9;
            case "green":
                return 10;
            case "aqua":
                return 11;
            case "red":
                return 12;
            case "light_purple":
                return 13;
            case "yellow":
                return 14;
            case "white":
                return 15;
            default:
                return 15;
        }
    }

    public static NamedTextColor fromId(int id) {
        if (id < 0) {
            return null;
        }
        switch (id) {
            case 0:
                return NamedTextColor.BLACK;
            case 1:
                return NamedTextColor.DARK_BLUE;
            case 2:
                return NamedTextColor.DARK_GREEN;
            case 3:
                return NamedTextColor.DARK_AQUA;
            case 4:
                return NamedTextColor.DARK_RED;
            case 5:
                return NamedTextColor.DARK_PURPLE;
            case 6:
                return NamedTextColor.GOLD;
            case 7:
                return NamedTextColor.GRAY;
            case 8:
                return NamedTextColor.DARK_GRAY;
            case 9:
                return NamedTextColor.BLUE;
            case 10:
                return NamedTextColor.GREEN;
            case 11:
                return NamedTextColor.AQUA;
            case 12:
                return NamedTextColor.RED;
            case 13:
                return NamedTextColor.LIGHT_PURPLE;
            case 14:
                return NamedTextColor.YELLOW;
            case 15:
                return NamedTextColor.WHITE;
            default:
                return NamedTextColor.WHITE;
        }
    }
}