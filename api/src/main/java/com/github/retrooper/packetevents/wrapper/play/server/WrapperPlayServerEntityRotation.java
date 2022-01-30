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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityRotation extends PacketWrapper<WrapperPlayServerEntityRotation> {
    private int entityID;
    private byte yaw;
    private byte pitch;
    private boolean onGround;
    public WrapperPlayServerEntityRotation(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityRotation(int entityID, byte yaw, byte pitch, boolean onGround) {
        super(PacketType.Play.Server.ENTITY_ROTATION);
        this.entityID = entityID;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    @Override
    public void readData() {
        entityID = readVarInt();
        yaw = readByte();
        pitch = readByte();
        onGround = readBoolean();
    }

    @Override
    public void readData(WrapperPlayServerEntityRotation wrapper) {
        entityID = wrapper.entityID;
        yaw = wrapper.yaw;
        pitch = wrapper.pitch;
        onGround = wrapper.onGround;
    }

    @Override
    public void writeData() {
        writeVarInt(entityID);
        writeByte(yaw);
        writeByte(pitch);
        writeBoolean(onGround);
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public byte getYaw() {
        return yaw;
    }

    public void setYaw(byte yaw) {
        this.yaw = yaw;
    }

    public byte getPitch() {
        return pitch;
    }

    public void setPitch(byte pitch) {
        this.pitch = pitch;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
