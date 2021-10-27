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

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.data.world.BlockFace;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientPlayerDigging extends PacketWrapper<WrapperPlayClientPlayerDigging> {
    private Action action;
    private Vector3i blockPosition;
    private BlockFace blockFace;

    public enum Action {
        START_DIGGING,
        CANCELLED_DIGGING,
        FINISHED_DIGGING,
        DROP_ITEM_STACK,
        DROP_ITEM,
        RELEASE_USE_ITEM,
        SWAP_ITEM_WITH_OFFHAND;

        public static final Action[] VALUES = values();
    }

    public WrapperPlayClientPlayerDigging(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPlayerDigging(Action action, Vector3i blockPosition, BlockFace blockFace) {
        super(PacketType.Play.Client.PLAYER_DIGGING);
        this.action = action;
        this.blockPosition = blockPosition;
        this.blockFace = blockFace;
    }

    @Override
    public void readData() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_8)) {
            action = Action.VALUES[readVarInt()];
            blockPosition = readBlockPosition();
        } else {
            action = Action.VALUES[readByte()];
            int x = readInt();
            int y = readUnsignedByte();
            int z = readInt();
            blockPosition = new Vector3i(x, y, z);
        }
        byte face = readByte();
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
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_8)) {
            writeVarInt(action.ordinal());
            writeBlockPosition(blockPosition);
        } else {
            writeByte(action.ordinal());
            writeInt(blockPosition.x);
            writeByte(blockPosition.y);
            writeInt(blockPosition.z);
        }
        writeByte(blockFace.getFaceValue());
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public BlockFace getDirection() {
        return blockFace;
    }

    public void setDirection(BlockFace blockFace) {
        this.blockFace = blockFace;
    }
}
