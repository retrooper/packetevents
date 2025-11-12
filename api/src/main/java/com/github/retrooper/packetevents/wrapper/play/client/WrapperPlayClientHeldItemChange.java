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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This packet is sent when the player changes their slot selection.
 */
public class WrapperPlayClientHeldItemChange extends PacketWrapper<WrapperPlayClientHeldItemChange> {
    private int slot;

    public WrapperPlayClientHeldItemChange(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientHeldItemChange(int slot) {
        super(PacketType.Play.Client.HELD_ITEM_CHANGE);
        this.slot = slot;
    }

    @Override
    public void read() {
        this.slot = readShort();
    }

    @Override
    public void write() {
        writeShort(slot);
    }

    @Override
    public void copy(WrapperPlayClientHeldItemChange wrapper) {
        this.slot = wrapper.slot;
    }

    /**
     * The slot which the player has selected.
     *
     * @return Target slot
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Modify the slot which the player has selected.
     *
     * @param slot Target slot
     */
    public void setSlot(int slot) {
        this.slot = slot;
    }
}
