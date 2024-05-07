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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface IComponentMap {

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
