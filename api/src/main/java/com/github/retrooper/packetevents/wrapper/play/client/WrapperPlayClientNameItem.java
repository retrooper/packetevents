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

public class WrapperPlayClientNameItem extends PacketWrapper<WrapperPlayClientNameItem> {
    private String itemName;

    public WrapperPlayClientNameItem(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientNameItem(String itemName) {
        super(PacketType.Play.Client.NAME_ITEM);
        this.itemName = itemName;
    }

    @Override
    public void read() {
        this.itemName = readString();
    }

    @Override
    public void write() {
        writeString(itemName);
    }

    @Override
    public void copy(WrapperPlayClientNameItem wrapper) {
        this.itemName = wrapper.itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
