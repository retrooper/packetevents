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

package com.github.retrooper.packetevents.protocol.entity.type;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.MappingHelper;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EntityTypes {
    private static final Map<String, EntityType> ENTITY_TYPE_MAP = new HashMap<>();
    private static final Map<Integer, EntityType> ENTITY_TYPE_ID_MAP = new HashMap<>();
    private static JsonObject MAPPINGS;
    private static JsonObject LEGACY_MAPPINGS;

    static {
        //Mappings have been used, we can clear them.
        //TODO Test this to see if it works.
        /*
        MAPPINGS = null;
        LEGACY_MAPPINGS = null;*/
    }

    @NotNull
    private static ServerVersion getMappingServerVersion(ServerVersion serverVersion) {
        if (serverVersion.isOlderThan(ServerVersion.V_1_11)) {
            return ServerVersion.V_1_10;
        } else if (serverVersion.isOlderThan(ServerVersion.V_1_12)) {
            return ServerVersion.V_1_11;
        } else if (serverVersion.isOlderThan(ServerVersion.V_1_13)) {
            return ServerVersion.V_1_12;
        } else if (serverVersion.isOlderThan(ServerVersion.V_1_14)) {
            return ServerVersion.V_1_13;
        } else if (serverVersion.isOlderThan(ServerVersion.V_1_15)) {
            return ServerVersion.V_1_14;
        } else if (serverVersion.isOlderThan(ServerVersion.V_1_16)) {
            return ServerVersion.V_1_15;
        } else if (serverVersion.isOlderThan(ServerVersion.V_1_16_2)) {
            return ServerVersion.V_1_16;
        } else if (serverVersion.isOlderThan(ServerVersion.V_1_17)) {
            return ServerVersion.V_1_16_2;
        } else {
            return ServerVersion.V_1_17;
        }
    }

    @Nullable
    private static ServerVersion getLegacyMappingServerVersion(ServerVersion serverVersion) {
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
            return ServerVersion.V_1_8;
        } else if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_9_4)) {
            return ServerVersion.V_1_9;
        } else if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_10_2)) {
            return ServerVersion.V_1_10;
        } else if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_11_2)) {
            return ServerVersion.V_1_11;
        } else if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
            return ServerVersion.V_1_12;
        } else if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_13_2)) {
            return ServerVersion.V_1_13;
        } else {
            return null;
        }
    }

    public static EntityType define(String key, @Nullable EntityType parent) {
        if (MAPPINGS == null) {
            MAPPINGS = MappingHelper.getJSONObject("entity/entity_type_mappings");
        }
        if (LEGACY_MAPPINGS == null) {
            LEGACY_MAPPINGS = MappingHelper.getJSONObject("entity/legacy_entity_type_mappings");
        }

        ResourceLocation identifier = ResourceLocation.minecraft(key);
        ServerVersion mappingsVersion = getMappingServerVersion(PacketEvents.getAPI().getServerManager().getVersion());
        ServerVersion legacyMapping = getLegacyMappingServerVersion(PacketEvents.getAPI().getServerManager().getVersion());

        final int id;
        final int legacy_id;

        if (MAPPINGS.has(mappingsVersion.name())) {
            JsonObject map = MAPPINGS.getAsJsonObject(mappingsVersion.name());
            if (map.has(key)) {
                id = map.get(key).getAsInt();
            } else {
                id = -1;
            }
        } else {
            throw new IllegalStateException("Failed to find EntityType mappings for the " + mappingsVersion.name() + " mappings version!");
        }
        if (legacyMapping != null && LEGACY_MAPPINGS.has(legacyMapping.name())) {
            JsonObject map = LEGACY_MAPPINGS.getAsJsonObject(legacyMapping.name());
            if (map.has(key)) {
                legacy_id = map.get(key).getAsInt();
            } else {
                legacy_id = -1;
            }
        } else {
            if (mappingsVersion.isOlderThan(ServerVersion.V_1_14)) {
                throw new IllegalStateException("Failed to find Legacy EntityType mappings for the " + mappingsVersion.name() + " mappings version!");
            }
            legacy_id = -1;
        }
        Optional<EntityType> optParent = parent != null ? Optional.of(parent) : Optional.empty();

        EntityType entityType = new EntityType() {
            @Override
            public ResourceLocation getName() {
                return identifier;
            }

            @Override
            public int getId() {
                return id;
            }

            @Override
            public int getLegacyId() {
                return legacy_id;
            }

            @Override
            public Optional<EntityType> getParent() {
                return optParent;
            }
        };

        ENTITY_TYPE_MAP.put(entityType.getName().toString(), entityType);
        ENTITY_TYPE_ID_MAP.put(entityType.getId(), entityType);
        return entityType;
    }

    public static boolean isTypeInstanceOf(EntityType type, EntityType parent) {
        while (type != null) {
            if (type == parent) {
                return true;
            }
            if (type.getParent().isPresent()) {
                type = type.getParent().get();
            } else {
                return false;
            }
        }
        return false;
    }

    //with minecraft:key
    public static EntityType getByName(String name) {
        return ENTITY_TYPE_MAP.get(name);
    }

    public static EntityType getById(int id) {
        return ENTITY_TYPE_ID_MAP.get(id);
    }

    public static EntityType getByLegacyId(int id) {
        for (EntityType type : ENTITY_TYPE_MAP.values()) {
            if (type.getLegacyId() == id) {
                return type;
            }
        }
        return null;
    }

    // Credit to ViaVersion for these categories
    public static final EntityType ENTITY = define("entity", null);
    public static final EntityType LIVINGENTITY = define("livingentity", ENTITY);
    public static final EntityType ABSTRACT_INSENTIENT = define("abstract_insentient", LIVINGENTITY);
    public static final EntityType ABSTRACT_CREATURE = define("abstract_creature", ABSTRACT_INSENTIENT);
    public static final EntityType ABSTRACT_AGEABLE = define("abstract_ageable", ABSTRACT_CREATURE);
    public static final EntityType ABSTRACT_ANIMAL = define("abstract_animal", ABSTRACT_AGEABLE);
    public static final EntityType ABSTRACT_TAMEABLE_ANIMAL = define("abstract_tameable_animal", ABSTRACT_ANIMAL);
    public static final EntityType ABSTRACT_PARROT = define("abstract_parrot", ABSTRACT_TAMEABLE_ANIMAL);
    public static final EntityType ABSTRACT_HORSE = define("abstract_horse", ABSTRACT_ANIMAL);
    public static final EntityType CHESTED_HORSE = define("chested_horse", ABSTRACT_HORSE);
    public static final EntityType ABSTRACT_GOLEM = define("abstract_golem", ABSTRACT_CREATURE);
    public static final EntityType ABSTRACT_FISHES = define("abstract_fishes", ABSTRACT_CREATURE);
    public static final EntityType ABSTRACT_MONSTER = define("abstract_monster", ABSTRACT_CREATURE);
    public static final EntityType ABSTRACT_PIGLIN = define("abstract_piglin", ABSTRACT_MONSTER);
    public static final EntityType ABSTRACT_ILLAGER_BASE = define("abstract_illager_base", ABSTRACT_MONSTER);
    public static final EntityType ABSTRACT_EVO_ILLU_ILLAGER = define("abstract_evo_illu_illager", ABSTRACT_ILLAGER_BASE);
    public static final EntityType ABSTRACT_SKELETON = define("abstract_skeleton", ABSTRACT_MONSTER);
    public static final EntityType ABSTRACT_FLYING = define("abstract_flying", ABSTRACT_INSENTIENT);
    public static final EntityType ABSTRACT_AMBIENT = define("abstract_ambient", ABSTRACT_INSENTIENT);
    public static final EntityType ABSTRACT_WATERMOB = define("abstract_watermob", ABSTRACT_INSENTIENT);
    public static final EntityType ABSTRACT_HANGING = define("abstract_hanging", ENTITY);
    public static final EntityType ABSTRACT_LIGHTNING = define("abstract_lightning", ENTITY);
    public static final EntityType ABSTRACT_ARROW = define("abstract_arrow", ENTITY);
    public static final EntityType ABSTRACT_FIREBALL = define("abstract_fireball", ENTITY);
    public static final EntityType PROJECTILE_ABSTRACT = define("projectile_abstract", ENTITY);
    public static final EntityType MINECART_ABSTRACT = define("minecart_abstract", ENTITY);
    public static final EntityType CHESTED_MINECART_ABSTRACT = define("chested_minecart_abstract", MINECART_ABSTRACT);
    public static final EntityType AREA_EFFECT_CLOUD = define("area_effect_cloud", ENTITY);
    public static final EntityType ARMOR_STAND = define("armor_stand", LIVINGENTITY);
    public static final EntityType ARROW = define("arrow", ABSTRACT_ARROW);
    public static final EntityType AXOLOTL = define("axolotl", ABSTRACT_ANIMAL);
    public static final EntityType BAT = define("bat", ABSTRACT_AMBIENT);
    public static final EntityType BEE = define("bee", ABSTRACT_INSENTIENT);
    public static final EntityType BLAZE = define("blaze", ABSTRACT_MONSTER);
    public static final EntityType BOAT = define("boat", ENTITY);
    public static final EntityType CAT = define("cat", ABSTRACT_TAMEABLE_ANIMAL);
    public static final EntityType SPIDER = define("spider", ABSTRACT_MONSTER);
    public static final EntityType CAVE_SPIDER = define("cave_spider", SPIDER);
    public static final EntityType CHICKEN = define("chicken", ABSTRACT_ANIMAL);
    public static final EntityType COD = define("cod", ABSTRACT_FISHES);
    public static final EntityType COW = define("cow", ABSTRACT_ANIMAL);
    public static final EntityType CREEPER = define("creeper", ABSTRACT_MONSTER);
    public static final EntityType DOLPHIN = define("dolphin", ABSTRACT_INSENTIENT);
    public static final EntityType DONKEY = define("donkey", CHESTED_HORSE);
    public static final EntityType DRAGON_FIREBALL = define("dragon_fireball", ABSTRACT_FIREBALL);
    public static final EntityType ZOMBIE = define("zombie", ABSTRACT_MONSTER);
    public static final EntityType DROWNED = define("drowned", ZOMBIE);
    public static final EntityType GUARDIAN = define("guardian", ABSTRACT_MONSTER);
    public static final EntityType ELDER_GUARDIAN = define("elder_guardian", GUARDIAN);
    public static final EntityType END_CRYSTAL = define("end_crystal", ENTITY);
    public static final EntityType ENDER_DRAGON = define("ender_dragon", ABSTRACT_INSENTIENT);
    public static final EntityType ENDERMAN = define("enderman", ABSTRACT_MONSTER);
    public static final EntityType ENDERMITE = define("endermite", ABSTRACT_MONSTER);
    public static final EntityType EVOKER = define("evoker", ABSTRACT_EVO_ILLU_ILLAGER);
    public static final EntityType EVOKER_FANGS = define("evoker_fangs", ENTITY);
    public static final EntityType EXPERIENCE_ORB = define("experience_orb", ENTITY);
    public static final EntityType EYE_OF_ENDER = define("eye_of_ender", ENTITY);
    public static final EntityType FALLING_BLOCK = define("falling_block", ENTITY);
    public static final EntityType FIREWORK_ROCKET = define("firework_rocket", ENTITY);
    public static final EntityType FOX = define("fox", ABSTRACT_ANIMAL);
    public static final EntityType GHAST = define("ghast", ABSTRACT_FLYING);
    public static final EntityType GIANT = define("giant", ABSTRACT_MONSTER);
    public static final EntityType ITEM_FRAME = define("item_frame", ABSTRACT_HANGING);
    public static final EntityType GLOW_ITEM_FRAME = define("glow_item_frame", ITEM_FRAME);
    public static final EntityType SQUID = define("squid", ABSTRACT_WATERMOB);
    public static final EntityType GLOW_SQUID = define("glow_squid", SQUID);
    public static final EntityType GOAT = define("goat", ABSTRACT_ANIMAL);
    public static final EntityType HOGLIN = define("hoglin", ABSTRACT_ANIMAL);
    public static final EntityType HORSE = define("horse", ABSTRACT_HORSE);
    public static final EntityType HUSK = define("husk", ZOMBIE);
    public static final EntityType ILLUSIONER = define("illusioner", ABSTRACT_EVO_ILLU_ILLAGER);
    public static final EntityType IRON_GOLEM = define("iron_golem", ABSTRACT_GOLEM);
    public static final EntityType ITEM = define("item", ENTITY);
    public static final EntityType FIREBALL = define("fireball", ABSTRACT_FIREBALL);
    public static final EntityType LEASH_KNOT = define("leash_knot", ABSTRACT_HANGING);
    public static final EntityType LIGHTNING_BOLT = define("lightning_bolt", ABSTRACT_LIGHTNING);
    public static final EntityType LLAMA = define("llama", CHESTED_HORSE);
    public static final EntityType LLAMA_SPIT = define("llama_spit", ENTITY);
    public static final EntityType SLIME = define("slime", ABSTRACT_INSENTIENT);
    public static final EntityType MAGMA_CUBE = define("magma_cube", SLIME);
    public static final EntityType MARKER = define("marker", ENTITY);
    public static final EntityType MINECART = define("minecart", MINECART_ABSTRACT);
    public static final EntityType CHEST_MINECART = define("chest_minecart", CHESTED_MINECART_ABSTRACT);
    public static final EntityType COMMAND_BLOCK_MINECART = define("command_block_minecart", MINECART_ABSTRACT);
    public static final EntityType FURNACE_MINECART = define("furnace_minecart", MINECART_ABSTRACT);
    public static final EntityType HOPPER_MINECART = define("hopper_minecart", CHESTED_MINECART_ABSTRACT);
    public static final EntityType SPAWNER_MINECART = define("spawner_minecart", MINECART_ABSTRACT);
    public static final EntityType TNT_MINECART = define("tnt_minecart", MINECART_ABSTRACT);
    public static final EntityType MULE = define("mule", CHESTED_HORSE);
    public static final EntityType MOOSHROOM = define("mooshroom", COW);
    public static final EntityType OCELOT = define("ocelot", ABSTRACT_TAMEABLE_ANIMAL);
    public static final EntityType PAINTING = define("painting", ABSTRACT_HANGING);
    public static final EntityType PANDA = define("panda", ABSTRACT_INSENTIENT);
    public static final EntityType PARROT = define("parrot", ABSTRACT_PARROT);
    public static final EntityType PHANTOM = define("phantom", ABSTRACT_FLYING);
    public static final EntityType PIG = define("pig", ABSTRACT_ANIMAL);
    public static final EntityType PIGLIN = define("piglin", ABSTRACT_PIGLIN);
    public static final EntityType PIGLIN_BRUTE = define("piglin_brute", ABSTRACT_PIGLIN);
    public static final EntityType PILLAGER = define("pillager", ABSTRACT_ILLAGER_BASE);
    public static final EntityType POLAR_BEAR = define("polar_bear", ABSTRACT_ANIMAL);
    public static final EntityType TNT = define("tnt", ENTITY);
    public static final EntityType PUFFERFISH = define("pufferfish", ABSTRACT_FISHES);
    public static final EntityType RABBIT = define("rabbit", ABSTRACT_ANIMAL);
    public static final EntityType RAVAGER = define("ravager", ABSTRACT_MONSTER);
    public static final EntityType SALMON = define("salmon", ABSTRACT_FISHES);
    public static final EntityType SHEEP = define("sheep", ABSTRACT_ANIMAL);
    public static final EntityType SHULKER = define("shulker", ABSTRACT_GOLEM); // yes this is correct
    public static final EntityType SHULKER_BULLET = define("shulker_bullet", ENTITY);
    public static final EntityType SILVERFISH = define("silverfish", ABSTRACT_MONSTER);
    public static final EntityType SKELETON = define("skeleton", ABSTRACT_SKELETON);
    public static final EntityType SKELETON_HORSE = define("skeleton_horse", ABSTRACT_HORSE);
    public static final EntityType SMALL_FIREBALL = define("small_fireball", ABSTRACT_FIREBALL);
    public static final EntityType SNOW_GOLEM = define("snow_golem", ABSTRACT_GOLEM);
    public static final EntityType SNOWBALL = define("snowball", PROJECTILE_ABSTRACT);
    public static final EntityType SPECTRAL_ARROW = define("spectral_arrow", ABSTRACT_ARROW);
    public static final EntityType STRAY = define("stray", ABSTRACT_SKELETON);
    public static final EntityType STRIDER = define("strider", ABSTRACT_ANIMAL);
    public static final EntityType EGG = define("egg", PROJECTILE_ABSTRACT);
    public static final EntityType ENDER_PEARL = define("ender_pearl", PROJECTILE_ABSTRACT);
    public static final EntityType EXPERIENCE_BOTTLE = define("experience_bottle", PROJECTILE_ABSTRACT);
    public static final EntityType POTION = define("potion", PROJECTILE_ABSTRACT);
    public static final EntityType TRIDENT = define("trident", ABSTRACT_ARROW);
    public static final EntityType TRADER_LLAMA = define("trader_llama", CHESTED_HORSE);
    public static final EntityType TROPICAL_FISH = define("tropical_fish", ABSTRACT_FISHES);
    public static final EntityType TURTLE = define("turtle", ABSTRACT_ANIMAL);
    public static final EntityType VEX = define("vex", ABSTRACT_MONSTER);
    public static final EntityType VILLAGER = define("villager", ABSTRACT_AGEABLE);
    public static final EntityType VINDICATOR = define("vindicator", ABSTRACT_ILLAGER_BASE);
    public static final EntityType WANDERING_TRADER = define("wandering_trader", ABSTRACT_AGEABLE);
    public static final EntityType WITCH = define("witch", ABSTRACT_MONSTER);
    public static final EntityType WITHER = define("wither", ABSTRACT_MONSTER);
    public static final EntityType WITHER_SKELETON = define("wither_skeleton", ABSTRACT_SKELETON);
    public static final EntityType WITHER_SKULL = define("wither_skull", ABSTRACT_FIREBALL);
    public static final EntityType WOLF = define("wolf", ABSTRACT_TAMEABLE_ANIMAL);
    public static final EntityType ZOGLIN = define("zoglin", ABSTRACT_MONSTER);
    public static final EntityType ZOMBIE_HORSE = define("zombie_horse", ABSTRACT_HORSE);
    public static final EntityType ZOMBIE_VILLAGER = define("zombie_villager", ZOMBIE);
    public static final EntityType ZOMBIFIED_PIGLIN = define("zombified_piglin", ZOMBIE);
    public static final EntityType PLAYER = define("player", LIVINGENTITY);
    public static final EntityType FISHING_BOBBER = define("fishing_bobber", ENTITY);
}
