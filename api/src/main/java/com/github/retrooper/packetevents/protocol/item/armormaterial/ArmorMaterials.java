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

package com.github.retrooper.packetevents.protocol.item.armormaterial;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * This is named "equipment asset" in vanilla since 1.21.4,
 * before this was also called "armor material" in vanilla.
 */
public final class ArmorMaterials {

    // this isn't great, but it works
    private static final Map<String, String> DFU = new HashMap<>();

    static {
        DFU.put("turtle", "minecraft:turtle_scute");
        DFU.put("minecraft:turtle", "minecraft:turtle_scute");
        DFU.put("armadillo", "minecraft:armadillo_scute");
        DFU.put("minecraft:armadillo", "minecraft:armadillo_scute");
    }

    private static final VersionedRegistry<ArmorMaterial> REGISTRY = new VersionedRegistry<>("equipment_asset");

    private ArmorMaterials() {
    }

    private static ArmorMaterial define(String name) {
        return REGISTRY.define(name, StaticArmorMaterial::new);
    }

    public static VersionedRegistry<ArmorMaterial> getRegistry() {
        return REGISTRY;
    }

    public static ArmorMaterial getByName(String name) {
        return REGISTRY.getByName(DFU.getOrDefault(name, name));
    }

    public static ArmorMaterial getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static final ArmorMaterial LEATHER = define("leather");

    public static final ArmorMaterial CHAINMAIL = define("chainmail");
    public static final ArmorMaterial IRON = define("iron");
    public static final ArmorMaterial GOLD = define("gold");
    public static final ArmorMaterial DIAMOND = define("diamond");

    /**
     * Renamed from {@link #TURTLE} in 1.21.4
     */
    public static final ArmorMaterial TURTLE_SCUTE = define("turtle_scute");
    /**
     * Added with 1.13, renamed to {@link #TURTLE_SCUTE} in 1.21.4
     */
    @Deprecated
    public static final ArmorMaterial TURTLE = TURTLE_SCUTE;

    /**
     * @versions 1.16+
     */
    public static final ArmorMaterial NETHERITE = define("netherite");

    /**
     * Renamed from {@link #ARMADILLO} in 1.21.4
     */
    public static final ArmorMaterial ARMADILLO_SCUTE = define("armadillo_scute");
    /**
     * Added with 1.20.5, renamed to {@link #ARMADILLO_SCUTE} in 1.21.4
     */
    @Deprecated
    public static final ArmorMaterial ARMADILLO = ARMADILLO_SCUTE;

    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial ELYTRA = define("elytra");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial WHITE_CARPET = define("white_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial ORANGE_CARPET = define("orange_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial MAGENTA_CARPET = define("magenta_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial LIGHT_BLUE_CARPET = define("light_blue_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial YELLOW_CARPET = define("yellow_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial LIME_CARPET = define("lime_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial PINK_CARPET = define("pink_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial GRAY_CARPET = define("gray_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial LIGHT_GRAY_CARPET = define("light_gray_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial CYAN_CARPET = define("cyan_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial PURPLE_CARPET = define("purple_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial BLUE_CARPET = define("blue_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial BROWN_CARPET = define("brown_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial GREEN_CARPET = define("green_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial RED_CARPET = define("red_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial BLACK_CARPET = define("black_carpet");
    /**
     * @versions 1.21.4+
     */
    public static final ArmorMaterial TRADER_LLAMA = define("trader_llama");

    /**
     * @versions 1.21.5+
     */
    public static final ArmorMaterial SADDLE = define("saddle");

    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial WHITE_HARNESS = define("white_harness");
    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial ORANGE_HARNESS = define("orange_harness");
    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial MAGENTA_HARNESS = define("magenta_harness");
    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial LIGHT_BLUE_HARNESS = define("light_blue_harness");
    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial YELLOW_HARNESS = define("yellow_harness");
    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial LIME_HARNESS = define("lime_harness");
    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial PINK_HARNESS = define("pink_harness");
    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial GRAY_HARNESS = define("gray_harness");
    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial LIGHT_GRAY_HARNESS = define("light_gray_harness");
    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial CYAN_HARNESS = define("cyan_harness");
    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial PURPLE_HARNESS = define("purple_harness");
    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial BLUE_HARNESS = define("blue_harness");
    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial BROWN_HARNESS = define("brown_harness");
    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial GREEN_HARNESS = define("green_harness");
    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial RED_HARNESS = define("red_harness");
    /**
     * @versions 1.21.6+
     */
    public static final ArmorMaterial BLACK_HARNESS = define("black_harness");

    /**
     * @versions 1.21.9+
     */
    public static final ArmorMaterial COPPER = define("copper");

    /**
     * @versions 26.1+
     */
    public static final ArmorMaterial TRADER_LLAMA_BABY = define("trader_llama_baby");

    static {
        REGISTRY.unloadMappings();
    }
}
