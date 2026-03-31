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

import com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder;
import com.github.retrooper.packetevents.util.mappings.IRegistryHolder;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@NullMarked
public class PatchableComponentMap implements IComponentMap {

    public static final PatchableComponentMap EMPTY = new PatchableComponentMap(
            Collections.emptyMap(), Collections.emptyMap());

    private final Map<ComponentType<?>, ?> base;
    private final Map<ComponentType<?>, Optional<?>> patches;
    private final IRegistryHolder registries;

    public PatchableComponentMap(StaticComponentMap base) {
        this(base.delegate, new HashMap<>(), base.registries);
    }

    @Deprecated
    public PatchableComponentMap(Map<ComponentType<?>, ?> base) {
        this(base, new HashMap<>(), GlobalRegistryHolder.INSTANCE);
    }

    public PatchableComponentMap(
            StaticComponentMap base,
            Map<ComponentType<?>, Optional<?>> patches
    ) {
        this(base.delegate, patches, base.registries);
    }

    @Deprecated
    public PatchableComponentMap(
            Map<ComponentType<?>, ?> base,
            Map<ComponentType<?>, Optional<?>> patches
    ) {
        this(base, patches, GlobalRegistryHolder.INSTANCE);
    }

    public PatchableComponentMap(StaticComponentMap base, IRegistryHolder registries) {
        this(base.delegate, new HashMap<>(), registries);
    }

    public PatchableComponentMap(Map<ComponentType<?>, ?> base, IRegistryHolder registries) {
        this(base, new HashMap<>(), registries);
    }

    public PatchableComponentMap(
            StaticComponentMap base,
            Map<ComponentType<?>, Optional<?>> patches,
            IRegistryHolder registries
    ) {
        this(base.delegate, patches, registries);
    }

    public PatchableComponentMap(
            Map<ComponentType<?>, ?> base,
            Map<ComponentType<?>, Optional<?>> patches,
            IRegistryHolder registries
    ) {
        this.base = Collections.unmodifiableMap(new HashMap<>(base));
        this.patches = patches;
        this.registries = registries;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @Nullable T get(ComponentType<T> type) {
        Optional<?> patched = this.patches.get(type);
        Object v = patched != null
                ? (T) patched.orElse(null)
                : (T) this.base.get(type);
        if (v instanceof ComponentValueRef) {
            v = ((ComponentValueRef<?>) v).resolve(this.registries);
        }
        return (T) v;
    }

    @Override
    public <T> void set(ComponentType<T> type, Optional<T> value) {
        Object baseVal = this.base.get(type);
        if (baseVal instanceof ComponentValueRef) {
            baseVal = ((ComponentValueRef<?>) baseVal).resolve(this.registries);
        }

        T newVal = value.orElse(null);
        if (Objects.equals(baseVal, newVal)) {
            this.patches.remove(type); // fallback to base
        } else {
            this.patches.put(type, value);
        }
    }

    @Override
    public boolean has(ComponentType<?> type) {
        Optional<?> patched = this.patches.get(type);
        return patched != null ? patched.isPresent() : this.base.containsKey(type);
    }

    @Override
    public PatchableComponentMap withRegistries(IRegistryHolder registries) {
        if (this.registries != registries) {
            return new PatchableComponentMap(this.base, this.patches, this.registries);
        }
        return this;
    }

    public PatchableComponentMap copy() {
        return new PatchableComponentMap(this.base, new HashMap<>(this.patches), this.registries);
    }

    @Deprecated
    public Map<ComponentType<?>, ?> getBase() {
        return this.base;
    }

    @Deprecated
    public Map<ComponentType<?>, Optional<?>> getPatches() {
        return this.patches;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PatchableComponentMap)) return false;
        PatchableComponentMap that = (PatchableComponentMap) obj;
        if (!this.base.equals(that.base)) return false;
        return this.patches.equals(that.patches);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.base, this.patches);
    }

    @Override
    public String toString() {
        return "PatchableComponentMap{base=" + this.base + ", patches=" + this.patches + '}';
    }
}
