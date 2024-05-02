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

package com.github.retrooper;

import com.github.steveice10.opennbt.tag.builtin.Tag;
import com.github.steveice10.opennbt.tag.io.NBTIO;
import com.github.steveice10.opennbt.tag.io.TagWriter;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class CompressionUtil {

    public static final Gson GSON = new Gson();
    public static final TagWriter TAG_WRITER = NBTIO.writer().named();

    private CompressionUtil() {
    }

    public static JsonObject loadJson(final Path path) {
        try (final BufferedReader reader = Files.newBufferedReader(path)) {
            return GSON.fromJson(reader, JsonObject.class);
        } catch (final IOException e) {
            throw new RuntimeException("Failed to load json file: " + path, e);
        }
    }

    public static void writeNbt(final Path path, final Tag tag) {
        try {
            TAG_WRITER.write(path, tag, true);
        } catch (final IOException e) {
            throw new RuntimeException("Failed to write nbt file: " + path, e);
        }
    }

    public static Map<String, JsonPrimitive> getObjectEntriesAsMap(final JsonObject object) {
        final Map<String, JsonPrimitive> sorted = new TreeMap<>();
        for (final Map.Entry<String, JsonElement> e : object.entrySet()) {
            if (e.getValue().isJsonPrimitive()) {
                sorted.put(e.getKey(), e.getValue().getAsJsonPrimitive());
            }
        }
        return sorted;
    }

    public static List<String> getArrayEntriesAsList(final JsonArray array) {
        final List<String> list = new ArrayList<>();
        for (final JsonElement e : array) {
            list.add(e.getAsString());
        }
        return list;
    }

}
