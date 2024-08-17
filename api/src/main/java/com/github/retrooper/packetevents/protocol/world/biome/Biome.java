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

package com.github.retrooper.packetevents.protocol.world.biome;

import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil.indexValueOrThrow;

public interface Biome extends MappedEntity, CopyableEntity<Biome> {

    boolean hasPrecipitation();

    @ApiStatus.Obsolete(since = "1.19.3")
    Precipitation getPrecipitation();

    float getTemperature();

    TemperatureModifier getTemperatureModifier();

    float getDownfall();

    @ApiStatus.Obsolete(since = "1.19")
    @Nullable Category getCategory();

    @ApiStatus.Obsolete(since = "1.18")
    @Nullable Float getDepth();

    @ApiStatus.Obsolete(since = "1.18")
    @Nullable Float getScale();

    BiomeEffects getEffects();

    static Biome decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        float temperature = compound.getNumberTagOrThrow("temperature").getAsFloat();
        TemperatureModifier temperatureModifier =
                Optional.ofNullable(compound.getStringTagValueOrNull("temperature_modifier"))
                        .map(id -> indexValueOrThrow(TemperatureModifier.ID_INDEX, id))
                        .orElse(TemperatureModifier.NONE);
        float downfall = compound.getNumberTagOrThrow("downfall").getAsFloat();
        boolean precipitation = version.isNewerThan(ClientVersion.V_1_19_3) ? compound.getBoolean("has_precipitation") :
                indexValueOrThrow(Precipitation.ID_INDEX, compound.getStringTagValueOrThrow("precipitation")) != Precipitation.NONE;
        BiomeEffects effects = BiomeEffects.decode(compound.getTagOrThrow("effects"), version);

        // removed with 1.19
        Category category = version.isNewerThanOrEquals(ClientVersion.V_1_19) ? null :
                indexValueOrThrow(Category.ID_INDEX, compound.getStringTagValueOrThrow("category"));
        Float depth = version.isNewerThanOrEquals(ClientVersion.V_1_18) ? null :
                compound.getNumberTagOrThrow("depth").getAsFloat();
        Float scale = version.isNewerThanOrEquals(ClientVersion.V_1_18) ? null :
                compound.getNumberTagOrThrow("scale").getAsFloat();

        return new StaticBiome(data, precipitation, temperature, temperatureModifier, downfall,
                category, depth, scale, effects);
    }

    static NBT encode(Biome biome, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        if (version.isNewerThan(ClientVersion.V_1_19_3)) {
            compound.setTag("has_precipitation", new NBTByte(biome.hasPrecipitation()));
        } else {
            compound.setTag("precipitation", new NBTString(biome.getPrecipitation().getId()));
        }
        compound.setTag("temperature", new NBTFloat(biome.getTemperature()));
        if (biome.getTemperatureModifier() != TemperatureModifier.NONE) {
            compound.setTag("temperature_modifier", new NBTString(biome.getTemperatureModifier().getId()));
        }
        compound.setTag("downfall", new NBTFloat(biome.getDownfall()));
        if (version.isOlderThan(ClientVersion.V_1_19)) {
            if (biome.getCategory() != null) {
                compound.setTag("category", new NBTString(biome.getCategory().getId()));
            }
            if (version.isOlderThan(ClientVersion.V_1_18)) {
                if (biome.getDepth() != null) {
                    compound.setTag("depth", new NBTFloat(biome.getDepth()));
                }
                if (biome.getScale() != null) {
                    compound.setTag("scale", new NBTFloat(biome.getScale()));
                }
            }
        }
        compound.setTag("effects", BiomeEffects.encode(biome.getEffects(), version));
        return compound;
    }

    @ApiStatus.Obsolete(since = "1.19")
    enum Category {

        NONE("none"),
        TAIGA("taiga"),
        EXTREME_HILLS("extreme_hills"),
        JUNGLE("jungle"),
        MESA("mesa"),
        PLAINS("plains"),
        SAVANNA("savanna"),
        ICY("icy"),
        THE_END("the_end"),
        BEACH("beach"),
        FOREST("forest"),
        OCEAN("ocean"),
        DESERT("desert"),
        RIVER("river"),
        SWAMP("swamp"),
        MUSHROOM("mushroom"),
        NETHER("nether"),
        UNDERGROUND("underground"),
        MOUNTAIN("mountain");

        public static final Index<String, Category> ID_INDEX = Index.create(Category.class,
                Category::getId);
        private final String id;

        Category(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }
    }

    @ApiStatus.Obsolete(since = "1.19.3")
    enum Precipitation {
        NONE("none"),
        RAIN("rain"),
        SNOW("snow");

        public static final Index<String, Precipitation> ID_INDEX = Index.create(Precipitation.class,
                Precipitation::getId);
        private final String id;

        Precipitation(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }
    }

    enum TemperatureModifier {

        NONE("none"),
        FROZEN("frozen");

        public static final Index<String, TemperatureModifier> ID_INDEX = Index.create(
                TemperatureModifier.class, TemperatureModifier::getId);

        private final String id;

        TemperatureModifier(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }
    }
}
