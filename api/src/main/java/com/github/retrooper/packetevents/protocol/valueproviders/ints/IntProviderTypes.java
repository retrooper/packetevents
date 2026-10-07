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

package com.github.retrooper.packetevents.protocol.valueproviders.ints;

import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.17+
 */
@NullMarked
public final class IntProviderTypes {

    private static final VersionedRegistry<IntProviderType<?>> REGISTRY = new VersionedRegistry<>("int_provider_type");

    private IntProviderTypes() {
    }

    @ApiStatus.Internal
    public static <T extends IntProvider> IntProviderType<T> define(String name, NbtMapCodec<T> codec) {
        return REGISTRY.define(name, data -> new StaticIntProviderType<>(data, codec));
    }

    public static final IntProviderType<ConstantInt> CONSTANT = define("constant", ConstantInt.MAP_CODEC);
    public static final IntProviderType<UniformInt> UNIFORM = define("uniform", UniformInt.MAP_CODEC);
    public static final IntProviderType<BiasedToBottomInt> BIASED_TO_BOTTOM = define("biased_to_bottom", BiasedToBottomInt.MAP_CODEC);
    public static final IntProviderType<VeryBiasedToBottomInt> VERY_BIASED_TO_BOTTOM = define("very_biased_to_bottom", VeryBiasedToBottomInt.MAP_CODEC);
    public static final IntProviderType<ClampedInt> CLAMPED = define("clamped", ClampedInt.MAP_CODEC);
    public static final IntProviderType<WeightedListInt> WEIGHTED_LIST = define("weighted_list", WeightedListInt.MAP_CODEC);
    public static final IntProviderType<ClampedNormalInt> CLAMPED_NORMAL = define("clamped_normal", ClampedNormalInt.MAP_CODEC);
    public static final IntProviderType<TrapezoidInt> TRAPEZOID = define("trapezoid", TrapezoidInt.MAP_CODEC);

    public static VersionedRegistry<IntProviderType<?>> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}
