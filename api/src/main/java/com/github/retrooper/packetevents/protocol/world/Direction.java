/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world;

import com.github.retrooper.packetevents.protocol.util.CodecNameable;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.world.states.enums.Axis;
import com.github.retrooper.packetevents.util.Vector3i;

// From MCProtocolLib
public enum Direction implements CodecNameable {

    DOWN("down", -1, Axis.Y, new Vector3i(0, -1, 0)),
    UP("up", -1, Axis.Y, new Vector3i(0, 1, 0)),
    NORTH("north", 0, Axis.Z, new Vector3i(0, 0, -1)),
    SOUTH("south", 1, Axis.Z, new Vector3i(0, 0, 1)),
    WEST("west", 2, Axis.X, new Vector3i(-1, 0, 0)),
    EAST("east", 3, Axis.X, new Vector3i(1, 0, 0));

    public static final NbtCodec<Direction> CODEC = NbtCodecs.forEnum(values());

    private static final Direction[] HORIZONTAL_VALUES = {NORTH, SOUTH, WEST, EAST};
    private static final Direction[] VALUES = values(); // Cache the values array

    private final String id;
    private final int horizontalIndex;
    private final Axis axis;
    private final Vector3i vec3i;

    Direction(String id, int horizontalIndex, Axis axis, Vector3i vec3i) {
        this.id = id;
        this.horizontalIndex = horizontalIndex;
        this.axis = axis;
        this.vec3i = vec3i;
    }

    @Override
    public String getCodecName() {
        return this.id;
    }

    public int getHorizontalIndex() {
        return horizontalIndex;
    }

    public static Direction getByHorizontalIndex(int index) {
        return HORIZONTAL_VALUES[index % HORIZONTAL_VALUES.length];
    }

    public static Direction getByIndex(int enumOrdinal) {
        return VALUES[enumOrdinal];
    }

    public Vector3i getVector() {
        return this.vec3i;
    }

    public Axis getAxis() {
        return this.axis;
    }
}
