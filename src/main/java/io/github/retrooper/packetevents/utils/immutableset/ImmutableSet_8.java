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

package io.github.retrooper.packetevents.utils.immutableset;

import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ImmutableSet_8<T> extends ImmutableSetAbstract<T> {
    private ImmutableSet<T> immutableSet;

    public ImmutableSet_8() {
        this.immutableSet = ImmutableSet.<T>builder().build();
    }

    public ImmutableSet_8(List<T> data) {
        this.immutableSet = ImmutableSet.<T>builder().addAll(data).build();
    }

    @SafeVarargs
    public ImmutableSet_8(T... data) {
        ImmutableSet.Builder<T> builder = ImmutableSet.builder();
        for (T value : data) {
            builder.add(value);
        }
        this.immutableSet = builder.build();
    }

    @Override
    public boolean contains(T element) {
        return immutableSet.contains(element);
    }

    @Override
    public void add(T element) {
        List<T> elements = new ArrayList<>(immutableSet);
        immutableSet = ImmutableSet.<T>builder().addAll(elements).add(element).build();
    }

    @SafeVarargs
    @Override
    public final void addAll(T... elements) {
        List<T> localElements = new ArrayList<>(immutableSet);
        immutableSet = ImmutableSet.<T>builder().addAll(localElements).addAll(Arrays.asList(elements)).build();
    }
}
