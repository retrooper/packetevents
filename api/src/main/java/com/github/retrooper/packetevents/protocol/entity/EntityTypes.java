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

package com.github.retrooper.packetevents.protocol.entity;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.MappingHelper;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class EntityTypes {
    private static final Map<String, EntityType> ENTITY_TYPE_MAP = new HashMap<>();
    private static final Map<Integer, EntityType> ENTITY_TYPE_ID_MAP = new HashMap<>();
    private static JsonObject MODERN_ENTITY_TYPES_JSON;

    public static EntityType define(String key) {
        if (MODERN_ENTITY_TYPES_JSON == null) {
            MODERN_ENTITY_TYPES_JSON = MappingHelper.getJSONObject("modernentitytypes");
        }
        int modernID = MODERN_ENTITY_TYPES_JSON.get(key).getAsInt();
        int latestProtocolVersion = ServerVersion.getLatest().getProtocolVersion();
        int serverProtocolVersion = PacketEvents.getAPI().getServerManager().getVersion().getProtocolVersion();

        int transformedID = transformEntityTypeId(modernID, latestProtocolVersion, serverProtocolVersion);
        ResourceLocation identifier = ResourceLocation.minecraft(key);
        EntityType entityType = new EntityType() {
            @Override
            public ResourceLocation getIdentifier() {
                return identifier;
            }

            @Override
            public int getId() {
                return transformedID;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof EntityType) {
                    return getId() == ((EntityType) obj).getId();
                }
                return false;
            }
        };

        ENTITY_TYPE_MAP.put(entityType.getIdentifier().getKey(), entityType);
        ENTITY_TYPE_ID_MAP.put(entityType.getId(), entityType);
        return entityType;
    }

    public static int transformEntityTypeId(int id, int currentProtocolVersion, int targetProtocolVersion) {
        if (currentProtocolVersion == targetProtocolVersion) {
            return id;
        }
        //TODO Hard coded conversions
        return id;
    }

    public static EntityType getByKey(String key) {
        return ENTITY_TYPE_MAP.get(key);
    }

    public static EntityType getById(int id) {
        return ENTITY_TYPE_ID_MAP.get(id);
    }

    public static final EntityType AREA_EFFECT_CLOUD = define("area_effect_cloud");
    public static final EntityType ARMOR_STAND = define("armor_stand");
    public static final EntityType ARROW = define("arrow");
    public static final EntityType AXOLOTL = define("axolotl");
    public static final EntityType BAT = define("bat");
    public static final EntityType BEE = define("bee");
    public static final EntityType BLAZE = define("blaze");
    public static final EntityType BOAT = define("boat");
    public static final EntityType CAT = define("cat");
    public static final EntityType CAVE_SPIDER = define("cave_spider");
    public static final EntityType CHICKEN = define("chicken");
    public static final EntityType COD = define("cod");
    public static final EntityType COW = define("cow");
    public static final EntityType CREEPER = define("creeper");
    public static final EntityType DOLPHIN = define("dolphin");
    public static final EntityType DONKEY = define("donkey");
    public static final EntityType DRAGON_FIREBALL = define("dragon_fireball");
    public static final EntityType DROWNED = define("drowned");
    public static final EntityType ELDER_GUARDIAN = define("elder_guardian");
    public static final EntityType END_CRYSTAL = define("end_crystal");
    public static final EntityType ENDER_DRAGON = define("ender_dragon");
    public static final EntityType ENDERMAN = define("enderman");
    public static final EntityType ENDERMITE = define("endermite");
    public static final EntityType EVOKER = define("evoker");
    public static final EntityType EVOKER_FANGS = define("evoker_fangs");
    public static final EntityType EXPERIENCE_ORB = define("experience_orb");
    public static final EntityType EYE_OF_ENDER = define("eye_of_ender");
    public static final EntityType FALLING_BLOCK = define("falling_block");
    public static final EntityType FIREWORK_ROCKET = define("firework_rocket");
    public static final EntityType FOX = define("fox");
    public static final EntityType GHAST = define("ghast");
    public static final EntityType GIANT = define("giant");
    public static final EntityType GLOW_ITEM_FRAME = define("glow_item_frame");
    public static final EntityType GLOW_SQUID = define("glow_squid");
    public static final EntityType GOAT = define("goat");
    public static final EntityType GUARDIAN = define("guardian");
    public static final EntityType HOGLIN = define("hoglin");
    public static final EntityType HORSE = define("horse");
    public static final EntityType HUSK = define("husk");
    public static final EntityType ILLUSIONER = define("illusioner");
    public static final EntityType IRON_GOLEM = define("iron_golem");
    public static final EntityType ITEM = define("item");
    public static final EntityType ITEM_FRAME = define("item_frame");
    public static final EntityType FIREBALL = define("fireball");
    public static final EntityType LEASH_KNOT = define("leash_knot");
    public static final EntityType LIGHTNING_BOLT = define("lightning_bolt");
    public static final EntityType LLAMA = define("llama");
    public static final EntityType LLAMA_SPIT = define("llama_spit");
    public static final EntityType MAGMA_CUBE = define("magma_cube");
    public static final EntityType MARKER = define("marker");
    public static final EntityType MINECART = define("minecart");
    public static final EntityType CHEST_MINECART = define("chest_minecart");
    public static final EntityType COMMAND_BLOCK_MINECART = define("command_block_minecart");
    public static final EntityType FURNACE_MINECART = define("furnace_minecart");
    public static final EntityType HOPPER_MINECART = define("hopper_minecart");
    public static final EntityType SPAWNER_MINECART = define("spawner_minecart");
    public static final EntityType TNT_MINECART = define("tnt_minecart");
    public static final EntityType MULE = define("mule");
    public static final EntityType MOOSHROOM = define("mooshroom");
    public static final EntityType OCELOT = define("ocelot");
    public static final EntityType PAINTING = define("painting");
    public static final EntityType PANDA = define("panda");
    public static final EntityType PARROT = define("parrot");
    public static final EntityType PHANTOM = define("phantom");
    public static final EntityType PIG = define("pig");
    public static final EntityType PIGLIN = define("piglin");
    public static final EntityType PIGLIN_BRUTE = define("piglin_brute");
    public static final EntityType PILLAGER = define("pillager");
    public static final EntityType POLAR_BEAR = define("polar_bear");
    public static final EntityType TNT = define("tnt");
    public static final EntityType PUFFERFISH = define("pufferfish");
    public static final EntityType RABBIT = define("rabbit");
    public static final EntityType RAVAGER = define("ravager");
    public static final EntityType SALMON = define("salmon");
    public static final EntityType SHEEP = define("sheep");
    public static final EntityType SHULKER = define("shulker");
    public static final EntityType SHULKER_BULLET = define("shulker_bullet");
    public static final EntityType SILVERFISH = define("silverfish");
    public static final EntityType SKELETON = define("skeleton");
    public static final EntityType SKELETON_HORSE = define("skeleton_horse");
    public static final EntityType SLIME = define("slime");
    public static final EntityType SMALL_FIREBALL = define("small_fireball");
    public static final EntityType SNOW_GOLEM = define("snow_golem");
    public static final EntityType SNOWBALL = define("snowball");
    public static final EntityType SPECTRAL_ARROW = define("spectral_arrow");
    public static final EntityType SPIDER = define("spider");
    public static final EntityType SQUID = define("squid");
    public static final EntityType STRAY = define("stray");
    public static final EntityType STRIDER = define("strider");
    public static final EntityType EGG = define("egg");
    public static final EntityType ENDER_PEARL = define("ender_pearl");
    public static final EntityType EXPERIENCE_BOTTLE = define("experience_bottle");
    public static final EntityType POTION = define("potion");
    public static final EntityType TRIDENT = define("trident");
    public static final EntityType TRADER_LLAMA = define("trader_llama");
    public static final EntityType TROPICAL_FISH = define("tropical_fish");
    public static final EntityType TURTLE = define("turtle");
    public static final EntityType VEX = define("vex");
    public static final EntityType VILLAGER = define("villager");
    public static final EntityType VINDICATOR = define("vindicator");
    public static final EntityType WANDERING_TRADER = define("wandering_trader");
    public static final EntityType WITCH = define("witch");
    public static final EntityType WITHER = define("wither");
    public static final EntityType WITHER_SKELETON = define("wither_skeleton");
    public static final EntityType WITHER_SKULL = define("wither_skull");
    public static final EntityType WOLF = define("wolf");
    public static final EntityType ZOGLIN = define("zoglin");
    public static final EntityType ZOMBIE = define("zombie");
    public static final EntityType ZOMBIE_HORSE = define("zombie_horse");
    public static final EntityType ZOMBIE_VILLAGER = define("zombie_villager");
    public static final EntityType ZOMBIFIED_PIGLIN = define("zombified_piglin");
    public static final EntityType PLAYER = define("player");
    public static final EntityType FISHING_BOBBER = define("fishing_bobber");
}
