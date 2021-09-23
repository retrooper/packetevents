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

package io.github.retrooper.packetevents.protocol.data.world;

/**
 * The {@code Direction} enum contains constants for the different valid faces in the minecraft protocol.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol#Player_Digging">https://wiki.vg/Protocol#Player_Digging</a>
 * @since 1.7.8
 */
public enum Direction {
    /**
     * -Y offset
     */
    DOWN,

    /**
     * +Y offset
     */
    UP,

    /**
     * -Z offset
     */
    NORTH,

    /**
     * +Z offset
     */
    SOUTH,

    /**
     * -X offset
     */
    WEST,

    /**
     * +X offset
     */
    EAST,

    /**
     * Face is set to 255
     */
    OTHER((short) 255),

    /**
     * Should not happen.... Invalid value?
     */
    INVALID;

    final short face;

    Direction(short face) {
        this.face = face;
    }

    Direction() {
        this.face = (short) ordinal();
    }

    public static Direction getDirection(int face) {
        if (face == 255) {
            return OTHER;
        } else if (face < 0 || face > 5) {
            return INVALID;
        }
        return values()[face];
    }

    public short getFaceValue() {
        return face;
    }
}
