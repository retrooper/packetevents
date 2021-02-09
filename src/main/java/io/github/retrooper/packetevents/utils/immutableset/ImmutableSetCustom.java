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

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.util.List;

public class ImmutableSetCustom<T> {
    private final ImmutableSetAbstract<T> immutableSetAbstract;
    public ImmutableSetCustom() {
        if (PacketEvents.get().getServerUtils().getVersion().isOlderThan(ServerVersion.v_1_8)) {
            immutableSetAbstract = new ImmutableSet_7<T>();
        }
        else {
            immutableSetAbstract = new ImmutableSet_8<T>();
        }
    }

    public ImmutableSetCustom(List<T> data) {
        if (PacketEvents.get().getServerUtils().getVersion().isOlderThan(ServerVersion.v_1_8)) {
            immutableSetAbstract = new ImmutableSet_7<T>(data);
        }
        else {
            immutableSetAbstract = new ImmutableSet_8<T>(data);
        }
    }

    public ImmutableSetCustom(T... data) {
        if (PacketEvents.get().getServerUtils().getVersion().isOlderThan(ServerVersion.v_1_8)) {
            immutableSetAbstract = new ImmutableSet_7<T>(data);
        }
        else {
            immutableSetAbstract = new ImmutableSet_8<T>(data);
        }
    }


    public boolean contains(T element) {
        return immutableSetAbstract.contains(element);
    }

    public void add(T element) {
        immutableSetAbstract.add(element);
    }

    public void addAll(T... elements) {
        immutableSetAbstract.addAll(elements);
    }
}
