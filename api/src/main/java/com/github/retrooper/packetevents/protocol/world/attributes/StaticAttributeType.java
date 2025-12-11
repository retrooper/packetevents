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
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.world.attributes.modifiers.AttributeModifier;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @versions 1.21.11+
 */
@NullMarked
public class StaticAttributeType<T> extends AbstractMappedEntity implements AttributeType<T> {

    private final @Nullable NbtCodec<T> valueCodec;
    private final NbtCodec<AttributeModifier<T, ?>> modifierCodec;

    @ApiStatus.Internal
    public StaticAttributeType(
            @Nullable TypesBuilderData data,
            @Nullable NbtCodec<T> valueCodec,
            NbtCodec<AttributeModifier<T, ?>> modifierCodec
    ) {
        super(data);
        this.valueCodec = valueCodec;
        this.modifierCodec = modifierCodec;
    }

    @Override
    public boolean isSynced() {
        return this.valueCodec != null;
    }

    @Override
    public NbtCodec<T> getValueCodec() {
        if (this.valueCodec == null) {
            throw new UnsupportedOperationException();
        }
        return this.valueCodec;
    }

    @Override
    public NbtCodec<AttributeModifier<T, ?>> getModifierCodec() {
        return this.modifierCodec;
    }
}
