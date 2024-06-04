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
import com.github.retrooper.packetevents.protocol.nbt.*;
import com.github.retrooper.packetevents.protocol.nbt.serializer.DefaultNBTSerializer;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class MappingHelper {

    public static NBTCompound decompress(final String path) {
        NBTLimiter limiter = new NBTLimiter();
        try (final DataInputStream dataInput = new DataInputStream(new GZIPInputStream(new BufferedInputStream(
                PacketEvents.getAPI().getSettings().getResourceProvider().apply( "assets/" + path + ".nbt"))))) {
            return (NBTCompound) DefaultNBTSerializer.INSTANCE.deserializeTag(limiter, dataInput);
        } catch (Exception e) {
            throw new RuntimeException("Cannot find resource file " + path + ".nbt", e);
        }
    }

    public static ListDiff<String>[] createListDiff(final NBTCompound compound) {
        final NBTList<NBTCompound> additions = compound.getCompoundListTagOrThrow("additions");
        final NBTList<NBTCompound> removals = compound.getCompoundListTagOrThrow("removals");
        final NBTList<NBTCompound> changes = compound.getCompoundListTagOrThrow("changes");

        final ListDiff<String>[] diffs = new ListDiff[additions.size() + removals.size() + changes.size()];
        int index = 0;
        for (NBTCompound entry : removals.getTags()) {
            diffs[index++] = new ListDiff.Removal<>(
                    entry.getNumberTagOrThrow("pos").getAsInt(),
                    entry.getNumberTagOrThrow("size").getAsInt()
            );
        }

        for (NBTCompound entry : additions.getTags()) {
            diffs[index++] = new ListDiff.Addition<>(
                    entry.getNumberTagOrThrow("pos").getAsInt(),
                    entry.getStringListTagOrThrow("lines").getTags().stream().map(NBTString::getValue).collect(Collectors.toList())
            );
        }

        for (NBTCompound entry : changes.getTags()) {
            diffs[index++] = new ListDiff.Changed<>(
                    entry.getNumberTagOrThrow("pos").getAsInt(),
                    entry.getNumberTagOrThrow("size").getAsInt(),
                    entry.getStringListTagOrThrow("lines").getTags().stream().map(NBTString::getValue).collect(Collectors.toList())
            );
        }

        Arrays.sort(diffs, Comparator.comparingInt(ListDiff::getIndex));

        return diffs;
    }

    public static MapDiff<String, Integer>[] createDiff(final NBTCompound compound) {
        final NBTCompound additions = compound.getCompoundTagOrThrow("additions");
        final NBTCompound removal = compound.getCompoundTagOrThrow("removals");

        final MapDiff<String, Integer>[] diffs = new MapDiff[additions.size() + removal.size()];
        int index = 0;
        for (Map.Entry<String, NBT> entry : removal.getTags().entrySet()) {
            diffs[index++] = new MapDiff.Removal<>(entry.getKey());
        }

        for (Map.Entry<String, NBT> entry : additions.getTags().entrySet()) {
            diffs[index++] = new MapDiff.Addition<>(entry.getKey(), ((NBTNumber) entry.getValue()).getAsInt());
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
