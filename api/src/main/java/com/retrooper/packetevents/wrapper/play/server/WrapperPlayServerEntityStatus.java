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

package com.retrooper.packetevents.wrapper.play.server;

import com.retrooper.packetevents.event.impl.PacketSendEvent;
import com.retrooper.packetevents.protocol.packettype.PacketType;
import com.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityStatus extends PacketWrapper<WrapperPlayServerEntityStatus> {
    private int entityID;
    private int status;
    public WrapperPlayServerEntityStatus(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityStatus(int entityID, int status) {
        super(PacketType.Play.Server.ENTITY_STATUS);
        this.entityID = entityID;
        this.status = status;
    }

    @Override
    public void readData() {
        entityID = readInt();
        status = readByte();
    }

    @Override
    public void readData(WrapperPlayServerEntityStatus wrapper) {
        entityID = wrapper.entityID;
        status = wrapper.status;
    }

    @Override
    public void writeData() {
        writeInt(entityID);
        writeByte(status);
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
