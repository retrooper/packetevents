/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 packetevents contributors
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

package com.github.retrooper.packetevents.protocol.vector.vecdelta;

import com.github.retrooper.packetevents.protocol.vector.positionpath.LinearPositionPath;
import com.github.retrooper.packetevents.protocol.vector.positionpath.PositionPath;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 26.3+
 */
@NullMarked
public final class LinearVecDelta implements VecDelta {

    private final short dx;
    private final short dy;
    private final short dz;

    public LinearVecDelta(double dx, double dy, double dz) {
        this(
                (short) VecDelta.encode(dx),
                (short) VecDelta.encode(dy),
                (short) VecDelta.encode(dz)
        );
    }

    public LinearVecDelta(short dx, short dy, short dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public static LinearVecDelta read(PacketWrapper<?> wrapper) {
        short xa = wrapper.readShort();
        short ya = wrapper.readShort();
        short za = wrapper.readShort();
        return new LinearVecDelta(xa, ya, za);
    }

    public static void write(PacketWrapper<?> wrapper, LinearVecDelta delta) {
        wrapper.writeShort(delta.dx);
        wrapper.writeShort(delta.dy);
        wrapper.writeShort(delta.dz);
    }

    @Override
    public PositionPath applyAsPath(Vector3d base) {
        return new LinearPositionPath(this.apply(base));
    }

    @Override
    public Vector3d apply(Vector3d base) {
        if (this.dx == 0 && this.dy == 0 && this.dz == 0) {
            return base;
        }
        return VecDelta.decode(base, this.dx, this.dy, this.dz);
    }

    @Override
    public int getStepCount() {
        return 0;
    }

    @Override
    public boolean hasDeltaX() {
        return this.dx != 0;
    }

    @Override
    public boolean hasDeltaZ() {
        return this.dz != 0;
    }

    public short dxRaw() {
        return this.dx;
    }

    public short dyRaw() {
        return this.dy;
    }

    public short dzRaw() {
        return this.dz;
    }

    public double dx() {
        return VecDelta.decode(this.dx);
    }

    public double dy() {
        return VecDelta.decode(this.dy);
    }

    public double dz() {
        return VecDelta.decode(this.dz);
    }
}
