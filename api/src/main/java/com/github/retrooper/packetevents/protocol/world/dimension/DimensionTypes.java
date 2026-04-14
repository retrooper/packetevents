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

import com.github.retrooper.packetevents.protocol.color.AlphaColor;
import com.github.retrooper.packetevents.protocol.color.Color;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.protocol.world.attributes.AmbientSounds;
import com.github.retrooper.packetevents.protocol.world.attributes.BackgroundMusic;
import com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributes;
import com.github.retrooper.packetevents.protocol.world.biome.BiomeEffects.MoodSettings;
import com.github.retrooper.packetevents.protocol.world.biome.BiomeEffects.MusicSettings;
import com.github.retrooper.packetevents.protocol.world.clock.WorldClocks;
import com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.VersionRange;
import com.github.retrooper.packetevents.util.adventure.AdventureNbtUtil;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.util.Ticks;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.function.Consumer;

@NullMarked
public final class DimensionTypes {

    private static final VersionedRegistry<DimensionType> REGISTRY = new VersionedRegistry<>(
            "dimension_type", ClientVersion.V_1_18);

    private DimensionTypes() {
    }

    @ApiStatus.Internal
    public static DimensionType define(String name, Consumer<DimensionTypeBuilder> builder) {
        return define(name, VersionRange.ALL_VERSIONS, builder);
    }

    @ApiStatus.Internal
    public static DimensionType define(String name, VersionRange range, Consumer<DimensionTypeBuilder> builder) {
        return REGISTRY.define(name, range, data -> {
            DimensionTypeBuilder dimBuilder = DimensionTypeBuilder.dimensionTypeBuilder();
            builder.accept(dimBuilder); // fill with data
            return dimBuilder.build(data);
        });
    }

    public static VersionedRegistry<DimensionType> getRegistry() {
        return REGISTRY;
    }

    /**
     * @versions -1.17.2
     */
    @ApiStatus.Obsolete
    public static final DimensionType OVERWORLD_PRE_1_18 = define("overworld", new VersionRange(null, ClientVersion.V_1_17_1),
            builder -> builder
                    .setCoordinateScale(1d)
                    .setMinY(0).setHeight(256).setLogicalHeight(256)
                    .setInfiniburn(BlockTags.INFINIBURN_OVERWORLD.getKey())
                    .setHasCeiling(false).setHasSkylight(true).setAmbientLight(0f)
                    .setNatural(true).setRespawnAnchorWorks(false).setBedWorks(true)
                    .setEffects(new ResourceLocation("overworld"))
                    .setAttribute(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID, true)
    );
    /**
     * @versions 1.18+
     */
    public static final DimensionType OVERWORLD = define("overworld", new VersionRange(ClientVersion.V_1_18, null),
            builder -> builder
                    // .setTimelines(new MappedEntityRef.Named<>()) TODO how?
                    .setMonsterSpawnLightLevel(AdventureNbtUtil.fromAdventure(
                            CompoundBinaryTag.builder()
                                    .putString("type", new ResourceLocation("uniform").toString())
                                    .putInt("min_inclusive", 0)
                                    .putInt("max_inclusive", 7)
                                    // legacy formatting
                                    .put("value", CompoundBinaryTag.builder()
                                            .putInt("min_inclusive", 0)
                                            .putInt("max_inclusive", 7)
                                            .build())
                                    .build()
                    ))
                    .setMonsterSpawnBlockLightLimit(0)
                    .setCoordinateScale(1d)
                    .setMinY(-64).setHeight(384).setLogicalHeight(384)
                    .setInfiniburn(BlockTags.INFINIBURN_OVERWORLD.getKey())
                    .setHasCeiling(false).setHasSkylight(true).setAmbientLight(0f)
                    .setAttribute(EnvironmentAttributes.AUDIO_BACKGROUND_MUSIC,
                            new BackgroundMusic(
                                    new MusicSettings(
                                            Sounds.MUSIC_GAME,
                                            10 * 60 * Ticks.TICKS_PER_SECOND,
                                            20 * 60 * Ticks.TICKS_PER_SECOND,
                                            false
                                    ),
                                    new MusicSettings(
                                            Sounds.MUSIC_CREATIVE,
                                            10 * 60 * Ticks.TICKS_PER_SECOND,
                                            20 * 60 * Ticks.TICKS_PER_SECOND,
                                            false
                                    ),
                                    null
                            ))
                    .setAttribute(EnvironmentAttributes.AUDIO_AMBIENT_SOUNDS, new AmbientSounds(
                            null,
                            new MoodSettings(
                                    Sounds.AMBIENT_CAVE,
                                    5 * 60 * Ticks.TICKS_PER_SECOND,
                                    8,
                                    2d
                            ),
                            Collections.emptyList()
                    ))
                    .setAttribute(EnvironmentAttributes.VISUAL_CLOUD_HEIGHT, 192.33f)
                    .setAttribute(EnvironmentAttributes.VISUAL_CLOUD_COLOR, new AlphaColor(0xCCFFFFFF))
                    .setAttribute(EnvironmentAttributes.VISUAL_FOG_COLOR, new Color(0xC0D8FF))
                    .setAttribute(EnvironmentAttributes.VISUAL_SKY_COLOR, new Color(0x78A7FF))
                    // pre 1.21.11 properties
                    .setNatural(true).setRespawnAnchorWorks(false).setBedWorks(true)
                    .setEffects(new ResourceLocation("overworld"))
                    .setAttribute(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID, true)
                    // 26.1+
                    .setAttribute(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, new Color(0x0A0A0A))
                    .setDefaultClock(WorldClocks.OVERWORLD)
    );

    /**
     * @versions -1.17.2
     */
    @ApiStatus.Obsolete
    public static final DimensionType OVERWORLD_CAVES_PRE_1_18 = define("overworld_caves", new VersionRange(null, ClientVersion.V_1_17_1),
            builder -> builder
                    .setCoordinateScale(1d)
                    .setMinY(0).setHeight(256).setLogicalHeight(256)
                    .setInfiniburn(BlockTags.INFINIBURN_OVERWORLD.getKey())
                    .setHasCeiling(true).setHasSkylight(true).setAmbientLight(0f)
                    .setNatural(true).setRespawnAnchorWorks(false).setBedWorks(true)
                    .setEffects(new ResourceLocation("overworld"))
                    .setAttribute(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID, true)
    );
    /**
     * @versions 1.18+
     */
    public static final DimensionType OVERWORLD_CAVES = define("overworld_caves", new VersionRange(ClientVersion.V_1_18, null),
            builder -> builder
                    // .setTimelines(new MappedEntityRef.Named<>()) TODO how?
                    .setMonsterSpawnLightLevel(AdventureNbtUtil.fromAdventure(
                            CompoundBinaryTag.builder()
                                    .putString("type", new ResourceLocation("uniform").toString())
                                    .putInt("min_inclusive", 0)
                                    .putInt("max_inclusive", 7)
                                    // legacy formatting
                                    .put("value", CompoundBinaryTag.builder()
                                            .putInt("min_inclusive", 0)
                                            .putInt("max_inclusive", 7)
                                            .build())
                                    .build()
                    ))
                    .setMonsterSpawnBlockLightLimit(0)
                    .setCoordinateScale(1d)
                    .setMinY(-64).setHeight(384).setLogicalHeight(384)
                    .setInfiniburn(BlockTags.INFINIBURN_OVERWORLD.getKey())
                    .setHasCeiling(true).setHasSkylight(true).setAmbientLight(0f)
                    .setAttribute(EnvironmentAttributes.AUDIO_BACKGROUND_MUSIC,
                            new BackgroundMusic(
                                    new MusicSettings(
                                            Sounds.MUSIC_GAME,
                                            10 * 60 * Ticks.TICKS_PER_SECOND,
                                            20 * 60 * Ticks.TICKS_PER_SECOND,
                                            false
                                    ),
                                    new MusicSettings(
                                            Sounds.MUSIC_CREATIVE,
                                            10 * 60 * Ticks.TICKS_PER_SECOND,
                                            20 * 60 * Ticks.TICKS_PER_SECOND,
                                            false
                                    ),
                                    null
                            ))
                    .setAttribute(EnvironmentAttributes.AUDIO_AMBIENT_SOUNDS, new AmbientSounds(
                            null,
                            new MoodSettings(
                                    Sounds.AMBIENT_CAVE,
                                    5 * 60 * Ticks.TICKS_PER_SECOND,
                                    8,
                                    2d
                            ),
                            Collections.emptyList()
                    ))
                    .setAttribute(EnvironmentAttributes.VISUAL_CLOUD_HEIGHT, 192.33f)
                    .setAttribute(EnvironmentAttributes.VISUAL_CLOUD_COLOR, new AlphaColor(0xCCFFFFFF))
                    .setAttribute(EnvironmentAttributes.VISUAL_FOG_COLOR, new Color(0xC0D8FF))
                    .setAttribute(EnvironmentAttributes.VISUAL_SKY_COLOR, new Color(0x78A7FF))
                    // pre 1.21.11 properties
                    .setNatural(true).setRespawnAnchorWorks(false).setBedWorks(true)
                    .setEffects(new ResourceLocation("overworld"))
                    .setAttribute(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID, true)
                    // 26.1+
                    .setAttribute(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, new Color(0x0A0A0A))
                    .setDefaultClock(WorldClocks.OVERWORLD)
    );

    private static final Consumer<DimensionTypeBuilder> THE_END_COMMON =
            builder -> builder
                    // .setTimelines(new MappedEntityRef.Named<>()) TODO how?
                    .setHasFixedTime(true)
                    .setMonsterSpawnBlockLightLimit(0)
                    .setMonsterSpawnLightLevel(new NBTInt(15))
                    .setCoordinateScale(1d)
                    .setSkybox(DimensionType.Skybox.END)
                    .setMinY(0).setHeight(256).setLogicalHeight(256)
                    .setInfiniburn(BlockTags.INFINIBURN_END.getKey())
                    .setHasCeiling(false).setHasSkylight(true).setAmbientLight(0.25f)
                    .setAttribute(EnvironmentAttributes.AUDIO_BACKGROUND_MUSIC,
                            new BackgroundMusic(
                                    new MusicSettings(
                                            Sounds.MUSIC_END,
                                            5 * 60 * Ticks.TICKS_PER_SECOND,
                                            20 * 60 * Ticks.TICKS_PER_SECOND,
                                            true
                                    ),
                                    null,
                                    null
                            ))
                    .setAttribute(EnvironmentAttributes.AUDIO_AMBIENT_SOUNDS, new AmbientSounds(
                            null,
                            new MoodSettings(
                                    Sounds.AMBIENT_CAVE,
                                    5 * 60 * Ticks.TICKS_PER_SECOND,
                                    8,
                                    2d
                            ),
                            Collections.emptyList()
                    ))
                    .setAttribute(EnvironmentAttributes.VISUAL_FOG_COLOR, new Color(0x181318))
                    .setAttribute(EnvironmentAttributes.VISUAL_SKY_COLOR, new Color(0x000000))
                    .setAttribute(EnvironmentAttributes.VISUAL_SKY_LIGHT_COLOR, new Color(0xAC60CD))
                    .setAttribute(EnvironmentAttributes.VISUAL_SKY_LIGHT_FACTOR, 0f)
                    // pre 1.21.11 properties
                    .setNatural(false).setRespawnAnchorWorks(false).setBedWorks(false)
                    .setEffects(new ResourceLocation("the_end")).setFixedTime(5L * 60L * Ticks.TICKS_PER_SECOND)
                    .setAttribute(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID, true)
                    // 26.1+
                    .setAttribute(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, new Color(0x3F473F))
                    .setDefaultClock(WorldClocks.THE_END)
                    .setHasEnderDragonFight(true);

    /**
     * @versions -1.21.8
     */
    @ApiStatus.Obsolete
    public static final DimensionType THE_END_PRE_1_21_9 = define("the_end",
            new VersionRange(null, ClientVersion.V_1_21_7),
            THE_END_COMMON.andThen(builder -> builder.setHasSkylight(false)));

    /**
     * @versions 1.21.9+
     */
    public static final DimensionType THE_END = define("the_end",
            new VersionRange(ClientVersion.V_1_21_9, null), THE_END_COMMON);

    public static final DimensionType THE_NETHER = define("the_nether",
            builder -> builder
                    // .setTimelines(new MappedEntityRef.Named<>()) TODO how?
                    .setHasFixedTime(true)
                    .setMonsterSpawnLightLevel(new NBTInt(7))
                    .setMonsterSpawnBlockLightLimit(15)
                    .setCoordinateScale(8d)
                    .setSkybox(DimensionType.Skybox.NONE)
                    .setCardinalLight(DimensionType.CardinalLight.NETHER)
                    .setMinY(0).setHeight(256).setLogicalHeight(128)
                    .setInfiniburn(BlockTags.INFINIBURN_NETHER.getKey())
                    .setHasCeiling(true).setHasSkylight(false).setAmbientLight(0.1f)
                    .setAttribute(EnvironmentAttributes.VISUAL_SKY_LIGHT_FACTOR, 0f)
                    .setAttribute(EnvironmentAttributes.VISUAL_SKY_LIGHT_COLOR, new Color(0x7A7AFF))
                    .setAttribute(EnvironmentAttributes.GAMEPLAY_SKY_LIGHT_LEVEL, 4f)
                    .setAttribute(EnvironmentAttributes.VISUAL_FOG_START_DISTANCE, 10f)
                    .setAttribute(EnvironmentAttributes.VISUAL_FOG_END_DISTANCE, 96f)
                    .setAttribute(EnvironmentAttributes.VISUAL_DEFAULT_DRIPSTONE_PARTICLE,
                            new Particle<>(ParticleTypes.DRIPPING_DRIPSTONE_LAVA))
                    .setAttribute(EnvironmentAttributes.GAMEPLAY_PIGLINS_ZOMBIFY, false)
                    .setAttribute(EnvironmentAttributes.GAMEPLAY_FAST_LAVA, true)
                    .setAttribute(EnvironmentAttributes.GAMEPLAY_WATER_EVAPORATES, true)
                    // pre 1.21.11 properties
                    .setNatural(false).setRespawnAnchorWorks(true).setBedWorks(false)
                    .setEffects(new ResourceLocation("the_nether"))
                    .setFixedTime(15L * 60L * Ticks.TICKS_PER_SECOND)
                    // 26.1+
                    .setAttribute(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, new Color(0x302821))
    );

    static {
        REGISTRY.unloadMappings();
    }
}
