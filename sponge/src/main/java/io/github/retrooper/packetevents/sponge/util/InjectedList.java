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

package io.github.retrooper.packetevents.sponge.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

public class InjectedList<E> implements List<E> {
    private final List<E> originalList;
    private final Consumer<E> pushBackAction;

    public InjectedList(List<E> originalList, Consumer<E> pushBackAction) {
        for (E key : originalList) {
            pushBackAction.accept(key);
        }
        this.originalList = originalList;
        this.pushBackAction = pushBackAction;
    }

    public List<E> originalList() {
        return originalList;
    }

    public Consumer<E> pushBackAction() {
        return pushBackAction;
    }

    @Override
    public synchronized boolean add(E e) {
        pushBackAction.accept(e);
        return originalList.add(e);
    }

    @Override
    public synchronized boolean addAll(@NotNull Collection<? extends E> c) {
        for (E element : c) {
            pushBackAction.accept(element);
        }
        return originalList.addAll(c);
    }

    @Override
    public synchronized boolean addAll(int index, @NotNull Collection<? extends E> c) {
        for (E element : c) {
            pushBackAction.accept(element);
        }
        return originalList.addAll(index, c);
    }

    @Override
    public synchronized void add(int index, E element) {
        pushBackAction.accept(element);
        originalList.add(index, element);
    }

    @Override
    public synchronized int size() {
        return originalList.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return originalList.isEmpty();
    }

    @Override
    public synchronized boolean contains(Object o) {
        return originalList.contains(o);
    }

    @NotNull
    @Override
    public synchronized Iterator<E> iterator() {
        return originalList.iterator();
    }

    @NotNull
    @Override
    public synchronized Object[] toArray() {
        return originalList.toArray();
    }

    @NotNull
    @Override
    public synchronized <T> T[] toArray(@NotNull T[] a) {
        return originalList.toArray(a);
    }

    @Override
    public synchronized boolean remove(Object o) {
        return originalList.remove(o);
    }

    @Override
    public synchronized boolean containsAll(@NotNull Collection<?> c) {
        return originalList.containsAll(c);
    }

    @Override
    public synchronized boolean removeAll(@NotNull Collection<?> c) {
        return originalList.removeAll(c);
    }

    @Override
    public synchronized boolean retainAll(@NotNull Collection<?> c) {
        return originalList.retainAll(c);
    }

    @Override
    public synchronized void clear() {
        originalList.clear();
    }

    @Override
    public synchronized E get(int index) {
        return originalList.get(index);
    }

    @Override
    public synchronized E set(int index, E element) {
        return originalList.set(index, element);
    }

    @Override
    public synchronized E remove(int index) {
        return originalList.remove(index);
    }

    @Override
    public synchronized int indexOf(Object o) {
        return originalList.indexOf(o);
    }

    @Override
    public synchronized int lastIndexOf(Object o) {
        return originalList.lastIndexOf(o);
    }

    @NotNull
    @Override
    public synchronized ListIterator<E> listIterator() {
        return originalList.listIterator();
    }

    @NotNull
    @Override
    public synchronized ListIterator<E> listIterator(int index) {
        return originalList.listIterator(index);
    }

    @NotNull
    @Override
    public synchronized List<E> subList(int fromIndex, int toIndex) {
        return originalList.subList(fromIndex, toIndex);
    }
}
