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

package com.github.retrooper.packetevents.protocol.item.armormaterial;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.MappingHelper;
import com.github.retrooper.packetevents.util.mappings.TypesBuilder;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;

import java.util.HashMap;
import java.util.Map;

public class ArmorMaterials {

    private static final Map<String, ArmorMaterial> MATERIAL_MAP = new HashMap<>();
    private static final Map<Byte, Map<Integer, ArmorMaterial>> MATERIAL_ID_MAP = new HashMap<>();
    private static final TypesBuilder TYPES_BUILDER = new TypesBuilder("item/item_armor_material_mappings");

    public static ArmorMaterial define(String key) {
        TypesBuilderData data = TYPES_BUILDER.define(key);
        ArmorMaterial instrument = new ArmorMaterial() {
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
                if (obj instanceof ArmorMaterial) {
                    return getName().equals(((ArmorMaterial) obj).getName());
                }
                return false;
            }
        };
        MappingHelper.registerMapping(TYPES_BUILDER, MATERIAL_MAP, MATERIAL_ID_MAP, instrument);
        return instrument;
    }

    // with key
    public static ArmorMaterial getByName(String name) {
        return MATERIAL_MAP.get(name);
    }

    public static ArmorMaterial getById(ClientVersion version, int id) {
        int index = TYPES_BUILDER.getDataIndex(version);
        Map<Integer, ArmorMaterial> idMap = MATERIAL_ID_MAP.get((byte) index);
        return idMap.get(id);
    }

    public static final ArmorMaterial LEATHER = define("leather");
    public static final ArmorMaterial CHAINMAIL = define("chainmail");
    public static final ArmorMaterial IRON = define("iron");
    public static final ArmorMaterial GOLD = define("gold");
    public static final ArmorMaterial DIAMOND = define("diamond");

    // Added with 1.13
    public static final ArmorMaterial TURTLE = define("turtle");

    // Added with 1.16
    public static final ArmorMaterial NETHERITE = define("netherite");

    // Added with 1.20.5
    public static final ArmorMaterial ARMADILLO = define("armadillo");

    static {
        TYPES_BUILDER.unloadFileMappings();
    }
}
