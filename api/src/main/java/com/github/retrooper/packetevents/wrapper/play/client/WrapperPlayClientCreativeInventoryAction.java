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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientCreativeInventoryAction extends PacketWrapper<WrapperPlayClientCreativeInventoryAction> {
    private int slot;
    private ItemStack itemStack;

    public WrapperPlayClientCreativeInventoryAction(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientCreativeInventoryAction(int slot, ItemStack itemStack) {
        super(PacketType.Play.Client.CREATIVE_INVENTORY_ACTION);
        this.slot = slot;
        this.itemStack = itemStack;
    }

    @Override
    public void read() {
        this.slot = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)
                ? this.readUnsignedShort() : this.readShort();
        this.itemStack = readItemStack();
    }

    @Override
    public void write() {
        writeShort(slot);
        writeItemStack(itemStack);
    }

    @Override
    public void copy(WrapperPlayClientCreativeInventoryAction wrapper) {
        this.slot = wrapper.slot;
        this.itemStack = wrapper.itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
