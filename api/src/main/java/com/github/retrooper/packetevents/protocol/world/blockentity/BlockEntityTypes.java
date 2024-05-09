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

package com.github.retrooper.packetevents.protocol.world.blockentity;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilder;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BlockEntityTypes {

    private static final Map<String, BlockEntityType> BLOCK_ENTITY_TYPE_MAP = new HashMap<>();
    private static final Map<Byte, Map<Integer, BlockEntityType>> BLOCK_ENTITY_TYPE_ID_MAP = new HashMap<>();
    private static final TypesBuilder TYPES_BUILDER = new TypesBuilder("block/block_entity_type_mappings");

    public static BlockEntityType define(String key) {
        TypesBuilderData data = TYPES_BUILDER.define(key);
        BlockEntityType blockEntityType = new BlockEntityType() {
            private final int[] ids = data.getData();

            @Override
            public ResourceLocation getName() {
                return data.getName();
            }

            @Override
            public int getId(ClientVersion version) {
                int index = TYPES_BUILDER.getDataIndex(version);
                return this.ids[index];
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof BlockEntityType) {
                    return this.getName().equals(((BlockEntityType) obj).getName());
                }
                return false;
            }
        };

        BLOCK_ENTITY_TYPE_MAP.put(blockEntityType.getName().toString(), blockEntityType);
        for (ClientVersion version : TYPES_BUILDER.getVersions()) {
            int index = TYPES_BUILDER.getDataIndex(version);
            Map<Integer, BlockEntityType> idMap = BLOCK_ENTITY_TYPE_ID_MAP.computeIfAbsent(
                    (byte) index, k -> new HashMap<>());
            idMap.put(blockEntityType.getId(version), blockEntityType);
        }
        return blockEntityType;
    }

    // with minecraft:key
    public static BlockEntityType getByName(String name) {
        return BLOCK_ENTITY_TYPE_MAP.get(name);
    }

    public static BlockEntityType getById(ClientVersion version, int id) {
        int index = TYPES_BUILDER.getDataIndex(version);
        Map<Integer, BlockEntityType> idMap = BLOCK_ENTITY_TYPE_ID_MAP.get((byte) index);
        return idMap.get(id);
    }

    public static final BlockEntityType FURNACE = define("furnace");
    public static final BlockEntityType CHEST = define("chest");
    public static final BlockEntityType TRAPPED_CHEST = define("trapped_chest");
    public static final BlockEntityType ENDER_CHEST = define("ender_chest");
    public static final BlockEntityType JUKEBOX = define("jukebox");
    public static final BlockEntityType DISPENSER = define("dispenser");
    public static final BlockEntityType DROPPER = define("dropper");
    public static final BlockEntityType SIGN = define("sign");
    public static final BlockEntityType HANGING_SIGN = define("hanging_sign");
    public static final BlockEntityType MOB_SPAWNER = define("mob_spawner");
    public static final BlockEntityType PISTON = define("piston");
    public static final BlockEntityType BREWING_STAND = define("brewing_stand");
    public static final BlockEntityType ENCHANTING_TABLE = define("enchanting_table");
    public static final BlockEntityType END_PORTAL = define("end_portal");
    public static final BlockEntityType BEACON = define("beacon");
    public static final BlockEntityType SKULL = define("skull");
    public static final BlockEntityType DAYLIGHT_DETECTOR = define("daylight_detector");
    public static final BlockEntityType HOPPER = define("hopper");
    public static final BlockEntityType COMPARATOR = define("comparator");
    public static final BlockEntityType BANNER = define("banner");
    public static final BlockEntityType STRUCTURE_BLOCK = define("structure_block");
    public static final BlockEntityType END_GATEWAY = define("end_gateway");
    public static final BlockEntityType COMMAND_BLOCK = define("command_block");
    public static final BlockEntityType SHULKER_BOX = define("shulker_box");
    public static final BlockEntityType BED = define("bed");
    public static final BlockEntityType CONDUIT = define("conduit");
    public static final BlockEntityType BARREL = define("barrel");
    public static final BlockEntityType SMOKER = define("smoker");
    public static final BlockEntityType BLAST_FURNACE = define("blast_furnace");
    public static final BlockEntityType LECTERN = define("lectern");
    public static final BlockEntityType BELL = define("bell");
    public static final BlockEntityType JIGSAW = define("jigsaw");
    public static final BlockEntityType CAMPFIRE = define("campfire");
    public static final BlockEntityType BEEHIVE = define("beehive");
    public static final BlockEntityType SCULK_SENSOR = define("sculk_sensor");
    public static final BlockEntityType CALIBRATED_SCULK_SENSOR = define("calibrated_sculk_sensor");
    public static final BlockEntityType SCULK_CATALYST = define("sculk_catalyst");
    public static final BlockEntityType SCULK_SHRIEKER = define("sculk_shrieker");
    public static final BlockEntityType CHISELED_BOOKSHELF = define("chiseled_bookshelf");
    public static final BlockEntityType BRUSHABLE_BLOCK = define("brushable_block");
    public static final BlockEntityType DECORATED_POT = define("decorated_pot");
    public static final BlockEntityType CRAFTER = define("crafter");
    public static final BlockEntityType TRIAL_SPAWNER = define("trial_spawner");
    public static final BlockEntityType VAULT = define("vault");

    /**
     * Returns an immutable view of the block entity types.
     * @return Block Entity Types
     */
    public static Collection<BlockEntityType> values() {
        return Collections.unmodifiableCollection(BLOCK_ENTITY_TYPE_MAP.values());
    }

    static {
        TYPES_BUILDER.unloadFileMappings();
    }
}
