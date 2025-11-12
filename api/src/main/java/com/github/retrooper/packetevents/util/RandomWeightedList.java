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

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.protocol.component.ComponentType.Decoder;
import com.github.retrooper.packetevents.protocol.component.ComponentType.Encoder;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RandomWeightedList<T> implements Iterable<RandomWeightedList.Entry<T>> {

    private List<Entry<T>> entries;

    public RandomWeightedList() {
        this(new ArrayList<>());
    }
    public RandomWeightedList(List<Entry<T>> entries) {
        this.entries = entries;
    }

    public RandomWeightedList(T entry, int weight) {
        this(new Entry<>(entry, weight));
    }

    public RandomWeightedList(Entry<T> entry) {
        this.entries = new ArrayList<>(1);
        this.entries.add(entry);
    }

    public static <T> RandomWeightedList<T> decode(NBT nbt, ClientVersion version, Decoder<T> decoder) {
        List<Entry<T>> entries;
        if (nbt instanceof NBTCompound) {
            entries = new ArrayList<>(1);
            entries.add(Entry.decode(nbt, version, decoder));
        } else if (nbt instanceof NBTList<?>) {
            NBTList<?> list = (NBTList<?>) nbt;
            entries = new ArrayList<>(list.size());
            for (NBT tag : list.getTags()) {
                entries.add(Entry.decode(tag, version, decoder));
            }
        } else {
            throw new UnsupportedOperationException("Can't decode " + nbt + " as random weighted list");
        }
        return new RandomWeightedList<>(entries);
    }

    public static <T> NBT encode(RandomWeightedList<T> list, ClientVersion version, Encoder<T> encoder) {
        NBTList<NBTCompound> nbt = new NBTList<>(NBTType.COMPOUND, list.entries.size());
        for (Entry<T> entry : list.entries) {
            nbt.addTag(Entry.encode(entry, version, encoder));
        }
        return nbt;
    }

    public List<Entry<T>> getEntries() {
        return this.entries;
    }

    public void setEntries(List<Entry<T>> entries) {
        this.entries = entries;
    }

    @Override
    public @NotNull Iterator<Entry<T>> iterator() {
        return this.entries.iterator();
    }

    public static final class Entry<T> {

        private final T data;
        private final int weight;

        public Entry(T data, int weight) {
            this.data = data;
            this.weight = weight;
        }

        public static <T> Entry<T> decode(NBT nbt, ClientVersion version, Decoder<T> decoder) {
            NBTCompound compound = (NBTCompound) nbt;
            int weight = compound.getNumberTagOrThrow("weight").getAsInt();
            T data = decoder.decode(compound.getTagOrThrow("data"), version);
            return new Entry<>(data, weight);
        }

        public static <T> NBTCompound encode(Entry<T> entry, ClientVersion version, Encoder<T> encoder) {
            NBTCompound compound = new NBTCompound();
            compound.setTag("weight", new NBTInt(entry.weight));
            compound.setTag("data", encoder.encode(entry.data, version));
            return compound;
        }

        public T getData() {
            return this.data;
        }

        public int getWeight() {
            return this.weight;
        }
    }
}
