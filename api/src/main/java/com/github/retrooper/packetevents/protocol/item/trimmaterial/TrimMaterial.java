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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterial;
import com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterials;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @versions 1.19.4+
 */
@NullMarked
public interface TrimMaterial extends MappedEntity, CopyableEntity<TrimMaterial>, DeepComparableEntity {

    NbtCodec<TrimMaterial> DIRECT_CODEC = new NbtMapCodec<TrimMaterial>() {
        @Override
        public TrimMaterial decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            boolean v263 = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_3);
            String assetId;
            ResourceLocation paletteId;
            if (v263) {
                paletteId = tag.getOrThrow("palette_id", ResourceLocation.CODEC, wrapper);
                assetId = paletteId.getKey();
                if (assetId.startsWith("trim/")) {
                    assetId = assetId.substring("trim/".length());
                }
            } else {
                assetId = tag.getStringTagValueOrThrow("asset_name");
                paletteId = ResourceLocation.minecraft("trim/" + assetId);
            }

            ItemType ingredient = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)
                    ? null : ItemTypes.getByName(tag.getStringTagValueOrThrow("ingredient"));
            float itemModelIndex = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4)
                    ? FALLBACK_ITEM_MODEL_INDEX : tag.getNumberTagOrThrow("item_model_index").getAsFloat();
            Map<ArmorMaterial, String> overrideArmorMaterials = Collections.emptyMap();
            if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_3)) {
                NBTCompound overrideArmorMaterialsTag = tag.getCompoundTagOrNull("override_armor_materials");
                if (overrideArmorMaterialsTag != null) {
                    overrideArmorMaterials = new HashMap<>();
                    for (Map.Entry<String, NBT> entry : overrideArmorMaterialsTag.getTags().entrySet()) {
                        ArmorMaterial material = ArmorMaterials.getByName(entry.getKey());
                        String override = ((NBTString) entry.getValue()).getValue();
                        overrideArmorMaterials.put(material, override);
                    }
                }
            }
            Component description = tag.getOrThrow("description", wrapper.getSerializers(), wrapper);
            return new StaticTrimMaterial(null, assetId, paletteId, ingredient, itemModelIndex,
                    overrideArmorMaterials, description);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, TrimMaterial value) throws NbtCodecException {
            NBTCompound overrideArmorMaterialsTag;
            if (!value.getOverrideArmorMaterials().isEmpty()) {
                overrideArmorMaterialsTag = new NBTCompound();
                for (Map.Entry<ArmorMaterial, String> entry : value.getOverrideArmorMaterials().entrySet()) {
                    String materialName = entry.getKey().getName().toString();
                    NBTString overrideTag = new NBTString(entry.getValue());
                    overrideArmorMaterialsTag.setTag(materialName, overrideTag);
                }
            } else {
                overrideArmorMaterialsTag = null;
            }

            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_3)) {
                tag.set("palette_id", value.getPaletteId(), ResourceLocation.CODEC, wrapper);
            } else {
                tag.setTag("asset_name", new NBTString(value.getAssetName()));
            }
            if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
                tag.setTag("ingredient", new NBTString(value.getIngredient().getName().toString()));
            }
            if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_4)) {
                tag.setTag("item_model_index", new NBTFloat(value.getItemModelIndex()));
            }
            if (overrideArmorMaterialsTag != null && wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_3)) {
                tag.setTag("override_armor_materials", overrideArmorMaterialsTag);
            }
            tag.set("description", value.getDescription(), wrapper.getSerializers(), wrapper);
        }
    }.codec();

    float FALLBACK_ITEM_MODEL_INDEX = 0f;

    /**
     * @versions -26.2
     */
    @ApiStatus.Obsolete
    String getAssetName();

    /**
     * @versions 26.3+
     */
    ResourceLocation getPaletteId();

    /**
     * Removed in 1.21.5
     */
    @ApiStatus.Obsolete
    ItemType getIngredient();

    /**
     * Removed in 1.21.4
     */
    @ApiStatus.Obsolete
    float getItemModelIndex();

    /**
     * @versions -26.2
     */
    @ApiStatus.Obsolete
    default @Nullable String getArmorMaterialOverride(ArmorMaterial armorMaterial) {
        return this.getOverrideArmorMaterials().get(armorMaterial);
    }

    /**
     * @versions -26.2
     */
    @ApiStatus.Obsolete
    Map<ArmorMaterial, String> getOverrideArmorMaterials();

    Component getDescription();

    static TrimMaterial read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntityOrDirect(TrimMaterials.getRegistry(), TrimMaterial::readDirect);
    }

    static TrimMaterial readDirect(PacketWrapper<?> wrapper) {
        boolean v263 = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_3);
        String assetId;
        ResourceLocation paletteId;
        if (v263) {
            paletteId = ResourceLocation.read(wrapper);
            assetId = paletteId.getKey();
            if (assetId.startsWith("trim/")) {
                assetId = assetId.substring("trim/".length());
            }
        } else {
            assetId = wrapper.readString();
            paletteId = ResourceLocation.minecraft("trim/" + assetId);
        }

        ItemType ingredient = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)
                ? null : wrapper.readMappedEntity(ItemTypes::getById);
        float itemModelIndex = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4)
                ? FALLBACK_ITEM_MODEL_INDEX : wrapper.readFloat();
        Map<ArmorMaterial, String> overrideArmorMaterials = Collections.emptyMap();
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_3)) {
            overrideArmorMaterials = wrapper.readMap(
                    ew -> ew.readMappedEntity(ArmorMaterials::getById),
                    PacketWrapper::readString);
        }
        Component description = wrapper.readComponent();
        return new StaticTrimMaterial(null, assetId, paletteId,
                ingredient, itemModelIndex, overrideArmorMaterials, description);
    }

    static void write(PacketWrapper<?> wrapper, TrimMaterial material) {
        wrapper.writeMappedEntityOrDirect(material, TrimMaterial::writeDirect);
    }

    static void writeDirect(PacketWrapper<?> wrapper, TrimMaterial material) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_3)) {
            ResourceLocation.write(wrapper, material.getPaletteId());
        } else {
            wrapper.writeString(material.getAssetName());
        }
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
            wrapper.writeMappedEntity(material.getIngredient());
        }
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_4)) {
            wrapper.writeFloat(material.getItemModelIndex());
        }
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_3)) {
            wrapper.writeMap(material.getOverrideArmorMaterials(),
                    PacketWrapper::writeMappedEntity, PacketWrapper::writeString);
        }
        wrapper.writeComponent(material.getDescription());
    }

    @Deprecated
    static TrimMaterial decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        return decode(nbt, PacketWrapper.createDummyWrapper(version), data);
    }

    @Deprecated
    static TrimMaterial decode(NBT nbt, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data) {
        return DIRECT_CODEC.decode(nbt, wrapper).copy(data);
    }

    @Deprecated
    static NBT encode(TrimMaterial material, ClientVersion version) {
        return encode(PacketWrapper.createDummyWrapper(version), material);
    }

    @Deprecated
    static NBT encode(PacketWrapper<?> wrapper, TrimMaterial material) {
        return DIRECT_CODEC.encode(wrapper, material);
    }
}
