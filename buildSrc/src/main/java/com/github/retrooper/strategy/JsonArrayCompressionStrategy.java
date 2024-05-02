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

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffAlgorithmI;
import com.github.difflib.algorithm.myers.MeyersDiff;
import com.github.difflib.patch.*;
import com.github.retrooper.CompressionUtil;
import com.github.retrooper.EntryVersion;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class JsonArrayCompressionStrategy extends JsonCompressionStrategy {

    public static final JsonArrayCompressionStrategy INSTANCE = new JsonArrayCompressionStrategy();

    private JsonArrayCompressionStrategy() {
    }

    @Override
    void serialize(final CompoundTag tag, final JsonObject json) {
        final TreeMap<EntryVersion, JsonElement> entries = separateVersions(json);
        // we will remove the first entry, which is the oldest version
        final Map.Entry<EntryVersion, JsonElement> oldestVersion = entries.pollFirstEntry();
        tag.put("start", new StringTag(oldestVersion.getKey().toString()));

        final CompoundTag nbtEntries = new CompoundTag();
        List<String> ent = CompressionUtil.getArrayEntriesAsList(oldestVersion.getValue().getAsJsonArray());

        nbtEntries.put(oldestVersion.getKey().toString(), new ListTag(ent.stream().map(StringTag::new).collect(Collectors.toList())));

        for (final Map.Entry<EntryVersion, JsonElement> e : entries.entrySet()) {
            final List<String> list = CompressionUtil.getArrayEntriesAsList(e.getValue().getAsJsonArray());
            final DiffAlgorithmI<String> algo = MeyersDiff.factory().create(String::equals);
            final Patch<String> diff = DiffUtils.diff(ent, list, algo, null, false);

            final CompoundTag versionTag = new CompoundTag();
            final ListTag additions = new ListTag(CompoundTag.class);
            final ListTag removals = new ListTag(CompoundTag.class);
            final ListTag changes = new ListTag(CompoundTag.class);
            for (AbstractDelta<String> delta : diff.getDeltas()) {
                if (delta instanceof InsertDelta) {
                    final CompoundTag addition = new CompoundTag();
                    addition.put("pos", new IntTag(delta.getSource().getPosition()));
                    addition.put("lines", new ListTag(delta.getTarget().getLines().stream().map(StringTag::new).collect(Collectors.toList())));
                    additions.add(addition);
                } else if (delta instanceof DeleteDelta) {
                    final CompoundTag removal = new CompoundTag();
                    removal.put("pos", new IntTag(delta.getSource().getPosition()));
                    removal.put("size", new IntTag(delta.getSource().size()));
                    removals.add(removal);
                } else if (delta instanceof ChangeDelta) {
                    final CompoundTag change = new CompoundTag();
                    change.put("pos", new IntTag(delta.getSource().getPosition()));
                    change.put("size", new IntTag(delta.getSource().size()));
                    change.put("lines", new ListTag(delta.getTarget().getLines().stream().map(StringTag::new).collect(Collectors.toList())));
                    changes.add(change);
                }
            }

            versionTag.put("additions", additions);
            versionTag.put("removals", removals);
            versionTag.put("changes", changes);
            nbtEntries.put(e.getKey().toString(), versionTag);

            ent = list;
        }

        tag.put("entries", nbtEntries);
    }

}
