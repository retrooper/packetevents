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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSetPlayerInventory extends PacketWrapper<WrapperPlayServerSetPlayerInventory> {

    private int slot;
    private ItemStack stack;

    public WrapperPlayServerSetPlayerInventory(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSetPlayerInventory(int slot, ItemStack stack) {
        super(PacketType.Play.Server.SET_PLAYER_INVENTORY);
        this.slot = slot;
        this.stack = stack;
    }

    @Override
    public void read() {
        this.slot = this.readVarInt();
        this.stack = this.readItemStack();
    }

    @Override
    public void write() {
        this.writeVarInt(this.slot);
        this.writeItemStack(this.stack);
    }

    @Override
    public void copy(WrapperPlayServerSetPlayerInventory wrapper) {
        this.slot = wrapper.slot;
        this.stack = wrapper.stack;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }
}
