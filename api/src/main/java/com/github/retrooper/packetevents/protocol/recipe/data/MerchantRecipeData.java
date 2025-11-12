package com.github.retrooper.packetevents.protocol.recipe.data;

import com.github.retrooper.packetevents.protocol.item.ItemStack;

public class MerchantRecipeData implements RecipeData{

    private ItemStack buyItem1;
    private ItemStack buyItem2;
    private ItemStack sellItem;
    private int uses;
    private int maxUses;
    private int xp;
    private float priceMultiplier;
    private int specialPrice;
    private int demand;

    private MerchantRecipeData(ItemStack buyItem1, ItemStack buyItem2, ItemStack sellItem, int uses, int maxUses, int xp, float priceMultiplier, int demand, int specialPrice) {
        this.buyItem1 = buyItem1;
        this.buyItem2 = buyItem2;
        this.sellItem = sellItem;
        this.uses = uses;
        this.maxUses = maxUses;
        this.xp = xp;
        this.priceMultiplier = priceMultiplier;
        this.demand = demand;
        this.specialPrice = specialPrice;
    }

    public static MerchantRecipeData of(ItemStack buyItem1, ItemStack buyItem2, ItemStack sellItem, int uses, int maxUses, int xp, float priceMultiplier, int demand, int specialPrice) {
        return new MerchantRecipeData(buyItem1, buyItem2, sellItem, uses, maxUses, xp, priceMultiplier, demand, specialPrice);
    }

    public static MerchantRecipeData of(ItemStack buyItem1, ItemStack sellItem, int uses, int maxUses, int xp, float priceMultiplier, int demand, int specialPrice) {
        return new MerchantRecipeData(buyItem1, null, sellItem, uses, maxUses, xp, priceMultiplier, demand, specialPrice);
    }

    public static MerchantRecipeData of(ItemStack buyItem1, ItemStack sellItem, int uses, int maxUses, int xp, float priceMultiplier, int demand) {
        return new MerchantRecipeData(buyItem1, null, sellItem, uses, maxUses, xp, priceMultiplier, demand, 0);
    }

    public ItemStack getBuyItem1() {
        return buyItem1;
    }

    public void setBuyItem1(ItemStack buyItem1) {
        this.buyItem1 = buyItem1;
    }

    public ItemStack getBuyItem2() {
        return buyItem2;
    }

    public void setBuyItem2(ItemStack buyItem2) {
        this.buyItem2 = buyItem2;
    }

    public ItemStack getSellItem() {
        return sellItem;
    }

    public void setSellItem(ItemStack sellItem) {
        this.sellItem = sellItem;
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
}
