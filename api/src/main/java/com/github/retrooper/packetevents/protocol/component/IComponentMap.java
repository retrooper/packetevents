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

package com.github.retrooper.packetevents.protocol.component;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.IRegistry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public interface IComponentMap {

    @SuppressWarnings("unchecked") // safe in this case
    static StaticComponentMap decode(NBT nbt, ClientVersion version, IRegistry<? extends ComponentType<?>> registry) {
        NBTCompound compound = (NBTCompound) nbt;
        StaticComponentMap.Builder components = StaticComponentMap.builder();
        for (Map.Entry<String, NBT> entry : compound.getTags().entrySet()) {
            ComponentType<?> type = registry.getByName(entry.getKey());
            if (type == null) {
                throw new IllegalStateException("Unknown component type named " + entry.getKey() + " encountered");
            }
            Object value = type.decode(entry.getValue(), version);
            components.set((ComponentType<? super Object>) type, value);
        }
        return components.build();
    }

    @SuppressWarnings("unchecked") // safe in this case
    static NBT encode(StaticComponentMap components, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        for (Map.Entry<ComponentType<?>, ?> entry : components.getDelegate().entrySet()) {
            String key = entry.getKey().getName().toString();
            NBT value = ((ComponentType<? super Object>) entry.getKey()).encode(entry.getValue(), version);
            compound.setTag(key, value);
        }
        return compound;
    }

    default <T> Optional<T> getOptional(ComponentType<T> type) {
        return Optional.ofNullable(this.get(type));
    }

    boolean has(ComponentType<?> type);

    @Contract("_, !null -> !null")
    default <T> @Nullable T getOr(ComponentType<T> type, @Nullable T otherValue) {
        T value = this.get(type);
        if (value != null) {
            return value;
        }
        return otherValue;
    }

    <T> @Nullable T get(ComponentType<T> type);

    default <T> void set(ComponentValue<T> component) {
        this.set(component.getType(), component.getValue());
    }

    default <T> void set(ComponentType<T> type, @Nullable T value) {
        this.set(type, Optional.ofNullable(value));
    }

    default <T> void unset(ComponentType<T> type) {
        this.set(type, Optional.empty());
    }

    <T> void set(ComponentType<T> type, Optional<T> value);
}
