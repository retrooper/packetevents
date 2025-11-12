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

package com.github.retrooper.packetevents.protocol.entity.frog;

import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

public final class FrogVariants {

    private static final VersionedRegistry<FrogVariant> REGISTRY =
            new VersionedRegistry<>("frog_variant");

    private FrogVariants() {
    }

    @ApiStatus.Internal
    public static FrogVariant define(String name, String texture) {
        ResourceLocation assetId = new ResourceLocation("entity/frog/" + texture);
        return REGISTRY.define(name, data ->
                new StaticFrogVariant(data, assetId));
    }

    public static VersionedRegistry<FrogVariant> getRegistry() {
        return REGISTRY;
    }

    public static final FrogVariant COLD = define("cold", "cold_frog");
    public static final FrogVariant TEMPERATE = define("temperate", "temperate_frog");
    public static final FrogVariant WARM = define("warm", "warm_frog");

    static {
        REGISTRY.unloadMappings();
    }
}
