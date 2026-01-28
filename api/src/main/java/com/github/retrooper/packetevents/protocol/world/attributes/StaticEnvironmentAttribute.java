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

package com.github.retrooper.packetevents.protocol.world.attributes;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @versions 1.21.11+
 */
@NullMarked
public class StaticEnvironmentAttribute<T> extends AbstractMappedEntity implements EnvironmentAttribute<T> {

    private final @Nullable AttributeType<T> attributeType;
    private final @Nullable T defaultValue;

    @ApiStatus.Internal
    public StaticEnvironmentAttribute(
            @Nullable TypesBuilderData data,
            @Nullable AttributeType<T> attributeType, @Nullable T defaultValue
    ) {
        super(data);
        this.attributeType = attributeType;
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean isSynced() {
        return this.attributeType != null;
    }

    @Override
    public AttributeType<T> getType() {
        if (this.attributeType == null) {
            throw new UnsupportedOperationException();
        }
        return this.attributeType;
    }

    @Override
    public T getDefaultValue() {
        if (this.defaultValue == null) {
            throw new UnsupportedOperationException();
        }
        return this.defaultValue;
    }
}
