/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.particle.type;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleBlockStateData;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleColorData;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleDustColorTransitionData;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleDustData;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleItemStackData;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleSculkChargeData;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleShriekData;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleVibrationData;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.MappingHelper;
import com.github.retrooper.packetevents.util.mappings.TypesBuilder;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.PacketWrapper.Reader;
import com.github.retrooper.packetevents.wrapper.PacketWrapper.Writer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ParticleTypes {

    private static final Map<String, ParticleType<?>> PARTICLE_TYPE_MAP = new HashMap<>();
    private static final Map<Byte, Map<Integer, ParticleType<?>>> PARTICLE_TYPE_ID_MAP = new HashMap<>();
    private static final TypesBuilder TYPES_BUILDER = new TypesBuilder("particle/particle_type_mappings");

    public static ParticleType<ParticleData> define(String key) {
        return define(
                key,
                wrapper -> ParticleData.emptyData(), null,
                (nbt, version) -> ParticleData.emptyData(), null
        );
    }

    public static <T extends ParticleData> ParticleType<T> define(
            String key,
            Reader<T> reader, @Nullable Writer<T> writer,
            Decoder<T> decoder, @Nullable Encoder<T> encoder
    ) {
        TypesBuilderData data = TYPES_BUILDER.define(key);
        ParticleType<T> particleType = new ParticleType<T>() {
            @Override
            public T readData(PacketWrapper<?> wrapper) {
                return reader.apply(wrapper);
            }

            @Override
            public void writeData(PacketWrapper<?> wrapper, T data) {
                if (writer != null) {
                    writer.accept(wrapper, data);
                } else if (!data.isEmpty()) {
                    throw new UnsupportedOperationException("Trying to write non-empty data for " + this.getName());
                }
            }

            @Override
            public T decodeData(NBTCompound compound, ClientVersion version) {
                return decoder.decode(compound, version);
            }

            @Override
            public void encodeData(T data, ClientVersion version, NBTCompound compound) {
                if (encoder != null) {
                    encoder.encode(data, version, compound);
                } else if (!data.isEmpty()) {
                    throw new UnsupportedOperationException("Trying to encode non-empty data for " + this.getName());
                }
            }

            @Override
            public ResourceLocation getName() {
                return data.getName();
            }

            @Override
            public int getId(ClientVersion version) {
                return MappingHelper.getId(version, TYPES_BUILDER, data);
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof ParticleType<?>) {
                    return this.getName().equals(((ParticleType<?>) obj).getName());
                }
                return false;
            }
        };
        MappingHelper.registerMapping(TYPES_BUILDER, PARTICLE_TYPE_MAP, PARTICLE_TYPE_ID_MAP, particleType);
        return particleType;
    }

    //with minecraft:key
    public static ParticleType<?> getByName(String name) {
        return PARTICLE_TYPE_MAP.get(name);
    }

    public static ParticleType<?> getById(ClientVersion version, int id) {
        int index = TYPES_BUILDER.getDataIndex(version);
        Map<Integer, ParticleType<?>> typeIdMap = PARTICLE_TYPE_ID_MAP.get((byte) index);
        return typeIdMap.get(id);
    }

    @Deprecated // Removed in 1.20.5
    public static final ParticleType<ParticleData> AMBIENT_ENTITY_EFFECT = define("ambient_entity_effect");
    public static final ParticleType<ParticleData> ANGRY_VILLAGER = define("angry_villager");
    public static final ParticleType<ParticleBlockStateData> BLOCK = define("block",
            ParticleBlockStateData::read, ParticleBlockStateData::write,
            ParticleBlockStateData::decode, ParticleBlockStateData::encode);
    public static final ParticleType<ParticleBlockStateData> BLOCK_MARKER = define("block_marker",
            ParticleBlockStateData::read, ParticleBlockStateData::write,
            ParticleBlockStateData::decode, ParticleBlockStateData::encode);
    public static final ParticleType<ParticleData> BUBBLE = define("bubble");
    public static final ParticleType<ParticleData> CLOUD = define("cloud");
    public static final ParticleType<ParticleData> CRIT = define("crit");
    public static final ParticleType<ParticleData> DAMAGE_INDICATOR = define("damage_indicator");
    public static final ParticleType<ParticleData> DRAGON_BREATH = define("dragon_breath");
    public static final ParticleType<ParticleData> DRIPPING_LAVA = define("dripping_lava");
    public static final ParticleType<ParticleData> FALLING_LAVA = define("falling_lava");
    public static final ParticleType<ParticleData> LANDING_LAVA = define("landing_lava");
    public static final ParticleType<ParticleData> DRIPPING_WATER = define("dripping_water");
    public static final ParticleType<ParticleData> FALLING_WATER = define("falling_water");
    public static final ParticleType<ParticleDustData> DUST = define("dust",
            ParticleDustData::read, ParticleDustData::write,
            ParticleDustData::decode, ParticleDustData::encode);
    public static final ParticleType<ParticleDustColorTransitionData> DUST_COLOR_TRANSITION = define("dust_color_transition",
            ParticleDustColorTransitionData::read, ParticleDustColorTransitionData::write,
            ParticleDustColorTransitionData::decode, ParticleDustColorTransitionData::encode);
    public static final ParticleType<ParticleData> EFFECT = define("effect");
    public static final ParticleType<ParticleData> ELDER_GUARDIAN = define("elder_guardian");
    public static final ParticleType<ParticleData> ENCHANTED_HIT = define("enchanted_hit");
    public static final ParticleType<ParticleData> ENCHANT = define("enchant");
    public static final ParticleType<ParticleData> END_ROD = define("end_rod");
    public static final ParticleType<ParticleColorData> ENTITY_EFFECT = define("entity_effect",
            ParticleColorData::read, ParticleColorData::write,
            ParticleColorData::decode, ParticleColorData::encode);
    public static final ParticleType<ParticleData> EXPLOSION_EMITTER = define("explosion_emitter");
    public static final ParticleType<ParticleData> EXPLOSION = define("explosion");
    public static final ParticleType<ParticleData> SONIC_BOOM = define("sonic_boom");
    public static final ParticleType<ParticleBlockStateData> FALLING_DUST = define("falling_dust",
            ParticleBlockStateData::read, ParticleBlockStateData::write,
            ParticleBlockStateData::decode, ParticleBlockStateData::encode);
    public static final ParticleType<ParticleData> FIREWORK = define("firework");
    public static final ParticleType<ParticleData> FISHING = define("fishing");
    public static final ParticleType<ParticleData> FLAME = define("flame");
    public static final ParticleType<ParticleData> SCULK_SOUL = define("sculk_soul");
    public static final ParticleType<ParticleSculkChargeData> SCULK_CHARGE = define("sculk_charge",
            ParticleSculkChargeData::read, ParticleSculkChargeData::write,
            ParticleSculkChargeData::decode, ParticleSculkChargeData::encode);
    public static final ParticleType<ParticleData> SCULK_CHARGE_POP = define("sculk_charge_pop");
    public static final ParticleType<ParticleData> SOUL_FIRE_FLAME = define("soul_fire_flame");
    public static final ParticleType<ParticleData> SOUL = define("soul");
    public static final ParticleType<ParticleData> FLASH = define("flash");
    public static final ParticleType<ParticleData> HAPPY_VILLAGER = define("happy_villager");
    public static final ParticleType<ParticleData> COMPOSTER = define("composter");
    public static final ParticleType<ParticleData> HEART = define("heart");
    public static final ParticleType<ParticleData> INSTANT_EFFECT = define("instant_effect");
    public static final ParticleType<ParticleItemStackData> ITEM = define("item",
            ParticleItemStackData::read, ParticleItemStackData::write,
            ParticleItemStackData::decode, ParticleItemStackData::encode);
    public static final ParticleType<ParticleVibrationData> VIBRATION = define("vibration",
            ParticleVibrationData::read, ParticleVibrationData::write,
            ParticleVibrationData::decode, ParticleVibrationData::encode);
    public static final ParticleType<ParticleData> ITEM_SLIME = define("item_slime");
    public static final ParticleType<ParticleData> ITEM_SNOWBALL = define("item_snowball");
    public static final ParticleType<ParticleData> LARGE_SMOKE = define("large_smoke");
    public static final ParticleType<ParticleData> LAVA = define("lava");
    public static final ParticleType<ParticleData> MYCELIUM = define("mycelium");
    public static final ParticleType<ParticleData> NOTE = define("note");
    public static final ParticleType<ParticleData> POOF = define("poof");
    public static final ParticleType<ParticleData> PORTAL = define("portal");
    public static final ParticleType<ParticleData> RAIN = define("rain");
    public static final ParticleType<ParticleData> SMOKE = define("smoke");
    public static final ParticleType<ParticleData> SNEEZE = define("sneeze");
    public static final ParticleType<ParticleData> SPIT = define("spit");
    public static final ParticleType<ParticleData> SQUID_INK = define("squid_ink");
    public static final ParticleType<ParticleData> SWEEP_ATTACK = define("sweep_attack");
    public static final ParticleType<ParticleData> TOTEM_OF_UNDYING = define("totem_of_undying");
    public static final ParticleType<ParticleData> UNDERWATER = define("underwater");
    public static final ParticleType<ParticleData> SPLASH = define("splash");
    public static final ParticleType<ParticleData> WITCH = define("witch");
    public static final ParticleType<ParticleData> BUBBLE_POP = define("bubble_pop");
    public static final ParticleType<ParticleData> CURRENT_DOWN = define("current_down");
    public static final ParticleType<ParticleData> BUBBLE_COLUMN_UP = define("bubble_column_up");
    public static final ParticleType<ParticleData> NAUTILUS = define("nautilus");
    public static final ParticleType<ParticleData> DOLPHIN = define("dolphin");
    public static final ParticleType<ParticleData> CAMPFIRE_COSY_SMOKE = define("campfire_cosy_smoke");
    public static final ParticleType<ParticleData> CAMPFIRE_SIGNAL_SMOKE = define("campfire_signal_smoke");
    public static final ParticleType<ParticleData> DRIPPING_HONEY = define("dripping_honey");
    public static final ParticleType<ParticleData> FALLING_HONEY = define("falling_honey");
    public static final ParticleType<ParticleData> LANDING_HONEY = define("landing_honey");
    public static final ParticleType<ParticleData> FALLING_NECTAR = define("falling_nectar");
    public static final ParticleType<ParticleData> FALLING_SPORE_BLOSSOM = define("falling_spore_blossom");
    public static final ParticleType<ParticleData> ASH = define("ash");
    public static final ParticleType<ParticleData> CRIMSON_SPORE = define("crimson_spore");
    public static final ParticleType<ParticleData> WARPED_SPORE = define("warped_spore");
    public static final ParticleType<ParticleData> SPORE_BLOSSOM_AIR = define("spore_blossom_air");
    public static final ParticleType<ParticleData> DRIPPING_OBSIDIAN_TEAR = define("dripping_obsidian_tear");
    public static final ParticleType<ParticleData> FALLING_OBSIDIAN_TEAR = define("falling_obsidian_tear");
    public static final ParticleType<ParticleData> LANDING_OBSIDIAN_TEAR = define("landing_obsidian_tear");
    public static final ParticleType<ParticleData> REVERSE_PORTAL = define("reverse_portal");
    public static final ParticleType<ParticleData> WHITE_ASH = define("white_ash");
    public static final ParticleType<ParticleData> SMALL_FLAME = define("small_flame");
    public static final ParticleType<ParticleData> SNOWFLAKE = define("snowflake");
    public static final ParticleType<ParticleData> DRIPPING_DRIPSTONE_LAVA = define("dripping_dripstone_lava");
    public static final ParticleType<ParticleData> FALLING_DRIPSTONE_LAVA = define("falling_dripstone_lava");
    public static final ParticleType<ParticleData> DRIPPING_DRIPSTONE_WATER = define("dripping_dripstone_water");
    public static final ParticleType<ParticleData> FALLING_DRIPSTONE_WATER = define("falling_dripstone_water");
    public static final ParticleType<ParticleData> GLOW_SQUID_INK = define("glow_squid_ink");
    public static final ParticleType<ParticleData> GLOW = define("glow");
    public static final ParticleType<ParticleData> WAX_ON = define("wax_on");
    public static final ParticleType<ParticleData> WAX_OFF = define("wax_off");
    public static final ParticleType<ParticleData> ELECTRIC_SPARK = define("electric_spark");
    public static final ParticleType<ParticleData> SCRAPE = define("scrape");
    public static final ParticleType<ParticleShriekData> SHRIEK = define("shriek",
            ParticleShriekData::read, ParticleShriekData::write,
            ParticleShriekData::decode, ParticleShriekData::encode);
    //Added in 1.19.3, BUT REMOVED in 1.20, replaced with CHERRY_LEAVES
    public static final ParticleType<ParticleData> DRIPPING_CHERRY_LEAVES = define("dripping_cherry_leaves");
    public static final ParticleType<ParticleData> FALLING_CHERRY_LEAVES = define("falling_cherry_leaves");
    public static final ParticleType<ParticleData> LANDING_CHERRY_LEAVES = define("landing_cherry_leaves");

    // Added in 1.20
    public static final ParticleType<ParticleData> CHERRY_LEAVES = define("cherry_leaves");
    public static final ParticleType<ParticleData> EGG_CRACK = define("egg_crack");

    // Added in 1.20.3
    public static final ParticleType<ParticleData> GUST = define("gust");
    @Deprecated // Replaced with GUST_EMITTER_LARGE/GUST_EMITTER_SMALL in 1.20.5
    public static final ParticleType<ParticleData> GUST_EMITTER = define("gust_emitter");
    public static final ParticleType<ParticleData> WHITE_SMOKE = define("white_smoke");
    public static final ParticleType<ParticleData> DUST_PLUME = define("dust_plume");
    public static final ParticleType<ParticleData> GUST_DUST = define("gust_dust");
    public static final ParticleType<ParticleData> TRIAL_SPAWNER_DETECTION = define("trial_spawner_detection");

    // Added in 1.20.5
    public static final ParticleType<ParticleData> SMALL_GUST = define("small_gust");
    public static final ParticleType<ParticleData> GUST_EMITTER_LARGE = define("gust_emitter_large");
    public static final ParticleType<ParticleData> GUST_EMITTER_SMALL = define("gust_emitter_small");
    public static final ParticleType<ParticleData> INFESTED = define("infested");
    public static final ParticleType<ParticleData> ITEM_COBWEB = define("item_cobweb");
    public static final ParticleType<ParticleData> TRIAL_SPAWNER_DETECTION_OMINOUS = define("trial_spawner_detection_ominous");
    public static final ParticleType<ParticleData> VAULT_CONNECTION = define("vault_connection");
    public static final ParticleType<ParticleBlockStateData> DUST_PILLAR = define("dust_pillar",
            ParticleBlockStateData::read, ParticleBlockStateData::write,
            ParticleBlockStateData::decode, ParticleBlockStateData::encode);
    public static final ParticleType<ParticleData> OMINOUS_SPAWNING = define("ominous_spawning");
    public static final ParticleType<ParticleData> RAID_OMEN = define("raid_omen");
    public static final ParticleType<ParticleData> TRIAL_OMEN = define("trial_omen");

    /**
     * Returns an immutable view of the particle types.
     *
     * @return Particle Types
     */
    public static Collection<ParticleType<?>> values() {
        return Collections.unmodifiableCollection(PARTICLE_TYPE_MAP.values());
    }

    static {
        TYPES_BUILDER.unloadFileMappings();
    }

    @ApiStatus.Internal
    @FunctionalInterface
    public interface Decoder<T> {
        T decode(NBTCompound compound, ClientVersion version);
    }

    @ApiStatus.Internal
    @FunctionalInterface
    public interface Encoder<T> {
        void encode(T value, ClientVersion version, NBTCompound compound);
    }
}
