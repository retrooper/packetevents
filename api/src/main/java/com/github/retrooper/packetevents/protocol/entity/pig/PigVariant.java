/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.entity.pig;

import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.Nullable;

public interface PigVariant extends MappedEntity, CopyableEntity<PigVariant>, DeepComparableEntity {

    ModelType getModelType();

    ResourceLocation getAssetId();

    static PigVariant read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntity(PigVariants.getRegistry());
    }

    static void write(PacketWrapper<?> wrapper, PigVariant variant) {
        wrapper.writeMappedEntity(variant);
    }

    static PigVariant decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        String modelTypeString = compound.getStringTagValueOrNull("model");
        ModelType modelType = modelTypeString != null ? ModelType.getByName(modelTypeString) : ModelType.NORMAL;
        ResourceLocation assetId = new ResourceLocation(compound.getStringTagValueOrThrow("asset_id"));
        return new StaticPigVariant(data, modelType, assetId);
    }

    static NBT encode(PigVariant variant, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("model", new NBTString(variant.getModelType().getName()));
        compound.setTag("asset_id", new NBTString(variant.getAssetId().toString()));
        return compound;
    }

    enum ModelType {

        NORMAL("normal"),
        COLD("cold"),
        ;

        private static final Index<String, ModelType> NAME_INDEX = Index.create(
                ModelType.class, ModelType::getName);

        private final String name;

        ModelType(String name) {
            this.name = name;
        }

        public static ModelType getByName(String name) {
            return AdventureIndexUtil.indexValueOrThrow(NAME_INDEX, name);
        }

        public String getName() {
            return this.name;
        }
    }
}
