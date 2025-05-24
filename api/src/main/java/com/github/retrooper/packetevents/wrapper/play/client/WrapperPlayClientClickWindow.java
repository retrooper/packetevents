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
import com.github.retrooper.packetevents.protocol.item.HashedStack;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class WrapperPlayClientClickWindow extends PacketWrapper<WrapperPlayClientClickWindow> {

    private static final int MAX_SLOT_COUNT = 128;

    private int windowID;
    /**
     * Added with 1.17.1
     */
    private @Nullable Integer stateID;
    private int slot;
    private int button;
    /**
     * Removed with 1.17
     */
    private @Nullable Integer actionNumber;
    private WindowClickType windowClickType;
    /**
     * Added with 1.17; Removed with 1.21.5, replaced with {@link #hashedSlots}
     */
    private @Nullable Map<Integer, ItemStack> slots;
    /**
     * Added with 1.21.5
     */
    private @Nullable Map<Integer, Optional<HashedStack>> hashedSlots;
    /**
     * Removed with 1.21.5, replaced with {@link #carriedHashedStack}
     */
    private @Nullable ItemStack carriedItemStack;
    /**
     * Added with 1.21.5
     */
    private @Nullable Optional<HashedStack> carriedHashedStack;

    public WrapperPlayClientClickWindow(PacketReceiveEvent event) {
        super(event);
    }

    /**
     * Changed in 1.21.5, ItemStack serialization has been replaced with serialization of {@link HashedStack}
     */
    @ApiStatus.Obsolete
    public WrapperPlayClientClickWindow(
            int windowID, Optional<Integer> stateID, int slot, int button, Optional<Integer> actionNumber,
            WindowClickType windowClickType, Optional<Map<Integer, ItemStack>> slots, ItemStack carriedItemStack
    ) {
        super(PacketType.Play.Client.CLICK_WINDOW);
        this.windowID = windowID;
        this.stateID = stateID.orElse(null);
        this.slot = slot;
        this.button = button;
        this.actionNumber = actionNumber.orElse(null);
        this.windowClickType = windowClickType;
        this.slots = slots.orElse(null);
        this.carriedItemStack = carriedItemStack;
    }

    public WrapperPlayClientClickWindow(
            int windowID, @Nullable Integer stateID, int slot,
            int button, WindowClickType windowClickType,
            @Nullable Map<Integer, Optional<HashedStack>> hashedSlots,
            @Nullable Optional<HashedStack> carriedHashedStack
    ) {
        super(PacketType.Play.Client.CLICK_WINDOW);
        this.windowID = windowID;
        this.stateID = stateID;
        this.slot = slot;
        this.button = button;
        this.windowClickType = windowClickType;
        this.hashedSlots = hashedSlots;
        this.carriedHashedStack = carriedHashedStack;
    }

    @Override
    public void read() {
        boolean v1_17 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17);
        this.windowID = this.readContainerId();
        this.stateID = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1) ? this.readVarInt() : null;
        this.slot = this.readShort();
        this.button = this.readByte();
        this.actionNumber = v1_17 ? null : (int) this.readShort();
        this.windowClickType = WindowClickType.getById(this.readVarInt());

        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
            this.hashedSlots = this.readMap(
                    ew -> Math.toIntExact(ew.readShort()),
                    HashedStack::read,
                    MAX_SLOT_COUNT
            );
            this.carriedHashedStack = HashedStack.read(this);
        } else {
            if (v1_17) {
                this.slots = this.readMap(
                        packetWrapper -> Math.toIntExact(packetWrapper.readShort()),
                        PacketWrapper::readItemStack
                );
            }
            this.carriedItemStack = this.readItemStack();
        }
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
        this.hashedSlots = wrapper.hashedSlots;
        this.carriedItemStack = wrapper.carriedItemStack;
        this.carriedHashedStack = wrapper.carriedHashedStack;
    }

    @Override
    public void write() {
        boolean v1_17 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17);
        this.writeContainerId(this.windowID);
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
            this.writeVarInt(this.stateID != null ? this.stateID : -1);
        }
        this.writeShort(this.slot);
        this.writeByte(this.button);
        if (!v1_17) {
            this.writeShort(this.actionNumber != null ? this.actionNumber : -1);
        }
        this.writeVarInt(this.windowClickType.ordinal());
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
            this.writeMap(this.hashedSlots != null ? this.hashedSlots : Collections.emptyMap(),
                    PacketWrapper::writeShort, HashedStack::write);
            HashedStack.write(this, this.carriedHashedStack);
        } else {
            if (v1_17) {
                this.writeMap(this.slots != null ? this.slots : Collections.emptyMap(),
                        PacketWrapper::writeShort, PacketWrapper::writeItemStack);
            }
            this.writeItemStack(this.carriedItemStack);
        }
    }

    public int getWindowId() {
        return this.windowID;
    }

    public void setWindowId(int windowID) {
        this.windowID = windowID;
    }

    /**
     * Added with 1.17.1, not actually optional
     */
    public Optional<Integer> getStateId() {
        return Optional.ofNullable(this.stateID);
    }

    /**
     * Added with 1.17.1, not actually optional
     */
    public void setStateID(Optional<Integer> stateID) {
        this.stateID = stateID.orElse(null);
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

    /**
     * Removed with 1.17
     */
    @ApiStatus.Obsolete
    public Optional<Integer> getActionNumber() {
        return Optional.ofNullable(this.actionNumber);
    }

    /**
     * Removed with 1.17
     */
    @ApiStatus.Obsolete
    public void setActionNumber(int button) {
        this.actionNumber = button;
    }

    public WindowClickType getWindowClickType() {
        return windowClickType;
    }

    public void setWindowClickType(WindowClickType windowClickType) {
        this.windowClickType = windowClickType;
    }

    /**
     * Added with 1.17, not actually optional; Removed with 1.21.5, replaced with {@link #getHashedSlots()}
     */
    public Optional<Map<Integer, ItemStack>> getSlots() {
        return Optional.ofNullable(this.slots);
    }

    /**
     * Added with 1.17, not actually optional; Removed with 1.21.5, replaced with {@link #setHashedSlots(Map)}
     */
    public void setSlots(Optional<Map<Integer, ItemStack>> slots) {
        this.slots = slots.orElse(null);
    }

    /**
     * Added with 1.21.5
     */
    public Map<Integer, Optional<HashedStack>> getHashedSlots() {
        return this.hashedSlots;
    }

    /**
     * Added with 1.21.5
     */
    public void setHashedSlots(Map<Integer, Optional<HashedStack>> hashedSlots) {
        this.hashedSlots = hashedSlots;
    }

    /**
     * Removed with 1.21.5, replaced with {@link #getCarriedHashedStack()}
     */
    public ItemStack getCarriedItemStack() {
        if (this.carriedItemStack == null && this.carriedHashedStack != null) {
            return this.carriedHashedStack
                    .map(HashedStack::asItemStack)
                    .orElse(ItemStack.EMPTY);
        }
        return this.carriedItemStack;
    }

    /**
     * Removed with 1.21.5, replaced with {@link #setCarriedHashedStack(Optional)}
     */
    public void setCarriedItemStack(ItemStack carriedItemStack) {
        this.carriedItemStack = carriedItemStack;
        this.carriedHashedStack = HashedStack.fromItemStack(carriedItemStack);
    }

    /**
     * Added with 1.21.5
     */
    public Optional<HashedStack> getCarriedHashedStack() {
        return this.carriedHashedStack;
    }

    /**
     * Added with 1.21.5
     */
    public void setCarriedHashedStack(Optional<HashedStack> carriedHashedStack) {
        this.carriedHashedStack = carriedHashedStack;
    }

    public enum WindowClickType {
        PICKUP,
        QUICK_MOVE,
        SWAP,
        CLONE,
        THROW,
        QUICK_CRAFT,
        PICKUP_ALL,
        UNKNOWN;

        public static final WindowClickType[] VALUES = values();

        public static WindowClickType getById(int id) {
            // We subtract by 1 as unknown is not a valid choice.
            if (id < 0 || id >= (VALUES.length - 1)) {
                return UNKNOWN;
            }

            return VALUES[id];
        }
    }
}
