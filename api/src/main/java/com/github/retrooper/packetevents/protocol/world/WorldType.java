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

import org.jetbrains.annotations.Nullable;

public enum WorldType {
    DEFAULT("default"),
    FLAT("flat"),
    LARGE_BIOMES("largeBiomes"),
    AMPLIFIED("amplified"),
    CUSTOMIZED("customized"),
    BUFFET("buffet"),
    DEBUG_ALL_BLOCK_STATES("debug_all_block_states"),
    DEFAULT_1_1("default_1_1");

    private static final WorldType[] VALUES = values();
    private final String name;

    WorldType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public static WorldType getByName(String name) {
        for (WorldType type : VALUES) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }
}
