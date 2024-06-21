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

package com.github.retrooper.packetevents.protocol.world;

@Deprecated
public enum TileEntityType {
    MOB_SPAWNER,
    COMMAND_BLOCK,
    BEACON,
    SKULL,
    CONDUIT,
    BANNER,
    STRUCTURE_BLOCK,
    END_GATEWAY,
    SIGN,
    BED,
    JIGSAW,
    CAMPFIRE,
    BEEHIVE,
    UNKNOWN;

    public static final TileEntityType[] VALUES = values();

    public int getId() {
        return ordinal() + 1;
    }

    public static TileEntityType getById(int id) {
        if (id < 0 || id >= VALUES.length) {
            return UNKNOWN;
        }
        return VALUES[id - 1];
    }

}
