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

import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

public interface TrimPattern extends MappedEntity {

    ResourceLocation getAssetId();

    ItemType getTemplateItem();

    Component getDescription();

    boolean isDecal();

    static TrimPattern read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntityOrDirect(TrimPatterns::getById, TrimPattern::readDirect);
    }

    static TrimPattern readDirect(PacketWrapper<?> wrapper) {
        ResourceLocation assetId = wrapper.readIdentifier();
        ItemType templateItem = wrapper.readMappedEntity(ItemTypes::getById);
        Component description = wrapper.readComponent();
        boolean decal = wrapper.readBoolean();
        return new StaticTrimPattern(assetId, templateItem, description, decal);
    }

    static void write(PacketWrapper<?> wrapper, TrimPattern pattern) {
        wrapper.writeMappedEntityOrDirect(pattern, TrimPattern::writeDirect);
    }

    static void writeDirect(PacketWrapper<?> wrapper, TrimPattern pattern) {
        wrapper.writeIdentifier(pattern.getAssetId());
        wrapper.writeMappedEntity(pattern.getTemplateItem());
        wrapper.writeComponent(pattern.getDescription());
        wrapper.writeBoolean(pattern.isDecal());
    }
}
