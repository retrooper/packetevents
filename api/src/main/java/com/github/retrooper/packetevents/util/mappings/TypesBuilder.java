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

import com.github.retrooper.packetevents.protocol.nbt.*;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.VersionMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TypesBuilder {
    private final String mapPath;
    private Map<ClientVersion, Map<String, Integer>> entries = new HashMap<>();
    private VersionMapper versionMapper;

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
        final NBTCompound compound = MappingHelper.decompress("mappings/" + mapPath);
        final ClientVersion start = ClientVersion.valueOf(compound.getStringTagValueOrThrow("start"));
        final NBTCompound entries = compound.getCompoundTagOrThrow("entries");

        final ClientVersion[] versions = new ClientVersion[entries.size()];
        int index = 0;
        for (final String name : entries.getTagNames()) {
            versions[index] = ClientVersion.valueOf(name);
            index++;
        }

        this.versionMapper = new VersionMapper(versions);

        if (entries.getTagOrThrow(start.name()).getType().equals(NBTType.LIST)) {
            loadAsArray(start, entries, versions);
        } else {
            loadAsMap(start, entries, versions);
        }
    }

    private void loadAsArray(final ClientVersion start, final NBTCompound entries, final ClientVersion[] versions) {
        final NBTList<NBTString> lastEntries = entries.getStringListTagOrThrow(start.name());

        final Consumer<ClientVersion> mapLoader = version -> {
            final Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < lastEntries.size(); i++) {
                map.put(lastEntries.getTag(i).getValue(), i);
            }
            this.entries.put(version, map);
        };
        mapLoader.accept(start);

        for (int i = 1; i < versions.length; i++) {
            final ClientVersion version = versions[i];
            final IndexedDiff<NBT>[] diff = MappingHelper.createIndexDiff(entries.getCompoundTagOrThrow(version.name()));

            MappingHelper.applyDiff(lastEntries, diff);
            mapLoader.accept(version);
        }
    }

    private void loadAsMap(final ClientVersion start, final NBTCompound entries, final ClientVersion[] versions) {
        final NBTCompound lastEntries = entries.getCompoundTagOrThrow(start.name());

        final Consumer<ClientVersion> mapLoader = version -> {
            final Map<String, Integer> map = new HashMap<>();
            for (final Map.Entry<String, NBT> entry : lastEntries.getTags().entrySet()) {
                map.put(entry.getKey(), ((NBTInt) entry.getValue()).getAsInt());
            }
            this.entries.put(version, map);
        };
        mapLoader.accept(start);

        for (int i = 1; i < versions.length; i++) {
            final ClientVersion version = versions[i];
            final Diff<Map.Entry<String, NBT>>[] diff = MappingHelper.createDiff(entries.getCompoundTagOrThrow(version.name()));

            MappingHelper.applyDiff(lastEntries, diff);
            mapLoader.accept(version);
        }
    }

    public ClientVersion[] getVersions() {
        return versionMapper.getVersions();
    }

    public ClientVersion[] getReversedVersions() {
        return versionMapper.getReversedVersions();
    }

    public int getDataIndex(ClientVersion rawVersion) {
        return versionMapper.getIndex(rawVersion);
    }

    public void unloadFileMappings() {
        entries.clear();
        entries = null;
    }

    public TypesBuilderData define(String key) {
        final ResourceLocation name = new ResourceLocation(key);
        final int[] ids = new int[getVersions().length];
        int index = 0;
        for (ClientVersion v : getVersions()) {
            final Map<String, Integer> map = entries.get(v);
            if (map.containsKey(key)) {
                int id = map.get(key);
                ids[index] = id;
            } else {
                ids[index] = -1;
            }
            index++;
        }
        return new TypesBuilderData(name, ids);
    }
}
