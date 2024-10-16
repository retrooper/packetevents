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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class ItemRepairable {

    private MappedEntitySet<ItemType> items;

    public ItemRepairable(MappedEntitySet<ItemType> items) {
        this.items = items;
    }

    public static ItemRepairable read(PacketWrapper<?> wrapper) {
        MappedEntitySet<ItemType> items = MappedEntitySet.read(wrapper, ItemTypes.getRegistry());
        return new ItemRepairable(items);
    }

    public static void write(PacketWrapper<?> wrapper, ItemRepairable repairable) {
        MappedEntitySet.write(wrapper, repairable.items);
    }

    public MappedEntitySet<ItemType> getItems() {
        return this.items;
    }

    public void setItems(MappedEntitySet<ItemType> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemRepairable)) return false;
        ItemRepairable that = (ItemRepairable) obj;
        return this.items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.items);
    }

    @Override
    public String toString() {
        return "ItemRepairable{items=" + this.items + '}';
    }
}
