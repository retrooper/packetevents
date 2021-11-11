/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.inventory;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;

public class ItemStack {
    public static final ItemStack NULL = new ItemStack(ItemTypes.AIR, -1);
    //TODO Support legacy data
    private ItemType type;
    private int amount;
    private NBTCompound nbt;

    private ItemStack(ItemType type, int amount) {
        this.type = type;
        this.amount = amount;
        this.nbt = new NBTCompound();
    }

    private ItemStack(ItemType type, int amount, NBTCompound nbt) {
        this.type = type;
        this.amount = amount;
        this.nbt = nbt;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public NBTCompound getNBT() {
        return nbt;
    }

    public void setNBT(NBTCompound nbt) {
        this.nbt = nbt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) obj;
            return type == itemStack.type
                    && amount == itemStack.amount
                    && nbt.equals(itemStack.nbt);
        }
        return false;
    }

    public boolean isEmpty() {
        return amount == 0;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ItemType type;
        private int amount;
        private NBTCompound nbt = new NBTCompound();

        public Builder type(ItemType type) {
            this.type = type;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder nbt(NBTCompound nbt) {
            this.nbt = nbt;
            return this;
        }

        public ItemStack build() {
            return new ItemStack(type, amount, nbt);
        }

    }
}
