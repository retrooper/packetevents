/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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

    public ImmutableSet_8(T... data) {
        ImmutableSet.Builder<T> builder = ImmutableSet.<T>builder();
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

    @Override
    public void addAll(T... elements) {
        List<T> localElements = new ArrayList<>(immutableSet);
        immutableSet = ImmutableSet.<T>builder().addAll(localElements).addAll(Arrays.asList(elements)).build();
    }
}
