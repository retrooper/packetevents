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

public class EnumUtil {
    public static Enum<?> valueOf(Class<? extends Enum<?>> cls, String constantName) {
        if (cls == null) {
            return null;
        }
        for (Enum<?> enumConstant : cls.getEnumConstants()) {
            if (enumConstant.name().equals(constantName)) {
                return enumConstant;
            }
        }
        return null;
    }

    public static Enum<?> valueByIndex(Class<? extends Enum<?>> cls, int index) {
        if (cls == null) {
            return null;
        }
        return cls.getEnumConstants()[index];
    }
}
