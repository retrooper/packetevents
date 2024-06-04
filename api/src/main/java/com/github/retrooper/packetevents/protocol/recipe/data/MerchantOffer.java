/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.recipe.data;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public class MerchantOffer implements RecipeData {

    private ItemStack firstInputItem;
    private @Nullable ItemStack secondInputItem;
    private ItemStack outputItem;
    private int uses;
    private int maxUses;
    private int xp;
    private int specialPrice;
    private float priceMultiplier;
    private int demand;

    private MerchantOffer(
            MerchantItemCost firstInputItem, @Nullable MerchantItemCost secondInputItem,
            ItemStack outputItem, int uses, int maxUses, int xp,
            int specialPrice, float priceMultiplier, int demand
    ) {
        this(
                firstInputItem.asItem(),
                secondInputItem == null ? null : secondInputItem.asItem(),
                outputItem, uses, maxUses, xp, specialPrice, priceMultiplier, demand
        );
    }

    private MerchantOffer(
            ItemStack firstInputItem, @Nullable ItemStack secondInputItem,
            ItemStack outputItem, int uses, int maxUses, int xp,
            int specialPrice, float priceMultiplier, int demand
    ) {
        this.firstInputItem = firstInputItem;
        this.secondInputItem = secondInputItem;
        this.outputItem = outputItem;
        this.uses = uses;
        this.maxUses = maxUses;
        this.xp = xp;
        this.priceMultiplier = priceMultiplier;
        this.demand = demand;
        this.specialPrice = specialPrice;
    }

    public static MerchantOffer of(ItemStack buyItem1, @Nullable ItemStack buyItem2, ItemStack sellItem, int uses, int maxUses, int xp, int specialPrice, float priceMultiplier, int demand) {
        return new MerchantOffer(buyItem1, buyItem2, sellItem, uses, maxUses, xp, specialPrice, priceMultiplier, demand);
    }

    public static MerchantOffer of(ItemStack buyItem1, ItemStack sellItem, int uses, int maxUses, int xp, int specialPrice, float priceMultiplier, int demand) {
        return new MerchantOffer(buyItem1, null, sellItem, uses, maxUses, xp, specialPrice, priceMultiplier, demand);
    }

    public static MerchantOffer of(ItemStack buyItem1, ItemStack sellItem, int uses, int maxUses, int xp, float priceMultiplier, int demand) {
        return new MerchantOffer(buyItem1, null, sellItem, uses, maxUses, xp, 0, priceMultiplier, demand);
    }

    public static MerchantOffer of(MerchantItemCost buyCost1, @Nullable MerchantItemCost buyCost2, ItemStack sellItem, int uses, int maxUses, int xp, int specialPrice, float priceMultiplier, int demand) {
        return new MerchantOffer(buyCost1, buyCost2, sellItem, uses, maxUses, xp, specialPrice, priceMultiplier, demand);
    }

    public static MerchantOffer of(MerchantItemCost buyCost1, ItemStack sellItem, int uses, int maxUses, int xp, int specialPrice, float priceMultiplier, int demand) {
        return new MerchantOffer(buyCost1, null, sellItem, uses, maxUses, xp, specialPrice, priceMultiplier, demand);
    }

    public static MerchantOffer of(MerchantItemCost buyCost1, ItemStack sellItem, int uses, int maxUses, int xp, float priceMultiplier, int demand) {
        return new MerchantOffer(buyCost1, null, sellItem, uses, maxUses, xp, 0, priceMultiplier, demand);
    }

    public MerchantItemCost getFirstInputCost() {
        return MerchantItemCost.ofItem(this.firstInputItem);
    }

    public void setFirstInputCost(MerchantItemCost firstInputCost) {
        this.firstInputItem = firstInputCost.asItem();
    }

    @ApiStatus.Obsolete
    public ItemStack getFirstInputItem() {
        return this.firstInputItem;
    }

    @ApiStatus.Obsolete
    public void setFirstInputItem(ItemStack firstInputItem) {
        this.firstInputItem = firstInputItem;
    }

    public @Nullable MerchantItemCost getSecondInputCost() {
        return MerchantItemCost.ofItem(this.firstInputItem);
    }

    public void setSecondInputCost(@Nullable MerchantItemCost secondInputCost) {
        this.secondInputItem = secondInputCost == null ? null : secondInputCost.asItem();
    }

    @ApiStatus.Obsolete
    public @Nullable ItemStack getSecondInputItem() {
        return this.secondInputItem;
    }

    @ApiStatus.Obsolete
    public void setSecondInputItem(@Nullable ItemStack secondInputItem) {
        this.secondInputItem = secondInputItem;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public void setOutputItem(ItemStack outputItem) {
        this.outputItem = outputItem;
    }

    public int getUses() {
        return uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public float getPriceMultiplier() {
        return priceMultiplier;
    }

    public void setPriceMultiplier(float priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public int getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(int specialPrice) {
        this.specialPrice = specialPrice;
    }

    public boolean isOutOfStock() {
        return uses >= maxUses;
    }
}
