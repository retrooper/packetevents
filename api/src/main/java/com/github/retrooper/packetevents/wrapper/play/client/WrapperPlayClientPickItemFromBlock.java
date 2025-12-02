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
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * Added with 1.21.4
 */
public class WrapperPlayClientPickItemFromBlock extends PacketWrapper<WrapperPlayClientPickItemFromBlock> {

    private Vector3i blockPos;
    private boolean includeData;

    public WrapperPlayClientPickItemFromBlock(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPickItemFromBlock(Vector3i blockPos, boolean includeData) {
        super(PacketType.Play.Client.PICK_ITEM_FROM_BLOCK);
        this.blockPos = blockPos;
        this.includeData = includeData;
    }

    @Override
    public void read() {
        this.blockPos = this.readBlockPosition();
        this.includeData = this.readBoolean();
    }

    @Override
    public void write() {
        this.writeBlockPosition(this.blockPos);
        this.writeBoolean(this.includeData);
    }

    @Override
    public void copy(WrapperPlayClientPickItemFromBlock wrapper) {
        this.blockPos = wrapper.blockPos;
        this.includeData = wrapper.includeData;
    }

    public Vector3i getBlockPos() {
        return this.blockPos;
    }

    public void setBlockPos(Vector3i blockPos) {
        this.blockPos = blockPos;
    }

    public boolean isIncludeData() {
        return this.includeData;
    }

    public void setIncludeData(boolean includeData) {
        this.includeData = includeData;
    }
}
