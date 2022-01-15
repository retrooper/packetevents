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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientPlayerPositionAndRotation extends PacketWrapper<WrapperPlayClientPlayerPositionAndRotation> {
    private Vector3d position;
    private float yaw;
    private float pitch;
    private boolean onGround;

    public WrapperPlayClientPlayerPositionAndRotation(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPlayerPositionAndRotation(Vector3d position, float yaw, float pitch, boolean onGround) {
        super(PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION);
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public WrapperPlayClientPlayerPositionAndRotation(Location location, boolean onGround) {
        super(PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION);
        this.position = location.getPosition();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        this.onGround = onGround;
    }

    @Override
    public void readData() {
        double x = readDouble();
        double y = readDouble();
        if (serverVersion == ServerVersion.V_1_7_10) {
            //headY = (y + 1.62), this is kind of constant
            double headY = readDouble();
        }
        double z = readDouble();
        position = new Vector3d(x, y, z);
        yaw = readFloat();
        pitch = readFloat();
        onGround = readBoolean();
    }

    @Override
    public void readData(WrapperPlayClientPlayerPositionAndRotation wrapper) {
        position = wrapper.position;
        yaw = wrapper.yaw;
        pitch = wrapper.pitch;
        onGround = wrapper.onGround;
    }

    @Override
    public void writeData() {
        writeDouble(position.x);
        writeDouble(position.y);
        if (serverVersion == ServerVersion.V_1_7_10) {
            //Writing head Y
            writeDouble(position.y + 1.62);
        }
        writeDouble(position.z);
        writeFloat(yaw);
        writeFloat(pitch);
        writeBoolean(onGround);
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

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
