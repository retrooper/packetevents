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

package io.github.retrooper.packetevents.utils.google;

import com.google.common.base.Optional;

public class GoogleOptionalUtils {
    public static Object getOptionalValue(Object opt) {
        return ((Optional<?>) opt).get();
    }

    public static Object getOptionalEmpty() {
        return Optional.absent();
    }

    public static Object getOptional(Object value) {
        return Optional.of(value);
    }
}
