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

public class Diff<T> {

    private final T value;

    public Diff(final T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public static class Addition<T> extends Diff<T> {

        public Addition(final T value) {
            super(value);
        }

        @Override
        public String toString() {
            return "+ " + getValue();
        }
    }

    public static class Removal<T> extends Diff<T> {

        public Removal(final T value) {
            super(value);
        }

        @Override
        public String toString() {
            return "- " + getValue();
        }
    }

    public static class Changed<T> extends Diff<T> {

        private final T oldValue;

        public Changed(final T oldValue, final T newValue) {
            super(newValue);
            this.oldValue = oldValue;
        }

        public T getOldValue() {
            return oldValue;
        }

        @Override
        public String toString() {
            return "~ " + getOldValue() + " -> " + getValue();
        }
    }

}
