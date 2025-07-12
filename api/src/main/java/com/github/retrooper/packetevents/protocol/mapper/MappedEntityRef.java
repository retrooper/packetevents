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

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.util.NbtDecoder;
import com.github.retrooper.packetevents.protocol.util.NbtEncoder;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.IRegistry;
import com.github.retrooper.packetevents.util.mappings.IRegistryHolder;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.function.Supplier;

@NullMarked
public interface MappedEntityRef<T extends MappedEntity> extends Supplier<T> {

    T get();

    static <T extends MappedEntity> MappedEntityRef<T> decode(
            NBT tag, IRegistry<T> registry, NbtDecoder<T> decoder, PacketWrapper<?> wrapper
    ) {
        if (tag instanceof NBTString) {
            ResourceLocation name = new ResourceLocation(((NBTString) tag).getValue());
            return new Named<>(wrapper, registry, name);
        }
        return new Static<>(decoder.decode(tag, wrapper));
    }

    static <T extends MappedEntity> NBT encode(PacketWrapper<?> wrapper, NbtEncoder<T> encoder, MappedEntityRef<T> ref) {
        if (ref instanceof Named) {
            return new NBTString(((Named<T>) ref).name.toString());
        } else if (ref instanceof Static) {
            return encoder.encode(wrapper, ((Static<T>) ref).entity);
        } else {
            throw new UnsupportedOperationException("Unsupported MappedEntityRef implementation: " + ref);
        }
    }

    final class Static<T extends MappedEntity> implements MappedEntityRef<T> {

        private final T entity;

        public Static(T entity) {
            this.entity = entity;
        }

        @Override
        public T get() {
            return this.entity;
        }
    }

    final class Named<T extends MappedEntity> implements MappedEntityRef<T> {

        private final WeakReference<IRegistryHolder> registryHolder;
        private final ClientVersion version;
        private final IRegistry<T> registry;
        private final ResourceLocation name;
        private volatile @Nullable T entity;

        public Named(PacketWrapper<?> wrapper, IRegistry<T> registry, ResourceLocation name) {
            this(wrapper.getRegistryHolder(), wrapper.getServerVersion().toClientVersion(), registry, name);
        }

        public Named(IRegistryHolder registryHolder, ClientVersion version, IRegistry<T> registry, ResourceLocation name) {
            this.registryHolder = new WeakReference<>(registryHolder);
            this.version = version;
            this.registry = registry;
            this.name = name;
        }

        @Override
        public T get() {
            T entity = this.entity;
            if (entity == null) {
                synchronized (this) {
                    entity = this.entity;
                    if (entity == null) {
                        // registry holder may be a user object, so ensure no user objects get leaked accidentally
                        IRegistryHolder registryHolder = this.registryHolder.get();
                        if (registryHolder == null) {
                            throw new IllegalStateException("Registry holder for " + this.registry
                                    + "/" + this.version + "/" + this.name + "has disappeared");
                        }
                        // replace registry and lookup entity
                        IRegistry<T> registry = registryHolder.getRegistryOr(this.registry, this.version);
                        entity = registry.getByNameOrThrow(this.name);
                        this.entity = entity;
                    }
                }
            }
            return entity;
        }
    }
}
