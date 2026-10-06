/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class IdTable<T> {

    public static final int MAX_ID = 1 << 16;

    private volatile Object[][] table;
    private boolean mutable = true;

    public IdTable() {
        this(0);
    }

    public IdTable(int rows) {
        this.table = new Object[rows][];
    }

    public void put(int index, Object[] array) {
        this.assertMutable();
        this.resize(index);
        this.table[index] = array;
    }

    public void put(int index, int id, T val) {
        this.assertMutable();
        if (id < 0) {
            throw new IllegalArgumentException("negative id: " + id);
        }
        this.resize(index);
        Object[] row = this.table[index];
        if (row == null) {
            this.table[index] = row = new Object[id + 1];
        } else if (row.length <= id) {
            this.table[index] = row = Arrays.copyOf(row,
                    Math.max(id + 1, row.length + (row.length >> 1) + 1));
        }
        row[id] = val;
    }

    public boolean putAll(int index, Map<Integer, ? extends T> values) {
        return this.putAll(index, values, MAX_ID);
    }

    public boolean putAll(int index, Map<Integer, ? extends T> values, int idLimit) {
        this.assertMutable();
        int maxId = -1;
        for (Integer id : values.keySet()) {
            if (id != null && id >= 0 && id < idLimit) {
                maxId = Math.max(maxId, id);
            }
        }
        if (maxId < 0) {
            return false;
        }
        Object[] row = new Object[maxId + 1];
        for (Map.Entry<Integer, ? extends T> entry : values.entrySet()) {
            Integer id = entry.getKey();
            if (id != null && id >= 0 && id <= maxId) {
                row[id] = entry.getValue();
            }
        }
        this.put(index, row);
        return true;
    }

    public void fill(Object[] val) {
        this.assertMutable();
        Arrays.fill(this.table, val);
    }

    public Object[] get(int index) {
        return this.table[index];
    }

    @SuppressWarnings("unchecked")
    public @Nullable T lookup(int index, int id) {
        Object[][] table = this.table;
        if (index < 0 || index >= table.length) {
            return null;
        }
        Object[] arr = table[index];
        return arr != null && id >= 0 && id < arr.length ? (T) arr[id] : null;
    }

    public boolean isMutable() {
        return this.mutable;
    }

    public void immutable() {
        this.mutable = false;
    }

    public void mutable() {
        this.mutable = true;
    }

    private void resize(int index) {
        if (index >= this.table.length) {
            this.table = Arrays.copyOf(this.table, index + 1);
        }
    }

    private void assertMutable() {
        if (!mutable) {
            throw new IllegalStateException("table is immutable");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IdTable<?> idTable = (IdTable<?>) o;
        return Objects.deepEquals(table, idTable.table);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(table);
    }
}
