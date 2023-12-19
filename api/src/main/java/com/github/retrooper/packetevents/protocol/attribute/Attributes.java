/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2023 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.attribute;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.TypesBuilder;
import com.github.retrooper.packetevents.util.TypesBuilderData;

import java.util.HashMap;
import java.util.Map;

public class Attributes {

    private static final Map<String, Attribute> ATTRIBUTE_MAP = new HashMap<>();
    private static final Map<Byte, Map<Integer, Attribute>> ATTRIBUTE_ID_MAP = new HashMap<>();
    private static final TypesBuilder TYPES_BUILDER = new TypesBuilder("attribute/attribute_mappings",
            ClientVersion.V_1_20_3,
            ClientVersion.V_1_20_5);

    public static Attribute define(String key) {
        TypesBuilderData data = TYPES_BUILDER.defineFromArray(key);
        Attribute attribute = new Attribute() {
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
                if (obj instanceof Attribute) {
                    return this.getName().equals(((Attribute) obj).getName());
                }
                return false;
            }
        };

        ATTRIBUTE_MAP.put(attribute.getName().toString(), attribute);
        for (ClientVersion version : TYPES_BUILDER.getVersions()) {
            int index = TYPES_BUILDER.getDataIndex(version);
            Map<Integer, Attribute> idMap = ATTRIBUTE_ID_MAP.computeIfAbsent((byte) index, k -> new HashMap<>());
            idMap.put(attribute.getId(version), attribute);
        }
        return attribute;
    }

    // with minecraft:key
    public static Attribute getByName(String name) {
        return ATTRIBUTE_MAP.get(name);
    }

    public static Attribute getById(ClientVersion version, int id) {
        int index = TYPES_BUILDER.getDataIndex(version);
        Map<Integer, Attribute> idMap = ATTRIBUTE_ID_MAP.get((byte) index);
        return idMap.get(id);
    }

    public static final Attribute GENERIC_ARMOR = define("generic.armor");
    public static final Attribute GENERIC_ARMOR_TOUGHNESS = define("generic.armor_toughness");
    public static final Attribute GENERIC_ATTACK_DAMAGE = define("generic.attack_damage");
    public static final Attribute GENERIC_ATTACK_KNOCKBACK = define("generic.attack_knockback");
    public static final Attribute GENERIC_ATTACK_SPEED = define("generic.attack_speed");
    public static final Attribute GENERIC_FLYING_SPEED = define("generic.flying_speed");
    public static final Attribute GENERIC_FOLLOW_RANGE = define("generic.follow_range");
    public static final Attribute HORSE_JUMP_STRENGTH = define("horse.jump_strength");
    public static final Attribute GENERIC_KNOCKBACK_RESISTANCE = define("generic.knockback_resistance");
    public static final Attribute GENERIC_LUCK = define("generic.luck");
    public static final Attribute GENERIC_MAX_ABSORPTION = define("generic.max_absorption");
    public static final Attribute GENERIC_MAX_HEALTH = define("generic.max_health");
    public static final Attribute GENERIC_MOVEMENT_SPEED = define("generic.movement_speed");
    public static final Attribute ZOMBIE_SPAWN_REINFORCEMENTS = define("zombie.spawn_reinforcements");

    // added in 1.20.5
    public static final Attribute GENERIC_BLOCK_INTERACTION_RANGE = define("generic.block_interaction_range");
    public static final Attribute GENERIC_ENTITY_INTERACTION_RANGE = define("generic.entity_interaction_range");
    public static final Attribute GENERIC_SCALE = define("generic.scale");
    public static final Attribute GENERIC_STEP_HEIGHT = define("generic.step_height");

    static {
        TYPES_BUILDER.unloadFileMappings();
    }
}
