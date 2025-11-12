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

import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class TagSlotDisplay extends SlotDisplay<TagSlotDisplay> {

    private ResourceLocation itemTag;

    public TagSlotDisplay(ResourceLocation itemTag) {
        super(SlotDisplayTypes.TAG);
        this.itemTag = itemTag;
    }

    public static TagSlotDisplay read(PacketWrapper<?> wrapper) {
        ResourceLocation itemTag = wrapper.readIdentifier();
        return new TagSlotDisplay(itemTag);
    }

    public static void write(PacketWrapper<?> wrapper, TagSlotDisplay display) {
        wrapper.writeIdentifier(display.itemTag);
    }

    public ResourceLocation getItemTag() {
        return this.itemTag;
    }

    public void setItemTag(ResourceLocation itemTag) {
        this.itemTag = itemTag;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TagSlotDisplay)) return false;
        TagSlotDisplay that = (TagSlotDisplay) obj;
        return this.itemTag.equals(that.itemTag);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.itemTag);
    }

    @Override
    public String toString() {
        return "TagSlotDisplay{itemTag=" + this.itemTag + '}';
    }
}
