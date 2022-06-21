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

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;

public class WrapperPlayClientPlayerPositionAndRotation extends WrapperPlayClientPlayerFlying {
    public WrapperPlayClientPlayerPositionAndRotation(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPlayerPositionAndRotation(Vector3d position, float yaw, float pitch, boolean onGround) {
        super(true, true, onGround, new Location(position, yaw, pitch));
    }

    public WrapperPlayClientPlayerPositionAndRotation(Location location, boolean onGround) {
        super(true, true, onGround, location);
    }

    public Vector3d getPosition() {
        return getLocation().getPosition();
    }

    public void setPosition(Vector3d position) {
        getLocation().setPosition(position);
    }

    public float getYaw() {
        return getLocation().getYaw();
    }

    public void setYaw(float yaw) {
        getLocation().setYaw(yaw);
    }

    public float getPitch() {
        return getLocation().getPitch();
    }

    public void setPitch(float pitch) {
        getLocation().setPitch(pitch);
    }
}
