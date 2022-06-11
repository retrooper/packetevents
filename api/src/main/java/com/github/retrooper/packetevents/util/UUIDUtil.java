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

import java.util.UUID;
import java.util.regex.Pattern;

public class UUIDUtil {
    public static final UUID DUMMY = new UUID(0L, 0L);
    private static final Pattern PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    public static UUID fromStringWithoutDashes(String uuid) {
        String correctedUUID = PATTERN.matcher(uuid).replaceAll("$1-$2-$3-$4-$5");
        return UUID.fromString(correctedUUID);
    }

    public static UUID fromString(String uuid) {
        return UUID.fromString(uuid);
    }

    public static String toString(UUID uuid) {
        return uuid.toString();
    }

    public static String toStringWithoutDashes(UUID uuid) {
        return uuid.toString().replace("-", "");
    }
}
