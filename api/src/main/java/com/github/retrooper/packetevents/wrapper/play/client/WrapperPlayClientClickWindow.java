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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WrapperPlayClientClickWindow extends PacketWrapper<WrapperPlayClientClickWindow> {
    private int windowID;
    private Optional<Integer> stateID;
    private int slot;
    private int button;
    private Optional<Integer> actionNumber;
    private WindowClickType windowClickType;
    private Optional<Map<Integer, ItemStack>> slots;
    private ItemStack carriedItemStack;

    public WrapperPlayClientClickWindow(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientClickWindow(int windowID, Optional<Integer> stateID, int slot, int button, Optional<Integer> actionNumber, WindowClickType windowClickType,
                                        Optional<Map<Integer, ItemStack>> slots, ItemStack carriedItemStack) {
        super(PacketType.Play.Client.CLICK_WINDOW);
        this.windowID = windowID;
        this.stateID = stateID;
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
        this.windowID = this.readContainerId();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
            this.stateID = Optional.of(readVarInt());
        } else {
            this.stateID = Optional.empty();
        }
        this.slot = readShort();
        this.button = readByte();
        if (!v1_17) {
            this.actionNumber = Optional.of((int) readShort());
        } else {
            this.actionNumber = Optional.empty();
        }
        int clickTypeIndex = readVarInt();
        this.windowClickType = WindowClickType.VALUES[clickTypeIndex];
        if (v1_17) {
            this.slots = Optional.of(readMap(
                    packetWrapper -> Math.toIntExact(packetWrapper.readShort()),
                    PacketWrapper::readItemStack
            ));
        } else {
            this.slots = Optional.empty();
        }
        this.carriedItemStack = readItemStack();
    }

    @Override
    public void copy(WrapperPlayClientClickWindow wrapper) {
        this.windowID = wrapper.windowID;
        this.stateID = wrapper.stateID;
        this.slot = wrapper.slot;
        this.button = wrapper.button;
        this.actionNumber = wrapper.actionNumber;
        this.windowClickType = wrapper.windowClickType;
        this.slots = wrapper.slots;
        this.carriedItemStack = wrapper.carriedItemStack;
    }

    @Override
    public void write() {
        boolean v1_17 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17);
        this.writeContainerId(this.windowID);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
            writeVarInt(this.stateID.orElse(-1));
        }
        writeShort(this.slot);
        writeByte(this.button);
        if (!v1_17) {
            writeShort(this.actionNumber.orElse(-1));
        }
        writeVarInt(windowClickType.ordinal());
        if (v1_17) {
            writeMap(slots.orElse(new HashMap<>()), PacketWrapper::writeShort, PacketWrapper::writeItemStack);
        }
        writeItemStack(carriedItemStack);
    }

    public int getWindowId() {
        return windowID;
    }

    public void setWindowId(int windowID) {
        this.windowID = windowID;
    }

    public Optional<Integer> getStateId() {
        return stateID;
    }

    public void setStateID(Optional<Integer> stateID) {
        this.stateID = stateID;
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

    public Optional<Integer> getActionNumber() {
        return actionNumber;
    }

    public void setActionNumber(int button) {
        this.actionNumber = Optional.of(button);
    }

    public WindowClickType getWindowClickType() {
        return windowClickType;
    }

    public void setWindowClickType(WindowClickType windowClickType) {
        this.windowClickType = windowClickType;
    }

    public Optional<Map<Integer, ItemStack>> getSlots() {
        return slots;
    }

    public void setSlots(Optional<Map<Integer, ItemStack>> slots) {
        this.slots = slots;
    }

    public ItemStack getCarriedItemStack() {
        return carriedItemStack;
    }

    public void setCarriedItemStack(ItemStack carriedItemStack) {
        this.carriedItemStack = carriedItemStack;
    }

    public enum WindowClickType {
        PICKUP, QUICK_MOVE, SWAP, CLONE, THROW, QUICK_CRAFT, PICKUP_ALL;

        public static final WindowClickType[] VALUES = values();
    }
}
