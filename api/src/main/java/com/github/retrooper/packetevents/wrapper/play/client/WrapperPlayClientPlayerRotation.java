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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

public class WrapperPlayClientPlayerRotation extends WrapperPlayClientPlayerFlying<WrapperPlayClientPlayerRotation> {
    private float yaw;
    private float pitch;
    public WrapperPlayClientPlayerRotation(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPlayerRotation(float yaw, float pitch, boolean onGround) {
        super(PacketType.Play.Client.PLAYER_ROTATION, onGround);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void readData() {
        yaw = readFloat();
        pitch = readFloat();
        super.readData();
    }

    @Override
    public void readData(WrapperPlayClientPlayerRotation wrapper) {
        yaw = wrapper.yaw;
        pitch = wrapper.pitch;
        super.readData(wrapper);
    }

    @Override
    public void writeData() {
        writeFloat(yaw);
        writeFloat(pitch);
        super.writeData();
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
