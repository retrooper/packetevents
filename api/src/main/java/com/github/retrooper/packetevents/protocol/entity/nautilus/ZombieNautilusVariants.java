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

package com.github.retrooper.packetevents.protocol.entity.nautilus;

import com.github.retrooper.packetevents.protocol.entity.nautilus.ZombieNautilusVariant.ModelType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.21.11+
 */
@NullMarked
public final class ZombieNautilusVariants {

    private static final VersionedRegistry<ZombieNautilusVariant> REGISTRY = new VersionedRegistry<>("zombie_nautilus_variant");

    private ZombieNautilusVariants() {
    }

    @ApiStatus.Internal
    public static ZombieNautilusVariant define(String name, ModelType modelType, String texture) {
        ResourceLocation assetId = new ResourceLocation("entity/nautilus/" + texture);
        return REGISTRY.define(name, data ->
                new StaticZombieNautilusVariant(data, modelType, assetId));
    }

    public static VersionedRegistry<ZombieNautilusVariant> getRegistry() {
        return REGISTRY;
    }

    public static final ZombieNautilusVariant TEMPERATE = define("temperate", ModelType.NORMAL, "zombie_nautilus");
    public static final ZombieNautilusVariant WARM = define("warm", ModelType.WARM, "zombie_nautilus_coral");

    static {
        REGISTRY.unloadMappings();
    }
}
