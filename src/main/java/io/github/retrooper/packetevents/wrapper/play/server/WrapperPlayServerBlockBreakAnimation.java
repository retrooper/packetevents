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

package io.github.retrooper.packetevents.wrapper.play.server;

import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.protocol.data.world.BlockPosition;
import io.github.retrooper.packetevents.protocol.packettype.PacketType;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockBreakAnimation extends PacketWrapper<WrapperPlayServerBlockBreakAnimation> {
    private int entityID;
    private BlockPosition blockPosition;
    private byte destroyStage;

    public WrapperPlayServerBlockBreakAnimation(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBlockBreakAnimation(int entityID, BlockPosition blockPosition, byte destroyStage) {
        super(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        this.entityID = entityID;
        this.blockPosition = blockPosition;
        this.destroyStage = destroyStage;
    }

    @Override
    public void readData() {
        entityID = readVarInt();
        if (serverVersion == ServerVersion.v_1_7_10) {
            int x = readInt();
            int y = readInt();
            int z = readInt();
            blockPosition = new BlockPosition(x, y, z);
        }
        else {
            blockPosition = readBlockPosition();
        }
        destroyStage = readByte();
    }

    @Override
    public void readData(WrapperPlayServerBlockBreakAnimation wrapper) {
        entityID = wrapper.entityID;
        blockPosition = wrapper.blockPosition;
        destroyStage = wrapper.destroyStage;
    }

    @Override
    public void writeData() {
        writeVarInt(entityID);
        if (serverVersion == ServerVersion.v_1_7_10) {
            writeInt(blockPosition.x);
            writeInt(blockPosition.y);
            writeInt(blockPosition.z);
        }
        else {
            writeBlockPosition(blockPosition);
        }
        writeByte(destroyStage);
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public BlockPosition getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(BlockPosition blockPosition) {
        this.blockPosition = blockPosition;
    }

    public byte getDestroyStage() {
        return destroyStage;
    }

    public void setDestroyStage(byte destroyStage) {
        this.destroyStage = destroyStage;
    }
}
