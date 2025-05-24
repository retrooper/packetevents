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

package com.github.retrooper.packetevents.protocol.item.trimpattern;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public interface TrimPattern extends MappedEntity, CopyableEntity<TrimPattern>, DeepComparableEntity {

    ResourceLocation getAssetId();

    /**
     * Removed with 1.21.5
     */
    @ApiStatus.Obsolete
    ItemType getTemplateItem();

    Component getDescription();

    boolean isDecal();

    static TrimPattern read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntityOrDirect(TrimPatterns.getRegistry(), TrimPattern::readDirect);
    }

    static TrimPattern readDirect(PacketWrapper<?> wrapper) {
        ResourceLocation assetId = wrapper.readIdentifier();
        ItemType templateItem = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) ? null :
                wrapper.readMappedEntity(ItemTypes::getById);
        Component description = wrapper.readComponent();
        boolean decal = wrapper.readBoolean();
        return new StaticTrimPattern(assetId, templateItem, description, decal);
    }

    static void write(PacketWrapper<?> wrapper, TrimPattern pattern) {
        wrapper.writeMappedEntityOrDirect(pattern, TrimPattern::writeDirect);
    }

    static void writeDirect(PacketWrapper<?> wrapper, TrimPattern pattern) {
        wrapper.writeIdentifier(pattern.getAssetId());
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
            wrapper.writeMappedEntity(pattern.getTemplateItem());
        }
        wrapper.writeComponent(pattern.getDescription());
        wrapper.writeBoolean(pattern.isDecal());
    }

    static TrimPattern decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        ResourceLocation assetId = new ResourceLocation(compound.getStringTagValueOrThrow("asset_id"));
        ItemType templateItem = version.isNewerThanOrEquals(ClientVersion.V_1_21_5) ? null :
                ItemTypes.getByName(compound.getStringTagValueOrThrow("template_item"));
        Component description = AdventureSerializer.serializer(version).fromNbtTag(compound.getTagOrThrow("description"));
        boolean decal = version.isNewerThanOrEquals(ClientVersion.V_1_20_2) && compound.getBoolean("decal");
        return new StaticTrimPattern(data, assetId, templateItem, description, decal);
    }

    static NBT encode(TrimPattern pattern, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("asset_id", new NBTString(pattern.getAssetId().toString()));
        if (version.isOlderThan(ClientVersion.V_1_21_5)) {
            compound.setTag("template_item", new NBTString(pattern.getTemplateItem().getName().toString()));
        }
        compound.setTag("description", AdventureSerializer.serializer(version).asNbtTag(pattern.getDescription()));
        if (version.isNewerThanOrEquals(ClientVersion.V_1_20_2)) {
            compound.setTag("decal", new NBTByte(pattern.isDecal()));
        }
        return compound;
    }
}
