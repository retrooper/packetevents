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
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientPlayerPosition extends PacketWrapper<WrapperPlayClientPlayerPosition> {
    private Vector3d position;
    private boolean onGround;

    public WrapperPlayClientPlayerPosition(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPlayerPosition(Vector3d position, boolean onGround) {
        super(PacketType.Play.Client.PLAYER_POSITION);
        this.position = position;
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
        onGround = readBoolean();
    }

    @Override
    public void readData(WrapperPlayClientPlayerPosition wrapper) {
        position = wrapper.position;
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
        writeBoolean(onGround);
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
