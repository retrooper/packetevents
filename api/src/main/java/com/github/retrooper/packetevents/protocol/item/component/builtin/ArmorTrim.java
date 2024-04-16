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

package com.github.retrooper.packetevents.protocol.item.component.builtin;

import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.mapper.GenericMappedEntity;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

import java.util.Map;

public class ArmorTrim {

    private final Material material;
    private final Pattern pattern;
    private final boolean showInTooltip;

    public ArmorTrim(Material material, Pattern pattern, boolean showInTooltip) {
        this.material = material;
        this.pattern = pattern;
        this.showInTooltip = showInTooltip;
    }

    public static ArmorTrim read(PacketWrapper<?> wrapper) {
        Material material = Material.read(wrapper);
        Pattern pattern = Pattern.read(wrapper);
        boolean showInTooltip = wrapper.readBoolean();
        return new ArmorTrim(material, pattern, showInTooltip);
    }

    public static void write(PacketWrapper<?> wrapper, ArmorTrim trim) {
        Material.write(wrapper, trim.material);
        Pattern.write(wrapper, trim.pattern);
        wrapper.writeBoolean(trim.showInTooltip);
    }

    // TODO: GenericMappedEntity -> minecraft:armor_material registry
    public static class Material {

        private final String assetName;
        private final ItemType ingredient;
        private final float itemModelIndex;
        private final Map<GenericMappedEntity, String> overrideArmorMaterials;
        private final Component description;

        public Material(
                String assetName, ItemType ingredient, float itemModelIndex,
                Map<GenericMappedEntity, String> overrideArmorMaterials, Component description
        ) {
            this.assetName = assetName;
            this.ingredient = ingredient;
            this.itemModelIndex = itemModelIndex;
            this.overrideArmorMaterials = overrideArmorMaterials;
            this.description = description;
        }

        public static Material read(PacketWrapper<?> wrapper) {
            String assetName = wrapper.readString();
            ItemType ingredient = wrapper.readMappedEntity(ItemTypes::getById);
            float itemModelIndex = wrapper.readFloat();
            Map<GenericMappedEntity, String> overrideArmorMaterials = wrapper.readMap(
                    GenericMappedEntity::read, PacketWrapper::readString);
            Component description = wrapper.readComponent();
            return new Material(assetName, ingredient, itemModelIndex, overrideArmorMaterials, description);
        }

        public static void write(PacketWrapper<?> wrapper, Material material) {
            wrapper.writeString(material.assetName);
            wrapper.writeMappedEntity(material.ingredient);
            wrapper.writeFloat(material.itemModelIndex);
            wrapper.writeMap(material.overrideArmorMaterials,
                    PacketWrapper::writeMappedEntity, PacketWrapper::writeString);
            wrapper.writeComponent(material.description);
        }
    }

    public static class Pattern {

        private final ResourceLocation assetId;
        private final ItemType templateItem;
        private final Component description;
        private final boolean decal;

        public Pattern(
                ResourceLocation assetId, ItemType templateItem,
                Component description, boolean decal
        ) {
            this.assetId = assetId;
            this.templateItem = templateItem;
            this.description = description;
            this.decal = decal;
        }

        public static Pattern read(PacketWrapper<?> wrapper) {
            ResourceLocation assetId = wrapper.readIdentifier();
            ItemType templateItem = wrapper.readMappedEntity(ItemTypes::getById);
            Component description = wrapper.readComponent();
            boolean decal = wrapper.readBoolean();
            return new Pattern(assetId, templateItem, description, decal);
        }

        public static void write(PacketWrapper<?> wrapper, Pattern pattern) {
            wrapper.writeIdentifier(pattern.assetId);
            wrapper.writeMappedEntity(pattern.templateItem);
            wrapper.writeComponent(pattern.description);
            wrapper.writeBoolean(pattern.decal);
        }
    }
}
