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
import com.github.steveice10.opennbt.tag.builtin.ByteTag;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

public abstract class JsonCompressionStrategy implements CompressionStrategy {

    public void compress(final Path from, final Path to) {
        final JsonObject json = CompressionUtil.loadJson(from);

        final CompoundTag tag = new CompoundTag();
        tag.put("version", new ByteTag(COMPRESSION_VERSION));

        serialize(tag, json);

        CompressionUtil.writeNbt(to, tag);
    }

    abstract void serialize(final CompoundTag tag, final JsonObject json);

    protected final TreeMap<EntryVersion, JsonElement> separateVersions(final JsonObject json) {
        final TreeMap<EntryVersion, JsonElement> entries = new TreeMap<>();
        for (final Map.Entry<String, JsonElement> e : json.entrySet()) {
            final EntryVersion version = EntryVersion.fromString(e.getKey());
            entries.put(version, e.getValue());
        }
        return entries;
    }

}
