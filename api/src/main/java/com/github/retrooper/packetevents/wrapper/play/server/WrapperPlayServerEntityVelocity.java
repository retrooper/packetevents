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

    // with larger short values there is a loss of precision and
    // re-encoding packets may cause the velocity to change
    //
    // as vanilla just casts to an int instead of properly rounding,
    // packetevents has to add a small number to the calculated velocity
    // to work around the loss of precision
    private static final double PRECISION_LOSS_FIX = 1e-11d;

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
        double velX = (double) this.readShort() / 8000d;
        double velY = (double) this.readShort() / 8000d;
        double velZ = (double) this.readShort() / 8000d;
        this.velocity = new Vector3d(velX, velY, velZ);
    }

    @Override
    public void write() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeInt(entityID);
        } else {
            writeVarInt(entityID);
        }
        this.writeShort((int) (this.velocity.x * 8000d + Math.copySign(PRECISION_LOSS_FIX, this.velocity.x)));
        this.writeShort((int) (this.velocity.y * 8000d + Math.copySign(PRECISION_LOSS_FIX, this.velocity.y)));
        this.writeShort((int) (this.velocity.z * 8000d + Math.copySign(PRECISION_LOSS_FIX, this.velocity.z)));
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
