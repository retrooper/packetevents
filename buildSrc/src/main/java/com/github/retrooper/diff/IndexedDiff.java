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

package com.github.retrooper.diff;

public class IndexedDiff<T> extends Diff<T> {

    private final int index;

    public IndexedDiff(final int index, final T value) {
        super(value);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static class Addition<T> extends IndexedDiff<T> {
        public Addition(final int index, final T value) {
            super(index, value);
        }

        @Override
        public String toString() {
            return "+ " + getIndex() + " : " + getValue();
        }
    }

    public static class Removal<T> extends IndexedDiff<T> {
        public Removal(final int index, final T value) {
            super(index, value);
        }

        @Override
        public String toString() {
            return "- " + getIndex() + " : " + getValue();
        }
    }

}
