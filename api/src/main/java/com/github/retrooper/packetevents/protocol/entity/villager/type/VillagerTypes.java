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

package com.github.retrooper.packetevents.protocol.entity.villager.type;

import com.github.retrooper.packetevents.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VillagerTypes {
    private static final Map<String, VillagerType> VILLAGER_TYPE_MAP = new HashMap<>();
    private static final Map<Byte, VillagerType> VILLAGER_TYPE_ID_MAP = new HashMap<>();


    public static VillagerType define(int id, String name) {
        ResourceLocation location = new ResourceLocation(name);
        VillagerType type = new VillagerType() {
            @Override
            public ResourceLocation getName() {
                return location;
            }

            @Override
            public int getId() {
                return id;
            }
        };
        VILLAGER_TYPE_MAP.put(type.getName().toString(), type);
        VILLAGER_TYPE_ID_MAP.put((byte)type.getId(), type);
        return type;
    }

    public static VillagerType getById(int id) {
        return VILLAGER_TYPE_ID_MAP.get((byte)id);
    }


    public static VillagerType getByName(String name) {
        return VILLAGER_TYPE_MAP.get(name);
    }

    public static final VillagerType DESERT = define(0, "minecraft:desert");
    public static final VillagerType JUNGLE = define(1, "minecraft:jungle");
    public static final VillagerType PLAINS = define(2, "minecraft:plains");
    public static final VillagerType SAVANNA = define(3, "minecraft:savanna");
    public static final VillagerType SNOW = define(4, "minecraft:snow");
    public static final VillagerType SWAMP = define(5, "minecraft:swamp");
    public static final VillagerType TAIGA = define(6, "minecraft:taiga");

    /**
     * Returns an immutable view of the villager types.
     * @return Villager Types
     */
    public static Collection<VillagerType> values() {
        return Collections.unmodifiableCollection(VILLAGER_TYPE_MAP.values());
    }
}
