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

package com.github.retrooper.packetevents.protocol.world.states.defaulttags;

import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class allows you to use block tags on outdated versions.
 * If you are on a version that sends tags to the player, you are suggested to listen to {@link com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTags}
 */
public class BlockTags {
    private static final HashMap<String, BlockTags> byName = new HashMap<>();

    public static final BlockTags WOOL = bind("wool");
    public static final BlockTags PLANKS = bind("planks");
    public static final BlockTags STONE_BRICKS = bind("stone_bricks");
    public static final BlockTags WOODEN_BUTTONS = bind("wooden_buttons");
    public static final BlockTags BUTTONS = bind("buttons");
    public static final BlockTags CARPETS = bind("carpets");
    public static final BlockTags WOODEN_DOORS = bind("wooden_doors");
    public static final BlockTags WOODEN_STAIRS = bind("wooden_stairs");
    public static final BlockTags WOODEN_SLABS = bind("wooden_slabs");
    public static final BlockTags WOODEN_FENCES = bind("wooden_fences");
    public static final BlockTags PRESSURE_PLATES = bind("pressure_plates");
    public static final BlockTags WOODEN_PRESSURE_PLATES = bind("wooden_pressure_plates");
    public static final BlockTags STONE_PRESSURE_PLATES = bind("stone_pressure_plates");
    public static final BlockTags WOODEN_TRAPDOORS = bind("wooden_trapdoors");
    public static final BlockTags DOORS = bind("doors");
    public static final BlockTags SAPLINGS = bind("saplings");
    public static final BlockTags LOGS_THAT_BURN = bind("logs_that_burn");
    public static final BlockTags LOGS = bind("logs");
    public static final BlockTags DARK_OAK_LOGS = bind("dark_oak_logs");
    public static final BlockTags OAK_LOGS = bind("oak_logs");
    public static final BlockTags BIRCH_LOGS = bind("birch_logs");
    public static final BlockTags ACACIA_LOGS = bind("acacia_logs");
    public static final BlockTags JUNGLE_LOGS = bind("jungle_logs");
    public static final BlockTags SPRUCE_LOGS = bind("spruce_logs");
    public static final BlockTags CRIMSON_STEMS = bind("crimson_stems");
    public static final BlockTags WARPED_STEMS = bind("warped_stems");
    public static final BlockTags BANNERS = bind("banners");
    public static final BlockTags SAND = bind("sand");
    public static final BlockTags STAIRS = bind("stairs");
    public static final BlockTags SLABS = bind("slabs");
    public static final BlockTags WALLS = bind("walls");
    public static final BlockTags ANVIL = bind("anvil");
    public static final BlockTags RAILS = bind("rails");
    public static final BlockTags LEAVES = bind("leaves");
    public static final BlockTags TRAPDOORS = bind("trapdoors");
    public static final BlockTags SMALL_FLOWERS = bind("small_flowers");
    public static final BlockTags BEDS = bind("beds");
    public static final BlockTags FENCES = bind("fences");
    public static final BlockTags TALL_FLOWERS = bind("tall_flowers");
    public static final BlockTags FLOWERS = bind("flowers");
    public static final BlockTags PIGLIN_REPELLENTS = bind("piglin_repellents");
    public static final BlockTags GOLD_ORES = bind("gold_ores");
    public static final BlockTags IRON_ORES = bind("iron_ores");
    public static final BlockTags DIAMOND_ORES = bind("diamond_ores");
    public static final BlockTags REDSTONE_ORES = bind("redstone_ores");
    public static final BlockTags LAPIS_ORES = bind("lapis_ores");
    public static final BlockTags COAL_ORES = bind("coal_ores");
    public static final BlockTags EMERALD_ORES = bind("emerald_ores");
    public static final BlockTags COPPER_ORES = bind("copper_ores");
    public static final BlockTags NON_FLAMMABLE_WOOD = bind("non_flammable_wood");
    public static final BlockTags CANDLES = bind("candles");
    public static final BlockTags DIRT = bind("dirt");
    public static final BlockTags TERRACOTTA = bind("terracotta");
    public static final BlockTags FLOWER_POTS = bind("flower_pots");
    public static final BlockTags ENDERMAN_HOLDABLE = bind("enderman_holdable");
    public static final BlockTags ICE = bind("ice");
    public static final BlockTags VALID_SPAWN = bind("valid_spawn");
    public static final BlockTags IMPERMEABLE = bind("impermeable");
    public static final BlockTags UNDERWATER_BONEMEALS = bind("underwater_bonemeals");
    public static final BlockTags CORAL_BLOCKS = bind("coral_blocks");
    public static final BlockTags WALL_CORALS = bind("wall_corals");
    public static final BlockTags CORAL_PLANTS = bind("coral_plants");
    public static final BlockTags CORALS = bind("corals");
    public static final BlockTags BAMBOO_PLANTABLE_ON = bind("bamboo_plantable_on");
    public static final BlockTags STANDING_SIGNS = bind("standing_signs");
    public static final BlockTags WALL_SIGNS = bind("wall_signs");
    public static final BlockTags SIGNS = bind("signs");
    public static final BlockTags DRAGON_IMMUNE = bind("dragon_immune");
    public static final BlockTags WITHER_IMMUNE = bind("wither_immune");
    public static final BlockTags WITHER_SUMMON_BASE_BLOCKS = bind("wither_summon_base_blocks");
    public static final BlockTags BEEHIVES = bind("beehives");
    public static final BlockTags CROPS = bind("crops");
    public static final BlockTags BEE_GROWABLES = bind("bee_growables");
    public static final BlockTags PORTALS = bind("portals");
    public static final BlockTags FIRE = bind("fire");
    public static final BlockTags NYLIUM = bind("nylium");
    public static final BlockTags WART_BLOCKS = bind("wart_blocks");
    public static final BlockTags BEACON_BASE_BLOCKS = bind("beacon_base_blocks");
    public static final BlockTags SOUL_SPEED_BLOCKS = bind("soul_speed_blocks");
    public static final BlockTags WALL_POST_OVERRIDE = bind("wall_post_override");
    public static final BlockTags CLIMBABLE = bind("climbable");
    public static final BlockTags SHULKER_BOXES = bind("shulker_boxes");
    public static final BlockTags HOGLIN_REPELLENTS = bind("hoglin_repellents");
    public static final BlockTags SOUL_FIRE_BASE_BLOCKS = bind("soul_fire_base_blocks");
    public static final BlockTags STRIDER_WARM_BLOCKS = bind("strider_warm_blocks");
    public static final BlockTags CAMPFIRES = bind("campfires");
    public static final BlockTags GUARDED_BY_PIGLINS = bind("guarded_by_piglins");
    public static final BlockTags PREVENT_MOB_SPAWNING_INSIDE = bind("prevent_mob_spawning_inside");
    public static final BlockTags FENCE_GATES = bind("fence_gates");
    public static final BlockTags UNSTABLE_BOTTOM_CENTER = bind("unstable_bottom_center");
    public static final BlockTags MUSHROOM_GROW_BLOCK = bind("mushroom_grow_block");
    public static final BlockTags INFINIBURN_OVERWORLD = bind("infiniburn_overworld");
    public static final BlockTags INFINIBURN_NETHER = bind("infiniburn_nether");
    public static final BlockTags INFINIBURN_END = bind("infiniburn_end");
    public static final BlockTags BASE_STONE_OVERWORLD = bind("base_stone_overworld");
    public static final BlockTags STONE_ORE_REPLACEABLES = bind("stone_ore_replaceables");
    public static final BlockTags DEEPSLATE_ORE_REPLACEABLES = bind("deepslate_ore_replaceables");
    public static final BlockTags BASE_STONE_NETHER = bind("base_stone_nether");
    public static final BlockTags CANDLE_CAKES = bind("candle_cakes");
    public static final BlockTags CAULDRONS = bind("cauldrons");
    public static final BlockTags CRYSTAL_SOUND_BLOCKS = bind("crystal_sound_blocks");
    public static final BlockTags INSIDE_STEP_SOUND_BLOCKS = bind("inside_step_sound_blocks");
    public static final BlockTags OCCLUDES_VIBRATION_SIGNALS = bind("occludes_vibration_signals");
    public static final BlockTags DRIPSTONE_REPLACEABLE = bind("dripstone_replaceable_blocks");
    public static final BlockTags CAVE_VINES = bind("cave_vines");
    public static final BlockTags MOSS_REPLACEABLE = bind("moss_replaceable");
    public static final BlockTags LUSH_GROUND_REPLACEABLE = bind("lush_ground_replaceable");
    public static final BlockTags AZALEA_ROOT_REPLACEABLE = bind("azalea_root_replaceable");
    public static final BlockTags SMALL_DRIPLEAF_PLACEABLE = bind("small_dripleaf_placeable");
    public static final BlockTags BIG_DRIPLEAF_PLACEABLE = bind("big_dripleaf_placeable");
    public static final BlockTags SNOW = bind("snow");
    public static final BlockTags MINEABLE_WITH_AXE = bind("mineable/axe");
    public static final BlockTags MINEABLE_WITH_HOE = bind("mineable/hoe");
    public static final BlockTags MINEABLE_WITH_PICKAXE = bind("mineable/pickaxe");
    public static final BlockTags MINEABLE_WITH_SHOVEL = bind("mineable/shovel");
    public static final BlockTags NEEDS_DIAMOND_TOOL = bind("needs_diamond_tool");
    public static final BlockTags NEEDS_IRON_TOOL = bind("needs_iron_tool");
    public static final BlockTags NEEDS_STONE_TOOL = bind("needs_stone_tool");
    public static final BlockTags FEATURES_CANNOT_REPLACE = bind("features_cannot_replace");
    public static final BlockTags LAVA_POOL_STONE_CANNOT_REPLACE = bind("lava_pool_stone_cannot_replace");
    public static final BlockTags GEODE_INVALID_BLOCKS = bind("geode_invalid_blocks");
    public static final BlockTags ANIMALS_SPAWNABLE_ON = bind("animals_spawnable_on");
    public static final BlockTags AXOLOTLS_SPAWNABLE_ON = bind("axolotls_spawnable_on");
    public static final BlockTags GOATS_SPAWNABLE_ON = bind("goats_spawnable_on");
    public static final BlockTags MOOSHROOMS_SPAWNABLE_ON = bind("mooshrooms_spawnable_on");
    public static final BlockTags PARROTS_SPAWNABLE_ON = bind("parrots_spawnable_on");
    public static final BlockTags POLAR_BEARS_SPAWNABLE_ON_IN_FROZEN_OCEAN = bind("polar_bears_spawnable_on_in_frozen_ocean");
    public static final BlockTags RABBITS_SPAWNABLE_ON = bind("rabbits_spawnable_on");
    public static final BlockTags FOXES_SPAWNABLE_ON = bind("foxes_spawnable_on");
    public static final BlockTags WOLVES_SPAWNABLE_ON = bind("wolves_spawnable_on");
    public static final BlockTags AZALEA_GROWS_ON = bind("azalea_grows_on");
    public static final BlockTags REPLACEABLE_PLANTS = bind("replaceable_plants");
    /**
     * Unofficial tag for all glass blocks
     */
    public static final BlockTags GLASS_BLOCKS = bind("glass_blocks");
    /**
     * Unofficial tag for all glass panes
     */
    public static final BlockTags GLASS_PANES = bind("glass_panes");

    static {
        BlockTags.WOOL.add(StateTypes.WHITE_WOOL, StateTypes.ORANGE_WOOL, StateTypes.MAGENTA_WOOL, StateTypes.LIGHT_BLUE_WOOL, StateTypes.YELLOW_WOOL, StateTypes.LIME_WOOL, StateTypes.PINK_WOOL, StateTypes.GRAY_WOOL, StateTypes.LIGHT_GRAY_WOOL, StateTypes.CYAN_WOOL, StateTypes.PURPLE_WOOL, StateTypes.BLUE_WOOL, StateTypes.BROWN_WOOL, StateTypes.GREEN_WOOL, StateTypes.RED_WOOL, StateTypes.BLACK_WOOL);
        BlockTags.PLANKS.add(StateTypes.OAK_PLANKS, StateTypes.SPRUCE_PLANKS, StateTypes.BIRCH_PLANKS, StateTypes.JUNGLE_PLANKS, StateTypes.ACACIA_PLANKS, StateTypes.DARK_OAK_PLANKS, StateTypes.CRIMSON_PLANKS, StateTypes.WARPED_PLANKS, StateTypes.MANGROVE_PLANKS);
        BlockTags.STONE_BRICKS.add(StateTypes.STONE_BRICKS, StateTypes.MOSSY_STONE_BRICKS, StateTypes.CRACKED_STONE_BRICKS, StateTypes.CHISELED_STONE_BRICKS);
        BlockTags.WOODEN_BUTTONS.add(StateTypes.OAK_BUTTON, StateTypes.SPRUCE_BUTTON, StateTypes.BIRCH_BUTTON, StateTypes.JUNGLE_BUTTON, StateTypes.ACACIA_BUTTON, StateTypes.DARK_OAK_BUTTON, StateTypes.CRIMSON_BUTTON, StateTypes.WARPED_BUTTON, StateTypes.MANGROVE_BUTTON);
        BlockTags.BUTTONS.addTag(BlockTags.WOODEN_BUTTONS).add(StateTypes.STONE_BUTTON).add(StateTypes.POLISHED_BLACKSTONE_BUTTON);
        // TODO: This was renamed to WOOL_CARPETS in 1.19
        BlockTags.CARPETS.add(StateTypes.WHITE_CARPET, StateTypes.ORANGE_CARPET, StateTypes.MAGENTA_CARPET, StateTypes.LIGHT_BLUE_CARPET, StateTypes.YELLOW_CARPET, StateTypes.LIME_CARPET, StateTypes.PINK_CARPET, StateTypes.GRAY_CARPET, StateTypes.LIGHT_GRAY_CARPET, StateTypes.CYAN_CARPET, StateTypes.PURPLE_CARPET, StateTypes.BLUE_CARPET, StateTypes.BROWN_CARPET, StateTypes.GREEN_CARPET, StateTypes.RED_CARPET, StateTypes.BLACK_CARPET);
        BlockTags.WOODEN_DOORS.add(StateTypes.OAK_DOOR, StateTypes.SPRUCE_DOOR, StateTypes.BIRCH_DOOR, StateTypes.JUNGLE_DOOR, StateTypes.ACACIA_DOOR, StateTypes.DARK_OAK_DOOR, StateTypes.CRIMSON_DOOR, StateTypes.WARPED_DOOR, StateTypes.MANGROVE_DOOR);
        BlockTags.WOODEN_STAIRS.add(StateTypes.OAK_STAIRS, StateTypes.SPRUCE_STAIRS, StateTypes.BIRCH_STAIRS, StateTypes.JUNGLE_STAIRS, StateTypes.ACACIA_STAIRS, StateTypes.DARK_OAK_STAIRS, StateTypes.CRIMSON_STAIRS, StateTypes.WARPED_STAIRS, StateTypes.MANGROVE_DOOR);
        BlockTags.WOODEN_SLABS.add(StateTypes.OAK_SLAB, StateTypes.SPRUCE_SLAB, StateTypes.BIRCH_SLAB, StateTypes.JUNGLE_SLAB, StateTypes.ACACIA_SLAB, StateTypes.DARK_OAK_SLAB, StateTypes.CRIMSON_SLAB, StateTypes.WARPED_SLAB, StateTypes.MANGROVE_SLAB);
        BlockTags.WOODEN_FENCES.add(StateTypes.OAK_FENCE, StateTypes.ACACIA_FENCE, StateTypes.DARK_OAK_FENCE, StateTypes.SPRUCE_FENCE, StateTypes.BIRCH_FENCE, StateTypes.JUNGLE_FENCE, StateTypes.CRIMSON_FENCE, StateTypes.WARPED_FENCE);
        BlockTags.DOORS.addTag(BlockTags.WOODEN_DOORS).add(StateTypes.IRON_DOOR);
        BlockTags.SAPLINGS.add(StateTypes.OAK_SAPLING, StateTypes.SPRUCE_SAPLING, StateTypes.BIRCH_SAPLING, StateTypes.JUNGLE_SAPLING, StateTypes.ACACIA_SAPLING, StateTypes.DARK_OAK_SAPLING, StateTypes.AZALEA, StateTypes.FLOWERING_AZALEA, StateTypes.MANGROVE_PROPAGULE);
        BlockTags.DARK_OAK_LOGS.add(StateTypes.DARK_OAK_LOG, StateTypes.DARK_OAK_WOOD, StateTypes.STRIPPED_DARK_OAK_LOG, StateTypes.STRIPPED_DARK_OAK_WOOD);
        BlockTags.OAK_LOGS.add(StateTypes.OAK_LOG, StateTypes.OAK_WOOD, StateTypes.STRIPPED_OAK_LOG, StateTypes.STRIPPED_OAK_WOOD);
        BlockTags.ACACIA_LOGS.add(StateTypes.ACACIA_LOG, StateTypes.ACACIA_WOOD, StateTypes.STRIPPED_ACACIA_LOG, StateTypes.STRIPPED_ACACIA_WOOD);
        BlockTags.BIRCH_LOGS.add(StateTypes.BIRCH_LOG, StateTypes.BIRCH_WOOD, StateTypes.STRIPPED_BIRCH_LOG, StateTypes.STRIPPED_BIRCH_WOOD);
        BlockTags.JUNGLE_LOGS.add(StateTypes.JUNGLE_LOG, StateTypes.JUNGLE_WOOD, StateTypes.STRIPPED_JUNGLE_LOG, StateTypes.STRIPPED_JUNGLE_WOOD);
        BlockTags.SPRUCE_LOGS.add(StateTypes.SPRUCE_LOG, StateTypes.SPRUCE_WOOD, StateTypes.STRIPPED_SPRUCE_LOG, StateTypes.STRIPPED_SPRUCE_WOOD);
        BlockTags.CRIMSON_STEMS.add(StateTypes.CRIMSON_STEM, StateTypes.STRIPPED_CRIMSON_STEM, StateTypes.CRIMSON_HYPHAE, StateTypes.STRIPPED_CRIMSON_HYPHAE);
        BlockTags.WARPED_STEMS.add(StateTypes.WARPED_STEM, StateTypes.STRIPPED_WARPED_STEM, StateTypes.WARPED_HYPHAE, StateTypes.STRIPPED_WARPED_HYPHAE);
        // TODO: this.tag(BlockTags.MANGROVE_LOGS).add(Blocks.MANGROVE_LOG, Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_WOOD);
        BlockTags.LOGS_THAT_BURN.addTag(BlockTags.DARK_OAK_LOGS).addTag(BlockTags.OAK_LOGS).addTag(BlockTags.ACACIA_LOGS).addTag(BlockTags.BIRCH_LOGS).addTag(BlockTags.JUNGLE_LOGS).addTag(BlockTags.SPRUCE_LOGS); // TODO: Add MANGROVE_LOGS tag to this
        // TODO:  this.tag(BlockTags.OVERWORLD_NATURAL_LOGS).add(Blocks.ACACIA_LOG).add(Blocks.BIRCH_LOG).add(Blocks.OAK_LOG).add(Blocks.JUNGLE_LOG).add(Blocks.SPRUCE_LOG).add(Blocks.DARK_OAK_LOG).add(Blocks.MANGROVE_LOG);
        BlockTags.LOGS.addTag(BlockTags.LOGS_THAT_BURN).addTag(BlockTags.CRIMSON_STEMS).addTag(BlockTags.WARPED_STEMS);
        BlockTags.ANVIL.add(StateTypes.ANVIL, StateTypes.CHIPPED_ANVIL, StateTypes.DAMAGED_ANVIL);
        BlockTags.SMALL_FLOWERS.add(StateTypes.DANDELION, StateTypes.POPPY, StateTypes.BLUE_ORCHID, StateTypes.ALLIUM, StateTypes.AZURE_BLUET, StateTypes.RED_TULIP, StateTypes.ORANGE_TULIP, StateTypes.WHITE_TULIP, StateTypes.PINK_TULIP, StateTypes.OXEYE_DAISY, StateTypes.CORNFLOWER, StateTypes.LILY_OF_THE_VALLEY, StateTypes.WITHER_ROSE);
        BlockTags.DIRT.add(StateTypes.DIRT, StateTypes.GRASS_BLOCK, StateTypes.PODZOL, StateTypes.COARSE_DIRT, StateTypes.MYCELIUM, StateTypes.ROOTED_DIRT, StateTypes.MOSS_BLOCK, StateTypes.MUD, StateTypes.MUDDY_MANGROVE_ROOTS);
        BlockTags.ENDERMAN_HOLDABLE.addTag(BlockTags.SMALL_FLOWERS).addTag(BlockTags.DIRT).add(StateTypes.SAND, StateTypes.RED_SAND, StateTypes.GRAVEL, StateTypes.BROWN_MUSHROOM, StateTypes.RED_MUSHROOM, StateTypes.TNT, StateTypes.CACTUS, StateTypes.CLAY, StateTypes.PUMPKIN, StateTypes.CARVED_PUMPKIN, StateTypes.MELON, StateTypes.CRIMSON_FUNGUS, StateTypes.CRIMSON_NYLIUM, StateTypes.CRIMSON_ROOTS, StateTypes.WARPED_FUNGUS, StateTypes.WARPED_NYLIUM, StateTypes.WARPED_ROOTS);
        BlockTags.FLOWER_POTS.add(StateTypes.FLOWER_POT, StateTypes.POTTED_POPPY, StateTypes.POTTED_BLUE_ORCHID, StateTypes.POTTED_ALLIUM, StateTypes.POTTED_AZURE_BLUET, StateTypes.POTTED_RED_TULIP, StateTypes.POTTED_ORANGE_TULIP, StateTypes.POTTED_WHITE_TULIP, StateTypes.POTTED_PINK_TULIP, StateTypes.POTTED_OXEYE_DAISY, StateTypes.POTTED_DANDELION, StateTypes.POTTED_OAK_SAPLING, StateTypes.POTTED_SPRUCE_SAPLING, StateTypes.POTTED_BIRCH_SAPLING, StateTypes.POTTED_JUNGLE_SAPLING, StateTypes.POTTED_ACACIA_SAPLING, StateTypes.POTTED_DARK_OAK_SAPLING, StateTypes.POTTED_RED_MUSHROOM, StateTypes.POTTED_BROWN_MUSHROOM, StateTypes.POTTED_DEAD_BUSH, StateTypes.POTTED_FERN, StateTypes.POTTED_CACTUS, StateTypes.POTTED_CORNFLOWER, StateTypes.POTTED_LILY_OF_THE_VALLEY, StateTypes.POTTED_WITHER_ROSE, StateTypes.POTTED_BAMBOO, StateTypes.POTTED_CRIMSON_FUNGUS, StateTypes.POTTED_WARPED_FUNGUS, StateTypes.POTTED_CRIMSON_ROOTS, StateTypes.POTTED_WARPED_ROOTS, StateTypes.POTTED_AZALEA_BUSH, StateTypes.POTTED_FLOWERING_AZALEA_BUSH, StateTypes.POTTED_MANGROVE_PROPAGULE);
        BlockTags.BANNERS.add(StateTypes.WHITE_BANNER, StateTypes.ORANGE_BANNER, StateTypes.MAGENTA_BANNER, StateTypes.LIGHT_BLUE_BANNER, StateTypes.YELLOW_BANNER, StateTypes.LIME_BANNER, StateTypes.PINK_BANNER, StateTypes.GRAY_BANNER, StateTypes.LIGHT_GRAY_BANNER, StateTypes.CYAN_BANNER, StateTypes.PURPLE_BANNER, StateTypes.BLUE_BANNER, StateTypes.BROWN_BANNER, StateTypes.GREEN_BANNER, StateTypes.RED_BANNER, StateTypes.BLACK_BANNER, StateTypes.WHITE_WALL_BANNER, StateTypes.ORANGE_WALL_BANNER, StateTypes.MAGENTA_WALL_BANNER, StateTypes.LIGHT_BLUE_WALL_BANNER, StateTypes.YELLOW_WALL_BANNER, StateTypes.LIME_WALL_BANNER, StateTypes.PINK_WALL_BANNER, StateTypes.GRAY_WALL_BANNER, StateTypes.LIGHT_GRAY_WALL_BANNER, StateTypes.CYAN_WALL_BANNER, StateTypes.PURPLE_WALL_BANNER, StateTypes.BLUE_WALL_BANNER, StateTypes.BROWN_WALL_BANNER, StateTypes.GREEN_WALL_BANNER, StateTypes.RED_WALL_BANNER, StateTypes.BLACK_WALL_BANNER);
        BlockTags.WOODEN_PRESSURE_PLATES.add(StateTypes.OAK_PRESSURE_PLATE, StateTypes.SPRUCE_PRESSURE_PLATE, StateTypes.BIRCH_PRESSURE_PLATE, StateTypes.JUNGLE_PRESSURE_PLATE, StateTypes.ACACIA_PRESSURE_PLATE, StateTypes.DARK_OAK_PRESSURE_PLATE, StateTypes.CRIMSON_PRESSURE_PLATE, StateTypes.WARPED_PRESSURE_PLATE, StateTypes.MANGROVE_PRESSURE_PLATE);
        BlockTags.STONE_PRESSURE_PLATES.add(StateTypes.STONE_PRESSURE_PLATE, StateTypes.POLISHED_BLACKSTONE_PRESSURE_PLATE);
        BlockTags.PRESSURE_PLATES.add(StateTypes.LIGHT_WEIGHTED_PRESSURE_PLATE, StateTypes.HEAVY_WEIGHTED_PRESSURE_PLATE).addTag(BlockTags.WOODEN_PRESSURE_PLATES).addTag(BlockTags.STONE_PRESSURE_PLATES);
        BlockTags.STAIRS.addTag(BlockTags.WOODEN_STAIRS).add(StateTypes.COBBLESTONE_STAIRS, StateTypes.SANDSTONE_STAIRS, StateTypes.NETHER_BRICK_STAIRS, StateTypes.STONE_BRICK_STAIRS, StateTypes.BRICK_STAIRS, StateTypes.PURPUR_STAIRS, StateTypes.QUARTZ_STAIRS, StateTypes.RED_SANDSTONE_STAIRS, StateTypes.PRISMARINE_BRICK_STAIRS, StateTypes.PRISMARINE_STAIRS, StateTypes.DARK_PRISMARINE_STAIRS, StateTypes.POLISHED_GRANITE_STAIRS, StateTypes.SMOOTH_RED_SANDSTONE_STAIRS, StateTypes.MOSSY_STONE_BRICK_STAIRS, StateTypes.POLISHED_DIORITE_STAIRS, StateTypes.MOSSY_COBBLESTONE_STAIRS, StateTypes.END_STONE_BRICK_STAIRS, StateTypes.STONE_STAIRS, StateTypes.SMOOTH_SANDSTONE_STAIRS, StateTypes.SMOOTH_QUARTZ_STAIRS, StateTypes.GRANITE_STAIRS, StateTypes.ANDESITE_STAIRS, StateTypes.RED_NETHER_BRICK_STAIRS, StateTypes.POLISHED_ANDESITE_STAIRS, StateTypes.DIORITE_STAIRS, StateTypes.BLACKSTONE_STAIRS, StateTypes.POLISHED_BLACKSTONE_BRICK_STAIRS, StateTypes.POLISHED_BLACKSTONE_STAIRS, StateTypes.COBBLED_DEEPSLATE_STAIRS, StateTypes.POLISHED_DEEPSLATE_STAIRS, StateTypes.DEEPSLATE_TILE_STAIRS, StateTypes.DEEPSLATE_BRICK_STAIRS, StateTypes.OXIDIZED_CUT_COPPER_STAIRS, StateTypes.WEATHERED_CUT_COPPER_STAIRS, StateTypes.EXPOSED_CUT_COPPER_STAIRS, StateTypes.CUT_COPPER_STAIRS, StateTypes.WAXED_WEATHERED_CUT_COPPER_STAIRS, StateTypes.WAXED_EXPOSED_CUT_COPPER_STAIRS, StateTypes.WAXED_CUT_COPPER_STAIRS, StateTypes.WAXED_OXIDIZED_CUT_COPPER_STAIRS, StateTypes.MUD_BRICK_STAIRS);
        BlockTags.SLABS.addTag(BlockTags.WOODEN_SLABS).add(StateTypes.STONE_SLAB, StateTypes.SMOOTH_STONE_SLAB, StateTypes.STONE_BRICK_SLAB, StateTypes.SANDSTONE_SLAB, StateTypes.PURPUR_SLAB, StateTypes.QUARTZ_SLAB, StateTypes.RED_SANDSTONE_SLAB, StateTypes.BRICK_SLAB, StateTypes.COBBLESTONE_SLAB, StateTypes.NETHER_BRICK_SLAB, StateTypes.PETRIFIED_OAK_SLAB, StateTypes.PRISMARINE_SLAB, StateTypes.PRISMARINE_BRICK_SLAB, StateTypes.DARK_PRISMARINE_SLAB, StateTypes.POLISHED_GRANITE_SLAB, StateTypes.SMOOTH_RED_SANDSTONE_SLAB, StateTypes.MOSSY_STONE_BRICK_SLAB, StateTypes.POLISHED_DIORITE_SLAB, StateTypes.MOSSY_COBBLESTONE_SLAB, StateTypes.END_STONE_BRICK_SLAB, StateTypes.SMOOTH_SANDSTONE_SLAB, StateTypes.SMOOTH_QUARTZ_SLAB, StateTypes.GRANITE_SLAB, StateTypes.ANDESITE_SLAB, StateTypes.RED_NETHER_BRICK_SLAB, StateTypes.POLISHED_ANDESITE_SLAB, StateTypes.DIORITE_SLAB, StateTypes.CUT_SANDSTONE_SLAB, StateTypes.CUT_RED_SANDSTONE_SLAB, StateTypes.BLACKSTONE_SLAB, StateTypes.POLISHED_BLACKSTONE_BRICK_SLAB, StateTypes.POLISHED_BLACKSTONE_SLAB, StateTypes.COBBLED_DEEPSLATE_SLAB, StateTypes.POLISHED_DEEPSLATE_SLAB, StateTypes.DEEPSLATE_TILE_SLAB, StateTypes.DEEPSLATE_BRICK_SLAB, StateTypes.WAXED_WEATHERED_CUT_COPPER_SLAB, StateTypes.WAXED_EXPOSED_CUT_COPPER_SLAB, StateTypes.WAXED_CUT_COPPER_SLAB, StateTypes.OXIDIZED_CUT_COPPER_SLAB, StateTypes.WEATHERED_CUT_COPPER_SLAB, StateTypes.EXPOSED_CUT_COPPER_SLAB, StateTypes.CUT_COPPER_SLAB, StateTypes.WAXED_OXIDIZED_CUT_COPPER_SLAB, StateTypes.MUD_BRICK_SLAB);
        BlockTags.WALLS.add(StateTypes.COBBLESTONE_WALL, StateTypes.MOSSY_COBBLESTONE_WALL, StateTypes.BRICK_WALL, StateTypes.PRISMARINE_WALL, StateTypes.RED_SANDSTONE_WALL, StateTypes.MOSSY_STONE_BRICK_WALL, StateTypes.GRANITE_WALL, StateTypes.STONE_BRICK_WALL, StateTypes.NETHER_BRICK_WALL, StateTypes.ANDESITE_WALL, StateTypes.RED_NETHER_BRICK_WALL, StateTypes.SANDSTONE_WALL, StateTypes.END_STONE_BRICK_WALL, StateTypes.DIORITE_WALL, StateTypes.BLACKSTONE_WALL, StateTypes.POLISHED_BLACKSTONE_BRICK_WALL, StateTypes.POLISHED_BLACKSTONE_WALL, StateTypes.COBBLED_DEEPSLATE_WALL, StateTypes.POLISHED_DEEPSLATE_WALL, StateTypes.DEEPSLATE_TILE_WALL, StateTypes.DEEPSLATE_BRICK_WALL, StateTypes.MUD_BRICK_WALL);
        BlockTags.CORAL_PLANTS.add(StateTypes.TUBE_CORAL, StateTypes.BRAIN_CORAL, StateTypes.BUBBLE_CORAL, StateTypes.FIRE_CORAL, StateTypes.HORN_CORAL);
        BlockTags.CORALS.addTag(BlockTags.CORAL_PLANTS).add(StateTypes.TUBE_CORAL_FAN, StateTypes.BRAIN_CORAL_FAN, StateTypes.BUBBLE_CORAL_FAN, StateTypes.FIRE_CORAL_FAN, StateTypes.HORN_CORAL_FAN);
        BlockTags.WALL_CORALS.add(StateTypes.TUBE_CORAL_WALL_FAN, StateTypes.BRAIN_CORAL_WALL_FAN, StateTypes.BUBBLE_CORAL_WALL_FAN, StateTypes.FIRE_CORAL_WALL_FAN, StateTypes.HORN_CORAL_WALL_FAN);
        BlockTags.SAND.add(StateTypes.SAND, StateTypes.RED_SAND);
        BlockTags.RAILS.add(StateTypes.RAIL, StateTypes.POWERED_RAIL, StateTypes.DETECTOR_RAIL, StateTypes.ACTIVATOR_RAIL);
        BlockTags.CORAL_BLOCKS.add(StateTypes.TUBE_CORAL_BLOCK, StateTypes.BRAIN_CORAL_BLOCK, StateTypes.BUBBLE_CORAL_BLOCK, StateTypes.FIRE_CORAL_BLOCK, StateTypes.HORN_CORAL_BLOCK);
        BlockTags.ICE.add(StateTypes.ICE, StateTypes.PACKED_ICE, StateTypes.BLUE_ICE, StateTypes.FROSTED_ICE);
        BlockTags.VALID_SPAWN.add(StateTypes.GRASS_BLOCK, StateTypes.PODZOL);
        BlockTags.LEAVES.add(StateTypes.JUNGLE_LEAVES, StateTypes.OAK_LEAVES, StateTypes.SPRUCE_LEAVES, StateTypes.DARK_OAK_LEAVES, StateTypes.ACACIA_LEAVES, StateTypes.BIRCH_LEAVES, StateTypes.AZALEA_LEAVES, StateTypes.FLOWERING_AZALEA_LEAVES, StateTypes.MANGROVE_LEAVES);
        BlockTags.IMPERMEABLE.add(StateTypes.GLASS, StateTypes.WHITE_STAINED_GLASS, StateTypes.ORANGE_STAINED_GLASS, StateTypes.MAGENTA_STAINED_GLASS, StateTypes.LIGHT_BLUE_STAINED_GLASS, StateTypes.YELLOW_STAINED_GLASS, StateTypes.LIME_STAINED_GLASS, StateTypes.PINK_STAINED_GLASS, StateTypes.GRAY_STAINED_GLASS, StateTypes.LIGHT_GRAY_STAINED_GLASS, StateTypes.CYAN_STAINED_GLASS, StateTypes.PURPLE_STAINED_GLASS, StateTypes.BLUE_STAINED_GLASS, StateTypes.BROWN_STAINED_GLASS, StateTypes.GREEN_STAINED_GLASS, StateTypes.RED_STAINED_GLASS, StateTypes.BLACK_STAINED_GLASS, StateTypes.TINTED_GLASS);
        BlockTags.WOODEN_TRAPDOORS.add(StateTypes.ACACIA_TRAPDOOR, StateTypes.BIRCH_TRAPDOOR, StateTypes.DARK_OAK_TRAPDOOR, StateTypes.JUNGLE_TRAPDOOR, StateTypes.OAK_TRAPDOOR, StateTypes.SPRUCE_TRAPDOOR, StateTypes.CRIMSON_TRAPDOOR, StateTypes.WARPED_TRAPDOOR, StateTypes.MANGROVE_TRAPDOOR);
        BlockTags.TRAPDOORS.addTag(BlockTags.WOODEN_TRAPDOORS).add(StateTypes.IRON_TRAPDOOR);
        BlockTags.UNDERWATER_BONEMEALS.add(StateTypes.SEAGRASS).addTag(BlockTags.CORALS).addTag(BlockTags.WALL_CORALS);
        BlockTags.BAMBOO_PLANTABLE_ON.addTag(BlockTags.SAND).addTag(BlockTags.DIRT).add(StateTypes.BAMBOO, StateTypes.BAMBOO_SAPLING, StateTypes.GRAVEL);
        BlockTags.STANDING_SIGNS.add(StateTypes.OAK_SIGN, StateTypes.SPRUCE_SIGN, StateTypes.BIRCH_SIGN, StateTypes.ACACIA_SIGN, StateTypes.JUNGLE_SIGN, StateTypes.DARK_OAK_SIGN, StateTypes.CRIMSON_SIGN, StateTypes.WARPED_SIGN, StateTypes.MANGROVE_SIGN);
        BlockTags.WALL_SIGNS.add(StateTypes.OAK_WALL_SIGN, StateTypes.SPRUCE_WALL_SIGN, StateTypes.BIRCH_WALL_SIGN, StateTypes.ACACIA_WALL_SIGN, StateTypes.JUNGLE_WALL_SIGN, StateTypes.DARK_OAK_WALL_SIGN, StateTypes.CRIMSON_WALL_SIGN, StateTypes.WARPED_WALL_SIGN, StateTypes.MANGROVE_WALL_SIGN);
        BlockTags.SIGNS.addTag(BlockTags.STANDING_SIGNS).addTag(BlockTags.WALL_SIGNS);
        BlockTags.BEDS.add(StateTypes.RED_BED, StateTypes.BLACK_BED, StateTypes.BLUE_BED, StateTypes.BROWN_BED, StateTypes.CYAN_BED, StateTypes.GRAY_BED, StateTypes.GREEN_BED, StateTypes.LIGHT_BLUE_BED, StateTypes.LIGHT_GRAY_BED, StateTypes.LIME_BED, StateTypes.MAGENTA_BED, StateTypes.ORANGE_BED, StateTypes.PINK_BED, StateTypes.PURPLE_BED, StateTypes.WHITE_BED, StateTypes.YELLOW_BED);
        BlockTags.FENCES.addTag(BlockTags.WOODEN_FENCES).add(StateTypes.NETHER_BRICK_FENCE);
        BlockTags.DRAGON_IMMUNE.add(StateTypes.BARRIER, StateTypes.BEDROCK, StateTypes.END_PORTAL, StateTypes.END_PORTAL_FRAME, StateTypes.END_GATEWAY, StateTypes.COMMAND_BLOCK, StateTypes.REPEATING_COMMAND_BLOCK, StateTypes.CHAIN_COMMAND_BLOCK, StateTypes.STRUCTURE_BLOCK, StateTypes.JIGSAW, StateTypes.MOVING_PISTON, StateTypes.OBSIDIAN, StateTypes.CRYING_OBSIDIAN, StateTypes.END_STONE, StateTypes.IRON_BARS, StateTypes.RESPAWN_ANCHOR, StateTypes.REINFORCED_DEEPSLATE);
        // TODO: this.tag(BlockTags.DRAGON_TRANSPARENT).add(Blocks.LIGHT).addTag(BlockTags.FIRE);
        BlockTags.WITHER_IMMUNE.add(StateTypes.BARRIER, StateTypes.BEDROCK, StateTypes.END_PORTAL, StateTypes.END_PORTAL_FRAME, StateTypes.END_GATEWAY, StateTypes.COMMAND_BLOCK, StateTypes.REPEATING_COMMAND_BLOCK, StateTypes.CHAIN_COMMAND_BLOCK, StateTypes.STRUCTURE_BLOCK, StateTypes.JIGSAW, StateTypes.MOVING_PISTON, StateTypes.LIGHT, StateTypes.REINFORCED_DEEPSLATE); // TODO: 1.17/1.18 doesn't have light (does it matter?)
        BlockTags.WITHER_SUMMON_BASE_BLOCKS.add(StateTypes.SOUL_SAND, StateTypes.SOUL_SOIL);
        BlockTags.TALL_FLOWERS.add(StateTypes.SUNFLOWER, StateTypes.LILAC, StateTypes.PEONY, StateTypes.ROSE_BUSH);
        BlockTags.FLOWERS.addTag(BlockTags.SMALL_FLOWERS).addTag(BlockTags.TALL_FLOWERS).add(StateTypes.FLOWERING_AZALEA_LEAVES, StateTypes.FLOWERING_AZALEA, StateTypes.MANGROVE_PROPAGULE);
        BlockTags.BEEHIVES.add(StateTypes.BEE_NEST, StateTypes.BEEHIVE);
        BlockTags.CROPS.add(StateTypes.BEETROOTS, StateTypes.CARROTS, StateTypes.POTATOES, StateTypes.WHEAT, StateTypes.MELON_STEM, StateTypes.PUMPKIN_STEM);
        BlockTags.BEE_GROWABLES.addTag(BlockTags.CROPS).add(StateTypes.SWEET_BERRY_BUSH).add(StateTypes.CAVE_VINES).add(StateTypes.CAVE_VINES_PLANT);
        BlockTags.SHULKER_BOXES.add(StateTypes.SHULKER_BOX, StateTypes.BLACK_SHULKER_BOX, StateTypes.BLUE_SHULKER_BOX, StateTypes.BROWN_SHULKER_BOX, StateTypes.CYAN_SHULKER_BOX, StateTypes.GRAY_SHULKER_BOX, StateTypes.GREEN_SHULKER_BOX, StateTypes.LIGHT_BLUE_SHULKER_BOX, StateTypes.LIGHT_GRAY_SHULKER_BOX, StateTypes.LIME_SHULKER_BOX, StateTypes.MAGENTA_SHULKER_BOX, StateTypes.ORANGE_SHULKER_BOX, StateTypes.PINK_SHULKER_BOX, StateTypes.PURPLE_SHULKER_BOX, StateTypes.RED_SHULKER_BOX, StateTypes.WHITE_SHULKER_BOX, StateTypes.YELLOW_SHULKER_BOX);
        BlockTags.PORTALS.add(StateTypes.NETHER_PORTAL, StateTypes.END_PORTAL, StateTypes.END_GATEWAY);
        BlockTags.FIRE.add(StateTypes.FIRE, StateTypes.SOUL_FIRE);
        BlockTags.NYLIUM.add(StateTypes.CRIMSON_NYLIUM, StateTypes.WARPED_NYLIUM);
        BlockTags.WART_BLOCKS.add(StateTypes.NETHER_WART_BLOCK, StateTypes.WARPED_WART_BLOCK);
        BlockTags.BEACON_BASE_BLOCKS.add(StateTypes.NETHERITE_BLOCK, StateTypes.EMERALD_BLOCK, StateTypes.DIAMOND_BLOCK, StateTypes.GOLD_BLOCK, StateTypes.IRON_BLOCK);
        BlockTags.SOUL_SPEED_BLOCKS.add(StateTypes.SOUL_SAND, StateTypes.SOUL_SOIL);
        BlockTags.WALL_POST_OVERRIDE.add(StateTypes.TORCH, StateTypes.SOUL_TORCH, StateTypes.REDSTONE_TORCH, StateTypes.TRIPWIRE).addTag(BlockTags.SIGNS).addTag(BlockTags.BANNERS).addTag(BlockTags.PRESSURE_PLATES);
        BlockTags.CLIMBABLE.add(StateTypes.LADDER, StateTypes.VINE, StateTypes.SCAFFOLDING, StateTypes.WEEPING_VINES, StateTypes.WEEPING_VINES_PLANT, StateTypes.TWISTING_VINES, StateTypes.TWISTING_VINES_PLANT, StateTypes.CAVE_VINES, StateTypes.CAVE_VINES_PLANT);
        BlockTags.PIGLIN_REPELLENTS.add(StateTypes.SOUL_FIRE).add(StateTypes.SOUL_TORCH).add(StateTypes.SOUL_LANTERN).add(StateTypes.SOUL_WALL_TORCH).add(StateTypes.SOUL_CAMPFIRE);
        BlockTags.HOGLIN_REPELLENTS.add(StateTypes.WARPED_FUNGUS).add(StateTypes.POTTED_WARPED_FUNGUS).add(StateTypes.NETHER_PORTAL).add(StateTypes.RESPAWN_ANCHOR);
        BlockTags.GOLD_ORES.add(StateTypes.GOLD_ORE, StateTypes.NETHER_GOLD_ORE, StateTypes.DEEPSLATE_GOLD_ORE);
        BlockTags.IRON_ORES.add(StateTypes.IRON_ORE, StateTypes.DEEPSLATE_IRON_ORE);
        BlockTags.DIAMOND_ORES.add(StateTypes.DIAMOND_ORE, StateTypes.DEEPSLATE_DIAMOND_ORE);
        BlockTags.REDSTONE_ORES.add(StateTypes.REDSTONE_ORE, StateTypes.DEEPSLATE_REDSTONE_ORE);
        BlockTags.COAL_ORES.add(StateTypes.COAL_ORE, StateTypes.DEEPSLATE_COAL_ORE);
        BlockTags.EMERALD_ORES.add(StateTypes.EMERALD_ORE, StateTypes.DEEPSLATE_EMERALD_ORE);
        BlockTags.COPPER_ORES.add(StateTypes.COPPER_ORE, StateTypes.DEEPSLATE_COPPER_ORE);
        BlockTags.LAPIS_ORES.add(StateTypes.LAPIS_ORE, StateTypes.DEEPSLATE_LAPIS_ORE);
        BlockTags.SOUL_FIRE_BASE_BLOCKS.add(StateTypes.SOUL_SAND, StateTypes.SOUL_SOIL);
        BlockTags.NON_FLAMMABLE_WOOD.add(StateTypes.WARPED_STEM, StateTypes.STRIPPED_WARPED_STEM, StateTypes.WARPED_HYPHAE, StateTypes.STRIPPED_WARPED_HYPHAE, StateTypes.CRIMSON_STEM, StateTypes.STRIPPED_CRIMSON_STEM, StateTypes.CRIMSON_HYPHAE, StateTypes.STRIPPED_CRIMSON_HYPHAE, StateTypes.CRIMSON_PLANKS, StateTypes.WARPED_PLANKS, StateTypes.CRIMSON_SLAB, StateTypes.WARPED_SLAB, StateTypes.CRIMSON_PRESSURE_PLATE, StateTypes.WARPED_PRESSURE_PLATE, StateTypes.CRIMSON_FENCE, StateTypes.WARPED_FENCE, StateTypes.CRIMSON_TRAPDOOR, StateTypes.WARPED_TRAPDOOR, StateTypes.CRIMSON_FENCE_GATE, StateTypes.WARPED_FENCE_GATE, StateTypes.CRIMSON_STAIRS, StateTypes.WARPED_STAIRS, StateTypes.CRIMSON_BUTTON, StateTypes.WARPED_BUTTON, StateTypes.CRIMSON_DOOR, StateTypes.WARPED_DOOR, StateTypes.CRIMSON_SIGN, StateTypes.WARPED_SIGN, StateTypes.CRIMSON_WALL_SIGN, StateTypes.WARPED_WALL_SIGN);
        BlockTags.STRIDER_WARM_BLOCKS.add(StateTypes.LAVA);
        BlockTags.CAMPFIRES.add(StateTypes.CAMPFIRE, StateTypes.SOUL_CAMPFIRE);
        BlockTags.GUARDED_BY_PIGLINS.add(StateTypes.GOLD_BLOCK, StateTypes.BARREL, StateTypes.CHEST, StateTypes.ENDER_CHEST, StateTypes.GILDED_BLACKSTONE, StateTypes.TRAPPED_CHEST, StateTypes.RAW_GOLD_BLOCK).addTag(BlockTags.SHULKER_BOXES).addTag(BlockTags.GOLD_ORES);
        BlockTags.PREVENT_MOB_SPAWNING_INSIDE.addTag(BlockTags.RAILS);
        BlockTags.FENCE_GATES.add(StateTypes.ACACIA_FENCE_GATE, StateTypes.BIRCH_FENCE_GATE, StateTypes.DARK_OAK_FENCE_GATE, StateTypes.JUNGLE_FENCE_GATE, StateTypes.OAK_FENCE_GATE, StateTypes.SPRUCE_FENCE_GATE, StateTypes.CRIMSON_FENCE_GATE, StateTypes.WARPED_FENCE_GATE, StateTypes.MANGROVE_FENCE_GATE);
        BlockTags.UNSTABLE_BOTTOM_CENTER.addTag(BlockTags.FENCE_GATES);
        BlockTags.MUSHROOM_GROW_BLOCK.add(StateTypes.MYCELIUM).add(StateTypes.PODZOL).add(StateTypes.CRIMSON_NYLIUM).add(StateTypes.WARPED_NYLIUM);
        BlockTags.INFINIBURN_OVERWORLD.add(StateTypes.NETHERRACK, StateTypes.MAGMA_BLOCK);
        BlockTags.INFINIBURN_NETHER.addTag(BlockTags.INFINIBURN_OVERWORLD);
        BlockTags.INFINIBURN_END.addTag(BlockTags.INFINIBURN_OVERWORLD).add(StateTypes.BEDROCK);
        BlockTags.STONE_ORE_REPLACEABLES.add(StateTypes.STONE).add(StateTypes.GRANITE).add(StateTypes.DIORITE).add(StateTypes.ANDESITE);
        BlockTags.DEEPSLATE_ORE_REPLACEABLES.add(StateTypes.DEEPSLATE).add(StateTypes.TUFF);
        BlockTags.BASE_STONE_OVERWORLD.add(StateTypes.STONE).add(StateTypes.GRANITE).add(StateTypes.DIORITE).add(StateTypes.ANDESITE).add(StateTypes.TUFF).add(StateTypes.DEEPSLATE);
        BlockTags.BASE_STONE_NETHER.add(StateTypes.NETHERRACK).add(StateTypes.BASALT).add(StateTypes.BLACKSTONE);
        // TODO:       this.tag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).addTag(BlockTags.BASE_STONE_OVERWORLD).addTag(BlockTags.DIRT).addTag(BlockTags.SAND).addTag(BlockTags.TERRACOTTA).addTag(BlockTags.IRON_ORES).addTag(BlockTags.COPPER_ORES).add(Blocks.WATER, Blocks.GRAVEL, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.CALCITE, Blocks.SNOW, Blocks.PACKED_ICE, Blocks.RAW_IRON_BLOCK, Blocks.RAW_COPPER_BLOCK);
        //      this.tag(BlockTags.NETHER_CARVER_REPLACEABLES).addTag(BlockTags.BASE_STONE_OVERWORLD).addTag(BlockTags.BASE_STONE_NETHER).addTag(BlockTags.DIRT).addTag(BlockTags.NYLIUM).addTag(BlockTags.WART_BLOCKS).add(Blocks.SOUL_SAND, Blocks.SOUL_SOIL);
        BlockTags.CANDLES.add(StateTypes.CANDLE, StateTypes.WHITE_CANDLE, StateTypes.ORANGE_CANDLE, StateTypes.MAGENTA_CANDLE, StateTypes.LIGHT_BLUE_CANDLE, StateTypes.YELLOW_CANDLE, StateTypes.LIME_CANDLE, StateTypes.PINK_CANDLE, StateTypes.GRAY_CANDLE, StateTypes.LIGHT_GRAY_CANDLE, StateTypes.CYAN_CANDLE, StateTypes.PURPLE_CANDLE, StateTypes.BLUE_CANDLE, StateTypes.BROWN_CANDLE, StateTypes.GREEN_CANDLE, StateTypes.RED_CANDLE, StateTypes.BLACK_CANDLE);
        BlockTags.CANDLE_CAKES.add(StateTypes.CANDLE_CAKE, StateTypes.WHITE_CANDLE_CAKE, StateTypes.ORANGE_CANDLE_CAKE, StateTypes.MAGENTA_CANDLE_CAKE, StateTypes.LIGHT_BLUE_CANDLE_CAKE, StateTypes.YELLOW_CANDLE_CAKE, StateTypes.LIME_CANDLE_CAKE, StateTypes.PINK_CANDLE_CAKE, StateTypes.GRAY_CANDLE_CAKE, StateTypes.LIGHT_GRAY_CANDLE_CAKE, StateTypes.CYAN_CANDLE_CAKE, StateTypes.PURPLE_CANDLE_CAKE, StateTypes.BLUE_CANDLE_CAKE, StateTypes.BROWN_CANDLE_CAKE, StateTypes.GREEN_CANDLE_CAKE, StateTypes.RED_CANDLE_CAKE, StateTypes.BLACK_CANDLE_CAKE);
        BlockTags.CRYSTAL_SOUND_BLOCKS.add(StateTypes.AMETHYST_BLOCK, StateTypes.BUDDING_AMETHYST);
        BlockTags.CAULDRONS.add(StateTypes.CAULDRON, StateTypes.WATER_CAULDRON, StateTypes.LAVA_CAULDRON, StateTypes.POWDER_SNOW_CAULDRON);
        BlockTags.INSIDE_STEP_SOUND_BLOCKS.add(StateTypes.SNOW, StateTypes.POWDER_SNOW, StateTypes.SCULK_VEIN);
        BlockTags.DRIPSTONE_REPLACEABLE.addTag(BlockTags.BASE_STONE_OVERWORLD).add(StateTypes.DIRT);
        BlockTags.CAVE_VINES.add(StateTypes.CAVE_VINES_PLANT).add(StateTypes.CAVE_VINES);
        BlockTags.MOSS_REPLACEABLE.addTag(BlockTags.BASE_STONE_OVERWORLD).addTag(BlockTags.CAVE_VINES).addTag(BlockTags.DIRT);
        BlockTags.LUSH_GROUND_REPLACEABLE.addTag(BlockTags.MOSS_REPLACEABLE).add(StateTypes.CLAY).add(StateTypes.GRAVEL).add(StateTypes.SAND);
        BlockTags.AZALEA_ROOT_REPLACEABLE.addTag(BlockTags.LUSH_GROUND_REPLACEABLE).addTag(BlockTags.TERRACOTTA).add(StateTypes.RED_SAND);
        BlockTags.SMALL_DRIPLEAF_PLACEABLE.add(StateTypes.CLAY).add(StateTypes.MOSS_BLOCK);
        BlockTags.BIG_DRIPLEAF_PLACEABLE.addTag(BlockTags.SMALL_DRIPLEAF_PLACEABLE).addTag(BlockTags.DIRT).add(StateTypes.FARMLAND);
        BlockTags.OCCLUDES_VIBRATION_SIGNALS.addTag(BlockTags.WOOL);
        BlockTags.SNOW.add(StateTypes.SNOW, StateTypes.SNOW_BLOCK, StateTypes.POWDER_SNOW);
        BlockTags.MINEABLE_WITH_AXE.add(StateTypes.NOTE_BLOCK, StateTypes.ATTACHED_MELON_STEM, StateTypes.ATTACHED_PUMPKIN_STEM, StateTypes.AZALEA, StateTypes.BAMBOO, StateTypes.BARREL, StateTypes.BEE_NEST, StateTypes.BEEHIVE, StateTypes.BEETROOTS, StateTypes.BIG_DRIPLEAF_STEM, StateTypes.BIG_DRIPLEAF, StateTypes.BOOKSHELF, StateTypes.BROWN_MUSHROOM_BLOCK, StateTypes.BROWN_MUSHROOM, StateTypes.CAMPFIRE, StateTypes.CARROTS, StateTypes.CARTOGRAPHY_TABLE, StateTypes.CARVED_PUMPKIN, StateTypes.CAVE_VINES_PLANT, StateTypes.CAVE_VINES, StateTypes.CHEST, StateTypes.CHORUS_FLOWER, StateTypes.CHORUS_PLANT, StateTypes.COCOA, StateTypes.COMPOSTER, StateTypes.CRAFTING_TABLE, StateTypes.CRIMSON_FUNGUS, StateTypes.DAYLIGHT_DETECTOR, StateTypes.DEAD_BUSH, StateTypes.FERN, StateTypes.FLETCHING_TABLE, StateTypes.GLOW_LICHEN, StateTypes.GRASS, StateTypes.HANGING_ROOTS, StateTypes.JACK_O_LANTERN, StateTypes.JUKEBOX, StateTypes.LADDER, StateTypes.LARGE_FERN, StateTypes.LECTERN, StateTypes.LILY_PAD, StateTypes.LOOM, StateTypes.MELON_STEM, StateTypes.MELON, StateTypes.MUSHROOM_STEM, StateTypes.NETHER_WART, StateTypes.POTATOES, StateTypes.PUMPKIN_STEM, StateTypes.PUMPKIN, StateTypes.RED_MUSHROOM_BLOCK, StateTypes.RED_MUSHROOM, StateTypes.SCAFFOLDING, StateTypes.SMALL_DRIPLEAF, StateTypes.SMITHING_TABLE, StateTypes.SOUL_CAMPFIRE, StateTypes.SPORE_BLOSSOM, StateTypes.SUGAR_CANE, StateTypes.SWEET_BERRY_BUSH, StateTypes.TALL_GRASS, StateTypes.TRAPPED_CHEST, StateTypes.TWISTING_VINES_PLANT, StateTypes.TWISTING_VINES, StateTypes.VINE, StateTypes.WARPED_FUNGUS, StateTypes.WEEPING_VINES_PLANT, StateTypes.WEEPING_VINES, StateTypes.WHEAT).addTag(BlockTags.BANNERS).addTag(BlockTags.FENCE_GATES).addTag(BlockTags.LOGS).addTag(BlockTags.PLANKS).addTag(BlockTags.SAPLINGS).addTag(BlockTags.SIGNS).addTag(BlockTags.WOODEN_BUTTONS).addTag(BlockTags.WOODEN_DOORS).addTag(BlockTags.WOODEN_FENCES).addTag(BlockTags.WOODEN_PRESSURE_PLATES).addTag(BlockTags.WOODEN_SLABS).addTag(BlockTags.WOODEN_STAIRS).addTag(BlockTags.WOODEN_TRAPDOORS).add(StateTypes.MANGROVE_ROOTS);
        BlockTags.MINEABLE_WITH_HOE.add(StateTypes.NETHER_WART_BLOCK, StateTypes.WARPED_WART_BLOCK, StateTypes.HAY_BLOCK, StateTypes.DRIED_KELP_BLOCK, StateTypes.TARGET, StateTypes.SHROOMLIGHT, StateTypes.SPONGE, StateTypes.WET_SPONGE, StateTypes.JUNGLE_LEAVES, StateTypes.OAK_LEAVES, StateTypes.SPRUCE_LEAVES, StateTypes.DARK_OAK_LEAVES, StateTypes.ACACIA_LEAVES, StateTypes.BIRCH_LEAVES, StateTypes.AZALEA_LEAVES, StateTypes.FLOWERING_AZALEA_LEAVES, StateTypes.SCULK_SENSOR, StateTypes.MOSS_BLOCK, StateTypes.MOSS_CARPET, StateTypes.MANGROVE_LEAVES, StateTypes.SCULK, StateTypes.SCULK_CATALYST, StateTypes.SCULK_VEIN, StateTypes.SCULK_SHRIEKER);
        BlockTags.MINEABLE_WITH_PICKAXE.add(StateTypes.STONE, StateTypes.GRANITE, StateTypes.POLISHED_GRANITE, StateTypes.DIORITE, StateTypes.POLISHED_DIORITE, StateTypes.ANDESITE, StateTypes.POLISHED_ANDESITE, StateTypes.COBBLESTONE, StateTypes.GOLD_ORE, StateTypes.DEEPSLATE_GOLD_ORE, StateTypes.IRON_ORE, StateTypes.DEEPSLATE_IRON_ORE, StateTypes.COAL_ORE, StateTypes.DEEPSLATE_COAL_ORE, StateTypes.NETHER_GOLD_ORE, StateTypes.LAPIS_ORE, StateTypes.DEEPSLATE_LAPIS_ORE, StateTypes.LAPIS_BLOCK, StateTypes.DISPENSER, StateTypes.SANDSTONE, StateTypes.CHISELED_SANDSTONE, StateTypes.CUT_SANDSTONE, StateTypes.GOLD_BLOCK, StateTypes.IRON_BLOCK, StateTypes.BRICKS, StateTypes.MOSSY_COBBLESTONE, StateTypes.OBSIDIAN, StateTypes.SPAWNER, StateTypes.DIAMOND_ORE, StateTypes.DEEPSLATE_DIAMOND_ORE, StateTypes.DIAMOND_BLOCK, StateTypes.FURNACE, StateTypes.COBBLESTONE_STAIRS, StateTypes.STONE_PRESSURE_PLATE, StateTypes.IRON_DOOR, StateTypes.REDSTONE_ORE, StateTypes.DEEPSLATE_REDSTONE_ORE, StateTypes.NETHERRACK, StateTypes.BASALT, StateTypes.POLISHED_BASALT, StateTypes.STONE_BRICKS, StateTypes.MOSSY_STONE_BRICKS, StateTypes.CRACKED_STONE_BRICKS, StateTypes.CHISELED_STONE_BRICKS, StateTypes.IRON_BARS, StateTypes.CHAIN, StateTypes.BRICK_STAIRS, StateTypes.STONE_BRICK_STAIRS, StateTypes.NETHER_BRICKS, StateTypes.NETHER_BRICK_FENCE, StateTypes.NETHER_BRICK_STAIRS, StateTypes.ENCHANTING_TABLE, StateTypes.BREWING_STAND, StateTypes.END_STONE, StateTypes.SANDSTONE_STAIRS, StateTypes.EMERALD_ORE, StateTypes.DEEPSLATE_EMERALD_ORE, StateTypes.ENDER_CHEST, StateTypes.EMERALD_BLOCK, StateTypes.LIGHT_WEIGHTED_PRESSURE_PLATE, StateTypes.HEAVY_WEIGHTED_PRESSURE_PLATE, StateTypes.REDSTONE_BLOCK, StateTypes.NETHER_QUARTZ_ORE, StateTypes.HOPPER, StateTypes.QUARTZ_BLOCK, StateTypes.CHISELED_QUARTZ_BLOCK, StateTypes.QUARTZ_PILLAR, StateTypes.QUARTZ_STAIRS, StateTypes.DROPPER, StateTypes.WHITE_TERRACOTTA, StateTypes.ORANGE_TERRACOTTA, StateTypes.MAGENTA_TERRACOTTA, StateTypes.LIGHT_BLUE_TERRACOTTA, StateTypes.YELLOW_TERRACOTTA, StateTypes.LIME_TERRACOTTA, StateTypes.PINK_TERRACOTTA, StateTypes.GRAY_TERRACOTTA, StateTypes.LIGHT_GRAY_TERRACOTTA, StateTypes.CYAN_TERRACOTTA, StateTypes.PURPLE_TERRACOTTA, StateTypes.BLUE_TERRACOTTA, StateTypes.BROWN_TERRACOTTA, StateTypes.GREEN_TERRACOTTA, StateTypes.RED_TERRACOTTA, StateTypes.BLACK_TERRACOTTA, StateTypes.IRON_TRAPDOOR, StateTypes.PRISMARINE, StateTypes.PRISMARINE_BRICKS, StateTypes.DARK_PRISMARINE, StateTypes.PRISMARINE_STAIRS, StateTypes.PRISMARINE_BRICK_STAIRS, StateTypes.DARK_PRISMARINE_STAIRS, StateTypes.PRISMARINE_SLAB, StateTypes.PRISMARINE_BRICK_SLAB, StateTypes.DARK_PRISMARINE_SLAB, StateTypes.TERRACOTTA, StateTypes.COAL_BLOCK, StateTypes.RED_SANDSTONE, StateTypes.CHISELED_RED_SANDSTONE, StateTypes.CUT_RED_SANDSTONE, StateTypes.RED_SANDSTONE_STAIRS, StateTypes.STONE_SLAB, StateTypes.SMOOTH_STONE_SLAB, StateTypes.SANDSTONE_SLAB, StateTypes.CUT_SANDSTONE_SLAB, StateTypes.PETRIFIED_OAK_SLAB, StateTypes.COBBLESTONE_SLAB, StateTypes.BRICK_SLAB, StateTypes.STONE_BRICK_SLAB, StateTypes.NETHER_BRICK_SLAB, StateTypes.QUARTZ_SLAB, StateTypes.RED_SANDSTONE_SLAB, StateTypes.CUT_RED_SANDSTONE_SLAB, StateTypes.PURPUR_SLAB, StateTypes.SMOOTH_STONE, StateTypes.SMOOTH_SANDSTONE, StateTypes.SMOOTH_QUARTZ, StateTypes.SMOOTH_RED_SANDSTONE, StateTypes.PURPUR_BLOCK, StateTypes.PURPUR_PILLAR, StateTypes.PURPUR_STAIRS, StateTypes.END_STONE_BRICKS, StateTypes.MAGMA_BLOCK, StateTypes.RED_NETHER_BRICKS, StateTypes.BONE_BLOCK, StateTypes.OBSERVER, StateTypes.WHITE_GLAZED_TERRACOTTA, StateTypes.ORANGE_GLAZED_TERRACOTTA, StateTypes.MAGENTA_GLAZED_TERRACOTTA, StateTypes.LIGHT_BLUE_GLAZED_TERRACOTTA, StateTypes.YELLOW_GLAZED_TERRACOTTA, StateTypes.LIME_GLAZED_TERRACOTTA, StateTypes.PINK_GLAZED_TERRACOTTA, StateTypes.GRAY_GLAZED_TERRACOTTA, StateTypes.LIGHT_GRAY_GLAZED_TERRACOTTA, StateTypes.CYAN_GLAZED_TERRACOTTA, StateTypes.PURPLE_GLAZED_TERRACOTTA, StateTypes.BLUE_GLAZED_TERRACOTTA, StateTypes.BROWN_GLAZED_TERRACOTTA, StateTypes.GREEN_GLAZED_TERRACOTTA, StateTypes.RED_GLAZED_TERRACOTTA, StateTypes.BLACK_GLAZED_TERRACOTTA, StateTypes.WHITE_CONCRETE, StateTypes.ORANGE_CONCRETE, StateTypes.MAGENTA_CONCRETE, StateTypes.LIGHT_BLUE_CONCRETE, StateTypes.YELLOW_CONCRETE, StateTypes.LIME_CONCRETE, StateTypes.PINK_CONCRETE, StateTypes.GRAY_CONCRETE, StateTypes.LIGHT_GRAY_CONCRETE, StateTypes.CYAN_CONCRETE, StateTypes.PURPLE_CONCRETE, StateTypes.BLUE_CONCRETE, StateTypes.BROWN_CONCRETE, StateTypes.GREEN_CONCRETE, StateTypes.RED_CONCRETE, StateTypes.BLACK_CONCRETE, StateTypes.DEAD_TUBE_CORAL_BLOCK, StateTypes.DEAD_BRAIN_CORAL_BLOCK, StateTypes.DEAD_BUBBLE_CORAL_BLOCK, StateTypes.DEAD_FIRE_CORAL_BLOCK, StateTypes.DEAD_HORN_CORAL_BLOCK, StateTypes.TUBE_CORAL_BLOCK, StateTypes.BRAIN_CORAL_BLOCK, StateTypes.BUBBLE_CORAL_BLOCK, StateTypes.FIRE_CORAL_BLOCK, StateTypes.HORN_CORAL_BLOCK, StateTypes.DEAD_TUBE_CORAL, StateTypes.DEAD_BRAIN_CORAL, StateTypes.DEAD_BUBBLE_CORAL, StateTypes.DEAD_FIRE_CORAL, StateTypes.DEAD_HORN_CORAL, StateTypes.DEAD_TUBE_CORAL_FAN, StateTypes.DEAD_BRAIN_CORAL_FAN, StateTypes.DEAD_BUBBLE_CORAL_FAN, StateTypes.DEAD_FIRE_CORAL_FAN, StateTypes.DEAD_HORN_CORAL_FAN, StateTypes.DEAD_TUBE_CORAL_WALL_FAN, StateTypes.DEAD_BRAIN_CORAL_WALL_FAN, StateTypes.DEAD_BUBBLE_CORAL_WALL_FAN, StateTypes.DEAD_FIRE_CORAL_WALL_FAN, StateTypes.DEAD_HORN_CORAL_WALL_FAN, StateTypes.POLISHED_GRANITE_STAIRS, StateTypes.SMOOTH_RED_SANDSTONE_STAIRS, StateTypes.MOSSY_STONE_BRICK_STAIRS, StateTypes.POLISHED_DIORITE_STAIRS, StateTypes.MOSSY_COBBLESTONE_STAIRS, StateTypes.END_STONE_BRICK_STAIRS, StateTypes.STONE_STAIRS, StateTypes.SMOOTH_SANDSTONE_STAIRS, StateTypes.SMOOTH_QUARTZ_STAIRS, StateTypes.GRANITE_STAIRS, StateTypes.ANDESITE_STAIRS, StateTypes.RED_NETHER_BRICK_STAIRS, StateTypes.POLISHED_ANDESITE_STAIRS, StateTypes.DIORITE_STAIRS, StateTypes.POLISHED_GRANITE_SLAB, StateTypes.SMOOTH_RED_SANDSTONE_SLAB, StateTypes.MOSSY_STONE_BRICK_SLAB, StateTypes.POLISHED_DIORITE_SLAB, StateTypes.MOSSY_COBBLESTONE_SLAB, StateTypes.END_STONE_BRICK_SLAB, StateTypes.SMOOTH_SANDSTONE_SLAB, StateTypes.SMOOTH_QUARTZ_SLAB, StateTypes.GRANITE_SLAB, StateTypes.ANDESITE_SLAB, StateTypes.RED_NETHER_BRICK_SLAB, StateTypes.POLISHED_ANDESITE_SLAB, StateTypes.DIORITE_SLAB, StateTypes.SMOKER, StateTypes.BLAST_FURNACE, StateTypes.GRINDSTONE, StateTypes.STONECUTTER, StateTypes.BELL, StateTypes.LANTERN, StateTypes.SOUL_LANTERN, StateTypes.WARPED_NYLIUM, StateTypes.CRIMSON_NYLIUM, StateTypes.NETHERITE_BLOCK, StateTypes.ANCIENT_DEBRIS, StateTypes.CRYING_OBSIDIAN, StateTypes.RESPAWN_ANCHOR, StateTypes.LODESTONE, StateTypes.BLACKSTONE, StateTypes.BLACKSTONE_STAIRS, StateTypes.BLACKSTONE_SLAB, StateTypes.POLISHED_BLACKSTONE, StateTypes.POLISHED_BLACKSTONE_BRICKS, StateTypes.CRACKED_POLISHED_BLACKSTONE_BRICKS, StateTypes.CHISELED_POLISHED_BLACKSTONE, StateTypes.POLISHED_BLACKSTONE_BRICK_SLAB, StateTypes.POLISHED_BLACKSTONE_BRICK_STAIRS, StateTypes.GILDED_BLACKSTONE, StateTypes.POLISHED_BLACKSTONE_STAIRS, StateTypes.POLISHED_BLACKSTONE_SLAB, StateTypes.POLISHED_BLACKSTONE_PRESSURE_PLATE, StateTypes.CHISELED_NETHER_BRICKS, StateTypes.CRACKED_NETHER_BRICKS, StateTypes.QUARTZ_BRICKS, StateTypes.TUFF, StateTypes.CALCITE, StateTypes.OXIDIZED_COPPER, StateTypes.WEATHERED_COPPER, StateTypes.EXPOSED_COPPER, StateTypes.COPPER_BLOCK, StateTypes.COPPER_ORE, StateTypes.DEEPSLATE_COPPER_ORE, StateTypes.OXIDIZED_CUT_COPPER, StateTypes.WEATHERED_CUT_COPPER, StateTypes.EXPOSED_CUT_COPPER, StateTypes.CUT_COPPER, StateTypes.OXIDIZED_CUT_COPPER_STAIRS, StateTypes.WEATHERED_CUT_COPPER_STAIRS, StateTypes.EXPOSED_CUT_COPPER_STAIRS, StateTypes.CUT_COPPER_STAIRS, StateTypes.OXIDIZED_CUT_COPPER_SLAB, StateTypes.WEATHERED_CUT_COPPER_SLAB, StateTypes.EXPOSED_CUT_COPPER_SLAB, StateTypes.CUT_COPPER_SLAB, StateTypes.WAXED_COPPER_BLOCK, StateTypes.WAXED_WEATHERED_COPPER, StateTypes.WAXED_EXPOSED_COPPER, StateTypes.WAXED_OXIDIZED_COPPER, StateTypes.WAXED_OXIDIZED_CUT_COPPER, StateTypes.WAXED_WEATHERED_CUT_COPPER, StateTypes.WAXED_EXPOSED_CUT_COPPER, StateTypes.WAXED_CUT_COPPER, StateTypes.WAXED_OXIDIZED_CUT_COPPER_STAIRS, StateTypes.WAXED_WEATHERED_CUT_COPPER_STAIRS, StateTypes.WAXED_EXPOSED_CUT_COPPER_STAIRS, StateTypes.WAXED_CUT_COPPER_STAIRS, StateTypes.WAXED_OXIDIZED_CUT_COPPER_SLAB, StateTypes.WAXED_WEATHERED_CUT_COPPER_SLAB, StateTypes.WAXED_EXPOSED_CUT_COPPER_SLAB, StateTypes.WAXED_CUT_COPPER_SLAB, StateTypes.LIGHTNING_ROD, StateTypes.POINTED_DRIPSTONE, StateTypes.DRIPSTONE_BLOCK, StateTypes.DEEPSLATE, StateTypes.COBBLED_DEEPSLATE, StateTypes.COBBLED_DEEPSLATE_STAIRS, StateTypes.COBBLED_DEEPSLATE_SLAB, StateTypes.POLISHED_DEEPSLATE, StateTypes.POLISHED_DEEPSLATE_STAIRS, StateTypes.POLISHED_DEEPSLATE_SLAB, StateTypes.DEEPSLATE_TILES, StateTypes.DEEPSLATE_TILE_STAIRS, StateTypes.DEEPSLATE_TILE_SLAB, StateTypes.DEEPSLATE_BRICKS, StateTypes.DEEPSLATE_BRICK_STAIRS, StateTypes.DEEPSLATE_BRICK_SLAB, StateTypes.CHISELED_DEEPSLATE, StateTypes.CRACKED_DEEPSLATE_BRICKS, StateTypes.CRACKED_DEEPSLATE_TILES, StateTypes.SMOOTH_BASALT, StateTypes.RAW_IRON_BLOCK, StateTypes.RAW_COPPER_BLOCK, StateTypes.RAW_GOLD_BLOCK, StateTypes.ICE, StateTypes.PACKED_ICE, StateTypes.BLUE_ICE, StateTypes.STONE_BUTTON, StateTypes.PISTON, StateTypes.STICKY_PISTON, StateTypes.PISTON_HEAD, StateTypes.AMETHYST_CLUSTER, StateTypes.SMALL_AMETHYST_BUD, StateTypes.MEDIUM_AMETHYST_BUD, StateTypes.LARGE_AMETHYST_BUD, StateTypes.AMETHYST_BLOCK, StateTypes.BUDDING_AMETHYST, StateTypes.INFESTED_COBBLESTONE, StateTypes.INFESTED_CHISELED_STONE_BRICKS, StateTypes.INFESTED_CRACKED_STONE_BRICKS, StateTypes.INFESTED_DEEPSLATE, StateTypes.INFESTED_STONE, StateTypes.INFESTED_MOSSY_STONE_BRICKS, StateTypes.INFESTED_STONE_BRICKS).addTag(BlockTags.WALLS).addTag(BlockTags.SHULKER_BOXES).addTag(BlockTags.ANVIL).addTag(BlockTags.CAULDRONS).addTag(BlockTags.RAILS).add(StateTypes.CONDUIT, StateTypes.MUD_BRICKS, StateTypes.MUD_BRICK_STAIRS, StateTypes.MUD_BRICK_SLAB, StateTypes.PACKED_MUD);
        BlockTags.MINEABLE_WITH_SHOVEL.add(StateTypes.CLAY, StateTypes.DIRT, StateTypes.COARSE_DIRT, StateTypes.PODZOL, StateTypes.FARMLAND, StateTypes.GRASS_BLOCK, StateTypes.GRAVEL, StateTypes.MYCELIUM, StateTypes.SAND, StateTypes.RED_SAND, StateTypes.SNOW_BLOCK, StateTypes.SNOW, StateTypes.SOUL_SAND, StateTypes.DIRT_PATH, StateTypes.WHITE_CONCRETE_POWDER, StateTypes.ORANGE_CONCRETE_POWDER, StateTypes.MAGENTA_CONCRETE_POWDER, StateTypes.LIGHT_BLUE_CONCRETE_POWDER, StateTypes.YELLOW_CONCRETE_POWDER, StateTypes.LIME_CONCRETE_POWDER, StateTypes.PINK_CONCRETE_POWDER, StateTypes.GRAY_CONCRETE_POWDER, StateTypes.LIGHT_GRAY_CONCRETE_POWDER, StateTypes.CYAN_CONCRETE_POWDER, StateTypes.PURPLE_CONCRETE_POWDER, StateTypes.BLUE_CONCRETE_POWDER, StateTypes.BROWN_CONCRETE_POWDER, StateTypes.GREEN_CONCRETE_POWDER, StateTypes.RED_CONCRETE_POWDER, StateTypes.BLACK_CONCRETE_POWDER, StateTypes.SOUL_SOIL, StateTypes.ROOTED_DIRT, StateTypes.MUDDY_MANGROVE_ROOTS, StateTypes.MUD);
        BlockTags.NEEDS_DIAMOND_TOOL.add(StateTypes.OBSIDIAN, StateTypes.CRYING_OBSIDIAN, StateTypes.NETHERITE_BLOCK, StateTypes.RESPAWN_ANCHOR, StateTypes.ANCIENT_DEBRIS);
        BlockTags.NEEDS_IRON_TOOL.add(StateTypes.DIAMOND_BLOCK, StateTypes.DIAMOND_ORE, StateTypes.DEEPSLATE_DIAMOND_ORE, StateTypes.EMERALD_ORE, StateTypes.DEEPSLATE_EMERALD_ORE, StateTypes.EMERALD_BLOCK, StateTypes.GOLD_BLOCK, StateTypes.RAW_GOLD_BLOCK, StateTypes.GOLD_ORE, StateTypes.DEEPSLATE_GOLD_ORE, StateTypes.REDSTONE_ORE, StateTypes.DEEPSLATE_REDSTONE_ORE);
        BlockTags.NEEDS_STONE_TOOL.add(StateTypes.IRON_BLOCK, StateTypes.RAW_IRON_BLOCK, StateTypes.IRON_ORE, StateTypes.DEEPSLATE_IRON_ORE, StateTypes.LAPIS_BLOCK, StateTypes.LAPIS_ORE, StateTypes.DEEPSLATE_LAPIS_ORE, StateTypes.COPPER_BLOCK, StateTypes.RAW_COPPER_BLOCK, StateTypes.COPPER_ORE, StateTypes.DEEPSLATE_COPPER_ORE, StateTypes.CUT_COPPER_SLAB, StateTypes.CUT_COPPER_STAIRS, StateTypes.CUT_COPPER, StateTypes.WEATHERED_COPPER, StateTypes.WEATHERED_CUT_COPPER_SLAB, StateTypes.WEATHERED_CUT_COPPER_STAIRS, StateTypes.WEATHERED_CUT_COPPER, StateTypes.OXIDIZED_COPPER, StateTypes.OXIDIZED_CUT_COPPER_SLAB, StateTypes.OXIDIZED_CUT_COPPER_STAIRS, StateTypes.OXIDIZED_CUT_COPPER, StateTypes.EXPOSED_COPPER, StateTypes.EXPOSED_CUT_COPPER_SLAB, StateTypes.EXPOSED_CUT_COPPER_STAIRS, StateTypes.EXPOSED_CUT_COPPER, StateTypes.WAXED_COPPER_BLOCK, StateTypes.WAXED_CUT_COPPER_SLAB, StateTypes.WAXED_CUT_COPPER_STAIRS, StateTypes.WAXED_CUT_COPPER, StateTypes.WAXED_WEATHERED_COPPER, StateTypes.WAXED_WEATHERED_CUT_COPPER_SLAB, StateTypes.WAXED_WEATHERED_CUT_COPPER_STAIRS, StateTypes.WAXED_WEATHERED_CUT_COPPER, StateTypes.WAXED_EXPOSED_COPPER, StateTypes.WAXED_EXPOSED_CUT_COPPER_SLAB, StateTypes.WAXED_EXPOSED_CUT_COPPER_STAIRS, StateTypes.WAXED_EXPOSED_CUT_COPPER, StateTypes.WAXED_OXIDIZED_COPPER, StateTypes.WAXED_OXIDIZED_CUT_COPPER_SLAB, StateTypes.WAXED_OXIDIZED_CUT_COPPER_STAIRS, StateTypes.WAXED_OXIDIZED_CUT_COPPER, StateTypes.LIGHTNING_ROD);
        BlockTags.FEATURES_CANNOT_REPLACE.add(StateTypes.BEDROCK, StateTypes.SPAWNER, StateTypes.CHEST, StateTypes.END_PORTAL_FRAME, StateTypes.REINFORCED_DEEPSLATE);
        BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE.addTag(BlockTags.FEATURES_CANNOT_REPLACE).addTag(BlockTags.LEAVES).addTag(BlockTags.LOGS);
        BlockTags.GEODE_INVALID_BLOCKS.add(StateTypes.BEDROCK, StateTypes.WATER, StateTypes.LAVA, StateTypes.ICE, StateTypes.PACKED_ICE, StateTypes.BLUE_ICE);
        BlockTags.ANIMALS_SPAWNABLE_ON.add(StateTypes.GRASS_BLOCK);
        BlockTags.AXOLOTLS_SPAWNABLE_ON.add(StateTypes.CLAY);
        BlockTags.GOATS_SPAWNABLE_ON.add(StateTypes.STONE, StateTypes.SNOW, StateTypes.POWDER_SNOW, StateTypes.SNOW_BLOCK, StateTypes.PACKED_ICE, StateTypes.GRAVEL);
        BlockTags.MOOSHROOMS_SPAWNABLE_ON.add(StateTypes.MYCELIUM);
        BlockTags.PARROTS_SPAWNABLE_ON.add(StateTypes.GRASS_BLOCK, StateTypes.AIR).addTag(BlockTags.LEAVES).addTag(BlockTags.LOGS);
        BlockTags.POLAR_BEARS_SPAWNABLE_ON_IN_FROZEN_OCEAN.add(StateTypes.ICE);
        BlockTags.RABBITS_SPAWNABLE_ON.add(StateTypes.GRASS_BLOCK, StateTypes.SNOW, StateTypes.SNOW_BLOCK, StateTypes.SAND);
        BlockTags.FOXES_SPAWNABLE_ON.add(StateTypes.GRASS_BLOCK, StateTypes.SNOW, StateTypes.SNOW_BLOCK, StateTypes.PODZOL, StateTypes.COARSE_DIRT);
        BlockTags.WOLVES_SPAWNABLE_ON.add(StateTypes.GRASS_BLOCK, StateTypes.SNOW, StateTypes.SNOW_BLOCK);
        // TODO:       this.tag(BlockTags.FROGS_SPAWNABLE_ON).add(Blocks.GRASS_BLOCK, Blocks.MUD, Blocks.MANGROVE_ROOTS, Blocks.MUDDY_MANGROVE_ROOTS);
        BlockTags.TERRACOTTA.add(StateTypes.TERRACOTTA, StateTypes.WHITE_TERRACOTTA, StateTypes.ORANGE_TERRACOTTA, StateTypes.MAGENTA_TERRACOTTA, StateTypes.LIGHT_BLUE_TERRACOTTA, StateTypes.YELLOW_TERRACOTTA, StateTypes.LIME_TERRACOTTA, StateTypes.PINK_TERRACOTTA, StateTypes.GRAY_TERRACOTTA, StateTypes.LIGHT_GRAY_TERRACOTTA, StateTypes.CYAN_TERRACOTTA, StateTypes.PURPLE_TERRACOTTA, StateTypes.BLUE_TERRACOTTA, StateTypes.BROWN_TERRACOTTA, StateTypes.GREEN_TERRACOTTA, StateTypes.RED_TERRACOTTA, StateTypes.BLACK_TERRACOTTA);
        BlockTags.AZALEA_GROWS_ON.addTag(BlockTags.DIRT).addTag(BlockTags.SAND).addTag(BlockTags.TERRACOTTA).add(StateTypes.SNOW_BLOCK).add(StateTypes.POWDER_SNOW);
        BlockTags.REPLACEABLE_PLANTS.add(StateTypes.GRASS, StateTypes.FERN, StateTypes.DEAD_BUSH, StateTypes.VINE, StateTypes.GLOW_LICHEN, StateTypes.SUNFLOWER, StateTypes.LILAC, StateTypes.ROSE_BUSH, StateTypes.PEONY, StateTypes.TALL_GRASS, StateTypes.LARGE_FERN, StateTypes.HANGING_ROOTS);
        // TODO:
        //      this.tag(BlockTags.FROG_PREFER_JUMP_TO).add(Blocks.LILY_PAD, Blocks.BIG_DRIPLEAF);
        //      this.tag(BlockTags.SCULK_REPLACEABLE).addTag(BlockTags.BASE_STONE_OVERWORLD).addTag(BlockTags.DIRT).addTag(BlockTags.TERRACOTTA).addTag(BlockTags.NYLIUM).addTag(BlockTags.BASE_STONE_NETHER).addTag(BlockTags.SAND).add(Blocks.GRAVEL).add(Blocks.SOUL_SAND).add(Blocks.SOUL_SOIL).add(Blocks.CALCITE).add(Blocks.SMOOTH_BASALT).add(Blocks.CLAY).add(Blocks.DRIPSTONE_BLOCK).add(Blocks.END_STONE).add(Blocks.RED_SANDSTONE).add(Blocks.SANDSTONE);
        //      this.tag(BlockTags.SCULK_REPLACEABLE_WORLD_GEN).addTag(BlockTags.SCULK_REPLACEABLE).add(Blocks.DEEPSLATE_BRICKS).add(Blocks.DEEPSLATE_TILES).add(Blocks.COBBLED_DEEPSLATE).add(Blocks.CRACKED_DEEPSLATE_BRICKS).add(Blocks.CRACKED_DEEPSLATE_TILES).add(Blocks.POLISHED_DEEPSLATE);
        //      this.tag(BlockTags.CONVERTABLE_TO_MUD).add(Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.ROOTED_DIRT);
        //      this.tag(BlockTags.ANCIENT_CITY_REPLACEABLE).add(Blocks.DEEPSLATE).add(Blocks.DEEPSLATE_BRICKS).add(Blocks.DEEPSLATE_TILES).add(Blocks.DEEPSLATE_BRICK_SLAB).add(Blocks.DEEPSLATE_TILE_SLAB).add(Blocks.DEEPSLATE_BRICK_STAIRS).add(Blocks.DEEPSLATE_TILE_WALL).add(Blocks.DEEPSLATE_BRICK_WALL).add(Blocks.COBBLED_DEEPSLATE).add(Blocks.CRACKED_DEEPSLATE_BRICKS).add(Blocks.CRACKED_DEEPSLATE_TILES).add(Blocks.GRAY_WOOL);
        //      this.tag(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH).add(Blocks.MUD, Blocks.MUDDY_MANGROVE_ROOTS, Blocks.MANGROVE_ROOTS, Blocks.MANGROVE_LEAVES, Blocks.MANGROVE_LOG, Blocks.MANGROVE_PROPAGULE, Blocks.MOSS_CARPET, Blocks.VINE);
        //      this.tag(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH).add(Blocks.MUD, Blocks.MUDDY_MANGROVE_ROOTS, Blocks.MANGROVE_ROOTS, Blocks.MOSS_CARPET, Blocks.VINE, Blocks.MANGROVE_PROPAGULE, Blocks.SNOW);
        //      this.tag(BlockTags.DEAD_BUSH_MAY_PLACE_ON).addTag(BlockTags.SAND).addTag(BlockTags.TERRACOTTA).addTag(BlockTags.DIRT);
        //      this.tag(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON).add(Blocks.ICE).add(Blocks.PACKED_ICE).add(Blocks.BARRIER);
        //      this.tag(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON).add(Blocks.HONEY_BLOCK).add(Blocks.SOUL_SAND).add(Blocks.MUD);
        //      this.tag(BlockTags.COMPLETES_FIND_TREE_TUTORIAL).addTag(BlockTags.LOGS).addTag(BlockTags.LEAVES).addTag(BlockTags.WART_BLOCKS);
        //      this.tag(BlockTags.SNAPS_GOAT_HORN).addTag(BlockTags.OVERWORLD_NATURAL_LOGS).add(Blocks.STONE).add(Blocks.PACKED_ICE).add(Blocks.IRON_ORE).add(Blocks.COAL_ORE).add(Blocks.COPPER_ORE).add(Blocks.EMERALD_ORE);
        // Unofficial blocks to help packetevents users
        BlockTags.GLASS_BLOCKS.add(StateTypes.GLASS, StateTypes.WHITE_STAINED_GLASS, StateTypes.ORANGE_STAINED_GLASS, StateTypes.MAGENTA_STAINED_GLASS, StateTypes.LIGHT_BLUE_STAINED_GLASS, StateTypes.YELLOW_STAINED_GLASS, StateTypes.LIME_STAINED_GLASS, StateTypes.PINK_STAINED_GLASS, StateTypes.GRAY_STAINED_GLASS, StateTypes.LIGHT_GRAY_STAINED_GLASS, StateTypes.CYAN_STAINED_GLASS, StateTypes.PURPLE_STAINED_GLASS, StateTypes.BLUE_STAINED_GLASS, StateTypes.BROWN_STAINED_GLASS, StateTypes.GREEN_STAINED_GLASS, StateTypes.RED_STAINED_GLASS, StateTypes.BLACK_STAINED_GLASS, StateTypes.TINTED_GLASS);
        BlockTags.GLASS_PANES.add(StateTypes.GLASS_PANE, StateTypes.WHITE_STAINED_GLASS_PANE, StateTypes.ORANGE_STAINED_GLASS_PANE, StateTypes.MAGENTA_STAINED_GLASS_PANE, StateTypes.LIGHT_BLUE_STAINED_GLASS_PANE, StateTypes.YELLOW_STAINED_GLASS_PANE, StateTypes.LIME_STAINED_GLASS_PANE, StateTypes.PINK_STAINED_GLASS_PANE, StateTypes.GRAY_STAINED_GLASS_PANE, StateTypes.LIGHT_GRAY_STAINED_GLASS_PANE, StateTypes.CYAN_STAINED_GLASS_PANE, StateTypes.PURPLE_STAINED_GLASS_PANE, StateTypes.BLUE_STAINED_GLASS_PANE, StateTypes.BROWN_STAINED_GLASS_PANE, StateTypes.GREEN_STAINED_GLASS_PANE, StateTypes.RED_STAINED_GLASS_PANE, StateTypes.BLACK_STAINED_GLASS_PANE);
    }

    String name;
    Set<StateType> states = new HashSet<>(); // o(1)

    public BlockTags(final String name) {
        byName.put(name, this);
        this.name = name;
    }

    private static BlockTags bind(final String s) {
        return new BlockTags(s);
    }

    private BlockTags add(StateType... state) {
        Collections.addAll(this.states, state);
        return this;
    }

    private BlockTags addTag(BlockTags tags) {
        this.states.addAll(tags.states);
        return this;
    }

    public boolean contains(StateType state) {
        return this.states.contains(state);
    }

    public String getName() {
        return this.name;
    }

    public BlockTags getByName(String name) {
        return byName.get(name);
    }

    public Set<StateType> getStates() {
        return this.states;
    }
}