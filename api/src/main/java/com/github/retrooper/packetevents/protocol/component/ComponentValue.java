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

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public final class ComponentValue<T> {

    private final ComponentType<T> type;
    private final T value;

    public ComponentValue(ComponentType<T> type, T value) {
        this.type = type;
        this.value = value;
    }

    public static ComponentValue<?> read(PacketWrapper<?> wrapper) {
        ComponentType<?> type = wrapper.readMappedEntity(ComponentTypes::getById);
        return read0(wrapper, type);
    }

    private static <T> ComponentValue<T> read0(PacketWrapper<?> wrapper, ComponentType<T> type) {
        // additional method required here for proper handling because of how generics work
        return new ComponentValue<>(type, type.read(wrapper));
    }

    public static <T> void write(PacketWrapper<?> wrapper, ComponentValue<T> value) {
        wrapper.writeMappedEntity(value.type);
        value.type.write(wrapper, value.value);
    }

    public ComponentType<T> getType() {
        return this.type;
    }

    public T getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ComponentValue)) return false;
        ComponentValue<?> that = (ComponentValue<?>) obj;
        if (!this.type.equals(that.type)) return false;
        return this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.value);
    }

    @Override
    public String toString() {
        return "ComponentValue{type=" + this.type + ", value=" + this.value + '}';
    }
}
