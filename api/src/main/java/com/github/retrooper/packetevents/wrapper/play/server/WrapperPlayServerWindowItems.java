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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WrapperPlayServerWindowItems extends PacketWrapper<WrapperPlayServerWindowItems> {
    private int windowId;
    private int stateId;
    private List<ItemStack> items;
    private @Nullable ItemStack carriedItem;

    public WrapperPlayServerWindowItems(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerWindowItems(int windowId, int stateId, List<ItemStack> items) {
        this(windowId, stateId, items, null);
    }

    public WrapperPlayServerWindowItems(int windowId, int stateId, List<ItemStack> items, @Nullable ItemStack carriedItem) {
        super(PacketType.Play.Server.WINDOW_ITEMS);
        this.windowId = windowId;
        this.stateId = stateId;
        this.items = items;
        this.carriedItem = carriedItem;
    }

    @Override
    public void read() {
        boolean v1_17_1 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1);

        windowId = readUnsignedByte();
        if (v1_17_1) {
            stateId = readVarInt();
        }

        int count = v1_17_1 ? readVarInt() : readShort();
        items = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            items.add(readItemStack());
        }

        if (v1_17_1) {
            carriedItem = readItemStack();
        }
    }

    @Override
    public void write() {
        boolean v1_17_1 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1);

        writeByte(windowId);
        if (v1_17_1) {
            writeVarInt(stateId);
            writeVarInt(items.size());
        } else {
            writeShort(items.size());
        }
        for (ItemStack item : items) {
            writeItemStack(item);
        }
        if (v1_17_1) {
            writeItemStack(carriedItem == null ? ItemStack.EMPTY : carriedItem);
        }
    }

    @Override
    public void copy(WrapperPlayServerWindowItems wrapper) {
        windowId = wrapper.windowId;
        stateId = wrapper.stateId;
        items = wrapper.items;
        carriedItem = wrapper.carriedItem;
    }

    public int getWindowId() {
        return windowId;
    }

    public void setWindowId(int windowId) {
        this.windowId = windowId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public Optional<ItemStack> getCarriedItem() {
        return Optional.ofNullable(carriedItem);
    }

    public void setCarriedItem(@Nullable ItemStack carriedItem) {
        this.carriedItem = carriedItem;
    }
}