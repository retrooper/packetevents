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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

/**
 * Mojang name: ServerboundAcceptTeleportationPacket
 */
public class WrapperPlayClientTeleportConfirm extends PacketWrapper<WrapperPlayClientTeleportConfirm> {

    private int teleportId;
    /**
     * @versions 26.3+
     */
    private double x;
    /**
     * @versions 26.3+
     */
    private double y;
    /**
     * @versions 26.3+
     */
    private double z;
    /**
     * @versions 26.3+
     */
    private float yaw;
    /**
     * @versions 26.3+
     */
    private float pitch;

    public WrapperPlayClientTeleportConfirm(PacketReceiveEvent event) {
        super(event);
    }

    /**
     * @versions -26.2
     */
    @ApiStatus.Obsolete
    public WrapperPlayClientTeleportConfirm(int teleportId) {
        super(PacketType.Play.Client.TELEPORT_CONFIRM);
        this.teleportId = teleportId;
    }

    public WrapperPlayClientTeleportConfirm(int teleportId, double x, double y, double z, float yaw, float pitch) {
        super(PacketType.Play.Client.TELEPORT_CONFIRM);
        this.teleportId = teleportId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void read() {
        this.teleportId = this.readVarInt();
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_3)) {
            this.x = this.readDouble();
            this.y = this.readDouble();
            this.z = this.readDouble();
            this.yaw = this.readFloat();
            this.pitch = this.readFloat();
        }
    }

    @Override
    public void write() {
        this.writeVarInt(this.teleportId);
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_3)) {
            this.writeDouble(this.x);
            this.writeDouble(this.y);
            this.writeDouble(this.z);
            this.writeFloat(this.yaw);
            this.writeFloat(this.pitch);
        }
    }

    @Override
    public void copy(WrapperPlayClientTeleportConfirm wrapper) {
        this.teleportId = wrapper.teleportId;
        this.x = wrapper.x;
        this.y = wrapper.y;
        this.z = wrapper.z;
        this.yaw = wrapper.yaw;
        this.pitch = wrapper.pitch;
    }

    public int getTeleportId() {
        return teleportId;
    }

    public void setTeleportId(int teleportID) {
        this.teleportId = teleportID;
    }

    /**
     * @versions 26.3+
     */
    public double getX() {
        return this.x;
    }

    /**
     * @versions 26.3+
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @versions 26.3+
     */
    public double getY() {
        return this.y;
    }

    /**
     * @versions 26.3+
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @versions 26.3+
     */
    public double getZ() {
        return this.z;
    }

    /**
     * @versions 26.3+
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * @versions 26.3+
     */
    public float getYaw() {
        return this.yaw;
    }

    /**
     * @versions 26.3+
     */
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    /**
     * @versions 26.3+
     */
    public float getPitch() {
        return this.pitch;
    }

    /**
     * @versions 26.3+
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
