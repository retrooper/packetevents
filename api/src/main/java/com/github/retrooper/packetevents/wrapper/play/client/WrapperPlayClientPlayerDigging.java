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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientPlayerDigging extends PacketWrapper<WrapperPlayClientPlayerDigging> {
    private DiggingAction action;
    private Vector3i blockPosition;
    private BlockFace blockFace;

    public WrapperPlayClientPlayerDigging(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPlayerDigging(DiggingAction action, Vector3i blockPosition, BlockFace blockFace) {
        super(PacketType.Play.Client.PLAYER_DIGGING);
        this.action = action;
        this.blockPosition = blockPosition;
        this.blockFace = blockFace;
    }

    @Override
    public void readData() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            action = DiggingAction.getById(readVarInt());
        }
        else {
            action = DiggingAction.getById(readByte());
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            blockPosition = readBlockPosition();
        } else {
            int x = readInt();
            int y = readUnsignedByte();
            int z = readInt();
            blockPosition = new Vector3i(x, y, z);
        }
        short face = readUnsignedByte();
        blockFace = BlockFace.getBlockFaceByValue(face);
    }

    @Override
    public void readData(WrapperPlayClientPlayerDigging wrapper) {
        action = wrapper.action;
        blockPosition = wrapper.blockPosition;
        blockFace = wrapper.blockFace;
    }

    @Override
    public void writeData() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            writeVarInt(action.getId());
            writeBlockPosition(blockPosition);
        } else {
            writeByte(action.getId());
            writeInt(blockPosition.x);
            writeByte(blockPosition.y);
            writeInt(blockPosition.z);
        }
        writeByte(blockFace.getFaceValue());
    }

    public DiggingAction getAction() {
        return action;
    }

    public void setAction(DiggingAction action) {
        this.action = action;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public BlockFace getFace() {
        return blockFace;
    }

    public void setFace(BlockFace blockFace) {
        this.blockFace = blockFace;
    }
}
