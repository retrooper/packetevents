/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.entity.villager.level;

import org.jetbrains.annotations.Nullable;

public enum VillagerLevel {

    NOVICE,
    APPRENTICE,
    JOURNEYMAN,
    EXPERT,
    MASTER;

    private static final VillagerLevel[] VALUES = values();

    public static @Nullable VillagerLevel getById(int id) {
        if (id >= 1 && id <= VALUES.length) {
            return VALUES[id - 1];
        }
        return null;
    }

    public int getId() {
        return this.ordinal() + 1;
    }
}

