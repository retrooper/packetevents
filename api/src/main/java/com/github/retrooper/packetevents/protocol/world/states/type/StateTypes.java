/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.states.type;

import com.github.retrooper.packetevents.protocol.world.MaterialType;
import com.github.retrooper.packetevents.protocol.world.PushReaction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StateTypes {
    private static final Map<String, StateType> BY_NAME = new HashMap<>();

    public static StateType AIR = StateTypes.builder().name("AIR").isAir(true).setMaterial(MaterialType.AIR).build();
    public static StateType STONE = StateTypes.builder().name("STONE").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType GRANITE = StateTypes.builder().name("GRANITE").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_GRANITE = StateTypes.builder().name("POLISHED_GRANITE").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DIORITE = StateTypes.builder().name("DIORITE").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_DIORITE = StateTypes.builder().name("POLISHED_DIORITE").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType ANDESITE = StateTypes.builder().name("ANDESITE").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_ANDESITE = StateTypes.builder().name("POLISHED_ANDESITE").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE = StateTypes.builder().name("DEEPSLATE").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType COBBLED_DEEPSLATE = StateTypes.builder().name("COBBLED_DEEPSLATE").blastResistance(6.0f).hardness(3.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_DEEPSLATE = StateTypes.builder().name("POLISHED_DEEPSLATE").blastResistance(6.0f).hardness(3.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CALCITE = StateTypes.builder().name("CALCITE").blastResistance(0.75f).hardness(0.75f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType TUFF = StateTypes.builder().name("TUFF").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DRIPSTONE_BLOCK = StateTypes.builder().name("DRIPSTONE_BLOCK").blastResistance(1.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType GRASS_BLOCK = StateTypes.builder().name("GRASS_BLOCK").blastResistance(0.6f).hardness(0.6f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.GRASS).isSolid(true).build();
    public static StateType DIRT = StateTypes.builder().name("DIRT").blastResistance(0.5f).hardness(0.5f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.DIRT).isSolid(true).build();
    public static StateType COARSE_DIRT = StateTypes.builder().name("COARSE_DIRT").blastResistance(0.5f).hardness(0.5f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.DIRT).isSolid(true).build();
    public static StateType PODZOL = StateTypes.builder().name("PODZOL").blastResistance(0.5f).hardness(0.5f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.DIRT).isSolid(true).build();
    public static StateType ROOTED_DIRT = StateTypes.builder().name("ROOTED_DIRT").blastResistance(0.5f).hardness(0.5f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.DIRT).isSolid(true).build();
    public static StateType CRIMSON_NYLIUM = StateTypes.builder().name("CRIMSON_NYLIUM").blastResistance(0.4f).hardness(0.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType WARPED_NYLIUM = StateTypes.builder().name("WARPED_NYLIUM").blastResistance(0.4f).hardness(0.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType COBBLESTONE = StateTypes.builder().name("COBBLESTONE").blastResistance(6.0f).hardness(2.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType OAK_PLANKS = StateTypes.builder().name("OAK_PLANKS").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType SPRUCE_PLANKS = StateTypes.builder().name("SPRUCE_PLANKS").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType BIRCH_PLANKS = StateTypes.builder().name("BIRCH_PLANKS").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType JUNGLE_PLANKS = StateTypes.builder().name("JUNGLE_PLANKS").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType ACACIA_PLANKS = StateTypes.builder().name("ACACIA_PLANKS").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType DARK_OAK_PLANKS = StateTypes.builder().name("DARK_OAK_PLANKS").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType CRIMSON_PLANKS = StateTypes.builder().name("CRIMSON_PLANKS").blastResistance(3.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType WARPED_PLANKS = StateTypes.builder().name("WARPED_PLANKS").blastResistance(3.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType OAK_SAPLING = StateTypes.builder().name("OAK_SAPLING").setMaterial(MaterialType.PLANT).build();
    public static StateType SPRUCE_SAPLING = StateTypes.builder().name("SPRUCE_SAPLING").setMaterial(MaterialType.PLANT).build();
    public static StateType BIRCH_SAPLING = StateTypes.builder().name("BIRCH_SAPLING").setMaterial(MaterialType.PLANT).build();
    public static StateType JUNGLE_SAPLING = StateTypes.builder().name("JUNGLE_SAPLING").setMaterial(MaterialType.PLANT).build();
    public static StateType ACACIA_SAPLING = StateTypes.builder().name("ACACIA_SAPLING").setMaterial(MaterialType.PLANT).build();
    public static StateType DARK_OAK_SAPLING = StateTypes.builder().name("DARK_OAK_SAPLING").setMaterial(MaterialType.PLANT).build();
    public static StateType BEDROCK = StateTypes.builder().name("BEDROCK").blastResistance(3600000.0f).hardness(-1.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SAND = StateTypes.builder().name("SAND").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType RED_SAND = StateTypes.builder().name("RED_SAND").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType GRAVEL = StateTypes.builder().name("GRAVEL").blastResistance(0.6f).hardness(0.6f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType COAL_ORE = StateTypes.builder().name("COAL_ORE").blastResistance(3.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_COAL_ORE = StateTypes.builder().name("DEEPSLATE_COAL_ORE").blastResistance(3.0f).hardness(4.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType IRON_ORE = StateTypes.builder().name("IRON_ORE").blastResistance(3.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_IRON_ORE = StateTypes.builder().name("DEEPSLATE_IRON_ORE").blastResistance(3.0f).hardness(4.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType COPPER_ORE = StateTypes.builder().name("COPPER_ORE").blastResistance(3.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_COPPER_ORE = StateTypes.builder().name("DEEPSLATE_COPPER_ORE").blastResistance(3.0f).hardness(4.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType GOLD_ORE = StateTypes.builder().name("GOLD_ORE").blastResistance(3.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_GOLD_ORE = StateTypes.builder().name("DEEPSLATE_GOLD_ORE").blastResistance(3.0f).hardness(4.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType REDSTONE_ORE = StateTypes.builder().name("REDSTONE_ORE").blastResistance(3.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_REDSTONE_ORE = StateTypes.builder().name("DEEPSLATE_REDSTONE_ORE").blastResistance(3.0f).hardness(4.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType EMERALD_ORE = StateTypes.builder().name("EMERALD_ORE").blastResistance(3.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_EMERALD_ORE = StateTypes.builder().name("DEEPSLATE_EMERALD_ORE").blastResistance(3.0f).hardness(4.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType LAPIS_ORE = StateTypes.builder().name("LAPIS_ORE").blastResistance(3.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_LAPIS_ORE = StateTypes.builder().name("DEEPSLATE_LAPIS_ORE").blastResistance(3.0f).hardness(4.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DIAMOND_ORE = StateTypes.builder().name("DIAMOND_ORE").blastResistance(3.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_DIAMOND_ORE = StateTypes.builder().name("DEEPSLATE_DIAMOND_ORE").blastResistance(3.0f).hardness(4.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType NETHER_GOLD_ORE = StateTypes.builder().name("NETHER_GOLD_ORE").blastResistance(3.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType NETHER_QUARTZ_ORE = StateTypes.builder().name("NETHER_QUARTZ_ORE").blastResistance(3.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType ANCIENT_DEBRIS = StateTypes.builder().name("ANCIENT_DEBRIS").blastResistance(1200.0f).hardness(30.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType COAL_BLOCK = StateTypes.builder().name("COAL_BLOCK").blastResistance(6.0f).hardness(5.0f).isBurnable(true).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType RAW_IRON_BLOCK = StateTypes.builder().name("RAW_IRON_BLOCK").blastResistance(6.0f).hardness(5.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType RAW_COPPER_BLOCK = StateTypes.builder().name("RAW_COPPER_BLOCK").blastResistance(6.0f).hardness(5.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType RAW_GOLD_BLOCK = StateTypes.builder().name("RAW_GOLD_BLOCK").blastResistance(6.0f).hardness(5.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType AMETHYST_BLOCK = StateTypes.builder().name("AMETHYST_BLOCK").blastResistance(1.5f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.AMETHYST).isSolid(true).build();
    public static StateType BUDDING_AMETHYST = StateTypes.builder().name("BUDDING_AMETHYST").blastResistance(1.5f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.AMETHYST).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType IRON_BLOCK = StateTypes.builder().name("IRON_BLOCK").blastResistance(6.0f).hardness(5.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType COPPER_BLOCK = StateTypes.builder().name("COPPER_BLOCK").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType GOLD_BLOCK = StateTypes.builder().name("GOLD_BLOCK").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType DIAMOND_BLOCK = StateTypes.builder().name("DIAMOND_BLOCK").blastResistance(6.0f).hardness(5.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType NETHERITE_BLOCK = StateTypes.builder().name("NETHERITE_BLOCK").blastResistance(1200.0f).hardness(50.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType EXPOSED_COPPER = StateTypes.builder().name("EXPOSED_COPPER").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WEATHERED_COPPER = StateTypes.builder().name("WEATHERED_COPPER").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType OXIDIZED_COPPER = StateTypes.builder().name("OXIDIZED_COPPER").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType CUT_COPPER = StateTypes.builder().name("CUT_COPPER").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType EXPOSED_CUT_COPPER = StateTypes.builder().name("EXPOSED_CUT_COPPER").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WEATHERED_CUT_COPPER = StateTypes.builder().name("WEATHERED_CUT_COPPER").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType OXIDIZED_CUT_COPPER = StateTypes.builder().name("OXIDIZED_CUT_COPPER").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType CUT_COPPER_STAIRS = StateTypes.builder().name("CUT_COPPER_STAIRS").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType EXPOSED_CUT_COPPER_STAIRS = StateTypes.builder().name("EXPOSED_CUT_COPPER_STAIRS").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WEATHERED_CUT_COPPER_STAIRS = StateTypes.builder().name("WEATHERED_CUT_COPPER_STAIRS").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType OXIDIZED_CUT_COPPER_STAIRS = StateTypes.builder().name("OXIDIZED_CUT_COPPER_STAIRS").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType CUT_COPPER_SLAB = StateTypes.builder().name("CUT_COPPER_SLAB").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType EXPOSED_CUT_COPPER_SLAB = StateTypes.builder().name("EXPOSED_CUT_COPPER_SLAB").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WEATHERED_CUT_COPPER_SLAB = StateTypes.builder().name("WEATHERED_CUT_COPPER_SLAB").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType OXIDIZED_CUT_COPPER_SLAB = StateTypes.builder().name("OXIDIZED_CUT_COPPER_SLAB").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_COPPER_BLOCK = StateTypes.builder().name("WAXED_COPPER_BLOCK").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_EXPOSED_COPPER = StateTypes.builder().name("WAXED_EXPOSED_COPPER").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_WEATHERED_COPPER = StateTypes.builder().name("WAXED_WEATHERED_COPPER").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_OXIDIZED_COPPER = StateTypes.builder().name("WAXED_OXIDIZED_COPPER").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_CUT_COPPER = StateTypes.builder().name("WAXED_CUT_COPPER").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_EXPOSED_CUT_COPPER = StateTypes.builder().name("WAXED_EXPOSED_CUT_COPPER").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_WEATHERED_CUT_COPPER = StateTypes.builder().name("WAXED_WEATHERED_CUT_COPPER").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_OXIDIZED_CUT_COPPER = StateTypes.builder().name("WAXED_OXIDIZED_CUT_COPPER").blastResistance(6.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_CUT_COPPER_STAIRS = StateTypes.builder().name("WAXED_CUT_COPPER_STAIRS").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_EXPOSED_CUT_COPPER_STAIRS = StateTypes.builder().name("WAXED_EXPOSED_CUT_COPPER_STAIRS").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_WEATHERED_CUT_COPPER_STAIRS = StateTypes.builder().name("WAXED_WEATHERED_CUT_COPPER_STAIRS").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_OXIDIZED_CUT_COPPER_STAIRS = StateTypes.builder().name("WAXED_OXIDIZED_CUT_COPPER_STAIRS").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_CUT_COPPER_SLAB = StateTypes.builder().name("WAXED_CUT_COPPER_SLAB").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_EXPOSED_CUT_COPPER_SLAB = StateTypes.builder().name("WAXED_EXPOSED_CUT_COPPER_SLAB").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_WEATHERED_CUT_COPPER_SLAB = StateTypes.builder().name("WAXED_WEATHERED_CUT_COPPER_SLAB").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WAXED_OXIDIZED_CUT_COPPER_SLAB = StateTypes.builder().name("WAXED_OXIDIZED_CUT_COPPER_SLAB").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType OAK_LOG = StateTypes.builder().name("OAK_LOG").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType SPRUCE_LOG = StateTypes.builder().name("SPRUCE_LOG").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType BIRCH_LOG = StateTypes.builder().name("BIRCH_LOG").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType JUNGLE_LOG = StateTypes.builder().name("JUNGLE_LOG").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType ACACIA_LOG = StateTypes.builder().name("ACACIA_LOG").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType DARK_OAK_LOG = StateTypes.builder().name("DARK_OAK_LOG").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType CRIMSON_STEM = StateTypes.builder().name("CRIMSON_STEM").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType WARPED_STEM = StateTypes.builder().name("WARPED_STEM").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType STRIPPED_OAK_LOG = StateTypes.builder().name("STRIPPED_OAK_LOG").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType STRIPPED_SPRUCE_LOG = StateTypes.builder().name("STRIPPED_SPRUCE_LOG").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType STRIPPED_BIRCH_LOG = StateTypes.builder().name("STRIPPED_BIRCH_LOG").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType STRIPPED_JUNGLE_LOG = StateTypes.builder().name("STRIPPED_JUNGLE_LOG").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType STRIPPED_ACACIA_LOG = StateTypes.builder().name("STRIPPED_ACACIA_LOG").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType STRIPPED_DARK_OAK_LOG = StateTypes.builder().name("STRIPPED_DARK_OAK_LOG").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType STRIPPED_CRIMSON_STEM = StateTypes.builder().name("STRIPPED_CRIMSON_STEM").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType STRIPPED_WARPED_STEM = StateTypes.builder().name("STRIPPED_WARPED_STEM").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType STRIPPED_OAK_WOOD = StateTypes.builder().name("STRIPPED_OAK_WOOD").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType STRIPPED_SPRUCE_WOOD = StateTypes.builder().name("STRIPPED_SPRUCE_WOOD").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType STRIPPED_BIRCH_WOOD = StateTypes.builder().name("STRIPPED_BIRCH_WOOD").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType STRIPPED_JUNGLE_WOOD = StateTypes.builder().name("STRIPPED_JUNGLE_WOOD").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType STRIPPED_ACACIA_WOOD = StateTypes.builder().name("STRIPPED_ACACIA_WOOD").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType STRIPPED_DARK_OAK_WOOD = StateTypes.builder().name("STRIPPED_DARK_OAK_WOOD").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType STRIPPED_CRIMSON_HYPHAE = StateTypes.builder().name("STRIPPED_CRIMSON_HYPHAE").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType STRIPPED_WARPED_HYPHAE = StateTypes.builder().name("STRIPPED_WARPED_HYPHAE").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType OAK_WOOD = StateTypes.builder().name("OAK_WOOD").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType SPRUCE_WOOD = StateTypes.builder().name("SPRUCE_WOOD").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType BIRCH_WOOD = StateTypes.builder().name("BIRCH_WOOD").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType JUNGLE_WOOD = StateTypes.builder().name("JUNGLE_WOOD").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType ACACIA_WOOD = StateTypes.builder().name("ACACIA_WOOD").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType DARK_OAK_WOOD = StateTypes.builder().name("DARK_OAK_WOOD").blastResistance(2.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType CRIMSON_HYPHAE = StateTypes.builder().name("CRIMSON_HYPHAE").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType WARPED_HYPHAE = StateTypes.builder().name("WARPED_HYPHAE").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType OAK_LEAVES = StateTypes.builder().name("OAK_LEAVES").blastResistance(0.2f).hardness(0.2f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.LEAVES).isSolid(true).build();
    public static StateType SPRUCE_LEAVES = StateTypes.builder().name("SPRUCE_LEAVES").blastResistance(0.2f).hardness(0.2f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.LEAVES).isSolid(true).build();
    public static StateType BIRCH_LEAVES = StateTypes.builder().name("BIRCH_LEAVES").blastResistance(0.2f).hardness(0.2f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.LEAVES).isSolid(true).build();
    public static StateType JUNGLE_LEAVES = StateTypes.builder().name("JUNGLE_LEAVES").blastResistance(0.2f).hardness(0.2f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.LEAVES).isSolid(true).build();
    public static StateType ACACIA_LEAVES = StateTypes.builder().name("ACACIA_LEAVES").blastResistance(0.2f).hardness(0.2f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.LEAVES).isSolid(true).build();
    public static StateType DARK_OAK_LEAVES = StateTypes.builder().name("DARK_OAK_LEAVES").blastResistance(0.2f).hardness(0.2f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.LEAVES).isSolid(true).build();
    public static StateType AZALEA_LEAVES = StateTypes.builder().name("AZALEA_LEAVES").blastResistance(0.2f).hardness(0.2f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.LEAVES).isSolid(true).build();
    public static StateType FLOWERING_AZALEA_LEAVES = StateTypes.builder().name("FLOWERING_AZALEA_LEAVES").blastResistance(0.2f).hardness(0.2f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.LEAVES).isSolid(true).build();
    public static StateType SPONGE = StateTypes.builder().name("SPONGE").blastResistance(0.6f).hardness(0.6f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SPONGE).isSolid(true).build();
    public static StateType WET_SPONGE = StateTypes.builder().name("WET_SPONGE").blastResistance(0.6f).hardness(0.6f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SPONGE).isSolid(true).build();
    public static StateType GLASS = StateTypes.builder().name("GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType TINTED_GLASS = StateTypes.builder().name("TINTED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType LAPIS_BLOCK = StateTypes.builder().name("LAPIS_BLOCK").blastResistance(3.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType SANDSTONE = StateTypes.builder().name("SANDSTONE").blastResistance(0.8f).hardness(0.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CHISELED_SANDSTONE = StateTypes.builder().name("CHISELED_SANDSTONE").blastResistance(0.8f).hardness(0.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CUT_SANDSTONE = StateTypes.builder().name("CUT_SANDSTONE").blastResistance(0.8f).hardness(0.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType COBWEB = StateTypes.builder().name("COBWEB").blastResistance(4.0f).hardness(4.0f).requiresCorrectTool(true).setMaterial(MaterialType.WEB).build();
    public static StateType GRASS = StateTypes.builder().name("GRASS").isBurnable(true).isFlammable(true).setMaterial(MaterialType.REPLACEABLE_PLANT).build();
    public static StateType FERN = StateTypes.builder().name("FERN").isBurnable(true).isFlammable(true).setMaterial(MaterialType.REPLACEABLE_PLANT).build();
    public static StateType AZALEA = StateTypes.builder().name("AZALEA").isBurnable(true).setMaterial(MaterialType.PLANT).isSolid(true).build();
    public static StateType FLOWERING_AZALEA = StateTypes.builder().name("FLOWERING_AZALEA").isBurnable(true).setMaterial(MaterialType.PLANT).isSolid(true).build();
    public static StateType DEAD_BUSH = StateTypes.builder().name("DEAD_BUSH").isBurnable(true).isFlammable(true).setMaterial(MaterialType.REPLACEABLE_PLANT).build();
    public static StateType SEAGRASS = StateTypes.builder().name("SEAGRASS").setMaterial(MaterialType.REPLACEABLE_WATER_PLANT).build();
    public static StateType SEA_PICKLE = StateTypes.builder().name("SEA_PICKLE").setMaterial(MaterialType.WATER_PLANT).isSolid(true).build();
    public static StateType WHITE_WOOL = StateTypes.builder().name("WHITE_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType ORANGE_WOOL = StateTypes.builder().name("ORANGE_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType MAGENTA_WOOL = StateTypes.builder().name("MAGENTA_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType LIGHT_BLUE_WOOL = StateTypes.builder().name("LIGHT_BLUE_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType YELLOW_WOOL = StateTypes.builder().name("YELLOW_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType LIME_WOOL = StateTypes.builder().name("LIME_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType PINK_WOOL = StateTypes.builder().name("PINK_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType GRAY_WOOL = StateTypes.builder().name("GRAY_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType LIGHT_GRAY_WOOL = StateTypes.builder().name("LIGHT_GRAY_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType CYAN_WOOL = StateTypes.builder().name("CYAN_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType PURPLE_WOOL = StateTypes.builder().name("PURPLE_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType BLUE_WOOL = StateTypes.builder().name("BLUE_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType BROWN_WOOL = StateTypes.builder().name("BROWN_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType GREEN_WOOL = StateTypes.builder().name("GREEN_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType RED_WOOL = StateTypes.builder().name("RED_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType BLACK_WOOL = StateTypes.builder().name("BLACK_WOOL").blastResistance(0.8f).hardness(0.8f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOL).isSolid(true).build();
    public static StateType DANDELION = StateTypes.builder().name("DANDELION").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType POPPY = StateTypes.builder().name("POPPY").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType BLUE_ORCHID = StateTypes.builder().name("BLUE_ORCHID").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType ALLIUM = StateTypes.builder().name("ALLIUM").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType AZURE_BLUET = StateTypes.builder().name("AZURE_BLUET").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType RED_TULIP = StateTypes.builder().name("RED_TULIP").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType ORANGE_TULIP = StateTypes.builder().name("ORANGE_TULIP").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType WHITE_TULIP = StateTypes.builder().name("WHITE_TULIP").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType PINK_TULIP = StateTypes.builder().name("PINK_TULIP").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType OXEYE_DAISY = StateTypes.builder().name("OXEYE_DAISY").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType CORNFLOWER = StateTypes.builder().name("CORNFLOWER").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType LILY_OF_THE_VALLEY = StateTypes.builder().name("LILY_OF_THE_VALLEY").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType WITHER_ROSE = StateTypes.builder().name("WITHER_ROSE").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType SPORE_BLOSSOM = StateTypes.builder().name("SPORE_BLOSSOM").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType BROWN_MUSHROOM = StateTypes.builder().name("BROWN_MUSHROOM").setMaterial(MaterialType.PLANT).build();
    public static StateType RED_MUSHROOM = StateTypes.builder().name("RED_MUSHROOM").setMaterial(MaterialType.PLANT).build();
    public static StateType CRIMSON_FUNGUS = StateTypes.builder().name("CRIMSON_FUNGUS").setMaterial(MaterialType.PLANT).build();
    public static StateType WARPED_FUNGUS = StateTypes.builder().name("WARPED_FUNGUS").setMaterial(MaterialType.PLANT).build();
    public static StateType CRIMSON_ROOTS = StateTypes.builder().name("CRIMSON_ROOTS").setMaterial(MaterialType.REPLACEABLE_FIREPROOF_PLANT).build();
    public static StateType WARPED_ROOTS = StateTypes.builder().name("WARPED_ROOTS").setMaterial(MaterialType.REPLACEABLE_FIREPROOF_PLANT).build();
    public static StateType NETHER_SPROUTS = StateTypes.builder().name("NETHER_SPROUTS").setMaterial(MaterialType.REPLACEABLE_FIREPROOF_PLANT).build();
    public static StateType WEEPING_VINES = StateTypes.builder().name("WEEPING_VINES").setMaterial(MaterialType.PLANT).build();
    public static StateType TWISTING_VINES = StateTypes.builder().name("TWISTING_VINES").setMaterial(MaterialType.PLANT).build();
    public static StateType SUGAR_CANE = StateTypes.builder().name("SUGAR_CANE").setMaterial(MaterialType.PLANT).build();
    public static StateType KELP = StateTypes.builder().name("KELP").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType MOSS_CARPET = StateTypes.builder().name("MOSS_CARPET").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.PLANT).isSolid(true).build();
    public static StateType MOSS_BLOCK = StateTypes.builder().name("MOSS_BLOCK").blastResistance(0.1f).hardness(0.1f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.MOSS).isSolid(true).build();
    public static StateType HANGING_ROOTS = StateTypes.builder().name("HANGING_ROOTS").isBurnable(true).isFlammable(true).setMaterial(MaterialType.REPLACEABLE_PLANT).build();
    public static StateType BIG_DRIPLEAF = StateTypes.builder().name("BIG_DRIPLEAF").blastResistance(0.1f).hardness(0.1f).isBurnable(true).setMaterial(MaterialType.PLANT).isSolid(true).build();
    public static StateType SMALL_DRIPLEAF = StateTypes.builder().name("SMALL_DRIPLEAF").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType BAMBOO = StateTypes.builder().name("BAMBOO").blastResistance(1.0f).hardness(1.0f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.BAMBOO).isSolid(true).build();
    public static StateType OAK_SLAB = StateTypes.builder().name("OAK_SLAB").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType SPRUCE_SLAB = StateTypes.builder().name("SPRUCE_SLAB").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType BIRCH_SLAB = StateTypes.builder().name("BIRCH_SLAB").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType JUNGLE_SLAB = StateTypes.builder().name("JUNGLE_SLAB").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType ACACIA_SLAB = StateTypes.builder().name("ACACIA_SLAB").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType DARK_OAK_SLAB = StateTypes.builder().name("DARK_OAK_SLAB").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType CRIMSON_SLAB = StateTypes.builder().name("CRIMSON_SLAB").blastResistance(3.0f).hardness(2.0f).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType WARPED_SLAB = StateTypes.builder().name("WARPED_SLAB").blastResistance(3.0f).hardness(2.0f).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType STONE_SLAB = StateTypes.builder().name("STONE_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SMOOTH_STONE_SLAB = StateTypes.builder().name("SMOOTH_STONE_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SANDSTONE_SLAB = StateTypes.builder().name("SANDSTONE_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CUT_SANDSTONE_SLAB = StateTypes.builder().name("CUT_SANDSTONE_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType PETRIFIED_OAK_SLAB = StateTypes.builder().name("PETRIFIED_OAK_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType COBBLESTONE_SLAB = StateTypes.builder().name("COBBLESTONE_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BRICK_SLAB = StateTypes.builder().name("BRICK_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType STONE_BRICK_SLAB = StateTypes.builder().name("STONE_BRICK_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType NETHER_BRICK_SLAB = StateTypes.builder().name("NETHER_BRICK_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType QUARTZ_SLAB = StateTypes.builder().name("QUARTZ_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType RED_SANDSTONE_SLAB = StateTypes.builder().name("RED_SANDSTONE_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CUT_RED_SANDSTONE_SLAB = StateTypes.builder().name("CUT_RED_SANDSTONE_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType PURPUR_SLAB = StateTypes.builder().name("PURPUR_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType PRISMARINE_SLAB = StateTypes.builder().name("PRISMARINE_SLAB").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType PRISMARINE_BRICK_SLAB = StateTypes.builder().name("PRISMARINE_BRICK_SLAB").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DARK_PRISMARINE_SLAB = StateTypes.builder().name("DARK_PRISMARINE_SLAB").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SMOOTH_QUARTZ = StateTypes.builder().name("SMOOTH_QUARTZ").blastResistance(6.0f).hardness(2.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SMOOTH_RED_SANDSTONE = StateTypes.builder().name("SMOOTH_RED_SANDSTONE").blastResistance(6.0f).hardness(2.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SMOOTH_SANDSTONE = StateTypes.builder().name("SMOOTH_SANDSTONE").blastResistance(6.0f).hardness(2.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SMOOTH_STONE = StateTypes.builder().name("SMOOTH_STONE").blastResistance(6.0f).hardness(2.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BRICKS = StateTypes.builder().name("BRICKS").blastResistance(6.0f).hardness(2.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BOOKSHELF = StateTypes.builder().name("BOOKSHELF").blastResistance(1.5f).hardness(1.5f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType MOSSY_COBBLESTONE = StateTypes.builder().name("MOSSY_COBBLESTONE").blastResistance(6.0f).hardness(2.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType OBSIDIAN = StateTypes.builder().name("OBSIDIAN").blastResistance(1200.0f).hardness(50.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType TORCH = StateTypes.builder().name("TORCH").setMaterial(MaterialType.DECORATION).build();
    public static StateType END_ROD = StateTypes.builder().name("END_ROD").setMaterial(MaterialType.DECORATION).setPushReaction(PushReaction.NORMAL).isSolid(true).build();
    public static StateType CHORUS_PLANT = StateTypes.builder().name("CHORUS_PLANT").blastResistance(0.4f).hardness(0.4f).setMaterial(MaterialType.PLANT).isSolid(true).build();
    public static StateType CHORUS_FLOWER = StateTypes.builder().name("CHORUS_FLOWER").blastResistance(0.4f).hardness(0.4f).setMaterial(MaterialType.PLANT).isSolid(true).build();
    public static StateType PURPUR_BLOCK = StateTypes.builder().name("PURPUR_BLOCK").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType PURPUR_PILLAR = StateTypes.builder().name("PURPUR_PILLAR").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType PURPUR_STAIRS = StateTypes.builder().name("PURPUR_STAIRS").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SPAWNER = StateTypes.builder().name("SPAWNER").blastResistance(5.0f).hardness(5.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType OAK_STAIRS = StateTypes.builder().name("OAK_STAIRS").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType CHEST = StateTypes.builder().name("CHEST").blastResistance(2.5f).hardness(2.5f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType CRAFTING_TABLE = StateTypes.builder().name("CRAFTING_TABLE").blastResistance(2.5f).hardness(2.5f).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType FARMLAND = StateTypes.builder().name("FARMLAND").blastResistance(0.6f).hardness(0.6f).isBlocking(true).setMaterial(MaterialType.DIRT).isSolid(true).build();
    public static StateType FURNACE = StateTypes.builder().name("FURNACE").blastResistance(3.5f).hardness(3.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType LADDER = StateTypes.builder().name("LADDER").blastResistance(0.4f).hardness(0.4f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType COBBLESTONE_STAIRS = StateTypes.builder().name("COBBLESTONE_STAIRS").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SNOW = StateTypes.builder().name("SNOW").blastResistance(0.1f).hardness(0.1f).requiresCorrectTool(true).setMaterial(MaterialType.TOP_SNOW).isSolid(true).build();
    public static StateType ICE = StateTypes.builder().name("ICE").blastResistance(0.5f).hardness(0.5f).slipperiness(0.98f).isBlocking(true).setMaterial(MaterialType.ICE).isSolid(true).build();
    public static StateType SNOW_BLOCK = StateTypes.builder().name("SNOW_BLOCK").blastResistance(0.2f).hardness(0.2f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.SNOW).isSolid(true).build();
    public static StateType CACTUS = StateTypes.builder().name("CACTUS").blastResistance(0.4f).hardness(0.4f).isBlocking(true).setMaterial(MaterialType.CACTUS).isSolid(true).build();
    public static StateType CLAY = StateTypes.builder().name("CLAY").blastResistance(0.6f).hardness(0.6f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.CLAY).isSolid(true).build();
    public static StateType JUKEBOX = StateTypes.builder().name("JUKEBOX").blastResistance(6.0f).hardness(2.0f).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType OAK_FENCE = StateTypes.builder().name("OAK_FENCE").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType SPRUCE_FENCE = StateTypes.builder().name("SPRUCE_FENCE").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType BIRCH_FENCE = StateTypes.builder().name("BIRCH_FENCE").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType JUNGLE_FENCE = StateTypes.builder().name("JUNGLE_FENCE").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType ACACIA_FENCE = StateTypes.builder().name("ACACIA_FENCE").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType DARK_OAK_FENCE = StateTypes.builder().name("DARK_OAK_FENCE").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType CRIMSON_FENCE = StateTypes.builder().name("CRIMSON_FENCE").blastResistance(3.0f).hardness(2.0f).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType WARPED_FENCE = StateTypes.builder().name("WARPED_FENCE").blastResistance(3.0f).hardness(2.0f).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType PUMPKIN = StateTypes.builder().name("PUMPKIN").blastResistance(1.0f).hardness(1.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.VEGETABLE).isSolid(true).build();
    public static StateType CARVED_PUMPKIN = StateTypes.builder().name("CARVED_PUMPKIN").blastResistance(1.0f).hardness(1.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.VEGETABLE).isSolid(true).build();
    public static StateType JACK_O_LANTERN = StateTypes.builder().name("JACK_O_LANTERN").blastResistance(1.0f).hardness(1.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.VEGETABLE).isSolid(true).build();
    public static StateType NETHERRACK = StateTypes.builder().name("NETHERRACK").blastResistance(0.4f).hardness(0.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SOUL_SAND = StateTypes.builder().name("SOUL_SAND").blastResistance(0.5f).hardness(0.5f).speed(0.4f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType SOUL_SOIL = StateTypes.builder().name("SOUL_SOIL").blastResistance(0.5f).hardness(0.5f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.DIRT).isSolid(true).build();
    public static StateType BASALT = StateTypes.builder().name("BASALT").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_BASALT = StateTypes.builder().name("POLISHED_BASALT").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SMOOTH_BASALT = StateTypes.builder().name("SMOOTH_BASALT").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SOUL_TORCH = StateTypes.builder().name("SOUL_TORCH").setMaterial(MaterialType.DECORATION).build();
    public static StateType GLOWSTONE = StateTypes.builder().name("GLOWSTONE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType INFESTED_STONE = StateTypes.builder().name("INFESTED_STONE").blastResistance(0.75f).hardness(0.75f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.CLAY).isSolid(true).build();
    public static StateType INFESTED_COBBLESTONE = StateTypes.builder().name("INFESTED_COBBLESTONE").blastResistance(0.75f).hardness(1.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.CLAY).isSolid(true).build();
    public static StateType INFESTED_STONE_BRICKS = StateTypes.builder().name("INFESTED_STONE_BRICKS").blastResistance(0.75f).hardness(0.75f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.CLAY).isSolid(true).build();
    public static StateType INFESTED_MOSSY_STONE_BRICKS = StateTypes.builder().name("INFESTED_MOSSY_STONE_BRICKS").blastResistance(0.75f).hardness(0.75f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.CLAY).isSolid(true).build();
    public static StateType INFESTED_CRACKED_STONE_BRICKS = StateTypes.builder().name("INFESTED_CRACKED_STONE_BRICKS").blastResistance(0.75f).hardness(0.75f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.CLAY).isSolid(true).build();
    public static StateType INFESTED_CHISELED_STONE_BRICKS = StateTypes.builder().name("INFESTED_CHISELED_STONE_BRICKS").blastResistance(0.75f).hardness(0.75f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.CLAY).isSolid(true).build();
    public static StateType INFESTED_DEEPSLATE = StateTypes.builder().name("INFESTED_DEEPSLATE").blastResistance(0.75f).hardness(1.5f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.CLAY).isSolid(true).build();
    public static StateType STONE_BRICKS = StateTypes.builder().name("STONE_BRICKS").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType MOSSY_STONE_BRICKS = StateTypes.builder().name("MOSSY_STONE_BRICKS").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CRACKED_STONE_BRICKS = StateTypes.builder().name("CRACKED_STONE_BRICKS").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CHISELED_STONE_BRICKS = StateTypes.builder().name("CHISELED_STONE_BRICKS").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_BRICKS = StateTypes.builder().name("DEEPSLATE_BRICKS").blastResistance(6.0f).hardness(3.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CRACKED_DEEPSLATE_BRICKS = StateTypes.builder().name("CRACKED_DEEPSLATE_BRICKS").blastResistance(6.0f).hardness(3.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_TILES = StateTypes.builder().name("DEEPSLATE_TILES").blastResistance(6.0f).hardness(3.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CRACKED_DEEPSLATE_TILES = StateTypes.builder().name("CRACKED_DEEPSLATE_TILES").blastResistance(6.0f).hardness(3.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CHISELED_DEEPSLATE = StateTypes.builder().name("CHISELED_DEEPSLATE").blastResistance(6.0f).hardness(3.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BROWN_MUSHROOM_BLOCK = StateTypes.builder().name("BROWN_MUSHROOM_BLOCK").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType RED_MUSHROOM_BLOCK = StateTypes.builder().name("RED_MUSHROOM_BLOCK").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType MUSHROOM_STEM = StateTypes.builder().name("MUSHROOM_STEM").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType IRON_BARS = StateTypes.builder().name("IRON_BARS").blastResistance(6.0f).hardness(5.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType CHAIN = StateTypes.builder().name("CHAIN").blastResistance(6.0f).hardness(5.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType GLASS_PANE = StateTypes.builder().name("GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType MELON = StateTypes.builder().name("MELON").blastResistance(1.0f).hardness(1.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.VEGETABLE).isSolid(true).build();
    public static StateType VINE = StateTypes.builder().name("VINE").blastResistance(0.2f).hardness(0.2f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.REPLACEABLE_PLANT).build();
    public static StateType GLOW_LICHEN = StateTypes.builder().name("GLOW_LICHEN").blastResistance(0.2f).hardness(0.2f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.REPLACEABLE_PLANT).build();
    public static StateType BRICK_STAIRS = StateTypes.builder().name("BRICK_STAIRS").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType STONE_BRICK_STAIRS = StateTypes.builder().name("STONE_BRICK_STAIRS").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType MYCELIUM = StateTypes.builder().name("MYCELIUM").blastResistance(0.6f).hardness(0.6f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.GRASS).isSolid(true).build();
    public static StateType LILY_PAD = StateTypes.builder().name("LILY_PAD").setMaterial(MaterialType.PLANT).isSolid(true).build();
    public static StateType NETHER_BRICKS = StateTypes.builder().name("NETHER_BRICKS").blastResistance(6.0f).hardness(2.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CRACKED_NETHER_BRICKS = StateTypes.builder().name("CRACKED_NETHER_BRICKS").blastResistance(6.0f).hardness(2.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CHISELED_NETHER_BRICKS = StateTypes.builder().name("CHISELED_NETHER_BRICKS").blastResistance(6.0f).hardness(2.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType NETHER_BRICK_FENCE = StateTypes.builder().name("NETHER_BRICK_FENCE").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType NETHER_BRICK_STAIRS = StateTypes.builder().name("NETHER_BRICK_STAIRS").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType ENCHANTING_TABLE = StateTypes.builder().name("ENCHANTING_TABLE").blastResistance(1200.0f).hardness(5.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType END_PORTAL_FRAME = StateTypes.builder().name("END_PORTAL_FRAME").blastResistance(3600000.0f).hardness(-1.0f).isBlocking(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType END_STONE = StateTypes.builder().name("END_STONE").blastResistance(9.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType END_STONE_BRICKS = StateTypes.builder().name("END_STONE_BRICKS").blastResistance(9.0f).hardness(3.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DRAGON_EGG = StateTypes.builder().name("DRAGON_EGG").blastResistance(9.0f).hardness(3.0f).hasGravity(true).isBlocking(true).setMaterial(MaterialType.EGG).isSolid(true).build();
    public static StateType SANDSTONE_STAIRS = StateTypes.builder().name("SANDSTONE_STAIRS").blastResistance(0.8f).hardness(0.8f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType ENDER_CHEST = StateTypes.builder().name("ENDER_CHEST").blastResistance(600.0f).hardness(22.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType EMERALD_BLOCK = StateTypes.builder().name("EMERALD_BLOCK").blastResistance(6.0f).hardness(5.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType SPRUCE_STAIRS = StateTypes.builder().name("SPRUCE_STAIRS").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType BIRCH_STAIRS = StateTypes.builder().name("BIRCH_STAIRS").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType JUNGLE_STAIRS = StateTypes.builder().name("JUNGLE_STAIRS").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType CRIMSON_STAIRS = StateTypes.builder().name("CRIMSON_STAIRS").blastResistance(3.0f).hardness(2.0f).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType WARPED_STAIRS = StateTypes.builder().name("WARPED_STAIRS").blastResistance(3.0f).hardness(2.0f).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType COMMAND_BLOCK = StateTypes.builder().name("COMMAND_BLOCK").blastResistance(3600000.0f).hardness(-1.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType BEACON = StateTypes.builder().name("BEACON").blastResistance(3.0f).hardness(3.0f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType COBBLESTONE_WALL = StateTypes.builder().name("COBBLESTONE_WALL").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType MOSSY_COBBLESTONE_WALL = StateTypes.builder().name("MOSSY_COBBLESTONE_WALL").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BRICK_WALL = StateTypes.builder().name("BRICK_WALL").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType PRISMARINE_WALL = StateTypes.builder().name("PRISMARINE_WALL").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType RED_SANDSTONE_WALL = StateTypes.builder().name("RED_SANDSTONE_WALL").blastResistance(0.8f).hardness(0.8f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType MOSSY_STONE_BRICK_WALL = StateTypes.builder().name("MOSSY_STONE_BRICK_WALL").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType GRANITE_WALL = StateTypes.builder().name("GRANITE_WALL").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType STONE_BRICK_WALL = StateTypes.builder().name("STONE_BRICK_WALL").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType NETHER_BRICK_WALL = StateTypes.builder().name("NETHER_BRICK_WALL").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType ANDESITE_WALL = StateTypes.builder().name("ANDESITE_WALL").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType RED_NETHER_BRICK_WALL = StateTypes.builder().name("RED_NETHER_BRICK_WALL").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SANDSTONE_WALL = StateTypes.builder().name("SANDSTONE_WALL").blastResistance(0.8f).hardness(0.8f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType END_STONE_BRICK_WALL = StateTypes.builder().name("END_STONE_BRICK_WALL").blastResistance(9.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DIORITE_WALL = StateTypes.builder().name("DIORITE_WALL").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BLACKSTONE_WALL = StateTypes.builder().name("BLACKSTONE_WALL").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_BLACKSTONE_WALL = StateTypes.builder().name("POLISHED_BLACKSTONE_WALL").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_BLACKSTONE_BRICK_WALL = StateTypes.builder().name("POLISHED_BLACKSTONE_BRICK_WALL").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType COBBLED_DEEPSLATE_WALL = StateTypes.builder().name("COBBLED_DEEPSLATE_WALL").blastResistance(6.0f).hardness(3.5f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_DEEPSLATE_WALL = StateTypes.builder().name("POLISHED_DEEPSLATE_WALL").blastResistance(6.0f).hardness(3.5f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_BRICK_WALL = StateTypes.builder().name("DEEPSLATE_BRICK_WALL").blastResistance(6.0f).hardness(3.5f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_TILE_WALL = StateTypes.builder().name("DEEPSLATE_TILE_WALL").blastResistance(6.0f).hardness(3.5f).isBlocking(true).requiresCorrectTool(true).isShapeExceedsCube(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType ANVIL = StateTypes.builder().name("ANVIL").blastResistance(1200.0f).hardness(5.0f).hasGravity(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.HEAVY_METAL).isSolid(true).build();
    public static StateType CHIPPED_ANVIL = StateTypes.builder().name("CHIPPED_ANVIL").blastResistance(1200.0f).hardness(5.0f).hasGravity(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.HEAVY_METAL).isSolid(true).build();
    public static StateType DAMAGED_ANVIL = StateTypes.builder().name("DAMAGED_ANVIL").blastResistance(1200.0f).hardness(5.0f).hasGravity(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.HEAVY_METAL).isSolid(true).build();
    public static StateType CHISELED_QUARTZ_BLOCK = StateTypes.builder().name("CHISELED_QUARTZ_BLOCK").blastResistance(0.8f).hardness(0.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType QUARTZ_BLOCK = StateTypes.builder().name("QUARTZ_BLOCK").blastResistance(0.8f).hardness(0.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType QUARTZ_BRICKS = StateTypes.builder().name("QUARTZ_BRICKS").blastResistance(0.8f).hardness(0.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType QUARTZ_PILLAR = StateTypes.builder().name("QUARTZ_PILLAR").blastResistance(0.8f).hardness(0.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType QUARTZ_STAIRS = StateTypes.builder().name("QUARTZ_STAIRS").blastResistance(0.8f).hardness(0.8f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType WHITE_TERRACOTTA = StateTypes.builder().name("WHITE_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType ORANGE_TERRACOTTA = StateTypes.builder().name("ORANGE_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType MAGENTA_TERRACOTTA = StateTypes.builder().name("MAGENTA_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType LIGHT_BLUE_TERRACOTTA = StateTypes.builder().name("LIGHT_BLUE_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType YELLOW_TERRACOTTA = StateTypes.builder().name("YELLOW_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType LIME_TERRACOTTA = StateTypes.builder().name("LIME_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType PINK_TERRACOTTA = StateTypes.builder().name("PINK_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType GRAY_TERRACOTTA = StateTypes.builder().name("GRAY_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType LIGHT_GRAY_TERRACOTTA = StateTypes.builder().name("LIGHT_GRAY_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CYAN_TERRACOTTA = StateTypes.builder().name("CYAN_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType PURPLE_TERRACOTTA = StateTypes.builder().name("PURPLE_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BLUE_TERRACOTTA = StateTypes.builder().name("BLUE_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BROWN_TERRACOTTA = StateTypes.builder().name("BROWN_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType GREEN_TERRACOTTA = StateTypes.builder().name("GREEN_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType RED_TERRACOTTA = StateTypes.builder().name("RED_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BLACK_TERRACOTTA = StateTypes.builder().name("BLACK_TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BARRIER = StateTypes.builder().name("BARRIER").blastResistance(3600000.8f).hardness(-1.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.BARRIER).isSolid(true).build();
    public static StateType LIGHT = StateTypes.builder().name("LIGHT").blastResistance(3600000.8f).hardness(-1.0f).setMaterial(MaterialType.AIR).isSolid(true).build();
    public static StateType HAY_BLOCK = StateTypes.builder().name("HAY_BLOCK").blastResistance(0.5f).hardness(0.5f).isBurnable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.GRASS).isSolid(true).build();
    public static StateType WHITE_CARPET = StateTypes.builder().name("WHITE_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType ORANGE_CARPET = StateTypes.builder().name("ORANGE_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType MAGENTA_CARPET = StateTypes.builder().name("MAGENTA_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType LIGHT_BLUE_CARPET = StateTypes.builder().name("LIGHT_BLUE_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType YELLOW_CARPET = StateTypes.builder().name("YELLOW_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType LIME_CARPET = StateTypes.builder().name("LIME_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType PINK_CARPET = StateTypes.builder().name("PINK_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType GRAY_CARPET = StateTypes.builder().name("GRAY_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType LIGHT_GRAY_CARPET = StateTypes.builder().name("LIGHT_GRAY_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType CYAN_CARPET = StateTypes.builder().name("CYAN_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType PURPLE_CARPET = StateTypes.builder().name("PURPLE_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType BLUE_CARPET = StateTypes.builder().name("BLUE_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType BROWN_CARPET = StateTypes.builder().name("BROWN_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType GREEN_CARPET = StateTypes.builder().name("GREEN_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType RED_CARPET = StateTypes.builder().name("RED_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType BLACK_CARPET = StateTypes.builder().name("BLACK_CARPET").blastResistance(0.1f).hardness(0.1f).isBurnable(true).isFlammable(true).setMaterial(MaterialType.CLOTH_DECORATION).isSolid(true).build();
    public static StateType TERRACOTTA = StateTypes.builder().name("TERRACOTTA").blastResistance(4.2f).hardness(1.25f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType PACKED_ICE = StateTypes.builder().name("PACKED_ICE").blastResistance(0.5f).hardness(0.5f).slipperiness(0.98f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.ICE_SOLID).isSolid(true).build();
    public static StateType ACACIA_STAIRS = StateTypes.builder().name("ACACIA_STAIRS").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType DARK_OAK_STAIRS = StateTypes.builder().name("DARK_OAK_STAIRS").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType DIRT_PATH = StateTypes.builder().name("DIRT_PATH").blastResistance(0.65f).hardness(0.65f).isBlocking(true).setMaterial(MaterialType.DIRT).isSolid(true).build();
    public static StateType SUNFLOWER = StateTypes.builder().name("SUNFLOWER").isBurnable(true).isFlammable(true).setMaterial(MaterialType.REPLACEABLE_PLANT).build();
    public static StateType LILAC = StateTypes.builder().name("LILAC").isBurnable(true).isFlammable(true).setMaterial(MaterialType.REPLACEABLE_PLANT).build();
    public static StateType ROSE_BUSH = StateTypes.builder().name("ROSE_BUSH").isBurnable(true).isFlammable(true).setMaterial(MaterialType.REPLACEABLE_PLANT).build();
    public static StateType PEONY = StateTypes.builder().name("PEONY").isBurnable(true).isFlammable(true).setMaterial(MaterialType.REPLACEABLE_PLANT).build();
    public static StateType TALL_GRASS = StateTypes.builder().name("TALL_GRASS").isBurnable(true).isFlammable(true).setMaterial(MaterialType.REPLACEABLE_PLANT).build();
    public static StateType LARGE_FERN = StateTypes.builder().name("LARGE_FERN").isBurnable(true).isFlammable(true).setMaterial(MaterialType.REPLACEABLE_PLANT).build();
    public static StateType WHITE_STAINED_GLASS = StateTypes.builder().name("WHITE_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType ORANGE_STAINED_GLASS = StateTypes.builder().name("ORANGE_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType MAGENTA_STAINED_GLASS = StateTypes.builder().name("MAGENTA_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType LIGHT_BLUE_STAINED_GLASS = StateTypes.builder().name("LIGHT_BLUE_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType YELLOW_STAINED_GLASS = StateTypes.builder().name("YELLOW_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType LIME_STAINED_GLASS = StateTypes.builder().name("LIME_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType PINK_STAINED_GLASS = StateTypes.builder().name("PINK_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType GRAY_STAINED_GLASS = StateTypes.builder().name("GRAY_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType LIGHT_GRAY_STAINED_GLASS = StateTypes.builder().name("LIGHT_GRAY_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType CYAN_STAINED_GLASS = StateTypes.builder().name("CYAN_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType PURPLE_STAINED_GLASS = StateTypes.builder().name("PURPLE_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType BLUE_STAINED_GLASS = StateTypes.builder().name("BLUE_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType BROWN_STAINED_GLASS = StateTypes.builder().name("BROWN_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType GREEN_STAINED_GLASS = StateTypes.builder().name("GREEN_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType RED_STAINED_GLASS = StateTypes.builder().name("RED_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType BLACK_STAINED_GLASS = StateTypes.builder().name("BLACK_STAINED_GLASS").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType WHITE_STAINED_GLASS_PANE = StateTypes.builder().name("WHITE_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType ORANGE_STAINED_GLASS_PANE = StateTypes.builder().name("ORANGE_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType MAGENTA_STAINED_GLASS_PANE = StateTypes.builder().name("MAGENTA_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType LIGHT_BLUE_STAINED_GLASS_PANE = StateTypes.builder().name("LIGHT_BLUE_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType YELLOW_STAINED_GLASS_PANE = StateTypes.builder().name("YELLOW_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType LIME_STAINED_GLASS_PANE = StateTypes.builder().name("LIME_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType PINK_STAINED_GLASS_PANE = StateTypes.builder().name("PINK_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType GRAY_STAINED_GLASS_PANE = StateTypes.builder().name("GRAY_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType LIGHT_GRAY_STAINED_GLASS_PANE = StateTypes.builder().name("LIGHT_GRAY_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType CYAN_STAINED_GLASS_PANE = StateTypes.builder().name("CYAN_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType PURPLE_STAINED_GLASS_PANE = StateTypes.builder().name("PURPLE_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType BLUE_STAINED_GLASS_PANE = StateTypes.builder().name("BLUE_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType BROWN_STAINED_GLASS_PANE = StateTypes.builder().name("BROWN_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType GREEN_STAINED_GLASS_PANE = StateTypes.builder().name("GREEN_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType RED_STAINED_GLASS_PANE = StateTypes.builder().name("RED_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType BLACK_STAINED_GLASS_PANE = StateTypes.builder().name("BLACK_STAINED_GLASS_PANE").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType PRISMARINE = StateTypes.builder().name("PRISMARINE").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType PRISMARINE_BRICKS = StateTypes.builder().name("PRISMARINE_BRICKS").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DARK_PRISMARINE = StateTypes.builder().name("DARK_PRISMARINE").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType PRISMARINE_STAIRS = StateTypes.builder().name("PRISMARINE_STAIRS").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType PRISMARINE_BRICK_STAIRS = StateTypes.builder().name("PRISMARINE_BRICK_STAIRS").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DARK_PRISMARINE_STAIRS = StateTypes.builder().name("DARK_PRISMARINE_STAIRS").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SEA_LANTERN = StateTypes.builder().name("SEA_LANTERN").blastResistance(0.3f).hardness(0.3f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType RED_SANDSTONE = StateTypes.builder().name("RED_SANDSTONE").blastResistance(0.8f).hardness(0.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CHISELED_RED_SANDSTONE = StateTypes.builder().name("CHISELED_RED_SANDSTONE").blastResistance(0.8f).hardness(0.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CUT_RED_SANDSTONE = StateTypes.builder().name("CUT_RED_SANDSTONE").blastResistance(0.8f).hardness(0.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType RED_SANDSTONE_STAIRS = StateTypes.builder().name("RED_SANDSTONE_STAIRS").blastResistance(0.8f).hardness(0.8f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType REPEATING_COMMAND_BLOCK = StateTypes.builder().name("REPEATING_COMMAND_BLOCK").blastResistance(3600000.0f).hardness(-1.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType CHAIN_COMMAND_BLOCK = StateTypes.builder().name("CHAIN_COMMAND_BLOCK").blastResistance(3600000.0f).hardness(-1.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType MAGMA_BLOCK = StateTypes.builder().name("MAGMA_BLOCK").blastResistance(0.5f).hardness(0.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType NETHER_WART_BLOCK = StateTypes.builder().name("NETHER_WART_BLOCK").blastResistance(1.0f).hardness(1.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.GRASS).isSolid(true).build();
    public static StateType WARPED_WART_BLOCK = StateTypes.builder().name("WARPED_WART_BLOCK").blastResistance(1.0f).hardness(1.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.GRASS).isSolid(true).build();
    public static StateType RED_NETHER_BRICKS = StateTypes.builder().name("RED_NETHER_BRICKS").blastResistance(6.0f).hardness(2.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BONE_BLOCK = StateTypes.builder().name("BONE_BLOCK").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType STRUCTURE_VOID = StateTypes.builder().name("STRUCTURE_VOID").setMaterial(MaterialType.STRUCTURAL_AIR).setPushReaction(PushReaction.DESTROY).build();
    public static StateType SHULKER_BOX = StateTypes.builder().name("SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType WHITE_SHULKER_BOX = StateTypes.builder().name("WHITE_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType ORANGE_SHULKER_BOX = StateTypes.builder().name("ORANGE_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType MAGENTA_SHULKER_BOX = StateTypes.builder().name("MAGENTA_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType LIGHT_BLUE_SHULKER_BOX = StateTypes.builder().name("LIGHT_BLUE_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType YELLOW_SHULKER_BOX = StateTypes.builder().name("YELLOW_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType LIME_SHULKER_BOX = StateTypes.builder().name("LIME_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType PINK_SHULKER_BOX = StateTypes.builder().name("PINK_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType GRAY_SHULKER_BOX = StateTypes.builder().name("GRAY_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType LIGHT_GRAY_SHULKER_BOX = StateTypes.builder().name("LIGHT_GRAY_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType CYAN_SHULKER_BOX = StateTypes.builder().name("CYAN_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType PURPLE_SHULKER_BOX = StateTypes.builder().name("PURPLE_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType BLUE_SHULKER_BOX = StateTypes.builder().name("BLUE_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType BROWN_SHULKER_BOX = StateTypes.builder().name("BROWN_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType GREEN_SHULKER_BOX = StateTypes.builder().name("GREEN_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType RED_SHULKER_BOX = StateTypes.builder().name("RED_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType BLACK_SHULKER_BOX = StateTypes.builder().name("BLACK_SHULKER_BOX").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SHULKER_SHELL).isSolid(true).build();
    public static StateType WHITE_GLAZED_TERRACOTTA = StateTypes.builder().name("WHITE_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType ORANGE_GLAZED_TERRACOTTA = StateTypes.builder().name("ORANGE_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType MAGENTA_GLAZED_TERRACOTTA = StateTypes.builder().name("MAGENTA_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType LIGHT_BLUE_GLAZED_TERRACOTTA = StateTypes.builder().name("LIGHT_BLUE_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType YELLOW_GLAZED_TERRACOTTA = StateTypes.builder().name("YELLOW_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType LIME_GLAZED_TERRACOTTA = StateTypes.builder().name("LIME_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType PINK_GLAZED_TERRACOTTA = StateTypes.builder().name("PINK_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType GRAY_GLAZED_TERRACOTTA = StateTypes.builder().name("GRAY_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType LIGHT_GRAY_GLAZED_TERRACOTTA = StateTypes.builder().name("LIGHT_GRAY_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType CYAN_GLAZED_TERRACOTTA = StateTypes.builder().name("CYAN_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType PURPLE_GLAZED_TERRACOTTA = StateTypes.builder().name("PURPLE_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType BLUE_GLAZED_TERRACOTTA = StateTypes.builder().name("BLUE_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType BROWN_GLAZED_TERRACOTTA = StateTypes.builder().name("BROWN_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType GREEN_GLAZED_TERRACOTTA = StateTypes.builder().name("GREEN_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType RED_GLAZED_TERRACOTTA = StateTypes.builder().name("RED_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType BLACK_GLAZED_TERRACOTTA = StateTypes.builder().name("BLACK_GLAZED_TERRACOTTA").blastResistance(1.4f).hardness(1.4f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.PUSH_ONLY).isSolid(true).build();
    public static StateType WHITE_CONCRETE = StateTypes.builder().name("WHITE_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType ORANGE_CONCRETE = StateTypes.builder().name("ORANGE_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType MAGENTA_CONCRETE = StateTypes.builder().name("MAGENTA_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType LIGHT_BLUE_CONCRETE = StateTypes.builder().name("LIGHT_BLUE_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType YELLOW_CONCRETE = StateTypes.builder().name("YELLOW_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType LIME_CONCRETE = StateTypes.builder().name("LIME_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType PINK_CONCRETE = StateTypes.builder().name("PINK_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType GRAY_CONCRETE = StateTypes.builder().name("GRAY_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType LIGHT_GRAY_CONCRETE = StateTypes.builder().name("LIGHT_GRAY_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CYAN_CONCRETE = StateTypes.builder().name("CYAN_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType PURPLE_CONCRETE = StateTypes.builder().name("PURPLE_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BLUE_CONCRETE = StateTypes.builder().name("BLUE_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BROWN_CONCRETE = StateTypes.builder().name("BROWN_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType GREEN_CONCRETE = StateTypes.builder().name("GREEN_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType RED_CONCRETE = StateTypes.builder().name("RED_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BLACK_CONCRETE = StateTypes.builder().name("BLACK_CONCRETE").blastResistance(1.8f).hardness(1.8f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType WHITE_CONCRETE_POWDER = StateTypes.builder().name("WHITE_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType ORANGE_CONCRETE_POWDER = StateTypes.builder().name("ORANGE_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType MAGENTA_CONCRETE_POWDER = StateTypes.builder().name("MAGENTA_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType LIGHT_BLUE_CONCRETE_POWDER = StateTypes.builder().name("LIGHT_BLUE_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType YELLOW_CONCRETE_POWDER = StateTypes.builder().name("YELLOW_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType LIME_CONCRETE_POWDER = StateTypes.builder().name("LIME_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType PINK_CONCRETE_POWDER = StateTypes.builder().name("PINK_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType GRAY_CONCRETE_POWDER = StateTypes.builder().name("GRAY_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType LIGHT_GRAY_CONCRETE_POWDER = StateTypes.builder().name("LIGHT_GRAY_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType CYAN_CONCRETE_POWDER = StateTypes.builder().name("CYAN_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType PURPLE_CONCRETE_POWDER = StateTypes.builder().name("PURPLE_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType BLUE_CONCRETE_POWDER = StateTypes.builder().name("BLUE_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType BROWN_CONCRETE_POWDER = StateTypes.builder().name("BROWN_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType GREEN_CONCRETE_POWDER = StateTypes.builder().name("GREEN_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType RED_CONCRETE_POWDER = StateTypes.builder().name("RED_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType BLACK_CONCRETE_POWDER = StateTypes.builder().name("BLACK_CONCRETE_POWDER").blastResistance(0.5f).hardness(0.5f).hasGravity(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SAND).isSolid(true).build();
    public static StateType TURTLE_EGG = StateTypes.builder().name("TURTLE_EGG").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.EGG).isSolid(true).build();
    public static StateType DEAD_TUBE_CORAL_BLOCK = StateTypes.builder().name("DEAD_TUBE_CORAL_BLOCK").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEAD_BRAIN_CORAL_BLOCK = StateTypes.builder().name("DEAD_BRAIN_CORAL_BLOCK").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEAD_BUBBLE_CORAL_BLOCK = StateTypes.builder().name("DEAD_BUBBLE_CORAL_BLOCK").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEAD_FIRE_CORAL_BLOCK = StateTypes.builder().name("DEAD_FIRE_CORAL_BLOCK").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEAD_HORN_CORAL_BLOCK = StateTypes.builder().name("DEAD_HORN_CORAL_BLOCK").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType TUBE_CORAL_BLOCK = StateTypes.builder().name("TUBE_CORAL_BLOCK").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BRAIN_CORAL_BLOCK = StateTypes.builder().name("BRAIN_CORAL_BLOCK").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BUBBLE_CORAL_BLOCK = StateTypes.builder().name("BUBBLE_CORAL_BLOCK").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType FIRE_CORAL_BLOCK = StateTypes.builder().name("FIRE_CORAL_BLOCK").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType HORN_CORAL_BLOCK = StateTypes.builder().name("HORN_CORAL_BLOCK").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType TUBE_CORAL = StateTypes.builder().name("TUBE_CORAL").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType BRAIN_CORAL = StateTypes.builder().name("BRAIN_CORAL").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType BUBBLE_CORAL = StateTypes.builder().name("BUBBLE_CORAL").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType FIRE_CORAL = StateTypes.builder().name("FIRE_CORAL").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType HORN_CORAL = StateTypes.builder().name("HORN_CORAL").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType DEAD_BRAIN_CORAL = StateTypes.builder().name("DEAD_BRAIN_CORAL").isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).build();
    public static StateType DEAD_BUBBLE_CORAL = StateTypes.builder().name("DEAD_BUBBLE_CORAL").isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).build();
    public static StateType DEAD_FIRE_CORAL = StateTypes.builder().name("DEAD_FIRE_CORAL").isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).build();
    public static StateType DEAD_HORN_CORAL = StateTypes.builder().name("DEAD_HORN_CORAL").isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).build();
    public static StateType DEAD_TUBE_CORAL = StateTypes.builder().name("DEAD_TUBE_CORAL").isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).build();
    public static StateType TUBE_CORAL_FAN = StateTypes.builder().name("TUBE_CORAL_FAN").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType BRAIN_CORAL_FAN = StateTypes.builder().name("BRAIN_CORAL_FAN").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType BUBBLE_CORAL_FAN = StateTypes.builder().name("BUBBLE_CORAL_FAN").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType FIRE_CORAL_FAN = StateTypes.builder().name("FIRE_CORAL_FAN").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType HORN_CORAL_FAN = StateTypes.builder().name("HORN_CORAL_FAN").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType DEAD_TUBE_CORAL_FAN = StateTypes.builder().name("DEAD_TUBE_CORAL_FAN").isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).build();
    public static StateType DEAD_BRAIN_CORAL_FAN = StateTypes.builder().name("DEAD_BRAIN_CORAL_FAN").isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).build();
    public static StateType DEAD_BUBBLE_CORAL_FAN = StateTypes.builder().name("DEAD_BUBBLE_CORAL_FAN").isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).build();
    public static StateType DEAD_FIRE_CORAL_FAN = StateTypes.builder().name("DEAD_FIRE_CORAL_FAN").isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).build();
    public static StateType DEAD_HORN_CORAL_FAN = StateTypes.builder().name("DEAD_HORN_CORAL_FAN").isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).build();
    public static StateType BLUE_ICE = StateTypes.builder().name("BLUE_ICE").blastResistance(2.8f).hardness(2.8f).slipperiness(0.989f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.ICE_SOLID).isSolid(true).build();
    public static StateType CONDUIT = StateTypes.builder().name("CONDUIT").blastResistance(3.0f).hardness(3.0f).isBlocking(true).setMaterial(MaterialType.GLASS).isSolid(true).build();
    public static StateType POLISHED_GRANITE_STAIRS = StateTypes.builder().name("POLISHED_GRANITE_STAIRS").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SMOOTH_RED_SANDSTONE_STAIRS = StateTypes.builder().name("SMOOTH_RED_SANDSTONE_STAIRS").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType MOSSY_STONE_BRICK_STAIRS = StateTypes.builder().name("MOSSY_STONE_BRICK_STAIRS").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_DIORITE_STAIRS = StateTypes.builder().name("POLISHED_DIORITE_STAIRS").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType MOSSY_COBBLESTONE_STAIRS = StateTypes.builder().name("MOSSY_COBBLESTONE_STAIRS").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType END_STONE_BRICK_STAIRS = StateTypes.builder().name("END_STONE_BRICK_STAIRS").blastResistance(9.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType STONE_STAIRS = StateTypes.builder().name("STONE_STAIRS").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SMOOTH_SANDSTONE_STAIRS = StateTypes.builder().name("SMOOTH_SANDSTONE_STAIRS").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SMOOTH_QUARTZ_STAIRS = StateTypes.builder().name("SMOOTH_QUARTZ_STAIRS").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType GRANITE_STAIRS = StateTypes.builder().name("GRANITE_STAIRS").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType ANDESITE_STAIRS = StateTypes.builder().name("ANDESITE_STAIRS").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType RED_NETHER_BRICK_STAIRS = StateTypes.builder().name("RED_NETHER_BRICK_STAIRS").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_ANDESITE_STAIRS = StateTypes.builder().name("POLISHED_ANDESITE_STAIRS").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DIORITE_STAIRS = StateTypes.builder().name("DIORITE_STAIRS").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType COBBLED_DEEPSLATE_STAIRS = StateTypes.builder().name("COBBLED_DEEPSLATE_STAIRS").blastResistance(6.0f).hardness(3.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_DEEPSLATE_STAIRS = StateTypes.builder().name("POLISHED_DEEPSLATE_STAIRS").blastResistance(6.0f).hardness(3.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_BRICK_STAIRS = StateTypes.builder().name("DEEPSLATE_BRICK_STAIRS").blastResistance(6.0f).hardness(3.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_TILE_STAIRS = StateTypes.builder().name("DEEPSLATE_TILE_STAIRS").blastResistance(6.0f).hardness(3.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_GRANITE_SLAB = StateTypes.builder().name("POLISHED_GRANITE_SLAB").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SMOOTH_RED_SANDSTONE_SLAB = StateTypes.builder().name("SMOOTH_RED_SANDSTONE_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType MOSSY_STONE_BRICK_SLAB = StateTypes.builder().name("MOSSY_STONE_BRICK_SLAB").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_DIORITE_SLAB = StateTypes.builder().name("POLISHED_DIORITE_SLAB").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType MOSSY_COBBLESTONE_SLAB = StateTypes.builder().name("MOSSY_COBBLESTONE_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType END_STONE_BRICK_SLAB = StateTypes.builder().name("END_STONE_BRICK_SLAB").blastResistance(9.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SMOOTH_SANDSTONE_SLAB = StateTypes.builder().name("SMOOTH_SANDSTONE_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SMOOTH_QUARTZ_SLAB = StateTypes.builder().name("SMOOTH_QUARTZ_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType GRANITE_SLAB = StateTypes.builder().name("GRANITE_SLAB").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType ANDESITE_SLAB = StateTypes.builder().name("ANDESITE_SLAB").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType RED_NETHER_BRICK_SLAB = StateTypes.builder().name("RED_NETHER_BRICK_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_ANDESITE_SLAB = StateTypes.builder().name("POLISHED_ANDESITE_SLAB").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DIORITE_SLAB = StateTypes.builder().name("DIORITE_SLAB").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType COBBLED_DEEPSLATE_SLAB = StateTypes.builder().name("COBBLED_DEEPSLATE_SLAB").blastResistance(6.0f).hardness(3.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_DEEPSLATE_SLAB = StateTypes.builder().name("POLISHED_DEEPSLATE_SLAB").blastResistance(6.0f).hardness(3.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_BRICK_SLAB = StateTypes.builder().name("DEEPSLATE_BRICK_SLAB").blastResistance(6.0f).hardness(3.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DEEPSLATE_TILE_SLAB = StateTypes.builder().name("DEEPSLATE_TILE_SLAB").blastResistance(6.0f).hardness(3.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SCAFFOLDING = StateTypes.builder().name("SCAFFOLDING").isBurnable(true).setMaterial(MaterialType.DECORATION).build();
    public static StateType REDSTONE_TORCH = StateTypes.builder().name("REDSTONE_TORCH").setMaterial(MaterialType.DECORATION).build();
    public static StateType REDSTONE_BLOCK = StateTypes.builder().name("REDSTONE_BLOCK").blastResistance(6.0f).hardness(5.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType REPEATER = StateTypes.builder().name("REPEATER").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType COMPARATOR = StateTypes.builder().name("COMPARATOR").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType PISTON = StateTypes.builder().name("PISTON").blastResistance(1.5f).hardness(1.5f).isBlocking(true).setMaterial(MaterialType.PISTON).isSolid(true).build();
    public static StateType STICKY_PISTON = StateTypes.builder().name("STICKY_PISTON").blastResistance(1.5f).hardness(1.5f).isBlocking(true).setMaterial(MaterialType.PISTON).isSolid(true).build();
    public static StateType SLIME_BLOCK = StateTypes.builder().name("SLIME_BLOCK").slipperiness(0.8f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.CLAY).isSolid(true).build();
    public static StateType HONEY_BLOCK = StateTypes.builder().name("HONEY_BLOCK").speed(0.4f).isBlocking(true).setMaterial(MaterialType.CLAY).isSolid(true).build();
    public static StateType OBSERVER = StateTypes.builder().name("OBSERVER").blastResistance(3.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType HOPPER = StateTypes.builder().name("HOPPER").blastResistance(4.8f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType DISPENSER = StateTypes.builder().name("DISPENSER").blastResistance(3.5f).hardness(3.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType DROPPER = StateTypes.builder().name("DROPPER").blastResistance(3.5f).hardness(3.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType LECTERN = StateTypes.builder().name("LECTERN").blastResistance(2.5f).hardness(2.5f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType TARGET = StateTypes.builder().name("TARGET").blastResistance(0.5f).hardness(0.5f).isBurnable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.GRASS).isSolid(true).build();
    public static StateType LEVER = StateTypes.builder().name("LEVER").blastResistance(0.5f).hardness(0.5f).setMaterial(MaterialType.DECORATION).build();
    public static StateType LIGHTNING_ROD = StateTypes.builder().name("LIGHTNING_ROD").blastResistance(6.0f).hardness(3.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType DAYLIGHT_DETECTOR = StateTypes.builder().name("DAYLIGHT_DETECTOR").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType SCULK_SENSOR = StateTypes.builder().name("SCULK_SENSOR").blastResistance(1.5f).hardness(1.5f).isBlocking(true).setMaterial(MaterialType.SCULK).isSolid(true).build();
    public static StateType TRIPWIRE_HOOK = StateTypes.builder().name("TRIPWIRE_HOOK").setMaterial(MaterialType.DECORATION).build();
    public static StateType TRAPPED_CHEST = StateTypes.builder().name("TRAPPED_CHEST").blastResistance(2.5f).hardness(2.5f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType TNT = StateTypes.builder().name("TNT").isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.EXPLOSIVE).isSolid(true).build();
    public static StateType REDSTONE_LAMP = StateTypes.builder().name("REDSTONE_LAMP").blastResistance(0.3f).hardness(0.3f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.BUILDABLE_GLASS).isSolid(true).build();
    public static StateType NOTE_BLOCK = StateTypes.builder().name("NOTE_BLOCK").blastResistance(0.8f).hardness(0.8f).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType STONE_BUTTON = StateTypes.builder().name("STONE_BUTTON").blastResistance(0.5f).hardness(0.5f).setMaterial(MaterialType.DECORATION).build();
    public static StateType POLISHED_BLACKSTONE_BUTTON = StateTypes.builder().name("POLISHED_BLACKSTONE_BUTTON").blastResistance(0.5f).hardness(0.5f).setMaterial(MaterialType.DECORATION).build();
    public static StateType OAK_BUTTON = StateTypes.builder().name("OAK_BUTTON").blastResistance(0.5f).hardness(0.5f).setMaterial(MaterialType.DECORATION).build();
    public static StateType SPRUCE_BUTTON = StateTypes.builder().name("SPRUCE_BUTTON").blastResistance(0.5f).hardness(0.5f).setMaterial(MaterialType.DECORATION).build();
    public static StateType BIRCH_BUTTON = StateTypes.builder().name("BIRCH_BUTTON").blastResistance(0.5f).hardness(0.5f).setMaterial(MaterialType.DECORATION).build();
    public static StateType JUNGLE_BUTTON = StateTypes.builder().name("JUNGLE_BUTTON").blastResistance(0.5f).hardness(0.5f).setMaterial(MaterialType.DECORATION).build();
    public static StateType ACACIA_BUTTON = StateTypes.builder().name("ACACIA_BUTTON").blastResistance(0.5f).hardness(0.5f).setMaterial(MaterialType.DECORATION).build();
    public static StateType DARK_OAK_BUTTON = StateTypes.builder().name("DARK_OAK_BUTTON").blastResistance(0.5f).hardness(0.5f).setMaterial(MaterialType.DECORATION).build();
    public static StateType CRIMSON_BUTTON = StateTypes.builder().name("CRIMSON_BUTTON").blastResistance(0.5f).hardness(0.5f).setMaterial(MaterialType.DECORATION).build();
    public static StateType WARPED_BUTTON = StateTypes.builder().name("WARPED_BUTTON").blastResistance(0.5f).hardness(0.5f).setMaterial(MaterialType.DECORATION).build();
    public static StateType STONE_PRESSURE_PLATE = StateTypes.builder().name("STONE_PRESSURE_PLATE").blastResistance(0.5f).hardness(0.5f).isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.DESTROY).build();
    public static StateType POLISHED_BLACKSTONE_PRESSURE_PLATE = StateTypes.builder().name("POLISHED_BLACKSTONE_PRESSURE_PLATE").blastResistance(0.5f).hardness(0.5f).isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.DESTROY).build();
    public static StateType LIGHT_WEIGHTED_PRESSURE_PLATE = StateTypes.builder().name("LIGHT_WEIGHTED_PRESSURE_PLATE").blastResistance(0.5f).hardness(0.5f).isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).setPushReaction(PushReaction.DESTROY).build();
    public static StateType HEAVY_WEIGHTED_PRESSURE_PLATE = StateTypes.builder().name("HEAVY_WEIGHTED_PRESSURE_PLATE").blastResistance(0.5f).hardness(0.5f).isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).setPushReaction(PushReaction.DESTROY).build();
    public static StateType OAK_PRESSURE_PLATE = StateTypes.builder().name("OAK_PRESSURE_PLATE").blastResistance(0.5f).hardness(0.5f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).setPushReaction(PushReaction.DESTROY).build();
    public static StateType SPRUCE_PRESSURE_PLATE = StateTypes.builder().name("SPRUCE_PRESSURE_PLATE").blastResistance(0.5f).hardness(0.5f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).setPushReaction(PushReaction.DESTROY).build();
    public static StateType BIRCH_PRESSURE_PLATE = StateTypes.builder().name("BIRCH_PRESSURE_PLATE").blastResistance(0.5f).hardness(0.5f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).setPushReaction(PushReaction.DESTROY).build();
    public static StateType JUNGLE_PRESSURE_PLATE = StateTypes.builder().name("JUNGLE_PRESSURE_PLATE").blastResistance(0.5f).hardness(0.5f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).setPushReaction(PushReaction.DESTROY).build();
    public static StateType ACACIA_PRESSURE_PLATE = StateTypes.builder().name("ACACIA_PRESSURE_PLATE").blastResistance(0.5f).hardness(0.5f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).setPushReaction(PushReaction.DESTROY).build();
    public static StateType DARK_OAK_PRESSURE_PLATE = StateTypes.builder().name("DARK_OAK_PRESSURE_PLATE").blastResistance(0.5f).hardness(0.5f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).setPushReaction(PushReaction.DESTROY).build();
    public static StateType CRIMSON_PRESSURE_PLATE = StateTypes.builder().name("CRIMSON_PRESSURE_PLATE").blastResistance(0.5f).hardness(0.5f).isSolid(false).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).setPushReaction(PushReaction.DESTROY).build();
    public static StateType WARPED_PRESSURE_PLATE = StateTypes.builder().name("WARPED_PRESSURE_PLATE").blastResistance(0.5f).hardness(0.5f).isSolid(false).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).setPushReaction(PushReaction.DESTROY).build();
    public static StateType IRON_DOOR = StateTypes.builder().name("IRON_DOOR").blastResistance(5.0f).hardness(5.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType OAK_DOOR = StateTypes.builder().name("OAK_DOOR").blastResistance(3.0f).hardness(3.0f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType SPRUCE_DOOR = StateTypes.builder().name("SPRUCE_DOOR").blastResistance(3.0f).hardness(3.0f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType BIRCH_DOOR = StateTypes.builder().name("BIRCH_DOOR").blastResistance(3.0f).hardness(3.0f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType JUNGLE_DOOR = StateTypes.builder().name("JUNGLE_DOOR").blastResistance(3.0f).hardness(3.0f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType ACACIA_DOOR = StateTypes.builder().name("ACACIA_DOOR").blastResistance(3.0f).hardness(3.0f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType DARK_OAK_DOOR = StateTypes.builder().name("DARK_OAK_DOOR").blastResistance(3.0f).hardness(3.0f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType CRIMSON_DOOR = StateTypes.builder().name("CRIMSON_DOOR").blastResistance(3.0f).hardness(3.0f).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType WARPED_DOOR = StateTypes.builder().name("WARPED_DOOR").blastResistance(3.0f).hardness(3.0f).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType IRON_TRAPDOOR = StateTypes.builder().name("IRON_TRAPDOOR").blastResistance(5.0f).hardness(5.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType OAK_TRAPDOOR = StateTypes.builder().name("OAK_TRAPDOOR").blastResistance(3.0f).hardness(3.0f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType SPRUCE_TRAPDOOR = StateTypes.builder().name("SPRUCE_TRAPDOOR").blastResistance(3.0f).hardness(3.0f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType BIRCH_TRAPDOOR = StateTypes.builder().name("BIRCH_TRAPDOOR").blastResistance(3.0f).hardness(3.0f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType JUNGLE_TRAPDOOR = StateTypes.builder().name("JUNGLE_TRAPDOOR").blastResistance(3.0f).hardness(3.0f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType ACACIA_TRAPDOOR = StateTypes.builder().name("ACACIA_TRAPDOOR").blastResistance(3.0f).hardness(3.0f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType DARK_OAK_TRAPDOOR = StateTypes.builder().name("DARK_OAK_TRAPDOOR").blastResistance(3.0f).hardness(3.0f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType CRIMSON_TRAPDOOR = StateTypes.builder().name("CRIMSON_TRAPDOOR").blastResistance(3.0f).hardness(3.0f).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType WARPED_TRAPDOOR = StateTypes.builder().name("WARPED_TRAPDOOR").blastResistance(3.0f).hardness(3.0f).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType OAK_FENCE_GATE = StateTypes.builder().name("OAK_FENCE_GATE").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType SPRUCE_FENCE_GATE = StateTypes.builder().name("SPRUCE_FENCE_GATE").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType BIRCH_FENCE_GATE = StateTypes.builder().name("BIRCH_FENCE_GATE").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType JUNGLE_FENCE_GATE = StateTypes.builder().name("JUNGLE_FENCE_GATE").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType ACACIA_FENCE_GATE = StateTypes.builder().name("ACACIA_FENCE_GATE").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType DARK_OAK_FENCE_GATE = StateTypes.builder().name("DARK_OAK_FENCE_GATE").blastResistance(3.0f).hardness(2.0f).isBurnable(true).isFlammable(true).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType CRIMSON_FENCE_GATE = StateTypes.builder().name("CRIMSON_FENCE_GATE").blastResistance(3.0f).hardness(2.0f).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType WARPED_FENCE_GATE = StateTypes.builder().name("WARPED_FENCE_GATE").blastResistance(3.0f).hardness(2.0f).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.NETHER_WOOD).isSolid(true).build();
    public static StateType POWERED_RAIL = StateTypes.builder().name("POWERED_RAIL").blastResistance(0.7f).hardness(0.7f).setMaterial(MaterialType.DECORATION).setPushReaction(PushReaction.NORMAL).build();
    public static StateType DETECTOR_RAIL = StateTypes.builder().name("DETECTOR_RAIL").blastResistance(0.7f).hardness(0.7f).setMaterial(MaterialType.DECORATION).setPushReaction(PushReaction.NORMAL).build();
    public static StateType RAIL = StateTypes.builder().name("RAIL").blastResistance(0.7f).hardness(0.7f).setMaterial(MaterialType.DECORATION).setPushReaction(PushReaction.NORMAL).build();
    public static StateType ACTIVATOR_RAIL = StateTypes.builder().name("ACTIVATOR_RAIL").blastResistance(0.7f).hardness(0.7f).setMaterial(MaterialType.DECORATION).setPushReaction(PushReaction.NORMAL).build();
    public static StateType STRUCTURE_BLOCK = StateTypes.builder().name("STRUCTURE_BLOCK").blastResistance(3600000.0f).hardness(-1.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType JIGSAW = StateTypes.builder().name("JIGSAW").blastResistance(3600000.0f).hardness(-1.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType WHEAT = StateTypes.builder().name("WHEAT").setMaterial(MaterialType.PLANT).build();
    public static StateType OAK_SIGN = StateTypes.builder().name("OAK_SIGN").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType SPRUCE_SIGN = StateTypes.builder().name("SPRUCE_SIGN").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType BIRCH_SIGN = StateTypes.builder().name("BIRCH_SIGN").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType JUNGLE_SIGN = StateTypes.builder().name("JUNGLE_SIGN").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType ACACIA_SIGN = StateTypes.builder().name("ACACIA_SIGN").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType DARK_OAK_SIGN = StateTypes.builder().name("DARK_OAK_SIGN").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType CRIMSON_SIGN = StateTypes.builder().name("CRIMSON_SIGN").blastResistance(1.0f).hardness(1.0f).isSolid(false).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).build();
    public static StateType WARPED_SIGN = StateTypes.builder().name("WARPED_SIGN").blastResistance(1.0f).hardness(1.0f).isSolid(false).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).build();
    public static StateType DRIED_KELP_BLOCK = StateTypes.builder().name("DRIED_KELP_BLOCK").blastResistance(2.5f).hardness(0.5f).isBurnable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.GRASS).isSolid(true).build();
    public static StateType CAKE = StateTypes.builder().name("CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType WHITE_BED = StateTypes.builder().name("WHITE_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType ORANGE_BED = StateTypes.builder().name("ORANGE_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType MAGENTA_BED = StateTypes.builder().name("MAGENTA_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType LIGHT_BLUE_BED = StateTypes.builder().name("LIGHT_BLUE_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType YELLOW_BED = StateTypes.builder().name("YELLOW_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType LIME_BED = StateTypes.builder().name("LIME_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType PINK_BED = StateTypes.builder().name("PINK_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType GRAY_BED = StateTypes.builder().name("GRAY_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType LIGHT_GRAY_BED = StateTypes.builder().name("LIGHT_GRAY_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType CYAN_BED = StateTypes.builder().name("CYAN_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType PURPLE_BED = StateTypes.builder().name("PURPLE_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType BLUE_BED = StateTypes.builder().name("BLUE_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType BROWN_BED = StateTypes.builder().name("BROWN_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType GREEN_BED = StateTypes.builder().name("GREEN_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType RED_BED = StateTypes.builder().name("RED_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType BLACK_BED = StateTypes.builder().name("BLACK_BED").blastResistance(0.2f).hardness(0.2f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType NETHER_WART = StateTypes.builder().name("NETHER_WART").setMaterial(MaterialType.PLANT).build();
    public static StateType BREWING_STAND = StateTypes.builder().name("BREWING_STAND").blastResistance(0.5f).hardness(0.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType CAULDRON = StateTypes.builder().name("CAULDRON").blastResistance(2.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType FLOWER_POT = StateTypes.builder().name("FLOWER_POT").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType SKELETON_SKULL = StateTypes.builder().name("SKELETON_SKULL").blastResistance(1.0f).hardness(1.0f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType WITHER_SKELETON_SKULL = StateTypes.builder().name("WITHER_SKELETON_SKULL").blastResistance(1.0f).hardness(1.0f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType PLAYER_HEAD = StateTypes.builder().name("PLAYER_HEAD").blastResistance(1.0f).hardness(1.0f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType ZOMBIE_HEAD = StateTypes.builder().name("ZOMBIE_HEAD").blastResistance(1.0f).hardness(1.0f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType CREEPER_HEAD = StateTypes.builder().name("CREEPER_HEAD").blastResistance(1.0f).hardness(1.0f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType DRAGON_HEAD = StateTypes.builder().name("DRAGON_HEAD").blastResistance(1.0f).hardness(1.0f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType WHITE_BANNER = StateTypes.builder().name("WHITE_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType ORANGE_BANNER = StateTypes.builder().name("ORANGE_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType MAGENTA_BANNER = StateTypes.builder().name("MAGENTA_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType LIGHT_BLUE_BANNER = StateTypes.builder().name("LIGHT_BLUE_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType YELLOW_BANNER = StateTypes.builder().name("YELLOW_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType LIME_BANNER = StateTypes.builder().name("LIME_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType PINK_BANNER = StateTypes.builder().name("PINK_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType GRAY_BANNER = StateTypes.builder().name("GRAY_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType LIGHT_GRAY_BANNER = StateTypes.builder().name("LIGHT_GRAY_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType CYAN_BANNER = StateTypes.builder().name("CYAN_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType PURPLE_BANNER = StateTypes.builder().name("PURPLE_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType BLUE_BANNER = StateTypes.builder().name("BLUE_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType BROWN_BANNER = StateTypes.builder().name("BROWN_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType GREEN_BANNER = StateTypes.builder().name("GREEN_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType RED_BANNER = StateTypes.builder().name("RED_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType BLACK_BANNER = StateTypes.builder().name("BLACK_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType LOOM = StateTypes.builder().name("LOOM").blastResistance(2.5f).hardness(2.5f).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType COMPOSTER = StateTypes.builder().name("COMPOSTER").blastResistance(0.6f).hardness(0.6f).isBurnable(true).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType BARREL = StateTypes.builder().name("BARREL").blastResistance(2.5f).hardness(2.5f).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType SMOKER = StateTypes.builder().name("SMOKER").blastResistance(3.5f).hardness(3.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BLAST_FURNACE = StateTypes.builder().name("BLAST_FURNACE").blastResistance(3.5f).hardness(3.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CARTOGRAPHY_TABLE = StateTypes.builder().name("CARTOGRAPHY_TABLE").blastResistance(2.5f).hardness(2.5f).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType FLETCHING_TABLE = StateTypes.builder().name("FLETCHING_TABLE").blastResistance(2.5f).hardness(2.5f).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType GRINDSTONE = StateTypes.builder().name("GRINDSTONE").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.HEAVY_METAL).isSolid(true).build();
    public static StateType SMITHING_TABLE = StateTypes.builder().name("SMITHING_TABLE").blastResistance(2.5f).hardness(2.5f).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType STONECUTTER = StateTypes.builder().name("STONECUTTER").blastResistance(3.5f).hardness(3.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BELL = StateTypes.builder().name("BELL").blastResistance(5.0f).hardness(5.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType LANTERN = StateTypes.builder().name("LANTERN").blastResistance(3.5f).hardness(3.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType SOUL_LANTERN = StateTypes.builder().name("SOUL_LANTERN").blastResistance(3.5f).hardness(3.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType CAMPFIRE = StateTypes.builder().name("CAMPFIRE").blastResistance(2.0f).hardness(2.0f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType SOUL_CAMPFIRE = StateTypes.builder().name("SOUL_CAMPFIRE").blastResistance(2.0f).hardness(2.0f).isFlammable(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType SHROOMLIGHT = StateTypes.builder().name("SHROOMLIGHT").blastResistance(1.0f).hardness(1.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.GRASS).isSolid(true).build();
    public static StateType BEE_NEST = StateTypes.builder().name("BEE_NEST").blastResistance(0.3f).hardness(0.3f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType BEEHIVE = StateTypes.builder().name("BEEHIVE").blastResistance(0.6f).hardness(0.6f).isBurnable(true).isFlammable(true).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType HONEYCOMB_BLOCK = StateTypes.builder().name("HONEYCOMB_BLOCK").blastResistance(0.6f).hardness(0.6f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.CLAY).isSolid(true).build();
    public static StateType LODESTONE = StateTypes.builder().name("LODESTONE").blastResistance(3.5f).hardness(3.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.HEAVY_METAL).isSolid(true).build();
    public static StateType CRYING_OBSIDIAN = StateTypes.builder().name("CRYING_OBSIDIAN").blastResistance(1200.0f).hardness(50.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BLACKSTONE = StateTypes.builder().name("BLACKSTONE").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BLACKSTONE_SLAB = StateTypes.builder().name("BLACKSTONE_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType BLACKSTONE_STAIRS = StateTypes.builder().name("BLACKSTONE_STAIRS").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType GILDED_BLACKSTONE = StateTypes.builder().name("GILDED_BLACKSTONE").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_BLACKSTONE = StateTypes.builder().name("POLISHED_BLACKSTONE").blastResistance(6.0f).hardness(2.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_BLACKSTONE_SLAB = StateTypes.builder().name("POLISHED_BLACKSTONE_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_BLACKSTONE_STAIRS = StateTypes.builder().name("POLISHED_BLACKSTONE_STAIRS").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CHISELED_POLISHED_BLACKSTONE = StateTypes.builder().name("CHISELED_POLISHED_BLACKSTONE").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_BLACKSTONE_BRICKS = StateTypes.builder().name("POLISHED_BLACKSTONE_BRICKS").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_BLACKSTONE_BRICK_SLAB = StateTypes.builder().name("POLISHED_BLACKSTONE_BRICK_SLAB").blastResistance(6.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType POLISHED_BLACKSTONE_BRICK_STAIRS = StateTypes.builder().name("POLISHED_BLACKSTONE_BRICK_STAIRS").blastResistance(6.0f).hardness(1.5f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CRACKED_POLISHED_BLACKSTONE_BRICKS = StateTypes.builder().name("CRACKED_POLISHED_BLACKSTONE_BRICKS").blastResistance(6.0f).hardness(1.5f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType RESPAWN_ANCHOR = StateTypes.builder().name("RESPAWN_ANCHOR").blastResistance(1200.0f).hardness(50.0f).isOccluding(true).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType CANDLE = StateTypes.builder().name("CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType WHITE_CANDLE = StateTypes.builder().name("WHITE_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType ORANGE_CANDLE = StateTypes.builder().name("ORANGE_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType MAGENTA_CANDLE = StateTypes.builder().name("MAGENTA_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType LIGHT_BLUE_CANDLE = StateTypes.builder().name("LIGHT_BLUE_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType YELLOW_CANDLE = StateTypes.builder().name("YELLOW_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType LIME_CANDLE = StateTypes.builder().name("LIME_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType PINK_CANDLE = StateTypes.builder().name("PINK_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType GRAY_CANDLE = StateTypes.builder().name("GRAY_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType LIGHT_GRAY_CANDLE = StateTypes.builder().name("LIGHT_GRAY_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType CYAN_CANDLE = StateTypes.builder().name("CYAN_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType PURPLE_CANDLE = StateTypes.builder().name("PURPLE_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType BLUE_CANDLE = StateTypes.builder().name("BLUE_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType BROWN_CANDLE = StateTypes.builder().name("BROWN_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType GREEN_CANDLE = StateTypes.builder().name("GREEN_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType RED_CANDLE = StateTypes.builder().name("RED_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType BLACK_CANDLE = StateTypes.builder().name("BLACK_CANDLE").blastResistance(0.1f).hardness(0.1f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType SMALL_AMETHYST_BUD = StateTypes.builder().name("SMALL_AMETHYST_BUD").blastResistance(1.5f).hardness(1.5f).isBlocking(true).setMaterial(MaterialType.AMETHYST).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType MEDIUM_AMETHYST_BUD = StateTypes.builder().name("MEDIUM_AMETHYST_BUD").blastResistance(1.5f).hardness(1.5f).isBlocking(true).setMaterial(MaterialType.AMETHYST).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType LARGE_AMETHYST_BUD = StateTypes.builder().name("LARGE_AMETHYST_BUD").blastResistance(1.5f).hardness(1.5f).isBlocking(true).setMaterial(MaterialType.AMETHYST).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType AMETHYST_CLUSTER = StateTypes.builder().name("AMETHYST_CLUSTER").blastResistance(1.5f).hardness(1.5f).isBlocking(true).setMaterial(MaterialType.AMETHYST).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType POINTED_DRIPSTONE = StateTypes.builder().name("POINTED_DRIPSTONE").blastResistance(3.0f).hardness(1.5f).isBlocking(true).setMaterial(MaterialType.STONE).setPushReaction(PushReaction.DESTROY).isSolid(true).build();
    public static StateType WATER = StateTypes.builder().name("WATER").blastResistance(100.0f).hardness(100.0f).setMaterial(MaterialType.WATER).build();
    public static StateType LAVA = StateTypes.builder().name("LAVA").blastResistance(100.0f).hardness(100.0f).setMaterial(MaterialType.LAVA).build();
    public static StateType TALL_SEAGRASS = StateTypes.builder().name("TALL_SEAGRASS").setMaterial(MaterialType.REPLACEABLE_WATER_PLANT).build();
    public static StateType PISTON_HEAD = StateTypes.builder().name("PISTON_HEAD").blastResistance(1.5f).hardness(1.5f).isBlocking(true).isShapeExceedsCube(true).setMaterial(MaterialType.PISTON).isSolid(true).build();
    public static StateType MOVING_PISTON = StateTypes.builder().name("MOVING_PISTON").blastResistance(0.0f).hardness(-1.0f).isBlocking(true).setMaterial(MaterialType.PISTON).isSolid(true).build();
    public static StateType WALL_TORCH = StateTypes.builder().name("WALL_TORCH").setMaterial(MaterialType.DECORATION).build();
    public static StateType FIRE = StateTypes.builder().name("FIRE").setMaterial(MaterialType.FIRE).build();
    public static StateType SOUL_FIRE = StateTypes.builder().name("SOUL_FIRE").setMaterial(MaterialType.FIRE).build();
    public static StateType REDSTONE_WIRE = StateTypes.builder().name("REDSTONE_WIRE").setMaterial(MaterialType.DECORATION).build();
    public static StateType OAK_WALL_SIGN = StateTypes.builder().name("OAK_WALL_SIGN").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType SPRUCE_WALL_SIGN = StateTypes.builder().name("SPRUCE_WALL_SIGN").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType BIRCH_WALL_SIGN = StateTypes.builder().name("BIRCH_WALL_SIGN").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType ACACIA_WALL_SIGN = StateTypes.builder().name("ACACIA_WALL_SIGN").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType JUNGLE_WALL_SIGN = StateTypes.builder().name("JUNGLE_WALL_SIGN").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType DARK_OAK_WALL_SIGN = StateTypes.builder().name("DARK_OAK_WALL_SIGN").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType MANGROVE_WALL_SIGN = StateTypes.builder().name("MANGROVE_WALL_SIGN").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType REDSTONE_WALL_TORCH = StateTypes.builder().name("REDSTONE_WALL_TORCH").setMaterial(MaterialType.DECORATION).build();
    public static StateType SOUL_WALL_TORCH = StateTypes.builder().name("SOUL_WALL_TORCH").setMaterial(MaterialType.DECORATION).build();
    public static StateType NETHER_PORTAL = StateTypes.builder().name("NETHER_PORTAL").blastResistance(0.0f).hardness(-1.0f).setMaterial(MaterialType.PORTAL).build();
    public static StateType ATTACHED_PUMPKIN_STEM = StateTypes.builder().name("ATTACHED_PUMPKIN_STEM").setMaterial(MaterialType.PLANT).build();
    public static StateType ATTACHED_MELON_STEM = StateTypes.builder().name("ATTACHED_MELON_STEM").setMaterial(MaterialType.PLANT).build();
    public static StateType PUMPKIN_STEM = StateTypes.builder().name("PUMPKIN_STEM").setMaterial(MaterialType.PLANT).build();
    public static StateType MELON_STEM = StateTypes.builder().name("MELON_STEM").setMaterial(MaterialType.PLANT).build();
    public static StateType WATER_CAULDRON = StateTypes.builder().name("WATER_CAULDRON").blastResistance(2.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType LAVA_CAULDRON = StateTypes.builder().name("LAVA_CAULDRON").blastResistance(2.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType POWDER_SNOW_CAULDRON = StateTypes.builder().name("POWDER_SNOW_CAULDRON").blastResistance(2.0f).hardness(2.0f).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.METAL).isSolid(true).build();
    public static StateType END_PORTAL = StateTypes.builder().name("END_PORTAL").blastResistance(3600000.0f).hardness(-1.0f).setMaterial(MaterialType.PORTAL).build();
    public static StateType COCOA = StateTypes.builder().name("COCOA").blastResistance(3.0f).hardness(0.2f).setMaterial(MaterialType.PLANT).isSolid(true).build();
    public static StateType TRIPWIRE = StateTypes.builder().name("TRIPWIRE").setMaterial(MaterialType.DECORATION).build();
    public static StateType POTTED_OAK_SAPLING = StateTypes.builder().name("POTTED_OAK_SAPLING").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_SPRUCE_SAPLING = StateTypes.builder().name("POTTED_SPRUCE_SAPLING").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_BIRCH_SAPLING = StateTypes.builder().name("POTTED_BIRCH_SAPLING").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_JUNGLE_SAPLING = StateTypes.builder().name("POTTED_JUNGLE_SAPLING").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_ACACIA_SAPLING = StateTypes.builder().name("POTTED_ACACIA_SAPLING").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_DARK_OAK_SAPLING = StateTypes.builder().name("POTTED_DARK_OAK_SAPLING").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_MANGROVE_PROPAGULE = StateTypes.builder().name("POTTED_MANGROVE_PROPAGULE").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_FERN = StateTypes.builder().name("POTTED_FERN").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_DANDELION = StateTypes.builder().name("POTTED_DANDELION").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_POPPY = StateTypes.builder().name("POTTED_POPPY").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_BLUE_ORCHID = StateTypes.builder().name("POTTED_BLUE_ORCHID").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_ALLIUM = StateTypes.builder().name("POTTED_ALLIUM").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_AZURE_BLUET = StateTypes.builder().name("POTTED_AZURE_BLUET").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_RED_TULIP = StateTypes.builder().name("POTTED_RED_TULIP").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_ORANGE_TULIP = StateTypes.builder().name("POTTED_ORANGE_TULIP").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_WHITE_TULIP = StateTypes.builder().name("POTTED_WHITE_TULIP").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_PINK_TULIP = StateTypes.builder().name("POTTED_PINK_TULIP").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_OXEYE_DAISY = StateTypes.builder().name("POTTED_OXEYE_DAISY").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_CORNFLOWER = StateTypes.builder().name("POTTED_CORNFLOWER").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_LILY_OF_THE_VALLEY = StateTypes.builder().name("POTTED_LILY_OF_THE_VALLEY").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_WITHER_ROSE = StateTypes.builder().name("POTTED_WITHER_ROSE").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_RED_MUSHROOM = StateTypes.builder().name("POTTED_RED_MUSHROOM").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_BROWN_MUSHROOM = StateTypes.builder().name("POTTED_BROWN_MUSHROOM").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_DEAD_BUSH = StateTypes.builder().name("POTTED_DEAD_BUSH").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_CACTUS = StateTypes.builder().name("POTTED_CACTUS").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType CARROTS = StateTypes.builder().name("CARROTS").setMaterial(MaterialType.PLANT).build();
    public static StateType POTATOES = StateTypes.builder().name("POTATOES").setMaterial(MaterialType.PLANT).build();
    public static StateType SKELETON_WALL_SKULL = StateTypes.builder().name("SKELETON_WALL_SKULL").blastResistance(1.0f).hardness(1.0f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType WITHER_SKELETON_WALL_SKULL = StateTypes.builder().name("WITHER_SKELETON_WALL_SKULL").blastResistance(1.0f).hardness(1.0f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType ZOMBIE_WALL_HEAD = StateTypes.builder().name("ZOMBIE_WALL_HEAD").blastResistance(1.0f).hardness(1.0f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType PLAYER_WALL_HEAD = StateTypes.builder().name("PLAYER_WALL_HEAD").blastResistance(1.0f).hardness(1.0f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType CREEPER_WALL_HEAD = StateTypes.builder().name("CREEPER_WALL_HEAD").blastResistance(1.0f).hardness(1.0f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType DRAGON_WALL_HEAD = StateTypes.builder().name("DRAGON_WALL_HEAD").blastResistance(1.0f).hardness(1.0f).setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType WHITE_WALL_BANNER = StateTypes.builder().name("WHITE_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType ORANGE_WALL_BANNER = StateTypes.builder().name("ORANGE_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType MAGENTA_WALL_BANNER = StateTypes.builder().name("MAGENTA_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType LIGHT_BLUE_WALL_BANNER = StateTypes.builder().name("LIGHT_BLUE_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType YELLOW_WALL_BANNER = StateTypes.builder().name("YELLOW_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType LIME_WALL_BANNER = StateTypes.builder().name("LIME_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType PINK_WALL_BANNER = StateTypes.builder().name("PINK_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType GRAY_WALL_BANNER = StateTypes.builder().name("GRAY_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType LIGHT_GRAY_WALL_BANNER = StateTypes.builder().name("LIGHT_GRAY_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType CYAN_WALL_BANNER = StateTypes.builder().name("CYAN_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType PURPLE_WALL_BANNER = StateTypes.builder().name("PURPLE_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType BLUE_WALL_BANNER = StateTypes.builder().name("BLUE_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType BROWN_WALL_BANNER = StateTypes.builder().name("BROWN_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType GREEN_WALL_BANNER = StateTypes.builder().name("GREEN_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType RED_WALL_BANNER = StateTypes.builder().name("RED_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType BLACK_WALL_BANNER = StateTypes.builder().name("BLACK_WALL_BANNER").blastResistance(1.0f).hardness(1.0f).isFlammable(true).isSolid(false).isBlocking(true).setMaterial(MaterialType.WOOD).build();
    public static StateType BEETROOTS = StateTypes.builder().name("BEETROOTS").setMaterial(MaterialType.PLANT).build();
    public static StateType END_GATEWAY = StateTypes.builder().name("END_GATEWAY").blastResistance(3600000.0f).hardness(-1.0f).setMaterial(MaterialType.PORTAL).build();
    public static StateType FROSTED_ICE = StateTypes.builder().name("FROSTED_ICE").blastResistance(0.5f).hardness(0.5f).slipperiness(0.98f).isBlocking(true).setMaterial(MaterialType.ICE).isSolid(true).build();
    public static StateType KELP_PLANT = StateTypes.builder().name("KELP_PLANT").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType DEAD_TUBE_CORAL_WALL_FAN = StateTypes.builder().name("DEAD_TUBE_CORAL_WALL_FAN").isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).build();
    public static StateType DEAD_BRAIN_CORAL_WALL_FAN = StateTypes.builder().name("DEAD_BRAIN_CORAL_WALL_FAN").isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).build();
    public static StateType DEAD_BUBBLE_CORAL_WALL_FAN = StateTypes.builder().name("DEAD_BUBBLE_CORAL_WALL_FAN").isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).build();
    public static StateType DEAD_FIRE_CORAL_WALL_FAN = StateTypes.builder().name("DEAD_FIRE_CORAL_WALL_FAN").isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).build();
    public static StateType DEAD_HORN_CORAL_WALL_FAN = StateTypes.builder().name("DEAD_HORN_CORAL_WALL_FAN").isSolid(false).isBlocking(true).requiresCorrectTool(true).setMaterial(MaterialType.STONE).build();
    public static StateType TUBE_CORAL_WALL_FAN = StateTypes.builder().name("TUBE_CORAL_WALL_FAN").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType BRAIN_CORAL_WALL_FAN = StateTypes.builder().name("BRAIN_CORAL_WALL_FAN").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType BUBBLE_CORAL_WALL_FAN = StateTypes.builder().name("BUBBLE_CORAL_WALL_FAN").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType FIRE_CORAL_WALL_FAN = StateTypes.builder().name("FIRE_CORAL_WALL_FAN").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType HORN_CORAL_WALL_FAN = StateTypes.builder().name("HORN_CORAL_WALL_FAN").setMaterial(MaterialType.WATER_PLANT).build();
    public static StateType BAMBOO_SAPLING = StateTypes.builder().name("BAMBOO_SAPLING").blastResistance(1.0f).hardness(1.0f).isFlammable(true).setMaterial(MaterialType.BAMBOO_SAPLING).build();
    public static StateType POTTED_BAMBOO = StateTypes.builder().name("POTTED_BAMBOO").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType VOID_AIR = StateTypes.builder().name("VOID_AIR").isAir(true).setMaterial(MaterialType.AIR).build();
    public static StateType CAVE_AIR = StateTypes.builder().name("CAVE_AIR").isAir(true).setMaterial(MaterialType.AIR).build();
    public static StateType BUBBLE_COLUMN = StateTypes.builder().name("BUBBLE_COLUMN").setMaterial(MaterialType.BUBBLE_COLUMN).build();
    public static StateType SWEET_BERRY_BUSH = StateTypes.builder().name("SWEET_BERRY_BUSH").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType WEEPING_VINES_PLANT = StateTypes.builder().name("WEEPING_VINES_PLANT").setMaterial(MaterialType.PLANT).build();
    public static StateType TWISTING_VINES_PLANT = StateTypes.builder().name("TWISTING_VINES_PLANT").setMaterial(MaterialType.PLANT).build();
    public static StateType CRIMSON_WALL_SIGN = StateTypes.builder().name("CRIMSON_WALL_SIGN").blastResistance(1.0f).hardness(1.0f).isSolid(false).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).build();
    public static StateType WARPED_WALL_SIGN = StateTypes.builder().name("WARPED_WALL_SIGN").blastResistance(1.0f).hardness(1.0f).isSolid(false).isBlocking(true).setMaterial(MaterialType.NETHER_WOOD).build();
    public static StateType POTTED_CRIMSON_FUNGUS = StateTypes.builder().name("POTTED_CRIMSON_FUNGUS").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_WARPED_FUNGUS = StateTypes.builder().name("POTTED_WARPED_FUNGUS").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_CRIMSON_ROOTS = StateTypes.builder().name("POTTED_CRIMSON_ROOTS").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_WARPED_ROOTS = StateTypes.builder().name("POTTED_WARPED_ROOTS").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType CANDLE_CAKE = StateTypes.builder().name("CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType WHITE_CANDLE_CAKE = StateTypes.builder().name("WHITE_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType ORANGE_CANDLE_CAKE = StateTypes.builder().name("ORANGE_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType MAGENTA_CANDLE_CAKE = StateTypes.builder().name("MAGENTA_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType LIGHT_BLUE_CANDLE_CAKE = StateTypes.builder().name("LIGHT_BLUE_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType YELLOW_CANDLE_CAKE = StateTypes.builder().name("YELLOW_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType LIME_CANDLE_CAKE = StateTypes.builder().name("LIME_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType PINK_CANDLE_CAKE = StateTypes.builder().name("PINK_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType GRAY_CANDLE_CAKE = StateTypes.builder().name("GRAY_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType LIGHT_GRAY_CANDLE_CAKE = StateTypes.builder().name("LIGHT_GRAY_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType CYAN_CANDLE_CAKE = StateTypes.builder().name("CYAN_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType PURPLE_CANDLE_CAKE = StateTypes.builder().name("PURPLE_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType BLUE_CANDLE_CAKE = StateTypes.builder().name("BLUE_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType BROWN_CANDLE_CAKE = StateTypes.builder().name("BROWN_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType GREEN_CANDLE_CAKE = StateTypes.builder().name("GREEN_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType RED_CANDLE_CAKE = StateTypes.builder().name("RED_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType BLACK_CANDLE_CAKE = StateTypes.builder().name("BLACK_CANDLE_CAKE").blastResistance(0.5f).hardness(0.5f).isBlocking(true).setMaterial(MaterialType.CAKE).isSolid(true).build();
    public static StateType POWDER_SNOW = StateTypes.builder().name("POWDER_SNOW").blastResistance(0.25f).hardness(0.25f).setMaterial(MaterialType.POWDER_SNOW).isSolid(true).build();
    public static StateType CAVE_VINES = StateTypes.builder().name("CAVE_VINES").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType CAVE_VINES_PLANT = StateTypes.builder().name("CAVE_VINES_PLANT").isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType BIG_DRIPLEAF_STEM = StateTypes.builder().name("BIG_DRIPLEAF_STEM").blastResistance(0.1f).hardness(0.1f).isBurnable(true).setMaterial(MaterialType.PLANT).build();
    public static StateType POTTED_AZALEA_BUSH = StateTypes.builder().name("POTTED_AZALEA_BUSH").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    public static StateType POTTED_FLOWERING_AZALEA_BUSH = StateTypes.builder().name("POTTED_FLOWERING_AZALEA_BUSH").setMaterial(MaterialType.DECORATION).isSolid(true).build();
    // 1.19 states
    public static StateType MUD = StateTypes.builder().name("MUD").blastResistance(0.5f).hardness(0.5f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.DIRT).isSolid(true).build();
    public static StateType MANGROVE_PLANKS = StateTypes.builder().name("MANGROVE_PLANKS").blastResistance(3.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType MANGROVE_PROPAGULE = StateTypes.builder().name("MANGROVE_PROPAGULE").blastResistance(0.0f).hardness(0.0f).isOccluding(false).isBlocking(false).setMaterial(MaterialType.PLANT).isSolid(false).build();
    public static StateType MANGROVE_LOG = StateTypes.builder().name("MANGROVE_LOG").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType MANGROVE_ROOTS = StateTypes.builder().name("MANGROVE_ROOTS").blastResistance(0.7f).hardness(0.7f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType MUDDY_MANGROVE_ROOTS = StateTypes.builder().name("MUDDY_MANGROVE_ROOTS").blastResistance(0.7f).hardness(0.7f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.DIRT).isSolid(true).build();
    public static StateType STRIPPED_MANGROVE_LOG = StateTypes.builder().name("STRIPPED_MANGROVE_LOG").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType STRIPPED_MANGROVE_WOOD = StateTypes.builder().name("STRIPPED_MANGROVE_WOOD").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType MANGROVE_WOOD = StateTypes.builder().name("MANGROVE_WOOD").blastResistance(2.0f).hardness(2.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType MANGROVE_LEAVES = StateTypes.builder().name("MANGROVE_LEAVES").blastResistance(0.2f).hardness(0.2f).isOccluding(false).isBlocking(true).setMaterial(MaterialType.LEAVES).isSolid(true).build();
    public static StateType MANGROVE_SLAB = StateTypes.builder().name("MANGROVE_SLAB").blastResistance(3.0f).hardness(2.0f).isOccluding(false).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType MUD_BRICK_SLAB = StateTypes.builder().name("MUD_BRICK_SLAB").blastResistance(3.0f).hardness(1.5f).isOccluding(false).isBlocking(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType MANGROVE_FENCE = StateTypes.builder().name("MANGROVE_FENCE").blastResistance(3.0f).hardness(2.0f).isOccluding(false).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType PACKED_MUD = StateTypes.builder().name("PACKED_MUD").blastResistance(3.0f).hardness(1.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.DIRT).isSolid(true).build();
    public static StateType MUD_BRICKS = StateTypes.builder().name("MUD_BRICKS").blastResistance(3.0f).hardness(1.5f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType REINFORCED_DEEPSLATE = StateTypes.builder().name("REINFORCED_DEEPSLATE").blastResistance(1200.0f).hardness(55.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType MUD_BRICK_STAIRS = StateTypes.builder().name("MUD_BRICK_STAIRS").blastResistance(3.0f).hardness(1.5f).isOccluding(false).isBlocking(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType SCULK = StateTypes.builder().name("SCULK").blastResistance(0.2f).hardness(0.2f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SCULK).isSolid(true).build();
    public static StateType SCULK_VEIN = StateTypes.builder().name("SCULK_VEIN").blastResistance(0.2f).hardness(0.2f).isOccluding(false).isBlocking(false).setMaterial(MaterialType.SCULK).isSolid(true).build();
    public static StateType SCULK_CATALYST = StateTypes.builder().name("SCULK_CATALYST").blastResistance(3.0f).hardness(3.0f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.SCULK).isSolid(true).build();
    public static StateType SCULK_SHRIEKER = StateTypes.builder().name("SCULK_SHRIEKER").blastResistance(3.0f).hardness(3.0f).isOccluding(false).isBlocking(true).setMaterial(MaterialType.SCULK).isSolid(true).build();
    public static StateType MUD_BRICK_WALL = StateTypes.builder().name("MUD_BRICK_WALL").blastResistance(3.0f).hardness(1.5f).isOccluding(false).isBlocking(true).setMaterial(MaterialType.STONE).isSolid(true).build();
    public static StateType MANGROVE_BUTTON = StateTypes.builder().name("MANGROVE_BUTTON").blastResistance(0.5f).hardness(0.5f).isOccluding(false).isBlocking(false).setMaterial(MaterialType.DECORATION).isSolid(false).build();
    public static StateType MANGROVE_PRESSURE_PLATE = StateTypes.builder().name("MANGROVE_PRESSURE_PLATE").blastResistance(0.5f).hardness(0.5f).isOccluding(false).isBlocking(false).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType MANGROVE_DOOR = StateTypes.builder().name("MANGROVE_DOOR").blastResistance(3.0f).hardness(3.0f).isOccluding(false).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType MANGROVE_TRAPDOOR = StateTypes.builder().name("MANGROVE_TRAPDOOR").blastResistance(3.0f).hardness(3.0f).isOccluding(false).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType MANGROVE_FENCE_GATE = StateTypes.builder().name("MANGROVE_FENCE_GATE").blastResistance(3.0f).hardness(2.0f).isOccluding(false).isBlocking(true).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType MANGROVE_SIGN = StateTypes.builder().name("MANGROVE_SIGN").blastResistance(1.0f).hardness(1.0f).isOccluding(false).isBlocking(false).setMaterial(MaterialType.WOOD).isSolid(true).build();
    public static StateType OCHRE_FROGLIGHT = StateTypes.builder().name("OCHRE_FROGLIGHT").blastResistance(0.3f).hardness(0.3f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.FROGLIGHT).isSolid(true).build();
    public static StateType VERDANT_FROGLIGHT = StateTypes.builder().name("VERDANT_FROGLIGHT").blastResistance(0.3f).hardness(0.3f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.FROGLIGHT).isSolid(true).build();
    public static StateType PEARLESCENT_FROGLIGHT = StateTypes.builder().name("PEARLESCENT_FROGLIGHT").blastResistance(0.3f).hardness(0.3f).isOccluding(true).isBlocking(true).setMaterial(MaterialType.FROGLIGHT).isSolid(true).build();
    public static StateType FROGSPAWN = StateTypes.builder().name("FROGSPAWN").blastResistance(0.0f).hardness(0.0f).isOccluding(false).isBlocking(false).setMaterial(MaterialType.FROGSPAWN).isSolid(false).build();

    public static Builder builder() {
        return new Builder();
    }

    public static Collection<StateType> values() {
        return BY_NAME.values();
    }

    public static StateType getByName(String blockString) {
        return BY_NAME.get(blockString.toLowerCase(Locale.ROOT));
    }

    public static class Builder {
        String name;
        float blastResistance = 0F;
        float hardness = 0F;
        float slipperiness = 0.6F;
        float friction = 0.6F;
        float speed = 1F;
        boolean hasGravity = false;
        boolean isAir = false;
        boolean isBurnable;
        boolean isFlammable;
        boolean isOccluding;
        boolean isSolid;
        boolean isLiquid;
        boolean isBlocking;
        boolean requiresCorrectTool = false;
        boolean isReplaceable;
        boolean isShapeExceedsCube;
        PushReaction pushReaction = null;
        MaterialType materialType;

        public Builder name(String name) {
            this.name = name.toLowerCase(Locale.ROOT); // TODO: Rethink whether all names are lowercase
            return this;
        }

        public Builder blastResistance(float blastResistance) {
            this.blastResistance = blastResistance;
            return this;
        }

        public Builder hardness(float hardness) {
            this.hardness = hardness;
            return this;
        }

        public Builder slipperiness(float slipperiness) {
            this.slipperiness = slipperiness;
            return this;
        }

        public Builder friction(float friction) {
            this.friction = friction;
            return this;
        }

        public Builder speed(float speed) {
            this.speed = speed;
            return this;
        }

        public Builder hasGravity(boolean hasGravity) {
            this.hasGravity = hasGravity;
            return this;
        }

        public Builder isAir(boolean isAir) {
            this.isAir = isAir;
            return this;
        }

        public Builder isBurnable(boolean isBurnable) {
            this.isBurnable = isBurnable;
            return this;
        }

        public Builder isFlammable(boolean isFlammable) {
            this.isFlammable = isFlammable;
            return this;
        }

        public Builder isOccluding(boolean isOccluding) {
            this.isOccluding = isOccluding;
            return this;
        }

        public Builder isSolid(boolean isSolid) {
            this.isSolid = isSolid;
            return this;
        }

        public Builder isLiquid(boolean isLiquid) {
            this.isLiquid = isLiquid;
            return this;
        }

        public Builder isBlocking(boolean isBlocking) {
            this.isBlocking = isBlocking;
            return this;
        }

        public Builder requiresCorrectTool(boolean requiresCorrectTool) {
            this.requiresCorrectTool = requiresCorrectTool;
            return this;
        }

        public Builder pushReaction(PushReaction pushReaction) {
            this.pushReaction = pushReaction;
            return this;
        }

        public Builder setMaterial(MaterialType materialType) {
            this.materialType = materialType;

            if (pushReaction != null) return this;

            switch (materialType) {
                case AIR:
                case STRUCTURAL_AIR:
                case REPLACEABLE_PLANT:
                case REPLACEABLE_FIREPROOF_PLANT:
                case REPLACEABLE_WATER_PLANT:
                case WATER:
                case BUBBLE_COLUMN:
                case LAVA:
                case TOP_SNOW:
                case FIRE:
                    this.isReplaceable = true;
            }

            switch (materialType) {
                case PLANT:
                case WATER_PLANT:
                case REPLACEABLE_PLANT:
                case REPLACEABLE_FIREPROOF_PLANT:
                case REPLACEABLE_WATER_PLANT:
                case WATER:
                case BUBBLE_COLUMN:
                case LAVA:
                case TOP_SNOW:
                case FIRE:
                case DECORATION:
                case WEB:
                case BAMBOO_SAPLING:
                case BAMBOO:
                case LEAVES:
                case CACTUS:
                case MOSS:
                case VEGETABLE:
                case EGG:
                case CAKE:
                case SHULKER_SHELL:
                case FROGSPAWN:
                    this.pushReaction = PushReaction.DESTROY;
                    return this;
                case PORTAL:
                case HEAVY_METAL:
                case BARRIER:
                case PISTON:
                    this.pushReaction = PushReaction.BLOCK;
                    return this;
            }
            return this;
        }

        public Builder setPushReaction(PushReaction pushReaction) {
            this.pushReaction = pushReaction;
            return this;
        }

        public Builder isShapeExceedsCube(boolean b) {
            this.isShapeExceedsCube = b;
            return this;
        }

        public StateType build() {
            StateType type = new StateType(name, blastResistance, hardness, slipperiness, friction, speed, hasGravity, isAir, isBurnable, isFlammable, isOccluding, isSolid, isLiquid, isBlocking, requiresCorrectTool, isReplaceable, isShapeExceedsCube, pushReaction, materialType);
            BY_NAME.put(name, type);
            return type;
        }
    }
}