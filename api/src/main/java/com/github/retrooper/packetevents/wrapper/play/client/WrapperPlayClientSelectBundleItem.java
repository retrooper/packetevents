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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientSelectBundleItem extends PacketWrapper<WrapperPlayClientSelectBundleItem> {

    private int slotId;
    private int selectedItemIndex;

    public WrapperPlayClientSelectBundleItem(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientSelectBundleItem(int slotId, int selectedItemIndex) {
        super(PacketType.Play.Client.SELECT_BUNDLE_ITEM);
        this.slotId = slotId;
        this.selectedItemIndex = selectedItemIndex;
    }

    @Override
    public void read() {
        this.slotId = this.readVarInt();
        this.selectedItemIndex = this.readVarInt();
    }

    @Override
    public void write() {
        this.writeVarInt(this.slotId);
        this.writeVarInt(this.selectedItemIndex);
    }

    @Override
    public void copy(WrapperPlayClientSelectBundleItem wrapper) {
        this.slotId = wrapper.slotId;
        this.selectedItemIndex = wrapper.selectedItemIndex;
    }

    public int getSlotId() {
        return this.slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public int getSelectedItemIndex() {
        return this.selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }
}
