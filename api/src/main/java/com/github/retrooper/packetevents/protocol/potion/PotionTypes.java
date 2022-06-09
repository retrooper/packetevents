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
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PotionTypes {
    private static final Map<String, PotionType> POTION_TYPE_MAP = new HashMap<>();
    private static final Map<Byte, PotionType> POTION_TYPE_ID_MAP = new HashMap<>();

    public static PotionType define(String key, int id) {
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

        //minecraft:speed would be a string for example
        POTION_TYPE_MAP.put(potionType.getName().toString(), potionType);
        POTION_TYPE_ID_MAP.put((byte) potionType.getId(), potionType);
        return potionType;
    }

    @Nullable
    public static PotionType getByName(String name) {
        return POTION_TYPE_MAP.get(name);
    }

    @Nullable
    public static PotionType getById(int id) {
        return POTION_TYPE_ID_MAP.get((byte)id);
    }

    public static final PotionType SPEED = define("speed", 1);
    public static final PotionType SLOWNESS = define("slowness", 2);
    public static final PotionType HASTE = define("haste", 3);
    public static final PotionType MINING_FATIGUE = define("mining_fatigue", 4);
    public static final PotionType STRENGTH = define("strength", 5);
    public static final PotionType INSTANT_HEALTH = define("instant_health", 6);
    public static final PotionType INSTANT_DAMAGE = define("instant_damage", 7);
    public static final PotionType JUMP_BOOST = define("jump_boost", 8);
    public static final PotionType NAUSEA = define("nausea", 9);
    public static final PotionType REGENERATION = define("regeneration", 10);
    public static final PotionType RESISTANCE = define("resistance", 11);
    public static final PotionType FIRE_RESISTANCE = define("fire_resistance", 12);
    public static final PotionType WATER_BREATHING = define("water_breathing", 13);
    public static final PotionType INVISIBILITY = define("invisibility", 14);
    public static final PotionType BLINDNESS = define("blindness", 15);
    public static final PotionType NIGHT_VISION = define("night_vision", 16);
    public static final PotionType HUNGER = define("hunger", 17);
    public static final PotionType WEAKNESS = define("weakness", 18);
    public static final PotionType POISON = define("poison", 19);
    public static final PotionType WITHER = define("wither", 20);
    public static final PotionType HEALTH_BOOST = define("health_boost", 21);
    public static final PotionType ABSORPTION = define("absorption", 22);
    public static final PotionType SATURATION = define("saturation", 23);
    public static final PotionType GLOWING = define("glowing", 24);
    public static final PotionType LEVITATION = define("levitation", 25);
    public static final PotionType LUCK = define("luck", 26);
    public static final PotionType UNLUCK = define("unluck", 27);
    public static final PotionType SLOW_FALLING = define("slow_falling", 28);
    public static final PotionType CONDUIT_POWER = define("conduit_power", 29);
    public static final PotionType DOLPHINS_GRACE = define("dolphins_grace", 30);
    public static final PotionType BAD_OMEN = define("bad_omen", 31);
    public static final PotionType HERO_OF_THE_VILLAGE = define("hero_of_the_village", 32);
    public static final PotionType DARKNESS = define("darkness", 33);
}