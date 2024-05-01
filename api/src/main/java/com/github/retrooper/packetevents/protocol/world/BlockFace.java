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

/**
 * The {@code BlockFace} enum contains constants for the different valid faces in the minecraft protocol.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol#Player_Digging">https://wiki.vg/Protocol#Player_Digging</a>
 * @since 1.7.8
 */
public enum BlockFace {
    /**
     * -Y offset
     */
    DOWN(0, -1, 0),

    /**
     * +Y offset
     */
    UP(0, 1, 0),

    /**
     * -Z offset
     */
    NORTH(0, 0, -1),

    /**
     * +Z offset
     */
    SOUTH(0, 0, 1),

    /**
     * -X offset
     */
    WEST(-1, 0, 0),

    /**
     * +X offset
     */
    EAST(1, 0, 0),

    /**
     * Face is set to 255
     */
    OTHER((short) 255, -1, -1, -1);

    private static final BlockFace[] VALUES = values();
    private static final BlockFace[] CARTESIAN_VALUES = new BlockFace[]{DOWN, UP, NORTH, SOUTH, WEST, EAST}; // FIXME: remove this or use this somewhere

    final short faceValue;
    final int modX;
    final int modY;
    final int modZ;

    BlockFace(short faceValue, int modX, int modY, int modZ) {
        this.faceValue = faceValue;
        this.modX = modX;
        this.modY = modY;
        this.modZ = modZ;
    }

    BlockFace(int modX, int modY, int modZ) {
        this.faceValue = (short) ordinal();
        this.modX = modX;
        this.modY = modY;
        this.modZ = modZ;
    }

    public static BlockFace getLegacyBlockFaceByValue(int face) {
        if (face == 255) return OTHER;
        return CARTESIAN_VALUES[face % CARTESIAN_VALUES.length];
    }

    public static BlockFace getBlockFaceByValue(int face) {
        return CARTESIAN_VALUES[face % CARTESIAN_VALUES.length];
    }

    public int getModX() {
        return modX;
    }

    public int getModY() {
        return modY;
    }

    public int getModZ() {
        return modZ;
    }

    public BlockFace getOppositeFace() {
        switch (this) {
            case DOWN:
                return UP;
            case UP:
                return DOWN;
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            case EAST:
                return WEST;
            default:
                return OTHER;
        }
    }

    public BlockFace getCCW() {
        switch (this) {
            case NORTH:
                return WEST;
            case SOUTH:
                return EAST;
            case WEST:
                return SOUTH;
            case EAST:
                return NORTH;
            default:
                return OTHER;
        }
    }

    public BlockFace getCW() {
        switch (this) {
            case NORTH:
                return EAST;
            case SOUTH:
                return WEST;
            case WEST:
                return NORTH;
            case EAST:
                return SOUTH;
            default:
                return OTHER;
        }
    }

    public short getFaceValue() {
        return faceValue;
    }

}
