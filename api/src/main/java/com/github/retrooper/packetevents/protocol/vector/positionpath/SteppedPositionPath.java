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

import java.util.List;

/**
 * @versions 26.3+
 */
@NullMarked
public final class SteppedPositionPath implements PositionPath {

    private final List<Step> steps;

    public SteppedPositionPath(List<Step> steps) {
        this.steps = steps;
    }

    public static SteppedPositionPath read(PacketWrapper<?> wrapper) {
        return new SteppedPositionPath(wrapper.readList(Step::read));
    }

    public static void write(PacketWrapper<?> wrapper, SteppedPositionPath path) {
        wrapper.writeList(path.steps, Step::write);
    }

    @Override
    public PositionPathType<?> getType() {
        return PositionPathTypes.STEPPED;
    }

    /**
     * @throws IllegalStateException if there are no steps
     */
    @Override
    public Vector3d getEndPosition() throws IllegalStateException {
        if (this.steps.isEmpty()) {
            throw new IllegalStateException("No steps present, can't get end position");
        }
        return this.steps.get(this.steps.size() - 1).position();
    }

    public List<Step> getSteps() {
        return this.steps;
    }

    public static final class Step {

        private final double x;
        private final double y;
        private final double z;
        private final int tickOffset;

        public Step(Vector3d vector, int tickOffset) {
            this(vector.x, vector.y, vector.z, tickOffset);
        }

        public Step(double x, double y, double z, int tickOffset) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.tickOffset = tickOffset;
        }

        public static Step read(PacketWrapper<?> wrapper) {
            double x = wrapper.readDouble();
            double y = wrapper.readDouble();
            double z = wrapper.readDouble();
            int tickOffset = wrapper.readVarInt();
            return new Step(x, y, z, tickOffset);
        }

        public static void write(PacketWrapper<?> wrapper, Step step) {
            wrapper.writeDouble(step.x);
            wrapper.writeDouble(step.y);
            wrapper.writeDouble(step.z);
            wrapper.writeVarInt(step.tickOffset);
        }

        public Vector3d position() {
            return new Vector3d(this.x, this.y, this.z);
        }

        public double x() {
            return this.x;
        }

        public double y() {
            return this.y;
        }

        public double z() {
            return this.z;
        }

        public int tickOffset() {
            return this.tickOffset;
        }
    }
}
