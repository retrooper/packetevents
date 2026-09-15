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
import com.github.retrooper.packetevents.protocol.vector.positionpath.SteppedPositionPath;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

/**
 * @versions 26.3+
 */
@NullMarked
public final class SteppedVecDelta implements VecDelta {

    private final List<DeltaStep> steps;

    public SteppedVecDelta(List<DeltaStep> steps) {
        this.steps = steps;
    }

    public static SteppedVecDelta read(PacketWrapper<?> wrapper, int stepCount) {
        List<DeltaStep> steps = new ArrayList<>(stepCount);
        for (int i = 0; i < stepCount; i++) {
            steps.add(DeltaStep.read(wrapper));
        }
        return new SteppedVecDelta(steps);
    }

    public static void write(PacketWrapper<?> wrapper, SteppedVecDelta delta) {
        for (DeltaStep step : delta.steps) {
            DeltaStep.write(wrapper, step);
        }
    }

    @Override
    public PositionPath applyAsPath(Vector3d base) {
        if (this.steps.isEmpty()) {
            return new LinearPositionPath(base);
        }
        List<SteppedPositionPath.Step> steps = new ArrayList<>(this.steps.size());
        for (DeltaStep step : this.steps) {
            Vector3d pos = VecDelta.decode(base, step.dx, step.dy, step.dz);
            steps.add(new SteppedPositionPath.Step(pos, step.ticks));
            base = pos;
        }
        return new SteppedPositionPath(steps);
    }

    @Override
    public Vector3d apply(Vector3d base) {
        if (this.steps.isEmpty()) {
            return base;
        }
        // only allocate one single Vector3d object as return value
        double x = base.x;
        double y = base.y;
        double z = base.z;
        for (DeltaStep step : this.steps) {
            if (step.dx != 0L) {
                x = VecDelta.decode(VecDelta.encode(x) + step.dx);
            }
            if (step.dy != 0L) {
                y = VecDelta.decode(VecDelta.encode(y) + step.dy);
            }
            if (step.dz != 0L) {
                z = VecDelta.decode(VecDelta.encode(z) + step.dz);
            }
        }
        return new Vector3d(x, y, z);
    }

    @Override
    public int getStepCount() {
        return this.steps.size();
    }

    @Override
    public boolean hasDeltaX() {
        for (DeltaStep step : this.steps) {
            if (step.dx != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasDeltaZ() {
        for (DeltaStep step : this.steps) {
            if (step.dz != 0) {
                return true;
            }
        }
        return false;
    }

    public List<DeltaStep> steps() {
        return this.steps;
    }

    public static final class DeltaStep {

        private final int ticks;
        private final short dx;
        private final short dy;
        private final short dz;

        public DeltaStep(int ticks, short dx, short dy, short dz) {
            this.ticks = ticks;
            this.dx = dx;
            this.dy = dy;
            this.dz = dz;
        }

        public static DeltaStep read(PacketWrapper<?> wrapper) {
            int ticks = wrapper.readVarInt();
            short xa = wrapper.readShort();
            short ya = wrapper.readShort();
            short za = wrapper.readShort();
            return new DeltaStep(ticks, xa, ya, za);
        }

        public static void write(PacketWrapper<?> wrapper, DeltaStep step) {
            wrapper.writeVarInt(step.ticks);
            wrapper.writeShort(step.dx);
            wrapper.writeShort(step.dy);
            wrapper.writeShort(step.dz);
        }

        public int ticks() {
            return this.ticks;
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
}
