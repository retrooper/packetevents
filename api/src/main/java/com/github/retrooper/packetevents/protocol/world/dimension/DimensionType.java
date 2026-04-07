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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntityRefSet;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.util.CodecNameable;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributeMap;
import com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributes;
import com.github.retrooper.packetevents.protocol.world.attributes.timelines.Timeline;
import com.github.retrooper.packetevents.protocol.world.clock.WorldClock;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.resources.TagKey;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.OptionalLong;

@NullMarked
public interface DimensionType extends MappedEntity, CopyableEntity<DimensionType>, DeepComparableEntity {

    NbtCodec<DimensionType> CODEC = new NbtMapCodec<DimensionType>() {
        @Override
        public DimensionType decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            DimensionTypeBuilder builder = DimensionTypeBuilder.dimensionTypeBuilder();

            ServerVersion version = wrapper.getServerVersion();
            if (version.isNewerThanOrEquals(ServerVersion.V_1_21_11)) {
                builder
                        .setHasFixedTime(compound.getBooleanOr("has_fixed_time", false))
                        .setSkybox(compound.getOr("skybox", Skybox.CODEC, Skybox.OVERWORLD, wrapper))
                        .setCardinalLight(compound.getOr("cardinal_light", CardinalLight.CODEC, CardinalLight.DEFAULT, wrapper))
                        .setAttributes(compound.getOr("attributes", EnvironmentAttributeMap.CODEC, EnvironmentAttributeMap.EMPTY, wrapper))
                        .setTimelines(compound.getOr("timelines", MappedEntitySet::decodeRefSet, MappedEntitySet.createEmpty(), wrapper));

                if (version.isNewerThanOrEquals(ServerVersion.V_26_1)) {
                    builder.setDefaultClock(compound.getOrNull("default_clock", WorldClock.CODEC, wrapper));
                    builder.setHasEnderDragonFight(compound.getBooleanOrThrow("has_ender_dragon_fight"));
                }
            } else {
                Number fixedTimeNum = compound.getNumberTagValueOrNull("fixed_time");
                builder
                        .setFixedTime(fixedTimeNum != null ? fixedTimeNum.longValue() : null)
                        .setAttribute(EnvironmentAttributes.GAMEPLAY_WATER_EVAPORATES, compound.getBoolean("ultrawarm"))
                        .setNatural(compound.getBoolean("natural"))
                        .setBedWorks(compound.getBoolean("bed_works"))
                        .setRespawnAnchorWorks(compound.getBoolean("respawn_anchor_works"))
                        .setAttribute(EnvironmentAttributes.GAMEPLAY_PIGLINS_ZOMBIFY, !compound.getBoolean("piglin_safe"))
                        .setAttribute(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID, compound.getBoolean("has_raids"));
                if (version.isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
                    builder.setEffects(compound.getOrThrow("effects", ResourceLocation.CODEC, wrapper));
                }
                if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
                    NBTNumber cloudHeightTag = compound.getNumberTagOrNull("cloud_height");
                    if (cloudHeightTag != null) {
                        builder.setAttribute(EnvironmentAttributes.VISUAL_CLOUD_HEIGHT, cloudHeightTag.getAsFloat());
                    }
                }
            }

            if (version.isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
                builder.setCoordinateScale(compound.getNumberTagOrThrow("coordinate_scale").getAsDouble());
                if (version.isNewerThanOrEquals(ServerVersion.V_1_17)) {
                    builder.setMinY(compound.getNumberTagOrThrow("min_y").getAsInt());
                    builder.setHeight(compound.getNumberTagOrThrow("height").getAsInt());
                    if (version.isNewerThanOrEquals(ServerVersion.V_1_19)) {
                        // TODO proper int provider decoding/encoding
                        builder.setMonsterSpawnLightLevel(compound.getTagOrThrow("monster_spawn_light_level"));
                        builder.setMonsterSpawnBlockLightLimit(compound.getNumberTagOrThrow("monster_spawn_block_light_limit").getAsInt());
                    }
                }
            } else {
                builder.setCoordinateScale(compound.getBoolean("shrunk") ? 8d : 1d);
            }

            builder.setInfiniburn(version.isNewerThanOrEquals(ServerVersion.V_1_18_2)
                    ? compound.getOrThrow("infiniburn", TagKey.CODEC, wrapper)
                    : new TagKey(compound.getOrThrow("infiniburn", ResourceLocation.CODEC, wrapper)));

            return builder
                    .setHasSkylight(compound.getBooleanOrThrow("has_skylight"))
                    .setHasCeiling(compound.getBooleanOrThrow("has_ceiling"))
                    .setLogicalHeight(compound.getNumberTagValueOrThrow("logical_height").intValue())
                    .setAmbientLight(compound.getNumberTagValueOrThrow("ambient_light").floatValue())
                    .build();
        }

        @Override
        public void encode(NBTCompound compound, PacketWrapper<?> wrapper, DimensionType value) throws NbtCodecException {
            ServerVersion version = wrapper.getServerVersion();
            if (version.isNewerThanOrEquals(ServerVersion.V_1_21_11)) {
                if (value.hasFixedTime()) {
                    compound.setTag("has_fixed_time", new NBTByte(true));
                }
                Skybox skybox = value.getSkybox();
                if (skybox != Skybox.OVERWORLD) {
                    compound.set("skybox", skybox, Skybox.CODEC, wrapper);
                }
                CardinalLight cardinalLight = value.getCardinalLight();
                if (cardinalLight != CardinalLight.DEFAULT) {
                    compound.set("cardinal_light", cardinalLight, CardinalLight.CODEC, wrapper);
                }
                EnvironmentAttributeMap env = value.getAttributes();
                if (!env.isEmpty()) {
                    compound.set("attributes", env, EnvironmentAttributeMap.CODEC, wrapper);
                }
                MappedEntityRefSet<Timeline> timelines = value.getTimelinesRef();
                if (!timelines.isEmpty()) {
                    compound.set("timelines", timelines, MappedEntitySet::encodeRefSet, wrapper);
                }
                if (version.isNewerThanOrEquals(ServerVersion.V_26_1)) {
                    WorldClock defaultClock = value.getDefaultClock();
                    if (defaultClock != null) {
                        compound.set("default_clock", defaultClock, WorldClock.CODEC, wrapper);
                    }
                    compound.setTag("has_ender_dragon_fight", new NBTByte(value.isHasEnderDragonFight()));
                }
            } else {
                OptionalLong fixedTime = value.getFixedTime();
                if (fixedTime.isPresent()) {
                    compound.setTag("fixed_time", new NBTLong(fixedTime.getAsLong()));
                }
                compound.setTag("ultrawarm", new NBTByte(value.isUltraWarm()));
                compound.setTag("natural", new NBTByte(value.isNatural()));
                compound.setTag("bed_works", new NBTByte(value.isBedWorking()));
                compound.setTag("respawn_anchor_works", new NBTByte(value.isRespawnAnchorWorking()));
                compound.setTag("piglin_safe", new NBTByte(value.isPiglinSafe()));
                compound.setTag("has_raids", new NBTByte(value.hasRaids()));
                if (version.isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
                    compound.set("effects", value.getEffectsLocation(), ResourceLocation.CODEC, wrapper);
                }
                if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
                    Integer cloudHeight = value.getCloudHeight();
                    if (cloudHeight != null) {
                        compound.setTag("cloud_height", new NBTInt(cloudHeight));
                    }
                }
            }
            if (version.isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
                compound.setTag("coordinate_scale", new NBTDouble(value.getCoordinateScale()));
                if (version.isNewerThanOrEquals(ServerVersion.V_1_17)) {
                    compound.setTag("min_y", new NBTInt(value.getMinY()));
                    compound.setTag("height", new NBTInt(value.getHeight()));
                    if (version.isNewerThanOrEquals(ServerVersion.V_1_19)) {
                        compound.setTag("monster_spawn_light_level", value.getMonsterSpawnLightLevel());
                        compound.setTag("monster_spawn_block_light_limit", new NBTInt(value.getMonsterSpawnBlockLightLimit()));
                    }
                }
            } else {
                compound.setTag("shrunk", new NBTByte(value.isShrunk()));
            }
            compound.setTag("has_skylight", new NBTByte(value.hasSkyLight()));
            compound.setTag("has_ceiling", new NBTByte(value.hasCeiling()));
            compound.setTag("logical_height", new NBTInt(value.getLogicalHeight()));
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_18_2)) {
                compound.set("infiniburn", value.getInfiniburn(), TagKey.CODEC, wrapper);
            } else {
                compound.set("infiniburn", value.getInfiniburn().getId(), ResourceLocation.CODEC, wrapper);
            }
            compound.setTag("ambient_light", new NBTFloat(value.getAmbientLight()));
        }
    }.codec();

    /**
     * @versions 1.21.11+
     */
    boolean hasFixedTime();

    /**
     * <strong>Warning:</strong> May be empty even if {@link #hasFixedTime()} returns true.
     *
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    OptionalLong getFixedTime();

    boolean hasSkyLight();

    boolean hasCeiling();

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    boolean isUltraWarm();

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    boolean isNatural();

    double getCoordinateScale();

    default boolean isShrunk() {
        return this.getCoordinateScale() > 1d;
    }

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    boolean isBedWorking();

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    boolean isRespawnAnchorWorking();

    /**
     * @versions 1.17+
     */
    int getMinY();

    /**
     * @versions 1.17+
     * @deprecated use {@link #getMinY()}
     */
    @Deprecated
    default int getMinY(ClientVersion version) {
        return this.getMinY();
    }

    /**
     * @versions 1.17+
     */
    int getHeight();

    /**
     * @versions 1.17+
     * @deprecated use {@link #getHeight()}
     */
    default int getHeight(ClientVersion version) {
        return this.getHeight();
    }

    int getLogicalHeight();

    /**
     * @deprecated use {@link #getLogicalHeight()}
     */
    @Deprecated
    default int getLogicalHeight(ClientVersion version) {
        return this.getLogicalHeight();
    }

    TagKey getInfiniburn();

    @Deprecated
    default String getInfiniburnTag() {
        return this.getInfiniburn().toString();
    }

    /**
     * @versions 1.16.2-1.21.10
     */
    @ApiStatus.Obsolete
    ResourceLocation getEffectsLocation();

    float getAmbientLight();

    /**
     * @versions 1.21.6-1.21.10
     */
    @ApiStatus.Obsolete
    @Nullable Integer getCloudHeight();

    /**
     * @versions 26.1+
     */
    @Nullable WorldClock getDefaultClock();

    /**
     * @versions 26.1+
     */
    boolean isHasEnderDragonFight();

    // monster settings

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    boolean isPiglinSafe();

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    boolean hasRaids();

    /**
     * @versions 1.19+
     */
    @ApiStatus.Experimental
    NBT getMonsterSpawnLightLevel();

    /**
     * @versions 1.19+
     */
    int getMonsterSpawnBlockLightLimit();

    // environment

    /**
     * @versions 1.21.11+
     */
    Skybox getSkybox();

    /**
     * @versions 1.21.11+
     */
    CardinalLight getCardinalLight();

    /**
     * @versions 1.21.11+
     */
    EnvironmentAttributeMap getAttributes();

    /**
     * @versions 1.21.11+
     */
    MappedEntitySet<Timeline> getTimelines();

    /**
     * @versions 1.21.11+
     */
    MappedEntityRefSet<Timeline> getTimelinesRef();

    // conversion utilities

    default DimensionTypeRef asRef(PacketWrapper<?> wrapper) {
        return new DimensionTypeRef.DirectRef(this, wrapper);
    }

    @Deprecated
    default DimensionTypeRef asRef(ClientVersion version) {
        return this.asRef(PacketWrapper.createDummyWrapper(version));
    }

    // nbt decoding/encoding

    @Deprecated
    static DimensionType decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        return CODEC.decode(nbt, PacketWrapper.createDummyWrapper(version)).copy(data);
    }

    @Deprecated
    static NBT encode(DimensionType dimensionType, ClientVersion version) {
        return CODEC.encode(PacketWrapper.createDummyWrapper(version), dimensionType);
    }

    /**
     * @versions 1.21.11+
     */
    enum Skybox implements CodecNameable {

        NONE("none"),
        OVERWORLD("overworld"),
        END("end"),
        ;

        public static final NbtCodec<Skybox> CODEC = NbtCodecs.forEnum(values());

        private final String name;

        Skybox(String name) {
            this.name = name;
        }

        @Override
        public String getCodecName() {
            return this.name;
        }
    }

    enum CardinalLight implements CodecNameable {

        DEFAULT("default"),
        NETHER("nether"),
        ;

        public static final NbtCodec<CardinalLight> CODEC = NbtCodecs.forEnum(values());

        private final String name;

        CardinalLight(String name) {
            this.name = name;
        }

        @Override
        public String getCodecName() {
            return this.name;
        }
    }
}
