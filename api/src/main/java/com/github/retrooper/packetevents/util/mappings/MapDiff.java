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

package com.github.retrooper.packetevents.util.mappings;

import java.util.Map;

public abstract class MapDiff<K, V> implements Diff<Map<K, V>> {

    private final K key;

    public MapDiff(final K key) {
        this.key = key;
    }

    public K getKey() {
        return key;
    }

    public abstract void applyTo(Map<K, V> map);

    public static class Addition<K, V> extends MapDiff<K, V> {
        private final V value;

        public Addition(final K key, final V value) {
            super(key);
            this.value = value;
        }

        public V getValue() {
            return value;
        }

        @Override
        public void applyTo(Map<K, V> map) {
            map.put(getKey(), getValue());
        }

        @Override
        public String toString() {
            return "+ " + getKey() + " : " + getValue();
        }

    }

    public static class Removal<K, V> extends MapDiff<K, V> {

        public Removal(final K key) {
            super(key);
        }

        @Override
        public void applyTo(Map<K, V> map) {
            map.remove(getKey());
        }

        @Override
        public String toString() {
            return "- " + getKey();
        }
    }

}
