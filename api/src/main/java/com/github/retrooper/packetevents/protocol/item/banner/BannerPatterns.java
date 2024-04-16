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

package com.github.retrooper.packetevents.protocol.item.banner;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.TypesBuilder;
import com.github.retrooper.packetevents.util.TypesBuilderData;

import java.util.HashMap;
import java.util.Map;

public class BannerPatterns {

    private static final Map<String, BannerPattern> PATTERN_TYPE_MAP = new HashMap<>();
    private static final Map<Byte, Map<Integer, BannerPattern>> PATTERN_TYPE_ID_MAP = new HashMap<>();
    private static final TypesBuilder TYPES_BUILDER = new TypesBuilder("item/item_banner_pattern_mappings",
            ClientVersion.V_1_20_5);

    public static BannerPattern define(String key, ResourceLocation assetId, String translationKey) {
        TypesBuilderData data = TYPES_BUILDER.defineFromArray(key);
        BannerPattern type = new BannerPattern() {
            private final int[] ids = data.getData();

            @Override
            public ResourceLocation getAssetId() {
                return assetId;
            }

            @Override
            public String getTranslationKey() {
                return translationKey;
            }

            @Override
            public ResourceLocation getName() {
                return data.getName();
            }

            @Override
            public int getId(ClientVersion version) {
                int index = TYPES_BUILDER.getDataIndex(version);
                return this.ids[index];
            }
        };

        PATTERN_TYPE_MAP.put(type.getName().toString(), type);
        for (ClientVersion version : TYPES_BUILDER.getVersions()) {
            int index = TYPES_BUILDER.getDataIndex(version);
            Map<Integer, BannerPattern> idMap = PATTERN_TYPE_ID_MAP.computeIfAbsent(
                    (byte) index, k -> new HashMap<>());
            idMap.put(type.getId(version), type);
        }
        return type;
    }

    // with key
    public static BannerPattern getByName(String name) {
        return PATTERN_TYPE_MAP.get(name);
    }

    public static BannerPattern getById(ClientVersion version, int id) {
        int index = TYPES_BUILDER.getDataIndex(version);
        Map<Integer, BannerPattern> idMap = PATTERN_TYPE_ID_MAP.get((byte) index);
        return idMap.get(id);
    }

    static {
        TYPES_BUILDER.unloadFileMappings();
    }
}
