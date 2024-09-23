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

package com.github.retrooper.packetevents.protocol.world.painting;

import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

public interface PaintingVariant extends MappedEntity, CopyableEntity<PaintingVariant>, DeepComparableEntity {

    int getWidth();

    int getHeight();

    ResourceLocation getAssetId();

    static PaintingVariant readDirect(PacketWrapper<?> wrapper) {
        int width = wrapper.readVarInt();
        int height = wrapper.readVarInt();
        ResourceLocation assetId = wrapper.readIdentifier();
        return new StaticPaintingVariant(width, height, assetId);
    }

    static void writeDirect(PacketWrapper<?> wrapper, PaintingVariant variant) {
        wrapper.writeVarInt(variant.getWidth());
        wrapper.writeVarInt(variant.getHeight());
        wrapper.writeIdentifier(variant.getAssetId());
    }

    static PaintingVariant decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        int width = compound.getNumberTagOrThrow("width").getAsInt();
        int height = compound.getNumberTagOrThrow("height").getAsInt();
        ResourceLocation assetId = new ResourceLocation(compound.getStringTagValueOrThrow("asset_id"));
        return new StaticPaintingVariant(data, width, height, assetId);
    }

    static NBT encode(PaintingVariant variant, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("width", new NBTInt(variant.getWidth()));
        compound.setTag("height", new NBTInt(variant.getHeight()));
        compound.setTag("asset_id", new NBTString(variant.getAssetId().toString()));
        return compound;
    }
}
