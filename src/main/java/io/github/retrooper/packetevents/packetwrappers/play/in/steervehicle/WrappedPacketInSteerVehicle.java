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

package io.github.retrooper.packetevents.packetwrappers.play.in.steervehicle;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInSteerVehicle extends WrappedPacket {
    public WrappedPacketInSteerVehicle(NMSPacket packet) {
        super(packet);
    }

    public float getSideValue() {
        return readFloat(0);
    }

    public void setSideValue(float value) {
        writeFloat(0, value);
    }

    public float getForwardValue() {
        return readFloat(1);
    }

    public void setForwardValue(float value) {
        writeFloat(1, value);
    }

    public boolean isJump() {
        return readBoolean(0);
    }

    public void setJump(boolean isJump) {
        writeBoolean(0, isJump);
    }

    public boolean isDismount() {
        return readBoolean(1);
    }

    public void setDismount(boolean isDismount) {
        writeBoolean(1, isDismount);
    }
}
