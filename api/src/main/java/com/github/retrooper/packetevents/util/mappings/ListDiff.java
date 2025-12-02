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

import java.util.List;

public abstract class ListDiff<T> implements Diff<List<T>> {

    private final int index;

    public ListDiff(final int index) {
        this.index = index;
    }

    public abstract void applyTo(List<T> list);

    public int getIndex() {
        return index;
    }

    public static class Addition<T> extends ListDiff<T> {
        private final List<T> values;

        public Addition(final int index, final List<T> values) {
            super(index);
            this.values = values;
        }

        public List<T> getValues() {
            return values;
        }

        @Override
        public void applyTo(List<T> list) {
            list.addAll(getIndex(), getValues());
        }

        @Override
        public String toString() {
            return "+ " + getIndex() + " : " + getValues();
        }
    }

    public static class Removal<T> extends ListDiff<T> {
        private final int size;

        public Removal(final int index, final int size) {
            super(index);
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        @Override
        public void applyTo(List<T> list) {
            list.subList(getIndex(), getIndex() + size).clear();
        }

        @Override
        public String toString() {
            return "- " + getIndex() + " : " + getSize();
        }
    }

    public static class Changed<T> extends ListDiff<T> {

        private final int oldSize;
        private final List<T> newValue;

        public Changed(final int index, final int oldSize, final List<T> newValue) {
            super(index);
            this.oldSize = oldSize;
            this.newValue = newValue;
        }

        public int getOldSize() {
            return oldSize;
        }

        public List<T> getNewValue() {
            return newValue;
        }

        @Override
        public String toString() {
            return "* " + getIndex() + " : " + getOldSize() + " -> " + getNewValue();
        }

        @Override
        public void applyTo(List<T> list) {
            list.subList(getIndex(), getIndex() + oldSize).clear();
            list.addAll(getIndex(), getNewValue());
        }
    }

}
