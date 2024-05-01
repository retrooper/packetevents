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

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class TypesBuilder {
    private final String mapPath;
    private JsonObject fileMappings;
    private final VersionMapper versionMapper;

    public TypesBuilder(String mapPath,
                        ClientVersion... versions) {
        this.mapPath = mapPath;
        this.versionMapper = new VersionMapper(versions);
    }

    public <T extends MappedEntity> void register(
            Map<String, T> typeMap, Map<Byte, Map<Integer, T>> typeIdMap, T type) {
        typeMap.put(type.getName().toString(), type);
        for (ClientVersion version : this.getVersions()) {
            int index = this.getDataIndex(version);
            Map<Integer, T> idMap = typeIdMap.computeIfAbsent(
                    (byte) index, k -> new HashMap<>());
            idMap.put(type.getId(version), type);
        }
    }

    public int getId(ClientVersion version, TypesBuilderData data) {
        return data.getData()[this.getDataIndex(version)];
    }

    public JsonObject getFileMappings() {
        return fileMappings;
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
        fileMappings = null;
    }

    public TypesBuilderData defineFromArray(String key) {
        if (fileMappings == null) {
            fileMappings = MappingHelper.getJSONObject(mapPath);
        }
        ResourceLocation name = new ResourceLocation(key);
        int[] ids = new int[getVersions().length];
        int index = 0;
        for (ClientVersion v : getVersions()) {
            JsonArray array = fileMappings.getAsJsonArray(v.name());
            int tempId = 0;
            for (JsonElement element : array) {
                if (element.isJsonPrimitive()) {
                    String elementString = element.getAsString();
                    if (elementString.equals(key)) {
                        ids[index] = tempId;
                        break;
                    }
                    tempId++;
                }
            }
            index++;
        }
        return new TypesBuilderData(name, ids);
    }

    public TypesBuilderData define(String key) {
        if (fileMappings == null) {
            fileMappings = MappingHelper.getJSONObject(mapPath);
        }
        ResourceLocation name = new ResourceLocation(key);
        int[] ids = new int[getVersions().length];
        int index = 0;
        for (ClientVersion v : getVersions()) {
            if (fileMappings.has(v.name())) {
                JsonObject jsonMap = fileMappings.getAsJsonObject(v.name());
                if (jsonMap.has(key)) {
                    int id = jsonMap.get(key).getAsInt();
                    ids[index] = id;
                } else {
                    ids[index] = -1;
                }
            }
            index++;
        }
        return new TypesBuilderData(name, ids);
    }
}
