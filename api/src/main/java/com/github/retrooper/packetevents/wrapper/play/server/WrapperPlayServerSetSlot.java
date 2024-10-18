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
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSetSlot extends PacketWrapper<WrapperPlayServerSetSlot> {
    private int windowID;
    private int stateID;
    private int slot;
    private ItemStack item;

    public WrapperPlayServerSetSlot(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSetSlot(int windowID, int stateID, int slot, ItemStack item) {
        super(PacketType.Play.Server.SET_SLOT);
        this.windowID = windowID;
        this.stateID = stateID;
        this.slot = slot;
        this.item = item;
    }

    @Override
    public void read() {
        this.windowID = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2) ? this.readContainerId() : this.readByte();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
            stateID = readVarInt();
        }
        slot = readShort();
        item = readItemStack();
    }

    @Override
    public void write() {
        this.writeContainerId(this.windowID);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
            writeVarInt(stateID);
        }
        writeShort(slot);
        writeItemStack(item);
    }

    @Override
    public void copy(WrapperPlayServerSetSlot wrapper) {
        windowID = wrapper.windowID;
        stateID = wrapper.stateID;
        slot = wrapper.slot;
        item = wrapper.item;
    }

    public int getWindowId() {
        return windowID;
    }

    public void setWindowId(int windowID) {
        this.windowID = windowID;
    }

    public int getStateId() {
        return stateID;
    }

    public void setStateId(int stateID) {
        this.stateID = stateID;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }
}
