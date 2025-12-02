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

import com.github.retrooper.packetevents.protocol.entity.cow.CowVariant.ModelType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

public final class CowVariants {

    private static final VersionedRegistry<CowVariant> REGISTRY =
            new VersionedRegistry<>("cow_variant");

    private CowVariants() {
    }

    @ApiStatus.Internal
    public static CowVariant define(String name, ModelType modelType, String texture) {
        ResourceLocation assetId = new ResourceLocation("entity/cow/" + texture);
        return REGISTRY.define(name, data ->
                new StaticCowVariant(data, modelType, assetId));
    }

    public static VersionedRegistry<CowVariant> getRegistry() {
        return REGISTRY;
    }

    public static final CowVariant COLD = define("cold", ModelType.COLD, "cold_cow");
    public static final CowVariant TEMPERATE = define("temperate", ModelType.NORMAL, "temperate_cow");
    public static final CowVariant WARM = define("warm", ModelType.WARM, "warm_cow");

    static {
        REGISTRY.unloadMappings();
    }
}
