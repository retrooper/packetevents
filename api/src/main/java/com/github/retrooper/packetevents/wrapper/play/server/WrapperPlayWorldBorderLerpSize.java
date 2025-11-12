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

public class WrapperPlayWorldBorderLerpSize extends PacketWrapper<WrapperPlayWorldBorderLerpSize> {
    private double oldDiameter;
    private double newDiameter;
    private long speed;

    public WrapperPlayWorldBorderLerpSize(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayWorldBorderLerpSize(double oldDiameter, double newDiameter, long speed) {
        super(PacketType.Play.Server.WORLD_BORDER_LERP_SIZE);
        this.oldDiameter = oldDiameter;
        this.newDiameter = newDiameter;
        this.speed = speed;
    }

    @Override
    public void read() {
        oldDiameter = readDouble();
        newDiameter = readDouble();
        speed = readVarLong();
    }

    @Override
    public void write() {
        writeDouble(oldDiameter);
        writeDouble(newDiameter);
        writeVarLong(speed);
    }

    @Override
    public void copy(WrapperPlayWorldBorderLerpSize packet) {
        this.oldDiameter = packet.oldDiameter;
        this.newDiameter = packet.newDiameter;
        this.speed = packet.speed;
    }

    public double getOldDiameter() {
        return oldDiameter;
    }

    public double getNewDiameter() {
        return newDiameter;
    }

    public long getSpeed() {
        return speed;
    }

    public void setOldDiameter(double oldDiameter) {
        this.oldDiameter = oldDiameter;
    }

    public void setNewDiameter(double newDiameter) {
        this.newDiameter = newDiameter;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }
}
