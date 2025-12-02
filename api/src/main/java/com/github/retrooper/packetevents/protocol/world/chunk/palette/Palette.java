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

/*
 * This class was taken from MCProtocolLib.
 *
 * https://github.com/Steveice10/MCProtocolLib
 */

package com.github.retrooper.packetevents.protocol.world.chunk.palette;

/**
 * A palette for mapping block states to storage IDs.
 */
public interface Palette {

    /**
     * Gets the number of block states known by this palette.
     *
     * @return The palette's size.
     */
    int size();

    /**
     * Converts a block state to a storage ID. If the state has not been mapped,
     * the palette will attempt to map it, returning -1 if it cannot.
     *
     * @param state Block state to convert.
     * @return The resulting storage ID.
     */
    int stateToId(int state);

    /**
     * Converts a storage ID to a block state. If the storage ID has no mapping,
     * it will return a block state of 0.
     *
     * @param id Storage ID to convert.
     * @return The resulting block state.
     */
    int idToState(int id);

    int getBits();
}
