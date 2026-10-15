/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.generation;

import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.generation.provider.CopyPropertiesProvider;
import com.github.retrooper.packetevents.protocol.world.generation.provider.DualNoiseProvider;
import com.github.retrooper.packetevents.protocol.world.generation.provider.NoiseProvider;
import com.github.retrooper.packetevents.protocol.world.generation.provider.NoiseThresholdProvider;
import com.github.retrooper.packetevents.protocol.world.generation.provider.RandomBlockProvider;
import com.github.retrooper.packetevents.protocol.world.generation.provider.RandomizedIntStateProvider;
import com.github.retrooper.packetevents.protocol.world.generation.provider.RotatedBlockProvider;
import com.github.retrooper.packetevents.protocol.world.generation.provider.RuleBasedStateProvider;
import com.github.retrooper.packetevents.protocol.world.generation.provider.SimpleStateProvider;
import com.github.retrooper.packetevents.protocol.world.generation.provider.WeightedStateProvider;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 26.3+
 */
@NullMarked
public final class BlockStateProviderTypes {

    private static final VersionedRegistry<BlockStateProviderType<?>> REGISTRY = new VersionedRegistry<>("worldgen/block_state_provider_type");

    private BlockStateProviderTypes() {
    }

    @ApiStatus.Internal
    public static <T extends BlockStateProvider> BlockStateProviderType<T> define(String name, NbtMapCodec<T> codec) {
        return REGISTRY.define(name, data -> new StaticBlockStateProviderType<>(data, codec));
    }

    public static final BlockStateProviderType<CopyPropertiesProvider> COPY_PROPERTIES = define("copy_properties", CopyPropertiesProvider.MAP_CODEC);
    public static final BlockStateProviderType<DualNoiseProvider> DUAL_NOISE = define("dual_noise", DualNoiseProvider.MAP_CODEC);
    public static final BlockStateProviderType<NoiseProvider> NOISE = define("noise", NoiseProvider.MAP_CODEC);
    public static final BlockStateProviderType<NoiseThresholdProvider> NOISE_THRESHOLD = define("noise_threshold", NoiseThresholdProvider.MAP_CODEC);
    public static final BlockStateProviderType<RandomBlockProvider> RANDOM_BLOCK = define("random_block", RandomBlockProvider.MAP_CODEC);
    public static final BlockStateProviderType<RandomizedIntStateProvider> RANDOMIZED_INT = define("randomized_int", RandomizedIntStateProvider.MAP_CODEC);
    public static final BlockStateProviderType<RotatedBlockProvider> ROTATED = define("rotated", RotatedBlockProvider.MAP_CODEC);
    public static final BlockStateProviderType<RuleBasedStateProvider> RULE_BASED = define("rule_based", RuleBasedStateProvider.MAP_CODEC);
    public static final BlockStateProviderType<SimpleStateProvider> SIMPLE = define("simple", SimpleStateProvider.MAP_CODEC);
    public static final BlockStateProviderType<WeightedStateProvider> WEIGHTED = define("weighted", WeightedStateProvider.MAP_CODEC);

    public static VersionedRegistry<BlockStateProviderType<?>> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}
