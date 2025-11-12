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

package com.github.retrooper.packetevents.protocol.world.states.enums;

public enum Mode {

    /**
     * Only applies to comparators
     */
    COMPARE,
    /**
     * Only applies to comparators
     */
    SUBTRACT,

    /**
     * Only applies to structure blocks
     */
    SAVE,
    /**
     * Only applies to structure blocks
     */
    LOAD,
    /**
     * Only applies to structure blocks
     */
    CORNER,
    /**
     * Only applies to structure blocks
     */
    DATA,

    /**
     * Added with 1.21.5, only applies to test blocks
     */
    START,
    /**
     * Added with 1.21.5, only applies to test blocks
     */
    LOG,
    /**
     * Added with 1.21.5, only applies to test blocks
     */
    FAIL,
    /**
     * Added with 1.21.5, only applies to test blocks
     */
    ACCEPT,
}
