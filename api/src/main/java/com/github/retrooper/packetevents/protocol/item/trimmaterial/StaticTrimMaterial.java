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
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import net.kyori.adventure.text.Component;

import java.util.Map;
import java.util.Objects;

public class StaticTrimMaterial implements TrimMaterial {

    private final String assetName;
    private final ItemType ingredient;
    private final float itemModelIndex;
    private final Map<ArmorMaterial, String> overrideArmorMaterials;
    private final Component description;

    public StaticTrimMaterial(
            String assetName, ItemType ingredient, float itemModelIndex,
            Map<ArmorMaterial, String> overrideArmorMaterials, Component description
    ) {
        this.assetName = assetName;
        this.ingredient = ingredient;
        this.itemModelIndex = itemModelIndex;
        this.overrideArmorMaterials = overrideArmorMaterials;
        this.description = description;
    }

    @Override
    public ResourceLocation getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getId(ClientVersion version) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRegistered() {
        return false;
    }

    @Override
    public String getAssetName() {
        return this.assetName;
    }

    @Override
    public ItemType getIngredient() {
        return this.ingredient;
    }

    @Override
    public float getItemModelIndex() {
        return this.itemModelIndex;
    }

    @Override
    public Map<ArmorMaterial, String> getOverrideArmorMaterials() {
        return this.overrideArmorMaterials;
    }

    @Override
    public Component getDescription() {
        return this.description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticTrimMaterial)) return false;
        StaticTrimMaterial material = (StaticTrimMaterial) obj;
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
