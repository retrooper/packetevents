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

package com.github.retrooper.packetevents.protocol.world.attributes.easing;
// Created by booky10 in packetevents (8:41 PM 07.12.2025)

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.util.FloatUnaryOperator;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @versions 1.21.11+
 */
@NullMarked
public class StaticEasingType extends AbstractMappedEntity implements EasingType {

    private final FloatUnaryOperator operator;

    @ApiStatus.Internal
    public StaticEasingType(@Nullable TypesBuilderData data, FloatUnaryOperator operator) {
        super(data);
        this.operator = operator;
    }

    @Override
    public float apply(float x) {
        return this.operator.apply(x);
    }
}
