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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.recipe.data.MerchantOffer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;

public class WrapperPlayServerMerchantOffers extends PacketWrapper<WrapperPlayServerMerchantOffers> {
    private int containerId;
    private List<MerchantOffer> merchantOffers;
    private int villagerLevel;
    private int villagerXp;
    private boolean showProgress;
    private boolean canRestock;

    public WrapperPlayServerMerchantOffers(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerMerchantOffers(int containerId, List<MerchantOffer> merchantOffers,
                                           int villagerLevel, int villagerXp, boolean showProgress, boolean canRestock) {
        super(PacketType.Play.Server.MERCHANT_OFFERS);
        this.containerId = containerId;
        this.merchantOffers = merchantOffers;
        this.villagerLevel = villagerLevel;
        this.villagerXp = villagerXp;
        this.showProgress = showProgress;
        this.canRestock = canRestock;
    }

    @Override
    public void read() {
        containerId = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2) ? this.readContainerId() : this.readVarInt();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            merchantOffers = readList(PacketWrapper::readMerchantOffer);
        } else {
            int size = readByte() & 0xFF;
            merchantOffers = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                merchantOffers.add(readMerchantOffer());
            }
        }
        villagerLevel = readVarInt();
        villagerXp = readVarInt();
        showProgress = readBoolean();
        canRestock = readBoolean();
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)){
            this.writeContainerId(this.containerId);
        } else {
            this.writeVarInt(this.containerId);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            writeList(merchantOffers, PacketWrapper::writeMerchantOffer);
        }
        else {
            writeByte(merchantOffers.size() & 0xFF);
            for (MerchantOffer data : merchantOffers) {
                writeMerchantOffer(data);
            }
        }
        writeVarInt(villagerLevel);
        writeVarInt(villagerXp);
        writeBoolean(showProgress);
        writeBoolean(canRestock);
    }

    @Override
    public void copy(WrapperPlayServerMerchantOffers wrapper) {
        containerId = wrapper.containerId;
        merchantOffers = wrapper.merchantOffers;
        villagerLevel = wrapper.villagerLevel;
        villagerXp = wrapper.villagerXp;
        showProgress = wrapper.showProgress;
        canRestock = wrapper.canRestock;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public List<MerchantOffer> getMerchantOffers() {
        return merchantOffers;
    }

    public void setMerchantOffers(List<MerchantOffer> merchantOffers) {
        this.merchantOffers = merchantOffers;
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
