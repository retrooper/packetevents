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

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

/**
 * A node in the Minecraft entity class hierarchy. Mirrors the Java inheritance
 * structure of Minecraft's entity classes (Entity → LivingEntity → Mob → ...).
 * <p>
 * Unlike {@link EntityType}, this is NOT a registry entry. It carries no
 * protocol
 * IDs, no versioned mappings, and is not stored in any registry. It exists
 * purely
 * for type-checking via {@link EntityType#isInstanceOf(EntityClass)}.
 * <p>
 * Design inspired by ViaVersion's entity type system.
 *
 * @see EntityHierarchy All predefined hierarchy nodes
 */
public final class EntityClass {

	private final String name;
	private final @Nullable EntityClass parent;

	EntityClass(String name, @Nullable EntityClass parent) {
		this.name = name;
		this.parent = parent;
	}

	public String getName() {
		return this.name;
	}

	public @Nullable EntityClass getParent() {
		return this.parent;
	}

	/**
	 * Returns true if {@code possibleChild} is this class or a descendant.
	 * Walks the parent chain and compares by reference equality.
	 *
	 * @throws NullPointerException if possibleChild is null
	 * @see Class#isAssignableFrom(Class)
	 */
	public boolean isAssignableFrom(EntityClass possibleChild) {
		Objects.requireNonNull(possibleChild, "possibleChild");
		EntityClass current = possibleChild;
		while (current != null) {
			if (current == this)
				return true;
			current = current.parent;
		}
		return false;
	}

	@Override
	public String toString() {
		return "EntityClass{" + name + "}";
	}
}
