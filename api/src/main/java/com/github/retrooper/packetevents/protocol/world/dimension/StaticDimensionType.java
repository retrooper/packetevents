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
import com.github.retrooper.packetevents.protocol.mapper.MappedEntityRefSet;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.mapper.ResolvableEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributeMap;
import com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributes;
import com.github.retrooper.packetevents.protocol.world.attributes.timelines.Timeline;
import com.github.retrooper.packetevents.protocol.world.attributes.timelines.Timelines;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.resources.TagKey;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.OptionalLong;

@NullMarked
public class StaticDimensionType extends AbstractMappedEntity implements DimensionType, ResolvableEntity {

    /**
     * @versions 1.21.11+
     */
    private final boolean hasFixedTime;
    /**
     * @versions 1.21.11+
     */
    private final Skybox skybox;
    /**
     * @versions 1.21.11+
     */
    private final CardinalLight cardinalLight;
    /**
     * @versions 1.21.11+
     */
    private final EnvironmentAttributeMap attributes;
    /**
     * @versions 1.21.11+
     */
    private final MappedEntityRefSet<Timeline> timelinesRef;
    /**
     * @versions 1.21.11+
     */
    private @Nullable MappedEntitySet<Timeline> timelines;
    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    private final @Nullable Long fixedTime;
    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    private final boolean natural;
    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    private final boolean bedWorks;
    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    private final boolean respawnAnchorWorks;
    /**
     * @versions 1.16.2-1.21.10
     */
    @ApiStatus.Obsolete
    private final @Nullable ResourceLocation effects;
    /**
     * @versions 1.16.2+
     */
    private final double coordinateScale;
    /**
     * @versions 1.17+
     */
    private final int minY;
    /**
     * @versions 1.17+
     */
    private final int height;
    /**
     * @versions 1.19+
     */
    @ApiStatus.Experimental
    private final NBT monsterSpawnLightLevel;
    /**
     * @versions 1.19+
     */
    private final int monsterSpawnBlockLightLimit;
    private final boolean hasSkylight;
    private final boolean hasCeiling;
    private final int logicalHeight;
    private final TagKey infiniburn;
    private final float ambientLight;

    /**
     * @deprecated use {@link DimensionTypeBuilder}
     */
    @Deprecated
    public StaticDimensionType(
            OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling,
            boolean ultraWarm, boolean natural, double coordinateScale, boolean bedWorking, boolean respawnAnchorWorking,
            int minY, int height, int logicalHeight, String infiniburnTag, @Nullable ResourceLocation effectsLocation,
            float ambientLight, boolean piglinSafe, boolean hasRaids, @Nullable NBT monsterSpawnLightLevel,
            int monsterSpawnBlockLightLimit
    ) {
        this(fixedTime, hasSkyLight, hasCeiling, ultraWarm, natural, coordinateScale, bedWorking, respawnAnchorWorking,
                minY, height, logicalHeight, infiniburnTag, effectsLocation, ambientLight, 192,
                piglinSafe, hasRaids, monsterSpawnLightLevel, monsterSpawnBlockLightLimit);
    }

    /**
     * @deprecated use {@link DimensionTypeBuilder}
     */
    @Deprecated
    public StaticDimensionType(
            OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling,
            boolean ultraWarm, boolean natural, double coordinateScale, boolean bedWorking, boolean respawnAnchorWorking,
            int minY, int height, int logicalHeight, String infiniburnTag, @Nullable ResourceLocation effectsLocation,
            float ambientLight, @Nullable Integer cloudHeight, boolean piglinSafe, boolean hasRaids,
            @Nullable NBT monsterSpawnLightLevel, int monsterSpawnBlockLightLimit
    ) {
        this(null, fixedTime.isPresent(), Skybox.OVERWORLD, CardinalLight.DEFAULT, EnvironmentAttributeMap.create()
                        .set(EnvironmentAttributes.GAMEPLAY_WATER_EVAPORATES, ultraWarm)
                        .set(EnvironmentAttributes.VISUAL_CLOUD_HEIGHT, cloudHeight != null ? cloudHeight : 192f)
                        .set(EnvironmentAttributes.GAMEPLAY_NETHER_PORTAL_SPAWNS_PIGLIN, !piglinSafe)
                        .set(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID, hasRaids)
                        .copyImmutable(), MappedEntitySet.createEmpty(), fixedTime.isPresent() ? fixedTime.getAsLong() : null,
                natural, bedWorking, respawnAnchorWorking, effectsLocation, coordinateScale, minY, height,
                monsterSpawnLightLevel != null ? monsterSpawnLightLevel : new NBTInt(7),
                monsterSpawnBlockLightLimit, hasSkyLight, hasCeiling, logicalHeight, TagKey.parse(infiniburnTag), ambientLight
        );
    }

    @ApiStatus.Internal
    public StaticDimensionType(
            @Nullable TypesBuilderData data, boolean hasFixedTime, Skybox skybox, CardinalLight cardinalLight,
            EnvironmentAttributeMap attributes, MappedEntityRefSet<Timeline> timelinesRef, @Nullable Long fixedTime,
            boolean natural, boolean bedWorks, boolean respawnAnchorWorks, @Nullable ResourceLocation effects,
            double coordinateScale, int minY, int height, NBT monsterSpawnLightLevel, int monsterSpawnBlockLightLimit,
            boolean hasSkylight, boolean hasCeiling, int logicalHeight, TagKey infiniburn, float ambientLight
    ) {
        super(data);
        this.hasFixedTime = hasFixedTime;
        this.skybox = skybox;
        this.cardinalLight = cardinalLight;
        this.attributes = attributes;
        this.timelinesRef = timelinesRef;
        this.fixedTime = fixedTime;
        this.natural = natural;
        this.bedWorks = bedWorks;
        this.respawnAnchorWorks = respawnAnchorWorks;
        this.effects = effects;
        this.coordinateScale = coordinateScale;
        this.minY = minY;
        this.height = height;
        this.monsterSpawnLightLevel = monsterSpawnLightLevel;
        this.monsterSpawnBlockLightLimit = monsterSpawnBlockLightLimit;
        this.hasSkylight = hasSkylight;
        this.hasCeiling = hasCeiling;
        this.logicalHeight = logicalHeight;
        this.infiniburn = infiniburn;
        this.ambientLight = ambientLight;
    }

    @Override
    public void doResolve(PacketWrapper<?> wrapper) {
        this.timelines = this.timelinesRef.resolve(wrapper, Timelines.getRegistry());
    }

    @Override
    public DimensionType copy(@Nullable TypesBuilderData newData) {
        return new StaticDimensionType(
                newData, this.hasFixedTime, this.skybox, this.cardinalLight, this.attributes, this.timelinesRef,
                this.fixedTime, this.natural, this.bedWorks, this.respawnAnchorWorks, this.effects, this.coordinateScale,
                this.minY, this.height, this.monsterSpawnLightLevel, this.monsterSpawnBlockLightLimit, this.hasSkylight,
                this.hasCeiling, this.logicalHeight, this.infiniburn, this.ambientLight
        );
    }

    @Override
    public boolean hasFixedTime() {
        return this.hasFixedTime;
    }

    @Override
    public OptionalLong getFixedTime() {
        return this.fixedTime != null
                ? OptionalLong.of(this.fixedTime)
                : OptionalLong.empty();
    }

    @Override
    public boolean hasSkyLight() {
        return this.hasSkylight;
    }

    @Override
    public boolean hasCeiling() {
        return this.hasCeiling;
    }

    @Override
    public boolean isUltraWarm() {
        return this.attributes.getOrDefault(EnvironmentAttributes.GAMEPLAY_WATER_EVAPORATES);
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
    public boolean isBedWorking() {
        return this.bedWorks;
    }

    @Override
    public boolean isRespawnAnchorWorking() {
        return this.respawnAnchorWorks;
    }

    @Override
    public int getMinY() {
        return this.minY;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getLogicalHeight() {
        return this.logicalHeight;
    }

    @Override
    public TagKey getInfiniburn() {
        return this.infiniburn;
    }

    @Override
    public ResourceLocation getEffectsLocation() {
        if (this.effects == null) {
            throw new UnsupportedOperationException();
        }
        return this.effects;
    }

    @Override
    public float getAmbientLight() {
        return this.ambientLight;
    }

    @Override
    public @Nullable Integer getCloudHeight() {
        return this.attributes.getOrDefault(EnvironmentAttributes.VISUAL_CLOUD_HEIGHT).intValue();
    }

    @Override
    public boolean isPiglinSafe() {
        return !this.attributes.getOrDefault(EnvironmentAttributes.GAMEPLAY_PIGLINS_ZOMBIFY);
    }

    @Override
    public boolean hasRaids() {
        return this.attributes.getOrDefault(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID);
    }

    @Override
    public NBT getMonsterSpawnLightLevel() {
        return this.monsterSpawnLightLevel;
    }

    @Override
    public int getMonsterSpawnBlockLightLimit() {
        return this.monsterSpawnBlockLightLimit;
    }

    @Override
    public Skybox getSkybox() {
        return this.skybox;
    }

    @Override
    public CardinalLight getCardinalLight() {
        return this.cardinalLight;
    }

    @Override
    public EnvironmentAttributeMap getAttributes() {
        return this.attributes;
    }

    @Override
    public MappedEntitySet<Timeline> getTimelines() {
        if (this.timelines == null) {
            throw new UnsupportedOperationException();
        }
        return this.timelines;
    }

    @Override
    public MappedEntityRefSet<Timeline> getTimelinesRef() {
        return this.timelinesRef;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        StaticDimensionType that = (StaticDimensionType) obj;
        if (this.hasFixedTime != that.hasFixedTime) return false;
        if (this.natural != that.natural) return false;
        if (this.bedWorks != that.bedWorks) return false;
        if (this.respawnAnchorWorks != that.respawnAnchorWorks) return false;
        if (Double.compare(that.coordinateScale, this.coordinateScale) != 0) return false;
        if (this.minY != that.minY) return false;
        if (this.height != that.height) return false;
        if (this.monsterSpawnBlockLightLimit != that.monsterSpawnBlockLightLimit) return false;
        if (this.hasSkylight != that.hasSkylight) return false;
        if (this.hasCeiling != that.hasCeiling) return false;
        if (this.logicalHeight != that.logicalHeight) return false;
        if (Float.compare(that.ambientLight, this.ambientLight) != 0) return false;
        if (this.skybox != that.skybox) return false;
        if (this.cardinalLight != that.cardinalLight) return false;
        if (!this.attributes.equals(that.attributes)) return false;
        if (!this.timelinesRef.equals(that.timelinesRef)) return false;
        if (!Objects.equals(this.fixedTime, that.fixedTime)) return false;
        if (!Objects.equals(this.effects, that.effects)) return false;
        if (!this.monsterSpawnLightLevel.equals(that.monsterSpawnLightLevel)) return false;
        return this.infiniburn.equals(that.infiniburn);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.hasFixedTime, this.skybox, this.cardinalLight, this.attributes, this.timelinesRef, this.fixedTime, this.natural, this.bedWorks, this.respawnAnchorWorks, this.effects, this.coordinateScale, this.minY, this.height, this.monsterSpawnLightLevel, this.monsterSpawnBlockLightLimit, this.hasSkylight, this.hasCeiling, this.logicalHeight, this.infiniburn, this.ambientLight);
    }
}
