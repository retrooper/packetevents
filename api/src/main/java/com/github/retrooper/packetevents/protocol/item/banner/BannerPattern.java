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

package com.github.retrooper.packetevents.protocol.item.banner;

import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public interface BannerPattern extends MappedEntity {

    ResourceLocation getAssetId();

    String getTranslationKey();

    static BannerPattern readDirect(PacketWrapper<?> wrapper) {
        ResourceLocation assetId = wrapper.readIdentifier();
        String translationKey = wrapper.readString();
        return new StaticBannerPattern(assetId, translationKey);
    }

    static void writeDirect(PacketWrapper<?> wrapper, BannerPattern pattern) {
        wrapper.writeIdentifier(pattern.getAssetId());
        wrapper.writeString(pattern.getTranslationKey());
    }
}
