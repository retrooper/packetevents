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

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;

import java.util.Objects;

public class StaticBannerPattern implements BannerPattern {

    private final ResourceLocation assetId;
    private final String translationKey;

    public StaticBannerPattern(ResourceLocation assetId, String translationKey) {
        this.assetId = assetId;
        this.translationKey = translationKey;
    }

    @Override
    public ResourceLocation getAssetId() {
        return this.assetId;
    }

    @Override
    public String getTranslationKey() {
        return this.translationKey;
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticBannerPattern)) return false;
        StaticBannerPattern that = (StaticBannerPattern) obj;
        if (!this.assetId.equals(that.assetId)) return false;
        return this.translationKey.equals(that.translationKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.assetId, this.translationKey);
    }
}
