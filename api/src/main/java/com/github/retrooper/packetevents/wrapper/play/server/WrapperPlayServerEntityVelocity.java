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
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityVelocity extends PacketWrapper<WrapperPlayServerEntityVelocity> {
    private int entityID;
    private Vector3d velocity;

    public WrapperPlayServerEntityVelocity(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityVelocity(int entityID, Vector3d velocity) {
        super(PacketType.Play.Server.ENTITY_VELOCITY);
        this.entityID = entityID;
        this.velocity = velocity;
    }

    @Override
    public void read() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            entityID = readInt();
        } else {
            entityID = readVarInt();
        }
        double velX = (double) readShort() / 8000.0;
        double velY = (double) readShort() / 8000.0;
        double velZ = (double) readShort() / 8000.0;
        velocity = new Vector3d(velX, velY, velZ);
    }

    @Override
    public void write() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeInt(entityID);
        } else {
            writeVarInt(entityID);
        }
        writeShort((int) (velocity.x * 8000.0));
        writeShort((int) (velocity.y * 8000.0));
        writeShort((int) (velocity.z * 8000.0));
    }

    @Override
    public void copy(WrapperPlayServerEntityVelocity wrapper) {
        entityID = wrapper.entityID;
        velocity = wrapper.velocity;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public Vector3d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
    }
}
