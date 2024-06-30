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

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
            try {
                versions[index] = ClientVersion.valueOf(name);
                index++;
            }
            catch(IllegalArgumentException ex) {
                throw new RuntimeException("Issue found in PacketEvents " + mapPath + " mappings. '" + name + "' is not a unique protocol release version! (It might just be a minecraft release version, not necessarily a unique protocol version)");
            }
        }

        this.versionMapper = new VersionMapper(versions);

        if (entries.getTagOrThrow(start.name()).getType().equals(NBTType.LIST)) {
            loadAsArray(start, entries, versions);
        } else {
            loadAsMap(start, entries, versions);
        }
    }

    private void loadAsArray(final ClientVersion start, final NBTCompound entries, final ClientVersion[] versions) {
        final List<String> lastEntries = entries.getStringListTagOrThrow(start.name()).getTags().stream().map(NBTString::getValue).collect(Collectors.toList());

        final Consumer<ClientVersion> mapLoader = version -> {
            final Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < lastEntries.size(); i++) {
                map.put(lastEntries.get(i), i);
            }
            this.entries.put(version, map);
        };
        mapLoader.accept(start);

        for (int i = 1; i < versions.length; i++) {
            final ClientVersion version = versions[i];
            final ListDiff<String>[] diff = MappingHelper.createListDiff(entries.getCompoundTagOrThrow(version.name()));

            for (int j = diff.length - 1; j >= 0; j--) {
                diff[j].applyTo(lastEntries);
            }
            mapLoader.accept(version);
        }
    }

    private void loadAsMap(final ClientVersion start, final NBTCompound entries, final ClientVersion[] versions) {
        final Map<String, Integer> lastEntries = entries.getCompoundTagOrThrow(start.name()).getTags().entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), ((NBTNumber) entry.getValue()).getAsInt()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        final Consumer<ClientVersion> mapLoader = version -> {
            final Map<String, Integer> map = new HashMap<>(lastEntries);
            this.entries.put(version, map);
        };
        mapLoader.accept(start);

        for (int i = 1; i < versions.length; i++) {
            final ClientVersion version = versions[i];
            final MapDiff<String, Integer>[] diff = MappingHelper.createDiff(entries.getCompoundTagOrThrow(version.name()));

            for (MapDiff<String, Integer> d : diff) {
                d.applyTo(lastEntries);
            }
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
