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

package com.github.retrooper.packetevents.util.mappings;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.nbt.serializer.SequentialNBTReader;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.zip.GZIPInputStream;

public class MappingHelper {

    public static SequentialNBTReader.Compound decompress(final String path) {
        try {
            final DataInputStream dataInput = new DataInputStream(new GZIPInputStream(new BufferedInputStream(
                    PacketEvents.getAPI().getSettings().getResourceProvider().apply( "assets/" + path + ".nbt"))));
            return (SequentialNBTReader.Compound) new SequentialNBTReader(dataInput).next(); // All mappings only contain one compound tag
        } catch (IOException e) {
            throw new RuntimeException("Cannot find resource file " + path + ".nbt", e);
        }
    }

    public static List<ListDiff<String>> createListDiff(final SequentialNBTReader.Compound compound) {
        final SequentialNBTReader.List additions = (SequentialNBTReader.List) compound.next().getValue(); // First tag is the additions
        final SequentialNBTReader.List removals = (SequentialNBTReader.List) compound.next().getValue(); // Second tag is the removals
        final SequentialNBTReader.List changes = (SequentialNBTReader.List) compound.next().getValue(); // Third tag is the changes

        final List<ListDiff<String>> diffs = new ArrayList<>();
        for (NBT entry : removals) {
            final SequentialNBTReader.Compound c = (SequentialNBTReader.Compound) entry;
            diffs.add(new ListDiff.Removal<>(
                    ((NBTNumber) c.next()).getAsInt(), // pos
                    ((NBTNumber) c.next()).getAsInt() // size
            ));
        }

        for (NBT entry : additions) {
            final SequentialNBTReader.Compound c = (SequentialNBTReader.Compound) entry;
            diffs.add(new ListDiff.Addition<>(
                    ((NBTNumber) c.next()).getAsInt(), // pos
                    StreamSupport.stream(((SequentialNBTReader.List) c.next()).spliterator(), false)
                            .map(nbt -> ((NBTString) nbt).getValue())
                            .collect(Collectors.toList()) // lines
            ));
        }

        for (NBT entry : changes) {
            final SequentialNBTReader.Compound c = (SequentialNBTReader.Compound) entry;
            diffs.add(new ListDiff.Changed<>(
                    ((NBTNumber) c.next()).getAsInt(), // pos
                    ((NBTNumber) c.next()).getAsInt(), // size
                    StreamSupport.stream(((SequentialNBTReader.List) c.next()).spliterator(), false)
                            .map(nbt -> ((NBTString) nbt).getValue())
                            .collect(Collectors.toList()) // lines
            ));
        }

        diffs.sort(Comparator.comparingInt(ListDiff::getIndex));

        return diffs;
    }

    public static List<MapDiff<String, Integer>> createDiff(final SequentialNBTReader.Compound compound) {
        final SequentialNBTReader.Compound additions = (SequentialNBTReader.Compound) compound.next().getValue(); // First tag is the additions
        final SequentialNBTReader.Compound removal = (SequentialNBTReader.Compound) compound.next().getValue(); // Second tag is the removals

        final List<MapDiff<String, Integer>> diffs = new ArrayList<>();
        for (Map.Entry<String, NBT> entry : removal) {
            diffs.add(new MapDiff.Removal<>(entry.getKey()));
        }

        for (Map.Entry<String, NBT> entry : additions) {
            diffs.add(new MapDiff.Addition<>(entry.getKey(), ((NBTNumber) entry.getValue()).getAsInt()));
        }

        return diffs;
    }

    public static  <T extends MappedEntity> void registerMapping(TypesBuilder builder, Map<String, T> typeMap, Map<Byte, Map<Integer, T>> typeIdMap, T type) {
        typeMap.put(type.getName().toString(), type);
        for (ClientVersion version : builder.getVersions()) {
            int index = builder.getDataIndex(version);
            Map<Integer, T> idMap = typeIdMap.computeIfAbsent((byte) index, k -> new HashMap<>());
            idMap.put(type.getId(version), type);
        }
    }

    public static int getId(ClientVersion version, TypesBuilder builder, TypesBuilderData data) {
        return data.getData()[builder.getDataIndex(version)];
    }
}
