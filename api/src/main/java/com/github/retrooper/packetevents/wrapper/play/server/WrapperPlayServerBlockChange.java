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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockChange extends PacketWrapper<WrapperPlayServerBlockChange> {
    private Vector3i blockPosition;
    private int blockID;

    public WrapperPlayServerBlockChange(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBlockChange(Vector3i blockPosition, int blockID) {
        super(PacketType.Play.Server.BLOCK_CHANGE);
        this.blockPosition = blockPosition;
        this.blockID = blockID;
    }

    @Override
    public void read() {
        blockPosition = readBlockPosition();
        blockID = readVarInt();
    }

    @Override
    public void write() {
        writeBlockPosition(blockPosition);
        writeVarInt(blockID);
    }

    @Override
    public void copy(WrapperPlayServerBlockChange wrapper) {
        blockPosition = wrapper.blockPosition;
        blockID = wrapper.blockID;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public int getBlockId() {
        return blockID;
    }

    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }

    public WrappedBlockState getBlockState() {
        return WrappedBlockState.getByGlobalId(serverVersion.toClientVersion(), blockID);
    }

    public void setBlockState(WrappedBlockState blockState) {
        this.blockID = blockState.getGlobalId();
    }
}
