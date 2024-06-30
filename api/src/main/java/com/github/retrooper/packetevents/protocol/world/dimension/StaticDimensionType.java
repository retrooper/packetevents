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

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalLong;

public class StaticDimensionType extends AbstractMappedEntity implements DimensionType {

    private final OptionalLong fixedTime;
    private final boolean hasSkyLight;
    private final boolean hasCeiling;
    private final boolean ultraWarm;
    private final boolean natural;
    private final double coordinateScale;
    private final boolean bedWorks;
    private final boolean respawnAnchorWorking;
    private final int minY;
    private final int height;
    private final int logicalHeight;
    private final String infiniburnTag;
    private final ResourceLocation effectsLocation;
    private final float ambientLight;
    private final boolean piglinSafe;
    private final boolean hasRaids;
    private final NBT monsterSpawnLightLevel;
    private final int monsterSpawnBlockLightLimit;

    public StaticDimensionType(
            OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling,
            boolean ultraWarm, boolean natural, double coordinateScale, boolean bedWorks, boolean respawnAnchorWorking,
            int minY, int height, int logicalHeight, String infiniburnTag, ResourceLocation effectsLocation,
            float ambientLight, boolean piglinSafe, boolean hasRaids, NBT monsterSpawnLightLevel,
            int monsterSpawnBlockLightLimit
    ) {
        this(null, fixedTime, hasSkyLight, hasCeiling, ultraWarm, natural, coordinateScale, bedWorks, respawnAnchorWorking,
                minY, height, logicalHeight, infiniburnTag, effectsLocation, ambientLight, piglinSafe, hasRaids,
                monsterSpawnLightLevel, monsterSpawnBlockLightLimit);
    }

    public StaticDimensionType(
            @Nullable TypesBuilderData data, OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling,
            boolean ultraWarm, boolean natural, double coordinateScale, boolean bedWorks, boolean respawnAnchorWorking,
            int minY, int height, int logicalHeight, String infiniburnTag, ResourceLocation effectsLocation,
            float ambientLight, boolean piglinSafe, boolean hasRaids, NBT monsterSpawnLightLevel,
            int monsterSpawnBlockLightLimit
    ) {
        super(data);
        this.fixedTime = fixedTime;
        this.hasSkyLight = hasSkyLight;
        this.hasCeiling = hasCeiling;
        this.ultraWarm = ultraWarm;
        this.natural = natural;
        this.coordinateScale = coordinateScale;
        this.bedWorks = bedWorks;
        this.respawnAnchorWorking = respawnAnchorWorking;
        this.minY = minY;
        this.height = height;
        this.logicalHeight = logicalHeight;
        this.infiniburnTag = infiniburnTag;
        this.effectsLocation = effectsLocation;
        this.ambientLight = ambientLight;
        this.piglinSafe = piglinSafe;
        this.hasRaids = hasRaids;
        this.monsterSpawnLightLevel = monsterSpawnLightLevel;
        this.monsterSpawnBlockLightLimit = monsterSpawnBlockLightLimit;
    }

    @Override
    public OptionalLong getFixedTime() {
        return this.fixedTime;
    }

    @Override
    public boolean hasCeiling() {
        return this.hasCeiling;
    }

    @Override
    public boolean hasSkyLight() {
        return this.hasSkyLight;
    }

    @Override
    public boolean isUltraWarm() {
        return this.ultraWarm;
    }

    @Override
    public boolean isNatural() {
        return this.natural;
    }

    @Override
    public double getCoordinateScale() {
        return this.coordinateScale;
    }

    @Override
    public boolean isBedWorks() {
        return this.bedWorks;
    }

    @Override
    public boolean isRespawnAnchorWorking() {
        return this.respawnAnchorWorking;
    }

    @Override
    public int getMinY(ClientVersion version) {
        return this.minY;
    }

    @Override
    public int getHeight(ClientVersion version) {
        return this.height;
    }

    @Override
    public int getLogicalHeight(ClientVersion version) {
        return this.logicalHeight;
    }

    @Override
    public String getInfiniburnTag() {
        return this.infiniburnTag;
    }

    @Override
    public ResourceLocation getEffectsLocation() {
        return this.effectsLocation;
    }

    @Override
    public float getAmbientLight() {
        return this.ambientLight;
    }

    @Override
    public boolean isPiglinSafe() {
        return this.piglinSafe;
    }

    @Override
    public boolean hasRaids() {
        return this.hasRaids;
    }

    @Override
    public NBT getMonsterSpawnLightLevel() {
        return this.monsterSpawnLightLevel;
    }

    @Override
    public int getMonsterSpawnBlockLightLimit() {
        return this.monsterSpawnBlockLightLimit;
    }
}
