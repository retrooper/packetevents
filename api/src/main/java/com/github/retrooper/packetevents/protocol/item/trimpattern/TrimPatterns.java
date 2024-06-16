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

package com.github.retrooper.packetevents.protocol.item.trimpattern;

import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.MappingHelper;
import com.github.retrooper.packetevents.util.mappings.TypesBuilder;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.Map;

public class TrimPatterns {

    private static final Map<String, TrimPattern> PATTERN_TYPE_MAP = new HashMap<>();
    private static final Map<Byte, Map<Integer, TrimPattern>> PATTERN_TYPE_ID_MAP = new HashMap<>();
    private static final TypesBuilder TYPES_BUILDER = new TypesBuilder("item/item_trim_pattern_mappings");

    public static TrimPattern define(String key) {
        ResourceLocation assetId = ResourceLocation.minecraft(key);
        ItemType templateItem = ItemTypes.getByName(assetId + "_armor_trim_smithing_template");
        Component description = Component.translatable("trim_pattern.minecraft." + key);
        boolean decal = false;
        return define(key, assetId, templateItem, description, decal);
    }

    public static TrimPattern define(
            String key, ResourceLocation assetId, ItemType templateItem,
            Component description, boolean decal
    ) {
        TypesBuilderData data = TYPES_BUILDER.define(key);
        TrimPattern pattern = new TrimPattern() {
            @Override
            public ResourceLocation getAssetId() {
                return assetId;
            }

            @Override
            public ItemType getTemplateItem() {
                return templateItem;
            }

            @Override
            public Component getDescription() {
                return description;
            }

            @Override
            public boolean isDecal() {
                return decal;
            }

            @Override
            public ResourceLocation getName() {
                return data.getName();
            }

            @Override
            public int getId(ClientVersion version) {
                return MappingHelper.getId(version, TYPES_BUILDER, data);
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof TrimPattern) {
                    return this.getName().equals(((TrimPattern) obj).getName());
                }
                return false;
            }
        };
        MappingHelper.registerMapping(TYPES_BUILDER, PATTERN_TYPE_MAP, PATTERN_TYPE_ID_MAP, pattern);
        return pattern;
    }

    // with key
    public static TrimPattern getByName(String name) {
        return PATTERN_TYPE_MAP.get(name);
    }

    public static TrimPattern getById(ClientVersion version, int id) {
        int index = TYPES_BUILDER.getDataIndex(version);
        Map<Integer, TrimPattern> idMap = PATTERN_TYPE_ID_MAP.get((byte) index);
        return idMap.get(id);
    }

    // Added in 1.19.4
    public static final TrimPattern COAST = define("coast");
    public static final TrimPattern DUNE = define("dune");
    public static final TrimPattern EYE = define("eye");
    public static final TrimPattern RIB = define("rib");
    public static final TrimPattern SENTRY = define("sentry");
    public static final TrimPattern SNOUT = define("snout");
    public static final TrimPattern SPIRE = define("spire");
    public static final TrimPattern TIDE = define("tide");
    public static final TrimPattern VEX = define("vex");
    public static final TrimPattern WARD = define("ward");
    public static final TrimPattern WILD = define("wild");

    // Added in 1.20
    public static final TrimPattern RAISER = define("raiser");
    public static final TrimPattern HOST = define("host");
    public static final TrimPattern SILENCE = define("silence");
    public static final TrimPattern SHAPER = define("shaper");
    public static final TrimPattern WAYFINDER = define("wayfinder");

    // Added in 1.20.5
    public static final TrimPattern BOLT = define("bolt");
    public static final TrimPattern FLOW = define("flow");

    static {
        TYPES_BUILDER.unloadFileMappings();
    }
}
