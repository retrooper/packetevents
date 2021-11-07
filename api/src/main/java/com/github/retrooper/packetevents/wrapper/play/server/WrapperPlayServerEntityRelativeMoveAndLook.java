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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityRelativeMoveAndLook extends PacketWrapper<WrapperPlayServerEntityRelativeMoveAndLook> {
    private static double MODERN_DELTA_DIVISOR = 4096.0;
    private static double LEGACY_DELTA_DIVISOR = 32.0;
    private int entityID;
    private double deltaX;
    private double deltaY;
    private double deltaZ;
    private byte yaw;
    private byte pitch;
    private boolean onGround;
    public WrapperPlayServerEntityRelativeMoveAndLook(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityRelativeMoveAndLook(int entityID, double deltaX, double deltaY, double deltaZ, byte yaw, byte pitch, boolean onGround) {
        super(PacketType.Play.Server.ENTITY_RELATIVE_MOVE_AND_LOOK);
        this.entityID = entityID;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    @Override
    public void readData() {
        entityID = readVarInt();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_9)) {
            deltaX = readShort() / MODERN_DELTA_DIVISOR;
            deltaY = readShort() / MODERN_DELTA_DIVISOR;
            deltaZ = readShort() / MODERN_DELTA_DIVISOR;
        }
        else {
            deltaX = readByte() / LEGACY_DELTA_DIVISOR;
            deltaY = readByte() / LEGACY_DELTA_DIVISOR;
            deltaZ = readByte() / LEGACY_DELTA_DIVISOR;
        }
        yaw = readByte();
        pitch = readByte();
        onGround = readBoolean();
    }

    @Override
    public void readData(WrapperPlayServerEntityRelativeMoveAndLook wrapper) {
        entityID = wrapper.entityID;
        deltaX = wrapper.deltaX;
        deltaY = wrapper.deltaY;
        deltaZ = wrapper.deltaZ;
        yaw = wrapper.yaw;
        pitch = wrapper.pitch;
        onGround = wrapper.onGround;
    }

    @Override
    public void writeData() {
        writeVarInt(entityID);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_9)) {
            writeShort((short) (deltaX * MODERN_DELTA_DIVISOR));
            writeShort((short) (deltaY * MODERN_DELTA_DIVISOR));
            writeShort((short) (deltaZ * MODERN_DELTA_DIVISOR));
        }
        else {
            writeByte((byte) (deltaX * LEGACY_DELTA_DIVISOR));
            writeByte((byte) (deltaY * LEGACY_DELTA_DIVISOR));
            writeByte((byte) (deltaZ * LEGACY_DELTA_DIVISOR));
        }
        writeByte(yaw);
        writeByte(pitch);
        writeBoolean(onGround);
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public void setDeltaX(double deltaX) {
        this.deltaX = deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public void setDeltaY(double deltaY) {
        this.deltaY = deltaY;
    }

    public double getDeltaZ() {
        return deltaZ;
    }

    public void setDeltaZ(double deltaZ) {
        this.deltaZ = deltaZ;
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
