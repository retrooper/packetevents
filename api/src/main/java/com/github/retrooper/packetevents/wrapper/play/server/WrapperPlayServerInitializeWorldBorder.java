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

public class WrapperPlayServerInitializeWorldBorder extends PacketWrapper<WrapperPlayServerInitializeWorldBorder> {
    private double x;
    private double z;
    private double oldDiameter;
    private double newDiameter;
    private long speed;
    private int portalTeleportBoundary;
    private int warningBlocks;
    private int warningTime;

    public WrapperPlayServerInitializeWorldBorder(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerInitializeWorldBorder(double x, double z, double oldDiameter, double newDiameter, long speed, int portalTeleportBoundary, int warningBlocks, int warningTime) {
        super(PacketType.Play.Server.INITIALIZE_WORLD_BORDER);
        this.x = x;
        this.z = z;
        this.oldDiameter = oldDiameter;
        this.newDiameter = newDiameter;
        this.speed = speed;
        this.portalTeleportBoundary = portalTeleportBoundary;
        this.warningBlocks = warningBlocks;
        this.warningTime = warningTime;
    }

    @Override
    public void read() {
        this.x = readDouble();
        this.z = readDouble();
        this.oldDiameter = readDouble();
        this.newDiameter = readDouble();
        this.speed = readVarLong();
        this.portalTeleportBoundary = readVarInt();
        this.warningBlocks = readVarInt();
        this.warningTime = readVarInt();
    }

    @Override
    public void write() {
        writeDouble(x);
        writeDouble(z);
        writeDouble(oldDiameter);
        writeDouble(newDiameter);
        writeVarLong(speed);
        writeVarInt(portalTeleportBoundary);
        writeVarInt(warningBlocks);
        writeVarInt(warningTime);
    }

    @Override
    public void copy(WrapperPlayServerInitializeWorldBorder wrapper) {
        this.x = wrapper.x;
        this.z = wrapper.z;
        this.oldDiameter = wrapper.oldDiameter;
        this.newDiameter = wrapper.newDiameter;
        this.speed = wrapper.speed;
        this.portalTeleportBoundary = wrapper.portalTeleportBoundary;
        this.warningBlocks = wrapper.warningBlocks;
        this.warningTime = wrapper.warningTime;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getOldDiameter() {
        return oldDiameter;
    }

    public void setOldDiameter(double oldDiameter) {
        this.oldDiameter = oldDiameter;
    }

    public double getNewDiameter() {
        return newDiameter;
    }

    public void setNewDiameter(double newDiameter) {
        this.newDiameter = newDiameter;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public int getPortalTeleportBoundary() {
        return portalTeleportBoundary;
    }

    public void setPortalTeleportBoundary(int portalTeleportBoundary) {
        this.portalTeleportBoundary = portalTeleportBoundary;
    }

    public int getWarningBlocks() {
        return warningBlocks;
    }

    public void setWarningBlocks(int warningBlocks) {
        this.warningBlocks = warningBlocks;
    }

    public int getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(int warningTime) {
        this.warningTime = warningTime;
    }
}
