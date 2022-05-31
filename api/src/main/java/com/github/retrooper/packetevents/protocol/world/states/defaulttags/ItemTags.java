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

import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ItemTags {
    private static final HashMap<String, ItemTags> byName = new HashMap<>();

    public static final ItemTags WOOL = bind("wool");
    public static final ItemTags PLANKS = bind("planks");
    public static final ItemTags STONE_BRICKS = bind("stone_bricks");
    public static final ItemTags WOODEN_BUTTONS = bind("wooden_buttons");
    public static final ItemTags BUTTONS = bind("buttons");
    public static final ItemTags CARPETS = bind("carpets");
    public static final ItemTags WOODEN_DOORS = bind("wooden_doors");
    public static final ItemTags WOODEN_STAIRS = bind("wooden_stairs");
    public static final ItemTags WOODEN_SLABS = bind("wooden_slabs");
    public static final ItemTags WOODEN_FENCES = bind("wooden_fences");
    public static final ItemTags WOODEN_PRESSURE_PLATES = bind("wooden_pressure_plates");
    public static final ItemTags WOODEN_TRAPDOORS = bind("wooden_trapdoors");
    public static final ItemTags DOORS = bind("doors");
    public static final ItemTags SAPLINGS = bind("saplings");
    public static final ItemTags LOGS_THAT_BURN = bind("logs_that_burn");
    public static final ItemTags LOGS = bind("logs");
    public static final ItemTags DARK_OAK_LOGS = bind("dark_oak_logs");
    public static final ItemTags OAK_LOGS = bind("oak_logs");
    public static final ItemTags BIRCH_LOGS = bind("birch_logs");
    public static final ItemTags ACACIA_LOGS = bind("acacia_logs");
    public static final ItemTags JUNGLE_LOGS = bind("jungle_logs");
    public static final ItemTags SPRUCE_LOGS = bind("spruce_logs");
    public static final ItemTags CRIMSON_STEMS = bind("crimson_stems");
    public static final ItemTags WARPED_STEMS = bind("warped_stems");
    public static final ItemTags BANNERS = bind("banners");
    public static final ItemTags SAND = bind("sand");
    public static final ItemTags STAIRS = bind("stairs");
    public static final ItemTags SLABS = bind("slabs");
    public static final ItemTags WALLS = bind("walls");
    public static final ItemTags ANVIL = bind("anvil");
    public static final ItemTags RAILS = bind("rails");
    public static final ItemTags LEAVES = bind("leaves");
    public static final ItemTags TRAPDOORS = bind("trapdoors");
    public static final ItemTags SMALL_FLOWERS = bind("small_flowers");
    public static final ItemTags BEDS = bind("beds");
    public static final ItemTags FENCES = bind("fences");
    public static final ItemTags TALL_FLOWERS = bind("tall_flowers");
    public static final ItemTags FLOWERS = bind("flowers");
    public static final ItemTags PIGLIN_REPELLENTS = bind("piglin_repellents");
    public static final ItemTags PIGLIN_LOVED = bind("piglin_loved");
    public static final ItemTags IGNORED_BY_PIGLIN_BABIES = bind("ignored_by_piglin_babies");
    public static final ItemTags PIGLIN_FOOD = bind("piglin_food");
    public static final ItemTags FOX_FOOD = bind("fox_food");
    public static final ItemTags GOLD_ORES = bind("gold_ores");
    public static final ItemTags IRON_ORES = bind("iron_ores");
    public static final ItemTags DIAMOND_ORES = bind("diamond_ores");
    public static final ItemTags REDSTONE_ORES = bind("redstone_ores");
    public static final ItemTags LAPIS_ORES = bind("lapis_ores");
    public static final ItemTags COAL_ORES = bind("coal_ores");
    public static final ItemTags EMERALD_ORES = bind("emerald_ores");
    public static final ItemTags COPPER_ORES = bind("copper_ores");
    public static final ItemTags NON_FLAMMABLE_WOOD = bind("non_flammable_wood");
    public static final ItemTags SOUL_FIRE_BASE_BLOCKS = bind("soul_fire_base_blocks");
    public static final ItemTags CANDLES = bind("candles");
    public static final ItemTags BOATS = bind("boats");
    public static final ItemTags FISHES = bind("fishes");
    public static final ItemTags SIGNS = bind("signs");
    public static final ItemTags MUSIC_DISCS = bind("music_discs");
    public static final ItemTags CREEPER_DROP_MUSIC_DISCS = bind("creeper_drop_music_discs");
    public static final ItemTags COALS = bind("coals");
    public static final ItemTags ARROWS = bind("arrows");
    public static final ItemTags LECTERN_BOOKS = bind("lectern_books");
    public static final ItemTags BEACON_PAYMENT_ITEMS = bind("beacon_payment_items");
    public static final ItemTags STONE_TOOL_MATERIALS = bind("stone_tool_materials");
    public static final ItemTags STONE_CRAFTING_MATERIALS = bind("stone_crafting_materials");
    public static final ItemTags FREEZE_IMMUNE_WEARABLES = bind("freeze_immune_wearables");
    public static final ItemTags AXOLOTL_TEMPT_ITEMS = bind("axolotl_tempt_items");
    public static final ItemTags OCCLUDES_VIBRATION_SIGNALS = bind("occludes_vibration_signals");
    public static final ItemTags CLUSTER_MAX_HARVESTABLES = bind("cluster_max_harvestables");

    static {
        copy(BlockTags.WOOL, ItemTags.WOOL);
        copy(BlockTags.PLANKS, ItemTags.PLANKS);
        copy(BlockTags.STONE_BRICKS, ItemTags.STONE_BRICKS);
        copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
        copy(BlockTags.BUTTONS, ItemTags.BUTTONS);
        copy(BlockTags.CARPETS, ItemTags.CARPETS);
        copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
        copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
        copy(BlockTags.DOORS, ItemTags.DOORS);
        copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
        copy(BlockTags.OAK_LOGS, ItemTags.OAK_LOGS);
        copy(BlockTags.DARK_OAK_LOGS, ItemTags.DARK_OAK_LOGS);
        copy(BlockTags.BIRCH_LOGS, ItemTags.BIRCH_LOGS);
        copy(BlockTags.ACACIA_LOGS, ItemTags.ACACIA_LOGS);
        copy(BlockTags.SPRUCE_LOGS, ItemTags.SPRUCE_LOGS);
        copy(BlockTags.JUNGLE_LOGS, ItemTags.JUNGLE_LOGS);
        copy(BlockTags.CRIMSON_STEMS, ItemTags.CRIMSON_STEMS);
        copy(BlockTags.WARPED_STEMS, ItemTags.WARPED_STEMS);
        copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
        copy(BlockTags.LOGS, ItemTags.LOGS);
        copy(BlockTags.SAND, ItemTags.SAND);
        copy(BlockTags.SLABS, ItemTags.SLABS);
        copy(BlockTags.WALLS, ItemTags.WALLS);
        copy(BlockTags.STAIRS, ItemTags.STAIRS);
        copy(BlockTags.ANVIL, ItemTags.ANVIL);
        copy(BlockTags.RAILS, ItemTags.RAILS);
        copy(BlockTags.LEAVES, ItemTags.LEAVES);
        copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
        copy(BlockTags.TRAPDOORS, ItemTags.TRAPDOORS);
        copy(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS);
        copy(BlockTags.BEDS, ItemTags.BEDS);
        copy(BlockTags.FENCES, ItemTags.FENCES);
        copy(BlockTags.TALL_FLOWERS, ItemTags.TALL_FLOWERS);
        copy(BlockTags.FLOWERS, ItemTags.FLOWERS);
        copy(BlockTags.SOUL_FIRE_BASE_BLOCKS, ItemTags.SOUL_FIRE_BASE_BLOCKS);
        copy(BlockTags.CANDLES, ItemTags.CANDLES);
        copy(BlockTags.OCCLUDES_VIBRATION_SIGNALS, ItemTags.OCCLUDES_VIBRATION_SIGNALS);
        copy(BlockTags.GOLD_ORES, ItemTags.GOLD_ORES);
        copy(BlockTags.IRON_ORES, ItemTags.IRON_ORES);
        copy(BlockTags.DIAMOND_ORES, ItemTags.DIAMOND_ORES);
        copy(BlockTags.REDSTONE_ORES, ItemTags.REDSTONE_ORES);
        copy(BlockTags.LAPIS_ORES, ItemTags.LAPIS_ORES);
        copy(BlockTags.COAL_ORES, ItemTags.COAL_ORES);
        copy(BlockTags.EMERALD_ORES, ItemTags.EMERALD_ORES);
        copy(BlockTags.COPPER_ORES, ItemTags.COPPER_ORES);
        ItemTags.BANNERS.add(ItemTypes.WHITE_BANNER, ItemTypes.ORANGE_BANNER, ItemTypes.MAGENTA_BANNER, ItemTypes.LIGHT_BLUE_BANNER, ItemTypes.YELLOW_BANNER, ItemTypes.LIME_BANNER, ItemTypes.PINK_BANNER, ItemTypes.GRAY_BANNER, ItemTypes.LIGHT_GRAY_BANNER, ItemTypes.CYAN_BANNER, ItemTypes.PURPLE_BANNER, ItemTypes.BLUE_BANNER, ItemTypes.BROWN_BANNER, ItemTypes.GREEN_BANNER, ItemTypes.RED_BANNER, ItemTypes.BLACK_BANNER);
        ItemTags.BOATS.add(ItemTypes.OAK_BOAT, ItemTypes.SPRUCE_BOAT, ItemTypes.BIRCH_BOAT, ItemTypes.JUNGLE_BOAT, ItemTypes.ACACIA_BOAT, ItemTypes.DARK_OAK_BOAT);
        ItemTags.FISHES.add(ItemTypes.COD, ItemTypes.COOKED_COD, ItemTypes.SALMON, ItemTypes.COOKED_SALMON, ItemTypes.PUFFERFISH, ItemTypes.TROPICAL_FISH);
        copy(BlockTags.STANDING_SIGNS, ItemTags.SIGNS);
        ItemTags.CREEPER_DROP_MUSIC_DISCS.add(ItemTypes.MUSIC_DISC_13, ItemTypes.MUSIC_DISC_CAT, ItemTypes.MUSIC_DISC_BLOCKS, ItemTypes.MUSIC_DISC_CHIRP, ItemTypes.MUSIC_DISC_FAR, ItemTypes.MUSIC_DISC_MALL, ItemTypes.MUSIC_DISC_MELLOHI, ItemTypes.MUSIC_DISC_STAL, ItemTypes.MUSIC_DISC_STRAD, ItemTypes.MUSIC_DISC_WARD, ItemTypes.MUSIC_DISC_11, ItemTypes.MUSIC_DISC_WAIT);
        ItemTags.MUSIC_DISCS.addTag(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(ItemTypes.MUSIC_DISC_PIGSTEP);
        ItemTags.COALS.add(ItemTypes.COAL, ItemTypes.CHARCOAL);
        ItemTags.ARROWS.add(ItemTypes.ARROW, ItemTypes.TIPPED_ARROW, ItemTypes.SPECTRAL_ARROW);
        ItemTags.LECTERN_BOOKS.add(ItemTypes.WRITTEN_BOOK, ItemTypes.WRITABLE_BOOK);
        ItemTags.BEACON_PAYMENT_ITEMS.add(ItemTypes.NETHERITE_INGOT, ItemTypes.EMERALD, ItemTypes.DIAMOND, ItemTypes.GOLD_INGOT, ItemTypes.IRON_INGOT);
        ItemTags.PIGLIN_REPELLENTS.add(ItemTypes.SOUL_TORCH).add(ItemTypes.SOUL_LANTERN).add(ItemTypes.SOUL_CAMPFIRE);
        ItemTags.PIGLIN_LOVED.addTag(ItemTags.GOLD_ORES).add(ItemTypes.GOLD_BLOCK, ItemTypes.GILDED_BLACKSTONE, ItemTypes.LIGHT_WEIGHTED_PRESSURE_PLATE, ItemTypes.GOLD_INGOT, ItemTypes.BELL, ItemTypes.CLOCK, ItemTypes.GOLDEN_CARROT, ItemTypes.GLISTERING_MELON_SLICE, ItemTypes.GOLDEN_APPLE, ItemTypes.ENCHANTED_GOLDEN_APPLE, ItemTypes.GOLDEN_HELMET, ItemTypes.GOLDEN_CHESTPLATE, ItemTypes.GOLDEN_LEGGINGS, ItemTypes.GOLDEN_BOOTS, ItemTypes.GOLDEN_HORSE_ARMOR, ItemTypes.GOLDEN_SWORD, ItemTypes.GOLDEN_PICKAXE, ItemTypes.GOLDEN_SHOVEL, ItemTypes.GOLDEN_AXE, ItemTypes.GOLDEN_HOE, ItemTypes.RAW_GOLD, ItemTypes.RAW_GOLD_BLOCK);
        ItemTags.IGNORED_BY_PIGLIN_BABIES.add(ItemTypes.LEATHER);
        ItemTags.PIGLIN_FOOD.add(ItemTypes.PORKCHOP, ItemTypes.COOKED_PORKCHOP);
        ItemTags.FOX_FOOD.add(ItemTypes.SWEET_BERRIES, ItemTypes.GLOW_BERRIES);
        ItemTags.NON_FLAMMABLE_WOOD.add(ItemTypes.WARPED_STEM, ItemTypes.STRIPPED_WARPED_STEM, ItemTypes.WARPED_HYPHAE, ItemTypes.STRIPPED_WARPED_HYPHAE, ItemTypes.CRIMSON_STEM, ItemTypes.STRIPPED_CRIMSON_STEM, ItemTypes.CRIMSON_HYPHAE, ItemTypes.STRIPPED_CRIMSON_HYPHAE, ItemTypes.CRIMSON_PLANKS, ItemTypes.WARPED_PLANKS, ItemTypes.CRIMSON_SLAB, ItemTypes.WARPED_SLAB, ItemTypes.CRIMSON_PRESSURE_PLATE, ItemTypes.WARPED_PRESSURE_PLATE, ItemTypes.CRIMSON_FENCE, ItemTypes.WARPED_FENCE, ItemTypes.CRIMSON_TRAPDOOR, ItemTypes.WARPED_TRAPDOOR, ItemTypes.CRIMSON_FENCE_GATE, ItemTypes.WARPED_FENCE_GATE, ItemTypes.CRIMSON_STAIRS, ItemTypes.WARPED_STAIRS, ItemTypes.CRIMSON_BUTTON, ItemTypes.WARPED_BUTTON, ItemTypes.CRIMSON_DOOR, ItemTypes.WARPED_DOOR, ItemTypes.CRIMSON_SIGN, ItemTypes.WARPED_SIGN);
        ItemTags.STONE_TOOL_MATERIALS.add(ItemTypes.COBBLESTONE, ItemTypes.BLACKSTONE, ItemTypes.COBBLED_DEEPSLATE);
        ItemTags.STONE_CRAFTING_MATERIALS.add(ItemTypes.COBBLESTONE, ItemTypes.BLACKSTONE, ItemTypes.COBBLED_DEEPSLATE);
        ItemTags.FREEZE_IMMUNE_WEARABLES.add(ItemTypes.LEATHER_BOOTS, ItemTypes.LEATHER_LEGGINGS, ItemTypes.LEATHER_CHESTPLATE, ItemTypes.LEATHER_HELMET, ItemTypes.LEATHER_HORSE_ARMOR);
        ItemTags.AXOLOTL_TEMPT_ITEMS.add(ItemTypes.TROPICAL_FISH_BUCKET);
        ItemTags.CLUSTER_MAX_HARVESTABLES.add(ItemTypes.DIAMOND_PICKAXE, ItemTypes.GOLDEN_PICKAXE, ItemTypes.IRON_PICKAXE, ItemTypes.NETHERITE_PICKAXE, ItemTypes.STONE_PICKAXE, ItemTypes.WOODEN_PICKAXE);
    }

    String name;
    Set<ItemType> states = new HashSet<>(); // o(1);

    public ItemTags(final String name) {
        byName.put(name, this);
        this.name = name;
    }

    private static ItemTags bind(final String s) {
        return new ItemTags(s);
    }

    private static void copy(BlockTags tag, ItemTags itemTag) {
        for (StateType state : tag.getStates()) {
            itemTag.states.add(ItemTypes.getTypePlacingState(state));
        }
        itemTag.states.remove(null); // In case getTypePlacingState returned null
    }

    private ItemTags add(ItemType... state) {
        Collections.addAll(this.states, state);
        return this;
    }

    private ItemTags addTag(ItemTags tags) {
        this.states.addAll(tags.states);
        return this;
    }

    public boolean contains(ItemType state) {
        return this.states.contains(state);
    }

    public String getName() {
        return this.name;
    }

    public ItemTags getByName(String name) {
        return byName.get(name);
    }

    public Set<ItemType> getStates() {
        return this.states;
    }
}
