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

package com.github.retrooper.packetevents.protocol.player;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import org.jetbrains.annotations.Nullable;

public enum EquipmentSlot {
    MAIN_HAND(0, 0),
    OFF_HAND(0, 5),
    BOOTS(1, 1),
    LEGGINGS(2, 2),
    CHEST_PLATE(3, 3),
    HELMET(4, 4),
    BODY(0, 6),
    SADDLE(0, 7);

    private static final EquipmentSlot[] VALUES = values();
    private static final EquipmentSlot[] BY_COMPONENT_ID = new EquipmentSlot[VALUES.length];

    static {
        for (EquipmentSlot slot : VALUES) {
            BY_COMPONENT_ID[slot.componentId] = slot;
        }
    }

    private final byte legacyId;
    private final byte componentId;

    EquipmentSlot(int legacyId, int componentId) {
        this.legacyId = (byte) legacyId;
        this.componentId = (byte) componentId;
    }

    public int getComponentId() {
        return this.componentId;
    }

    public int getId(ServerVersion version) {
        return getId(version.toClientVersion());
    }

    public int getId(ClientVersion version) {
        if (version.isOlderThan(ClientVersion.V_1_9)) {
            return legacyId;
        } else {
            return ordinal();
        }
    }

    @Nullable
    public static EquipmentSlot getById(ClientVersion version, int id) {
        if (version.isOlderThan(ClientVersion.V_1_9)) {
            for (EquipmentSlot slot : VALUES) {
                if (slot.getId(version) == id) {
                    return slot;
                }
            }
        } else {
            return VALUES[id];
        }
        return null;
    }

    @Nullable
    public static EquipmentSlot getById(ServerVersion version, int id) {
        return getById(version.toClientVersion(), id);
    }

    public static EquipmentSlot getByComponentId(int id) {
        return BY_COMPONENT_ID[id];
    }
}
