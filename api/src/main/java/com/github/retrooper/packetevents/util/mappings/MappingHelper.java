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
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class MappingHelper {

    public static NBTCompound decompress(final String path) {
        try (final DataInputStream dataInput = new DataInputStream(new GZIPInputStream(new BufferedInputStream(
                PacketEvents.getAPI().getSettings().getResourceProvider().apply( "assets/" + path + ".nbt"))))) {
            return (NBTCompound) DefaultNBTSerializer.INSTANCE.deserializeTag(dataInput);
        } catch (Exception e) {
            throw new RuntimeException("Cannot find resource file " + path + ".nbt", e);
        }
    }

    public static <T extends NBT> void applyDiff(final NBTList<T> list, final IndexedDiff<NBT>[] diffs) {
        for (final IndexedDiff<NBT> diff : diffs) {
            if (diff instanceof IndexedDiff.Addition) {
                list.addTagUnsafe(diff.getIndex(), diff.getValue());
            } else if (diff instanceof IndexedDiff.Removal) {
                list.removeTag(diff.getIndex());
            }
        }
    }

    public static void applyDiff(final NBTCompound compound, final Diff<Map.Entry<String, NBT>>[] diffs) {
        for (final Diff<Map.Entry<String, NBT>> diff : diffs) {
            if (diff instanceof Diff.Addition) {
                compound.setTag(diff.getValue().getKey(), diff.getValue().getValue());
            } else if (diff instanceof Diff.Removal) {
                compound.removeTag(diff.getValue().getKey());
            } else if (diff instanceof Diff.Changed) {
                compound.setTag(diff.getValue().getKey(), diff.getValue().getValue());
            }
        }
    }

    public static IndexedDiff<NBT>[] createIndexDiff(final NBTCompound compound) {
        final NBTCompound removal = compound.getCompoundTagOrThrow("removals");
        final NBTCompound additions = compound.getCompoundTagOrThrow("additions");

        final IndexedDiff<NBT>[] diffs = new IndexedDiff[removal.size() + additions.size()];
        int index = 0;
        for (Map.Entry<String, NBT> entry : removal.getTags().entrySet()) {
            diffs[index++] = new IndexedDiff.Removal<>(Integer.parseInt(entry.getKey()), entry.getValue());
        }

        for (Map.Entry<String, NBT> entry : additions.getTags().entrySet()) {
            diffs[index++] = new IndexedDiff.Addition<>(Integer.parseInt(entry.getKey()), entry.getValue());
        }

        return diffs;
    }

    public static Diff<Map.Entry<String, NBT>>[] createDiff(final NBTCompound compound) {
        final NBTCompound removal = compound.getCompoundTagOrThrow("removals");
        final NBTCompound additions = compound.getCompoundTagOrThrow("additions");
        final NBTCompound changes = compound.getCompoundTagOrThrow("changes");

        final Diff<Map.Entry<String, NBT>>[] diffs = new Diff[removal.size() + additions.size() + changes.size()];
        int index = 0;
        for (Map.Entry<String, NBT> entry : removal.getTags().entrySet()) {
            diffs[index++] = new Diff.Removal<>(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
        }

        for (Map.Entry<String, NBT> entry : additions.getTags().entrySet()) {
            diffs[index++] = new Diff.Addition<>(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
        }

        for (Map.Entry<String, NBT> entry : changes.getTags().entrySet()) {
            final NBTIntArray c = (NBTIntArray) entry.getValue();
            diffs[index++] = new Diff.Changed<>(
                    new AbstractMap.SimpleEntry<>(entry.getKey(), new NBTInt(c.getValue()[0])),
                    new AbstractMap.SimpleEntry<>(entry.getKey(), new NBTInt(c.getValue()[1]))
            );
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
