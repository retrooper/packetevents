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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.MapUtil;
import com.github.retrooper.packetevents.util.VersionRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@NullMarked
public final class VersionedRegistry<T extends MappedEntity> implements IRegistry<T> {

    private static final String REGISTRY_MAPPINGS_PREFIX = "registries/";

    private final ResourceLocation registryKey;
    private final TypesBuilder typesBuilder;
    private final ClientVersion[] extraSteps;

    private final Map<String, T>[] typeNames;
    private final Map<Integer, T>[] typeIds;
    private final Set<T> entries = new HashSet<>();

    public VersionedRegistry(String registry) {
        this(registry, new ClientVersion[0]);
    }

    public VersionedRegistry(String registry, ClientVersion... extraSteps) {
        this(registry, REGISTRY_MAPPINGS_PREFIX + registry, extraSteps);
    }

    public VersionedRegistry(String registry, String mappingsPath) {
        this(registry, mappingsPath, new ClientVersion[0]);
    }

    public VersionedRegistry(String registry, String mappingsPath, ClientVersion... extraSteps) {
        this(new ResourceLocation(registry), mappingsPath, extraSteps);
    }

    public VersionedRegistry(ResourceLocation registryKey, String mappingsPath) {
        this(registryKey, mappingsPath, new ClientVersion[0]);
    }

    @SuppressWarnings("unchecked") // there is no way to create arrays with generic types properly
    public VersionedRegistry(ResourceLocation registryKey, String mappingsPath, ClientVersion... extraSteps) {
        this.registryKey = registryKey;
        this.typesBuilder = new TypesBuilder(mappingsPath);
        this.extraSteps = extraSteps;
        this.postLoadMappings();

        int versions = this.typesBuilder.getVersionMapper().size();
        this.typeNames = new Map[versions];
        this.typeIds = new Map[versions];
    }

    @ApiStatus.Internal
    public <Z extends T> Z define(String name, Function<TypesBuilderData, Z> builder) {
        return this.define(name, VersionRange.ALL_VERSIONS, builder);
    }

    @ApiStatus.Internal
    public <Z extends T> Z define(String name, VersionRange range, Function<TypesBuilderData, Z> builder) {
        TypesBuilderData typeData = this.typesBuilder.define(name, range);
        Z instance = builder.apply(typeData);
        MappingHelper.registerMapping(this.typesBuilder, this.typeNames, this.typeIds, typeData, instance);
        return instance;
    }

    @VisibleForTesting
    @ApiStatus.Internal
    public TypesBuilder getTypesBuilder() {
        return this.typesBuilder;
    }

    @VisibleForTesting
    @ApiStatus.Internal
    public void postLoadMappings() {
        this.typesBuilder.registry = this;
        // add extra steps to version mapper
        for (ClientVersion extraStep : this.extraSteps) {
            this.typesBuilder.addExtraVersionStep(extraStep);
        }
    }

    @ApiStatus.Internal
    public void unloadMappings() {
        this.typesBuilder.unloadFileMappings();

        // de-duplicate map objects for names to save memory;
        // only lookup last map to save time
        Map<String, T> lastNameMap = this.typeNames[0];
        for (int i = 1; i < this.typeNames.length; i++) {
            Map<String, T> nameMap = this.typeNames[i];
            if (MapUtil.isDeepEqual(lastNameMap, nameMap)) {
                this.typeNames[i] = lastNameMap;
            } else {
                lastNameMap = nameMap;
            }
        }

        // add all entries to a set
        Set<String> entryNames = new HashSet<>();
        // reverse iteration so we add the newest entry for each name
        for (int i = this.typeNames.length - 1; i >= 0; --i) {
            for (Map.Entry<String, T> entry : this.typeNames[i].entrySet()) {
                // de-duplicate by name to prevent adding the same entry from multiple versions
                if (entryNames.add(entry.getKey())) {
                    this.entries.add(entry.getValue());
                }
            }
        }
    }

    @Override
    public @Nullable T getByName(ClientVersion version, ResourceLocation name) {
        int index = this.typesBuilder.getDataIndex(version);
        return this.typeNames[index].get(name.toString());
    }

    @Override
    public @Nullable T getByName(ClientVersion version, String name) {
        int index = this.typesBuilder.getDataIndex(version);
        // prepend "minecraft:" prefix if no other namespace has been specified
        return this.typeNames[index].get(ResourceLocation.normString(name));
    }

    @Override
    public @Nullable T getByName(ResourceLocation name) {
        ClientVersion version = PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
        return this.getByName(version, name);
    }

    @Override
    public @Nullable T getByName(String name) {
        ClientVersion version = PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
        return this.getByName(version, name);
    }

    @Override
    public @Nullable T getById(ClientVersion version, int id) {
        int index = this.typesBuilder.getDataIndex(version);
        return this.typeIds[index].get(id);
    }

    @Override
    public int getId(MappedEntity entity, ClientVersion version) {
        return entity.getId(version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<T> getEntries() {
        return Collections.unmodifiableCollection(this.entries);
    }

    @Override
    public int size() {
        return this.entries.size();
    }

    @Override
    public ResourceLocation getRegistryKey() {
        return this.registryKey;
    }

    @Override
    public String toString() {
        return "VersionedRegistry[" + this.registryKey + ']';
    }
}
