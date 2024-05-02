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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSpawnWeatherEntity extends PacketWrapper<WrapperPlayServerSpawnWeatherEntity> {
    private int entityId;
    private byte type;
    private double x;
    private double y;
    private double z;

    public WrapperPlayServerSpawnWeatherEntity(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSpawnWeatherEntity(int entityId, byte type, double x, double y, double z) {
        super(PacketType.Play.Server.SPAWN_WEATHER_ENTITY);
        this.entityId = entityId;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void read() {
        this.entityId = readVarInt();
        this.type = readByte();
        this.x = readDouble();
        this.y = readDouble();
        this.z = readDouble();
    }

    @Override
    public void write() {
        writeVarInt(entityId);
        writeByte(type);
        writeDouble(this.x);
        writeDouble(this.y);
        writeDouble(this.z);
    }

    @Override
    public void copy(WrapperPlayServerSpawnWeatherEntity wrapper) {
        this.entityId = wrapper.entityId;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}