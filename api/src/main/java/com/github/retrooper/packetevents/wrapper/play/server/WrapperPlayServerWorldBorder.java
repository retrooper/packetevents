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

// Thanks MCProtocolLib, this is taken entirely from them
public class WrapperPlayServerWorldBorder extends PacketWrapper<WrapperPlayServerWorldBorder> {
    private WorldBorderAction action;

    private double radius;

    private double oldRadius;
    private double newRadius;
    private long speed;

    private double centerX;
    private double centerZ;

    private int portalTeleportBoundary;

    private int warningTime;

    private int warningBlocks;

    public WrapperPlayServerWorldBorder(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerWorldBorder(double radius) {
        super(PacketType.Play.Server.WORLD_BORDER);
        this.action = WorldBorderAction.SET_SIZE;
        this.radius = radius;
    }

    public WrapperPlayServerWorldBorder(double oldRadius, double newRadius, long speed) {
        super(PacketType.Play.Server.WORLD_BORDER);
        this.action = WorldBorderAction.LERP_SIZE;
        this.oldRadius = oldRadius;
        this.newRadius = newRadius;
        this.speed = speed;
    }

    public WrapperPlayServerWorldBorder(double centerX, double centerZ) {
        super(PacketType.Play.Server.WORLD_BORDER);
        this.action = WorldBorderAction.SET_CENTER;
        this.centerX = centerX;
        this.centerZ = centerZ;
    }

    public WrapperPlayServerWorldBorder(double centerX, double centerZ, double oldRadius, double newRadius, long speed, int portalTeleportBoundary, int warningTime, int warningBlocks) {
        super(PacketType.Play.Server.WORLD_BORDER);
        this.action = WorldBorderAction.INITIALIZE;
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.oldRadius = oldRadius;
        this.newRadius = newRadius;
        this.speed = speed;
        this.portalTeleportBoundary = portalTeleportBoundary;
        this.warningTime = warningTime;
        this.warningBlocks = warningBlocks;
    }

    public WrapperPlayServerWorldBorder(int warning, boolean time) {
        super(PacketType.Play.Server.WORLD_BORDER);
        if (time) {
            this.action = WorldBorderAction.SET_WARNING_TIME;
            this.warningTime = warning;
        } else {
            this.action = WorldBorderAction.SET_WARNING_BLOCKS;
            this.warningBlocks = warning;
        }
    }

    @Override
    public void read() {
        this.action = WorldBorderAction.values()[this.readVarInt()];
        if (this.action == WorldBorderAction.SET_SIZE) {
            this.radius = readDouble();
        } else if (this.action == WorldBorderAction.LERP_SIZE) {
            this.oldRadius = readDouble();
            this.newRadius = readDouble();
            this.speed = readVarLong();
        } else if (this.action == WorldBorderAction.SET_CENTER) {
            this.centerX = readDouble();
            this.centerZ = readDouble();
        } else if (this.action == WorldBorderAction.INITIALIZE) {
            this.centerX = readDouble();
            this.centerZ = readDouble();
            this.oldRadius = readDouble();
            this.newRadius = readDouble();
            this.speed = readVarLong();
            this.portalTeleportBoundary = readVarInt();
            this.warningTime = readVarInt();
            this.warningBlocks = readVarInt();
        } else if (this.action == WorldBorderAction.SET_WARNING_TIME) {
            this.warningTime = readVarInt();
        } else if (this.action == WorldBorderAction.SET_WARNING_BLOCKS) {
            this.warningBlocks = readVarInt();
        }
    }

    @Override
    public void write() {
        writeVarInt(this.action.ordinal());
        if (this.action == WorldBorderAction.SET_SIZE) {
            writeDouble(this.radius);
        } else if (this.action == WorldBorderAction.LERP_SIZE) {
            writeDouble(this.oldRadius);
            writeDouble(this.newRadius);
            writeVarLong(this.speed);
        } else if (this.action == WorldBorderAction.SET_CENTER) {
            writeDouble(this.centerX);
            writeDouble(this.centerZ);
        } else if (this.action == WorldBorderAction.INITIALIZE) {
            writeDouble(this.centerX);
            writeDouble(this.centerZ);
            writeDouble(this.oldRadius);
            writeDouble(this.newRadius);
            writeVarLong(this.speed);
            writeVarInt(this.portalTeleportBoundary);
            writeVarInt(this.warningTime);
            writeVarInt(this.warningBlocks);
        } else if (this.action == WorldBorderAction.SET_WARNING_TIME) {
            writeVarInt(this.warningTime);
        } else if (this.action == WorldBorderAction.SET_WARNING_BLOCKS) {
            writeVarInt(this.warningBlocks);
        }
    }

    @Override
    public void copy(WrapperPlayServerWorldBorder wrapper) {
        this.action = wrapper.action;
        this.radius = wrapper.radius;
        this.oldRadius = wrapper.oldRadius;
        this.newRadius = wrapper.newRadius;
        this.speed = wrapper.speed;
        this.centerX = wrapper.centerX;
        this.centerZ = wrapper.centerZ;
        this.portalTeleportBoundary = wrapper.portalTeleportBoundary;
        this.warningTime = wrapper.warningTime;
        this.warningBlocks = wrapper.warningBlocks;
    }

    public WorldBorderAction getAction() {
        return this.action;
    }

    public double getRadius() {
        return this.radius;
    }

    public double getOldRadius() {
        return this.oldRadius;
    }

    public double getNewRadius() {
        return this.newRadius;
    }

    public long getSpeed() {
        return this.speed;
    }

    public double getCenterX() {
        return this.centerX;
    }

    public double getCenterZ() {
        return this.centerZ;
    }

    public int getPortalTeleportBoundary() {
        return this.portalTeleportBoundary;
    }

    public int getWarningTime() {
        return this.warningTime;
    }

    public int getWarningBlocks() {
        return this.warningBlocks;
    }

    public enum WorldBorderAction {
        SET_SIZE,
        LERP_SIZE,
        SET_CENTER,
        INITIALIZE,
        SET_WARNING_TIME,
        SET_WARNING_BLOCKS;
    }
}
