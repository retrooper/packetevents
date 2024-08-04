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

import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAttributeModifiers;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEnchantments;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemLore;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemRarity;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class StaticComponentMap implements IComponentMap {

    public static final StaticComponentMap EMPTY = new StaticComponentMap(Collections.emptyMap());
    public static final StaticComponentMap SHARED_ITEM_COMPONENTS = builder()
            .set(ComponentTypes.MAX_STACK_SIZE, 64)
            .set(ComponentTypes.LORE, ItemLore.EMPTY)
            .set(ComponentTypes.ENCHANTMENTS, ItemEnchantments.EMPTY)
            .set(ComponentTypes.REPAIR_COST, 0)
            .set(ComponentTypes.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY)
            .set(ComponentTypes.RARITY, ItemRarity.COMMON)
            .build();

    private final boolean empty;
    private final Map<ComponentType<?>, ?> delegate;

    public StaticComponentMap(Map<ComponentType<?>, ?> delegate) {
        this.empty = delegate.isEmpty();
        this.delegate = this.empty ? Collections.emptyMap()
                : Collections.unmodifiableMap(new HashMap<>(delegate));
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean has(ComponentType<?> type) {
        return !this.empty && this.delegate.containsKey(type);
    }

    @SuppressWarnings("unchecked") // no
    @Override
    public <T> @Nullable T get(ComponentType<T> type) {
        return this.empty ? null : (T) this.delegate.get(type);
    }

    @Override
    public <T> void set(ComponentType<T> type, Optional<T> value) {
        throw new UnsupportedOperationException();
    }

    public StaticComponentMap merge(StaticComponentMap prioritizedMap) {
        return builder().setAll(this).setAll(prioritizedMap).build();
    }

    public Map<ComponentType<?>, ?> getDelegate() {
        return this.delegate;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticComponentMap)) return false;
        StaticComponentMap that = (StaticComponentMap) obj;
        return this.delegate.equals(that.delegate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.delegate);
    }

    @Override
    public String toString() {
        return "StaticComponentMap{empty=" + this.empty + ", delegate=" + this.delegate + '}';
    }

    public static class Builder {

        private final Map<ComponentType<?>, Object> map = new HashMap<>();

        public Builder() {
        }

        public StaticComponentMap build() {
            return new StaticComponentMap(this.map);
        }

        public Builder setAll(StaticComponentMap map) {
            return this.setAll(map.getDelegate());
        }

        @SuppressWarnings("unchecked")
        public Builder setAll(Map<ComponentType<?>, ?> map) {
            for (Map.Entry<ComponentType<?>, ?> entry : map.entrySet()) {
                this.set((ComponentType<Object>) entry.getKey(), entry.getValue());
            }
            return this;
        }

        public <T> Builder set(ComponentType<T> type, Optional<T> value) {
            return this.set(type, value.orElse(null));
        }

        public <T> Builder set(ComponentType<T> type, @Nullable T value) {
            if (value == null) {
                this.map.remove(type);
            } else {
                this.map.put(type, value);
            }
            return this;
        }
    }
}
