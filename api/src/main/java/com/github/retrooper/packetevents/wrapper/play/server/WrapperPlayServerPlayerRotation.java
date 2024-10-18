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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerPlayerRotation extends PacketWrapper<WrapperPlayServerPlayerRotation> {

    private float yaw;
    private float pitch;

    public WrapperPlayServerPlayerRotation(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerPlayerRotation(float yaw, float pitch) {
        super(PacketType.Play.Server.PLAYER_ROTATION);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void read() {
        this.yaw = this.readFloat();
        this.pitch = this.readFloat();
    }

    @Override
    public void write() {
        this.writeFloat(this.yaw);
        this.writeFloat(this.pitch);
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

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
