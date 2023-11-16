/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2023 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientSlotStateChange extends PacketWrapper<WrapperPlayClientSlotStateChange> {

    private int slot;
    private int windowId;
    private boolean state;

    public WrapperPlayClientSlotStateChange(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientSlotStateChange(int slot, int windowId, boolean state) {
        super(PacketType.Play.Client.SLOT_STATE_CHANGE);
        this.slot = slot;
        this.windowId = windowId;
        this.state = state;
    }

    @Override
    public void read() {
        this.slot = this.readVarInt();
        this.windowId = this.readVarInt();
        this.state = this.readBoolean();
    }

    @Override
    public void write() {
        this.writeVarInt(this.slot);
        this.writeVarInt(this.windowId);
        this.writeBoolean(this.state);
    }

    @Override
    public void copy(WrapperPlayClientSlotStateChange wrapper) {
        this.slot = wrapper.slot;
        this.windowId = wrapper.windowId;
        this.state = wrapper.state;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public void setWindowId(int windowId) {
        this.windowId = windowId;
    }

    public boolean isState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
