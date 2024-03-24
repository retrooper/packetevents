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

package com.github.retrooper.strategy;

import com.github.retrooper.CompressionUtil;
import com.github.retrooper.EntryVersion;
import com.github.retrooper.diff.Diff;
import com.github.steveice10.opennbt.tag.builtin.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JsonObjectCompressionStrategy extends JsonCompressionStrategy {

    public static final JsonObjectCompressionStrategy INSTANCE = new JsonObjectCompressionStrategy();

    private JsonObjectCompressionStrategy() {
    }

    @Override
    void serialize(final CompoundTag tag, final JsonObject json) {
        final TreeMap<EntryVersion, JsonElement> entries = separateVersions(json);
        // we will remove the first entry, which is the oldest version
        final Map.Entry<EntryVersion, JsonElement> oldestVersion = entries.pollFirstEntry();
        tag.put("start", new StringTag(oldestVersion.getKey().toString()));

        final CompoundTag nbtEntries = new CompoundTag();
        Map<String, JsonPrimitive> ent = CompressionUtil.getObjectEntriesAsMap(oldestVersion.getValue().getAsJsonObject());

        final CompoundTag oldestVersionTag = new CompoundTag();
        for (final Map.Entry<String, JsonPrimitive> e : ent.entrySet()) {
            oldestVersionTag.put(e.getKey(), getAsNbtTag(e.getValue()));
        }
        nbtEntries.put(oldestVersion.getKey().toString(), oldestVersionTag);

        for (Map.Entry<EntryVersion, JsonElement> e : entries.entrySet()) {
            final Map<String, JsonPrimitive> map = CompressionUtil.getObjectEntriesAsMap(e.getValue().getAsJsonObject());
            final List<Diff<Map.Entry<String, JsonPrimitive>>> diff = CompressionUtil.getDiff(ent, map);

            final CompoundTag versionTag = new CompoundTag();
            final CompoundTag additions = new CompoundTag();
            final CompoundTag removals = new CompoundTag();
            final CompoundTag changes = new CompoundTag();
            for (final Diff<Map.Entry<String, JsonPrimitive>> d : diff) {
                if (d instanceof Diff.Addition) {
                    additions.put(d.getValue().getKey(), getAsNbtTag(d.getValue().getValue()));
                } else if (d instanceof Diff.Removal) {
                    removals.put(d.getValue().getKey(), getAsNbtTag(d.getValue().getValue()));
                } else if (d instanceof Diff.Changed) {
                    final Diff.Changed<Map.Entry<String, JsonPrimitive>> changed = (Diff.Changed<Map.Entry<String, JsonPrimitive>>) d;
                    changes.put(d.getValue().getKey(), getAsNbtTag(changed.getOldValue().getValue(), changed.getValue().getValue()));
                }
            }

            versionTag.put("additions", additions);
            versionTag.put("removals", removals);
            versionTag.put("changes", changes);
            nbtEntries.put(e.getKey().toString(), versionTag);

            ent = map;
        }

        tag.put("entries", nbtEntries);
    }

    private Tag getAsNbtTag(final JsonPrimitive primitive) {
        // Will only be string or int as of the mappings
        if (primitive.isNumber()) {
            return new IntTag(primitive.getAsInt());
        } else if (primitive.isString()) {
            return new StringTag(primitive.getAsString());
        } else {
            throw new IllegalArgumentException("Unknown primitive type: " + primitive);
        }
    }

    private Tag getAsNbtTag(final JsonPrimitive oldValue, final JsonPrimitive newValue) {
        // Will only be string or int as of the mappings
        if (oldValue.isNumber()) {
            return new IntArrayTag(new int[]{oldValue.getAsInt(), newValue.getAsInt()});
        } else if (oldValue.isString()) {
            return new ListTag(Arrays.asList(new StringTag(oldValue.getAsString()), new StringTag(newValue.getAsString())));
        } else {
            throw new IllegalArgumentException("Unknown primitive type: " + oldValue);
        }
    }

}
