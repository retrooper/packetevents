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

package com.github.retrooper.packetevents.protocol.data.chat;

import org.jetbrains.annotations.Nullable;

public enum Color {
    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_CYAN('3'),
    DARK_RED('4'),
    PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    BRIGHT_GREEN('a'),
    CYAN('b'),
    RED('c'),
    PINK('d'),
    YELLOW('e'),
    WHITE('f'),

    OBFUSCATED('k', false),
    BOLD('l', false),
    STRIKETHROUGH('m', false),
    UNDERLINE('n', false),
    ITALIC('o', false),
    RESET('r', false);

    public static final char PREFIX = '&';

    private final char code;
    private final boolean color;
    private final String name;
    private final String fullCode;

    Color(char code) {
        this(code, true);
    }

    Color(char code, boolean color) {
        this.code = code;
        this.color = color;
        this.name = name().toLowerCase();
        this.fullCode = new String(new char[] {PREFIX, code});

    }

    public boolean isColor() {
        return color;
    }

    @Nullable
    public static Color getByCode(char code) {
        for (Color color : values()) {
            if (color.code == code) {
                return color;
            }
        }
        return null;
    }

    @Nullable
    public static Color getByName(String name) {
        if ("".equals(name)) {
            return Color.WHITE;
        }
        for (Color color : values()) {
            if (color.name.equals(name)) {
                return color;
            }
        }
        return null;
    }

    public char getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getFullCode() {
        return fullCode;
    }
}
