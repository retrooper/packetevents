/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.strategy;

import com.github.steveice10.opennbt.tag.builtin.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LazilyParsedNumber;

import java.util.Map;

public class JsonToNbtStrategy extends JsonCompressionStrategy {

    public static final JsonToNbtStrategy INSTANCE = new JsonToNbtStrategy();

    private JsonToNbtStrategy() {
    }

    @Override
    void serialize(final CompoundTag tag, final JsonObject json) {
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            final Tag nbt = getAsNbtTag(entry.getValue());
            if (nbt != null)
                tag.put(entry.getKey(), nbt);
        }
    }

    private Tag getAsNbtTag(final JsonElement element) {
        if (element.isJsonPrimitive()) {
            final JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isBoolean()) {
                return new ByteTag(primitive.getAsBoolean() ? (byte) 1 : (byte) 0);
            } else if (primitive.isNumber()) {
                return getAsNbtTag(primitive.getAsNumber());
            } else if (primitive.isString()) {
                return new StringTag(primitive.getAsString());
            }
        } else if (element.isJsonArray()) {
            final JsonArray array = element.getAsJsonArray();
            final ListTag listTag = new ListTag();
            for (JsonElement e : array) {
                final Tag nbt = getAsNbtTag(e);
                if (nbt != null)
                    listTag.add(nbt);
            }
            return listTag;
        } else if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            final CompoundTag compoundTag = new CompoundTag();
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                final Tag nbt = getAsNbtTag(entry.getValue());
                if (nbt != null)
                    compoundTag.put(entry.getKey(), nbt);
            }
            return compoundTag;
        } else if (element.isJsonNull()) {
            return new CompoundTag();
        }

        return null;
    }

    private Tag getAsNbtTag(final Number value) {
        if (value instanceof Double) {
            return new DoubleTag(value.doubleValue());
        } else if (value instanceof Float) {
            return new FloatTag(value.floatValue());
        } else if (value instanceof Long) {
            return new LongTag(value.longValue());
        } else if (value instanceof Integer ||value instanceof LazilyParsedNumber) {
            return new IntTag(value.intValue());
        } else if (value instanceof Short) {
            return new ShortTag(value.shortValue());
        } else if (value instanceof Byte) {
            return new ByteTag(value.byteValue());
        } else {
            throw new IllegalArgumentException("Unknown number type: " + value.getClass());
        }
    }

}
