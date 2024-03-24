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

package com.github.retrooper.packetevents.protocol.entity.villager.profession;

import com.github.retrooper.packetevents.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class VillagerProfessions {
    private static final Map<String, VillagerProfession> VILLAGER_PROFESSION_MAP = new HashMap<>();
    private static final Map<Byte, VillagerProfession> VILLAGER_PROFESSION_ID_MAP = new HashMap<>();

    public static VillagerProfession define(int id, String name) {
        ResourceLocation location = new ResourceLocation(name);
        VillagerProfession type = new VillagerProfession() {
            @Override
            public ResourceLocation getName() {
                return location;
            }

            @Override
            public int getId() {
                return id;
            }
        };
        VILLAGER_PROFESSION_MAP.put(type.getName().toString(), type);
        VILLAGER_PROFESSION_ID_MAP.put((byte) type.getId(), type);
        return type;
    }

    public static VillagerProfession getById(int id) {
        return VILLAGER_PROFESSION_ID_MAP.get((byte)id);
    }

    public static VillagerProfession getByName(String name) {
        return VILLAGER_PROFESSION_MAP.get(name);
    }

    public static final VillagerProfession NONE = define(0, "minecraft:none");
    public static final VillagerProfession ARMORER = define(1, "minecraft:armorer");
    public static final VillagerProfession BUTCHER = define(2, "minecraft:butcher");
    public static final VillagerProfession CARTOGRAPHER = define(3, "minecraft:cartographer");
    public static final VillagerProfession CLERIC = define(4, "minecraft:cleric");
    public static final VillagerProfession FARMER = define(5, "minecraft:farmer");
    public static final VillagerProfession FISHERMAN = define(6, "minecraft:fisherman");
    public static final VillagerProfession FLETCHER = define(7, "minecraft:fletcher");
    public static final VillagerProfession LEATHERWORKER = define(8, "minecraft:leatherworker");
    public static final VillagerProfession LIBRARIAN = define(9, "minecraft:librarian");
    public static final VillagerProfession MASON = define(10, "minecraft:mason");
    public static final VillagerProfession NITWIT = define(11, "minecraft:nitwit");
    public static final VillagerProfession SHEPHERD = define(12, "minecraft:shepherd");
    public static final VillagerProfession TOOLSMITH = define(13, "minecraft:toolsmith");
    public static final VillagerProfession WEAPONSMITH = define(14, "minecraft:weaponsmith");
}
