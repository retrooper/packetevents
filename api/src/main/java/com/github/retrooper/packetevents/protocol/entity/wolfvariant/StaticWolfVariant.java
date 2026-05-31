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

package com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.world.biome.Biome;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 1.20.5+
 */
@NullMarked
public class StaticWolfVariant extends AbstractMappedEntity implements WolfVariant {

    private final WolfAssetSet assets;
    /**
     * @versions 26.1+
     */
    private final WolfAssetSet babyAssets;
    /**
     * @versions 1.20.5-1.21.4
     */
    private final MappedEntitySet<Biome> biomes;

    /**
     * @versions 1.20.5-1.21.4
     */
    @ApiStatus.Obsolete
    @Deprecated
    public StaticWolfVariant(
            ResourceLocation wildTexture,
            ResourceLocation tameTexture,
            ResourceLocation angryTexture,
            MappedEntitySet<Biome> biomes
    ) {
        this(new WolfAssetSet(wildTexture, tameTexture, angryTexture), biomes);
    }

    /**
     * @versions 1.21.5-1.21.11
     */
    @ApiStatus.Obsolete
    @Deprecated
    public StaticWolfVariant(
            ResourceLocation angryTexture,
            ResourceLocation tameTexture,
            ResourceLocation wildTexture
    ) {
        this(new WolfAssetSet(wildTexture, tameTexture, angryTexture));
    }

    /**
     * @versions 1.20.5-1.21.4
     */
    @ApiStatus.Obsolete
    public StaticWolfVariant(WolfAssetSet assets, MappedEntitySet<Biome> biomes) {
        this(null, assets, assets, biomes);
    }

    /**
     * @versions 1.21.5-1.21.11
     */
    @ApiStatus.Obsolete
    public StaticWolfVariant(WolfAssetSet assets) {
        this(null, assets, assets, MappedEntitySet.createEmpty());
    }

    /**
     * @versions 26.1+
     */
    public StaticWolfVariant(WolfAssetSet assets, WolfAssetSet babyAssets) {
        this(null, assets, babyAssets, MappedEntitySet.createEmpty());
    }

    @ApiStatus.Internal
    public StaticWolfVariant(
            @Nullable TypesBuilderData data,
            WolfAssetSet assets,
            WolfAssetSet babyAssets,
            MappedEntitySet<Biome> biomes
    ) {
        super(data);
        this.assets = assets;
        this.babyAssets = babyAssets;
        this.biomes = biomes;
    }

    @Override
    public WolfVariant copy(@Nullable TypesBuilderData newData) {
        return new StaticWolfVariant(newData, this.assets, this.babyAssets, this.biomes);
    }

    @Override
    public WolfAssetSet getAssets() {
        return this.assets;
    }

    @Override
    public WolfAssetSet getBabyAssets() {
        return this.babyAssets;
    }

    @Override
    public MappedEntitySet<Biome> getBiomes() {
        return this.biomes;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticWolfVariant)) return false;
        StaticWolfVariant that = (StaticWolfVariant) obj;
        if (!this.assets.equals(that.assets)) return false;
        if (!this.babyAssets.equals(that.babyAssets)) return false;
        return this.biomes.equals(that.biomes);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.assets, this.babyAssets, this.biomes);
    }
}
