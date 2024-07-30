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
import com.github.retrooper.packetevents.protocol.nbt.*;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import net.kyori.adventure.util.Codec;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Biome extends MappedEntity, CopyableEntity<Biome> {

    boolean hasPrecipitation();

    @Deprecated
    Precipitation getPrecipitation();

    float getTemperature();

    TemperatureModifier getTemperatureModifier();

    float getDownfall();

    BiomeEffects getEffects();

    static Biome decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        float temperature = compound.getNumberTagOrThrow("temperature").getAsFloat();
        TemperatureModifier temperatureModifier =
                Optional.ofNullable(compound.getStringTagValueOrNull("temperature_modifier"))
                        .map(TemperatureModifier.ID_INDEX::valueOrThrow)
                        .orElse(TemperatureModifier.NONE);
        float downfall = compound.getNumberTagOrThrow("downfall").getAsFloat();
        BiomeEffects effects = BiomeEffects.decode(compound.getTagOrThrow("effects"), version);


        if (version.isNewerThan(ClientVersion.V_1_19_3)) {
            boolean hasPrecipitation = compound.getBoolean("has_precipitation");

            return new StaticBiome(data, hasPrecipitation, temperature, temperatureModifier, downfall, effects);
        } else {
            Precipitation precipitation = Precipitation.ID_INDEX.valueOrThrow(compound.getStringTagValueOrThrow("precipitation"));

            return new StaticBiome(data, precipitation, temperature, temperatureModifier, downfall, effects);
        }
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
        compound.setTag("effects", BiomeEffects.encode(biome.getEffects(), version));
        return compound;
    }

    @Deprecated
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
