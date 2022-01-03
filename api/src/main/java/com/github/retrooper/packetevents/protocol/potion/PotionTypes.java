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

package com.github.retrooper.packetevents.protocol.potion;

import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.MappingHelper;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PotionTypes {
    private static final Map<String, PotionType> POTION_TYPE_MAP = new HashMap<>();
    private static final Map<Integer, PotionType> POTION_TYPE_ID_MAP = new HashMap<>();
    private static JsonObject POTION_TYPES_JSON;

    public static PotionType define(String key) {
        if (POTION_TYPES_JSON == null) {
            POTION_TYPES_JSON = MappingHelper.getJSONObject("item/potion_type_mappings");
        }
        int id = POTION_TYPES_JSON.get(key).getAsInt();
        ResourceLocation identifier = ResourceLocation.minecraft(key);
        PotionType potionType = new PotionType() {
            @Override
            public ResourceLocation getName() {
                return identifier;
            }

            @Override
            public int getId() {
                return id;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof PotionType) {
                    return getId() == ((PotionType)obj).getId();
                }
                return false;
            }
        };

        POTION_TYPE_MAP.put(potionType.getName().toString(), potionType);
        POTION_TYPE_ID_MAP.put(potionType.getId(), potionType);
        return potionType;
    }

    @Nullable
    public static PotionType getByName(String name) {
        return POTION_TYPE_MAP.get(name);
    }

    @Nullable
    public static PotionType getById(int id) {
        return POTION_TYPE_ID_MAP.get(id);
    }

    public static final PotionType SPEED = define("speed");
    public static final PotionType SLOWNESS = define("slowness");
    public static final PotionType HASTE = define("haste");
    public static final PotionType MINING_FATIGUE = define("mining_fatigue");
    public static final PotionType STRENGTH = define("strength");
    public static final PotionType INSTANT_HEALTH = define("instant_health");
    public static final PotionType INSTANT_DAMAGE = define("instant_damage");
    public static final PotionType JUMP_BOOST = define("jump_boost");
    public static final PotionType NAUSEA = define("nausea");
    public static final PotionType REGENERATION = define("regeneration");
    public static final PotionType RESISTANCE = define("resistance");
    public static final PotionType FIRE_RESISTANCE = define("fire_resistance");
    public static final PotionType WATER_BREATHING = define("water_breathing");
    public static final PotionType INVISIBILITY = define("invisibility");
    public static final PotionType BLINDNESS = define("blindness");
    public static final PotionType NIGHT_VISION = define("night_vision");
    public static final PotionType HUNGER = define("hunger");
    public static final PotionType WEAKNESS = define("weakness");
    public static final PotionType POISON = define("poison");
    public static final PotionType WITHER = define("wither");
    public static final PotionType HEALTH_BOOST = define("health_boost");
    public static final PotionType ABSORPTION = define("absorption");
    public static final PotionType SATURATION = define("saturation");
    public static final PotionType GLOWING = define("glowing");
    public static final PotionType LEVITATION = define("levitation");
    public static final PotionType LUCK = define("luck");
    public static final PotionType UNLUCK = define("unluck");
    public static final PotionType SLOW_FALLING = define("slow_falling");
    public static final PotionType CONDUIT_POWER = define("conduit_power");
    public static final PotionType DOLPHINS_GRACE = define("dolphins_grace");
    public static final PotionType BAD_OMEN = define("bad_omen");
    public static final PotionType HERO_OF_THE_VILLAGE = define("hero_of_the_village");


}
