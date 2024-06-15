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
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface TrimMaterial extends MappedEntity {

    String getAssetName();

    ItemType getIngredient();

    float getItemModelIndex();

    default @Nullable String getArmorMaterialOverride(ArmorMaterial armorMaterial) {
        return this.getOverrideArmorMaterials().get(armorMaterial);
    }

    Map<ArmorMaterial, String> getOverrideArmorMaterials();

    Component getDescription();

    static TrimMaterial read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntityOrDirect(TrimMaterials::getById, TrimMaterial::readDirect);
    }

    static TrimMaterial readDirect(PacketWrapper<?> wrapper) {
        String assetName = wrapper.readString();
        ItemType ingredient = wrapper.readMappedEntity(ItemTypes::getById);
        float itemModelIndex = wrapper.readFloat();
        Map<ArmorMaterial, String> overrideArmorMaterials = wrapper.readMap(
                ew -> ew.readMappedEntity(ArmorMaterials::getById),
                PacketWrapper::readString);
        Component description = wrapper.readComponent();
        return new StaticTrimMaterial(assetName, ingredient, itemModelIndex, overrideArmorMaterials, description);
    }

    static void write(PacketWrapper<?> wrapper, TrimMaterial material) {
        wrapper.writeMappedEntityOrDirect(material, TrimMaterial::writeDirect);
    }

    static void writeDirect(PacketWrapper<?> wrapper, TrimMaterial material) {
        wrapper.writeString(material.getAssetName());
        wrapper.writeMappedEntity(material.getIngredient());
        wrapper.writeFloat(material.getItemModelIndex());
        wrapper.writeMap(material.getOverrideArmorMaterials(),
                PacketWrapper::writeMappedEntity, PacketWrapper::writeString);
        wrapper.writeComponent(material.getDescription());
    }
}
