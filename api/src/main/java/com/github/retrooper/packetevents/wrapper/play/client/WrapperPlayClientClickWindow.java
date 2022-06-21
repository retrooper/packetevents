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

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.window.WindowClickType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class WrapperPlayClientClickWindow extends PacketWrapper<WrapperPlayClientClickWindow> {
    private int windowId;
    private int stateId;
    private int slot;
    private int button;
    private int actionNumber;
    private WindowClickType windowClickType;
    private Map<Integer, ItemStack> slots;
    private ItemStack carriedItemStack;

    public WrapperPlayClientClickWindow(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientClickWindow(int windowId, int stateId, int slot, int button, int actionNumber, WindowClickType windowClickType,
                                        Map<Integer, ItemStack> slots, ItemStack carriedItemStack) {
        super(PacketType.Play.Client.CLICK_WINDOW);
        this.windowId = windowId;
        this.stateId = stateId;
        this.slot = slot;
        this.button = button;
        this.actionNumber = actionNumber;
        this.windowClickType = windowClickType;
        this.slots = slots;
        this.carriedItemStack = carriedItemStack;
    }

    @Override
    public void read() {
        boolean v1_17 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17);
        this.windowId = readUnsignedByte();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
            this.stateId = readVarInt();
        }
        this.slot = readShort();
        this.button = readByte();
        if (!v1_17) {
            this.actionNumber = readShort();
        }
        int clickTypeIndex = readVarInt();
        this.windowClickType = WindowClickType.getById(clickTypeIndex);
        if (v1_17) {
            Function<PacketWrapper<?>, Integer> slotReader = wrapper -> (int) wrapper.readShort();
            Function<PacketWrapper<?>, ItemStack> itemStackReader = PacketWrapper::readItemStack;
            this.slots = readMap(slotReader, itemStackReader);
        }
        this.carriedItemStack = readItemStack();
    }

    @Override
    public void write() {
        boolean v1_17 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17);
        writeByte(windowId);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
            writeVarInt(this.stateId);
        }
        writeShort(this.slot);
        writeByte(this.button);
        if (!v1_17) {
            writeShort(this.actionNumber);
        }
        writeVarInt(windowClickType.ordinal());
        if (v1_17) {
            BiConsumer<PacketWrapper<?>, Integer> keyConsumer = PacketWrapper::writeShort;
            BiConsumer<PacketWrapper<?>, ItemStack> valueConsumer = PacketWrapper::writeItemStack;
            writeMap(slots, keyConsumer, valueConsumer);
        }
        writeItemStack(carriedItemStack);
    }

    @Override
    public void copy(WrapperPlayClientClickWindow wrapper) {
        this.windowId = wrapper.windowId;
        this.stateId = wrapper.stateId;
        this.slot = wrapper.slot;
        this.button = wrapper.button;
        this.actionNumber = wrapper.actionNumber;
        this.windowClickType = wrapper.windowClickType;
        this.slots = wrapper.slots;
        this.carriedItemStack = wrapper.carriedItemStack;
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

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public int getActionNumber() {
        return actionNumber;
    }

    public void setActionNumber(int actionNumber) {
        this.actionNumber = actionNumber;
    }

    public WindowClickType getWindowClickType() {
        return windowClickType;
    }

    public void setWindowClickType(WindowClickType windowClickType) {
        this.windowClickType = windowClickType;
    }

    public Map<Integer, ItemStack> getSlots() {
        return slots;
    }

    public void setSlots(Map<Integer, ItemStack> slots) {
        this.slots = slots;
    }

    public ItemStack getCarriedItemStack() {
        return carriedItemStack;
    }

    public void setCarriedItemStack(ItemStack carriedItemStack) {
        this.carriedItemStack = carriedItemStack;
    }
}
