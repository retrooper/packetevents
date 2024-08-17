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

package com.github.retrooper.packetevents.util.adventure;

import net.kyori.adventure.util.Index;

import java.util.NoSuchElementException;

public final class AdventureIndexUtil {

    private AdventureIndexUtil() {
    }

    // support for adventure < 4.11.0
    public static <K, V> V indexValueOrThrow(Index<K, V> index, K key) {
        final V value = index.value(key);
        if (value == null) {
            throw new NoSuchElementException("There is no value for key " + key);
        }
        return value;
    }

    // support for adventure < 4.11.0
    public static <K, V> K indexKeyOrThrow(Index<K, V> index, V value) {
        final K key = index.key(value);
        if (key == null) {
            throw new NoSuchElementException("There is no key for value " + value);
        }
        return key;
    }
}
