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

/**
 * Mojang name: ClientboundPlayerRotationPacket
 */
public class WrapperPlayServerPlayerRotation extends PacketWrapper<WrapperPlayServerPlayerRotation> {

    private float yaw;
    /**
     * @versions 1.21.9+
     */
    private boolean relativeYaw;
    private float pitch;
    /**
     * @versions 1.21.9+
     */
    private boolean relativePitch;

    public WrapperPlayServerPlayerRotation(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerPlayerRotation(float yaw, float pitch) {
        this(yaw, false, pitch, false);
    }

    public WrapperPlayServerPlayerRotation(float yaw, boolean relativeYaw, float pitch, boolean relativePitch) {
        super(PacketType.Play.Server.PLAYER_ROTATION);
        this.yaw = yaw;
        this.relativeYaw = relativeYaw;
        this.pitch = pitch;
        this.relativePitch = relativePitch;
    }

    @Override
    public void read() {
        this.yaw = this.readFloat();
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            this.relativeYaw = this.readBoolean();
        }
        this.pitch = this.readFloat();
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            this.relativePitch = this.readBoolean();
        }
    }

    @Override
    public void write() {
        this.writeFloat(this.yaw);
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            this.writeBoolean(this.relativeYaw);
        }
        this.writeFloat(this.pitch);
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            this.writeBoolean(this.relativePitch);
        }
    }

    @Override
    public void copy(WrapperPlayServerPlayerRotation wrapper) {
        this.yaw = wrapper.yaw;
        this.pitch = wrapper.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    /**
     * @versions 1.21.9+
     */
    public boolean isRelativeYaw() {
        return this.relativeYaw;
    }

    /**
     * @versions 1.21.9+
     */
    public void setRelativeYaw(boolean relativeYaw) {
        this.relativeYaw = relativeYaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * @versions 1.21.9+
     */
    public boolean isRelativePitch() {
        return this.relativePitch;
    }

    /**
     * @versions 1.21.9+
     */
    public void setRelativePitch(boolean relativePitch) {
        this.relativePitch = relativePitch;
    }
}
