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

package io.github.retrooper.packetevents.wrapper.game.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.bukkit.inventory.ItemStack;

public class WrapperGameClientCreativeInventoryAction extends PacketWrapper<WrapperGameClientCreativeInventoryAction> {
    private int slot;
    private ItemStack itemStack;
    public WrapperGameClientCreativeInventoryAction(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperGameClientCreativeInventoryAction(ClientVersion clientVersion) {
        super(PacketType.Game.Client.CREATIVE_INVENTORY_ACTION.getPacketID(clientVersion), clientVersion);
    }

    @Override
    public void readData() {
        this.slot = readShort();
        this.itemStack = readItemStack();
    }

    @Override
    public void readData(WrapperGameClientCreativeInventoryAction wrapper) {
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

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
