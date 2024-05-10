/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

public class WrapperPlayServerProjectilePower extends PacketWrapper<WrapperPlayServerProjectilePower> {

    private int entityId;
    private double powerX;
    private double powerY;
    private double powerZ;

    public WrapperPlayServerProjectilePower(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerProjectilePower(int entityId, double power) {
        this(entityId, power, power, power);
    }

    public WrapperPlayServerProjectilePower(int entityId, double powerX, double powerY, double powerZ) {
        super(PacketType.Play.Server.PROJECTILE_POWER);
        this.entityId = entityId;
        this.powerX = powerX;
        this.powerY = powerY;
        this.powerZ = powerZ;
    }

    @Override
    public void read() {
        this.entityId = this.readVarInt();
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21)) {
            this.setPower(this.readDouble());
        } else {
            this.powerX = this.readDouble();
            this.powerY = this.readDouble();
            this.powerZ = this.readDouble();
        }
    }

    @Override
    public void write() {
        this.writeVarInt(this.entityId);
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21)) {
            this.writeDouble(this.getPower());
        } else {
            this.writeDouble(this.powerX);
            this.writeDouble(this.powerY);
            this.writeDouble(this.powerZ);
        }
    }

    @Override
    public void copy(WrapperPlayServerProjectilePower wrapper) {
        this.entityId = wrapper.entityId;
        this.powerX = wrapper.powerX;
        this.powerY = wrapper.powerY;
        this.powerZ = wrapper.powerZ;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public double getPower() {
        return this.powerX;
    }

    public void setPower(double power) {
        this.powerX = power;
        this.powerY = power;
        this.powerZ = power;
    }

    public double getPowerX() {
        return this.powerX;
    }

    public void setPowerX(double powerX) {
        this.powerX = powerX;
    }

    public double getPowerY() {
        return this.powerY;
    }

    public void setPowerY(double powerY) {
        this.powerY = powerY;
    }

    public double getPowerZ() {
        return this.powerZ;
    }

    public void setPowerZ(double powerZ) {
        this.powerZ = powerZ;
    }
}
