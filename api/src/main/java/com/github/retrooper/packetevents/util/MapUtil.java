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

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class MapUtil {

    private MapUtil() {
    }

    public static boolean isDeepEqual(@Nullable Object o1, @Nullable Object o2) {
        if (o1 == o2) {
            return true;
        } else if (o1 == null || o2 == null) {
            return false;
        } else if (o1 instanceof DeepComparableEntity) {
            return ((DeepComparableEntity) o1).deepEquals(o2);
        } else if (o2 instanceof DeepComparableEntity) {
            return ((DeepComparableEntity) o2).deepEquals(o2);
        } else {
            return Objects.equals(o1, o2);
        }
    }

    public static <K> boolean isDeepEqual(Map<K, ?> map1, Map<K, ?> map2) {
        if (map1.isEmpty() && map2.isEmpty()) {
            return true;
        } else if (map1.size() != map2.size()) {
            return false;
        }
        // compare each entry
        for (Map.Entry<K, ?> entry : map1.entrySet()) {
            Object val2 = map2.get(entry.getKey());
            if (!isDeepEqual(entry.getValue(), val2)) {
                return false;
            }
        }
        return true;
    }

    // would be built-in with modern java...
    @SafeVarargs
    public static <K, V> Map<K, V> createMap(Map.Entry<? extends K, ? extends V>... entries) {
        if (entries.length == 0) {
            return Collections.emptyMap();
        }
        Map<K, V> map = new HashMap<>(entries.length);
        for (Map.Entry<? extends K, ? extends V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return Collections.unmodifiableMap(map);
    }
}
