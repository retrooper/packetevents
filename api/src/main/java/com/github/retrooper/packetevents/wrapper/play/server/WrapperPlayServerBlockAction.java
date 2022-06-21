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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.MultiVersion;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockAction extends PacketWrapper<WrapperPlayServerBlockAction> {
    private Vector3i blockPosition;
    private int actionId;
    private int actionData;
    private int blockTypeId;

    public WrapperPlayServerBlockAction(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBlockAction(Vector3i blockPosition, int actionId, int actionParam, int blockTypeId) {
        super(PacketType.Play.Server.BLOCK_ACTION);
        this.blockPosition = blockPosition;
        this.actionId = actionId;
        this.actionData = actionParam;
        this.blockTypeId = blockTypeId;
    }

    @Override
    public void read() {
        this.blockPosition = readMultiVersional(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_8,
                PacketWrapper::readBlockPosition,
                packetWrapper -> {
                    int x = packetWrapper.readInt();
                    int y = packetWrapper.readShort();
                    int z = packetWrapper.readInt();
                    return new Vector3i(x, y, z);
                });
        this.actionId = readUnsignedByte();
        this.actionData = readUnsignedByte();
        this.blockTypeId = readVarInt();
    }

    @Override
    public void write() {
        writeMultiVersional(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_8, blockPosition,
                PacketWrapper::writeBlockPosition,
                (packetWrapper, vector3i) -> {
                    packetWrapper.writeInt(blockPosition.x);
                    packetWrapper.writeShort(blockPosition.y);
                    packetWrapper.writeInt(blockPosition.z);
                });
        writeByte(actionId);
        writeByte(actionData);
        writeVarInt(blockTypeId);
    }

    @Override
    public void copy(WrapperPlayServerBlockAction wrapper) {
        this.blockPosition = wrapper.blockPosition;
        this.actionId = wrapper.actionId;
        this.actionData = wrapper.actionData;
        this.blockTypeId = wrapper.blockTypeId;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionID) {
        this.actionId = actionID;
    }

    public int getActionData() {
        return actionData;
    }

    public void setActionData(int actionData) {
        this.actionData = actionData;
    }

    public int getBlockTypeId() {
        return blockTypeId;
    }

    public void setBlockTypeId(int blockTypeID) {
        this.blockTypeId = blockTypeID;
    }

    public WrappedBlockState getBlockType() {
        return WrappedBlockState.getByGlobalId(serverVersion.toClientVersion(), blockTypeId);
    }

    public void setBlockType(WrappedBlockState blockType) {
        this.blockTypeId = blockType.getGlobalId();
    }
}
