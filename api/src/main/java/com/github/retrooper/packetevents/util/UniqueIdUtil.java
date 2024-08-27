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

package com.github.retrooper.packetevents.util;

import java.util.UUID;

public final class UniqueIdUtil {

    private UniqueIdUtil() {
    }

    public static UUID fromIntArray(int[] array) {
        if (array.length != 4) {
            throw new IllegalStateException("Invalid encoded uuid length: " + array.length + " != 4");
        }
        return new UUID(
                (long) array[0] << 32 | (long) array[1] & 0xFFFFFFFFL,
                (long) array[2] << 32 | (long) array[3] & 0xFFFFFFFFL
        );
    }

    public static int[] toIntArray(UUID uniqueId) {
        return new int[]{
                (int) (uniqueId.getMostSignificantBits() >> 32),
                (int) uniqueId.getMostSignificantBits(),
                (int) (uniqueId.getLeastSignificantBits() >> 32),
                (int) uniqueId.getLeastSignificantBits()
        };
    }
}
