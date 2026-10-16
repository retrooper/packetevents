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

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import com.github.retrooper.packetevents.protocol.nbt.serializer.SequentialNBTReader;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.VersionMapper;
import com.github.retrooper.packetevents.util.VersionRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ApiStatus.Internal
public class TypesBuilder {
    private final String mapPath;
    private @Nullable Map<String, int[]> idsByName = new HashMap<>();
    private int[] fileSlotByVersionOrdinal = new int[0];
    private int[] slotBySortedIndex = new int[0];
    private VersionMapper versionMapper;

    @Nullable
    VersionedRegistry<?> registry;

    public TypesBuilder(String mapPath, boolean lazy) {
        this.mapPath = mapPath;
        if (!lazy) {
            load();
        }
    }

    public TypesBuilder(String mapPath) {
        this(mapPath, false);
    }

    public void load() {
        if (this.idsByName == null) {
            this.idsByName = new HashMap<>();
        }
        try (final SequentialNBTReader.Compound rootCompound = MappingHelper.decompress("mappings/" + this.mapPath)) {
            rootCompound.skipOne(); // skip version tag for now
            SequentialNBTReader.Compound compound = (SequentialNBTReader.Compound) rootCompound.next().getValue();

            int length = ((NBTNumber) compound.next().getValue()).getAsInt(); // Second tag is the length
            final SequentialNBTReader.Compound entries = (SequentialNBTReader.Compound) compound.next().getValue(); // Third tag are the entries

            final ClientVersion[] versions = new ClientVersion[length];
            final Map.Entry<String, NBT> first = entries.next();
            if (first.getValue().getType() == NBTType.LIST) {
                loadAsArray(first, entries, versions);
            } else {
                loadAsMap(first, entries, versions);
            }

            this.fileSlotByVersionOrdinal = new int[ClientVersion.values().length];
            Arrays.fill(this.fileSlotByVersionOrdinal, -1);
            for (int slot = 0; slot < versions.length; slot++) {
                this.fileSlotByVersionOrdinal[versions[slot].ordinal()] = slot;
            }
            this.versionMapper = new VersionMapper(versions);
            this.rebuildSlotMapping();
        } catch (IOException e) {
            throw new RuntimeException("Unable to load mapping files.", e);
        }
    }

    private void loadAsArray(
            final Map.Entry<String, NBT> first,
            final SequentialNBTReader.Compound entries,
            final ClientVersion[] versions
    ) {
        final ClientVersion start = ClientVersion.valueOf(first.getKey());
        versions[0] = start;
        final List<String> lastEntries = new ArrayList<>();
        for (NBT entry : ((SequentialNBTReader.List) first.getValue())) {
            lastEntries.add(((NBTString) entry).getValue());
        }

        final int slots = versions.length;
        final IntConsumer slotLoader = slot -> {
            int size = lastEntries.size();
            for (int i = 0; i < size; i++) {
                this.idsFor(lastEntries.get(i), slots)[slot] = i;
            }
        };
        slotLoader.accept(0);

        int i = 1;
        for (Map.Entry<String, NBT> entry : entries) {
            final ClientVersion version = ClientVersion.valueOf(entry.getKey());
            versions[i] = version;
            final List<ListDiff<String>> diff = MappingHelper.createListDiff((SequentialNBTReader.Compound) entry.getValue());

            for (int j = diff.size() - 1; j >= 0; j--) {
                diff.get(j).applyTo(lastEntries);
            }
            slotLoader.accept(i++);
        }
    }

    private int[] idsFor(String name, int slots) {
        int[] ids = this.idsByName.get(name);
        if (ids == null) {
            ids = new int[slots];
            Arrays.fill(ids, -1);
            this.idsByName.put(name, ids);
        }
        return ids;
    }

    private void rebuildSlotMapping() {
        ClientVersion[] sorted = this.versionMapper.getVersions();
        int[] mapping = new int[sorted.length];
        for (int i = 0; i < sorted.length; i++) {
            int slot = this.fileSlotByVersionOrdinal[sorted[i].ordinal()];
            mapping[i] = slot != -1 ? slot : (i > 0 ? mapping[i - 1] : 0);
        }
        this.slotBySortedIndex = mapping;
    }

    private void loadAsMap(
            final Map.Entry<String, NBT> first,
            final SequentialNBTReader.Compound entries,
            final ClientVersion[] versions
    ) {
        final ClientVersion start = ClientVersion.valueOf(first.getKey());
        versions[0] = start;
        final Map<String, Integer> lastEntries = StreamSupport.stream(((SequentialNBTReader.Compound) first.getValue()).spliterator(), false)
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> ((NBTNumber) entry.getValue()).getAsInt()));

        final int slots = versions.length;
        final IntConsumer slotLoader = slot -> {
            for (Map.Entry<String, Integer> e : lastEntries.entrySet()) {
                this.idsFor(e.getKey(), slots)[slot] = e.getValue();
            }
        };
        slotLoader.accept(0);

        int i = 1;
        for (Map.Entry<String, NBT> entry : entries) {
            final ClientVersion version = ClientVersion.valueOf(entry.getKey());
            versions[i] = version;
            final List<MapDiff<String, Integer>> diff = MappingHelper.createDiff((SequentialNBTReader.Compound) entry.getValue());

            for (MapDiff<String, Integer> d : diff) {
                d.applyTo(lastEntries);
            }
            slotLoader.accept(i++);
        }
    }

    public @Nullable VersionedRegistry<?> getRegistry() {
        return this.registry;
    }

    public ClientVersion[] getVersions() {
        return versionMapper.getVersions();
    }

    public ClientVersion[] getReversedVersions() {
        return versionMapper.getReversedVersions();
    }

    public int getDataIndex(ClientVersion rawVersion) {
        return this.versionMapper.getIndex(rawVersion);
    }

    public VersionMapper getVersionMapper() {
        return this.versionMapper;
    }

    public void addExtraVersionStep(ClientVersion version) {
        VersionMapper newMapper = this.versionMapper.withExtra(version);
        if (this.versionMapper != newMapper) {
            this.versionMapper = newMapper; // save new mapper
            this.rebuildSlotMapping(); // the reads the nearest older version's slot
        }
    }

    @VisibleForTesting
    public boolean isMappingDataLoaded() {
        return this.idsByName != null;
    }

    public void unloadFileMappings() {
        if (this.idsByName != null) {
            this.idsByName.clear();
            this.idsByName = null;
        }
    }

    public TypesBuilderData define(String key, VersionRange range) {
        final ResourceLocation name = new ResourceLocation(key);
        final int[] slots = this.slotBySortedIndex;
        final int[] ids = new int[slots.length];
        final int[] fileIds = this.idsByName.get(key);
        if (fileIds == null) {
            Arrays.fill(ids, -1);
        } else {
            for (int i = 0; i < slots.length; i++) {
                ids[i] = fileIds[slots[i]];
            }
        }
        return new TypesBuilderData(name, ids, this, range);
    }

    public @Nullable Map<ClientVersion, Map<String, Integer>> getEntries() {
        if (this.idsByName == null) {
            return null;
        }
        ClientVersion[] sorted = this.versionMapper.getVersions();
        Map<ClientVersion, Map<String, Integer>> view = new HashMap<>(sorted.length * 2);
        for (int i = 0; i < sorted.length; i++) {
            Map<String, Integer> perVersion = new HashMap<>();
            int slot = this.slotBySortedIndex[i];
            for (Map.Entry<String, int[]> entry : this.idsByName.entrySet()) {
                int id = entry.getValue()[slot];
                if (id != -1) {
                    perVersion.put(entry.getKey(), id);
                }
            }
            view.put(sorted[i], perVersion);
        }
        return view;
    }
}
