/*
 * This file is part of ViaVersion - https://github.com/ViaVersion/ViaVersion
 * Copyright (C) 2016-2021 ViaVersion and contributors
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

package io.github.retrooper.packetevents.utils.list;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class ListWrapper implements List {
    private final List list;

    public ListWrapper(List inputList) {
        this.list = inputList;
    }

    public abstract void processAdd(Object o);

    public List getOriginalList() {
        return list;
    }

    @Override
    public int size() {
        synchronized (this) {
            return this.list.size();
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (this) {
            return this.list.isEmpty();
        }
    }


    @Override
    public boolean contains(Object o) {
        synchronized (this) {
            return this.list.contains(o);
        }
    }

    @Override
    public @NotNull Iterator iterator() {
        synchronized (this) {
            return listIterator();
        }
    }

    @Override
    public Object[] toArray() {
        synchronized (this) {
            return this.list.toArray();
        }
    }

    @Override
    public boolean add(Object o) {
        processAdd(o);
        synchronized (this) {
            return this.list.add(o);
        }
    }

    @Override
    public boolean remove(Object o) {
        synchronized (this) {
            return this.list.remove(o);
        }
    }

    @Override
    public boolean addAll(Collection c) {
        for (Object o : c) {
            processAdd(o);
        }
        synchronized (this) {
            return this.list.addAll(c);
        }
    }

    @Override
    public boolean addAll(int index, Collection c) {
        for (Object o : c) {
            processAdd(o);
        }
        synchronized (this) {
            return this.list.addAll(index, c);
        }
    }

    @Override
    public void clear() {
        synchronized (this) {
            this.list.clear();
        }
    }

    @Override
    public Object get(int index) {
        synchronized (this) {
            return this.list.get(index);
        }
    }

    @Override
    public Object set(int index, Object element) {
        synchronized (this) {
            return this.list.set(index, element);
        }
    }

    @Override
    public void add(int index, Object element) {
        synchronized (this) {
            this.list.add(index, element);
        }
    }

    @Override
    public Object remove(int index) {
        synchronized (this) {
            return this.list.remove(index);
        }
    }

    @Override
    public int indexOf(Object o) {
        synchronized (this) {
            return this.list.indexOf(o);
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        synchronized (this) {
            return this.list.lastIndexOf(o);
        }
    }

    @Override
    public @NotNull ListIterator listIterator() {
        synchronized (this) {
            return this.list.listIterator();
        }
    }

    @Override
    public @NotNull ListIterator listIterator(int index) {
        synchronized (this) {
            return this.list.listIterator(index);
        }
    }

    @Override
    public @NotNull List subList(int fromIndex, int toIndex) {
        synchronized (this) {
            return this.list.subList(fromIndex, toIndex);
        }
    }

    @Override
    public boolean retainAll(@NotNull Collection c) {
        synchronized (this) {
            return this.list.retainAll(c);
        }
    }

    @Override
    public boolean removeAll(@NotNull Collection c) {
        synchronized (this) {
            return this.list.removeAll(c);
        }
    }

    @Override
    public boolean containsAll(@NotNull Collection c) {
        synchronized (this) {
            return this.list.containsAll(c);
        }
    }

    @Override
    public Object[] toArray(Object[] a) {
        synchronized (this) {
            return this.list.toArray(a);
        }
    }
}