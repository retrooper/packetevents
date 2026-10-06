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
import com.github.retrooper.packetevents.util.IdTable;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@NullMarked
public final class SimpleRegistry<T extends MappedEntity> implements IRegistry<T> {

    private final ResourceLocation registryKey;
    private final Map<String, T> typeMap = new HashMap<>();
    private final Map<String, Integer> reverseTypeIdMap = new HashMap<>();
    private final IdTable<T> typeIdTable = new IdTable<>(1);
    private @Nullable Map<Integer, T> typeIdOverflow;

    public SimpleRegistry(String registryKey) {
        this(new ResourceLocation(registryKey));
    }

    public SimpleRegistry(ResourceLocation registryKey) {
        this.registryKey = registryKey;
    }

    @ApiStatus.Internal
    public <Z extends T> Z define(String name, int id, Z instance) {
        return this.define(new ResourceLocation(name), id, instance);
    }

    @ApiStatus.Internal
    public <Z extends T> Z define(ResourceLocation name, int id, Z instance) {
        String nameStr = name.toString();
        this.typeMap.put(nameStr, instance);
        this.reverseTypeIdMap.put(nameStr, id);
        if (id >= 0 && id < IdTable.MAX_ID) {
            this.typeIdTable.put(0, id, instance);
        } else {
            if (this.typeIdOverflow == null) {
                this.typeIdOverflow = new HashMap<>(4);
            }
            this.typeIdOverflow.put(id, instance);
        }
        return instance;
    }

    @Override
    public @Nullable T getByName(String name) {
        // prepend "minecraft:" prefix if no other namespace has been specified
        return this.typeMap.get(ResourceLocation.normString(name));
    }

    @Override
    public @Nullable T getById(ClientVersion version, int id) {
        T byId = this.typeIdTable.lookup(0, id);
        if (byId != null) {
            return byId;
        }
        return this.typeIdOverflow != null ? this.typeIdOverflow.get(id) : null;
    }

    @Override
    public int getId(String entityName, ClientVersion version) {
        // prepend "minecraft:" prefix if no other namespace has been specified
        String normedName = ResourceLocation.normString(entityName);
        return this.reverseTypeIdMap.getOrDefault(normedName, -1);
    }

    @Override
    public int getId(MappedEntity entity, ClientVersion version) {
        return this.getId(entity.getName().toString(), version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<T> getEntries() {
        return Collections.unmodifiableCollection(this.typeMap.values());
    }

    @Override
    public int size() {
        return this.typeMap.size();
    }

    @Override
    public ResourceLocation getRegistryKey() {
        return this.registryKey;
    }

    @Override
    public String toString() {
        return "SimpleRegistry[" + this.registryKey + ']';
    }
}
