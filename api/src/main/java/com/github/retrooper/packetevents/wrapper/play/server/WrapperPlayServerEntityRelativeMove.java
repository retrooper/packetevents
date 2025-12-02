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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityRelativeMove extends PacketWrapper<WrapperPlayServerEntityRelativeMove> {
    //(Short.MAX_VALUE + 1) / 8.0
    private static double MODERN_DELTA_DIVISOR = 4096.0;
    //(Byte.MAX_VALUE + 1) / 4.0
    private static double LEGACY_DELTA_DIVISOR = 32.0;
    private int entityID;
    private double deltaX;
    private double deltaY;
    private double deltaZ;
    private boolean onGround;

    public WrapperPlayServerEntityRelativeMove(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityRelativeMove(int entityID, double deltaX, double deltaY, double deltaZ, boolean onGround) {
        super(PacketType.Play.Server.ENTITY_RELATIVE_MOVE);
        this.entityID = entityID;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.onGround = onGround;
    }

    @Override
    public void read() {
        entityID = readVarInt();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            deltaX = readShort() / MODERN_DELTA_DIVISOR;
            deltaY = readShort() / MODERN_DELTA_DIVISOR;
            deltaZ = readShort() / MODERN_DELTA_DIVISOR;
        }
        else {
            deltaX = readByte() / LEGACY_DELTA_DIVISOR;
            deltaY = readByte() / LEGACY_DELTA_DIVISOR;
            deltaZ = readByte() / LEGACY_DELTA_DIVISOR;
        }
        onGround = readBoolean();
    }

    @Override
    public void write() {
        writeVarInt(entityID);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            writeShort((short) (deltaX * MODERN_DELTA_DIVISOR));
            writeShort((short) (deltaY * MODERN_DELTA_DIVISOR));
            writeShort((short) (deltaZ * MODERN_DELTA_DIVISOR));
        }
        else {
            writeByte((byte) (deltaX * LEGACY_DELTA_DIVISOR));
            writeByte((byte) (deltaY * LEGACY_DELTA_DIVISOR));
            writeByte((byte) (deltaZ * LEGACY_DELTA_DIVISOR));
        }
        writeBoolean(onGround);
    }

    @Override
    public void copy(WrapperPlayServerEntityRelativeMove wrapper) {
        entityID = wrapper.entityID;
        deltaX = wrapper.deltaX;
        deltaY = wrapper.deltaY;
        deltaZ = wrapper.deltaZ;
        onGround = wrapper.onGround;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
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

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
