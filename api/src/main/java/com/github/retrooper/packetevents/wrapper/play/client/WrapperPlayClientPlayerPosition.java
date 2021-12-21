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

public class WrapperPlayClientPlayerPosition extends WrapperPlayClientPlayerFlying<WrapperPlayClientPlayerPosition> {
    private Vector3d position;

    public WrapperPlayClientPlayerPosition(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPlayerPosition(Vector3d position, boolean onGround) {
        super(PacketType.Play.Client.PLAYER_POSITION, onGround);
        this.position = position;
    }

    @Override
    public void readData() {
        double x = readDouble();
        double y = readDouble();
        if (serverVersion == ServerVersion.V_1_7_10) {
            //Can be ignored, cause stance = (y + 1.62)
            double stance = readDouble();
        }
        double z = readDouble();
        position = new Vector3d(x, y, z);
        super.readData();
    }

    @Override
    public void readData(WrapperPlayClientPlayerPosition wrapper) {
        position = wrapper.position;
        super.readData(wrapper);
    }

    @Override
    public void writeData() {
        writeDouble(position.x);
        writeDouble(position.y);
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeDouble(position.y + 1.62);
        }
        writeDouble(position.z);
        super.writeData();
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }
}
