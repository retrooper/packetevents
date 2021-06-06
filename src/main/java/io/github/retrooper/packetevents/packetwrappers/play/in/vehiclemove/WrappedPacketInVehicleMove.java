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

package io.github.retrooper.packetevents.packetwrappers.play.in.vehiclemove;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.vector.Vector3d;

public class WrappedPacketInVehicleMove extends WrappedPacket {
    public WrappedPacketInVehicleMove(NMSPacket packet) {
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

    //TODO Cut off at 1.8.1
    @Deprecated
    public double getX() {
        return readDouble(0);
    }

    @Deprecated
    public void setX(double x) {
        writeDouble(0, x);
    }

    @Deprecated
    public double getY() {
        return readDouble(1);
    }

    @Deprecated
    public void setY(double y) {
        writeDouble(1, y);
    }

    @Deprecated
    public double getZ() {
        return readDouble(2);
    }

    @Deprecated
    public void setZ(double z) {
        writeDouble(2, z);
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
}
