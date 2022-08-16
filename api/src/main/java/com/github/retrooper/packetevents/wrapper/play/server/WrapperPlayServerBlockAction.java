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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockAction extends PacketWrapper<WrapperPlayServerBlockAction> {
    private Vector3i blockPosition;
    private int actionID;
    private int actionData;
    private int blockTypeID;

    public WrapperPlayServerBlockAction(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBlockAction(Vector3i blockPosition, int actionID, int actionParam, int blockTypeID) {
        super(PacketType.Play.Server.BLOCK_ACTION);
        this.blockPosition = blockPosition;
        this.actionID = actionID;
        this.actionData = actionParam;
        this.blockTypeID = blockTypeID;
    }

    @Override
    public void read() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            int x = readInt();
            int y = readShort();
            int z = readInt();
            blockPosition = new Vector3i(x, y, z);
        } else {
            this.blockPosition = readBlockPosition();
        }
        this.actionID = readUnsignedByte();
        this.actionData = readUnsignedByte();
        this.blockTypeID = readVarInt();
    }

    @Override
    public void write() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeInt(blockPosition.x);
            writeShort(blockPosition.y);
            writeInt(blockPosition.z);
        } else {
            writeBlockPosition(blockPosition);
        }
        writeByte(actionID);
        writeByte(actionData);
        writeVarInt(blockTypeID);
    }

    @Override
    public void copy(WrapperPlayServerBlockAction wrapper) {
        this.blockPosition = wrapper.blockPosition;
        this.actionID = wrapper.actionID;
        this.actionData = wrapper.actionData;
        this.blockTypeID = wrapper.blockTypeID;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public int getActionId() {
        return actionID;
    }

    public void setActionId(int actionID) {
        this.actionID = actionID;
    }

    public int getActionData() {
        return actionData;
    }

    public void setActionData(int actionData) {
        this.actionData = actionData;
    }

    public int getBlockTypeId() {
        return blockTypeID;
    }

    public void setBlockTypeId(int blockTypeID) {
        this.blockTypeID = blockTypeID;
    }

    public WrappedBlockState getBlockType() {
        return WrappedBlockState.getByGlobalId(serverVersion.toClientVersion(), blockTypeID);
    }

    public void setBlockType(WrappedBlockState blockType) {
        this.blockTypeID = blockType.getGlobalId();
    }
}
