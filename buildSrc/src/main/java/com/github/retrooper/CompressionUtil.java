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

import com.github.retrooper.diff.Diff;
import com.github.retrooper.diff.IndexedDiff;
import com.github.steveice10.opennbt.tag.builtin.*;
import com.github.steveice10.opennbt.tag.io.NBTIO;
import com.github.steveice10.opennbt.tag.io.TagWriter;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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

    /**
     * Returns a list of differences between the two lists.
     * @param last List to compare to
     * @param current List to get differences from
     * @return List of differences
     * @param <T> Type of the list
     */
    public static <T> List<IndexedDiff<T>> getDiff(final List<T> last, final List<T> current) {
        final List<IndexedDiff<T>> diff = new ArrayList<>();

        final List<T> copy = new ArrayList<>(current);
        for (int i = 0; i < last.size(); i++) {
            if (copy.isEmpty()) {
                diff.add(new IndexedDiff.Removal<>(i, last.get(i)));
                continue;
            }

            final T value = copy.remove(0);
            if (!value.equals(last.get(i))) {
                diff.add(new IndexedDiff.Changed<>(i, value, last.get(i)));
            }
        }

        for (int i = 0; i < copy.size(); i++) {
            diff.add(new IndexedDiff.Addition<>(i, copy.get(i)));
        }

        return diff;
    }

    /**
     * Returns a list of differences between the two maps.
     * @param last Map to compare to
     * @param current Map to get differences from
     * @return List of differences
     * @param <K> Type of the key
     * @param <V> Type of the value
     */
    public static <K, V> List<Diff<Map.Entry<K, V>>> getDiff(final Map<K, V> last, final Map<K, V> current) {
        final List<Diff<Map.Entry<K, V>>> diff = new ArrayList<>();

        final Map<K, V> copy = new LinkedHashMap<>(current);
        for (final Map.Entry<K, V> e : last.entrySet()) {
            final V value = copy.remove(e.getKey());
            if (value == null) {
                diff.add(new Diff.Removal<>(e));
            } else if (!value.equals(e.getValue())) {
                diff.add(new Diff.Changed<>(e, new AbstractMap.SimpleEntry<>(e.getKey(), value)));
            }
        }

        for (final Map.Entry<K, V> e : copy.entrySet()) {
            diff.add(new Diff.Addition<>(e));
        }

        return diff;
    }

}
