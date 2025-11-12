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

public class WrapperPlayServerWorldBorderCenter extends PacketWrapper<WrapperPlayServerWorldBorderCenter> {
    private double x;
    private double z;

    public WrapperPlayServerWorldBorderCenter(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerWorldBorderCenter(double x, double z) {
        super(PacketType.Play.Server.WORLD_BORDER_CENTER);
        this.x = x;
        this.z = z;
    }

    @Override
    public void read() {
        this.x = readDouble();
        this.z = readDouble();
    }

    @Override
    public void write() {
        writeDouble(this.x);
        writeDouble(this.z);
    }

    @Override
    public void copy(WrapperPlayServerWorldBorderCenter wrapper) {
        this.x = wrapper.x;
        this.z = wrapper.z;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
