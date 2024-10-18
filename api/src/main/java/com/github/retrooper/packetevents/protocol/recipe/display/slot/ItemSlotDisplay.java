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

import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class ItemSlotDisplay extends SlotDisplay<ItemSlotDisplay> {

    private ItemType item;

    public ItemSlotDisplay(ItemType item) {
        super(SlotDisplayTypes.ITEM);
        this.item = item;
    }

    public static ItemSlotDisplay read(PacketWrapper<?> wrapper) {
        ItemType item = wrapper.readMappedEntity(ItemTypes.getRegistry());
        return new ItemSlotDisplay(item);
    }

    public static void write(PacketWrapper<?> wrapper, ItemSlotDisplay display) {
        wrapper.writeMappedEntity(display.getItem());
    }

    public ItemType getItem() {
        return this.item;
    }

    public void setItem(ItemType item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemSlotDisplay)) return false;
        ItemSlotDisplay that = (ItemSlotDisplay) obj;
        return this.item.equals(that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.item);
    }

    @Override
    public String toString() {
        return "ItemSlotDisplay{item=" + this.item + '}';
    }
}
