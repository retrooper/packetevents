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
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface TrimMaterial extends MappedEntity, CopyableEntity<TrimMaterial> {

    String getAssetName();

    ItemType getIngredient();

    float getItemModelIndex();

    default @Nullable String getArmorMaterialOverride(ArmorMaterial armorMaterial) {
        return this.getOverrideArmorMaterials().get(armorMaterial);
    }

    Map<ArmorMaterial, String> getOverrideArmorMaterials();

    Component getDescription();

    static TrimMaterial read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntityOrDirect(TrimMaterials.getRegistry(), TrimMaterial::readDirect);
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

    static TrimMaterial decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        String assetName = compound.getStringTagValueOrThrow("asset_name");
        ItemType ingredient = ItemTypes.getByName(compound.getStringTagValueOrThrow("ingredient"));
        float itemModelIndex = compound.getNumberTagOrThrow("item_model_index").getAsFloat();
        NBTCompound overrideArmorMaterialsTag = compound.getCompoundTagOrNull("override_armor_materials");
        Map<ArmorMaterial, String> overrideArmorMaterials;
        if (overrideArmorMaterialsTag != null) {
            overrideArmorMaterials = new HashMap<>();
            for (Map.Entry<String, NBT> entry : overrideArmorMaterialsTag.getTags().entrySet()) {
                ArmorMaterial material = ArmorMaterials.getByName(entry.getKey());
                String override = ((NBTString) entry.getValue()).getValue();
                overrideArmorMaterials.put(material, override);
            }
        } else {
            overrideArmorMaterials = Collections.emptyMap();
        }
        Component description = AdventureSerializer.fromNbt(((NBTCompound) nbt).getTagOrThrow("description"));
        return new StaticTrimMaterial(data, assetName, ingredient, itemModelIndex, overrideArmorMaterials, description);
    }

    static NBT encode(TrimMaterial material, ClientVersion version) {
        NBTCompound overrideArmorMaterialsTag;
        if (!material.getOverrideArmorMaterials().isEmpty()) {
            overrideArmorMaterialsTag = new NBTCompound();
            for (Map.Entry<ArmorMaterial, String> entry : material.getOverrideArmorMaterials().entrySet()) {
                String materialName = entry.getKey().getName().toString();
                NBTString overrideTag = new NBTString(entry.getValue());
                overrideArmorMaterialsTag.setTag(materialName, overrideTag);
            }
        } else {
            overrideArmorMaterialsTag = null;
        }

        NBTCompound compound = new NBTCompound();
        compound.setTag("asset_name", new NBTString(material.getAssetName()));
        compound.setTag("ingredient", new NBTString(material.getIngredient().getName().toString()));
        compound.setTag("item_model_index", new NBTFloat(material.getItemModelIndex()));
        if (overrideArmorMaterialsTag != null) {
            compound.setTag("override_armor_materials", overrideArmorMaterialsTag);
        }
        compound.setTag("description", AdventureSerializer.toNbt(material.getDescription()));
        return compound;
    }
}
