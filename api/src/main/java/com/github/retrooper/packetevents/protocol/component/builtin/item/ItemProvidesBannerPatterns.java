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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.banner.BannerPattern;
import com.github.retrooper.packetevents.protocol.item.banner.BannerPatterns;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * @versions 1.21.5+
 */
@NullMarked
public class ItemProvidesBannerPatterns {

    /**
     * @versions 26.1+
     */
    private MappedEntitySet<BannerPattern> set;

    /**
     * @versions 26.1+
     */
    public ItemProvidesBannerPatterns(MappedEntitySet<BannerPattern> set) {
        this.set = set;
    }

    public ItemProvidesBannerPatterns(ResourceLocation tagKey) {
        this(new MappedEntitySet<>(tagKey));
    }

    public static ItemProvidesBannerPatterns read(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_1)) {
            MappedEntitySet<BannerPattern> set = MappedEntitySet.read(wrapper, BannerPatterns.getRegistry());
            return new ItemProvidesBannerPatterns(set);
        }
        ResourceLocation tagKey = wrapper.readIdentifier();
        return new ItemProvidesBannerPatterns(tagKey);
    }

    public static void write(PacketWrapper<?> wrapper, ItemProvidesBannerPatterns patterns) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_1)) {
            MappedEntitySet.write(wrapper, patterns.set);
        } else {
            wrapper.writeIdentifier(patterns.getTagKey());
        }
    }

    /**
     * @versions 26.1+
     */
    public MappedEntitySet<BannerPattern> getSet() {
        return this.set;
    }

    /**
     * @versions 26.1+
     */
    public void setSet(MappedEntitySet<BannerPattern> set) {
        this.set = set;
    }

    public ResourceLocation getTagKey() {
        ResourceLocation tagKey = this.set.getTagKey();
        if (tagKey == null) {
            throw new IllegalStateException("No tag key present");
        }
        return tagKey;
    }

    public void setTagKey(ResourceLocation tagKey) {
        this.set = new MappedEntitySet<>(tagKey);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemProvidesBannerPatterns)) return false;
        ItemProvidesBannerPatterns that = (ItemProvidesBannerPatterns) obj;
        return this.set.equals(that.set);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.set);
    }
}
