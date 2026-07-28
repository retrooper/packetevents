/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

/**
 * Predefined {@link EntityClass} hierarchy nodes mirroring Minecraft's
 * entity class inheritance tree. These are NOT registry entries — they
 * are pure type-markers for use with
 * {@link EntityType#isInstanceOf(EntityClass)}.
 * <p>
 * Hierarchy modelled after Minecraft's class tree:
 * {@code Entity → LivingEntity → Mob → PathfinderMob → ...}
 * <p>
 * Credit to ViaVersion for the original category structure.
 */
public final class EntityHierarchy {

	private EntityHierarchy() {
	}

	// ───── Root ─────
	/** Root of all entity types. */
	public static final EntityClass ENTITY = new EntityClass("entity", null);

	// ───── Living entities ─────
	/** Base for all living entities (has health, can take damage). */
	public static final EntityClass LIVING_ENTITY = new EntityClass("living_entity", ENTITY);
	/** Living entities without AI pathfinding (slime, bee, ender dragon). */
	public static final EntityClass ABSTRACT_INSENTIENT = new EntityClass("abstract_insentient", LIVING_ENTITY);
	/** Living entities that can move and perceive. */
	public static final EntityClass ABSTRACT_CREATURE = new EntityClass("abstract_creature", ABSTRACT_INSENTIENT);
	/** Creature that can age (baby/adult). */
	public static final EntityClass ABSTRACT_AGEABLE = new EntityClass("abstract_ageable", ABSTRACT_CREATURE);
	/** Non-monster animal. */
	public static final EntityClass ABSTRACT_ANIMAL = new EntityClass("abstract_animal", ABSTRACT_AGEABLE);
	/** Animal that can be tamed. */
	public static final EntityClass ABSTRACT_TAMEABLE_ANIMAL = new EntityClass("abstract_tameable_animal",
			ABSTRACT_ANIMAL);
	/** Shoulder-riding tameable (parrot). */
	public static final EntityClass ABSTRACT_PARROT = new EntityClass("abstract_parrot", ABSTRACT_TAMEABLE_ANIMAL);
	/** Horse family. */
	public static final EntityClass ABSTRACT_HORSE = new EntityClass("abstract_horse", ABSTRACT_ANIMAL);
	/** Horses with inventory chest slot. */
	public static final EntityClass CHESTED_HORSE = new EntityClass("chested_horse", ABSTRACT_HORSE);
	/** Nautilus family (1.21.11+). */
	public static final EntityClass ABSTRACT_NAUTILUS = new EntityClass("abstract_nautilus", ABSTRACT_TAMEABLE_ANIMAL);
	/** Golem family. */
	public static final EntityClass ABSTRACT_GOLEM = new EntityClass("abstract_golem", ABSTRACT_CREATURE);
	/** Fish family (swim, require water). */
	public static final EntityClass ABSTRACT_FISHES = new EntityClass("abstract_fishes", ABSTRACT_CREATURE);
	/** Hostile monster. */
	public static final EntityClass ABSTRACT_MONSTER = new EntityClass("abstract_monster", ABSTRACT_CREATURE);
	/** Piglin family. */
	public static final EntityClass ABSTRACT_PIGLIN = new EntityClass("abstract_piglin", ABSTRACT_MONSTER);
	/** Illager base (pillager, vindicator). */
	public static final EntityClass ABSTRACT_ILLAGER = new EntityClass("abstract_illager_base", ABSTRACT_MONSTER);
	/** Evoker / illusioner. */
	public static final EntityClass ABSTRACT_EVO_ILLU_ILLAGER = new EntityClass("abstract_evo_illu_illager",
			ABSTRACT_ILLAGER);
	/** Skeleton family. */
	public static final EntityClass ABSTRACT_SKELETON = new EntityClass("abstract_skeleton", ABSTRACT_MONSTER);
	/** Flying mob (ghast, phantom). */
	public static final EntityClass ABSTRACT_FLYING = new EntityClass("abstract_flying", ABSTRACT_INSENTIENT);
	/** Ambient mob (bat). */
	public static final EntityClass ABSTRACT_AMBIENT = new EntityClass("abstract_ambient", ABSTRACT_INSENTIENT);
	/** Water-dwelling creature (squid, dolphin). */
	public static final EntityClass ABSTRACT_WATERMOB = new EntityClass("abstract_watermob", ABSTRACT_INSENTIENT);
	/** Player and mannequin (1.21.9+). */
	public static final EntityClass AVATAR = new EntityClass("avatar", LIVING_ENTITY);

	// ───── Non-living entities ─────
	/** Hanging entities (item frame, painting, leash knot). */
	public static final EntityClass ABSTRACT_HANGING = new EntityClass("abstract_hanging", ENTITY);
	/** Lightning bolt. */
	public static final EntityClass ABSTRACT_LIGHTNING = new EntityClass("abstract_lightning", ENTITY);
	/** Arrows and tridents. */
	public static final EntityClass ABSTRACT_ARROW = new EntityClass("abstract_arrow", ENTITY);
	/** Fireball entities. */
	public static final EntityClass ABSTRACT_FIREBALL = new EntityClass("abstract_fireball", ENTITY);
	/** Throwable projectiles (snowball, egg, potion, ender pearl). */
	public static final EntityClass ABSTRACT_PROJECTILE = new EntityClass("abstract_projectile", ENTITY);
	/** Wind charge projectiles (1.20.3+). */
	public static final EntityClass ABSTRACT_WIND_CHARGE = new EntityClass("abstract_wind_charge", ABSTRACT_PROJECTILE);
	/** Minecart family. */
	public static final EntityClass ABSTRACT_MINECART = new EntityClass("abstract_minecart", ENTITY);
	/** Minecart with chest-like inventory. */
	public static final EntityClass ABSTRACT_CHESTED_MINECART = new EntityClass("abstract_chested_minecart",
			ABSTRACT_MINECART);
	/** Boat family (pre-1.21.2 generic, post-1.21.2 as parent). */
	public static final EntityClass ABSTRACT_BOAT = new EntityClass("abstract_boat", ENTITY);
	/** Boat with chest. */
	public static final EntityClass ABSTRACT_CHEST_BOAT = new EntityClass("abstract_chest_boat", ABSTRACT_BOAT);
	/** Display entity (1.19.4+). */
	public static final EntityClass DISPLAY = new EntityClass("display", ENTITY);
}
