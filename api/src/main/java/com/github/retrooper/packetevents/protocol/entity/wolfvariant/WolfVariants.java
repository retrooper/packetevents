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

import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.world.biome.Biome;
import com.github.retrooper.packetevents.protocol.world.biome.Biomes;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;

public final class WolfVariants {

    private static final VersionedRegistry<WolfVariant> REGISTRY = new VersionedRegistry<>(
            "wolf_variant", "entity/wolf_variant_mappings");

    private WolfVariants() {
    }

    @ApiStatus.Internal
    public static WolfVariant define(String key, MappedEntitySet<Biome> biomes) {
        return define(key, "wolf_" + key, biomes);
    }

    @ApiStatus.Internal
    public static WolfVariant define(String key, String assetId, MappedEntitySet<Biome> biomes) {
        return define(key, ResourceLocation.minecraft("entity/wolf/" + assetId),
                ResourceLocation.minecraft("entity/wolf/" + assetId + "_tame"),
                ResourceLocation.minecraft("entity/wolf/" + assetId + "_angry"), biomes);
    }

    @ApiStatus.Internal
    public static WolfVariant define(
            String key,
            ResourceLocation wildTexture,
            ResourceLocation tameTexture,
            ResourceLocation angryTexture,
            MappedEntitySet<Biome> biomes
    ) {
        return REGISTRY.define(key, data -> new StaticWolfVariant(
                data, wildTexture, tameTexture, angryTexture, biomes));
    }

    public static VersionedRegistry<WolfVariant> getRegistry() {
        return REGISTRY;
    }

    public static final WolfVariant PALE = define("pale", "wolf",
            new MappedEntitySet<>(Arrays.asList(Biomes.TAIGA)));
    public static final WolfVariant SPOTTED = define("spotted",
            new MappedEntitySet<>(ResourceLocation.minecraft("is_savanna")));
    public static final WolfVariant SNOWY = define("snowy",
            new MappedEntitySet<>(Arrays.asList(Biomes.GROVE)));
    public static final WolfVariant BLACK = define("black",
            new MappedEntitySet<>(Arrays.asList(Biomes.OLD_GROWTH_PINE_TAIGA)));
    public static final WolfVariant ASHEN = define("ashen",
            new MappedEntitySet<>(Arrays.asList(Biomes.SNOWY_TAIGA)));
    public static final WolfVariant RUSTY = define("rusty",
            new MappedEntitySet<>(ResourceLocation.minecraft("is_jungle")));
    public static final WolfVariant WOODS = define("woods",
            new MappedEntitySet<>(Arrays.asList(Biomes.FOREST)));
    public static final WolfVariant CHESTNUT = define("chestnut",
            new MappedEntitySet<>(Arrays.asList(Biomes.OLD_GROWTH_SPRUCE_TAIGA)));
    public static final WolfVariant STRIPED = define("striped",
            new MappedEntitySet<>(ResourceLocation.minecraft("is_badlands")));

    static {
        REGISTRY.unloadMappings();
    }
}
