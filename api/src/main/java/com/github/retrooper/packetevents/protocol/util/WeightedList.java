/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.util;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public final class WeightedList<T> {

    private final List<Entry<T>> entries;

    public WeightedList() {
        this(new ArrayList<>());
    }

    public WeightedList(List<Entry<T>> entries) {
        this.entries = entries;
    }

    public static <T> WeightedList<T> read(PacketWrapper<?> wrapper, PacketWrapper.Reader<T> reader) {
        List<Entry<T>> entries = wrapper.readList(ew -> Entry.read(wrapper, reader));
        return new WeightedList<>(entries);
    }

    public static <T> void write(PacketWrapper<?> wrapper, WeightedList<T> list, PacketWrapper.Writer<T> writer) {
        wrapper.writeList(list.entries, (ew, entry) -> Entry.write(ew, entry, writer));
    }

    public List<Entry<T>> getEntries() {
        return this.entries;
    }

    public static final class Entry<T> {

        private final int weight;
        private final T value;

        public Entry(int weight, T value) {
            this.weight = weight;
            this.value = value;
        }

        public static <T> Entry<T> read(PacketWrapper<?> wrapper, PacketWrapper.Reader<T> reader) {
            int weight = wrapper.readVarInt();
            T value = reader.apply(wrapper);
            return new Entry<>(weight, value);
        }

        public static <T> void write(PacketWrapper<?> wrapper, Entry<T> entry, PacketWrapper.Writer<T> writer) {
            wrapper.writeVarInt(entry.weight);
            writer.accept(wrapper, entry.value);
        }

        public int getWeight() {
            return this.weight;
        }

        public T getValue() {
            return this.value;
        }
    }
}
