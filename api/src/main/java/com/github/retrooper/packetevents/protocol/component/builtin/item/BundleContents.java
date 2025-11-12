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

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;
import java.util.Objects;

public class BundleContents {

    private List<ItemStack> items;

    public BundleContents(List<ItemStack> items) {
        this.items = items;
    }

    public static BundleContents read(PacketWrapper<?> wrapper) {
        List<ItemStack> items = wrapper.readList(PacketWrapper::readPresentItemStack);
        return new BundleContents(items);
    }

    public static void write(PacketWrapper<?> wrapper, BundleContents projectiles) {
        wrapper.writeList(projectiles.items, PacketWrapper::writePresentItemStack);
    }

    public void addItem(ItemStack itemStack) {
        this.items.add(itemStack);
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BundleContents)) return false;
        BundleContents that = (BundleContents) obj;
        return this.items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.items);
    }
}
