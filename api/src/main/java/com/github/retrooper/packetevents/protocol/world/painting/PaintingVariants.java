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

package com.github.retrooper.packetevents.protocol.world.painting;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.MappingHelper;
import com.github.retrooper.packetevents.util.mappings.TypesBuilder;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;

import java.util.HashMap;
import java.util.Map;

public class PaintingVariants {

    private static final Map<String, PaintingVariant> PAINTING_TYPE_MAP = new HashMap<>();
    private static final Map<Byte, Map<Integer, PaintingVariant>> PAINTING_TYPE_ID_MAP = new HashMap<>();
    private static final TypesBuilder TYPES_BUILDER = new TypesBuilder("item/item_banner_pattern_mappings");

    public static PaintingVariant define(String key, int width, int height) {
        ResourceLocation assetId = ResourceLocation.minecraft(key);
        return define(key, width, height, assetId);
    }

    public static PaintingVariant define(String key, int width, int height, ResourceLocation assetId) {
        TypesBuilderData data = TYPES_BUILDER.define(key);
        PaintingVariant variant = new PaintingVariant() {
            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public ResourceLocation getAssetId() {
                return assetId;
            }

            @Override
            public ResourceLocation getName() {
                return data.getName();
            }

            @Override
            public int getId(ClientVersion version) {
                return TYPES_BUILDER.getDataIndex(version);
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof PaintingVariant) {
                    return this.getName().equals(((PaintingVariant) obj).getName());
                }
                return false;
            }
        };
        MappingHelper.registerMapping(TYPES_BUILDER, PAINTING_TYPE_MAP, PAINTING_TYPE_ID_MAP, variant);
        return variant;
    }

    // with key
    public static PaintingVariant getByName(String name) {
        return PAINTING_TYPE_MAP.get(name);
    }

    public static PaintingVariant getById(ClientVersion version, int id) {
        int index = TYPES_BUILDER.getDataIndex(version);
        Map<Integer, PaintingVariant> idMap = PAINTING_TYPE_ID_MAP.get((byte) index);
        return idMap.get(id);
    }

    public static final PaintingVariant POINTER = define("pointer", 4, 4);
    public static final PaintingVariant CREEBET = define("creebet", 2, 1);
    public static final PaintingVariant PRAIRIE_RIDE = define("prairie_ride", 1, 2);
    public static final PaintingVariant POOL = define("pool", 2, 1);
    public static final PaintingVariant EARTH = define("earth", 2, 2);
    public static final PaintingVariant SKELETON = define("skeleton", 4, 3);
    public static final PaintingVariant MATCH = define("match", 2, 2);
    public static final PaintingVariant POND = define("pond", 3, 4);
    public static final PaintingVariant HUMBLE = define("humble", 2, 2);
    public static final PaintingVariant PIGSCENE = define("pigscene", 4, 4);
    public static final PaintingVariant WATER = define("water", 2, 2);
    public static final PaintingVariant ALBAN = define("alban", 1, 1);
    public static final PaintingVariant FINDING = define("finding", 4, 2);
    public static final PaintingVariant AZTEC2 = define("aztec2", 1, 1);
    public static final PaintingVariant TIDES = define("tides", 3, 3);
    public static final PaintingVariant FIGHTERS = define("fighters", 4, 2);
    public static final PaintingVariant FIRE = define("fire", 2, 2);
    public static final PaintingVariant CHANGING = define("changing", 4, 2);
    public static final PaintingVariant BURNING_SKULL = define("burning_skull", 4, 4);
    public static final PaintingVariant COTAN = define("cotan", 3, 3);
    public static final PaintingVariant WANDERER = define("wanderer", 1, 2);
    public static final PaintingVariant UNPACKED = define("unpacked", 4, 4);
    public static final PaintingVariant SUNSET = define("sunset", 2, 1);
    public static final PaintingVariant FERN = define("fern", 3, 3);
    public static final PaintingVariant BUST = define("bust", 2, 2);
    public static final PaintingVariant WIND = define("wind", 2, 2);
    public static final PaintingVariant LOWMIST = define("lowmist", 4, 2);
    public static final PaintingVariant PASSAGE = define("passage", 4, 2);
    public static final PaintingVariant SUNFLOWERS = define("sunflowers", 3, 3);
    public static final PaintingVariant GRAHAM = define("graham", 1, 2);
    public static final PaintingVariant WASTELAND = define("wasteland", 1, 1);
    public static final PaintingVariant SKULL_AND_ROSES = define("skull_and_roses", 2, 2);
    public static final PaintingVariant BOUQUET = define("bouquet", 3, 3);
    public static final PaintingVariant ORB = define("orb", 4, 4);
    public static final PaintingVariant BOMB = define("bomb", 1, 1);
    public static final PaintingVariant WITHER = define("wither", 2, 2);
    public static final PaintingVariant BACKYARD = define("backyard", 3, 4);
    public static final PaintingVariant ENDBOSS = define("endboss", 3, 3);
    public static final PaintingVariant MEDITATIVE = define("meditative", 1, 1);
    public static final PaintingVariant VOID = define("void", 2, 2);
    public static final PaintingVariant KEBAB = define("kebab", 1, 1);
    public static final PaintingVariant SEA = define("sea", 2, 1);
    public static final PaintingVariant DONKEY_KONG = define("donkey_kong", 4, 3);
    public static final PaintingVariant BAROQUE = define("baroque", 2, 2);
    public static final PaintingVariant STAGE = define("stage", 2, 2);
    public static final PaintingVariant AZTEC = define("aztec", 1, 1);
    public static final PaintingVariant PLANT = define("plant", 1, 1);
    public static final PaintingVariant CAVEBIRD = define("cavebird", 3, 3);
    public static final PaintingVariant COURBET = define("courbet", 2, 1);
    public static final PaintingVariant OWLEMONS = define("owlemons", 3, 3);

    static {
        TYPES_BUILDER.unloadFileMappings();
    }
}
