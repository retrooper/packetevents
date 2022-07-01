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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;

public enum EquipmentSlot {
    MAINHAND(0),
    OFFHAND(0),
    BOOTS(1),
    LEGGINGS(2),
    CHESTPLATE(3),
    HELMET(4);

    private static final EquipmentSlot[] VALUES;
    private static final Map<Integer, EquipmentSlot> EQUIPMENT_SLOT_MAP;

    static {
        // TODO: Remove the cache at some point as we don't want to cache this.
        // TODO: We can remove it after removing the deprecated method.
        VALUES = values();
        EQUIPMENT_SLOT_MAP = new HashMap<>(VALUES.length);
        for (EquipmentSlot slot : VALUES) {
            EQUIPMENT_SLOT_MAP.put(slot.getId(PacketEvents.getAPI().getServerManager().getVersion()), slot);
        }
    }

    private final byte legacyId;

    EquipmentSlot(int legacyId) {
        this.legacyId = (byte) legacyId;
    }

    /**
     * Gets the legacy id, or the modern id, depending on the server version.
     * The legacy id is being returned on server versions below or equals 1.9.
     *
     * @param version The server version.
     * @return The legacy id, or the modern id, depending on the server version.
     */
    public int getId(ServerVersion version) {
        if (version.isOlderThan(ServerVersion.V_1_9)) {
            return legacyId;
        } else {
            return ordinal();
        }
    }

    /**
     * Get the EquipmentSlot from the given id.<p>
     * This method only returns {@code null} if the id is not valid.</p>
     * <p></p>
     * <b>IMPORTANT</b><p>
     * If your server version is below or equals 1.9, the id can only be between 0 and 4.</p>
     * If your server version is newer than 1.9, the id can be between 0 and 5.
     *
     * @param id      The id of the EquipmentSlot.
     * @return The EquipmentSlot from the given id.
     */
    public static Optional<EquipmentSlot> byId(@Range(from = 0, to = 5) int id) {
        // This is O(1)
        return Optional.ofNullable(EQUIPMENT_SLOT_MAP.get(id));
    }

    /**
     * Gets the EquipmentSlot from the given id.
     * This method only returns {@code null} if the id is not valid.
     *
     * @param version The version of the server.
     * @param id      The id of the EquipmentSlot.
     * @return The EquipmentSlot from the given id.
     * @deprecated Use {@link #byId(int)} instead.
     */
    @Nullable
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0-RELEASE")
    public static EquipmentSlot getById(ServerVersion version, @Range(from = 0, to = 5) int id) {
        // FIXME: try making this O(1)
        for (EquipmentSlot slot : VALUES) {
            if (slot.getId(version) == id) {
                return slot;
            }
        }
        return null;
    }
}
