/*
 * This file is part of ProtocolSupport - https://github.com/ProtocolSupport/ProtocolSupport
 * Copyright (C) 2021 ProtocolSupport
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.protocol.nbt;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NBTList<T extends NBT> extends NBT {

    protected final NBTType<T> type;
    protected final List<T> tags;

    public NBTList(NBTType<T> type) {
        this.type = type;
        this.tags = new ArrayList<>();
    }

    public NBTList(NBTType<T> type, int size) {
        this.type = type;
        this.tags = new ArrayList<>(size);
    }

    public NBTList(NBTType<T> type, List<T> tags) {
        this.type = type;
        this.tags = new ArrayList<>();
        this.tags.addAll(tags);
    }

    public static NBTList<NBTCompound> createCompoundList() {
        return new NBTList<>(NBTType.COMPOUND);
    }

    public static NBTList<NBTString> createStringList() {
        return new NBTList<>(NBTType.STRING);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public NBTType<NBTList> getType() {
        return NBTType.LIST;
    }

    public NBTType<T> getTagsType() {
        return type;
    }

    public boolean isEmpty() {
        return tags.isEmpty();
    }

    public int size() {
        return tags.size();
    }

    public List<T> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public T getTag(int index) {
        return tags.get(index);
    }

    public void setTag(int index, T tag) {
        validateAddTag(tag);
        tags.set(index, tag);
    }

    public void addTag(int index, T tag) {
        validateAddTag(tag);
        tags.add(index, tag);
    }

    public void addTag(T tag) {
        validateAddTag(tag);
        tags.add(tag);
    }

    public void addTagUnsafe(int index, NBT nbt) {
        addTag(index, (T) nbt);
    }

    public void addTagUnsafe(NBT nbt) {
        addTag((T) nbt);
    }

    public void removeTag(int index) {
        tags.remove(index);
    }

    protected void validateAddTag(T tag) {
        if (type != tag.getType()) {
            throw new IllegalArgumentException(MessageFormat.format("Invalid tag type. Expected {0}, got {1}.", type.getNBTClass(), tag.getClass()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NBTList<T> other = (NBTList<T>) obj;
        return Objects.equals(type, other.type) && Objects.equals(tags, other.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, tags);
    }

    @Override
    public NBTList<T> copy() {
        List<T> newTags = new ArrayList<>();
        for (T tag : this.tags) {
            newTags.add((T) tag.copy());
        }
        return new NBTList<>(type, newTags);
    }

    @Override
    public String toString() {
        return "List(" + tags + ")";
    }
}
