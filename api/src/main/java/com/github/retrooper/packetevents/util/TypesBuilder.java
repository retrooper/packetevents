/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TypesBuilder {
    private final String mapPath;
    private JsonObject fileMappings;
    private final ClientVersion[] versions;
    private final ClientVersion[] reversedVersions;

    public TypesBuilder(String mapPath,
                        ClientVersion... versions) {
        this.mapPath = mapPath;
        this.versions = versions;
        reversedVersions = new ClientVersion[versions.length];
        int index = 0;
        for (int i = versions.length - 1; i >= 0; i--) {
            reversedVersions[index] = versions[i];
            index++;
        }
    }

    public JsonObject getFileMappings() {
        return fileMappings;
    }

    public ClientVersion[] getVersions() {
        return versions;
    }

    public ClientVersion[] getReversedVersions() {
        return reversedVersions;
    }

    public int getDataIndex(ClientVersion rawVersion) {
        int index = reversedVersions.length - 1;
        for (ClientVersion v : reversedVersions) {
            if (rawVersion.isNewerThanOrEquals(v)) {
                return index;
            }
            index--;
        }
        //Give them the oldest version
        return 0;
    }

    public TypesBuilderData defineFromArray(String key) {
        if (fileMappings == null) {
            fileMappings = MappingHelper.getJSONObject(mapPath);
        }
        ResourceLocation name = new ResourceLocation(key);
        int[] ids = new int[versions.length];
        int index = 0;
        for (ClientVersion v : versions) {
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
        int[] ids = new int[versions.length];
        int index = 0;
        for (ClientVersion v : versions) {
            if (fileMappings.has(v.name())) {
                JsonObject jsonMap = fileMappings.getAsJsonObject(v.name());
                if (jsonMap.has(key)) {
                    int id = jsonMap.get(key).getAsInt();
                    ids[index] = id;
                }
                else {
                    ids[index] = -1;
                }
            }
            index++;
        }
        return new TypesBuilderData(name, ids);
    }
}
