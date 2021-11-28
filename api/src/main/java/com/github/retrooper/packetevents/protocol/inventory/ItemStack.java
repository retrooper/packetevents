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
    //TODO Rethink my decision of making NULL = AIR
    public static final ItemStack NULL = new ItemStack(ItemTypes.AIR, -1, new NBTCompound(), -1);
    private ItemType type;
    private int amount;
    private NBTCompound nbt;
    private int legacyData = -1;

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

    private ItemStack(ItemType type, int amount, NBTCompound nbt, int legacyData) {
        this.type = type;
        this.amount = amount;
        this.nbt = nbt;
        this.legacyData = legacyData;
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

    public int getLegacyData() {
        return legacyData;
    }

    public void setLegacyData(int legacyData) {
        this.legacyData = legacyData;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) obj;
            return type.equals(itemStack.type)
                    && amount == itemStack.amount
                    && nbt.equals(itemStack.nbt)
                    && legacyData == itemStack.legacyData;
        }
        return false;
    }

    @Override
    public String toString() {
        if (NULL.equals(this)) {
            return "ItemStack[null]";
        }
        else {
            String identifier = type == null ? "null" : type.getIdentifier().toString();
            int maxAmount = type == null ? -1 : type.getMaxAmount();
            return "ItemStack[type=" + identifier + ", amount=" + amount + "/" + maxAmount
                    + ", nbt tag names: " + nbt.getTagNames() + ", legacyData=" + legacyData + "]";
        }
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
        private int legacyData = -1;

        public Builder type(ItemType type) {
            this.type = type;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder nbt(NBTCompound nbt) {
            if (nbt == null) {
                nbt = new NBTCompound();
            }
            this.nbt = nbt;
            return this;
        }

        public Builder legacyData(int legacyData) {
            this.legacyData = legacyData;
            return this;
        }

        public ItemStack build() {
            return new ItemStack(type, amount, nbt, legacyData);
        }

    }
}
