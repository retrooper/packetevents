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

import java.util.Objects;

public class ItemUseRemainder {

    private ItemStack target;

    public ItemUseRemainder(ItemStack target) {
        this.target = target;
    }

    public static ItemUseRemainder read(PacketWrapper<?> wrapper) {
        ItemStack target = wrapper.readItemStack();
        return new ItemUseRemainder(target);
    }

    public static void write(PacketWrapper<?> wrapper, ItemUseRemainder remainder) {
        wrapper.writeItemStack(remainder.target);
    }

    public ItemStack getTarget() {
        return this.target;
    }

    public void setTarget(ItemStack target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemUseRemainder)) return false;
        ItemUseRemainder that = (ItemUseRemainder) obj;
        return this.target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.target);
    }

    @Override
    public String toString() {
        return "ItemUseRemainder{target=" + this.target + '}';
    }
}
