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

package io.github.retrooper.packetevents.packetwrappers.play.in.flying;


import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.vector.Vector3d;

public class WrappedPacketInFlying extends WrappedPacket {
    public WrappedPacketInFlying(NMSPacket packet) {
        super(packet);
    }

    public Vector3d getPosition() {
        return new Vector3d(readDouble(0), readDouble(1), readDouble(2));
    }

    public void setPosition(Vector3d position) {
        writeDouble(0, position.x);
        writeDouble(1, position.y);
        writeDouble(2, position.z);
    }

    public float getYaw() {
        return readFloat(0);
    }

    public void setYaw(float yaw) {
        writeFloat(0, yaw);
    }

    public float getPitch() {
        return readFloat(1);
    }

    public void setPitch(float pitch) {
        writeFloat(1, pitch);
    }

    public boolean isOnGround() {
        return readBoolean(0);
    }

    public void setOnGround(boolean onGround) {
        writeBoolean(0, onGround);
    }

    @Deprecated
    public boolean isPosition() {
        return readBoolean(1);
    }

    @Deprecated
    public void setIsPosition(boolean isPosition) {
        writeBoolean(1, isPosition);
    }

    @Deprecated
    public boolean isLook() {
        return readBoolean(2);
    }

    @Deprecated
    public void setIsLook(boolean isLook) {
        writeBoolean(2, isLook);
    }

    public boolean hasPositionChanged() {
        return readBoolean(1);
    }

    public void setPositionChanged(boolean positionChanged) {
        writeBoolean(1, positionChanged);
    }

    public boolean hasRotationChanged() {
        return readBoolean(2);
    }

    public void setRotationChanged(boolean rotationChanged) {
        writeBoolean(2, rotationChanged);
    }
}
