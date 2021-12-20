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

package com.github.retrooper.packetevents.protocol.item;

import com.github.retrooper.packetevents.protocol.enchantment.Enchantment;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;

public class ItemStack {
    //TODO Rethink my decision of making NULL = AIR
    public static final ItemStack EMPTY = new ItemStack(ItemTypes.AIR, -1, new NBTCompound(), -1);
    private final ItemType type;
    private int amount;
    private NBTCompound nbt;
    private int legacyData = -1;

    private boolean cachedIsEmpty = false;

    private ItemStack(ItemType type, int amount) {
        this.type = type;
        this.amount = amount;
        this.nbt = new NBTCompound();
        updateCachedEmptyStatus();
    }

    private ItemStack(ItemType type, int amount, NBTCompound nbt) {
        this.type = type;
        this.amount = amount;
        this.nbt = nbt;
        updateCachedEmptyStatus();
    }

    private ItemStack(ItemType type, int amount, NBTCompound nbt, int legacyData) {
        this.type = type;
        this.amount = amount;
        this.nbt = nbt;
        this.legacyData = legacyData;
        updateCachedEmptyStatus();
    }

    public int getMaxStackSize() {
        return getType().getMaxAmount();
    }

    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.isDamageableItem() || !this.isDamaged());
    }

    public boolean isDamageableItem() {
        if (!this.cachedIsEmpty && this.getType().getMaxDurability() > 0) {
            NBTCompound compoundtag = this.getNBT();
            return compoundtag == null || !compoundtag.getBoolean("Unbreakable");
        } else {
            return false;
        }
    }

    public boolean isDamaged() {
        return this.isDamageableItem() && this.getDamageValue() > 0;
    }

    public int getDamageValue() {
        if (nbt == null) return 0;
        NBTInt damage = this.nbt.getTagOfTypeOrNull("Damage", NBTInt.class);
        return damage == null ? 0 : damage.getAsInt();
    }

    public void setDamageValue(int damage) {
        this.getOrCreateTag().setTag("Damage", new NBTInt(Math.max(0, damage)));
    }

    public int getMaxDamage() {
        return this.getType().getMaxDurability();
    }

    public NBTCompound getOrCreateTag() {
        if (this.nbt == null) {
            this.nbt = new NBTCompound();
        }

        return this.nbt;
    }

    private void updateCachedEmptyStatus() {
        cachedIsEmpty = isEmpty();
    }

    public ItemType getType() {
        return cachedIsEmpty ? ItemTypes.AIR : type;
    }

    public int getAmount() {
        return cachedIsEmpty ? 0 : amount;
    }

    public void shrink(int amount) {
        this.setAmount(this.getAmount() - amount);
    }

    public void grow(int amount) {
        this.setAmount(this.getAmount() + amount);
    }

    public void setAmount(int amount) {
        this.amount = amount;
        updateCachedEmptyStatus();
    }

    public ItemStack split(int toTake) {
        int i = Math.min(toTake, getAmount());
        ItemStack itemstack = this.copy();
        itemstack.setAmount(i);
        this.shrink(i);
        return itemstack;
    }

    public ItemStack copy() {
        return cachedIsEmpty ? EMPTY : new ItemStack(type, amount, nbt == null ? new NBTCompound() : nbt.copy(), legacyData);
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

    public boolean isEnchantable() {
        if (getType() == ItemTypes.BOOK) return getAmount() == 1;
        if (getType() == ItemTypes.ENCHANTED_BOOK) return false;
        return getMaxStackSize() == 1 && canBeDepleted() && !isEnchanted();
    }

    public boolean isEnchanted() {
        if (this.nbt != null && this.nbt.getCompoundListTagOrNull("Enchantments") != null) {
            return !this.nbt.getCompoundListTagOrNull("Enchantments").isEmpty();
        } else {
            return false;
        }
    }

    // TODO: Probably broken on some outdated version
    public int getEnchantmentLevel(Enchantment enchantment) {
        if (isEmpty()) return 0;

        if (nbt != null && nbt.getCompoundListTagOrNull("Enchantments") != null) {
            for (NBTCompound base : nbt.getCompoundListTagOrNull("Enchantments").getTags()) {
                NBTString string = base.getTagOfTypeOrNull("id", NBTString.class);
                if (string != null && string.getValue().equals(enchantment.getIdentifier().toString())) {
                    return base.getTagOfTypeOrNull("lvl", NBTShort.class).getAsInt();
                }
            }
        }

        return 0;
    }

    public boolean canBeDepleted() {
        return this.getType().getMaxDurability() > 0;
    }

    public boolean is(ItemType type) {
        return this.getType() == type;
    }

    public static boolean isSameItemSameTags(ItemStack stack, ItemStack otherStack) {
        return stack.is(otherStack.getType()) && ItemStack.tagMatches(stack, otherStack);
    }

    public static boolean tagMatches(ItemStack left, ItemStack right) {
        return left.isEmpty() && right.isEmpty() ? true : (!left.isEmpty() && !right.isEmpty() ? (left.nbt == null && right.nbt != null ? false : left.nbt == null || left.nbt.equals(right.nbt)) : false);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) obj;
            return getType().equals(itemStack.getType())
                    && amount == itemStack.amount
                    && nbt.equals(itemStack.nbt)
                    && legacyData == itemStack.legacyData;
        }
        return false;
    }

    @Override
    public String toString() {
        if (cachedIsEmpty) {
            return "ItemStack[null]";
        }
        else {
            String identifier = type == null ? "null" : type.getIdentifier().toString();
            int maxAmount = getType().getMaxAmount();
            return "ItemStack[type=" + identifier + ", amount=" + amount + "/" + maxAmount
                    + ", nbt tag names: " + nbt.getTagNames() + ", legacyData=" + legacyData + "]";
        }
    }

    public boolean isEmpty() {
        return type == null || type == ItemTypes.AIR || amount <= 0;
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
