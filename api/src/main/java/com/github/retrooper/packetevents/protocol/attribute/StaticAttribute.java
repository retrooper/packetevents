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

package com.github.retrooper.packetevents.protocol.attribute;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public class StaticAttribute extends AbstractMappedEntity implements Attribute {

    private final @Nullable ResourceLocation legacyName;
    private final double defaultValue;
    private final double minValue;
    private final double maxValue;

    /**
     * Unregistered attribute that round-trips by name (custom server attributes on 1.16–1.20.4).
     */
    public StaticAttribute(ResourceLocation name, double defaultValue, double minValue, double maxValue) {
        super(null);
        this.legacyName = name;
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @ApiStatus.Internal
    public StaticAttribute(
            @Nullable TypesBuilderData data, String legacyPrefix,
            double defaultValue, double minValue, double maxValue
    ) {
        super(data);
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.legacyName = legacyPrefix == null || data == null ? null : new ResourceLocation(
                data.getName().getNamespace(), legacyPrefix + "." + data.getName().getKey());
    }

    @Override
    public ResourceLocation getName(ClientVersion version) {
        if (this.data == null) {
            // unregistered custom attribute — name stored in legacyName
            if (this.legacyName != null) {
                return this.legacyName;
            }
            throw new UnsupportedOperationException();
        }
        return version.isNewerThanOrEquals(ClientVersion.V_1_21_2) || this.legacyName == null
                ? this.data.getName() : this.legacyName;
    }

    @Override
    public double getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public double getMinValue() {
        return this.minValue;
    }

    @Override
    public double getMaxValue() {
        return this.maxValue;
    }
}
