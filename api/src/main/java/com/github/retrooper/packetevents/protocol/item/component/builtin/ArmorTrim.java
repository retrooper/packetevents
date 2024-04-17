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

import com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterial;
import com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterials;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class ArmorTrim {

    private Material material;
    private Pattern pattern;
    private boolean showInTooltip;

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

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public boolean isShowInTooltip() {
        return this.showInTooltip;
    }

    public void setShowInTooltip(boolean showInTooltip) {
        this.showInTooltip = showInTooltip;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ArmorTrim)) return false;
        ArmorTrim armorTrim = (ArmorTrim) obj;
        if (this.showInTooltip != armorTrim.showInTooltip) return false;
        if (!this.material.equals(armorTrim.material)) return false;
        return this.pattern.equals(armorTrim.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.material, this.pattern, this.showInTooltip);
    }

    public static class Material {

        private String assetName;
        private ItemType ingredient;
        private float itemModelIndex;
        private Map<ArmorMaterial, String> overrideArmorMaterials;
        private Component description;

        public Material(
                String assetName, ItemType ingredient, float itemModelIndex,
                Map<ArmorMaterial, String> overrideArmorMaterials, Component description
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
            Map<ArmorMaterial, String> overrideArmorMaterials = wrapper.readMap(
                    ew -> ew.readMappedEntity(ArmorMaterials::getById),
                    PacketWrapper::readString);
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

        public String getAssetName() {
            return this.assetName;
        }

        public void setAssetName(String assetName) {
            this.assetName = assetName;
        }

        public ItemType getIngredient() {
            return this.ingredient;
        }

        public void setIngredient(ItemType ingredient) {
            this.ingredient = ingredient;
        }

        public float getItemModelIndex() {
            return this.itemModelIndex;
        }

        public void setItemModelIndex(float itemModelIndex) {
            this.itemModelIndex = itemModelIndex;
        }

        public @Nullable String getArmorMaterialOverride(ArmorMaterial armorMaterial) {
            return this.overrideArmorMaterials.get(armorMaterial);
        }

        public void setArmorMaterialOverride(ArmorMaterial armorMaterial, @Nullable String override) {
            if (override == null) {
                this.overrideArmorMaterials.remove(armorMaterial);
            } else {
                this.overrideArmorMaterials.put(armorMaterial, override);
            }
        }

        public Map<ArmorMaterial, String> getOverrideArmorMaterials() {
            return this.overrideArmorMaterials;
        }

        public void setOverrideArmorMaterials(Map<ArmorMaterial, String> overrideArmorMaterials) {
            this.overrideArmorMaterials = overrideArmorMaterials;
        }

        public Component getDescription() {
            return this.description;
        }

        public void setDescription(Component description) {
            this.description = description;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Material)) return false;
            Material material = (Material) obj;
            if (Float.compare(material.itemModelIndex, this.itemModelIndex) != 0) return false;
            if (!this.assetName.equals(material.assetName)) return false;
            if (!this.ingredient.equals(material.ingredient)) return false;
            if (!this.overrideArmorMaterials.equals(material.overrideArmorMaterials)) return false;
            return this.description.equals(material.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.assetName, this.ingredient, this.itemModelIndex, this.overrideArmorMaterials, this.description);
        }
    }

    public static class Pattern {

        private ResourceLocation assetId;
        private ItemType templateItem;
        private Component description;
        private boolean decal;

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

        public ResourceLocation getAssetId() {
            return this.assetId;
        }

        public void setAssetId(ResourceLocation assetId) {
            this.assetId = assetId;
        }

        public ItemType getTemplateItem() {
            return this.templateItem;
        }

        public void setTemplateItem(ItemType templateItem) {
            this.templateItem = templateItem;
        }

        public Component getDescription() {
            return this.description;
        }

        public void setDescription(Component description) {
            this.description = description;
        }

        public boolean isDecal() {
            return this.decal;
        }

        public void setDecal(boolean decal) {
            this.decal = decal;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Pattern)) return false;
            Pattern pattern = (Pattern) obj;
            if (this.decal != pattern.decal) return false;
            if (!this.assetId.equals(pattern.assetId)) return false;
            if (!this.templateItem.equals(pattern.templateItem)) return false;
            return this.description.equals(pattern.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.assetId, this.templateItem, this.description, this.decal);
        }
    }
}
