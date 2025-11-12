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

import com.github.retrooper.packetevents.protocol.util.NbtDecoder;
import com.github.retrooper.packetevents.protocol.util.NbtEncoder;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class NBTCompound extends NBT {

    protected final Map<String, NBT> tags = new LinkedHashMap<>();

    @Override
    public NBTType<NBTCompound> getType() {
        return NBTType.COMPOUND;
    }

    public boolean isEmpty() {
        return tags.isEmpty();
    }

    public boolean contains(String key) {
        return this.tags.containsKey(key);
    }

    public Set<String> getTagNames() {
        return Collections.unmodifiableSet(tags.keySet());
    }

    public Map<String, NBT> getTags() {
        return Collections.unmodifiableMap(tags);
    }

    public int size() {
        return tags.size();
    }

    public NBT getTagOrThrow(String key) {
        NBT tag = getTagOrNull(key);
        if (tag == null) {
            throw new IllegalStateException(MessageFormat.format("NBT {0} does not exist", key));
        }
        return tag;
    }

    public @Nullable NBT getTagOrNull(String key) {
        return tags.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T extends NBT> T getTagOfTypeOrThrow(String key, Class<T> type) {
        NBT tag = getTagOrThrow(key);
        if (type.isInstance(tag)) {
            return (T) tag;
        } else {
            throw new IllegalStateException(MessageFormat.format("NBT {0} has unexpected type, expected {1}, but got {2}", key, type, tag.getClass()));
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends NBT> @Nullable T getTagOfTypeOrNull(String key, Class<T> type) {
        NBT tag = getTagOrNull(key);
        if (type.isInstance(tag)) {
            return (T) tag;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends NBT> NBTList<T> getTagListOfTypeOrThrow(String key, Class<T> type) {
        NBTList<? extends NBT> list = getTagOfTypeOrThrow(key, NBTList.class);
        if (!type.isAssignableFrom(list.getTagsType().getNBTClass())) {
            throw new IllegalStateException(MessageFormat.format("NBTList {0} tags type has unexpected type, expected {1}, but got {2}", key, type, list.getTagsType().getNBTClass()));
        }
        return (NBTList<T>) list;
    }

    @SuppressWarnings("unchecked")
    public <T extends NBT> @Nullable NBTList<T> getTagListOfTypeOrNull(String key, Class<T> type) {
        NBTList<? extends NBT> list = getTagOfTypeOrNull(key, NBTList.class);
        if ((list != null) && type.isAssignableFrom(list.getTagsType().getNBTClass())) {
            return (NBTList<T>) list;
        }
        return null;
    }

    public NBTCompound getCompoundTagOrThrow(String key) {
        return getTagOfTypeOrThrow(key, NBTCompound.class);
    }

    public @Nullable NBTCompound getCompoundTagOrNull(String key) {
        return getTagOfTypeOrNull(key, NBTCompound.class);
    }

    public Number getNumberTagValueOrThrow(String key) {
        return this.getNumberTagOrThrow(key).getAsNumber();
    }

    public @Nullable Number getNumberTagValueOrNull(String key) {
        return this.getNumberTagValueOrDefault(key, null);
    }

    @Contract("_, !null -> !null")
    public @Nullable Number getNumberTagValueOrDefault(String key, @Nullable Number number) {
        NBTNumber tag = this.getNumberTagOrNull(key);
        return tag != null ? tag.getAsNumber() : number;
    }

    public NBTNumber getNumberTagOrThrow(String key) {
        return getTagOfTypeOrThrow(key, NBTNumber.class);
    }

    public @Nullable NBTNumber getNumberTagOrNull(String key) {
        return getTagOfTypeOrNull(key, NBTNumber.class);
    }

    public NBTString getStringTagOrThrow(String key) {
        return getTagOfTypeOrThrow(key, NBTString.class);
    }

    public @Nullable NBTString getStringTagOrNull(String key) {
        return getTagOfTypeOrNull(key, NBTString.class);
    }

    public NBTList<NBTCompound> getCompoundListTagOrThrow(String key) {
        return getTagListOfTypeOrThrow(key, NBTCompound.class);
    }

    public @Nullable NBTList<NBTCompound> getCompoundListTagOrNull(String key) {
        return getTagListOfTypeOrNull(key, NBTCompound.class);
    }

    public NBTList<NBTNumber> getNumberTagListTagOrThrow(String key) {
        return getTagListOfTypeOrThrow(key, NBTNumber.class);
    }

    public @Nullable NBTList<NBTNumber> getNumberListTagOrNull(String key) {
        return getTagListOfTypeOrNull(key, NBTNumber.class);
    }

    public NBTList<NBTString> getStringListTagOrThrow(String key) {
        return getTagListOfTypeOrThrow(key, NBTString.class);
    }

    public @Nullable NBTList<NBTString> getStringListTagOrNull(String key) {
        return getTagListOfTypeOrNull(key, NBTString.class);
    }

    public String getStringTagValueOrThrow(String key) {
        return getStringTagOrThrow(key).getValue();
    }

    public @Nullable String getStringTagValueOrNull(String key) {
        NBT tag = getTagOrNull(key);
        if (tag instanceof NBTString) {
            return ((NBTString) tag).getValue();
        }
        return null;
    }

    public String getStringTagValueOrDefault(String key, String defaultValue) {
        NBT tag = getTagOrNull(key);
        if (tag instanceof NBTString) {
            return ((NBTString) tag).getValue();
        }
        return defaultValue;
    }

    public NBT removeTag(String key) {
        return tags.remove(key);
    }

    @SuppressWarnings("unchecked")
    public <T extends NBT> T removeTagAndReturnIfType(String key, Class<T> type) {
        NBT tag = removeTag(key);
        if (type.isInstance(tag)) {
            return (T) tag;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends NBT> NBTList<T> removeTagAndReturnIfListType(String key, Class<T> type) {
        NBTList<?> list = removeTagAndReturnIfType(key, NBTList.class);
        if ((list != null) && type.isAssignableFrom(list.getTagsType().getNBTClass())) {
            return (NBTList<T>) list;
        }
        return null;
    }

    public void setTag(String key, NBT tag) {
        if (tag != null) {
            tags.put(key, tag);
        } else {
            tags.remove(key);
        }
    }

    public NBTCompound copy() {
        NBTCompound clone = new NBTCompound();
        for (Map.Entry<String, NBT> entry : tags.entrySet()) {
            clone.setTag(entry.getKey(), entry.getValue().copy());
        }
        return clone;
    }

    public boolean getBoolean(String string) {
        return this.getBooleanOr(string, false);
    }

    public boolean getBooleanOr(String string, boolean defaultValue) {
        NBTNumber nbtByte = this.getTagOfTypeOrNull(string, NBTNumber.class);
        return nbtByte != null ? nbtByte.getAsByte() != 0 : defaultValue;
    }

    @Contract("_, _, !null, _ -> !null")
    public <T> @Nullable T getOr(String key, NbtDecoder<T> decoder, @Nullable T def, PacketWrapper<?> wrapper) {
        NBT tag = this.getTagOrNull(key);
        return tag != null ? decoder.decode(tag, wrapper) : def;
    }

    @Contract("_, _, !null, _ -> !null")
    public <T> @Nullable T getOrSupply(String key, NbtDecoder<T> decoder, Supplier<@Nullable T> def, PacketWrapper<?> wrapper) {
        NBT tag = this.getTagOrNull(key);
        return tag != null ? decoder.decode(tag, wrapper) : def.get();
    }

    public <T> @Nullable T getOrNull(String key, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
        return this.getOr(key, decoder, null, wrapper);
    }

    public <T> T getOrThrow(String key, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
        return decoder.decode(this.getTagOrThrow(key), wrapper);
    }

    @Contract("_, _, !null, _ -> !null")
    public <T> @Nullable List<T> getListOr(String key, NbtDecoder<T> decoder, @Nullable List<T> def, PacketWrapper<?> wrapper) {
        NBT tag = this.getTagOrNull(key);
        if (tag instanceof NBTList) {
            // entries in list format
            List<? extends NBT> tags = ((NBTList<?>) tag).getTags();
            List<T> list = new ArrayList<>(tags.size());
            for (NBT element : tags) {
                list.add(decoder.decode(element, wrapper));
            }
            return list;
        } else if (tag != null) {
            // single entry
            List<T> list = new ArrayList<>(1);
            list.add(decoder.decode(tag, wrapper));
            return list;
        } else {
            // null, return default
            return def;
        }
    }

    public <T> @Nullable List<T> getListOrNull(String key, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
        return this.getListOr(key, decoder, null, wrapper);
    }

    public <T> List<T> getListOrEmpty(String key, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
        return this.getListOr(key, decoder, Collections.emptyList(), wrapper);
    }

    public <T> List<T> getListOrThrow(String key, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
        List<T> list = this.getListOrNull(key, decoder, wrapper);
        if (list == null) {
            throw new IllegalStateException(MessageFormat.format("NBT {0} does not exist", key));
        }
        return list;
    }

    public <T> void set(String key, T value, NbtEncoder<T> encoder, PacketWrapper<?> wrapper) {
        this.setTag(key, encoder.encode(wrapper, value));
    }

    public <T> void setList(String key, List<T> value, NbtEncoder<T> encoder, PacketWrapper<?> wrapper) {
        if (value.isEmpty()) {
            this.setTag(key, new NBTList<>(NBTType.END, 0));
        } else {
            // determine list type using first value in list
            NBT firstVal = encoder.encode(wrapper, value.get(0));
            NBTList<?> list = new NBTList<>(firstVal.getType(), value.size());
            list.addTagUnsafe(firstVal);
            // add remaining list entries
            for (int i = 1; i < value.size(); i++) {
                list.addTagUnsafe(encoder.encode(wrapper, value.get(i)));
            }
            this.setTag(key, list);
        }
    }

    public <T> void setCompactList(String key, List<T> value, NbtEncoder<T> encoder, PacketWrapper<?> wrapper) {
        if (value.size() == 1) {
            this.set(key, value.get(0), encoder, wrapper);
        } else {
            this.setList(key, value, encoder, wrapper);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof NBTCompound) {
            if (isEmpty() && ((NBTCompound) other).isEmpty()) {
                return true;
            }
            return tags.equals(((NBTCompound) other).tags);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return tags.hashCode();
    }

    @Override
    public String toString() {
        return "Compound{" + tags + "}";
    }
}
