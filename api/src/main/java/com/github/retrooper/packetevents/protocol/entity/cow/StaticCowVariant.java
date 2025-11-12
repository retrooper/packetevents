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

package com.github.retrooper.packetevents.protocol.entity.cow;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StaticCowVariant extends AbstractMappedEntity implements CowVariant {

    private final ModelType modelType;
    private final ResourceLocation assetId;

    public StaticCowVariant(ModelType modelType, ResourceLocation assetId) {
        this(null, modelType, assetId);
    }

    @ApiStatus.Internal
    public StaticCowVariant(@Nullable TypesBuilderData data, ModelType modelType, ResourceLocation assetId) {
        super(data);
        this.modelType = modelType;
        this.assetId = assetId;
    }

    @Override
    public CowVariant copy(@Nullable TypesBuilderData newData) {
        return new StaticCowVariant(newData, this.modelType, this.assetId);
    }

    @Override
    public ModelType getModelType() {
        return this.modelType;
    }

    @Override
    public ResourceLocation getAssetId() {
        return this.assetId;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (!(obj instanceof StaticCowVariant)) return false;
        if (!super.equals(obj)) return false;
        StaticCowVariant that = (StaticCowVariant) obj;
        if (!this.modelType.equals(that.modelType)) return false;
        return this.assetId.equals(that.assetId);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.modelType, this.assetId);
    }
}
