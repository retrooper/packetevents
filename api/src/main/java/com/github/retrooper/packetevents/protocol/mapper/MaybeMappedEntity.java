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

package com.github.retrooper.packetevents.protocol.mapper;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.IRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Equivalent to vanilla's EitherHolder; either a direct entity
 * reference is present, or just a name of one.
 */
// TODO find a better name for this class
@ApiStatus.Experimental
public final class MaybeMappedEntity<T extends MappedEntity> {

    private final @Nullable T entity;
    private final @Nullable ResourceLocation name;
    private final @Nullable IRegistry<T> registry;

    public MaybeMappedEntity(T entity) {
        this(entity, null, null);
    }

    public MaybeMappedEntity(ResourceLocation name) {
        this(name, null);
    }

    public MaybeMappedEntity(ResourceLocation name, @Nullable IRegistry<T> registry) {
        this(null, name, registry);
    }

    public MaybeMappedEntity(@Nullable T entity, @Nullable ResourceLocation name) {
        this(entity, name, null);
    }

    public MaybeMappedEntity(@Nullable T entity, @Nullable ResourceLocation name, @Nullable IRegistry<T> registry) {
        if ((entity == null) && (name == null)) {
            throw new IllegalArgumentException("Only one of entity and name is allowed to be null");
        }
        this.entity = entity;
        this.name = name;
        this.registry = registry;
    }

    public static <T extends MappedEntity> MaybeMappedEntity<T> read(
            PacketWrapper<?> wrapper, IRegistry<T> registry, PacketWrapper.Reader<T> reader
    ) {
        if (wrapper.readBoolean()) {
            // direct reference, use supplied reader
            return new MaybeMappedEntity<>(reader.apply(wrapper));
        }
        // read identifier and try to look up in registry
        ClientVersion version = wrapper.getServerVersion().toClientVersion();
        IRegistry<T> replacedRegistry = wrapper.getRegistryHolder().getRegistryOr(registry, version);
        return new MaybeMappedEntity<>(wrapper.readIdentifier(), replacedRegistry);
    }

    public static <T extends MappedEntity> void write(
            PacketWrapper<?> wrapper, MaybeMappedEntity<T> entity, PacketWrapper.Writer<T> writer
    ) {
        if (entity.entity != null) {
            wrapper.writeBoolean(true);
            writer.accept(wrapper, entity.entity);
        } else {
            wrapper.writeBoolean(false);
            wrapper.writeIdentifier(entity.name);
        }
    }

    public T getValueOrThrow() {
        T value = this.getValue();
        if (value == null) {
            throw new IllegalStateException("Can't resolve entity by name " + this.name);
        }
        return value;
    }

    public @Nullable T getValue() {
        if (this.entity != null) {
            return this.entity;
        } else if (this.registry != null && this.name != null) {
            return this.registry.getByName(this.name);
        } else {
            return null; // should only occur if registry is null
        }
    }

    public ResourceLocation getName() {
        if (this.name != null) {
            return this.name;
        } else if (this.entity != null) {
            return this.entity.getName();
        } else {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MaybeMappedEntity)) return false;
        MaybeMappedEntity<?> that = (MaybeMappedEntity<?>) obj;
        if (!Objects.equals(this.entity, that.entity)) return false;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.entity, this.name);
    }
}
