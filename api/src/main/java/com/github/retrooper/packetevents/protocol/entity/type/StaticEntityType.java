/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;

public class StaticEntityType extends AbstractMappedEntity implements EntityType {

	private final EntityClass entityClass;
	private final Map<EntityClass, Boolean> classChain;
	private final @Nullable EntityType concreteParent;

	private @Nullable TypesBuilderData legacyData;

	@ApiStatus.Internal
	public StaticEntityType(@Nullable TypesBuilderData data, EntityClass entityClass) {
		this(data, entityClass, null);
	}

	@ApiStatus.Internal
	public StaticEntityType(@Nullable TypesBuilderData data, EntityClass entityClass,
			@Nullable EntityType concreteParent) {
		super(data);
		this.entityClass = entityClass;
		this.concreteParent = concreteParent;

		// build the class chain by walking the EntityClass hierarchy
		this.classChain = new IdentityHashMap<>();
		EntityClass cur = entityClass;
		while (cur != null) {
			this.classChain.put(cur, true);
			cur = cur.getParent();
		}
	}

	StaticEntityType setLegacyData(@Nullable TypesBuilderData legacyData) {
		this.legacyData = legacyData;
		return this;
	}

	@Override
	public EntityClass getEntityClass() {
		return this.entityClass;
	}

	@Override
	public boolean isInstanceOf(EntityClass clazz) {
		return clazz != null && this.classChain.containsKey(clazz);
	}

	@Override
	@Deprecated
	public boolean isInstanceOf(EntityType parent) {
		if (parent == null)
			return false;

		EntityClass parentClass = parent.getEntityClass();
		if (parentClass != null && this.classChain.containsKey(parentClass)) {
			return true;
		}

		// walk the concrete parent chain (DROWNED → ZOMBIE etc.)
		// the walk is needed when the concrete parent reference matters
		// (e.g. two types sharing the same EntityClass but unrelated)
		EntityType cur = this.concreteParent;
		while (cur != null) {
			if (cur == parent)
				return true;
			// note: instanceof guard is necessary because the EntityType interface
			// is public and could theoretically have non-StaticEntityType implementations
			if (cur instanceof StaticEntityType) {
				cur = ((StaticEntityType) cur).concreteParent;
			} else {
				cur = null;
			}
		}

		return false;
	}

	@Override
	@Deprecated
	public Optional<EntityType> getParent() {
		return Optional.ofNullable(this.concreteParent);
	}

	@Override
	public int getLegacyId(ClientVersion version) {
		if (version.isNewerThanOrEquals(ClientVersion.V_1_14)) {
			return -1;
		} else if (this.legacyData != null) {
			return this.legacyData.getId(version);
		}
		throw new UnsupportedOperationException();
	}
}
