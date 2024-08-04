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

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.Nullable;

public class StaticBiome extends AbstractMappedEntity implements Biome {

    private final boolean precipitation;
    private final float temperature;
    private final TemperatureModifier temperatureModifier;
    private final float downfall;
    private final BiomeEffects effects;

    public StaticBiome(
            boolean precipitation,
            float temperature, TemperatureModifier temperatureModifier,
            float downfall, BiomeEffects effects
    ) {
        this(null, precipitation, temperature, temperatureModifier, downfall, effects);
    }

    @Deprecated
    public StaticBiome(@Nullable TypesBuilderData data, Precipitation precipitation, float temperature,
            TemperatureModifier temperatureModifier, float downfall, BiomeEffects effects) {
        this(data, precipitation != Precipitation.NONE, temperature, temperatureModifier, downfall, effects);
    }

    public StaticBiome(
            @Nullable TypesBuilderData data, boolean precipitation,
            float temperature, TemperatureModifier temperatureModifier,
            float downfall, BiomeEffects effects
    ) {
        super(data);
        this.precipitation = precipitation;
        this.temperature = temperature;
        this.temperatureModifier = temperatureModifier;
        this.downfall = downfall;
        this.effects = effects;
    }

    @Override
    public Biome copy(@Nullable TypesBuilderData newData) {
        return new StaticBiome(newData, this.precipitation, this.temperature,
                this.temperatureModifier, this.downfall, this.effects);
    }

    @Override
    public boolean hasPrecipitation() {
        return this.precipitation;
    }

    @Override
    public Precipitation getPrecipitation() {
        if (!hasPrecipitation()) {
            return Precipitation.NONE;
        }

        switch (getTemperatureModifier()) {
            case NONE:
                return Precipitation.RAIN;
            case FROZEN:
                return Precipitation.SNOW;
            default:
                throw new IllegalArgumentException("Unrecognized Temperature Modifier " + getTemperatureModifier());
        }
    }

    @Override
    public float getTemperature() {
        return this.temperature;
    }

    @Override
    public TemperatureModifier getTemperatureModifier() {
        return this.temperatureModifier;
    }

    @Override
    public float getDownfall() {
        return this.downfall;
    }

    @Override
    public BiomeEffects getEffects() {
        return this.effects;
    }
}
