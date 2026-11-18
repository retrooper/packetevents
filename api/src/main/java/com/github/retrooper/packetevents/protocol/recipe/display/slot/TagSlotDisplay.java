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

package com.github.retrooper.packetevents.protocol.recipe.display.slot;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class TagSlotDisplay extends SlotDisplay<TagSlotDisplay> {

    private MappedEntitySet<ItemType> items;

    public TagSlotDisplay(ResourceLocation itemTag) {
        this(new MappedEntitySet<>(itemTag));
    }

    /**
     * @versions 26.3+
     */
    public TagSlotDisplay(MappedEntitySet<ItemType> items) {
        super(SlotDisplayTypes.TAG);
        this.items = items;
    }

    public static TagSlotDisplay read(PacketWrapper<?> wrapper) {
        MappedEntitySet<ItemType> items = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_3)
                ? MappedEntitySet.read(wrapper, ItemTypes.getRegistry())
                : new MappedEntitySet<>(wrapper.readIdentifier());
        return new TagSlotDisplay(items);
    }

    public static void write(PacketWrapper<?> wrapper, TagSlotDisplay display) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_3)) {
            MappedEntitySet.write(wrapper, display.items);
        } else {
            wrapper.writeIdentifier(display.getItemTag());
        }
    }

    /**
     * @versions 26.3+
     */
    public MappedEntitySet<ItemType> getItems() {
        return this.items;
    }

    /**
     * @versions 26.3+
     */
    public void setItems(MappedEntitySet<ItemType> items) {
        this.items = items;
    }

    public ResourceLocation getItemTag() {
        ResourceLocation tagKey = this.items.getTagKey();
        if (tagKey == null) {
            throw new IllegalStateException("No tag key present for " + this);
        }
        return tagKey;
    }

    public void setItemTag(ResourceLocation itemTag) {
        this.items = new MappedEntitySet<>(itemTag);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TagSlotDisplay)) return false;
        TagSlotDisplay that = (TagSlotDisplay) obj;
        return this.items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.items);
    }

    @Override
    public String toString() {
        return "TagSlotDisplay{items=" + this.items + '}';
    }
}
