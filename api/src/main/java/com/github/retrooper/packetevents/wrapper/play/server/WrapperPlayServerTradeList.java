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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.recipe.data.MerchantRecipeData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WrapperPlayServerTradeList extends PacketWrapper<WrapperPlayServerTradeList> {

    public WrapperPlayServerTradeList(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerTradeList(int containerId, Collection<? extends MerchantRecipeData> merchantRecipeData, int villagerLevel, int villagerXp, boolean showProgress, boolean canRestock) {
        super(PacketType.Play.Server.TRADE_LIST);
        this.containerId = containerId;
        this.merchantRecipeData = new ArrayList<>(merchantRecipeData);
        this.villagerLevel = villagerLevel;
        this.villagerXp = villagerXp;
        this.showProgress = showProgress;
        this.canRestock = canRestock;
    }

    private int containerId;
    private List<MerchantRecipeData> merchantRecipeData;
    private int villagerLevel;
    private int villagerXp;
    private boolean showProgress;
    private boolean canRestock;

    @Override
    public void read() {
        containerId = readVarInt();
        int size = readByte() & 0xFF;
        merchantRecipeData = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ItemStack buyItemPrimary = readItemStack();
            ItemStack sellItem = readItemStack();
            ItemStack buyItemSecondary = null;
            if (readBoolean()) {
                buyItemSecondary = readItemStack();
            }
            readBoolean(); //WASTE FOR FULL USE
            int uses = readInt();
            int maxUses = readInt();
            int xp = readInt();
            int specialPrice = readInt();
            float priceMultiplier = readFloat();
            int demand = readInt();
            merchantRecipeData.add(MerchantRecipeData.of(buyItemPrimary, sellItem, buyItemSecondary, uses, maxUses, xp, priceMultiplier, demand, specialPrice));
        }
        villagerLevel = readVarInt();
        villagerXp = readVarInt();
        showProgress = readBoolean();
        canRestock = readBoolean();
    }

    @Override
    public void copy(WrapperPlayServerTradeList wrapper) {
        containerId = wrapper.containerId;
        merchantRecipeData = wrapper.merchantRecipeData;
        villagerLevel = wrapper.villagerLevel;
        villagerXp = wrapper.villagerXp;
        showProgress = wrapper.showProgress;
        canRestock = wrapper.canRestock;
    }

    @Override
    public void write() {
        writeVarInt(containerId);
        writeByte(this.merchantRecipeData.size() & 0xFF);
        for (MerchantRecipeData data : this.merchantRecipeData) {
            writeItemStack(data.getBuyItem1());
            writeItemStack(data.getSellItem());
            ItemStack buyItemSecondary = data.getBuyItem2();
            if (buyItemSecondary == null || buyItemSecondary.isEmpty()) {
                writeBoolean(false);
            } else {
                writeBoolean(true);
                writeItemStack(buyItemSecondary);
            }
            writeBoolean(data.getUses() >= data.getMaxUses());
            writeInt(data.getUses());
            writeInt(data.getMaxUses());
            writeInt(data.getXp());
            writeInt(data.getSpecialPrice());
            writeFloat(data.getPriceMultiplier());
            writeInt(data.getDemand());
        }
        writeVarInt(this.villagerLevel);
        writeVarInt(this.villagerXp);
        writeBoolean(this.showProgress);
        writeBoolean(this.canRestock);
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public List<MerchantRecipeData> getMerchantRecipeData() {
        return merchantRecipeData;
    }

    public void setMerchantRecipeData(List<MerchantRecipeData> merchantRecipeData) {
        this.merchantRecipeData = merchantRecipeData;
    }

    public int getVillagerLevel() {
        return villagerLevel;
    }

    public void setVillagerLevel(int villagerLevel) {
        this.villagerLevel = villagerLevel;
    }

    public int getVillagerXp() {
        return villagerXp;
    }

    public void setVillagerXp(int villagerXp) {
        this.villagerXp = villagerXp;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isCanRestock() {
        return canRestock;
    }

    public void setCanRestock(boolean canRestock) {
        this.canRestock = canRestock;
    }
}
