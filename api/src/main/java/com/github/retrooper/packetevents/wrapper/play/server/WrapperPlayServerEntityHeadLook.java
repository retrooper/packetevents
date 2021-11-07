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

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityHeadLook extends PacketWrapper<WrapperPlayServerEntityHeadLook> {
    private int entityID;
    private byte headYaw;

    public WrapperPlayServerEntityHeadLook(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityHeadLook(int entityID, byte headYaw) {
        super(PacketType.Play.Server.ENTITY_HEAD_LOOK);
        this.entityID = entityID;
        this.headYaw = headYaw;
    }

    @Override
    public void readData() {
        entityID = readVarInt();
        headYaw = readByte();
    }

    @Override
    public void readData(WrapperPlayServerEntityHeadLook wrapper) {
        entityID = wrapper.entityID;
        headYaw = wrapper.headYaw;
    }

    @Override
    public void writeData() {
        writeVarInt(entityID);
        writeByte(headYaw);
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public byte getHeadYaw() {
        return headYaw;
    }

    public void setHeadYaw(byte headYaw) {
        this.headYaw = headYaw;
    }
}
