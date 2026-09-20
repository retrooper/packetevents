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

import com.github.retrooper.packetevents.protocol.world.generation.predicate.AllOfPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.predicate.AnyOfPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.predicate.HasSturdyFacePredicate;
import com.github.retrooper.packetevents.protocol.world.generation.predicate.HeightRangePredicate;
import com.github.retrooper.packetevents.protocol.world.generation.predicate.InsideWorldBoundsPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.predicate.MatchingBiomesPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.predicate.MatchingBlockTagPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.predicate.MatchingBlocksPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.predicate.MatchingFluidsPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.predicate.NotPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.predicate.ReplaceablePredicate;
import com.github.retrooper.packetevents.protocol.world.generation.predicate.SolidPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.predicate.TrueBlockPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.predicate.UnobstructedPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.predicate.VolumeMatchPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.predicate.WouldSurvivePredicate;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 26.3+
 */
@NullMarked
public final class BlockPredicateTypes {

    private static final VersionedRegistry<BlockPredicateType<?>> REGISTRY = new VersionedRegistry<>("block_predicate_type");

    private BlockPredicateTypes() {
    }

    @ApiStatus.Internal
    public static <T extends BlockPredicate> BlockPredicateType<T> define(String name, NbtMapCodec<T> codec) {
        return REGISTRY.define(name, data -> new StaticBlockPredicateType<>(data, codec));
    }

    public static final BlockPredicateType<MatchingBlocksPredicate> MATCHING_BLOCKS = define("matching_blocks", MatchingBlocksPredicate.MAP_CODEC);
    public static final BlockPredicateType<MatchingBlockTagPredicate> MATCHING_BLOCK_TAG = define("matching_block_tag", MatchingBlockTagPredicate.MAP_CODEC);
    public static final BlockPredicateType<MatchingFluidsPredicate> MATCHING_FLUIDS = define("matching_fluids", MatchingFluidsPredicate.MAP_CODEC);
    public static final BlockPredicateType<MatchingBiomesPredicate> MATCHING_BIOMES = define("matching_biomes", MatchingBiomesPredicate.MAP_CODEC);
    public static final BlockPredicateType<HasSturdyFacePredicate> HAS_STURDY_FACE = define("has_sturdy_face", HasSturdyFacePredicate.MAP_CODEC);
    public static final BlockPredicateType<SolidPredicate> SOLID = define("solid", SolidPredicate.MAP_CODEC);
    public static final BlockPredicateType<ReplaceablePredicate> REPLACEABLE = define("replaceable", ReplaceablePredicate.MAP_CODEC);
    public static final BlockPredicateType<WouldSurvivePredicate> WOULD_SURVIVE = define("would_survive", WouldSurvivePredicate.MAP_CODEC);
    public static final BlockPredicateType<InsideWorldBoundsPredicate> INSIDE_WORLD_BOUNDS = define("inside_world_bounds", InsideWorldBoundsPredicate.MAP_CODEC);
    public static final BlockPredicateType<AnyOfPredicate> ANY_OF = define("any_of", AnyOfPredicate.MAP_CODEC);
    public static final BlockPredicateType<AllOfPredicate> ALL_OF = define("all_of", AllOfPredicate.MAP_CODEC);
    public static final BlockPredicateType<NotPredicate> NOT = define("not", NotPredicate.MAP_CODEC);
    public static final BlockPredicateType<TrueBlockPredicate> TRUE = define("true", TrueBlockPredicate.MAP_CODEC);
    public static final BlockPredicateType<UnobstructedPredicate> UNOBSTRUCTED = define("unobstructed", UnobstructedPredicate.MAP_CODEC);
    public static final BlockPredicateType<HeightRangePredicate> HEIGHT_RANGE = define("height_range", HeightRangePredicate.MAP_CODEC);
    public static final BlockPredicateType<VolumeMatchPredicate> VOLUME_MATCH = define("volume_match", VolumeMatchPredicate.MAP_CODEC);

    public static VersionedRegistry<BlockPredicateType<?>> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}
