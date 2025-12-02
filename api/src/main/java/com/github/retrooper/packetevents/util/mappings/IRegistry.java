/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.util.mappings;

import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.function.BiFunction;

@NullMarked
public interface IRegistry<T extends MappedEntity> extends BiFunction<ClientVersion, Integer, T> {

    default T getByNameOrThrow(ResourceLocation name) {
        T value = this.getByName(name);
        if (value == null) {
            throw new IllegalArgumentException("Can't resolve '" + name + "' in '" + this.getRegistryKey() + "'");
        }
        return value;
    }

    default @Nullable T getByName(ResourceLocation name) {
        return this.getByName(name.toString());
    }

    default T getByNameOrThrow(String name) {
        T value = this.getByName(name);
        if (value == null) {
            String normedName = ResourceLocation.normString(name);
            throw new IllegalArgumentException("Can't resolve '" + normedName + "' in '" + this.getRegistryKey() + "'");
        }
        return value;
    }

    @Nullable
    T getByName(String name);

    default T getByIdOrThrow(ClientVersion version, int id) {
        T value = this.getById(version, id);
        if (value == null) {
            throw new IllegalArgumentException("Can't resolve #" + id + " (" + version
                    + ") in '" + this.getRegistryKey() + "'");
        }
        return value;
    }

    @Nullable
    T getById(ClientVersion version, int id);

    default int getId(String entityName, ClientVersion version) {
        return this.getId(this.getByNameOrThrow(entityName), version);
    }

    int getId(MappedEntity entity, ClientVersion version);

    /**
     * Returns an immutable view of the registry entries.
     *
     * @return Registry entries
     */
    Collection<T> getEntries();

    int size();

    ResourceLocation getRegistryKey();

    @Override
    default T apply(ClientVersion version, Integer id) {
        return this.getByIdOrThrow(version, id);
    }
}
