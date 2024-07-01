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

package com.github.retrooper.packetevents.protocol.world.dimension;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.util.OptionalLong;
import java.util.function.Supplier;

public final class DimensionTypes {

    private static final VersionedRegistry<DimensionType> REGISTRY = new VersionedRegistry<>(
            "dimension_type", "world/dimension_type_mappings");

    private DimensionTypes() {
    }

    @ApiStatus.Internal
    public static DimensionType define(
            String key, OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling,
            boolean ultraWarm, boolean natural, double coordinateScale, boolean bedWorking, boolean respawnAnchorWorking,
            int minY, int height, int logicalHeight, String infiniburnTag, ResourceLocation effectsLocation,
            float ambientLight, boolean piglinSafe, boolean hasRaids, Supplier<NBT> monsterSpawnLightLevel,
            int monsterSpawnBlockLightLimit
    ) {
        return REGISTRY.define(key, data ->
                new StaticDimensionType(data, fixedTime, hasSkyLight, hasCeiling, ultraWarm, natural, coordinateScale,
                        bedWorking, respawnAnchorWorking, minY, height, logicalHeight, infiniburnTag, effectsLocation,
                        ambientLight, piglinSafe, hasRaids, monsterSpawnLightLevel.get(), monsterSpawnBlockLightLimit));
    }

    public static VersionedRegistry<DimensionType> getRegistry() {
        return REGISTRY;
    }

    public static final DimensionType OVERWORLD = define("overworld", OptionalLong.empty(), true,
            false, false, true, 1d, true, false,
            -64, 384, 384, "#minecraft:infiniburn_overworld",
            ResourceLocation.minecraft("overworld"), 0f, false,
            true, () -> {
                NBTCompound compound = new NBTCompound();
                compound.setTag("type", new NBTString("minecraft:uniform"));
                compound.setTag("min_inclusive", new NBTInt(0));
                compound.setTag("max_inclusive", new NBTInt(7));
                return compound;
            }, 0);

    public static final DimensionType OVERWORLD_CAVES = define("overworld_caves", OptionalLong.empty(), true,
            true, false, true, 1d, true, false,
            -64, 384, 384, "#minecraft:infiniburn_overworld",
            ResourceLocation.minecraft("overworld"), 0f, false,
            true, () -> {
                NBTCompound compound = new NBTCompound();
                compound.setTag("type", new NBTString("minecraft:uniform"));
                compound.setTag("min_inclusive", new NBTInt(0));
                compound.setTag("max_inclusive", new NBTInt(7));
                return compound;
            }, 0);

    public static final DimensionType THE_END = define("the_end", OptionalLong.of(6000L), false,
            false, false, false, 1d, false, false,
            0, 256, 256, "#minecraft:infiniburn_end",
            ResourceLocation.minecraft("the_end"), 0f, false, true,
            () -> {
                NBTCompound compound = new NBTCompound();
                compound.setTag("type", new NBTString("minecraft:uniform"));
                compound.setTag("min_inclusive", new NBTInt(0));
                compound.setTag("max_inclusive", new NBTInt(7));
                return compound;
            }, 0);

    public static final DimensionType THE_NETHER = define("the_nether", OptionalLong.of(18000L), false,
            true, true, false, 8d, false, true, 0,
            256, 128, "#minecraft:infiniburn_nether",
            ResourceLocation.minecraft("the_nether"), 0.1f, true, false,
            () -> new NBTInt(7), 15);

    static {
        REGISTRY.unloadMappings();
    }
}
