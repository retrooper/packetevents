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

package com.github.retrooper.packetevents.protocol.player;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import org.jetbrains.annotations.Nullable;

public enum EquipmentSlot {
    MAINHAND(0),
    OFFHAND(0),
    BOOTS(1),
    LEGGINGS(2),
    CHESTPLATE(3),
    HELMET(4);

    private static final EquipmentSlot[] VALUES = values();

    private final byte legacyId;

    EquipmentSlot(int legacyId) {
        this.legacyId =(byte) legacyId;
    }

    public int getId(ServerVersion version) {
        if (version.isOlderThan(ServerVersion.V_1_9)) {
            return legacyId;
        }
        else {
            return ordinal();
        }
    }

    @Nullable
    public static EquipmentSlot getById(ServerVersion version, int id) {
        // FIXME: try making this O(1)
        for (EquipmentSlot slot : VALUES) {
            if (slot.getId(version) == id) {
                return slot;
            }
        }
        return null;
    }
}
