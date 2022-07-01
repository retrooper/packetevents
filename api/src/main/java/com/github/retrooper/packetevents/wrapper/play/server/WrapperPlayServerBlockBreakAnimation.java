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
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockBreakAnimation extends PacketWrapper<WrapperPlayServerBlockBreakAnimation> {
    private int entityID;
    private Vector3i blockPosition;
    private byte destroyStage;

    public WrapperPlayServerBlockBreakAnimation(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBlockBreakAnimation(int entityID, Vector3i blockPosition, byte destroyStage) {
        super(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        this.entityID = entityID;
        this.blockPosition = blockPosition;
        this.destroyStage = destroyStage;
    }

    @Override
    public void read() {
        entityID = readVarInt();
        blockPosition = readMultiVersional(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_8, PacketWrapper::readBlockPosition, packetWrapper -> {
            int x = packetWrapper.readInt();
            int y = packetWrapper.readInt();
            int z = packetWrapper.readInt();
            return new Vector3i(x, y, z);
        });
        destroyStage = (byte) readUnsignedByte();
    }

    @Override
    public void write() {
        writeVarInt(entityID);
        writeMultiVersional(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_8, blockPosition,
                PacketWrapper::writeBlockPosition,
                (packetWrapper, vector3i) -> {
                    packetWrapper.writeInt(blockPosition.x);
                    packetWrapper.writeInt(blockPosition.y);
                    packetWrapper.writeInt(blockPosition.z);
                });
        writeByte(destroyStage);
    }

    @Override
    public void copy(WrapperPlayServerBlockBreakAnimation wrapper) {
        entityID = wrapper.entityID;
        blockPosition = wrapper.blockPosition;
        destroyStage = wrapper.destroyStage;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public byte getDestroyStage() {
        return destroyStage;
    }

    public void setDestroyStage(byte destroyStage) {
        this.destroyStage = destroyStage;
    }
}
