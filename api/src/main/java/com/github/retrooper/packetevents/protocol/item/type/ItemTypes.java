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

package com.github.retrooper.packetevents.protocol.item.type;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.TypesBuilder;
import com.github.retrooper.packetevents.util.TypesBuilderData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ItemTypes {
    private static final Map<String, ItemType> ITEM_TYPE_MAP = new HashMap<>();
    private static final Map<Byte, Map<Integer, ItemType>> ITEM_TYPE_ID_MAP = new HashMap<>();
    private static final Map<StateType, ItemType> HELD_TO_PLACED_MAP = new HashMap<>();
    private static final TypesBuilder TYPES_BUILDER = new TypesBuilder("item/item_type_mappings",
            ClientVersion.V_1_12,
            ClientVersion.V_1_13,
            ClientVersion.V_1_13_2,
            ClientVersion.V_1_14,
            ClientVersion.V_1_15,
            ClientVersion.V_1_16,
            ClientVersion.V_1_16_2,
            ClientVersion.V_1_17,
            ClientVersion.V_1_18);

    public static final ItemType GILDED_BLACKSTONE = builder("gilded_blackstone").setMaxAmount(64).setPlacedType(StateTypes.GILDED_BLACKSTONE).build();
    public static final ItemType NETHER_BRICK_SLAB = builder("nether_brick_slab").setMaxAmount(64).setPlacedType(StateTypes.NETHER_BRICK_SLAB).build();
    public static final ItemType ANDESITE_SLAB = builder("andesite_slab").setMaxAmount(64).setPlacedType(StateTypes.ANDESITE_SLAB).build();
    public static final ItemType EGG = builder("egg").setMaxAmount(16).build();
    public static final ItemType MUSIC_DISC_STAL = builder("music_disc_stal").setMaxAmount(1).setAttributes(ItemAttribute.MUSIC_DISC).build();
    public static final ItemType PIGLIN_BRUTE_SPAWN_EGG = builder("piglin_brute_spawn_egg").setMaxAmount(64).build();
    public static final ItemType BIRCH_STAIRS = builder("birch_stairs").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BIRCH_STAIRS).build();
    public static final ItemType SPRUCE_SIGN = builder("spruce_sign").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.SPRUCE_SIGN).build();
    public static final ItemType DRAGON_HEAD = builder("dragon_head").setMaxAmount(64).setPlacedType(StateTypes.DRAGON_HEAD).build();
    public static final ItemType HONEY_BLOCK = builder("honey_block").setMaxAmount(64).setPlacedType(StateTypes.HONEY_BLOCK).build();
    public static final ItemType GREEN_DYE = builder("green_dye").setMaxAmount(64).build();
    public static final ItemType DIAMOND_ORE = builder("diamond_ore").setMaxAmount(64).setPlacedType(StateTypes.DIAMOND_ORE).build();
    public static final ItemType DEBUG_STICK = builder("debug_stick").setMaxAmount(1).build();
    public static final ItemType BLACK_STAINED_GLASS_PANE = builder("black_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.BLACK_STAINED_GLASS_PANE).build();
    public static final ItemType SPRUCE_FENCE_GATE = builder("spruce_fence_gate").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.SPRUCE_FENCE_GATE).build();
    public static final ItemType AZURE_BLUET = builder("azure_bluet").setMaxAmount(64).setPlacedType(StateTypes.AZURE_BLUET).build();
    public static final ItemType SLIME_BALL = builder("slime_ball").setMaxAmount(64).build();
    public static final ItemType RABBIT = builder("rabbit").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType AMETHYST_CLUSTER = builder("amethyst_cluster").setMaxAmount(64).setPlacedType(StateTypes.AMETHYST_CLUSTER).build();
    public static final ItemType PRISMARINE_BRICK_SLAB = builder("prismarine_brick_slab").setMaxAmount(64).setPlacedType(StateTypes.PRISMARINE_BRICK_SLAB).build();
    public static final ItemType DRAGON_EGG = builder("dragon_egg").setMaxAmount(64).setPlacedType(StateTypes.DRAGON_EGG).build();
    public static final ItemType PARROT_SPAWN_EGG = builder("parrot_spawn_egg").setMaxAmount(64).build();
    public static final ItemType WEATHERED_CUT_COPPER_SLAB = builder("weathered_cut_copper_slab").setMaxAmount(64).setPlacedType(StateTypes.WEATHERED_CUT_COPPER_SLAB).build();
    public static final ItemType LIGHT_GRAY_STAINED_GLASS_PANE = builder("light_gray_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.LIGHT_GRAY_STAINED_GLASS_PANE).build();
    public static final ItemType SCAFFOLDING = builder("scaffolding").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.SCAFFOLDING).build();
    public static final ItemType WARPED_PRESSURE_PLATE = builder("warped_pressure_plate").setMaxAmount(64).setPlacedType(StateTypes.WARPED_PRESSURE_PLATE).build();
    public static final ItemType MULE_SPAWN_EGG = builder("mule_spawn_egg").setMaxAmount(64).build();
    public static final ItemType SUSPICIOUS_STEW = builder("suspicious_stew").setMaxAmount(1).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType MAGENTA_STAINED_GLASS_PANE = builder("magenta_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.MAGENTA_STAINED_GLASS_PANE).build();
    public static final ItemType LARGE_FERN = builder("large_fern").setMaxAmount(64).setPlacedType(StateTypes.LARGE_FERN).build();
    public static final ItemType LIGHT_BLUE_CONCRETE = builder("light_blue_concrete").setMaxAmount(64).setPlacedType(StateTypes.LIGHT_BLUE_CONCRETE).build();
    public static final ItemType LAPIS_ORE = builder("lapis_ore").setMaxAmount(64).setPlacedType(StateTypes.LAPIS_ORE).build();
    public static final ItemType LIGHT_BLUE_BED = builder("light_blue_bed").setMaxAmount(1).setPlacedType(StateTypes.LIGHT_BLUE_BED).build();
    public static final ItemType BIRCH_PRESSURE_PLATE = builder("birch_pressure_plate").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BIRCH_PRESSURE_PLATE).build();
    public static final ItemType HONEYCOMB = builder("honeycomb").setMaxAmount(64).build();
    public static final ItemType GOLD_BLOCK = builder("gold_block").setMaxAmount(64).setPlacedType(StateTypes.GOLD_BLOCK).build();
    public static final ItemType WRITABLE_BOOK = builder("writable_book").setMaxAmount(1).build();
    public static final ItemType DRIPSTONE_BLOCK = builder("dripstone_block").setMaxAmount(64).setPlacedType(StateTypes.DRIPSTONE_BLOCK).build();
    public static final ItemType ACACIA_LOG = builder("acacia_log").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ACACIA_LOG).build();
    public static final ItemType TROPICAL_FISH_SPAWN_EGG = builder("tropical_fish_spawn_egg").setMaxAmount(64).build();
    public static final ItemType ZOMBIE_SPAWN_EGG = builder("zombie_spawn_egg").setMaxAmount(64).build();
    public static final ItemType GLOW_ITEM_FRAME = builder("glow_item_frame").setMaxAmount(64).build();
    public static final ItemType WHITE_DYE = builder("white_dye").setMaxAmount(64).build();
    public static final ItemType REDSTONE = builder("redstone").setMaxAmount(64).setPlacedType(StateTypes.REDSTONE_WIRE).build();
    public static final ItemType BONE_BLOCK = builder("bone_block").setMaxAmount(64).setPlacedType(StateTypes.BONE_BLOCK).build();
    public static final ItemType DEAD_TUBE_CORAL_FAN = builder("dead_tube_coral_fan").setMaxAmount(64).setPlacedType(StateTypes.DEAD_TUBE_CORAL_FAN).build();
    public static final ItemType TURTLE_SPAWN_EGG = builder("turtle_spawn_egg").setMaxAmount(64).build();
    public static final ItemType BIRCH_FENCE = builder("birch_fence").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BIRCH_FENCE).build();
    public static final ItemType CYAN_TERRACOTTA = builder("cyan_terracotta").setMaxAmount(64).setPlacedType(StateTypes.CYAN_TERRACOTTA).build();
    public static final ItemType PRISMARINE_STAIRS = builder("prismarine_stairs").setMaxAmount(64).setPlacedType(StateTypes.PRISMARINE_STAIRS).build();
    public static final ItemType IRON_BOOTS = builder("iron_boots").setMaxAmount(1).setMaxDurability(195).build();
    public static final ItemType BROWN_CONCRETE_POWDER = builder("brown_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.BROWN_CONCRETE_POWDER).build();
    public static final ItemType END_STONE = builder("end_stone").setMaxAmount(64).setPlacedType(StateTypes.END_STONE).build();
    public static final ItemType GLISTERING_MELON_SLICE = builder("glistering_melon_slice").setMaxAmount(64).build();
    public static final ItemType NETHER_SPROUTS = builder("nether_sprouts").setMaxAmount(64).setPlacedType(StateTypes.NETHER_SPROUTS).build();
    public static final ItemType GREEN_CONCRETE = builder("green_concrete").setMaxAmount(64).setPlacedType(StateTypes.GREEN_CONCRETE).build();
    public static final ItemType ACACIA_DOOR = builder("acacia_door").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ACACIA_DOOR).build();
    public static final ItemType GOLDEN_AXE = builder("golden_axe").setMaxAmount(1).setAttributes(ItemAttribute.AXE, ItemAttribute.GOLD_TIER).setMaxDurability(32).build();
    public static final ItemType WHITE_STAINED_GLASS_PANE = builder("white_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.WHITE_STAINED_GLASS_PANE).build();
    public static final ItemType COBBLESTONE_WALL = builder("cobblestone_wall").setMaxAmount(64).setPlacedType(StateTypes.COBBLESTONE_WALL).build();
    public static final ItemType WHITE_GLAZED_TERRACOTTA = builder("white_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.WHITE_GLAZED_TERRACOTTA).build();
    public static final ItemType END_STONE_BRICK_WALL = builder("end_stone_brick_wall").setMaxAmount(64).setPlacedType(StateTypes.END_STONE_BRICK_WALL).build();
    public static final ItemType COOKED_RABBIT = builder("cooked_rabbit").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType RED_MUSHROOM_BLOCK = builder("red_mushroom_block").setMaxAmount(64).setPlacedType(StateTypes.RED_MUSHROOM_BLOCK).build();
    public static final ItemType CRIMSON_SLAB = builder("crimson_slab").setMaxAmount(64).setPlacedType(StateTypes.CRIMSON_SLAB).build();
    public static final ItemType AMETHYST_SHARD = builder("amethyst_shard").setMaxAmount(64).build();
    public static final ItemType CHARCOAL = builder("charcoal").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).build();
    public static final ItemType NETHER_WART_BLOCK = builder("nether_wart_block").setMaxAmount(64).setPlacedType(StateTypes.NETHER_WART_BLOCK).build();
    public static final ItemType DEEPSLATE_GOLD_ORE = builder("deepslate_gold_ore").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_GOLD_ORE).build();
    public static final ItemType INFESTED_STONE = builder("infested_stone").setMaxAmount(64).setPlacedType(StateTypes.INFESTED_STONE).build();
    public static final ItemType STRIPPED_OAK_LOG = builder("stripped_oak_log").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.STRIPPED_OAK_LOG).build();
    public static final ItemType LIGHT_GRAY_CONCRETE_POWDER = builder("light_gray_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.LIGHT_GRAY_CONCRETE_POWDER).build();
    public static final ItemType COOKED_PORKCHOP = builder("cooked_porkchop").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType NETHERITE_HELMET = builder("netherite_helmet").setMaxAmount(1).setAttributes(ItemAttribute.FIRE_RESISTANT).build();
    public static final ItemType BLACK_CANDLE = builder("black_candle").setMaxAmount(64).setPlacedType(StateTypes.BLACK_CANDLE).build();
    public static final ItemType CYAN_CONCRETE_POWDER = builder("cyan_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.CYAN_CONCRETE_POWDER).build();
    public static final ItemType SADDLE = builder("saddle").setMaxAmount(1).build();
    public static final ItemType OAK_SIGN = builder("oak_sign").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.OAK_SIGN).build();
    public static final ItemType REDSTONE_ORE = builder("redstone_ore").setMaxAmount(64).setPlacedType(StateTypes.REDSTONE_ORE).build();
    public static final ItemType NETHER_GOLD_ORE = builder("nether_gold_ore").setMaxAmount(64).setPlacedType(StateTypes.NETHER_GOLD_ORE).build();
    public static final ItemType HORN_CORAL_FAN = builder("horn_coral_fan").setMaxAmount(64).setPlacedType(StateTypes.HORN_CORAL_FAN).build();
    public static final ItemType STRIPPED_WARPED_HYPHAE = builder("stripped_warped_hyphae").setMaxAmount(64).setPlacedType(StateTypes.STRIPPED_WARPED_HYPHAE).build();
    public static final ItemType COOKED_BEEF = builder("cooked_beef").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType DEEPSLATE_EMERALD_ORE = builder("deepslate_emerald_ore").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_EMERALD_ORE).build();
    public static final ItemType FARMLAND = builder("farmland").setMaxAmount(64).setPlacedType(StateTypes.FARMLAND).build();
    public static final ItemType BLACK_CONCRETE = builder("black_concrete").setMaxAmount(64).setPlacedType(StateTypes.BLACK_CONCRETE).build();
    public static final ItemType CHISELED_DEEPSLATE = builder("chiseled_deepslate").setMaxAmount(64).setPlacedType(StateTypes.CHISELED_DEEPSLATE).build();
    public static final ItemType RED_WOOL = builder("red_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.RED_WOOL).build();
    public static final ItemType WAXED_CUT_COPPER_SLAB = builder("waxed_cut_copper_slab").setMaxAmount(64).setPlacedType(StateTypes.WAXED_CUT_COPPER_SLAB).build();
    public static final ItemType BLACK_WOOL = builder("black_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BLACK_WOOL).build();
    public static final ItemType GOLD_INGOT = builder("gold_ingot").setMaxAmount(64).build();
    public static final ItemType CRACKED_DEEPSLATE_BRICKS = builder("cracked_deepslate_bricks").setMaxAmount(64).setPlacedType(StateTypes.CRACKED_DEEPSLATE_BRICKS).build();
    public static final ItemType STONE_BUTTON = builder("stone_button").setMaxAmount(64).setPlacedType(StateTypes.STONE_BUTTON).build();
    public static final ItemType MELON = builder("melon").setMaxAmount(64).setPlacedType(StateTypes.MELON).build();
    public static final ItemType INFESTED_CHISELED_STONE_BRICKS = builder("infested_chiseled_stone_bricks").setMaxAmount(64).setPlacedType(StateTypes.INFESTED_CHISELED_STONE_BRICKS).build();
    public static final ItemType MUSIC_DISC_STRAD = builder("music_disc_strad").setMaxAmount(1).setAttributes(ItemAttribute.MUSIC_DISC).build();
    public static final ItemType STRUCTURE_BLOCK = builder("structure_block").setMaxAmount(64).setPlacedType(StateTypes.STRUCTURE_BLOCK).build();
    public static final ItemType STICKY_PISTON = builder("sticky_piston").setMaxAmount(64).setPlacedType(StateTypes.STICKY_PISTON).build();
    public static final ItemType GRAY_STAINED_GLASS = builder("gray_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.GRAY_STAINED_GLASS).build();
    public static final ItemType LIGHT_GRAY_SHULKER_BOX = builder("light_gray_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.LIGHT_GRAY_SHULKER_BOX).build();
    public static final ItemType DARK_OAK_BUTTON = builder("dark_oak_button").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DARK_OAK_BUTTON).build();
    public static final ItemType NETHERITE_AXE = builder("netherite_axe").setMaxAmount(1).setAttributes(ItemAttribute.AXE, ItemAttribute.FIRE_RESISTANT, ItemAttribute.NETHERITE_TIER).setMaxDurability(2031).build();
    public static final ItemType SAND = builder("sand").setMaxAmount(64).setPlacedType(StateTypes.SAND).build();
    public static final ItemType POLISHED_GRANITE_SLAB = builder("polished_granite_slab").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_GRANITE_SLAB).build();
    public static final ItemType DARK_OAK_DOOR = builder("dark_oak_door").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DARK_OAK_DOOR).build();
    public static final ItemType MOJANG_BANNER_PATTERN = builder("mojang_banner_pattern").setMaxAmount(1).build();
    public static final ItemType BEACON = builder("beacon").setMaxAmount(64).setPlacedType(StateTypes.BEACON).build();
    public static final ItemType BIRCH_WOOD = builder("birch_wood").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BIRCH_WOOD).build();
    public static final ItemType MUSHROOM_STEW = builder("mushroom_stew").setMaxAmount(1).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType FLINT = builder("flint").setMaxAmount(64).build();
    public static final ItemType SMOOTH_SANDSTONE_SLAB = builder("smooth_sandstone_slab").setMaxAmount(64).setPlacedType(StateTypes.SMOOTH_SANDSTONE_SLAB).build();
    public static final ItemType WARPED_PLANKS = builder("warped_planks").setMaxAmount(64).setPlacedType(StateTypes.WARPED_PLANKS).build();
    public static final ItemType MUSHROOM_STEM = builder("mushroom_stem").setMaxAmount(64).setPlacedType(StateTypes.MUSHROOM_STEM).build();
    public static final ItemType EMERALD = builder("emerald").setMaxAmount(64).build();
    public static final ItemType BLACKSTONE = builder("blackstone").setMaxAmount(64).setPlacedType(StateTypes.BLACKSTONE).build();
    public static final ItemType HOGLIN_SPAWN_EGG = builder("hoglin_spawn_egg").setMaxAmount(64).build();
    public static final ItemType DEAD_BRAIN_CORAL_BLOCK = builder("dead_brain_coral_block").setMaxAmount(64).setPlacedType(StateTypes.DEAD_BRAIN_CORAL_BLOCK).build();
    public static final ItemType OXIDIZED_COPPER = builder("oxidized_copper").setMaxAmount(64).setPlacedType(StateTypes.OXIDIZED_COPPER).build();
    public static final ItemType SHULKER_SPAWN_EGG = builder("shulker_spawn_egg").setMaxAmount(64).build();
    public static final ItemType BEEHIVE = builder("beehive").setMaxAmount(64).setPlacedType(StateTypes.BEEHIVE).build();
    public static final ItemType POLISHED_BASALT = builder("polished_basalt").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_BASALT).build();
    public static final ItemType PURPLE_WOOL = builder("purple_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.PURPLE_WOOL).build();
    public static final ItemType PINK_GLAZED_TERRACOTTA = builder("pink_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.PINK_GLAZED_TERRACOTTA).build();
    public static final ItemType CHORUS_FLOWER = builder("chorus_flower").setMaxAmount(64).setPlacedType(StateTypes.CHORUS_FLOWER).build();
    public static final ItemType LILAC = builder("lilac").setMaxAmount(64).setPlacedType(StateTypes.LILAC).build();
    public static final ItemType CRACKED_DEEPSLATE_TILES = builder("cracked_deepslate_tiles").setMaxAmount(64).setPlacedType(StateTypes.CRACKED_DEEPSLATE_TILES).build();
    public static final ItemType SHEEP_SPAWN_EGG = builder("sheep_spawn_egg").setMaxAmount(64).build();
    public static final ItemType SMALL_DRIPLEAF = builder("small_dripleaf").setMaxAmount(64).setPlacedType(StateTypes.SMALL_DRIPLEAF).build();
    public static final ItemType SOUL_TORCH = builder("soul_torch").setMaxAmount(64).setPlacedType(StateTypes.SOUL_TORCH).build();
    public static final ItemType POLISHED_BLACKSTONE_BRICK_STAIRS = builder("polished_blackstone_brick_stairs").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_BLACKSTONE_BRICK_STAIRS).build();
    public static final ItemType SPRUCE_FENCE = builder("spruce_fence").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.SPRUCE_FENCE).build();
    public static final ItemType COAL_BLOCK = builder("coal_block").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.COAL_BLOCK).build();
    public static final ItemType STRIPPED_CRIMSON_HYPHAE = builder("stripped_crimson_hyphae").setMaxAmount(64).setPlacedType(StateTypes.STRIPPED_CRIMSON_HYPHAE).build();
    public static final ItemType WOODEN_PICKAXE = builder("wooden_pickaxe").setMaxAmount(1).setAttributes(ItemAttribute.PICKAXE, ItemAttribute.WOOD_TIER, ItemAttribute.FUEL).setMaxDurability(59).build();
    public static final ItemType BIRCH_LEAVES = builder("birch_leaves").setMaxAmount(64).setPlacedType(StateTypes.BIRCH_LEAVES).build();
    public static final ItemType DIAMOND_PICKAXE = builder("diamond_pickaxe").setMaxAmount(1).setAttributes(ItemAttribute.PICKAXE, ItemAttribute.DIAMOND_TIER).setMaxDurability(1561).build();
    public static final ItemType FLOWER_POT = builder("flower_pot").setMaxAmount(64).setPlacedType(StateTypes.FLOWER_POT).build();
    public static final ItemType ACACIA_BUTTON = builder("acacia_button").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ACACIA_BUTTON).build();
    public static final ItemType STRIPPED_DARK_OAK_WOOD = builder("stripped_dark_oak_wood").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.STRIPPED_DARK_OAK_WOOD).build();
    public static final ItemType PINK_TERRACOTTA = builder("pink_terracotta").setMaxAmount(64).setPlacedType(StateTypes.PINK_TERRACOTTA).build();
    public static final ItemType PURPLE_CANDLE = builder("purple_candle").setMaxAmount(64).setPlacedType(StateTypes.PURPLE_CANDLE).build();
    public static final ItemType MAGENTA_TERRACOTTA = builder("magenta_terracotta").setMaxAmount(64).setPlacedType(StateTypes.MAGENTA_TERRACOTTA).build();
    public static final ItemType DEEPSLATE_COPPER_ORE = builder("deepslate_copper_ore").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_COPPER_ORE).build();
    public static final ItemType GRAY_DYE = builder("gray_dye").setMaxAmount(64).build();
    public static final ItemType BLACK_SHULKER_BOX = builder("black_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.BLACK_SHULKER_BOX).build();
    public static final ItemType OCELOT_SPAWN_EGG = builder("ocelot_spawn_egg").setMaxAmount(64).build();
    public static final ItemType WAXED_EXPOSED_CUT_COPPER_STAIRS = builder("waxed_exposed_cut_copper_stairs").setMaxAmount(64).setPlacedType(StateTypes.WAXED_EXPOSED_CUT_COPPER_STAIRS).build();
    public static final ItemType POLISHED_BLACKSTONE_WALL = builder("polished_blackstone_wall").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_BLACKSTONE_WALL).build();
    public static final ItemType BRAIN_CORAL_FAN = builder("brain_coral_fan").setMaxAmount(64).setPlacedType(StateTypes.BRAIN_CORAL_FAN).build();
    public static final ItemType RED_NETHER_BRICK_SLAB = builder("red_nether_brick_slab").setMaxAmount(64).setPlacedType(StateTypes.RED_NETHER_BRICK_SLAB).build();
    public static final ItemType SUGAR_CANE = builder("sugar_cane").setMaxAmount(64).setPlacedType(StateTypes.SUGAR_CANE).build();
    public static final ItemType FLOWERING_AZALEA_LEAVES = builder("flowering_azalea_leaves").setMaxAmount(64).setPlacedType(StateTypes.FLOWERING_AZALEA_LEAVES).build();
    public static final ItemType TALL_GRASS = builder("tall_grass").setMaxAmount(64).setPlacedType(StateTypes.TALL_GRASS).build();
    public static final ItemType ORANGE_STAINED_GLASS = builder("orange_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.ORANGE_STAINED_GLASS).build();
    public static final ItemType MAGENTA_CONCRETE = builder("magenta_concrete").setMaxAmount(64).setPlacedType(StateTypes.MAGENTA_CONCRETE).build();
    public static final ItemType CHAIN_COMMAND_BLOCK = builder("chain_command_block").setMaxAmount(64).setPlacedType(StateTypes.CHAIN_COMMAND_BLOCK).build();
    public static final ItemType IRON_CHESTPLATE = builder("iron_chestplate").setMaxAmount(1).setMaxDurability(240).build();
    public static final ItemType WEEPING_VINES = builder("weeping_vines").setMaxAmount(64).setPlacedType(StateTypes.WEEPING_VINES).build();
    public static final ItemType OXIDIZED_CUT_COPPER_SLAB = builder("oxidized_cut_copper_slab").setMaxAmount(64).setPlacedType(StateTypes.OXIDIZED_CUT_COPPER_SLAB).build();
    public static final ItemType GLOWSTONE = builder("glowstone").setMaxAmount(64).setPlacedType(StateTypes.GLOWSTONE).build();
    public static final ItemType SNOW_BLOCK = builder("snow_block").setMaxAmount(64).setPlacedType(StateTypes.SNOW_BLOCK).build();
    public static final ItemType GREEN_STAINED_GLASS = builder("green_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.GREEN_STAINED_GLASS).build();
    public static final ItemType PRISMARINE_BRICKS = builder("prismarine_bricks").setMaxAmount(64).setPlacedType(StateTypes.PRISMARINE_BRICKS).build();
    public static final ItemType WHITE_TULIP = builder("white_tulip").setMaxAmount(64).setPlacedType(StateTypes.WHITE_TULIP).build();
    public static final ItemType IRON_SWORD = builder("iron_sword").setMaxAmount(1).setAttributes(ItemAttribute.IRON_TIER, ItemAttribute.SWORD).setMaxDurability(250).build();
    public static final ItemType COPPER_BLOCK = builder("copper_block").setMaxAmount(64).setPlacedType(StateTypes.COPPER_BLOCK).build();
    public static final ItemType MAGENTA_BED = builder("magenta_bed").setMaxAmount(1).setPlacedType(StateTypes.MAGENTA_BED).build();
    public static final ItemType WARPED_NYLIUM = builder("warped_nylium").setMaxAmount(64).setPlacedType(StateTypes.WARPED_NYLIUM).build();
    public static final ItemType DIORITE = builder("diorite").setMaxAmount(64).setPlacedType(StateTypes.DIORITE).build();
    public static final ItemType SPRUCE_WOOD = builder("spruce_wood").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.SPRUCE_WOOD).build();
    public static final ItemType CYAN_SHULKER_BOX = builder("cyan_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.CYAN_SHULKER_BOX).build();
    public static final ItemType COBWEB = builder("cobweb").setMaxAmount(64).setPlacedType(StateTypes.COBWEB).build();
    public static final ItemType BLAZE_SPAWN_EGG = builder("blaze_spawn_egg").setMaxAmount(64).build();
    public static final ItemType GRAVEL = builder("gravel").setMaxAmount(64).setPlacedType(StateTypes.GRAVEL).build();
    public static final ItemType WITCH_SPAWN_EGG = builder("witch_spawn_egg").setMaxAmount(64).build();
    public static final ItemType ELYTRA = builder("elytra").setMaxAmount(1).setMaxDurability(432).build();
    public static final ItemType ACACIA_FENCE_GATE = builder("acacia_fence_gate").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ACACIA_FENCE_GATE).build();
    public static final ItemType JIGSAW = builder("jigsaw").setMaxAmount(64).setPlacedType(StateTypes.JIGSAW).build();
    public static final ItemType BLUE_GLAZED_TERRACOTTA = builder("blue_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.BLUE_GLAZED_TERRACOTTA).build();
    public static final ItemType FLINT_AND_STEEL = builder("flint_and_steel").setMaxAmount(1).setMaxDurability(64).build();
    public static final ItemType TNT = builder("tnt").setMaxAmount(64).setPlacedType(StateTypes.TNT).build();
    public static final ItemType PINK_SHULKER_BOX = builder("pink_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.PINK_SHULKER_BOX).build();
    public static final ItemType MOSSY_STONE_BRICK_SLAB = builder("mossy_stone_brick_slab").setMaxAmount(64).setPlacedType(StateTypes.MOSSY_STONE_BRICK_SLAB).build();
    public static final ItemType YELLOW_CARPET = builder("yellow_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.YELLOW_CARPET).build();
    public static final ItemType TINTED_GLASS = builder("tinted_glass").setMaxAmount(64).setPlacedType(StateTypes.TINTED_GLASS).build();
    public static final ItemType AIR = builder("air").setMaxAmount(64).build();
    public static final ItemType JUNGLE_FENCE_GATE = builder("jungle_fence_gate").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.JUNGLE_FENCE_GATE).build();
    public static final ItemType SANDSTONE = builder("sandstone").setMaxAmount(64).setPlacedType(StateTypes.SANDSTONE).build();
    public static final ItemType BLUE_TERRACOTTA = builder("blue_terracotta").setMaxAmount(64).setPlacedType(StateTypes.BLUE_TERRACOTTA).build();
    public static final ItemType DARK_PRISMARINE_SLAB = builder("dark_prismarine_slab").setMaxAmount(64).setPlacedType(StateTypes.DARK_PRISMARINE_SLAB).build();
    public static final ItemType CONDUIT = builder("conduit").setMaxAmount(64).setPlacedType(StateTypes.CONDUIT).build();
    public static final ItemType TROPICAL_FISH = builder("tropical_fish").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType IRON_INGOT = builder("iron_ingot").setMaxAmount(64).build();
    public static final ItemType NETHER_STAR = builder("nether_star").setMaxAmount(64).build();
    public static final ItemType OAK_STAIRS = builder("oak_stairs").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.OAK_STAIRS).build();
    public static final ItemType PLAYER_HEAD = builder("player_head").setMaxAmount(64).setPlacedType(StateTypes.PLAYER_HEAD).build();
    public static final ItemType LIGHT_BLUE_CANDLE = builder("light_blue_candle").setMaxAmount(64).setPlacedType(StateTypes.LIGHT_BLUE_CANDLE).build();
    public static final ItemType BEDROCK = builder("bedrock").setMaxAmount(64).setPlacedType(StateTypes.BEDROCK).build();
    public static final ItemType POTATO = builder("potato").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).setPlacedType(StateTypes.POTATOES).build();
    public static final ItemType DEEPSLATE_LAPIS_ORE = builder("deepslate_lapis_ore").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_LAPIS_ORE).build();
    public static final ItemType NETHER_BRICKS = builder("nether_bricks").setMaxAmount(64).setPlacedType(StateTypes.NETHER_BRICKS).build();
    public static final ItemType POISONOUS_POTATO = builder("poisonous_potato").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType BROWN_STAINED_GLASS = builder("brown_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.BROWN_STAINED_GLASS).build();
    public static final ItemType BLACK_DYE = builder("black_dye").setMaxAmount(64).build();
    public static final ItemType CHISELED_NETHER_BRICKS = builder("chiseled_nether_bricks").setMaxAmount(64).setPlacedType(StateTypes.CHISELED_NETHER_BRICKS).build();
    public static final ItemType POLISHED_BLACKSTONE_SLAB = builder("polished_blackstone_slab").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_BLACKSTONE_SLAB).build();
    public static final ItemType POLISHED_ANDESITE_SLAB = builder("polished_andesite_slab").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_ANDESITE_SLAB).build();
    public static final ItemType MAGENTA_BANNER = builder("magenta_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.MAGENTA_BANNER).build();
    public static final ItemType LIGHT_GRAY_STAINED_GLASS = builder("light_gray_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.LIGHT_GRAY_STAINED_GLASS).build();
    public static final ItemType TROPICAL_FISH_BUCKET = builder("tropical_fish_bucket").setMaxAmount(1).build();
    public static final ItemType GREEN_CONCRETE_POWDER = builder("green_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.GREEN_CONCRETE_POWDER).build();
    public static final ItemType PURPUR_BLOCK = builder("purpur_block").setMaxAmount(64).setPlacedType(StateTypes.PURPUR_BLOCK).build();
    public static final ItemType BLUE_BANNER = builder("blue_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BLUE_BANNER).build();
    public static final ItemType SMITHING_TABLE = builder("smithing_table").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.SMITHING_TABLE).build();
    public static final ItemType COMPARATOR = builder("comparator").setMaxAmount(64).setPlacedType(StateTypes.COMPARATOR).build();
    public static final ItemType GRAY_SHULKER_BOX = builder("gray_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.GRAY_SHULKER_BOX).build();
    public static final ItemType INFESTED_CRACKED_STONE_BRICKS = builder("infested_cracked_stone_bricks").setMaxAmount(64).setPlacedType(StateTypes.INFESTED_CRACKED_STONE_BRICKS).build();
    public static final ItemType YELLOW_CONCRETE_POWDER = builder("yellow_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.YELLOW_CONCRETE_POWDER).build();
    public static final ItemType BLACKSTONE_WALL = builder("blackstone_wall").setMaxAmount(64).setPlacedType(StateTypes.BLACKSTONE_WALL).build();
    public static final ItemType COD = builder("cod").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType SMOOTH_STONE = builder("smooth_stone").setMaxAmount(64).setPlacedType(StateTypes.SMOOTH_STONE).build();
    public static final ItemType SPRUCE_PRESSURE_PLATE = builder("spruce_pressure_plate").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.SPRUCE_PRESSURE_PLATE).build();
    public static final ItemType SPRUCE_SAPLING = builder("spruce_sapling").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.SPRUCE_SAPLING).build();
    public static final ItemType ACACIA_FENCE = builder("acacia_fence").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ACACIA_FENCE).build();
    public static final ItemType WARPED_ROOTS = builder("warped_roots").setMaxAmount(64).setPlacedType(StateTypes.WARPED_ROOTS).build();
    public static final ItemType ARROW = builder("arrow").setMaxAmount(64).build();
    public static final ItemType CRIMSON_HYPHAE = builder("crimson_hyphae").setMaxAmount(64).setPlacedType(StateTypes.CRIMSON_HYPHAE).build();
    public static final ItemType CLAY_BALL = builder("clay_ball").setMaxAmount(64).build();
    public static final ItemType CRIMSON_BUTTON = builder("crimson_button").setMaxAmount(64).setPlacedType(StateTypes.CRIMSON_BUTTON).build();
    public static final ItemType BROWN_MUSHROOM = builder("brown_mushroom").setMaxAmount(64).setPlacedType(StateTypes.BROWN_MUSHROOM).build();
    public static final ItemType BUDDING_AMETHYST = builder("budding_amethyst").setMaxAmount(64).setPlacedType(StateTypes.BUDDING_AMETHYST).build();
    public static final ItemType ENDERMAN_SPAWN_EGG = builder("enderman_spawn_egg").setMaxAmount(64).build();
    public static final ItemType IRON_NUGGET = builder("iron_nugget").setMaxAmount(64).build();
    public static final ItemType DONKEY_SPAWN_EGG = builder("donkey_spawn_egg").setMaxAmount(64).build();
    public static final ItemType STONECUTTER = builder("stonecutter").setMaxAmount(64).setPlacedType(StateTypes.STONECUTTER).build();
    public static final ItemType CHAINMAIL_BOOTS = builder("chainmail_boots").setMaxAmount(1).build();
    public static final ItemType TERRACOTTA = builder("terracotta").setMaxAmount(64).setPlacedType(StateTypes.TERRACOTTA).build();
    public static final ItemType LIME_STAINED_GLASS_PANE = builder("lime_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.LIME_STAINED_GLASS_PANE).build();
    public static final ItemType STRUCTURE_VOID = builder("structure_void").setMaxAmount(64).setPlacedType(StateTypes.STRUCTURE_VOID).build();
    public static final ItemType DEAD_BRAIN_CORAL = builder("dead_brain_coral").setMaxAmount(64).setPlacedType(StateTypes.DEAD_BRAIN_CORAL).build();
    public static final ItemType GREEN_WOOL = builder("green_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.GREEN_WOOL).build();
    public static final ItemType CRIMSON_STAIRS = builder("crimson_stairs").setMaxAmount(64).setPlacedType(StateTypes.CRIMSON_STAIRS).build();
    public static final ItemType CLOCK = builder("clock").setMaxAmount(64).build();
    public static final ItemType LLAMA_SPAWN_EGG = builder("llama_spawn_egg").setMaxAmount(64).build();
    public static final ItemType LIGHT_BLUE_STAINED_GLASS_PANE = builder("light_blue_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.LIGHT_BLUE_STAINED_GLASS_PANE).build();
    public static final ItemType DEAD_FIRE_CORAL_FAN = builder("dead_fire_coral_fan").setMaxAmount(64).setPlacedType(StateTypes.DEAD_FIRE_CORAL_FAN).build();
    public static final ItemType CREEPER_SPAWN_EGG = builder("creeper_spawn_egg").setMaxAmount(64).build();
    public static final ItemType OAK_LOG = builder("oak_log").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.OAK_LOG).build();
    public static final ItemType JUNGLE_PLANKS = builder("jungle_planks").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.JUNGLE_PLANKS).build();
    public static final ItemType SNOW = builder("snow").setMaxAmount(64).setPlacedType(StateTypes.SNOW).build();
    public static final ItemType MAGENTA_CARPET = builder("magenta_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.MAGENTA_CARPET).build();
    public static final ItemType BIG_DRIPLEAF = builder("big_dripleaf").setMaxAmount(64).setPlacedType(StateTypes.BIG_DRIPLEAF).build();
    public static final ItemType GRANITE_STAIRS = builder("granite_stairs").setMaxAmount(64).setPlacedType(StateTypes.GRANITE_STAIRS).build();
    public static final ItemType POWERED_RAIL = builder("powered_rail").setMaxAmount(64).setPlacedType(StateTypes.POWERED_RAIL).build();
    public static final ItemType LEATHER_HELMET = builder("leather_helmet").setMaxAmount(1).setMaxDurability(55).build();
    public static final ItemType EMERALD_ORE = builder("emerald_ore").setMaxAmount(64).setPlacedType(StateTypes.EMERALD_ORE).build();
    public static final ItemType STRIPPED_SPRUCE_LOG = builder("stripped_spruce_log").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.STRIPPED_SPRUCE_LOG).build();
    public static final ItemType CUT_RED_SANDSTONE = builder("cut_red_sandstone").setMaxAmount(64).setPlacedType(StateTypes.CUT_RED_SANDSTONE).build();
    public static final ItemType CRIMSON_FENCE = builder("crimson_fence").setMaxAmount(64).setPlacedType(StateTypes.CRIMSON_FENCE).build();
    public static final ItemType BLUE_CARPET = builder("blue_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BLUE_CARPET).build();
    public static final ItemType IRON_HOE = builder("iron_hoe").setMaxAmount(1).setAttributes(ItemAttribute.HOE, ItemAttribute.IRON_TIER).setMaxDurability(250).build();
    public static final ItemType CHICKEN = builder("chicken").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType CRIMSON_STEM = builder("crimson_stem").setMaxAmount(64).setPlacedType(StateTypes.CRIMSON_STEM).build();
    public static final ItemType DEAD_HORN_CORAL_BLOCK = builder("dead_horn_coral_block").setMaxAmount(64).setPlacedType(StateTypes.DEAD_HORN_CORAL_BLOCK).build();
    public static final ItemType CYAN_BANNER = builder("cyan_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.CYAN_BANNER).build();
    public static final ItemType WARPED_DOOR = builder("warped_door").setMaxAmount(64).setPlacedType(StateTypes.WARPED_DOOR).build();
    public static final ItemType SCULK_SENSOR = builder("sculk_sensor").setMaxAmount(64).setPlacedType(StateTypes.SCULK_SENSOR).build();
    public static final ItemType BREWING_STAND = builder("brewing_stand").setMaxAmount(64).setPlacedType(StateTypes.BREWING_STAND).build();
    public static final ItemType LIME_CANDLE = builder("lime_candle").setMaxAmount(64).setPlacedType(StateTypes.LIME_CANDLE).build();
    public static final ItemType STONE_BRICKS = builder("stone_bricks").setMaxAmount(64).setPlacedType(StateTypes.STONE_BRICKS).build();
    public static final ItemType STRIPPED_OAK_WOOD = builder("stripped_oak_wood").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.STRIPPED_OAK_WOOD).build();
    public static final ItemType BUBBLE_CORAL_FAN = builder("bubble_coral_fan").setMaxAmount(64).setPlacedType(StateTypes.BUBBLE_CORAL_FAN).build();
    public static final ItemType OAK_PRESSURE_PLATE = builder("oak_pressure_plate").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.OAK_PRESSURE_PLATE).build();
    public static final ItemType CYAN_GLAZED_TERRACOTTA = builder("cyan_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.CYAN_GLAZED_TERRACOTTA).build();
    public static final ItemType BASALT = builder("basalt").setMaxAmount(64).setPlacedType(StateTypes.BASALT).build();
    public static final ItemType JUNGLE_DOOR = builder("jungle_door").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.JUNGLE_DOOR).build();
    public static final ItemType BROWN_CARPET = builder("brown_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BROWN_CARPET).build();
    public static final ItemType FISHING_ROD = builder("fishing_rod").setMaxAmount(1).setAttributes(ItemAttribute.FUEL).setMaxDurability(64).build();
    public static final ItemType HORSE_SPAWN_EGG = builder("horse_spawn_egg").setMaxAmount(64).build();
    public static final ItemType GRAY_CONCRETE_POWDER = builder("gray_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.GRAY_CONCRETE_POWDER).build();
    public static final ItemType RED_CANDLE = builder("red_candle").setMaxAmount(64).setPlacedType(StateTypes.RED_CANDLE).build();
    public static final ItemType QUARTZ = builder("quartz").setMaxAmount(64).build();
    public static final ItemType RAW_COPPER = builder("raw_copper").setMaxAmount(64).build();
    public static final ItemType BEETROOT = builder("beetroot").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType DEAD_FIRE_CORAL = builder("dead_fire_coral").setMaxAmount(64).setPlacedType(StateTypes.DEAD_FIRE_CORAL).build();
    public static final ItemType MUSIC_DISC_MALL = builder("music_disc_mall").setMaxAmount(1).setAttributes(ItemAttribute.MUSIC_DISC).build();
    public static final ItemType LADDER = builder("ladder").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.LADDER).build();
    public static final ItemType LODESTONE = builder("lodestone").setMaxAmount(64).setPlacedType(StateTypes.LODESTONE).build();
    public static final ItemType RAVAGER_SPAWN_EGG = builder("ravager_spawn_egg").setMaxAmount(64).build();
    public static final ItemType NETHERITE_HOE = builder("netherite_hoe").setMaxAmount(1).setAttributes(ItemAttribute.HOE, ItemAttribute.FIRE_RESISTANT, ItemAttribute.NETHERITE_TIER).setMaxDurability(2031).build();
    public static final ItemType INFESTED_STONE_BRICKS = builder("infested_stone_bricks").setMaxAmount(64).setPlacedType(StateTypes.INFESTED_STONE_BRICKS).build();
    public static final ItemType END_STONE_BRICK_SLAB = builder("end_stone_brick_slab").setMaxAmount(64).setPlacedType(StateTypes.END_STONE_BRICK_SLAB).build();
    public static final ItemType LEATHER_BOOTS = builder("leather_boots").setMaxAmount(1).setMaxDurability(65).build();
    public static final ItemType LIGHT_BLUE_DYE = builder("light_blue_dye").setMaxAmount(64).build();
    public static final ItemType WARPED_STAIRS = builder("warped_stairs").setMaxAmount(64).setPlacedType(StateTypes.WARPED_STAIRS).build();
    public static final ItemType DEAD_BUBBLE_CORAL = builder("dead_bubble_coral").setMaxAmount(64).setPlacedType(StateTypes.DEAD_BUBBLE_CORAL).build();
    public static final ItemType CHAINMAIL_HELMET = builder("chainmail_helmet").setMaxAmount(1).setMaxDurability(165).build();
    public static final ItemType OAK_SLAB = builder("oak_slab").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.OAK_SLAB).build();
    public static final ItemType SPRUCE_DOOR = builder("spruce_door").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.SPRUCE_DOOR).build();
    public static final ItemType ZOMBIE_HEAD = builder("zombie_head").setMaxAmount(64).setPlacedType(StateTypes.ZOMBIE_HEAD).build();
    public static final ItemType DEAD_TUBE_CORAL = builder("dead_tube_coral").setMaxAmount(64).setPlacedType(StateTypes.DEAD_TUBE_CORAL).build();
    public static final ItemType CHORUS_FRUIT = builder("chorus_fruit").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType HORN_CORAL = builder("horn_coral").setMaxAmount(64).setPlacedType(StateTypes.HORN_CORAL).build();
    public static final ItemType PRISMARINE_CRYSTALS = builder("prismarine_crystals").setMaxAmount(64).build();
    public static final ItemType WHITE_CONCRETE_POWDER = builder("white_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.WHITE_CONCRETE_POWDER).build();
    public static final ItemType GRANITE_SLAB = builder("granite_slab").setMaxAmount(64).setPlacedType(StateTypes.GRANITE_SLAB).build();
    public static final ItemType SANDSTONE_SLAB = builder("sandstone_slab").setMaxAmount(64).setPlacedType(StateTypes.SANDSTONE_SLAB).build();
    public static final ItemType CAKE = builder("cake").setMaxAmount(1).setPlacedType(StateTypes.CAKE).build();
    public static final ItemType ACACIA_LEAVES = builder("acacia_leaves").setMaxAmount(64).setPlacedType(StateTypes.ACACIA_LEAVES).build();
    public static final ItemType YELLOW_SHULKER_BOX = builder("yellow_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.YELLOW_SHULKER_BOX).build();
    public static final ItemType MOSS_CARPET = builder("moss_carpet").setMaxAmount(64).setPlacedType(StateTypes.MOSS_CARPET).build();
    public static final ItemType BROWN_BANNER = builder("brown_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BROWN_BANNER).build();
    public static final ItemType GUNPOWDER = builder("gunpowder").setMaxAmount(64).build();
    public static final ItemType PUFFERFISH_BUCKET = builder("pufferfish_bucket").setMaxAmount(1).build();
    public static final ItemType NETHER_BRICK = builder("nether_brick").setMaxAmount(64).build();
    public static final ItemType PINK_STAINED_GLASS_PANE = builder("pink_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.PINK_STAINED_GLASS_PANE).build();
    public static final ItemType GLOW_SQUID_SPAWN_EGG = builder("glow_squid_spawn_egg").setMaxAmount(64).build();
    public static final ItemType BAMBOO = builder("bamboo").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BAMBOO).build();
    public static final ItemType RED_SAND = builder("red_sand").setMaxAmount(64).setPlacedType(StateTypes.RED_SAND).build();
    public static final ItemType PURPLE_SHULKER_BOX = builder("purple_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.PURPLE_SHULKER_BOX).build();
    public static final ItemType CLAY = builder("clay").setMaxAmount(64).setPlacedType(StateTypes.CLAY).build();
    public static final ItemType CHISELED_STONE_BRICKS = builder("chiseled_stone_bricks").setMaxAmount(64).setPlacedType(StateTypes.CHISELED_STONE_BRICKS).build();
    public static final ItemType LECTERN = builder("lectern").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.LECTERN).build();
    public static final ItemType DIAMOND_LEGGINGS = builder("diamond_leggings").setMaxAmount(1).setMaxDurability(495).build();
    public static final ItemType DIAMOND_HELMET = builder("diamond_helmet").setMaxAmount(1).setMaxDurability(363).build();
    public static final ItemType WARPED_SLAB = builder("warped_slab").setMaxAmount(64).setPlacedType(StateTypes.WARPED_SLAB).build();
    public static final ItemType QUARTZ_BLOCK = builder("quartz_block").setMaxAmount(64).setPlacedType(StateTypes.QUARTZ_BLOCK).build();
    public static final ItemType DIAMOND_CHESTPLATE = builder("diamond_chestplate").setMaxAmount(1).setMaxDurability(528).build();
    public static final ItemType MOSSY_COBBLESTONE_SLAB = builder("mossy_cobblestone_slab").setMaxAmount(64).setPlacedType(StateTypes.MOSSY_COBBLESTONE_SLAB).build();
    public static final ItemType WOODEN_HOE = builder("wooden_hoe").setMaxAmount(1).setAttributes(ItemAttribute.HOE, ItemAttribute.WOOD_TIER, ItemAttribute.FUEL).setMaxDurability(59).build();
    public static final ItemType MUSIC_DISC_BLOCKS = builder("music_disc_blocks").setMaxAmount(1).setAttributes(ItemAttribute.MUSIC_DISC).build();
    public static final ItemType WHITE_WOOL = builder("white_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.WHITE_WOOL).build();
    public static final ItemType HANGING_ROOTS = builder("hanging_roots").setMaxAmount(64).setPlacedType(StateTypes.HANGING_ROOTS).build();
    public static final ItemType END_STONE_BRICK_STAIRS = builder("end_stone_brick_stairs").setMaxAmount(64).setPlacedType(StateTypes.END_STONE_BRICK_STAIRS).build();
    public static final ItemType EXPOSED_COPPER = builder("exposed_copper").setMaxAmount(64).setPlacedType(StateTypes.EXPOSED_COPPER).build();
    public static final ItemType CHAINMAIL_CHESTPLATE = builder("chainmail_chestplate").setMaxAmount(1).setMaxDurability(240).build();
    public static final ItemType IRON_LEGGINGS = builder("iron_leggings").setMaxAmount(1).setMaxDurability(225).build();
    public static final ItemType PURPLE_STAINED_GLASS = builder("purple_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.PURPLE_STAINED_GLASS).build();
    public static final ItemType PURPLE_TERRACOTTA = builder("purple_terracotta").setMaxAmount(64).setPlacedType(StateTypes.PURPLE_TERRACOTTA).build();
    public static final ItemType GREEN_BED = builder("green_bed").setMaxAmount(1).setPlacedType(StateTypes.GREEN_BED).build();
    public static final ItemType RED_CONCRETE_POWDER = builder("red_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.RED_CONCRETE_POWDER).build();
    public static final ItemType REPEATER = builder("repeater").setMaxAmount(64).setPlacedType(StateTypes.REPEATER).build();
    public static final ItemType MYCELIUM = builder("mycelium").setMaxAmount(64).setPlacedType(StateTypes.MYCELIUM).build();
    public static final ItemType CHISELED_SANDSTONE = builder("chiseled_sandstone").setMaxAmount(64).setPlacedType(StateTypes.CHISELED_SANDSTONE).build();
    public static final ItemType LINGERING_POTION = builder("lingering_potion").setMaxAmount(1).build();
    public static final ItemType CUT_COPPER_STAIRS = builder("cut_copper_stairs").setMaxAmount(64).setPlacedType(StateTypes.CUT_COPPER_STAIRS).build();
    public static final ItemType CALCITE = builder("calcite").setMaxAmount(64).setPlacedType(StateTypes.CALCITE).build();
    public static final ItemType STRIPPED_BIRCH_LOG = builder("stripped_birch_log").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.STRIPPED_BIRCH_LOG).build();
    public static final ItemType HAY_BLOCK = builder("hay_block").setMaxAmount(64).setPlacedType(StateTypes.HAY_BLOCK).build();
    public static final ItemType LIGHT_BLUE_CONCRETE_POWDER = builder("light_blue_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.LIGHT_BLUE_CONCRETE_POWDER).build();
    public static final ItemType PINK_DYE = builder("pink_dye").setMaxAmount(64).build();
    public static final ItemType ORANGE_CARPET = builder("orange_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ORANGE_CARPET).build();
    public static final ItemType MAGENTA_CONCRETE_POWDER = builder("magenta_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.MAGENTA_CONCRETE_POWDER).build();
    public static final ItemType ANDESITE_WALL = builder("andesite_wall").setMaxAmount(64).setPlacedType(StateTypes.ANDESITE_WALL).build();
    public static final ItemType YELLOW_CONCRETE = builder("yellow_concrete").setMaxAmount(64).setPlacedType(StateTypes.YELLOW_CONCRETE).build();
    public static final ItemType WARPED_FUNGUS_ON_A_STICK = builder("warped_fungus_on_a_stick").setMaxAmount(1).setMaxDurability(100).build();
    public static final ItemType WAXED_WEATHERED_CUT_COPPER = builder("waxed_weathered_cut_copper").setMaxAmount(64).setPlacedType(StateTypes.WAXED_WEATHERED_CUT_COPPER).build();
    public static final ItemType COBBLESTONE_SLAB = builder("cobblestone_slab").setMaxAmount(64).setPlacedType(StateTypes.COBBLESTONE_SLAB).build();
    public static final ItemType ARMOR_STAND = builder("armor_stand").setMaxAmount(16).build();
    public static final ItemType RED_NETHER_BRICKS = builder("red_nether_bricks").setMaxAmount(64).setPlacedType(StateTypes.RED_NETHER_BRICKS).build();
    public static final ItemType LIGHT_GRAY_CONCRETE = builder("light_gray_concrete").setMaxAmount(64).setPlacedType(StateTypes.LIGHT_GRAY_CONCRETE).build();
    public static final ItemType GLASS = builder("glass").setMaxAmount(64).setPlacedType(StateTypes.GLASS).build();
    public static final ItemType CHEST = builder("chest").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.CHEST).build();
    public static final ItemType SEAGRASS = builder("seagrass").setMaxAmount(64).setPlacedType(StateTypes.SEAGRASS).build();
    public static final ItemType WARPED_TRAPDOOR = builder("warped_trapdoor").setMaxAmount(64).setPlacedType(StateTypes.WARPED_TRAPDOOR).build();
    public static final ItemType STONE_STAIRS = builder("stone_stairs").setMaxAmount(64).setPlacedType(StateTypes.STONE_STAIRS).build();
    public static final ItemType RED_TERRACOTTA = builder("red_terracotta").setMaxAmount(64).setPlacedType(StateTypes.RED_TERRACOTTA).build();
    public static final ItemType FURNACE_MINECART = builder("furnace_minecart").setMaxAmount(1).build();
    public static final ItemType END_PORTAL_FRAME = builder("end_portal_frame").setMaxAmount(64).setPlacedType(StateTypes.END_PORTAL_FRAME).build();
    public static final ItemType GRINDSTONE = builder("grindstone").setMaxAmount(64).setPlacedType(StateTypes.GRINDSTONE).build();
    public static final ItemType LEATHER_LEGGINGS = builder("leather_leggings").setMaxAmount(1).setMaxDurability(75).build();
    public static final ItemType WAXED_COPPER_BLOCK = builder("waxed_copper_block").setMaxAmount(64).setPlacedType(StateTypes.WAXED_COPPER_BLOCK).build();
    public static final ItemType DARK_OAK_LEAVES = builder("dark_oak_leaves").setMaxAmount(64).setPlacedType(StateTypes.DARK_OAK_LEAVES).build();
    public static final ItemType LIME_SHULKER_BOX = builder("lime_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.LIME_SHULKER_BOX).build();
    public static final ItemType JUNGLE_SAPLING = builder("jungle_sapling").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.JUNGLE_SAPLING).build();
    public static final ItemType AMETHYST_BLOCK = builder("amethyst_block").setMaxAmount(64).setPlacedType(StateTypes.AMETHYST_BLOCK).build();
    public static final ItemType CREEPER_HEAD = builder("creeper_head").setMaxAmount(64).setPlacedType(StateTypes.CREEPER_HEAD).build();
    public static final ItemType WEATHERED_COPPER = builder("weathered_copper").setMaxAmount(64).setPlacedType(StateTypes.WEATHERED_COPPER).build();
    public static final ItemType GRAY_BANNER = builder("gray_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.GRAY_BANNER).build();
    public static final ItemType STRING = builder("string").setMaxAmount(64).setPlacedType(StateTypes.TRIPWIRE).build();
    public static final ItemType WHITE_TERRACOTTA = builder("white_terracotta").setMaxAmount(64).setPlacedType(StateTypes.WHITE_TERRACOTTA).build();
    public static final ItemType BOOK = builder("book").setMaxAmount(64).build();
    public static final ItemType WOODEN_SHOVEL = builder("wooden_shovel").setMaxAmount(1).setAttributes(ItemAttribute.SHOVEL, ItemAttribute.WOOD_TIER, ItemAttribute.FUEL).setMaxDurability(59).build();
    public static final ItemType BLACKSTONE_SLAB = builder("blackstone_slab").setMaxAmount(64).setPlacedType(StateTypes.BLACKSTONE_SLAB).build();
    public static final ItemType JUNGLE_TRAPDOOR = builder("jungle_trapdoor").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.JUNGLE_TRAPDOOR).build();
    public static final ItemType BLACK_CARPET = builder("black_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BLACK_CARPET).build();
    public static final ItemType FIRE_CORAL = builder("fire_coral").setMaxAmount(64).setPlacedType(StateTypes.FIRE_CORAL).build();
    public static final ItemType MAGENTA_GLAZED_TERRACOTTA = builder("magenta_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.MAGENTA_GLAZED_TERRACOTTA).build();
    public static final ItemType GRAY_BED = builder("gray_bed").setMaxAmount(1).setPlacedType(StateTypes.GRAY_BED).build();
    public static final ItemType TRIDENT = builder("trident").setMaxAmount(1).setMaxDurability(250).build();
    public static final ItemType WET_SPONGE = builder("wet_sponge").setMaxAmount(64).setPlacedType(StateTypes.WET_SPONGE).build();
    public static final ItemType YELLOW_WOOL = builder("yellow_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.YELLOW_WOOL).build();
    public static final ItemType CHICKEN_SPAWN_EGG = builder("chicken_spawn_egg").setMaxAmount(64).build();
    public static final ItemType DRIED_KELP_BLOCK = builder("dried_kelp_block").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DRIED_KELP_BLOCK).build();
    public static final ItemType BONE = builder("bone").setMaxAmount(64).build();
    public static final ItemType YELLOW_TERRACOTTA = builder("yellow_terracotta").setMaxAmount(64).setPlacedType(StateTypes.YELLOW_TERRACOTTA).build();
    public static final ItemType TARGET = builder("target").setMaxAmount(64).setPlacedType(StateTypes.TARGET).build();
    public static final ItemType WAXED_WEATHERED_COPPER = builder("waxed_weathered_copper").setMaxAmount(64).setPlacedType(StateTypes.WAXED_WEATHERED_COPPER).build();
    public static final ItemType MAGMA_BLOCK = builder("magma_block").setMaxAmount(64).setPlacedType(StateTypes.MAGMA_BLOCK).build();
    public static final ItemType CAULDRON = builder("cauldron").setMaxAmount(64).setPlacedType(StateTypes.CAULDRON).build();
    public static final ItemType PINK_CONCRETE_POWDER = builder("pink_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.PINK_CONCRETE_POWDER).build();
    public static final ItemType WITHER_SKELETON_SPAWN_EGG = builder("wither_skeleton_spawn_egg").setMaxAmount(64).build();
    public static final ItemType CROSSBOW = builder("crossbow").setMaxAmount(1).setAttributes(ItemAttribute.FUEL).setMaxDurability(465).build();
    public static final ItemType SOUL_SAND = builder("soul_sand").setMaxAmount(64).setPlacedType(StateTypes.SOUL_SAND).build();
    public static final ItemType HORN_CORAL_BLOCK = builder("horn_coral_block").setMaxAmount(64).setPlacedType(StateTypes.HORN_CORAL_BLOCK).build();
    public static final ItemType DIAMOND_SHOVEL = builder("diamond_shovel").setMaxAmount(1).setAttributes(ItemAttribute.SHOVEL, ItemAttribute.DIAMOND_TIER).setMaxDurability(1561).build();
    public static final ItemType PRISMARINE_BRICK_STAIRS = builder("prismarine_brick_stairs").setMaxAmount(64).setPlacedType(StateTypes.PRISMARINE_BRICK_STAIRS).build();
    public static final ItemType EXPERIENCE_BOTTLE = builder("experience_bottle").setMaxAmount(64).build();
    public static final ItemType GOLDEN_HORSE_ARMOR = builder("golden_horse_armor").setMaxAmount(1).build();
    public static final ItemType BLUE_CANDLE = builder("blue_candle").setMaxAmount(64).setPlacedType(StateTypes.BLUE_CANDLE).build();
    public static final ItemType ORANGE_TULIP = builder("orange_tulip").setMaxAmount(64).setPlacedType(StateTypes.ORANGE_TULIP).build();
    public static final ItemType DEEPSLATE = builder("deepslate").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE).build();
    public static final ItemType BEE_NEST = builder("bee_nest").setMaxAmount(64).setPlacedType(StateTypes.BEE_NEST).build();
    public static final ItemType SMOOTH_RED_SANDSTONE_SLAB = builder("smooth_red_sandstone_slab").setMaxAmount(64).setPlacedType(StateTypes.SMOOTH_RED_SANDSTONE_SLAB).build();
    public static final ItemType CUT_SANDSTONE_SLAB = builder("cut_sandstone_slab").setMaxAmount(64).setPlacedType(StateTypes.CUT_SANDSTONE_SLAB).build();
    public static final ItemType GRASS_BLOCK = builder("grass_block").setMaxAmount(64).setPlacedType(StateTypes.GRASS_BLOCK).build();
    public static final ItemType MUSIC_DISC_PIGSTEP = builder("music_disc_pigstep").setMaxAmount(1).setAttributes(ItemAttribute.MUSIC_DISC).build();
    public static final ItemType BLACK_BED = builder("black_bed").setMaxAmount(1).setPlacedType(StateTypes.BLACK_BED).build();
    public static final ItemType WAXED_OXIDIZED_COPPER = builder("waxed_oxidized_copper").setMaxAmount(64).setPlacedType(StateTypes.WAXED_OXIDIZED_COPPER).build();
    public static final ItemType MINECART = builder("minecart").setMaxAmount(1).build();
    public static final ItemType DEAD_HORN_CORAL_FAN = builder("dead_horn_coral_fan").setMaxAmount(64).setPlacedType(StateTypes.DEAD_HORN_CORAL_FAN).build();
    public static final ItemType LIGHT = builder("light").setMaxAmount(64).setPlacedType(StateTypes.LIGHT).build();
    public static final ItemType SPECTRAL_ARROW = builder("spectral_arrow").setMaxAmount(64).build();
    public static final ItemType JUNGLE_STAIRS = builder("jungle_stairs").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.JUNGLE_STAIRS).build();
    public static final ItemType NETHERITE_SHOVEL = builder("netherite_shovel").setMaxAmount(1).setAttributes(ItemAttribute.SHOVEL, ItemAttribute.FIRE_RESISTANT, ItemAttribute.NETHERITE_TIER).setMaxDurability(2031).build();
    public static final ItemType PIGLIN_SPAWN_EGG = builder("piglin_spawn_egg").setMaxAmount(64).build();
    public static final ItemType OXEYE_DAISY = builder("oxeye_daisy").setMaxAmount(64).setPlacedType(StateTypes.OXEYE_DAISY).build();
    public static final ItemType WAXED_OXIDIZED_CUT_COPPER_STAIRS = builder("waxed_oxidized_cut_copper_stairs").setMaxAmount(64).setPlacedType(StateTypes.WAXED_OXIDIZED_CUT_COPPER_STAIRS).build();
    public static final ItemType SMOOTH_SANDSTONE_STAIRS = builder("smooth_sandstone_stairs").setMaxAmount(64).setPlacedType(StateTypes.SMOOTH_SANDSTONE_STAIRS).build();
    public static final ItemType LEATHER_CHESTPLATE = builder("leather_chestplate").setMaxAmount(1).setMaxDurability(80).build();
    public static final ItemType BLUE_WOOL = builder("blue_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BLUE_WOOL).build();
    public static final ItemType AXOLOTL_BUCKET = builder("axolotl_bucket").setMaxAmount(1).build();
    public static final ItemType POPPED_CHORUS_FRUIT = builder("popped_chorus_fruit").setMaxAmount(64).build();
    public static final ItemType CREEPER_BANNER_PATTERN = builder("creeper_banner_pattern").setMaxAmount(1).build();
    public static final ItemType SMALL_AMETHYST_BUD = builder("small_amethyst_bud").setMaxAmount(64).setPlacedType(StateTypes.SMALL_AMETHYST_BUD).build();
    public static final ItemType WAXED_EXPOSED_CUT_COPPER_SLAB = builder("waxed_exposed_cut_copper_slab").setMaxAmount(64).setPlacedType(StateTypes.WAXED_EXPOSED_CUT_COPPER_SLAB).build();
    public static final ItemType POLAR_BEAR_SPAWN_EGG = builder("polar_bear_spawn_egg").setMaxAmount(64).build();
    public static final ItemType STRIPPED_DARK_OAK_LOG = builder("stripped_dark_oak_log").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.STRIPPED_DARK_OAK_LOG).build();
    public static final ItemType RED_BED = builder("red_bed").setMaxAmount(1).setPlacedType(StateTypes.RED_BED).build();
    public static final ItemType YELLOW_BANNER = builder("yellow_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.YELLOW_BANNER).build();
    public static final ItemType BARREL = builder("barrel").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BARREL).build();
    public static final ItemType CHAIN = builder("chain").setMaxAmount(64).setPlacedType(StateTypes.CHAIN).build();
    public static final ItemType DEAD_BRAIN_CORAL_FAN = builder("dead_brain_coral_fan").setMaxAmount(64).setPlacedType(StateTypes.DEAD_BRAIN_CORAL_FAN).build();
    public static final ItemType ROTTEN_FLESH = builder("rotten_flesh").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType SLIME_BLOCK = builder("slime_block").setMaxAmount(64).setPlacedType(StateTypes.SLIME_BLOCK).build();
    public static final ItemType EMERALD_BLOCK = builder("emerald_block").setMaxAmount(64).setPlacedType(StateTypes.EMERALD_BLOCK).build();
    public static final ItemType PURPLE_BANNER = builder("purple_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.PURPLE_BANNER).build();
    public static final ItemType OAK_FENCE = builder("oak_fence").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.OAK_FENCE).build();
    public static final ItemType TNT_MINECART = builder("tnt_minecart").setMaxAmount(1).build();
    public static final ItemType CHAINMAIL_LEGGINGS = builder("chainmail_leggings").setMaxAmount(1).setMaxDurability(225).build();
    public static final ItemType PINK_CANDLE = builder("pink_candle").setMaxAmount(64).setPlacedType(StateTypes.PINK_CANDLE).build();
    public static final ItemType ACACIA_PLANKS = builder("acacia_planks").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ACACIA_PLANKS).build();
    public static final ItemType IRON_ORE = builder("iron_ore").setMaxAmount(64).setPlacedType(StateTypes.IRON_ORE).build();
    public static final ItemType BLAZE_POWDER = builder("blaze_powder").setMaxAmount(64).build();
    public static final ItemType QUARTZ_PILLAR = builder("quartz_pillar").setMaxAmount(64).setPlacedType(StateTypes.QUARTZ_PILLAR).build();
    public static final ItemType DEAD_BUBBLE_CORAL_BLOCK = builder("dead_bubble_coral_block").setMaxAmount(64).setPlacedType(StateTypes.DEAD_BUBBLE_CORAL_BLOCK).build();
    public static final ItemType PURPLE_CONCRETE_POWDER = builder("purple_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.PURPLE_CONCRETE_POWDER).build();
    public static final ItemType POLISHED_DIORITE_SLAB = builder("polished_diorite_slab").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_DIORITE_SLAB).build();
    public static final ItemType SMOOTH_STONE_SLAB = builder("smooth_stone_slab").setMaxAmount(64).setPlacedType(StateTypes.SMOOTH_STONE_SLAB).build();
    public static final ItemType RAW_IRON = builder("raw_iron").setMaxAmount(64).build();
    public static final ItemType GOLDEN_SWORD = builder("golden_sword").setMaxAmount(1).setAttributes(ItemAttribute.GOLD_TIER, ItemAttribute.SWORD).setMaxDurability(32).build();
    public static final ItemType PRISMARINE = builder("prismarine").setMaxAmount(64).setPlacedType(StateTypes.PRISMARINE).build();
    public static final ItemType WAXED_WEATHERED_CUT_COPPER_SLAB = builder("waxed_weathered_cut_copper_slab").setMaxAmount(64).setPlacedType(StateTypes.WAXED_WEATHERED_CUT_COPPER_SLAB).build();
    public static final ItemType CRIMSON_TRAPDOOR = builder("crimson_trapdoor").setMaxAmount(64).setPlacedType(StateTypes.CRIMSON_TRAPDOOR).build();
    public static final ItemType FILLED_MAP = builder("filled_map").setMaxAmount(64).build();
    public static final ItemType LIME_CONCRETE = builder("lime_concrete").setMaxAmount(64).setPlacedType(StateTypes.LIME_CONCRETE).build();
    public static final ItemType MOSSY_COBBLESTONE_STAIRS = builder("mossy_cobblestone_stairs").setMaxAmount(64).setPlacedType(StateTypes.MOSSY_COBBLESTONE_STAIRS).build();
    public static final ItemType IRON_BLOCK = builder("iron_block").setMaxAmount(64).setPlacedType(StateTypes.IRON_BLOCK).build();
    public static final ItemType BIRCH_SIGN = builder("birch_sign").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BIRCH_SIGN).build();
    public static final ItemType PORKCHOP = builder("porkchop").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType PINK_TULIP = builder("pink_tulip").setMaxAmount(64).setPlacedType(StateTypes.PINK_TULIP).build();
    public static final ItemType WARPED_FENCE_GATE = builder("warped_fence_gate").setMaxAmount(64).setPlacedType(StateTypes.WARPED_FENCE_GATE).build();
    public static final ItemType BLUE_ICE = builder("blue_ice").setMaxAmount(64).setPlacedType(StateTypes.BLUE_ICE).build();
    public static final ItemType WOLF_SPAWN_EGG = builder("wolf_spawn_egg").setMaxAmount(64).build();
    public static final ItemType DEEPSLATE_TILE_STAIRS = builder("deepslate_tile_stairs").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_TILE_STAIRS).build();
    public static final ItemType STRIPPED_WARPED_STEM = builder("stripped_warped_stem").setMaxAmount(64).setPlacedType(StateTypes.STRIPPED_WARPED_STEM).build();
    public static final ItemType CRYING_OBSIDIAN = builder("crying_obsidian").setMaxAmount(64).setPlacedType(StateTypes.CRYING_OBSIDIAN).build();
    public static final ItemType BROWN_TERRACOTTA = builder("brown_terracotta").setMaxAmount(64).setPlacedType(StateTypes.BROWN_TERRACOTTA).build();
    public static final ItemType VINE = builder("vine").setMaxAmount(64).setPlacedType(StateTypes.VINE).build();
    public static final ItemType DARK_OAK_FENCE = builder("dark_oak_fence").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DARK_OAK_FENCE).build();
    public static final ItemType QUARTZ_STAIRS = builder("quartz_stairs").setMaxAmount(64).setPlacedType(StateTypes.QUARTZ_STAIRS).build();
    public static final ItemType RAIL = builder("rail").setMaxAmount(64).setPlacedType(StateTypes.RAIL).build();
    public static final ItemType WHITE_BANNER = builder("white_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.WHITE_BANNER).build();
    public static final ItemType MOSS_BLOCK = builder("moss_block").setMaxAmount(64).setPlacedType(StateTypes.MOSS_BLOCK).build();
    public static final ItemType BLUE_STAINED_GLASS = builder("blue_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.BLUE_STAINED_GLASS).build();
    public static final ItemType GREEN_TERRACOTTA = builder("green_terracotta").setMaxAmount(64).setPlacedType(StateTypes.GREEN_TERRACOTTA).build();
    public static final ItemType IRON_HORSE_ARMOR = builder("iron_horse_armor").setMaxAmount(1).build();
    public static final ItemType RED_CARPET = builder("red_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.RED_CARPET).build();
    public static final ItemType WHITE_CONCRETE = builder("white_concrete").setMaxAmount(64).setPlacedType(StateTypes.WHITE_CONCRETE).build();
    public static final ItemType FLOWER_BANNER_PATTERN = builder("flower_banner_pattern").setMaxAmount(1).build();
    public static final ItemType OAK_WOOD = builder("oak_wood").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.OAK_WOOD).build();
    public static final ItemType GLOW_LICHEN = builder("glow_lichen").setMaxAmount(64).setPlacedType(StateTypes.GLOW_LICHEN).build();
    public static final ItemType LIME_CONCRETE_POWDER = builder("lime_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.LIME_CONCRETE_POWDER).build();
    public static final ItemType RED_SHULKER_BOX = builder("red_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.RED_SHULKER_BOX).build();
    public static final ItemType LIGHT_BLUE_TERRACOTTA = builder("light_blue_terracotta").setMaxAmount(64).setPlacedType(StateTypes.LIGHT_BLUE_TERRACOTTA).build();
    public static final ItemType BLUE_DYE = builder("blue_dye").setMaxAmount(64).build();
    public static final ItemType SUGAR = builder("sugar").setMaxAmount(64).build();
    public static final ItemType CAT_SPAWN_EGG = builder("cat_spawn_egg").setMaxAmount(64).build();
    public static final ItemType MUSIC_DISC_FAR = builder("music_disc_far").setMaxAmount(1).setAttributes(ItemAttribute.MUSIC_DISC).build();
    public static final ItemType BROWN_GLAZED_TERRACOTTA = builder("brown_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.BROWN_GLAZED_TERRACOTTA).build();
    public static final ItemType COPPER_INGOT = builder("copper_ingot").setMaxAmount(64).build();
    public static final ItemType COD_BUCKET = builder("cod_bucket").setMaxAmount(1).build();
    public static final ItemType CRIMSON_PLANKS = builder("crimson_planks").setMaxAmount(64).setPlacedType(StateTypes.CRIMSON_PLANKS).build();
    public static final ItemType INK_SAC = builder("ink_sac").setMaxAmount(64).build();
    public static final ItemType NOTE_BLOCK = builder("note_block").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.NOTE_BLOCK).build();
    public static final ItemType BOWL = builder("bowl").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).build();
    public static final ItemType CRACKED_STONE_BRICKS = builder("cracked_stone_bricks").setMaxAmount(64).setPlacedType(StateTypes.CRACKED_STONE_BRICKS).build();
    public static final ItemType SKELETON_SKULL = builder("skeleton_skull").setMaxAmount(64).setPlacedType(StateTypes.SKELETON_SKULL).build();
    public static final ItemType PURPUR_STAIRS = builder("purpur_stairs").setMaxAmount(64).setPlacedType(StateTypes.PURPUR_STAIRS).build();
    public static final ItemType ORANGE_DYE = builder("orange_dye").setMaxAmount(64).build();
    public static final ItemType YELLOW_BED = builder("yellow_bed").setMaxAmount(1).setPlacedType(StateTypes.YELLOW_BED).build();
    public static final ItemType CUT_COPPER = builder("cut_copper").setMaxAmount(64).setPlacedType(StateTypes.CUT_COPPER).build();
    public static final ItemType JUNGLE_SIGN = builder("jungle_sign").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.JUNGLE_SIGN).build();
    public static final ItemType GREEN_GLAZED_TERRACOTTA = builder("green_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.GREEN_GLAZED_TERRACOTTA).build();
    public static final ItemType SCUTE = builder("scute").setMaxAmount(64).build();
    public static final ItemType GOLDEN_CHESTPLATE = builder("golden_chestplate").setMaxAmount(1).setMaxDurability(112).build();
    public static final ItemType NETHERITE_LEGGINGS = builder("netherite_leggings").setMaxAmount(1).setAttributes(ItemAttribute.FIRE_RESISTANT).build();
    public static final ItemType GOLDEN_SHOVEL = builder("golden_shovel").setMaxAmount(1).setAttributes(ItemAttribute.SHOVEL, ItemAttribute.GOLD_TIER).setMaxDurability(32).build();
    public static final ItemType SPRUCE_STAIRS = builder("spruce_stairs").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.SPRUCE_STAIRS).build();
    public static final ItemType BIRCH_PLANKS = builder("birch_planks").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BIRCH_PLANKS).build();
    public static final ItemType GRAY_WOOL = builder("gray_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.GRAY_WOOL).build();
    public static final ItemType SILVERFISH_SPAWN_EGG = builder("silverfish_spawn_egg").setMaxAmount(64).build();
    public static final ItemType WHITE_STAINED_GLASS = builder("white_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.WHITE_STAINED_GLASS).build();
    public static final ItemType ANCIENT_DEBRIS = builder("ancient_debris").setMaxAmount(64).setAttributes(ItemAttribute.FIRE_RESISTANT).setMaxDurability(32).setPlacedType(StateTypes.ANCIENT_DEBRIS).build();
    public static final ItemType GREEN_STAINED_GLASS_PANE = builder("green_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.GREEN_STAINED_GLASS_PANE).build();
    public static final ItemType SMOOTH_BASALT = builder("smooth_basalt").setMaxAmount(64).setPlacedType(StateTypes.SMOOTH_BASALT).build();
    public static final ItemType DIAMOND = builder("diamond").setMaxAmount(64).build();
    public static final ItemType BLACK_CONCRETE_POWDER = builder("black_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.BLACK_CONCRETE_POWDER).build();
    public static final ItemType RABBIT_SPAWN_EGG = builder("rabbit_spawn_egg").setMaxAmount(64).build();
    public static final ItemType LIME_CARPET = builder("lime_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.LIME_CARPET).build();
    public static final ItemType BLUE_CONCRETE_POWDER = builder("blue_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.BLUE_CONCRETE_POWDER).build();
    public static final ItemType MAGENTA_CANDLE = builder("magenta_candle").setMaxAmount(64).setPlacedType(StateTypes.MAGENTA_CANDLE).build();
    public static final ItemType PURPUR_SLAB = builder("purpur_slab").setMaxAmount(64).setPlacedType(StateTypes.PURPUR_SLAB).build();
    public static final ItemType HOPPER = builder("hopper").setMaxAmount(64).setPlacedType(StateTypes.HOPPER).build();
    public static final ItemType STRIDER_SPAWN_EGG = builder("strider_spawn_egg").setMaxAmount(64).build();
    public static final ItemType POLISHED_DIORITE = builder("polished_diorite").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_DIORITE).build();
    public static final ItemType LIME_TERRACOTTA = builder("lime_terracotta").setMaxAmount(64).setPlacedType(StateTypes.LIME_TERRACOTTA).build();
    public static final ItemType BEEF = builder("beef").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType BAKED_POTATO = builder("baked_potato").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType MOSSY_COBBLESTONE = builder("mossy_cobblestone").setMaxAmount(64).setPlacedType(StateTypes.MOSSY_COBBLESTONE).build();
    public static final ItemType BRICK_WALL = builder("brick_wall").setMaxAmount(64).setPlacedType(StateTypes.BRICK_WALL).build();
    public static final ItemType BRAIN_CORAL_BLOCK = builder("brain_coral_block").setMaxAmount(64).setPlacedType(StateTypes.BRAIN_CORAL_BLOCK).build();
    public static final ItemType BIRCH_FENCE_GATE = builder("birch_fence_gate").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BIRCH_FENCE_GATE).build();
    public static final ItemType MUSIC_DISC_CHIRP = builder("music_disc_chirp").setMaxAmount(1).setAttributes(ItemAttribute.MUSIC_DISC).build();
    public static final ItemType NETHERITE_SWORD = builder("netherite_sword").setMaxAmount(1).setAttributes(ItemAttribute.SWORD, ItemAttribute.FIRE_RESISTANT, ItemAttribute.NETHERITE_TIER).setMaxDurability(2031).build();
    public static final ItemType COBBLED_DEEPSLATE = builder("cobbled_deepslate").setMaxAmount(64).setPlacedType(StateTypes.COBBLED_DEEPSLATE).build();
    public static final ItemType BROWN_CANDLE = builder("brown_candle").setMaxAmount(64).setPlacedType(StateTypes.BROWN_CANDLE).build();
    public static final ItemType YELLOW_STAINED_GLASS_PANE = builder("yellow_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.YELLOW_STAINED_GLASS_PANE).build();
    public static final ItemType DIRT_PATH = builder("dirt_path").setMaxAmount(64).setPlacedType(StateTypes.DIRT_PATH).build();
    public static final ItemType DARK_OAK_PLANKS = builder("dark_oak_planks").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DARK_OAK_PLANKS).build();
    public static final ItemType PHANTOM_MEMBRANE = builder("phantom_membrane").setMaxAmount(64).build();
    public static final ItemType WOODEN_SWORD = builder("wooden_sword").setMaxAmount(1).setAttributes(ItemAttribute.SWORD, ItemAttribute.WOOD_TIER, ItemAttribute.FUEL).setMaxDurability(59).build();
    public static final ItemType ALLIUM = builder("allium").setMaxAmount(64).setPlacedType(StateTypes.ALLIUM).build();
    public static final ItemType JUNGLE_LEAVES = builder("jungle_leaves").setMaxAmount(64).setPlacedType(StateTypes.JUNGLE_LEAVES).build();
    public static final ItemType CHORUS_PLANT = builder("chorus_plant").setMaxAmount(64).setPlacedType(StateTypes.CHORUS_PLANT).build();
    public static final ItemType INFESTED_DEEPSLATE = builder("infested_deepslate").setMaxAmount(64).setPlacedType(StateTypes.INFESTED_DEEPSLATE).build();
    public static final ItemType BUCKET = builder("bucket").setMaxAmount(16).build();
    public static final ItemType MILK_BUCKET = builder("milk_bucket").setMaxAmount(1).setCraftRemainder(ItemTypes.BUCKET).build();
    public static final ItemType WATER_BUCKET = builder("water_bucket").setMaxAmount(1).setCraftRemainder(ItemTypes.BUCKET).build();
    public static final ItemType LAVA_BUCKET = builder("lava_bucket").setMaxAmount(1).setCraftRemainder(ItemTypes.BUCKET).setAttributes(ItemAttribute.FUEL).build();
    public static final ItemType WARPED_BUTTON = builder("warped_button").setMaxAmount(64).setPlacedType(StateTypes.WARPED_BUTTON).build();
    public static final ItemType OAK_TRAPDOOR = builder("oak_trapdoor").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.OAK_TRAPDOOR).build();
    public static final ItemType BLACK_STAINED_GLASS = builder("black_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.BLACK_STAINED_GLASS).build();
    public static final ItemType GOLDEN_HELMET = builder("golden_helmet").setMaxAmount(1).setMaxDurability(77).build();
    public static final ItemType DARK_OAK_PRESSURE_PLATE = builder("dark_oak_pressure_plate").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DARK_OAK_PRESSURE_PLATE).build();
    public static final ItemType WEATHERED_CUT_COPPER_STAIRS = builder("weathered_cut_copper_stairs").setMaxAmount(64).setPlacedType(StateTypes.WEATHERED_CUT_COPPER_STAIRS).build();
    public static final ItemType CUT_RED_SANDSTONE_SLAB = builder("cut_red_sandstone_slab").setMaxAmount(64).setPlacedType(StateTypes.CUT_RED_SANDSTONE_SLAB).build();
    public static final ItemType LIME_BED = builder("lime_bed").setMaxAmount(1).setPlacedType(StateTypes.LIME_BED).build();
    public static final ItemType BLAST_FURNACE = builder("blast_furnace").setMaxAmount(64).setPlacedType(StateTypes.BLAST_FURNACE).build();
    public static final ItemType SPONGE = builder("sponge").setMaxAmount(64).setPlacedType(StateTypes.SPONGE).build();
    public static final ItemType CARTOGRAPHY_TABLE = builder("cartography_table").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.CARTOGRAPHY_TABLE).build();
    public static final ItemType NETHERITE_INGOT = builder("netherite_ingot").setMaxAmount(64).setAttributes(ItemAttribute.FIRE_RESISTANT).build();
    public static final ItemType LIGHT_GRAY_DYE = builder("light_gray_dye").setMaxAmount(64).build();
    public static final ItemType DAYLIGHT_DETECTOR = builder("daylight_detector").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DAYLIGHT_DETECTOR).build();
    public static final ItemType SLIME_SPAWN_EGG = builder("slime_spawn_egg").setMaxAmount(64).build();
    public static final ItemType BEETROOT_SOUP = builder("beetroot_soup").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType RAW_COPPER_BLOCK = builder("raw_copper_block").setMaxAmount(64).setPlacedType(StateTypes.RAW_COPPER_BLOCK).build();
    public static final ItemType LIGHT_GRAY_CARPET = builder("light_gray_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.LIGHT_GRAY_CARPET).build();
    public static final ItemType MUSIC_DISC_WARD = builder("music_disc_ward").setMaxAmount(1).setAttributes(ItemAttribute.MUSIC_DISC).build();
    public static final ItemType GRASS = builder("grass").setMaxAmount(64).setPlacedType(StateTypes.GRASS).build();
    public static final ItemType END_CRYSTAL = builder("end_crystal").setMaxAmount(64).build();
    public static final ItemType VINDICATOR_SPAWN_EGG = builder("vindicator_spawn_egg").setMaxAmount(64).build();
    public static final ItemType WHEAT = builder("wheat").setMaxAmount(64).build();
    public static final ItemType END_ROD = builder("end_rod").setMaxAmount(64).setPlacedType(StateTypes.END_ROD).build();
    public static final ItemType DEEPSLATE_COAL_ORE = builder("deepslate_coal_ore").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_COAL_ORE).build();
    public static final ItemType PHANTOM_SPAWN_EGG = builder("phantom_spawn_egg").setMaxAmount(64).build();
    public static final ItemType STONE_PICKAXE = builder("stone_pickaxe").setMaxAmount(1).setAttributes(ItemAttribute.PICKAXE, ItemAttribute.STONE_TIER).setMaxDurability(131).build();
    public static final ItemType IRON_HELMET = builder("iron_helmet").setMaxAmount(1).setMaxDurability(165).build();
    public static final ItemType GUARDIAN_SPAWN_EGG = builder("guardian_spawn_egg").setMaxAmount(64).build();
    public static final ItemType PINK_STAINED_GLASS = builder("pink_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.PINK_STAINED_GLASS).build();
    public static final ItemType PISTON = builder("piston").setMaxAmount(64).setPlacedType(StateTypes.PISTON).build();
    public static final ItemType DEAD_FIRE_CORAL_BLOCK = builder("dead_fire_coral_block").setMaxAmount(64).setPlacedType(StateTypes.DEAD_FIRE_CORAL_BLOCK).build();
    public static final ItemType CYAN_CANDLE = builder("cyan_candle").setMaxAmount(64).setPlacedType(StateTypes.CYAN_CANDLE).build();
    public static final ItemType WAXED_CUT_COPPER = builder("waxed_cut_copper").setMaxAmount(64).setPlacedType(StateTypes.WAXED_CUT_COPPER).build();
    public static final ItemType MELON_SLICE = builder("melon_slice").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType ENDER_CHEST = builder("ender_chest").setMaxAmount(64).setPlacedType(StateTypes.ENDER_CHEST).build();
    public static final ItemType KELP = builder("kelp").setMaxAmount(64).setPlacedType(StateTypes.KELP).build();
    public static final ItemType LIGHT_GRAY_WOOL = builder("light_gray_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.LIGHT_GRAY_WOOL).build();
    public static final ItemType SUNFLOWER = builder("sunflower").setMaxAmount(64).setPlacedType(StateTypes.SUNFLOWER).build();
    public static final ItemType LIGHTNING_ROD = builder("lightning_rod").setMaxAmount(64).setPlacedType(StateTypes.LIGHTNING_ROD).build();
    public static final ItemType BROWN_BED = builder("brown_bed").setMaxAmount(1).setPlacedType(StateTypes.BROWN_BED).build();
    public static final ItemType RAW_IRON_BLOCK = builder("raw_iron_block").setMaxAmount(64).setPlacedType(StateTypes.RAW_IRON_BLOCK).build();
    public static final ItemType HEART_OF_THE_SEA = builder("heart_of_the_sea").setMaxAmount(64).build();
    public static final ItemType POLISHED_BLACKSTONE_BRICKS = builder("polished_blackstone_bricks").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_BLACKSTONE_BRICKS).build();
    public static final ItemType SPYGLASS = builder("spyglass").setMaxAmount(1).build();
    public static final ItemType JACK_O_LANTERN = builder("jack_o_lantern").setMaxAmount(64).setPlacedType(StateTypes.JACK_O_LANTERN).build();
    public static final ItemType POLISHED_GRANITE = builder("polished_granite").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_GRANITE).build();
    public static final ItemType SMOOTH_RED_SANDSTONE = builder("smooth_red_sandstone").setMaxAmount(64).setPlacedType(StateTypes.SMOOTH_RED_SANDSTONE).build();
    public static final ItemType DEAD_BUBBLE_CORAL_FAN = builder("dead_bubble_coral_fan").setMaxAmount(64).setPlacedType(StateTypes.DEAD_BUBBLE_CORAL_FAN).build();
    public static final ItemType FURNACE = builder("furnace").setMaxAmount(64).setPlacedType(StateTypes.FURNACE).build();
    public static final ItemType POLISHED_DEEPSLATE = builder("polished_deepslate").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_DEEPSLATE).build();
    public static final ItemType SPORE_BLOSSOM = builder("spore_blossom").setMaxAmount(64).setPlacedType(StateTypes.SPORE_BLOSSOM).build();
    public static final ItemType RED_STAINED_GLASS = builder("red_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.RED_STAINED_GLASS).build();
    public static final ItemType STONE_SLAB = builder("stone_slab").setMaxAmount(64).setPlacedType(StateTypes.STONE_SLAB).build();
    public static final ItemType DANDELION = builder("dandelion").setMaxAmount(64).setPlacedType(StateTypes.DANDELION).build();
    public static final ItemType PINK_BED = builder("pink_bed").setMaxAmount(1).setPlacedType(StateTypes.PINK_BED).build();
    public static final ItemType ZOMBIFIED_PIGLIN_SPAWN_EGG = builder("zombified_piglin_spawn_egg").setMaxAmount(64).build();
    public static final ItemType ROSE_BUSH = builder("rose_bush").setMaxAmount(64).setPlacedType(StateTypes.ROSE_BUSH).build();
    public static final ItemType BLAZE_ROD = builder("blaze_rod").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).build();
    public static final ItemType IRON_TRAPDOOR = builder("iron_trapdoor").setMaxAmount(64).setPlacedType(StateTypes.IRON_TRAPDOOR).build();
    public static final ItemType DROWNED_SPAWN_EGG = builder("drowned_spawn_egg").setMaxAmount(64).build();
    public static final ItemType STONE_PRESSURE_PLATE = builder("stone_pressure_plate").setMaxAmount(64).setPlacedType(StateTypes.STONE_PRESSURE_PLATE).build();
    public static final ItemType IRON_BARS = builder("iron_bars").setMaxAmount(64).setPlacedType(StateTypes.IRON_BARS).build();
    public static final ItemType SMOOTH_RED_SANDSTONE_STAIRS = builder("smooth_red_sandstone_stairs").setMaxAmount(64).setPlacedType(StateTypes.SMOOTH_RED_SANDSTONE_STAIRS).build();
    public static final ItemType QUARTZ_BRICKS = builder("quartz_bricks").setMaxAmount(64).setPlacedType(StateTypes.QUARTZ_BRICKS).build();
    public static final ItemType WHEAT_SEEDS = builder("wheat_seeds").setMaxAmount(64).setPlacedType(StateTypes.WHEAT).build();
    public static final ItemType BROWN_CONCRETE = builder("brown_concrete").setMaxAmount(64).setPlacedType(StateTypes.BROWN_CONCRETE).build();
    public static final ItemType CYAN_CONCRETE = builder("cyan_concrete").setMaxAmount(64).setPlacedType(StateTypes.CYAN_CONCRETE).build();
    public static final ItemType STONE_SHOVEL = builder("stone_shovel").setMaxAmount(1).setAttributes(ItemAttribute.SHOVEL, ItemAttribute.STONE_TIER).setMaxDurability(131).build();
    public static final ItemType STRIPPED_BIRCH_WOOD = builder("stripped_birch_wood").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.STRIPPED_BIRCH_WOOD).build();
    public static final ItemType BUBBLE_CORAL_BLOCK = builder("bubble_coral_block").setMaxAmount(64).setPlacedType(StateTypes.BUBBLE_CORAL_BLOCK).build();
    public static final ItemType ZOMBIE_HORSE_SPAWN_EGG = builder("zombie_horse_spawn_egg").setMaxAmount(64).build();
    public static final ItemType WAXED_OXIDIZED_CUT_COPPER_SLAB = builder("waxed_oxidized_cut_copper_slab").setMaxAmount(64).setPlacedType(StateTypes.WAXED_OXIDIZED_CUT_COPPER_SLAB).build();
    public static final ItemType GREEN_BANNER = builder("green_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.GREEN_BANNER).build();
    public static final ItemType LIGHT_GRAY_BANNER = builder("light_gray_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.LIGHT_GRAY_BANNER).build();
    public static final ItemType DIAMOND_SWORD = builder("diamond_sword").setMaxAmount(1).setAttributes(ItemAttribute.SWORD, ItemAttribute.DIAMOND_TIER).setMaxDurability(1561).build();
    public static final ItemType RABBIT_FOOT = builder("rabbit_foot").setMaxAmount(64).build();
    public static final ItemType NETHERITE_BLOCK = builder("netherite_block").setMaxAmount(64).setAttributes(ItemAttribute.FIRE_RESISTANT).setPlacedType(StateTypes.NETHERITE_BLOCK).build();
    public static final ItemType BAT_SPAWN_EGG = builder("bat_spawn_egg").setMaxAmount(64).build();
    public static final ItemType DIAMOND_HORSE_ARMOR = builder("diamond_horse_armor").setMaxAmount(1).build();
    public static final ItemType GLOWSTONE_DUST = builder("glowstone_dust").setMaxAmount(64).build();
    public static final ItemType CRIMSON_FENCE_GATE = builder("crimson_fence_gate").setMaxAmount(64).setPlacedType(StateTypes.CRIMSON_FENCE_GATE).build();
    public static final ItemType WHITE_CARPET = builder("white_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.WHITE_CARPET).build();
    public static final ItemType FLETCHING_TABLE = builder("fletching_table").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.FLETCHING_TABLE).build();
    public static final ItemType RED_CONCRETE = builder("red_concrete").setMaxAmount(64).setPlacedType(StateTypes.RED_CONCRETE).build();
    public static final ItemType COCOA_BEANS = builder("cocoa_beans").setMaxAmount(64).setPlacedType(StateTypes.COCOA).build();
    public static final ItemType CRACKED_POLISHED_BLACKSTONE_BRICKS = builder("cracked_polished_blackstone_bricks").setMaxAmount(64).setPlacedType(StateTypes.CRACKED_POLISHED_BLACKSTONE_BRICKS).build();
    public static final ItemType PURPLE_GLAZED_TERRACOTTA = builder("purple_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.PURPLE_GLAZED_TERRACOTTA).build();
    public static final ItemType ACACIA_SLAB = builder("acacia_slab").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ACACIA_SLAB).build();
    public static final ItemType WITHER_SKELETON_SKULL = builder("wither_skeleton_skull").setMaxAmount(64).setPlacedType(StateTypes.WITHER_SKELETON_SKULL).build();
    public static final ItemType FIRE_CORAL_FAN = builder("fire_coral_fan").setMaxAmount(64).setPlacedType(StateTypes.FIRE_CORAL_FAN).build();
    public static final ItemType HUSK_SPAWN_EGG = builder("husk_spawn_egg").setMaxAmount(64).build();
    public static final ItemType ZOMBIE_VILLAGER_SPAWN_EGG = builder("zombie_villager_spawn_egg").setMaxAmount(64).build();
    public static final ItemType BUBBLE_CORAL = builder("bubble_coral").setMaxAmount(64).setPlacedType(StateTypes.BUBBLE_CORAL).build();
    public static final ItemType DISPENSER = builder("dispenser").setMaxAmount(64).setPlacedType(StateTypes.DISPENSER).build();
    public static final ItemType STRIPPED_CRIMSON_STEM = builder("stripped_crimson_stem").setMaxAmount(64).setPlacedType(StateTypes.STRIPPED_CRIMSON_STEM).build();
    public static final ItemType LIME_WOOL = builder("lime_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.LIME_WOOL).build();
    public static final ItemType RAW_GOLD_BLOCK = builder("raw_gold_block").setMaxAmount(64).setPlacedType(StateTypes.RAW_GOLD_BLOCK).build();
    public static final ItemType REDSTONE_LAMP = builder("redstone_lamp").setMaxAmount(64).setPlacedType(StateTypes.REDSTONE_LAMP).build();
    public static final ItemType DIAMOND_AXE = builder("diamond_axe").setMaxAmount(1).setAttributes(ItemAttribute.AXE, ItemAttribute.DIAMOND_TIER).setMaxDurability(1561).build();
    public static final ItemType SOUL_LANTERN = builder("soul_lantern").setMaxAmount(64).setPlacedType(StateTypes.SOUL_LANTERN).build();
    public static final ItemType ORANGE_WOOL = builder("orange_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ORANGE_WOOL).build();
    public static final ItemType TURTLE_HELMET = builder("turtle_helmet").setMaxAmount(1).setMaxDurability(275).build();
    public static final ItemType JUNGLE_WOOD = builder("jungle_wood").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.JUNGLE_WOOD).build();
    public static final ItemType HEAVY_WEIGHTED_PRESSURE_PLATE = builder("heavy_weighted_pressure_plate").setMaxAmount(64).setPlacedType(StateTypes.HEAVY_WEIGHTED_PRESSURE_PLATE).build();
    public static final ItemType POTION = builder("potion").setMaxAmount(1).build();
    public static final ItemType GOLD_NUGGET = builder("gold_nugget").setMaxAmount(64).build();
    public static final ItemType RED_SANDSTONE_WALL = builder("red_sandstone_wall").setMaxAmount(64).setPlacedType(StateTypes.RED_SANDSTONE_WALL).build();
    public static final ItemType CRIMSON_DOOR = builder("crimson_door").setMaxAmount(64).setPlacedType(StateTypes.CRIMSON_DOOR).build();
    public static final ItemType WARPED_STEM = builder("warped_stem").setMaxAmount(64).setPlacedType(StateTypes.WARPED_STEM).build();
    public static final ItemType ACACIA_TRAPDOOR = builder("acacia_trapdoor").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ACACIA_TRAPDOOR).build();
    public static final ItemType MOSSY_COBBLESTONE_WALL = builder("mossy_cobblestone_wall").setMaxAmount(64).setPlacedType(StateTypes.MOSSY_COBBLESTONE_WALL).build();
    public static final ItemType POLISHED_BLACKSTONE_PRESSURE_PLATE = builder("polished_blackstone_pressure_plate").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_BLACKSTONE_PRESSURE_PLATE).build();
    public static final ItemType CHIPPED_ANVIL = builder("chipped_anvil").setMaxAmount(64).setPlacedType(StateTypes.CHIPPED_ANVIL).build();
    public static final ItemType DEEPSLATE_IRON_ORE = builder("deepslate_iron_ore").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_IRON_ORE).build();
    public static final ItemType PURPUR_PILLAR = builder("purpur_pillar").setMaxAmount(64).setPlacedType(StateTypes.PURPUR_PILLAR).build();
    public static final ItemType RED_STAINED_GLASS_PANE = builder("red_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.RED_STAINED_GLASS_PANE).build();
    public static final ItemType FEATHER = builder("feather").setMaxAmount(64).build();
    public static final ItemType TRADER_LLAMA_SPAWN_EGG = builder("trader_llama_spawn_egg").setMaxAmount(64).build();
    public static final ItemType ACACIA_STAIRS = builder("acacia_stairs").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ACACIA_STAIRS).build();
    public static final ItemType DROPPER = builder("dropper").setMaxAmount(64).setPlacedType(StateTypes.DROPPER).build();
    public static final ItemType DEEPSLATE_BRICK_SLAB = builder("deepslate_brick_slab").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_BRICK_SLAB).build();
    public static final ItemType LIGHT_BLUE_BANNER = builder("light_blue_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.LIGHT_BLUE_BANNER).build();
    public static final ItemType COBBLED_DEEPSLATE_STAIRS = builder("cobbled_deepslate_stairs").setMaxAmount(64).setPlacedType(StateTypes.COBBLED_DEEPSLATE_STAIRS).build();
    public static final ItemType PURPLE_BED = builder("purple_bed").setMaxAmount(1).setPlacedType(StateTypes.PURPLE_BED).build();
    public static final ItemType LIGHT_BLUE_WOOL = builder("light_blue_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.LIGHT_BLUE_WOOL).build();
    public static final ItemType POLISHED_BLACKSTONE_STAIRS = builder("polished_blackstone_stairs").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_BLACKSTONE_STAIRS).build();
    public static final ItemType LIGHT_BLUE_STAINED_GLASS = builder("light_blue_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.LIGHT_BLUE_STAINED_GLASS).build();
    public static final ItemType ACACIA_SAPLING = builder("acacia_sapling").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ACACIA_SAPLING).build();
    public static final ItemType FIREWORK_ROCKET = builder("firework_rocket").setMaxAmount(64).build();
    public static final ItemType WAXED_WEATHERED_CUT_COPPER_STAIRS = builder("waxed_weathered_cut_copper_stairs").setMaxAmount(64).setPlacedType(StateTypes.WAXED_WEATHERED_CUT_COPPER_STAIRS).build();
    public static final ItemType ORANGE_GLAZED_TERRACOTTA = builder("orange_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.ORANGE_GLAZED_TERRACOTTA).build();
    public static final ItemType BLACKSTONE_STAIRS = builder("blackstone_stairs").setMaxAmount(64).setPlacedType(StateTypes.BLACKSTONE_STAIRS).build();
    public static final ItemType CHISELED_POLISHED_BLACKSTONE = builder("chiseled_polished_blackstone").setMaxAmount(64).setPlacedType(StateTypes.CHISELED_POLISHED_BLACKSTONE).build();
    public static final ItemType BLACK_GLAZED_TERRACOTTA = builder("black_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.BLACK_GLAZED_TERRACOTTA).build();
    public static final ItemType PURPLE_CONCRETE = builder("purple_concrete").setMaxAmount(64).setPlacedType(StateTypes.PURPLE_CONCRETE).build();
    public static final ItemType COOKIE = builder("cookie").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType BOOKSHELF = builder("bookshelf").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BOOKSHELF).build();
    public static final ItemType ORANGE_STAINED_GLASS_PANE = builder("orange_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.ORANGE_STAINED_GLASS_PANE).build();
    public static final ItemType PINK_CARPET = builder("pink_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.PINK_CARPET).build();
    public static final ItemType CUT_COPPER_SLAB = builder("cut_copper_slab").setMaxAmount(64).setPlacedType(StateTypes.CUT_COPPER_SLAB).build();
    public static final ItemType BELL = builder("bell").setMaxAmount(64).setPlacedType(StateTypes.BELL).build();
    public static final ItemType WARPED_HYPHAE = builder("warped_hyphae").setMaxAmount(64).setPlacedType(StateTypes.WARPED_HYPHAE).build();
    public static final ItemType POLISHED_DEEPSLATE_SLAB = builder("polished_deepslate_slab").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_DEEPSLATE_SLAB).build();
    public static final ItemType INFESTED_MOSSY_STONE_BRICKS = builder("infested_mossy_stone_bricks").setMaxAmount(64).setPlacedType(StateTypes.INFESTED_MOSSY_STONE_BRICKS).build();
    public static final ItemType MOSSY_STONE_BRICK_STAIRS = builder("mossy_stone_brick_stairs").setMaxAmount(64).setPlacedType(StateTypes.MOSSY_STONE_BRICK_STAIRS).build();
    public static final ItemType WARPED_FUNGUS = builder("warped_fungus").setMaxAmount(64).setPlacedType(StateTypes.WARPED_FUNGUS).build();
    public static final ItemType STRIPPED_JUNGLE_LOG = builder("stripped_jungle_log").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.STRIPPED_JUNGLE_LOG).build();
    public static final ItemType OAK_BOAT = builder("oak_boat").setMaxAmount(1).setAttributes(ItemAttribute.FUEL).build();
    public static final ItemType NAUTILUS_SHELL = builder("nautilus_shell").setMaxAmount(64).build();
    public static final ItemType DEAD_BUSH = builder("dead_bush").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DEAD_BUSH).build();
    public static final ItemType DETECTOR_RAIL = builder("detector_rail").setMaxAmount(64).setPlacedType(StateTypes.DETECTOR_RAIL).build();
    public static final ItemType CARROT_ON_A_STICK = builder("carrot_on_a_stick").setMaxAmount(1).setMaxDurability(25).build();
    public static final ItemType SHIELD = builder("shield").setMaxAmount(1).setMaxDurability(336).build();
    public static final ItemType DAMAGED_ANVIL = builder("damaged_anvil").setMaxAmount(64).setPlacedType(StateTypes.DAMAGED_ANVIL).build();
    public static final ItemType ANVIL = builder("anvil").setMaxAmount(64).setPlacedType(StateTypes.ANVIL).build();
    public static final ItemType AZALEA_LEAVES = builder("azalea_leaves").setMaxAmount(64).setPlacedType(StateTypes.AZALEA_LEAVES).build();
    public static final ItemType TOTEM_OF_UNDYING = builder("totem_of_undying").setMaxAmount(1).build();
    public static final ItemType RED_DYE = builder("red_dye").setMaxAmount(64).build();
    public static final ItemType MAGENTA_STAINED_GLASS = builder("magenta_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.MAGENTA_STAINED_GLASS).build();
    public static final ItemType LAPIS_LAZULI = builder("lapis_lazuli").setMaxAmount(64).build();
    public static final ItemType MUSIC_DISC_WAIT = builder("music_disc_wait").setMaxAmount(1).setAttributes(ItemAttribute.MUSIC_DISC).build();
    public static final ItemType NETHERITE_PICKAXE = builder("netherite_pickaxe").setMaxAmount(1).setAttributes(ItemAttribute.FIRE_RESISTANT, ItemAttribute.NETHERITE_TIER, ItemAttribute.PICKAXE).setMaxDurability(2031).build();
    public static final ItemType BUNDLE = builder("bundle").setMaxAmount(1).build();
    public static final ItemType MOOSHROOM_SPAWN_EGG = builder("mooshroom_spawn_egg").setMaxAmount(64).build();
    public static final ItemType MAP = builder("map").setMaxAmount(64).build();
    public static final ItemType DARK_OAK_SIGN = builder("dark_oak_sign").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DARK_OAK_SIGN).build();
    public static final ItemType NETHERITE_BOOTS = builder("netherite_boots").setMaxAmount(1).setAttributes(ItemAttribute.FIRE_RESISTANT).build();
    public static final ItemType BREAD = builder("bread").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType DEEPSLATE_TILE_SLAB = builder("deepslate_tile_slab").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_TILE_SLAB).build();
    public static final ItemType POLISHED_BLACKSTONE_BUTTON = builder("polished_blackstone_button").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_BLACKSTONE_BUTTON).build();
    public static final ItemType DEEPSLATE_REDSTONE_ORE = builder("deepslate_redstone_ore").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_REDSTONE_ORE).build();
    public static final ItemType TORCH = builder("torch").setMaxAmount(64).setPlacedType(StateTypes.TORCH).build();
    public static final ItemType LANTERN = builder("lantern").setMaxAmount(64).setPlacedType(StateTypes.LANTERN).build();
    public static final ItemType OAK_BUTTON = builder("oak_button").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.OAK_BUTTON).build();
    public static final ItemType IRON_SHOVEL = builder("iron_shovel").setMaxAmount(1).setAttributes(ItemAttribute.SHOVEL, ItemAttribute.IRON_TIER).setMaxDurability(250).build();
    public static final ItemType STRAY_SPAWN_EGG = builder("stray_spawn_egg").setMaxAmount(64).build();
    public static final ItemType SPAWNER = builder("spawner").setMaxAmount(64).setPlacedType(StateTypes.SPAWNER).build();
    public static final ItemType BLACK_BANNER = builder("black_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BLACK_BANNER).build();
    public static final ItemType CANDLE = builder("candle").setMaxAmount(64).setPlacedType(StateTypes.CANDLE).build();
    public static final ItemType BROWN_WOOL = builder("brown_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BROWN_WOOL).build();
    public static final ItemType WARPED_WART_BLOCK = builder("warped_wart_block").setMaxAmount(64).setPlacedType(StateTypes.WARPED_WART_BLOCK).build();
    public static final ItemType COOKED_SALMON = builder("cooked_salmon").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType GREEN_CARPET = builder("green_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.GREEN_CARPET).build();
    public static final ItemType WAXED_EXPOSED_COPPER = builder("waxed_exposed_copper").setMaxAmount(64).setPlacedType(StateTypes.WAXED_EXPOSED_COPPER).build();
    public static final ItemType MOSSY_STONE_BRICKS = builder("mossy_stone_bricks").setMaxAmount(64).setPlacedType(StateTypes.MOSSY_STONE_BRICKS).build();
    public static final ItemType BIRCH_DOOR = builder("birch_door").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BIRCH_DOOR).build();
    public static final ItemType STRIPPED_ACACIA_WOOD = builder("stripped_acacia_wood").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.STRIPPED_ACACIA_WOOD).build();
    public static final ItemType COW_SPAWN_EGG = builder("cow_spawn_egg").setMaxAmount(64).build();
    public static final ItemType MUSIC_DISC_13 = builder("music_disc_13").setMaxAmount(1).setAttributes(ItemAttribute.MUSIC_DISC).build();
    public static final ItemType DIORITE_WALL = builder("diorite_wall").setMaxAmount(64).setPlacedType(StateTypes.DIORITE_WALL).build();
    public static final ItemType MUSIC_DISC_11 = builder("music_disc_11").setMaxAmount(1).setAttributes(ItemAttribute.MUSIC_DISC).build();
    public static final ItemType BLUE_CONCRETE = builder("blue_concrete").setMaxAmount(64).setPlacedType(StateTypes.BLUE_CONCRETE).build();
    public static final ItemType WOODEN_AXE = builder("wooden_axe").setMaxAmount(1).setAttributes(ItemAttribute.AXE, ItemAttribute.WOOD_TIER, ItemAttribute.FUEL).setMaxDurability(59).build();
    public static final ItemType VEX_SPAWN_EGG = builder("vex_spawn_egg").setMaxAmount(64).build();
    public static final ItemType BRAIN_CORAL = builder("brain_coral").setMaxAmount(64).setPlacedType(StateTypes.BRAIN_CORAL).build();
    public static final ItemType SHEARS = builder("shears").setMaxAmount(1).setMaxDurability(238).build();
    public static final ItemType SPRUCE_PLANKS = builder("spruce_planks").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.SPRUCE_PLANKS).build();
    public static final ItemType WARPED_FENCE = builder("warped_fence").setMaxAmount(64).setPlacedType(StateTypes.WARPED_FENCE).build();
    public static final ItemType SPLASH_POTION = builder("splash_potion").setMaxAmount(1).build();
    public static final ItemType LIGHT_BLUE_GLAZED_TERRACOTTA = builder("light_blue_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.LIGHT_BLUE_GLAZED_TERRACOTTA).build();
    public static final ItemType WRITTEN_BOOK = builder("written_book").setMaxAmount(16).build();
    public static final ItemType CYAN_STAINED_GLASS_PANE = builder("cyan_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.CYAN_STAINED_GLASS_PANE).build();
    public static final ItemType GHAST_TEAR = builder("ghast_tear").setMaxAmount(64).build();
    public static final ItemType GLASS_PANE = builder("glass_pane").setMaxAmount(64).setPlacedType(StateTypes.GLASS_PANE).build();
    public static final ItemType NETHER_QUARTZ_ORE = builder("nether_quartz_ore").setMaxAmount(64).setPlacedType(StateTypes.NETHER_QUARTZ_ORE).build();
    public static final ItemType SEA_PICKLE = builder("sea_pickle").setMaxAmount(64).setPlacedType(StateTypes.SEA_PICKLE).build();
    public static final ItemType WAXED_OXIDIZED_CUT_COPPER = builder("waxed_oxidized_cut_copper").setMaxAmount(64).setPlacedType(StateTypes.WAXED_OXIDIZED_CUT_COPPER).build();
    public static final ItemType ENCHANTED_BOOK = builder("enchanted_book").setMaxAmount(1).build();
    public static final ItemType CARVED_PUMPKIN = builder("carved_pumpkin").setMaxAmount(64).setPlacedType(StateTypes.CARVED_PUMPKIN).build();
    public static final ItemType GRANITE_WALL = builder("granite_wall").setMaxAmount(64).setPlacedType(StateTypes.GRANITE_WALL).build();
    public static final ItemType REDSTONE_TORCH = builder("redstone_torch").setMaxAmount(64).setPlacedType(StateTypes.REDSTONE_TORCH).build();
    public static final ItemType SANDSTONE_WALL = builder("sandstone_wall").setMaxAmount(64).setPlacedType(StateTypes.SANDSTONE_WALL).build();
    public static final ItemType POLISHED_GRANITE_STAIRS = builder("polished_granite_stairs").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_GRANITE_STAIRS).build();
    public static final ItemType GLOW_BERRIES = builder("glow_berries").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).setPlacedType(StateTypes.CAVE_VINES).build();
    public static final ItemType DEAD_HORN_CORAL = builder("dead_horn_coral").setMaxAmount(64).setPlacedType(StateTypes.DEAD_HORN_CORAL).build();
    public static final ItemType DIRT = builder("dirt").setMaxAmount(64).setPlacedType(StateTypes.DIRT).build();
    public static final ItemType ORANGE_CONCRETE_POWDER = builder("orange_concrete_powder").setMaxAmount(64).setPlacedType(StateTypes.ORANGE_CONCRETE_POWDER).build();
    public static final ItemType DARK_OAK_WOOD = builder("dark_oak_wood").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DARK_OAK_WOOD).build();
    public static final ItemType AZALEA = builder("azalea").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.AZALEA).build();
    public static final ItemType LEVER = builder("lever").setMaxAmount(64).setPlacedType(StateTypes.LEVER).build();
    public static final ItemType BLUE_BED = builder("blue_bed").setMaxAmount(1).setPlacedType(StateTypes.BLUE_BED).build();
    public static final ItemType ACACIA_BOAT = builder("acacia_boat").setMaxAmount(1).setAttributes(ItemAttribute.FUEL).build();
    public static final ItemType ACACIA_PRESSURE_PLATE = builder("acacia_pressure_plate").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ACACIA_PRESSURE_PLATE).build();
    public static final ItemType ANDESITE_STAIRS = builder("andesite_stairs").setMaxAmount(64).setPlacedType(StateTypes.ANDESITE_STAIRS).build();
    public static final ItemType SKELETON_HORSE_SPAWN_EGG = builder("skeleton_horse_spawn_egg").setMaxAmount(64).build();
    public static final ItemType DARK_OAK_FENCE_GATE = builder("dark_oak_fence_gate").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DARK_OAK_FENCE_GATE).build();
    public static final ItemType COMPASS = builder("compass").setMaxAmount(64).build();
    public static final ItemType POLISHED_ANDESITE_STAIRS = builder("polished_andesite_stairs").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_ANDESITE_STAIRS).build();
    public static final ItemType COAL = builder("coal").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).build();
    public static final ItemType WAXED_EXPOSED_CUT_COPPER = builder("waxed_exposed_cut_copper").setMaxAmount(64).setPlacedType(StateTypes.WAXED_EXPOSED_CUT_COPPER).build();
    public static final ItemType JUNGLE_PRESSURE_PLATE = builder("jungle_pressure_plate").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.JUNGLE_PRESSURE_PLATE).build();
    public static final ItemType SMOOTH_QUARTZ_SLAB = builder("smooth_quartz_slab").setMaxAmount(64).setPlacedType(StateTypes.SMOOTH_QUARTZ_SLAB).build();
    public static final ItemType CYAN_BED = builder("cyan_bed").setMaxAmount(1).setPlacedType(StateTypes.CYAN_BED).build();
    public static final ItemType STRIPPED_ACACIA_LOG = builder("stripped_acacia_log").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.STRIPPED_ACACIA_LOG).build();
    public static final ItemType GOLDEN_PICKAXE = builder("golden_pickaxe").setMaxAmount(1).setAttributes(ItemAttribute.PICKAXE, ItemAttribute.PICKAXE, ItemAttribute.GOLD_TIER).setMaxDurability(32).build();
    public static final ItemType SWEET_BERRIES = builder("sweet_berries").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).setPlacedType(StateTypes.SWEET_BERRY_BUSH).build();
    public static final ItemType BOW = builder("bow").setMaxAmount(1).setAttributes(ItemAttribute.FUEL).setMaxDurability(384).build();
    public static final ItemType CRIMSON_ROOTS = builder("crimson_roots").setMaxAmount(64).setPlacedType(StateTypes.CRIMSON_ROOTS).build();
    public static final ItemType SMOOTH_SANDSTONE = builder("smooth_sandstone").setMaxAmount(64).setPlacedType(StateTypes.SMOOTH_SANDSTONE).build();
    public static final ItemType DEEPSLATE_BRICKS = builder("deepslate_bricks").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_BRICKS).build();
    public static final ItemType COBBLESTONE = builder("cobblestone").setMaxAmount(64).setPlacedType(StateTypes.COBBLESTONE).build();
    public static final ItemType REDSTONE_BLOCK = builder("redstone_block").setMaxAmount(64).setPlacedType(StateTypes.REDSTONE_BLOCK).build();
    public static final ItemType COOKED_COD = builder("cooked_cod").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType BONE_MEAL = builder("bone_meal").setMaxAmount(64).build();
    public static final ItemType HONEYCOMB_BLOCK = builder("honeycomb_block").setMaxAmount(64).setPlacedType(StateTypes.HONEYCOMB_BLOCK).build();
    public static final ItemType BRICK = builder("brick").setMaxAmount(64).build();
    public static final ItemType YELLOW_DYE = builder("yellow_dye").setMaxAmount(64).build();
    public static final ItemType CYAN_STAINED_GLASS = builder("cyan_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.CYAN_STAINED_GLASS).build();
    public static final ItemType BRICKS = builder("bricks").setMaxAmount(64).setPlacedType(StateTypes.BRICKS).build();
    public static final ItemType NETHERITE_CHESTPLATE = builder("netherite_chestplate").setMaxAmount(1).setAttributes(ItemAttribute.FIRE_RESISTANT).build();
    public static final ItemType ORANGE_BED = builder("orange_bed").setMaxAmount(1).setPlacedType(StateTypes.ORANGE_BED).build();
    public static final ItemType PETRIFIED_OAK_SLAB = builder("petrified_oak_slab").setMaxAmount(64).setPlacedType(StateTypes.PETRIFIED_OAK_SLAB).build();
    public static final ItemType SALMON_SPAWN_EGG = builder("salmon_spawn_egg").setMaxAmount(64).build();
    public static final ItemType LEATHER_HORSE_ARMOR = builder("leather_horse_armor").setMaxAmount(1).build();
    public static final ItemType ORANGE_BANNER = builder("orange_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ORANGE_BANNER).build();
    public static final ItemType TUBE_CORAL = builder("tube_coral").setMaxAmount(64).setPlacedType(StateTypes.TUBE_CORAL).build();
    public static final ItemType PIG_SPAWN_EGG = builder("pig_spawn_egg").setMaxAmount(64).build();
    public static final ItemType STONE_BRICK_SLAB = builder("stone_brick_slab").setMaxAmount(64).setPlacedType(StateTypes.STONE_BRICK_SLAB).build();
    public static final ItemType DARK_PRISMARINE = builder("dark_prismarine").setMaxAmount(64).setPlacedType(StateTypes.DARK_PRISMARINE).build();
    public static final ItemType PAINTING = builder("painting").setMaxAmount(64).build();
    public static final ItemType PUMPKIN = builder("pumpkin").setMaxAmount(64).setPlacedType(StateTypes.PUMPKIN).build();
    public static final ItemType TRAPPED_CHEST = builder("trapped_chest").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.TRAPPED_CHEST).build();
    public static final ItemType BROWN_SHULKER_BOX = builder("brown_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.BROWN_SHULKER_BOX).build();
    public static final ItemType ORANGE_CONCRETE = builder("orange_concrete").setMaxAmount(64).setPlacedType(StateTypes.ORANGE_CONCRETE).build();
    public static final ItemType PUMPKIN_SEEDS = builder("pumpkin_seeds").setMaxAmount(64).setPlacedType(StateTypes.PUMPKIN_STEM).build();
    public static final ItemType OAK_SAPLING = builder("oak_sapling").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.OAK_SAPLING).build();
    public static final ItemType BROWN_STAINED_GLASS_PANE = builder("brown_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.BROWN_STAINED_GLASS_PANE).build();
    public static final ItemType NETHER_BRICK_FENCE = builder("nether_brick_fence").setMaxAmount(64).setPlacedType(StateTypes.NETHER_BRICK_FENCE).build();
    public static final ItemType WHITE_BED = builder("white_bed").setMaxAmount(1).setPlacedType(StateTypes.WHITE_BED).build();
    public static final ItemType BIRCH_SLAB = builder("birch_slab").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BIRCH_SLAB).build();
    public static final ItemType LILY_PAD = builder("lily_pad").setMaxAmount(64).setPlacedType(StateTypes.LILY_PAD).build();
    public static final ItemType OBSERVER = builder("observer").setMaxAmount(64).setPlacedType(StateTypes.OBSERVER).build();
    public static final ItemType PODZOL = builder("podzol").setMaxAmount(64).setPlacedType(StateTypes.PODZOL).build();
    public static final ItemType CYAN_WOOL = builder("cyan_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.CYAN_WOOL).build();
    public static final ItemType STICK = builder("stick").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).build();
    public static final ItemType ANDESITE = builder("andesite").setMaxAmount(64).setPlacedType(StateTypes.ANDESITE).build();
    public static final ItemType DIAMOND_BOOTS = builder("diamond_boots").setMaxAmount(1).setMaxDurability(429).build();
    public static final ItemType FIRE_CORAL_BLOCK = builder("fire_coral_block").setMaxAmount(64).setPlacedType(StateTypes.FIRE_CORAL_BLOCK).build();
    public static final ItemType REPEATING_COMMAND_BLOCK = builder("repeating_command_block").setMaxAmount(64).setPlacedType(StateTypes.REPEATING_COMMAND_BLOCK).build();
    public static final ItemType PACKED_ICE = builder("packed_ice").setMaxAmount(64).setPlacedType(StateTypes.PACKED_ICE).build();
    public static final ItemType DEEPSLATE_TILES = builder("deepslate_tiles").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_TILES).build();
    public static final ItemType GRAY_CARPET = builder("gray_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.GRAY_CARPET).build();
    public static final ItemType ENCHANTING_TABLE = builder("enchanting_table").setMaxAmount(64).setPlacedType(StateTypes.ENCHANTING_TABLE).build();
    public static final ItemType COD_SPAWN_EGG = builder("cod_spawn_egg").setMaxAmount(64).build();
    public static final ItemType GRAY_STAINED_GLASS_PANE = builder("gray_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.GRAY_STAINED_GLASS_PANE).build();
    public static final ItemType GLOW_INK_SAC = builder("glow_ink_sac").setMaxAmount(64).build();
    public static final ItemType EXPOSED_CUT_COPPER = builder("exposed_cut_copper").setMaxAmount(64).setPlacedType(StateTypes.EXPOSED_CUT_COPPER).build();
    public static final ItemType LIME_DYE = builder("lime_dye").setMaxAmount(64).build();
    public static final ItemType WAXED_CUT_COPPER_STAIRS = builder("waxed_cut_copper_stairs").setMaxAmount(64).setPlacedType(StateTypes.WAXED_CUT_COPPER_STAIRS).build();
    public static final ItemType ACACIA_SIGN = builder("acacia_sign").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ACACIA_SIGN).build();
    public static final ItemType POLISHED_BLACKSTONE_BRICK_WALL = builder("polished_blackstone_brick_wall").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_BLACKSTONE_BRICK_WALL).build();
    public static final ItemType RABBIT_STEW = builder("rabbit_stew").setMaxAmount(1).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType PEONY = builder("peony").setMaxAmount(64).setPlacedType(StateTypes.PEONY).build();
    public static final ItemType STONE_AXE = builder("stone_axe").setMaxAmount(1).setAttributes(ItemAttribute.AXE, ItemAttribute.STONE_TIER).setMaxDurability(131).build();
    public static final ItemType DARK_OAK_LOG = builder("dark_oak_log").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DARK_OAK_LOG).build();
    public static final ItemType POINTED_DRIPSTONE = builder("pointed_dripstone").setMaxAmount(64).setPlacedType(StateTypes.POINTED_DRIPSTONE).build();
    public static final ItemType ROOTED_DIRT = builder("rooted_dirt").setMaxAmount(64).setPlacedType(StateTypes.ROOTED_DIRT).build();
    public static final ItemType POPPY = builder("poppy").setMaxAmount(64).setPlacedType(StateTypes.POPPY).build();
    public static final ItemType NETHERITE_SCRAP = builder("netherite_scrap").setMaxAmount(64).setAttributes(ItemAttribute.FIRE_RESISTANT).build();
    public static final ItemType RED_NETHER_BRICK_WALL = builder("red_nether_brick_wall").setMaxAmount(64).setPlacedType(StateTypes.RED_NETHER_BRICK_WALL).build();
    public static final ItemType ENDER_EYE = builder("ender_eye").setMaxAmount(64).build();
    public static final ItemType STONE_SWORD = builder("stone_sword").setMaxAmount(1).setAttributes(ItemAttribute.SWORD, ItemAttribute.STONE_TIER).setMaxDurability(131).build();
    public static final ItemType CUT_SANDSTONE = builder("cut_sandstone").setMaxAmount(64).setPlacedType(StateTypes.CUT_SANDSTONE).build();
    public static final ItemType CHEST_MINECART = builder("chest_minecart").setMaxAmount(1).build();
    public static final ItemType STONE_BRICK_STAIRS = builder("stone_brick_stairs").setMaxAmount(64).setPlacedType(StateTypes.STONE_BRICK_STAIRS).build();
    public static final ItemType JUNGLE_SLAB = builder("jungle_slab").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.JUNGLE_SLAB).build();
    public static final ItemType CACTUS = builder("cactus").setMaxAmount(64).setPlacedType(StateTypes.CACTUS).build();
    public static final ItemType SPRUCE_TRAPDOOR = builder("spruce_trapdoor").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.SPRUCE_TRAPDOOR).build();
    public static final ItemType NAME_TAG = builder("name_tag").setMaxAmount(64).build();
    public static final ItemType SPRUCE_LOG = builder("spruce_log").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.SPRUCE_LOG).build();
    public static final ItemType BLACK_TERRACOTTA = builder("black_terracotta").setMaxAmount(64).setPlacedType(StateTypes.BLACK_TERRACOTTA).build();
    public static final ItemType GRAY_GLAZED_TERRACOTTA = builder("gray_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.GRAY_GLAZED_TERRACOTTA).build();
    public static final ItemType BIRCH_BUTTON = builder("birch_button").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BIRCH_BUTTON).build();
    public static final ItemType RED_SANDSTONE_SLAB = builder("red_sandstone_slab").setMaxAmount(64).setPlacedType(StateTypes.RED_SANDSTONE_SLAB).build();
    public static final ItemType OXIDIZED_CUT_COPPER_STAIRS = builder("oxidized_cut_copper_stairs").setMaxAmount(64).setPlacedType(StateTypes.OXIDIZED_CUT_COPPER_STAIRS).build();
    public static final ItemType ORANGE_TERRACOTTA = builder("orange_terracotta").setMaxAmount(64).setPlacedType(StateTypes.ORANGE_TERRACOTTA).build();
    public static final ItemType BROWN_DYE = builder("brown_dye").setMaxAmount(64).build();
    public static final ItemType OXIDIZED_CUT_COPPER = builder("oxidized_cut_copper").setMaxAmount(64).setPlacedType(StateTypes.OXIDIZED_CUT_COPPER).build();
    public static final ItemType LIGHT_GRAY_BED = builder("light_gray_bed").setMaxAmount(1).setPlacedType(StateTypes.LIGHT_GRAY_BED).build();
    public static final ItemType SOUL_CAMPFIRE = builder("soul_campfire").setMaxAmount(64).setPlacedType(StateTypes.SOUL_CAMPFIRE).build();
    public static final ItemType SALMON_BUCKET = builder("salmon_bucket").setMaxAmount(1).build();
    public static final ItemType BRICK_SLAB = builder("brick_slab").setMaxAmount(64).setPlacedType(StateTypes.BRICK_SLAB).build();
    public static final ItemType SPRUCE_LEAVES = builder("spruce_leaves").setMaxAmount(64).setPlacedType(StateTypes.SPRUCE_LEAVES).build();
    public static final ItemType COMMAND_BLOCK_MINECART = builder("command_block_minecart").setMaxAmount(1).build();
    public static final ItemType LIGHT_GRAY_TERRACOTTA = builder("light_gray_terracotta").setMaxAmount(64).setPlacedType(StateTypes.LIGHT_GRAY_TERRACOTTA).build();
    public static final ItemType SPRUCE_SLAB = builder("spruce_slab").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.SPRUCE_SLAB).build();
    public static final ItemType RED_NETHER_BRICK_STAIRS = builder("red_nether_brick_stairs").setMaxAmount(64).setPlacedType(StateTypes.RED_NETHER_BRICK_STAIRS).build();
    public static final ItemType YELLOW_CANDLE = builder("yellow_candle").setMaxAmount(64).setPlacedType(StateTypes.YELLOW_CANDLE).build();
    public static final ItemType GRAY_TERRACOTTA = builder("gray_terracotta").setMaxAmount(64).setPlacedType(StateTypes.GRAY_TERRACOTTA).build();
    public static final ItemType SEA_LANTERN = builder("sea_lantern").setMaxAmount(64).setPlacedType(StateTypes.SEA_LANTERN).build();
    public static final ItemType ICE = builder("ice").setMaxAmount(64).setPlacedType(StateTypes.ICE).build();
    public static final ItemType GREEN_SHULKER_BOX = builder("green_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.GREEN_SHULKER_BOX).build();
    public static final ItemType OAK_DOOR = builder("oak_door").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.OAK_DOOR).build();
    public static final ItemType DARK_OAK_STAIRS = builder("dark_oak_stairs").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DARK_OAK_STAIRS).build();
    public static final ItemType BLUE_SHULKER_BOX = builder("blue_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.BLUE_SHULKER_BOX).build();
    public static final ItemType IRON_AXE = builder("iron_axe").setMaxAmount(1).setAttributes(ItemAttribute.AXE, ItemAttribute.IRON_TIER).setMaxDurability(250).build();
    public static final ItemType SMOKER = builder("smoker").setMaxAmount(64).setPlacedType(StateTypes.SMOKER).build();
    public static final ItemType LOOM = builder("loom").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.LOOM).build();
    public static final ItemType POLISHED_BLACKSTONE = builder("polished_blackstone").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_BLACKSTONE).build();
    public static final ItemType NETHER_WART = builder("nether_wart").setMaxAmount(64).setPlacedType(StateTypes.NETHER_WART).build();
    public static final ItemType OAK_LEAVES = builder("oak_leaves").setMaxAmount(64).setPlacedType(StateTypes.OAK_LEAVES).build();
    public static final ItemType COBBLED_DEEPSLATE_SLAB = builder("cobbled_deepslate_slab").setMaxAmount(64).setPlacedType(StateTypes.COBBLED_DEEPSLATE_SLAB).build();
    public static final ItemType COMPOSTER = builder("composter").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.COMPOSTER).build();
    public static final ItemType MUTTON = builder("mutton").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType COPPER_ORE = builder("copper_ore").setMaxAmount(64).setPlacedType(StateTypes.COPPER_ORE).build();
    public static final ItemType KNOWLEDGE_BOOK = builder("knowledge_book").setMaxAmount(1).build();
    public static final ItemType OBSIDIAN = builder("obsidian").setMaxAmount(64).setPlacedType(StateTypes.OBSIDIAN).build();
    public static final ItemType CYAN_CARPET = builder("cyan_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.CYAN_CARPET).build();
    public static final ItemType SKULL_BANNER_PATTERN = builder("skull_banner_pattern").setMaxAmount(1).build();
    public static final ItemType FIREWORK_STAR = builder("firework_star").setMaxAmount(64).build();
    public static final ItemType MUSIC_DISC_MELLOHI = builder("music_disc_mellohi").setMaxAmount(1).setAttributes(ItemAttribute.MUSIC_DISC).build();
    public static final ItemType PURPLE_CARPET = builder("purple_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.PURPLE_CARPET).build();
    public static final ItemType GOLDEN_HOE = builder("golden_hoe").setMaxAmount(1).setAttributes(ItemAttribute.HOE, ItemAttribute.GOLD_TIER).setMaxDurability(32).build();
    public static final ItemType COOKED_CHICKEN = builder("cooked_chicken").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType DOLPHIN_SPAWN_EGG = builder("dolphin_spawn_egg").setMaxAmount(64).build();
    public static final ItemType COARSE_DIRT = builder("coarse_dirt").setMaxAmount(64).setPlacedType(StateTypes.COARSE_DIRT).build();
    public static final ItemType WHITE_CANDLE = builder("white_candle").setMaxAmount(64).setPlacedType(StateTypes.WHITE_CANDLE).build();
    public static final ItemType DARK_PRISMARINE_STAIRS = builder("dark_prismarine_stairs").setMaxAmount(64).setPlacedType(StateTypes.DARK_PRISMARINE_STAIRS).build();
    public static final ItemType JUNGLE_BUTTON = builder("jungle_button").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.JUNGLE_BUTTON).build();
    public static final ItemType DEAD_TUBE_CORAL_BLOCK = builder("dead_tube_coral_block").setMaxAmount(64).setPlacedType(StateTypes.DEAD_TUBE_CORAL_BLOCK).build();
    public static final ItemType DARK_OAK_BOAT = builder("dark_oak_boat").setMaxAmount(1).setAttributes(ItemAttribute.FUEL).build();
    public static final ItemType COOKED_MUTTON = builder("cooked_mutton").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType JUNGLE_FENCE = builder("jungle_fence").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.JUNGLE_FENCE).build();
    public static final ItemType JUKEBOX = builder("jukebox").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.JUKEBOX).build();
    public static final ItemType PURPLE_STAINED_GLASS_PANE = builder("purple_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.PURPLE_STAINED_GLASS_PANE).build();
    public static final ItemType BIRCH_TRAPDOOR = builder("birch_trapdoor").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BIRCH_TRAPDOOR).build();
    public static final ItemType APPLE = builder("apple").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType ELDER_GUARDIAN_SPAWN_EGG = builder("elder_guardian_spawn_egg").setMaxAmount(64).build();
    public static final ItemType SPIDER_EYE = builder("spider_eye").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType ZOGLIN_SPAWN_EGG = builder("zoglin_spawn_egg").setMaxAmount(64).build();
    public static final ItemType PIGLIN_BANNER_PATTERN = builder("piglin_banner_pattern").setMaxAmount(1).build();
    public static final ItemType GOLDEN_BOOTS = builder("golden_boots").setMaxAmount(1).setMaxDurability(91).build();
    public static final ItemType LILY_OF_THE_VALLEY = builder("lily_of_the_valley").setMaxAmount(64).setPlacedType(StateTypes.LILY_OF_THE_VALLEY).build();
    public static final ItemType BLUE_ORCHID = builder("blue_orchid").setMaxAmount(64).setPlacedType(StateTypes.BLUE_ORCHID).build();
    public static final ItemType PUMPKIN_PIE = builder("pumpkin_pie").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType RED_SANDSTONE_STAIRS = builder("red_sandstone_stairs").setMaxAmount(64).setPlacedType(StateTypes.RED_SANDSTONE_STAIRS).build();
    public static final ItemType SQUID_SPAWN_EGG = builder("squid_spawn_egg").setMaxAmount(64).build();
    public static final ItemType CRAFTING_TABLE = builder("crafting_table").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.CRAFTING_TABLE).build();
    public static final ItemType CAVE_SPIDER_SPAWN_EGG = builder("cave_spider_spawn_egg").setMaxAmount(64).build();
    public static final ItemType COBBLESTONE_STAIRS = builder("cobblestone_stairs").setMaxAmount(64).setPlacedType(StateTypes.COBBLESTONE_STAIRS).build();
    public static final ItemType BROWN_MUSHROOM_BLOCK = builder("brown_mushroom_block").setMaxAmount(64).setPlacedType(StateTypes.BROWN_MUSHROOM_BLOCK).build();
    public static final ItemType LIGHT_GRAY_CANDLE = builder("light_gray_candle").setMaxAmount(64).setPlacedType(StateTypes.LIGHT_GRAY_CANDLE).build();
    public static final ItemType DIAMOND_BLOCK = builder("diamond_block").setMaxAmount(64).setPlacedType(StateTypes.DIAMOND_BLOCK).build();
    public static final ItemType END_STONE_BRICKS = builder("end_stone_bricks").setMaxAmount(64).setPlacedType(StateTypes.END_STONE_BRICKS).build();
    public static final ItemType GOLDEN_CARROT = builder("golden_carrot").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType STONE = builder("stone").setMaxAmount(64).setPlacedType(StateTypes.STONE).build();
    public static final ItemType NETHER_BRICK_WALL = builder("nether_brick_wall").setMaxAmount(64).setPlacedType(StateTypes.NETHER_BRICK_WALL).build();
    public static final ItemType CRIMSON_SIGN = builder("crimson_sign").setMaxAmount(16).setPlacedType(StateTypes.CRIMSON_SIGN).build();
    public static final ItemType DARK_OAK_TRAPDOOR = builder("dark_oak_trapdoor").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DARK_OAK_TRAPDOOR).build();
    public static final ItemType PRISMARINE_WALL = builder("prismarine_wall").setMaxAmount(64).setPlacedType(StateTypes.PRISMARINE_WALL).build();
    public static final ItemType ENCHANTED_GOLDEN_APPLE = builder("enchanted_golden_apple").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType MAGMA_CREAM = builder("magma_cream").setMaxAmount(64).build();
    public static final ItemType GHAST_SPAWN_EGG = builder("ghast_spawn_egg").setMaxAmount(64).build();
    public static final ItemType PINK_BANNER = builder("pink_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.PINK_BANNER).build();
    public static final ItemType EXPOSED_CUT_COPPER_SLAB = builder("exposed_cut_copper_slab").setMaxAmount(64).setPlacedType(StateTypes.EXPOSED_CUT_COPPER_SLAB).build();
    public static final ItemType MELON_SEEDS = builder("melon_seeds").setMaxAmount(64).setPlacedType(StateTypes.MELON_STEM).build();
    public static final ItemType MUSIC_DISC_CAT = builder("music_disc_cat").setMaxAmount(1).setAttributes(ItemAttribute.MUSIC_DISC).build();
    public static final ItemType RED_SANDSTONE = builder("red_sandstone").setMaxAmount(64).setPlacedType(StateTypes.RED_SANDSTONE).build();
    public static final ItemType PURPLE_DYE = builder("purple_dye").setMaxAmount(64).build();
    public static final ItemType COBBLED_DEEPSLATE_WALL = builder("cobbled_deepslate_wall").setMaxAmount(64).setPlacedType(StateTypes.COBBLED_DEEPSLATE_WALL).build();
    public static final ItemType FIRE_CHARGE = builder("fire_charge").setMaxAmount(64).build();
    public static final ItemType CHISELED_RED_SANDSTONE = builder("chiseled_red_sandstone").setMaxAmount(64).setPlacedType(StateTypes.CHISELED_RED_SANDSTONE).build();
    public static final ItemType TUBE_CORAL_BLOCK = builder("tube_coral_block").setMaxAmount(64).setPlacedType(StateTypes.TUBE_CORAL_BLOCK).build();
    public static final ItemType SANDSTONE_STAIRS = builder("sandstone_stairs").setMaxAmount(64).setPlacedType(StateTypes.SANDSTONE_STAIRS).build();
    public static final ItemType POWDER_SNOW_BUCKET = builder("powder_snow_bucket").setMaxAmount(1).setPlacedType(StateTypes.POWDER_SNOW).build();
    public static final ItemType AXOLOTL_SPAWN_EGG = builder("axolotl_spawn_egg").setMaxAmount(64).build();
    public static final ItemType WHITE_SHULKER_BOX = builder("white_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.WHITE_SHULKER_BOX).build();
    public static final ItemType DEEPSLATE_BRICK_STAIRS = builder("deepslate_brick_stairs").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_BRICK_STAIRS).build();
    public static final ItemType FERN = builder("fern").setMaxAmount(64).setPlacedType(StateTypes.FERN).build();
    public static final ItemType SKELETON_SPAWN_EGG = builder("skeleton_spawn_egg").setMaxAmount(64).build();
    public static final ItemType PUFFERFISH_SPAWN_EGG = builder("pufferfish_spawn_egg").setMaxAmount(64).build();
    public static final ItemType GOAT_SPAWN_EGG = builder("goat_spawn_egg").setMaxAmount(64).build();
    public static final ItemType LIGHT_BLUE_CARPET = builder("light_blue_carpet").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.LIGHT_BLUE_CARPET).build();
    public static final ItemType DIORITE_SLAB = builder("diorite_slab").setMaxAmount(64).setPlacedType(StateTypes.DIORITE_SLAB).build();
    public static final ItemType LIME_BANNER = builder("lime_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.LIME_BANNER).build();
    public static final ItemType SOUL_SOIL = builder("soul_soil").setMaxAmount(64).setPlacedType(StateTypes.SOUL_SOIL).build();
    public static final ItemType GOLDEN_LEGGINGS = builder("golden_leggings").setMaxAmount(1).setMaxDurability(105).build();
    public static final ItemType DARK_OAK_SAPLING = builder("dark_oak_sapling").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DARK_OAK_SAPLING).build();
    public static final ItemType POLISHED_DIORITE_STAIRS = builder("polished_diorite_stairs").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_DIORITE_STAIRS).build();
    public static final ItemType ENDERMITE_SPAWN_EGG = builder("endermite_spawn_egg").setMaxAmount(64).build();
    public static final ItemType TUBE_CORAL_FAN = builder("tube_coral_fan").setMaxAmount(64).setPlacedType(StateTypes.TUBE_CORAL_FAN).build();
    public static final ItemType LIME_GLAZED_TERRACOTTA = builder("lime_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.LIME_GLAZED_TERRACOTTA).build();
    public static final ItemType MEDIUM_AMETHYST_BUD = builder("medium_amethyst_bud").setMaxAmount(64).setPlacedType(StateTypes.MEDIUM_AMETHYST_BUD).build();
    public static final ItemType MAGENTA_DYE = builder("magenta_dye").setMaxAmount(64).build();
    public static final ItemType CRIMSON_FUNGUS = builder("crimson_fungus").setMaxAmount(64).setPlacedType(StateTypes.CRIMSON_FUNGUS).build();
    public static final ItemType LEATHER = builder("leather").setMaxAmount(64).build();
    public static final ItemType CRACKED_NETHER_BRICKS = builder("cracked_nether_bricks").setMaxAmount(64).setPlacedType(StateTypes.CRACKED_NETHER_BRICKS).build();
    public static final ItemType BIRCH_BOAT = builder("birch_boat").setMaxAmount(1).setAttributes(ItemAttribute.FUEL).build();
    public static final ItemType NETHER_BRICK_STAIRS = builder("nether_brick_stairs").setMaxAmount(64).setPlacedType(StateTypes.NETHER_BRICK_STAIRS).build();
    public static final ItemType COMMAND_BLOCK = builder("command_block").setMaxAmount(64).setPlacedType(StateTypes.COMMAND_BLOCK).build();
    public static final ItemType WANDERING_TRADER_SPAWN_EGG = builder("wandering_trader_spawn_egg").setMaxAmount(64).build();
    public static final ItemType VILLAGER_SPAWN_EGG = builder("villager_spawn_egg").setMaxAmount(64).build();
    public static final ItemType TUFF = builder("tuff").setMaxAmount(64).setPlacedType(StateTypes.TUFF).build();
    public static final ItemType SNOWBALL = builder("snowball").setMaxAmount(16).build();
    public static final ItemType GRAY_CONCRETE = builder("gray_concrete").setMaxAmount(64).setPlacedType(StateTypes.GRAY_CONCRETE).build();
    public static final ItemType LIGHT_BLUE_SHULKER_BOX = builder("light_blue_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.LIGHT_BLUE_SHULKER_BOX).build();
    public static final ItemType SMOOTH_QUARTZ_STAIRS = builder("smooth_quartz_stairs").setMaxAmount(64).setPlacedType(StateTypes.SMOOTH_QUARTZ_STAIRS).build();
    public static final ItemType GREEN_CANDLE = builder("green_candle").setMaxAmount(64).setPlacedType(StateTypes.GREEN_CANDLE).build();
    public static final ItemType STONE_BRICK_WALL = builder("stone_brick_wall").setMaxAmount(64).setPlacedType(StateTypes.STONE_BRICK_WALL).build();
    public static final ItemType GLASS_BOTTLE = builder("glass_bottle").setMaxAmount(64).build();
    public static final ItemType DRAGON_BREATH = builder("dragon_breath").setMaxAmount(64).setCraftRemainder(ItemTypes.GLASS_BOTTLE).build();
    public static final ItemType HONEY_BOTTLE = builder("honey_bottle").setMaxAmount(16).setAttributes(ItemAttribute.EDIBLE).setCraftRemainder(ItemTypes.GLASS_BOTTLE).build();
    public static final ItemType RESPAWN_ANCHOR = builder("respawn_anchor").setMaxAmount(64).setPlacedType(StateTypes.RESPAWN_ANCHOR).build();
    public static final ItemType RED_BANNER = builder("red_banner").setMaxAmount(16).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.RED_BANNER).build();
    public static final ItemType CRIMSON_NYLIUM = builder("crimson_nylium").setMaxAmount(64).setPlacedType(StateTypes.CRIMSON_NYLIUM).build();
    public static final ItemType WEATHERED_CUT_COPPER = builder("weathered_cut_copper").setMaxAmount(64).setPlacedType(StateTypes.WEATHERED_CUT_COPPER).build();
    public static final ItemType ACACIA_WOOD = builder("acacia_wood").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.ACACIA_WOOD).build();
    public static final ItemType BEE_SPAWN_EGG = builder("bee_spawn_egg").setMaxAmount(64).build();
    public static final ItemType LARGE_AMETHYST_BUD = builder("large_amethyst_bud").setMaxAmount(64).setPlacedType(StateTypes.LARGE_AMETHYST_BUD).build();
    public static final ItemType LIME_STAINED_GLASS = builder("lime_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.LIME_STAINED_GLASS).build();
    public static final ItemType MAGENTA_SHULKER_BOX = builder("magenta_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.MAGENTA_SHULKER_BOX).build();
    public static final ItemType GLOBE_BANNER_PATTERN = builder("globe_banner_pattern").setMaxAmount(1).build();
    public static final ItemType POLISHED_DEEPSLATE_STAIRS = builder("polished_deepslate_stairs").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_DEEPSLATE_STAIRS).build();
    public static final ItemType TIPPED_ARROW = builder("tipped_arrow").setMaxAmount(64).build();
    public static final ItemType ORANGE_CANDLE = builder("orange_candle").setMaxAmount(64).setPlacedType(StateTypes.ORANGE_CANDLE).build();
    public static final ItemType YELLOW_STAINED_GLASS = builder("yellow_stained_glass").setMaxAmount(64).setPlacedType(StateTypes.YELLOW_STAINED_GLASS).build();
    public static final ItemType ENDER_PEARL = builder("ender_pearl").setMaxAmount(16).build();
    public static final ItemType DEEPSLATE_DIAMOND_ORE = builder("deepslate_diamond_ore").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_DIAMOND_ORE).build();
    public static final ItemType GOLDEN_APPLE = builder("golden_apple").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType PRISMARINE_SLAB = builder("prismarine_slab").setMaxAmount(64).setPlacedType(StateTypes.PRISMARINE_SLAB).build();
    public static final ItemType DRIED_KELP = builder("dried_kelp").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType EVOKER_SPAWN_EGG = builder("evoker_spawn_egg").setMaxAmount(64).build();
    public static final ItemType PRISMARINE_SHARD = builder("prismarine_shard").setMaxAmount(64).build();
    public static final ItemType LEAD = builder("lead").setMaxAmount(64).build();
    public static final ItemType LAPIS_BLOCK = builder("lapis_block").setMaxAmount(64).setPlacedType(StateTypes.LAPIS_BLOCK).build();
    public static final ItemType MAGENTA_WOOL = builder("magenta_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.MAGENTA_WOOL).build();
    public static final ItemType STONE_HOE = builder("stone_hoe").setMaxAmount(1).setAttributes(ItemAttribute.HOE, ItemAttribute.STONE_TIER).setMaxDurability(131).build();
    public static final ItemType BEETROOT_SEEDS = builder("beetroot_seeds").setMaxAmount(64).setPlacedType(StateTypes.BEETROOTS).build();
    public static final ItemType PANDA_SPAWN_EGG = builder("panda_spawn_egg").setMaxAmount(64).build();
    public static final ItemType CORNFLOWER = builder("cornflower").setMaxAmount(64).setPlacedType(StateTypes.CORNFLOWER).build();
    public static final ItemType SHULKER_SHELL = builder("shulker_shell").setMaxAmount(64).build();
    public static final ItemType OAK_FENCE_GATE = builder("oak_fence_gate").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.OAK_FENCE_GATE).build();
    public static final ItemType STRIPPED_JUNGLE_WOOD = builder("stripped_jungle_wood").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.STRIPPED_JUNGLE_WOOD).build();
    public static final ItemType ORANGE_SHULKER_BOX = builder("orange_shulker_box").setMaxAmount(1).setPlacedType(StateTypes.ORANGE_SHULKER_BOX).build();
    public static final ItemType IRON_DOOR = builder("iron_door").setMaxAmount(64).setPlacedType(StateTypes.IRON_DOOR).build();
    public static final ItemType SPRUCE_BOAT = builder("spruce_boat").setMaxAmount(1).setAttributes(ItemAttribute.FUEL).build();
    public static final ItemType SMOOTH_QUARTZ = builder("smooth_quartz").setMaxAmount(64).setPlacedType(StateTypes.SMOOTH_QUARTZ).build();
    public static final ItemType BARRIER = builder("barrier").setMaxAmount(64).setPlacedType(StateTypes.BARRIER).build();
    public static final ItemType GRAY_CANDLE = builder("gray_candle").setMaxAmount(64).setPlacedType(StateTypes.GRAY_CANDLE).build();
    public static final ItemType RABBIT_HIDE = builder("rabbit_hide").setMaxAmount(64).build();
    public static final ItemType PINK_WOOL = builder("pink_wool").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.PINK_WOOL).build();
    public static final ItemType PILLAGER_SPAWN_EGG = builder("pillager_spawn_egg").setMaxAmount(64).build();
    public static final ItemType CAMPFIRE = builder("campfire").setMaxAmount(64).setPlacedType(StateTypes.CAMPFIRE).build();
    public static final ItemType DEEPSLATE_BRICK_WALL = builder("deepslate_brick_wall").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_BRICK_WALL).build();
    public static final ItemType WITHER_ROSE = builder("wither_rose").setMaxAmount(64).setPlacedType(StateTypes.WITHER_ROSE).build();
    public static final ItemType LIGHT_GRAY_GLAZED_TERRACOTTA = builder("light_gray_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.LIGHT_GRAY_GLAZED_TERRACOTTA).build();
    public static final ItemType JUNGLE_BOAT = builder("jungle_boat").setMaxAmount(1).setAttributes(ItemAttribute.FUEL).build();
    public static final ItemType EXPOSED_CUT_COPPER_STAIRS = builder("exposed_cut_copper_stairs").setMaxAmount(64).setPlacedType(StateTypes.EXPOSED_CUT_COPPER_STAIRS).build();
    public static final ItemType SALMON = builder("salmon").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType FOX_SPAWN_EGG = builder("fox_spawn_egg").setMaxAmount(64).build();
    public static final ItemType DIAMOND_HOE = builder("diamond_hoe").setMaxAmount(1).setAttributes(ItemAttribute.HOE, ItemAttribute.DIAMOND_TIER).setMaxDurability(1561).build();
    public static final ItemType POLISHED_BLACKSTONE_BRICK_SLAB = builder("polished_blackstone_brick_slab").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_BLACKSTONE_BRICK_SLAB).build();
    public static final ItemType TWISTING_VINES = builder("twisting_vines").setMaxAmount(64).setPlacedType(StateTypes.TWISTING_VINES).build();
    public static final ItemType TURTLE_EGG = builder("turtle_egg").setMaxAmount(64).setPlacedType(StateTypes.TURTLE_EGG).build();
    public static final ItemType RED_GLAZED_TERRACOTTA = builder("red_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.RED_GLAZED_TERRACOTTA).build();
    public static final ItemType ITEM_FRAME = builder("item_frame").setMaxAmount(64).build();
    public static final ItemType RED_TULIP = builder("red_tulip").setMaxAmount(64).setPlacedType(StateTypes.RED_TULIP).build();
    public static final ItemType COAL_ORE = builder("coal_ore").setMaxAmount(64).setPlacedType(StateTypes.COAL_ORE).build();
    public static final ItemType BIRCH_SAPLING = builder("birch_sapling").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BIRCH_SAPLING).build();
    public static final ItemType POLISHED_ANDESITE = builder("polished_andesite").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_ANDESITE).build();
    public static final ItemType BRICK_STAIRS = builder("brick_stairs").setMaxAmount(64).setPlacedType(StateTypes.BRICK_STAIRS).build();
    public static final ItemType DIORITE_STAIRS = builder("diorite_stairs").setMaxAmount(64).setPlacedType(StateTypes.DIORITE_STAIRS).build();
    public static final ItemType SHROOMLIGHT = builder("shroomlight").setMaxAmount(64).setPlacedType(StateTypes.SHROOMLIGHT).build();
    public static final ItemType SPIDER_SPAWN_EGG = builder("spider_spawn_egg").setMaxAmount(64).build();
    public static final ItemType TRIPWIRE_HOOK = builder("tripwire_hook").setMaxAmount(64).setPlacedType(StateTypes.TRIPWIRE_HOOK).build();
    public static final ItemType STRIPPED_SPRUCE_WOOD = builder("stripped_spruce_wood").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.STRIPPED_SPRUCE_WOOD).build();
    public static final ItemType SPRUCE_BUTTON = builder("spruce_button").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.SPRUCE_BUTTON).build();
    public static final ItemType PAPER = builder("paper").setMaxAmount(64).build();
    public static final ItemType MAGMA_CUBE_SPAWN_EGG = builder("magma_cube_spawn_egg").setMaxAmount(64).build();
    public static final ItemType DEEPSLATE_TILE_WALL = builder("deepslate_tile_wall").setMaxAmount(64).setPlacedType(StateTypes.DEEPSLATE_TILE_WALL).build();
    public static final ItemType JUNGLE_LOG = builder("jungle_log").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.JUNGLE_LOG).build();
    public static final ItemType IRON_PICKAXE = builder("iron_pickaxe").setMaxAmount(1).setAttributes(ItemAttribute.PICKAXE, ItemAttribute.IRON_TIER).setMaxDurability(250).build();
    public static final ItemType DARK_OAK_SLAB = builder("dark_oak_slab").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.DARK_OAK_SLAB).build();
    public static final ItemType NETHERRACK = builder("netherrack").setMaxAmount(64).setPlacedType(StateTypes.NETHERRACK).build();
    public static final ItemType POLISHED_DEEPSLATE_WALL = builder("polished_deepslate_wall").setMaxAmount(64).setPlacedType(StateTypes.POLISHED_DEEPSLATE_WALL).build();
    public static final ItemType BIRCH_LOG = builder("birch_log").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.BIRCH_LOG).build();
    public static final ItemType LIGHT_WEIGHTED_PRESSURE_PLATE = builder("light_weighted_pressure_plate").setMaxAmount(64).setPlacedType(StateTypes.LIGHT_WEIGHTED_PRESSURE_PLATE).build();
    public static final ItemType WARPED_SIGN = builder("warped_sign").setMaxAmount(16).setPlacedType(StateTypes.WARPED_SIGN).build();
    public static final ItemType ACTIVATOR_RAIL = builder("activator_rail").setMaxAmount(64).setPlacedType(StateTypes.ACTIVATOR_RAIL).build();
    public static final ItemType PUFFERFISH = builder("pufferfish").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).build();
    public static final ItemType OAK_PLANKS = builder("oak_planks").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.OAK_PLANKS).build();
    public static final ItemType GOLD_ORE = builder("gold_ore").setMaxAmount(64).setPlacedType(StateTypes.GOLD_ORE).build();
    public static final ItemType SHULKER_BOX = builder("shulker_box").setMaxAmount(1).setPlacedType(StateTypes.SHULKER_BOX).build();
    public static final ItemType RAW_GOLD = builder("raw_gold").setMaxAmount(64).build();
    public static final ItemType CRIMSON_PRESSURE_PLATE = builder("crimson_pressure_plate").setMaxAmount(64).setPlacedType(StateTypes.CRIMSON_PRESSURE_PLATE).build();
    public static final ItemType FERMENTED_SPIDER_EYE = builder("fermented_spider_eye").setMaxAmount(64).build();
    public static final ItemType CHISELED_QUARTZ_BLOCK = builder("chiseled_quartz_block").setMaxAmount(64).setPlacedType(StateTypes.CHISELED_QUARTZ_BLOCK).build();
    public static final ItemType HOPPER_MINECART = builder("hopper_minecart").setMaxAmount(1).build();
    public static final ItemType FLOWERING_AZALEA = builder("flowering_azalea").setMaxAmount(64).setAttributes(ItemAttribute.FUEL).setPlacedType(StateTypes.FLOWERING_AZALEA).build();
    public static final ItemType YELLOW_GLAZED_TERRACOTTA = builder("yellow_glazed_terracotta").setMaxAmount(64).setPlacedType(StateTypes.YELLOW_GLAZED_TERRACOTTA).build();
    public static final ItemType CYAN_DYE = builder("cyan_dye").setMaxAmount(64).build();
    public static final ItemType QUARTZ_SLAB = builder("quartz_slab").setMaxAmount(64).setPlacedType(StateTypes.QUARTZ_SLAB).build();
    public static final ItemType BLUE_STAINED_GLASS_PANE = builder("blue_stained_glass_pane").setMaxAmount(64).setPlacedType(StateTypes.BLUE_STAINED_GLASS_PANE).build();
    public static final ItemType MOSSY_STONE_BRICK_WALL = builder("mossy_stone_brick_wall").setMaxAmount(64).setPlacedType(StateTypes.MOSSY_STONE_BRICK_WALL).build();
    public static final ItemType GRANITE = builder("granite").setMaxAmount(64).setPlacedType(StateTypes.GRANITE).build();
    public static final ItemType RED_MUSHROOM = builder("red_mushroom").setMaxAmount(64).setPlacedType(StateTypes.RED_MUSHROOM).build();
    public static final ItemType INFESTED_COBBLESTONE = builder("infested_cobblestone").setMaxAmount(64).setPlacedType(StateTypes.INFESTED_COBBLESTONE).build();
    public static final ItemType PINK_CONCRETE = builder("pink_concrete").setMaxAmount(64).setPlacedType(StateTypes.PINK_CONCRETE).build();
    public static final ItemType CARROT = builder("carrot").setMaxAmount(64).setAttributes(ItemAttribute.EDIBLE).setPlacedType(StateTypes.CARROTS).build();
    public static final ItemType MUSIC_DISC_OTHERSIDE = builder("music_disc_otherside").setMaxAmount(1).setAttributes(ItemAttribute.MUSIC_DISC).build();
    // 1.19 items
    public static final ItemType MUD = builder("MUD").setMaxAmount(64).setPlacedType(StateTypes.MUD).build();
    public static final ItemType MANGROVE_PLANKS = builder("MANGROVE_PLANKS").setMaxAmount(64).setPlacedType(StateTypes.MANGROVE_PLANKS).build();
    public static final ItemType MANGROVE_PROPAGULE = builder("MANGROVE_PROPAGULE").setMaxAmount(64).setPlacedType(StateTypes.MANGROVE_PROPAGULE).build();
    public static final ItemType MANGROVE_LOG = builder("MANGROVE_LOG").setMaxAmount(64).setPlacedType(StateTypes.MANGROVE_LOG).build();
    public static final ItemType MANGROVE_ROOTS = builder("MANGROVE_ROOTS").setMaxAmount(64).setPlacedType(StateTypes.MANGROVE_ROOTS).build();
    public static final ItemType MUDDY_MANGROVE_ROOTS = builder("MUDDY_MANGROVE_ROOTS").setMaxAmount(64).setPlacedType(StateTypes.MUDDY_MANGROVE_ROOTS).build();
    public static final ItemType STRIPPED_MANGROVE_LOG = builder("STRIPPED_MANGROVE_LOG").setMaxAmount(64).setPlacedType(StateTypes.STRIPPED_MANGROVE_LOG).build();
    public static final ItemType STRIPPED_MANGROVE_WOOD = builder("STRIPPED_MANGROVE_WOOD").setMaxAmount(64).setPlacedType(StateTypes.STRIPPED_MANGROVE_WOOD).build();
    public static final ItemType MANGROVE_WOOD = builder("MANGROVE_WOOD").setMaxAmount(64).setPlacedType(StateTypes.MANGROVE_WOOD).build();
    public static final ItemType MANGROVE_LEAVES = builder("MANGROVE_LEAVES").setMaxAmount(64).setPlacedType(StateTypes.MANGROVE_LEAVES).build();
    public static final ItemType MANGROVE_SLAB = builder("MANGROVE_SLAB").setMaxAmount(64).setPlacedType(StateTypes.MANGROVE_SLAB).build();
    public static final ItemType MUD_BRICK_SLAB = builder("MUD_BRICK_SLAB").setMaxAmount(64).setPlacedType(StateTypes.MUD_BRICK_SLAB).build();
    public static final ItemType MANGROVE_FENCE = builder("MANGROVE_FENCE").setMaxAmount(64).setPlacedType(StateTypes.MANGROVE_FENCE).build();
    public static final ItemType PACKED_MUD = builder("PACKED_MUD").setMaxAmount(64).setPlacedType(StateTypes.PACKED_MUD).build();
    public static final ItemType MUD_BRICKS = builder("MUD_BRICKS").setMaxAmount(64).setPlacedType(StateTypes.MUD_BRICKS).build();
    public static final ItemType REINFORCED_DEEPSLATE = builder("REINFORCED_DEEPSLATE").setMaxAmount(64).setPlacedType(StateTypes.REINFORCED_DEEPSLATE).build();
    public static final ItemType MUD_BRICK_STAIRS = builder("MUD_BRICK_STAIRS").setMaxAmount(64).setPlacedType(StateTypes.MUD_BRICK_STAIRS).build();
    public static final ItemType SCULK = builder("SCULK").setMaxAmount(64).setPlacedType(StateTypes.SCULK).build();
    public static final ItemType SCULK_VEIN = builder("SCULK_VEIN").setMaxAmount(64).setPlacedType(StateTypes.SCULK_VEIN).build();
    public static final ItemType SCULK_CATALYST = builder("SCULK_CATALYST").setMaxAmount(64).setPlacedType(StateTypes.SCULK_CATALYST).build();
    public static final ItemType SCULK_SHRIEKER = builder("SCULK_SHRIEKER").setMaxAmount(64).setPlacedType(StateTypes.SCULK_SHRIEKER).build();
    public static final ItemType MUD_BRICK_WALL = builder("MUD_BRICK_WALL").setMaxAmount(64).setPlacedType(StateTypes.MUD_BRICK_WALL).build();
    public static final ItemType MANGROVE_BUTTON = builder("MANGROVE_BUTTON").setMaxAmount(64).setPlacedType(StateTypes.MANGROVE_BUTTON).build();
    public static final ItemType MANGROVE_PRESSURE_PLATE = builder("MANGROVE_PRESSURE_PLATE").setMaxAmount(64).setPlacedType(StateTypes.MANGROVE_PRESSURE_PLATE).build();
    public static final ItemType MANGROVE_DOOR = builder("MANGROVE_DOOR").setMaxAmount(64).setPlacedType(StateTypes.MANGROVE_DOOR).build();
    public static final ItemType MANGROVE_TRAPDOOR = builder("MANGROVE_TRAPDOOR").setMaxAmount(64).setPlacedType(StateTypes.MANGROVE_TRAPDOOR).build();
    public static final ItemType MANGROVE_FENCE_GATE = builder("MANGROVE_FENCE_GATE").setMaxAmount(64).setPlacedType(StateTypes.MANGROVE_FENCE_GATE).build();
    public static final ItemType MANGROVE_SIGN = builder("MANGROVE_SIGN").setMaxAmount(16).setPlacedType(StateTypes.MANGROVE_SIGN).build();
    public static final ItemType TADPOLE_BUCKET = builder("TADPOLE_BUCKET").setMaxAmount(1).build();
    public static final ItemType RECOVERY_COMPASS = builder("RECOVERY_COMPASS").setMaxAmount(64).build();
    public static final ItemType ALLAY_SPAWN_EGG = builder("ALLAY_SPAWN_EGG").setMaxAmount(64).build();
    public static final ItemType FROG_SPAWN_EGG = builder("FROG_SPAWN_EGG").setMaxAmount(64).build();
    public static final ItemType TADPOLE_SPAWN_EGG = builder("TADPOLE_SPAWN_EGG").setMaxAmount(64).build();
    public static final ItemType WARDEN_SPAWN_EGG = builder("WARDEN_SPAWN_EGG").setMaxAmount(64).build();
    public static final ItemType MUSIC_DISC_5 = builder("MUSIC_DISC_5").setMaxAmount(1).build();
    public static final ItemType DISC_FRAGMENT_5 = builder("DISC_FRAGMENT_5").setMaxAmount(64).build();
    public static final ItemType OCHRE_FROGLIGHT = builder("OCHRE_FROGLIGHT").setMaxAmount(64).setPlacedType(StateTypes.OCHRE_FROGLIGHT).build();
    public static final ItemType VERDANT_FROGLIGHT = builder("VERDANT_FROGLIGHT").setMaxAmount(64).setPlacedType(StateTypes.VERDANT_FROGLIGHT).build();
    public static final ItemType PEARLESCENT_FROGLIGHT = builder("PEARLESCENT_FROGLIGHT").setMaxAmount(64).setPlacedType(StateTypes.PEARLESCENT_FROGLIGHT).build();
    public static final ItemType FROGSPAWN = builder("FROGSPAWN").setMaxAmount(64).setPlacedType(StateTypes.FROGSPAWN).build();
    public static final ItemType ECHO_SHARD = builder("ECHO_SHARD").setMaxAmount(64).build();
    public static final ItemType GOAT_HORN = builder("GOAT_HORN").setMaxAmount(1).build();
    public static final ItemType OAK_CHEST_BOAT = builder("OAK_CHEST_BOAT").setMaxAmount(1).build();
    public static final ItemType SPRUCE_CHEST_BOAT = builder("SPRUCE_CHEST_BOAT").setMaxAmount(1).build();
    public static final ItemType BIRCH_CHEST_BOAT = builder("BIRCH_CHEST_BOAT").setMaxAmount(1).build();
    public static final ItemType JUNGLE_CHEST_BOAT = builder("JUNGLE_CHEST_BOAT").setMaxAmount(1).build();
    public static final ItemType ACACIA_CHEST_BOAT = builder("ACACIA_CHEST_BOAT").setMaxAmount(1).build();
    public static final ItemType DARK_OAK_CHEST_BOAT = builder("DARK_OAK_CHEST_BOAT").setMaxAmount(1).build();
    public static final ItemType MANGROVE_BOAT = builder("MANGROVE_BOAT").setMaxAmount(1).build();
    public static final ItemType MANGROVE_CHEST_BOAT = builder("MANGROVE_CHEST_BOAT").setMaxAmount(1).build();
    /**
     * @deprecated Burning furnace shows up as a missing texture, removed in 1.9
     */
    @Deprecated
    public static final ItemType BURNING_FURNACE = builder("burning_furnace").setMaxAmount(64).setPlacedType(StateTypes.FURNACE).build();
    /**
     * @deprecated Fire was removed in 1.8 as an item
     */
    public static final ItemType FIRE = builder("fire").setMaxAmount(64).build();
    /**
     * @deprecated Nether portals were removed in 1.8 as an item
     */
    public static final ItemType NETHER_PORTAL = builder("nether_portal").setMaxAmount(1).build();
    /**
     * @deprecated End portals were removed in 1.8 as an item
     */
    public static final ItemType END_PORTAL = builder("end_portal").setMaxAmount(1).build();

    public static Collection<ItemType> values() {
        return ITEM_TYPE_MAP.values();
    }

    public static Builder builder(String key) {
        return new Builder(key);
    }

    public static ItemType define(int maxAmount, String key, ItemType craftRemainder, StateType placedType, int maxDurability, List<ItemAttribute> attributesArr) {
        // Creates an immutable set
        Set<ItemAttribute> attributes = attributesArr == null ? Collections.emptySet() :
                Collections.unmodifiableSet(new HashSet<>(attributesArr));

        TypesBuilderData data = TYPES_BUILDER.define(key);
        boolean musicDisc = attributes.contains(ItemAttribute.MUSIC_DISC);

        ItemType type = new ItemType() {
            private final int[] ids = data.getData();

            @Override
            public int getMaxAmount() {
                return maxAmount;
            }

            @Override
            public int getMaxDurability() {
                return maxDurability;
            }

            @Override
            public ResourceLocation getName() {
                return data.getName();
            }

            @Override
            public int getId(ClientVersion version) {
                int index = TYPES_BUILDER.getDataIndex(version);
                return ids[index];
            }

            @Override
            public boolean isMusicDisc() {
                return musicDisc;
            }

            @Override
            public Set<ItemAttribute> getAttributes() {
                return attributes;
            }

            @Override
            public boolean hasAttribute(ItemAttribute attribute) {
                return attributes.contains(attribute);
            }

            @Override
            public ItemType getCraftRemainder() {
                return craftRemainder;
            }

            @Override
            @Nullable
            public StateType getPlacedType() {
                return placedType;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof ItemType) {
                    return getName().equals(((ItemType) obj).getName());
                }
                return false;
            }
        };
        ITEM_TYPE_MAP.put(type.getName().toString(), type);
        for (ClientVersion version : TYPES_BUILDER.getVersions()) {
            int index = TYPES_BUILDER.getDataIndex(version);
            Map<Integer, ItemType> typeIdMap = ITEM_TYPE_ID_MAP.computeIfAbsent((byte) index, k -> new HashMap<>());
            typeIdMap.put(type.getId(version), type);
        }
        return type;
    }

    @Nullable
    public static ItemType getByName(String name) {
        return ITEM_TYPE_MAP.get(name);
    }

    @NotNull
    public static ItemType getById(ClientVersion version, int id) {
        int index = TYPES_BUILDER.getDataIndex(version);
        Map<Integer, ItemType> typeIdMap = ITEM_TYPE_ID_MAP.get((byte) index);
        return typeIdMap.getOrDefault(id, ItemTypes.AIR);
    }

    public static ItemType getTypePlacingState(StateType type) {
        return HELD_TO_PLACED_MAP.get(type);
    }

    public enum ItemAttribute {
        MUSIC_DISC, EDIBLE, FIRE_RESISTANT, WOOD_TIER, STONE_TIER, IRON_TIER, DIAMOND_TIER, GOLD_TIER, NETHERITE_TIER, FUEL,
        SWORD, SHOVEL, AXE, PICKAXE, HOE;
    }

    public static class Builder {
        int maxAmount;
        String key;
        StateType placedType;
        ItemType craftRemainder;
        int maxDurability;
        List<ItemAttribute> attributes;

        public Builder(String key) {
            this.key = key;
        }

        public ItemType build() {
            ItemType define = ItemTypes.define(maxAmount, key, craftRemainder, placedType, maxDurability, attributes);
            if (placedType != null) {
                HELD_TO_PLACED_MAP.put(placedType, define);
            }
            return define;
        }

        public Builder setMaxAmount(int maxAmount) {
            this.maxAmount = maxAmount;
            return this;
        }

        public Builder setCraftRemainder(ItemType craftRemainder) {
            this.craftRemainder = craftRemainder;
            return this;
        }

        public Builder setMaxDurability(int maxDurability) {
            this.maxDurability = maxDurability;
            return this;
        }

        public Builder setAttributes(ItemAttribute... attributes) {
            this.attributes = Arrays.stream(attributes).collect(Collectors.toList());
            return this;
        }

        public Builder setPlacedType(StateType type) {
            placedType = type;
            return this;
        }
    }

    static {
        TYPES_BUILDER.unloadFileMappings();
    }
}
