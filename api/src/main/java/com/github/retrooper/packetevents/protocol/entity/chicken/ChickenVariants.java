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

package com.github.retrooper.packetevents.protocol.entity.chicken;

import com.github.retrooper.packetevents.protocol.entity.chicken.ChickenVariant.ModelType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

/**
 * @versions 1.21.5+
 */
public final class ChickenVariants {

    private static final VersionedRegistry<ChickenVariant> REGISTRY =
            new VersionedRegistry<>("chicken_variant");

    private ChickenVariants() {
    }

    @ApiStatus.Internal
    public static ChickenVariant define(String name, ModelType modelType, String texture) {
        ResourceLocation assetId = new ResourceLocation("entity/chicken/chicken_" + texture);
        ResourceLocation babyAssetId = new ResourceLocation("entity/chicken/chicken_" + texture + "_baby");
        return REGISTRY.define(name, data ->
                new StaticChickenVariant(data, modelType, assetId, babyAssetId));
    }

    public static VersionedRegistry<ChickenVariant> getRegistry() {
        return REGISTRY;
    }

    public static final ChickenVariant COLD = define("cold", ModelType.COLD, "cold");
    public static final ChickenVariant TEMPERATE = define("temperate", ModelType.NORMAL, "temperate");
    public static final ChickenVariant WARM = define("warm", ModelType.NORMAL, "warm");

    static {
        REGISTRY.unloadMappings();
    }
}
