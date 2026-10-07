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

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NullMarked
public final class WeightedList<T> {

    private final List<Entry<T>> entries;

    public WeightedList() {
        this(new ArrayList<>());
    }

    public WeightedList(List<Entry<T>> entries) {
        this.entries = entries;
    }

    public static <T> NbtCodec<WeightedList<T>> codec(NbtCodec<T> valueCodec) {
        return Entry.codec(valueCodec).applyList().apply(WeightedList::new, WeightedList::getEntries);
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof WeightedList)) return false;
        WeightedList<?> that = (WeightedList<?>) obj;
        return this.entries.equals(that.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.entries);
    }

    public static final class Entry<T> {

        private final int weight;
        private final T value;

        public Entry(int weight, T value) {
            this.weight = weight;
            this.value = value;
        }

        public static <T> NbtCodec<Entry<T>> codec(NbtCodec<T> valueCodec) {
            return new NbtMapCodec<Entry<T>>() {
                @Override
                public Entry<T> decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
                    T value = tag.getOrThrow("data", valueCodec, wrapper);
                    int weight = tag.getNumberTagValueOrThrow("weight").intValue();
                    return new Entry<>(weight, value);
                }

                @Override
                public void encode(NBTCompound tag, PacketWrapper<?> wrapper, Entry<T> value) throws NbtCodecException {
                    tag.set("data", value.getValue(), valueCodec, wrapper);
                    tag.setTag("weight", new NBTInt(value.getWeight()));
                }
            }.codec();
        }

        public static <T> Entry<T> read(PacketWrapper<?> wrapper, PacketWrapper.Reader<T> reader) {
            T value = reader.apply(wrapper);
            int weight = wrapper.readVarInt();
            return new Entry<>(weight, value);
        }

        public static <T> void write(PacketWrapper<?> wrapper, Entry<T> entry, PacketWrapper.Writer<T> writer) {
            writer.accept(wrapper, entry.value);
            wrapper.writeVarInt(entry.weight);
        }

        public int getWeight() {
            return this.weight;
        }

        public T getValue() {
            return this.value;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof Entry)) return false;
            Entry<?> entry = (Entry<?>) obj;
            if (this.weight != entry.weight) return false;
            return this.value.equals(entry.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.weight, this.value);
        }
    }
}
