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

package com.github.retrooper.packetevents.protocol.item.trimmaterial;

import com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterial;
import com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterials;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.MappingHelper;
import com.github.retrooper.packetevents.util.mappings.TypesBuilder;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import net.kyori.adventure.text.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.TextColor.color;

public class TrimMaterials {

    private static final Map<String, TrimMaterial> TRIM_MATERIAL_MAP = new HashMap<>();
    private static final Map<Byte, Map<Integer, TrimMaterial>> TRIM_MATERIAL_ID_MAP = new HashMap<>();
    private static final TypesBuilder TYPES_BUILDER = new TypesBuilder("item/item_trim_material_mappings");

    public static TrimMaterial define(String key, ItemType ingredient, float itemModelIndex, int color) {
        // darken own armor material - if present
        Map<ArmorMaterial, String> overrideArmorMaterials = new HashMap<>(2);
        String armorMaterialId = ResourceLocation.minecraft(key).toString();
        ArmorMaterial armorMaterial = ArmorMaterials.getByName(armorMaterialId);
        if (armorMaterial != null) {
            overrideArmorMaterials.put(armorMaterial, key + "_darker");
        }

        Component description = translatable("trim_material.minecraft." + key, color(color));
        return define(key, key, ingredient, itemModelIndex, overrideArmorMaterials, description);
    }

    public static TrimMaterial define(
            String key, String assetName, ItemType ingredient, float itemModelIndex,
            Map<ArmorMaterial, String> overrideArmorMaterials, Component description
    ) {
        TypesBuilderData data = TYPES_BUILDER.define(key);
        TrimMaterial material = new TrimMaterial() {
            @Override
            public String getAssetName() {
                return assetName;
            }

            @Override
            public ItemType getIngredient() {
                return ingredient;
            }

            @Override
            public float getItemModelIndex() {
                return itemModelIndex;
            }

            @Override
            public Map<ArmorMaterial, String> getOverrideArmorMaterials() {
                return overrideArmorMaterials;
            }

            @Override
            public Component getDescription() {
                return description;
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
                if (obj instanceof TrimMaterial) {
                    return this.getName().equals(((TrimMaterial) obj).getName());
                }
                return false;
            }
        };
        MappingHelper.registerMapping(TYPES_BUILDER, TRIM_MATERIAL_MAP, TRIM_MATERIAL_ID_MAP, material);
        return material;
    }

    // with key
    public static TrimMaterial getByName(String name) {
        return TRIM_MATERIAL_MAP.get(name);
    }

    public static TrimMaterial getById(ClientVersion version, int id) {
        int index = TYPES_BUILDER.getDataIndex(version);
        Map<Integer, TrimMaterial> idMap = TRIM_MATERIAL_ID_MAP.get((byte) index);
        return idMap.get(id);
    }

    // Added in 1.19.4
    public static final TrimMaterial AMETHYST = define("amethyst", ItemTypes.AMETHYST_SHARD, 1f, 0x9A5CC6);
    public static final TrimMaterial COPPER = define("copper", ItemTypes.COPPER_INGOT, 0.5f, 0xB4684D);
    public static final TrimMaterial DIAMOND = define("diamond", ItemTypes.DIAMOND, 0.8f, 0x6EECD2);
    public static final TrimMaterial EMERALD = define("emerald", ItemTypes.EMERALD, 0.7f, 0x11A036);
    public static final TrimMaterial GOLD = define("gold", ItemTypes.GOLD_INGOT, 0.6f, 0xDEB12D);
    public static final TrimMaterial IRON = define("iron", ItemTypes.IRON_INGOT, 0.2f, 0xECECEC);
    public static final TrimMaterial LAPIS = define("lapis", ItemTypes.LAPIS_LAZULI, 0.9f, 0x416E97);
    public static final TrimMaterial NETHERITE = define("netherite", ItemTypes.NETHERITE_INGOT, 0.3f, 0x625859);
    public static final TrimMaterial QUARTZ = define("quartz", ItemTypes.QUARTZ, 0.1f, 0xE3D4C4);
    public static final TrimMaterial REDSTONE = define("redstone", ItemTypes.REDSTONE, 0.4f, 0x971607);

    /**
     * Returns an immutable view of the trim materials.
     * @return Trim Materials
     */
    public static Collection<TrimMaterial> values() {
        return Collections.unmodifiableCollection(TRIM_MATERIAL_MAP.values());
    }

    static {
        TYPES_BUILDER.unloadFileMappings();
    }
}
