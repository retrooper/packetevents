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

import com.github.retrooper.packetevents.protocol.vector.positionpath.PositionPath;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 26.3+
 */
@NullMarked
@ApiStatus.NonExtendable
public interface VecDelta {

    double TRUNCATION_STEPS = 4096d;

    static long encode(double input) {
        return Math.round(input * TRUNCATION_STEPS);
    }

    static double decode(long v) {
        return v / TRUNCATION_STEPS;
    }

    static Vector3d decode(Vector3d base, long dx, long dy, long dz) {
        return new Vector3d(
                dx == 0L ? base.x : VecDelta.decode(VecDelta.encode(base.x) + dx),
                dy == 0L ? base.y : VecDelta.decode(VecDelta.encode(base.y) + dy),
                dz == 0L ? base.z : VecDelta.decode(VecDelta.encode(base.z) + dz)
        );
    }

    static VecDelta read(PacketWrapper<?> wrapper, int stepCount) {
        if (stepCount <= 0) {
            return LinearVecDelta.read(wrapper);
        }
        return SteppedVecDelta.read(wrapper, stepCount);
    }

    static void write(PacketWrapper<?> wrapper, VecDelta vecDelta) {
        if (vecDelta instanceof LinearVecDelta) {
            LinearVecDelta.write(wrapper, (LinearVecDelta) vecDelta);
        } else if (vecDelta instanceof SteppedVecDelta) {
            SteppedVecDelta.write(wrapper, (SteppedVecDelta) vecDelta);
        } else {
            throw new UnsupportedOperationException("Unsupported VecDelta implementation: " + vecDelta);
        }
    }

    PositionPath applyAsPath(Vector3d base);

    Vector3d apply(Vector3d base);

    int getStepCount();

    boolean hasDeltaX();

    boolean hasDeltaZ();
}
