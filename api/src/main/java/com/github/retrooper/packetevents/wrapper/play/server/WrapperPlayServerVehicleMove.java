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
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerVehicleMove extends PacketWrapper<WrapperPlayServerVehicleMove> {
    private Vector3d position;
    private float yaw;
    private float pitch;

    public WrapperPlayServerVehicleMove(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerVehicleMove(Vector3d position, float yaw, float pitch) {
        super(PacketType.Play.Server.VEHICLE_MOVE);
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void read() {
        position = new Vector3d(readDouble(), readDouble(), readDouble());
        yaw = readFloat();
        pitch = readFloat();
    }

    @Override
    public void write() {
        writeDouble(position.x);
        writeDouble(position.y);
        writeDouble(position.z);
        writeFloat(yaw);
        writeFloat(pitch);
    }

    @Override
    public void copy(WrapperPlayServerVehicleMove wrapper) {
        position = wrapper.position;
        yaw = wrapper.yaw;
        pitch = wrapper.pitch;
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
