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

package com.github.retrooper.packetevents.protocol.entity.type;

import java.util.Collection;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;

// bejiihiu loves node.js

public final class EntityTypes {

	private static final VersionedRegistry<EntityType> REGISTRY = new VersionedRegistry<>("entity_type");
	private static final VersionedRegistry<EntityType> LEGACY_SPAWN_REGISTRY = new VersionedRegistry<>(
			"legacy_spawn_entity_type");

	private EntityTypes() {
	}

	public static VersionedRegistry<EntityType> getRegistry() {
		return REGISTRY;
	}

	@ApiStatus.Obsolete
	public static VersionedRegistry<EntityType> getLegacySpawnRegistry() {
		return LEGACY_SPAWN_REGISTRY;
	}

	@ApiStatus.Internal
	public static EntityType define(String name, EntityClass clazz) {
		return define(name, clazz, null);
	}

	@ApiStatus.Internal
	public static EntityType define(String name, EntityClass clazz, @Nullable EntityType concreteParent) {
		StaticEntityType type = REGISTRY.define(name, data -> new StaticEntityType(data, clazz, concreteParent));
		return LEGACY_SPAWN_REGISTRY.define(name, type::setLegacyData);
	}

	@Deprecated
	private static EntityType bridge(EntityClass clazz) {
		return new StaticEntityType(null, clazz);
	}

	@SuppressWarnings("deprecation")
	public static boolean isTypeInstanceOf(EntityType type, EntityType parent) {
		return type != null && type.isInstanceOf(parent);
	}

	public static EntityType getByName(String name) {
		return REGISTRY.getByName(name);
	}

	public static EntityType getById(ClientVersion version, int id) {
		return REGISTRY.getById(version, id);
	}

	@ApiStatus.Obsolete
	public static EntityType getByLegacyId(ClientVersion version, int id) {
		if (version.isNewerThanOrEquals(ClientVersion.V_1_14)) {
			return null;
		}
		return LEGACY_SPAWN_REGISTRY.getById(version, id);
	}

	// ═══════════════════════════════════════════════
	// DEPRECATED HIERARCHY BRIDGES
	// These constants exist only for backward compat.
	// Use EntityHierarchy.XYZ instead.
	// ═══════════════════════════════════════════════

	/**
	 * @deprecated Use {@link EntityHierarchy#ENTITY}
	 */
	@Deprecated
	public static final EntityType ENTITY = bridge(EntityHierarchy.ENTITY);
	/**
	 * @deprecated Use {@link EntityHierarchy#LIVING_ENTITY}
	 */
	@Deprecated
	public static final EntityType LIVING_ENTITY = bridge(EntityHierarchy.LIVING_ENTITY);
	/**
	 * @deprecated Use {@link EntityHierarchy#LIVING_ENTITY}
	 */
	@Deprecated
	public static final EntityType LIVINGENTITY = LIVING_ENTITY;
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_INSENTIENT}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_INSENTIENT = bridge(EntityHierarchy.ABSTRACT_INSENTIENT);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_CREATURE}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_CREATURE = bridge(EntityHierarchy.ABSTRACT_CREATURE);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_AGEABLE}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_AGEABLE = bridge(EntityHierarchy.ABSTRACT_AGEABLE);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_ANIMAL}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_ANIMAL = bridge(EntityHierarchy.ABSTRACT_ANIMAL);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_TAMEABLE_ANIMAL}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_TAMEABLE_ANIMAL = bridge(EntityHierarchy.ABSTRACT_TAMEABLE_ANIMAL);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_PARROT}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_PARROT = bridge(EntityHierarchy.ABSTRACT_PARROT);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_HORSE}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_HORSE = bridge(EntityHierarchy.ABSTRACT_HORSE);
	/**
	 * @deprecated Use {@link EntityHierarchy#CHESTED_HORSE}
	 */
	@Deprecated
	public static final EntityType CHESTED_HORSE = bridge(EntityHierarchy.CHESTED_HORSE);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_GOLEM}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_GOLEM = bridge(EntityHierarchy.ABSTRACT_GOLEM);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_FISHES}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_FISHES = bridge(EntityHierarchy.ABSTRACT_FISHES);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_MONSTER}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_MONSTER = bridge(EntityHierarchy.ABSTRACT_MONSTER);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_PIGLIN}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_PIGLIN = bridge(EntityHierarchy.ABSTRACT_PIGLIN);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_ILLAGER}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_ILLAGER_BASE = bridge(EntityHierarchy.ABSTRACT_ILLAGER);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_EVO_ILLU_ILLAGER}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_EVO_ILLU_ILLAGER = bridge(EntityHierarchy.ABSTRACT_EVO_ILLU_ILLAGER);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_SKELETON}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_SKELETON = bridge(EntityHierarchy.ABSTRACT_SKELETON);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_FLYING}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_FLYING = bridge(EntityHierarchy.ABSTRACT_FLYING);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_AMBIENT}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_AMBIENT = bridge(EntityHierarchy.ABSTRACT_AMBIENT);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_WATERMOB}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_WATERMOB = bridge(EntityHierarchy.ABSTRACT_WATERMOB);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_HANGING}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_HANGING = bridge(EntityHierarchy.ABSTRACT_HANGING);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_LIGHTNING}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_LIGHTNING = bridge(EntityHierarchy.ABSTRACT_LIGHTNING);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_ARROW}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_ARROW = bridge(EntityHierarchy.ABSTRACT_ARROW);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_FIREBALL}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_FIREBALL = bridge(EntityHierarchy.ABSTRACT_FIREBALL);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_PROJECTILE}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_PROJECTILE = bridge(EntityHierarchy.ABSTRACT_PROJECTILE);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_PROJECTILE}
	 */
	@Deprecated
	public static final EntityType PROJECTILE_ABSTRACT = ABSTRACT_PROJECTILE;
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_MINECART}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_MINECART = bridge(EntityHierarchy.ABSTRACT_MINECART);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_MINECART}
	 */
	@Deprecated
	public static final EntityType MINECART_ABSTRACT = ABSTRACT_MINECART;
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_CHESTED_MINECART}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_CHESTED_MINECART = bridge(EntityHierarchy.ABSTRACT_CHESTED_MINECART);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_CHESTED_MINECART}
	 */
	@Deprecated
	public static final EntityType CHESTED_MINECART_ABSTRACT = ABSTRACT_CHESTED_MINECART;
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_BOAT}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_BOAT = bridge(EntityHierarchy.ABSTRACT_BOAT);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_CHEST_BOAT}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_CHEST_BOAT = bridge(EntityHierarchy.ABSTRACT_CHEST_BOAT);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_WIND_CHARGE}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_WIND_CHARGE = bridge(EntityHierarchy.ABSTRACT_WIND_CHARGE);
	/**
	 * @deprecated Use {@link EntityHierarchy#ABSTRACT_NAUTILUS}
	 */
	@Deprecated
	public static final EntityType ABSTRACT_NAUTILUS = bridge(EntityHierarchy.ABSTRACT_NAUTILUS);
	/**
	 * @deprecated Use {@link EntityHierarchy#AVATAR}
	 */
	@Deprecated
	public static final EntityType AVATAR = bridge(EntityHierarchy.AVATAR);
	/**
	 * @deprecated Use {@link EntityHierarchy#DISPLAY}
	 */
	@Deprecated
	public static final EntityType DISPLAY = bridge(EntityHierarchy.DISPLAY);

	// ═══════════════════════════════════════════════
	// CONCRETE TYPES (from minecraft:entity_type)
	// Every entry here is a real registry entry.
	// ═══════════════════════════════════════════════

	public static final EntityType AREA_EFFECT_CLOUD = define("area_effect_cloud", EntityHierarchy.ENTITY);
	public static final EntityType ARMOR_STAND = define("armor_stand", EntityHierarchy.LIVING_ENTITY);
	public static final EntityType ALLAY = define("allay", EntityHierarchy.ABSTRACT_CREATURE);
	public static final EntityType ARROW = define("arrow", EntityHierarchy.ABSTRACT_ARROW);
	public static final EntityType AXOLOTL = define("axolotl", EntityHierarchy.ABSTRACT_ANIMAL);
	public static final EntityType BAT = define("bat", EntityHierarchy.ABSTRACT_AMBIENT);
	public static final EntityType BEE = define("bee", EntityHierarchy.ABSTRACT_INSENTIENT);
	public static final EntityType BLAZE = define("blaze", EntityHierarchy.ABSTRACT_MONSTER);
	/**
	 * <strong>WARNING:</strong> Does not exist itself anymore since 1.21.2
	 */
	public static final EntityType BOAT = define("boat", EntityHierarchy.ABSTRACT_BOAT);
	/**
	 * <strong>WARNING:</strong> Does not exist itself anymore since 1.21.2
	 */
	public static final EntityType CHEST_BOAT = define("chest_boat", EntityHierarchy.ABSTRACT_CHEST_BOAT);
	public static final EntityType CAT = define("cat", EntityHierarchy.ABSTRACT_TAMEABLE_ANIMAL);
	public static final EntityType CAMEL = define("camel", EntityHierarchy.ABSTRACT_HORSE);
	public static final EntityType SPIDER = define("spider", EntityHierarchy.ABSTRACT_MONSTER);
	public static final EntityType CAVE_SPIDER = define("cave_spider", EntityHierarchy.ABSTRACT_MONSTER, SPIDER);
	public static final EntityType CHICKEN = define("chicken", EntityHierarchy.ABSTRACT_ANIMAL);
	public static final EntityType COD = define("cod", EntityHierarchy.ABSTRACT_FISHES);
	public static final EntityType COW = define("cow", EntityHierarchy.ABSTRACT_ANIMAL);
	public static final EntityType CREEPER = define("creeper", EntityHierarchy.ABSTRACT_MONSTER);
	public static final EntityType DOLPHIN = define("dolphin", EntityHierarchy.ABSTRACT_INSENTIENT);
	public static final EntityType DONKEY = define("donkey", EntityHierarchy.CHESTED_HORSE);
	public static final EntityType DRAGON_FIREBALL = define("dragon_fireball", EntityHierarchy.ABSTRACT_FIREBALL);
	public static final EntityType ZOMBIE = define("zombie", EntityHierarchy.ABSTRACT_MONSTER);
	public static final EntityType DROWNED = define("drowned", EntityHierarchy.ABSTRACT_MONSTER, ZOMBIE);
	public static final EntityType GUARDIAN = define("guardian", EntityHierarchy.ABSTRACT_MONSTER);
	public static final EntityType ELDER_GUARDIAN = define("elder_guardian", EntityHierarchy.ABSTRACT_MONSTER,
			GUARDIAN);
	public static final EntityType END_CRYSTAL = define("end_crystal", EntityHierarchy.ENTITY);
	public static final EntityType ENDER_DRAGON = define("ender_dragon", EntityHierarchy.ABSTRACT_INSENTIENT);
	public static final EntityType ENDERMAN = define("enderman", EntityHierarchy.ABSTRACT_MONSTER);
	public static final EntityType ENDERMITE = define("endermite", EntityHierarchy.ABSTRACT_MONSTER);
	public static final EntityType EVOKER = define("evoker", EntityHierarchy.ABSTRACT_EVO_ILLU_ILLAGER);
	public static final EntityType EVOKER_FANGS = define("evoker_fangs", EntityHierarchy.ENTITY);
	public static final EntityType EXPERIENCE_ORB = define("experience_orb", EntityHierarchy.ENTITY);
	public static final EntityType EYE_OF_ENDER = define("eye_of_ender", EntityHierarchy.ENTITY);
	public static final EntityType FALLING_BLOCK = define("falling_block", EntityHierarchy.ENTITY);
	public static final EntityType FIREWORK_ROCKET = define("firework_rocket", EntityHierarchy.ENTITY);
	public static final EntityType FOX = define("fox", EntityHierarchy.ABSTRACT_ANIMAL);
	public static final EntityType FROG = define("frog", EntityHierarchy.ABSTRACT_ANIMAL);
	public static final EntityType GHAST = define("ghast", EntityHierarchy.ABSTRACT_FLYING);
	public static final EntityType GIANT = define("giant", EntityHierarchy.ABSTRACT_MONSTER);
	public static final EntityType ITEM_FRAME = define("item_frame", EntityHierarchy.ABSTRACT_HANGING);
	public static final EntityType GLOW_ITEM_FRAME = define("glow_item_frame", EntityHierarchy.ABSTRACT_HANGING,
			ITEM_FRAME);
	public static final EntityType SQUID = define("squid", EntityHierarchy.ABSTRACT_WATERMOB);
	public static final EntityType GLOW_SQUID = define("glow_squid", EntityHierarchy.ABSTRACT_WATERMOB, SQUID);
	public static final EntityType GOAT = define("goat", EntityHierarchy.ABSTRACT_ANIMAL);
	public static final EntityType HOGLIN = define("hoglin", EntityHierarchy.ABSTRACT_ANIMAL);
	public static final EntityType HORSE = define("horse", EntityHierarchy.ABSTRACT_HORSE);
	public static final EntityType HUSK = define("husk", EntityHierarchy.ABSTRACT_MONSTER, ZOMBIE);
	public static final EntityType ILLUSIONER = define("illusioner", EntityHierarchy.ABSTRACT_EVO_ILLU_ILLAGER);
	public static final EntityType IRON_GOLEM = define("iron_golem", EntityHierarchy.ABSTRACT_GOLEM);
	public static final EntityType ITEM = define("item", EntityHierarchy.ENTITY);
	public static final EntityType FIREBALL = define("fireball", EntityHierarchy.ABSTRACT_FIREBALL);
	public static final EntityType LEASH_KNOT = define("leash_knot", EntityHierarchy.ABSTRACT_HANGING);
	public static final EntityType LIGHTNING_BOLT = define("lightning_bolt", EntityHierarchy.ABSTRACT_LIGHTNING);
	public static final EntityType LLAMA = define("llama", EntityHierarchy.CHESTED_HORSE);
	public static final EntityType LLAMA_SPIT = define("llama_spit", EntityHierarchy.ENTITY);
	public static final EntityType SLIME = define("slime", EntityHierarchy.ABSTRACT_INSENTIENT);
	public static final EntityType MAGMA_CUBE = define("magma_cube", EntityHierarchy.ABSTRACT_INSENTIENT, SLIME);
	public static final EntityType MARKER = define("marker", EntityHierarchy.ENTITY);
	public static final EntityType MINECART = define("minecart", EntityHierarchy.ABSTRACT_MINECART);
	public static final EntityType CHEST_MINECART = define("chest_minecart", EntityHierarchy.ABSTRACT_CHESTED_MINECART);
	public static final EntityType COMMAND_BLOCK_MINECART = define("command_block_minecart",
			EntityHierarchy.ABSTRACT_MINECART);
	public static final EntityType FURNACE_MINECART = define("furnace_minecart", EntityHierarchy.ABSTRACT_MINECART);
	public static final EntityType HOPPER_MINECART = define("hopper_minecart",
			EntityHierarchy.ABSTRACT_CHESTED_MINECART);
	public static final EntityType SPAWNER_MINECART = define("spawner_minecart", EntityHierarchy.ABSTRACT_MINECART);
	public static final EntityType TNT_MINECART = define("tnt_minecart", EntityHierarchy.ABSTRACT_MINECART);
	public static final EntityType MULE = define("mule", EntityHierarchy.CHESTED_HORSE);
	public static final EntityType MOOSHROOM = define("mooshroom", EntityHierarchy.ABSTRACT_ANIMAL, COW);
	public static final EntityType OCELOT = define("ocelot", EntityHierarchy.ABSTRACT_TAMEABLE_ANIMAL);
	public static final EntityType PAINTING = define("painting", EntityHierarchy.ABSTRACT_HANGING);
	public static final EntityType PANDA = define("panda", EntityHierarchy.ABSTRACT_INSENTIENT);
	public static final EntityType PARROT = define("parrot", EntityHierarchy.ABSTRACT_PARROT);
	public static final EntityType PHANTOM = define("phantom", EntityHierarchy.ABSTRACT_FLYING);
	public static final EntityType PIG = define("pig", EntityHierarchy.ABSTRACT_ANIMAL);
	public static final EntityType PIGLIN = define("piglin", EntityHierarchy.ABSTRACT_PIGLIN);
	public static final EntityType PIGLIN_BRUTE = define("piglin_brute", EntityHierarchy.ABSTRACT_PIGLIN);
	public static final EntityType PILLAGER = define("pillager", EntityHierarchy.ABSTRACT_ILLAGER);
	public static final EntityType POLAR_BEAR = define("polar_bear", EntityHierarchy.ABSTRACT_ANIMAL);
	public static final EntityType TNT = define("tnt", EntityHierarchy.ENTITY);
	public static final EntityType PUFFERFISH = define("pufferfish", EntityHierarchy.ABSTRACT_FISHES);
	public static final EntityType RABBIT = define("rabbit", EntityHierarchy.ABSTRACT_ANIMAL);
	public static final EntityType RAVAGER = define("ravager", EntityHierarchy.ABSTRACT_MONSTER);
	public static final EntityType SALMON = define("salmon", EntityHierarchy.ABSTRACT_FISHES);
	public static final EntityType SHEEP = define("sheep", EntityHierarchy.ABSTRACT_ANIMAL);
	public static final EntityType SHULKER = define("shulker", EntityHierarchy.ABSTRACT_GOLEM); // yes this is correct
	public static final EntityType SHULKER_BULLET = define("shulker_bullet", EntityHierarchy.ENTITY);
	public static final EntityType SILVERFISH = define("silverfish", EntityHierarchy.ABSTRACT_MONSTER);
	public static final EntityType SKELETON = define("skeleton", EntityHierarchy.ABSTRACT_SKELETON);
	public static final EntityType SKELETON_HORSE = define("skeleton_horse", EntityHierarchy.ABSTRACT_HORSE);
	public static final EntityType SMALL_FIREBALL = define("small_fireball", EntityHierarchy.ABSTRACT_FIREBALL);
	public static final EntityType SNOW_GOLEM = define("snow_golem", EntityHierarchy.ABSTRACT_GOLEM);
	public static final EntityType SNOWBALL = define("snowball", EntityHierarchy.ABSTRACT_PROJECTILE);
	public static final EntityType SPECTRAL_ARROW = define("spectral_arrow", EntityHierarchy.ABSTRACT_ARROW);
	public static final EntityType STRAY = define("stray", EntityHierarchy.ABSTRACT_SKELETON);
	public static final EntityType STRIDER = define("strider", EntityHierarchy.ABSTRACT_ANIMAL);
	public static final EntityType EGG = define("egg", EntityHierarchy.ABSTRACT_PROJECTILE);
	public static final EntityType ENDER_PEARL = define("ender_pearl", EntityHierarchy.ABSTRACT_PROJECTILE);
	public static final EntityType EXPERIENCE_BOTTLE = define("experience_bottle", EntityHierarchy.ABSTRACT_PROJECTILE);
	/**
	 * <strong>WARNING:</strong> Does not exist itself anymore since 1.21.5, this
	 * has
	 * been split into {@link #SPLASH_POTION} and {@link #LINGERING_POTION}
	 */
	public static final EntityType POTION = define("potion", EntityHierarchy.ABSTRACT_PROJECTILE);
	public static final EntityType TADPOLE = define("tadpole", EntityHierarchy.ABSTRACT_FISHES);
	@Deprecated // Exists only in 1.9 and 1.10
	public static final EntityType TIPPED_ARROW = define("tipped_arrow", EntityHierarchy.ABSTRACT_ARROW, ARROW);
	public static final EntityType TRIDENT = define("trident", EntityHierarchy.ABSTRACT_ARROW);
	public static final EntityType TRADER_LLAMA = define("trader_llama", EntityHierarchy.CHESTED_HORSE, LLAMA);
	public static final EntityType TROPICAL_FISH = define("tropical_fish", EntityHierarchy.ABSTRACT_FISHES);
	public static final EntityType TURTLE = define("turtle", EntityHierarchy.ABSTRACT_ANIMAL);
	public static final EntityType VEX = define("vex", EntityHierarchy.ABSTRACT_MONSTER);
	public static final EntityType VILLAGER = define("villager", EntityHierarchy.ABSTRACT_AGEABLE);
	public static final EntityType VINDICATOR = define("vindicator", EntityHierarchy.ABSTRACT_ILLAGER);
	public static final EntityType WANDERING_TRADER = define("wandering_trader", EntityHierarchy.ABSTRACT_AGEABLE);
	public static final EntityType WARDEN = define("warden", EntityHierarchy.ABSTRACT_MONSTER);
	public static final EntityType WITCH = define("witch", EntityHierarchy.ABSTRACT_MONSTER);
	public static final EntityType WITHER = define("wither", EntityHierarchy.ABSTRACT_MONSTER);
	public static final EntityType WITHER_SKELETON = define("wither_skeleton", EntityHierarchy.ABSTRACT_SKELETON);
	public static final EntityType WITHER_SKULL = define("wither_skull", EntityHierarchy.ABSTRACT_FIREBALL);
	public static final EntityType WOLF = define("wolf", EntityHierarchy.ABSTRACT_TAMEABLE_ANIMAL);
	public static final EntityType ZOGLIN = define("zoglin", EntityHierarchy.ABSTRACT_MONSTER);
	public static final EntityType ZOMBIE_HORSE = define("zombie_horse", EntityHierarchy.ABSTRACT_HORSE);
	public static final EntityType ZOMBIE_VILLAGER = define("zombie_villager", EntityHierarchy.ABSTRACT_MONSTER,
			ZOMBIE);
	public static final EntityType ZOMBIFIED_PIGLIN = define("zombified_piglin", EntityHierarchy.ABSTRACT_MONSTER,
			ZOMBIE);
	public static final EntityType PLAYER = define("player", EntityHierarchy.AVATAR);
	public static final EntityType FISHING_BOBBER = define("fishing_bobber", EntityHierarchy.ENTITY);
	public static final EntityType ENDER_SIGNAL = define("ender_signal", EntityHierarchy.ENTITY);
	public static final EntityType THROWN_EXP_BOTTLE = define("thrown_exp_bottle", EntityHierarchy.ABSTRACT_PROJECTILE);
	public static final EntityType PRIMED_TNT = define("primed_tnt", EntityHierarchy.ENTITY);
	public static final EntityType FIREWORK = define("firework", EntityHierarchy.ENTITY);
	public static final EntityType MINECART_COMMAND = define("minecart_command", EntityHierarchy.ABSTRACT_MINECART);
	public static final EntityType MINECART_RIDEABLE = define("minecart_rideable", EntityHierarchy.ABSTRACT_MINECART);
	public static final EntityType MINECART_CHEST = define("minecart_chest", EntityHierarchy.ABSTRACT_MINECART);
	public static final EntityType MINECART_FURNACE = define("minecart_furnace", EntityHierarchy.ABSTRACT_MINECART);
	public static final EntityType MINECART_TNT = define("minecart_tnt", EntityHierarchy.ABSTRACT_MINECART);
	public static final EntityType MINECART_HOPPER = define("minecart_hopper", EntityHierarchy.ABSTRACT_MINECART);
	public static final EntityType MINECART_MOB_SPAWNER = define("minecart_mob_spawner",
			EntityHierarchy.ABSTRACT_MINECART);
	/**
	 * @versions 1.19.4+
	 */
	public static final EntityType BLOCK_DISPLAY = define("block_display", EntityHierarchy.DISPLAY);
	/**
	 * @versions 1.19.4+
	 */
	public static final EntityType ITEM_DISPLAY = define("item_display", EntityHierarchy.DISPLAY);
	/**
	 * @versions 1.19.4+
	 */
	public static final EntityType TEXT_DISPLAY = define("text_display", EntityHierarchy.DISPLAY);
	/**
	 * @versions 1.19.4+
	 */
	public static final EntityType INTERACTION = define("interaction", EntityHierarchy.ENTITY);
	/**
	 * @versions 1.19.4+
	 */
	public static final EntityType SNIFFER = define("sniffer", EntityHierarchy.ABSTRACT_ANIMAL);
	/**
	 * @versions 1.20.3+
	 */
	public static final EntityType BREEZE = define("breeze", EntityHierarchy.ABSTRACT_MONSTER);
	/**
	 * @versions 1.20.3+
	 */
	public static final EntityType WIND_CHARGE = define("wind_charge", EntityHierarchy.ABSTRACT_WIND_CHARGE);
	/**
	 * @versions 1.20.5+
	 */
	public static final EntityType ARMADILLO = define("armadillo", EntityHierarchy.ABSTRACT_ANIMAL);
	/**
	 * @versions 1.20.5+
	 */
	public static final EntityType BOGGED = define("bogged", EntityHierarchy.ABSTRACT_SKELETON);
	/**
	 * @versions 1.20.5+
	 */
	public static final EntityType BREEZE_WIND_CHARGE = define("breeze_wind_charge",
			EntityHierarchy.ABSTRACT_WIND_CHARGE);
	/**
	 * @versions 1.20.5+
	 */
	public static final EntityType OMINOUS_ITEM_SPAWNER = define("ominous_item_spawner", EntityHierarchy.ENTITY);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType ACACIA_BOAT = define("acacia_boat", EntityHierarchy.ABSTRACT_BOAT, BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType ACACIA_CHEST_BOAT = define("acacia_chest_boat", EntityHierarchy.ABSTRACT_CHEST_BOAT,
			CHEST_BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType BAMBOO_CHEST_RAFT = define("bamboo_chest_raft", EntityHierarchy.ABSTRACT_CHEST_BOAT,
			CHEST_BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType BAMBOO_RAFT = define("bamboo_raft", EntityHierarchy.ABSTRACT_BOAT, BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType BIRCH_BOAT = define("birch_boat", EntityHierarchy.ABSTRACT_BOAT, BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType BIRCH_CHEST_BOAT = define("birch_chest_boat", EntityHierarchy.ABSTRACT_CHEST_BOAT,
			CHEST_BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType CHERRY_BOAT = define("cherry_boat", EntityHierarchy.ABSTRACT_BOAT, BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType CHERRY_CHEST_BOAT = define("cherry_chest_boat", EntityHierarchy.ABSTRACT_CHEST_BOAT,
			CHEST_BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType CREAKING = define("creaking", EntityHierarchy.ABSTRACT_MONSTER);
	/**
	 * @versions 1.21.2-1.21.3
	 */
	@ApiStatus.Obsolete
	public static final EntityType CREAKING_TRANSIENT = define("creaking_transient", EntityHierarchy.ABSTRACT_MONSTER,
			CREAKING);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType DARK_OAK_BOAT = define("dark_oak_boat", EntityHierarchy.ABSTRACT_BOAT, BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType DARK_OAK_CHEST_BOAT = define("dark_oak_chest_boat",
			EntityHierarchy.ABSTRACT_CHEST_BOAT, CHEST_BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType JUNGLE_BOAT = define("jungle_boat", EntityHierarchy.ABSTRACT_BOAT, BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType JUNGLE_CHEST_BOAT = define("jungle_chest_boat", EntityHierarchy.ABSTRACT_CHEST_BOAT,
			CHEST_BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType MANGROVE_BOAT = define("mangrove_boat", EntityHierarchy.ABSTRACT_BOAT, BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType MANGROVE_CHEST_BOAT = define("mangrove_chest_boat",
			EntityHierarchy.ABSTRACT_CHEST_BOAT, CHEST_BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType OAK_BOAT = define("oak_boat", EntityHierarchy.ABSTRACT_BOAT, BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType OAK_CHEST_BOAT = define("oak_chest_boat", EntityHierarchy.ABSTRACT_CHEST_BOAT,
			CHEST_BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType PALE_OAK_BOAT = define("pale_oak_boat", EntityHierarchy.ABSTRACT_BOAT, BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType PALE_OAK_CHEST_BOAT = define("pale_oak_chest_boat",
			EntityHierarchy.ABSTRACT_CHEST_BOAT, CHEST_BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType SPRUCE_BOAT = define("spruce_boat", EntityHierarchy.ABSTRACT_BOAT, BOAT);
	/**
	 * @versions 1.21.2+
	 */
	public static final EntityType SPRUCE_CHEST_BOAT = define("spruce_chest_boat", EntityHierarchy.ABSTRACT_CHEST_BOAT,
			CHEST_BOAT);
	/**
	 * @versions 1.21.5+
	 */
	public static final EntityType SPLASH_POTION = define("splash_potion", EntityHierarchy.ABSTRACT_PROJECTILE, POTION);
	/**
	 * @versions 1.21.5+
	 */
	public static final EntityType LINGERING_POTION = define("lingering_potion", EntityHierarchy.ABSTRACT_PROJECTILE,
			POTION);
	/**
	 * @versions 1.21.6+
	 */
	public static final EntityType HAPPY_GHAST = define("happy_ghast", EntityHierarchy.ABSTRACT_ANIMAL);
	/**
	 * @versions 1.21.9+
	 */
	public static final EntityType COPPER_GOLEM = define("copper_golem", EntityHierarchy.ABSTRACT_GOLEM);
	/**
	 * @versions 1.21.9+
	 */
	public static final EntityType MANNEQUIN = define("mannequin", EntityHierarchy.AVATAR);
	/**
	 * @versions 1.21.11+
	 */
	public static final EntityType CAMEL_HUSK = define("camel_husk", EntityHierarchy.ABSTRACT_HORSE, CAMEL);
	/**
	 * @versions 1.21.11+
	 */
	public static final EntityType NAUTILUS = define("nautilus", EntityHierarchy.ABSTRACT_NAUTILUS);
	/**
	 * @versions 1.21.11+
	 */
	public static final EntityType PARCHED = define("parched", EntityHierarchy.ABSTRACT_SKELETON);
	/**
	 * @versions 1.21.11+
	 */
	public static final EntityType ZOMBIE_NAUTILUS = define("zombie_nautilus", EntityHierarchy.ABSTRACT_NAUTILUS);
	/**
	 * @versions 26.2+
	 */
	public static final EntityType SULFUR_CUBE = define("sulfur_cube", EntityHierarchy.ABSTRACT_AGEABLE);

	/**
	 * Returns an immutable view of the concrete entity types.
	 * Every entry here is a real Minecraft registry entry with protocol IDs.
	 *
	 * @return all concrete entity types
	 */
	public static Collection<EntityType> values() {
		return REGISTRY.getEntries();
	}

	static {
		REGISTRY.unloadMappings();
		LEGACY_SPAWN_REGISTRY.unloadMappings();
	}
}
