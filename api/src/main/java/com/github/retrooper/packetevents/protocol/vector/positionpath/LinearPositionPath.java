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

package com.github.retrooper.packetevents.protocol.vector.positionpath;

import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 26.3+
 */
@NullMarked
public final class LinearPositionPath implements PositionPath {

    private final Vector3d endPosition;

    public LinearPositionPath(Vector3d endPosition) {
        this.endPosition = endPosition;
    }

    public static LinearPositionPath read(PacketWrapper<?> wrapper) {
        return new LinearPositionPath(Vector3d.read(wrapper));
    }

    public static void write(PacketWrapper<?> wrapper, LinearPositionPath path) {
        Vector3d.write(wrapper, path.endPosition);
    }

    @Override
    public PositionPathType<?> getType() {
        return PositionPathTypes.LINEAR;
    }

    @Override
    public Vector3d getEndPosition() {
        return this.endPosition;
    }
}
