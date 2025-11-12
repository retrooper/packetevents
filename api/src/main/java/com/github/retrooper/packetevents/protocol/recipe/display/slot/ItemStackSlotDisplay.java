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

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class ItemStackSlotDisplay extends SlotDisplay<ItemStackSlotDisplay> {

    private ItemStack stack;

    public ItemStackSlotDisplay(ItemStack stack) {
        super(SlotDisplayTypes.ITEM_STACK);
        this.stack = stack;
    }

    public static ItemStackSlotDisplay read(PacketWrapper<?> wrapper) {
        ItemStack stack = wrapper.readItemStack();
        return new ItemStackSlotDisplay(stack);
    }

    public static void write(PacketWrapper<?> wrapper, ItemStackSlotDisplay display) {
        wrapper.writeItemStack(display.stack);
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemStackSlotDisplay)) return false;
        ItemStackSlotDisplay that = (ItemStackSlotDisplay) obj;
        return this.stack.equals(that.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.stack);
    }

    @Override
    public String toString() {
        return "ItemStackSlotDisplay{stack=" + this.stack + '}';
    }
}
