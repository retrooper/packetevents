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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientCreativeInventoryAction extends PacketWrapper<WrapperPlayClientCreativeInventoryAction> {
    private int slot;
    private Object itemStack;

    public WrapperPlayClientCreativeInventoryAction(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientCreativeInventoryAction(int slot, Object itemStack) {
        super(PacketType.Play.Client.CREATIVE_INVENTORY_ACTION);
        this.slot = slot;
        this.itemStack = itemStack;
    }

    @Override
    public void readData() {
        this.slot = readShort();
        this.itemStack = readItemStack();
    }

    @Override
    public void readData(WrapperPlayClientCreativeInventoryAction wrapper) {
        this.slot = wrapper.slot;
        this.itemStack = wrapper.itemStack;
    }

    @Override
    public void writeData() {
        writeShort(slot);
        writeItemStack(itemStack);
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Object getItemStack() {
        return itemStack;
    }

    public void setItemStack(Object itemStack) {
        this.itemStack = itemStack;
    }
}
