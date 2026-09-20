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

public enum DiggingAction {

    /**
     * Mojang name: START_DESTROY_BLOCK
     */
    START_DIGGING,
    /**
     * Mojang name: CHANGE_DESTROY_DIRECTION
     *
     * @versions 26.3+
     */
    CHANGE_DESTROY_DIRECTION,
    /**
     * Mojang name: ABORT_DESTROY_BLOCK
     */
    CANCELLED_DIGGING,
    /**
     * Mojang name: STOP_DESTROY_BLOCK
     */
    FINISHED_DIGGING,
    /**
     * Mojang name: DROP_ALL_ITEMS
     */
    DROP_ITEM_STACK,
    /**
     * Mojang name: DROP_ITEM
     */
    DROP_ITEM,
    /**
     * Mojang name: RELEASE_USE_ITEM
     */
    RELEASE_USE_ITEM,
    /**
     * Mojang name: SWAP_ITEM_WITH_OFFHAND
     */
    SWAP_ITEM_WITH_OFFHAND,
    /**
     * Mojang name: STAB
     *
     * @versions 1.21.11+
     */
    STAB,
    ;

    private static final DiggingAction[] VALUES = values();

    public int getId(ServerVersion version) {
        if (version.isNewerThanOrEquals(ServerVersion.V_26_3) || this == START_DIGGING) {
            return this.ordinal();
        }
        if (this == CHANGE_DESTROY_DIRECTION) {
            throw new IllegalStateException("CHANGE_DESTROY_DIRECTION has no id before 26.3");
        }
        return this.ordinal() - 1;
    }

    /**
     * Pre-26.3 wire id. Prefer {@link #getId(ServerVersion)}.
     */
    @Deprecated
    public int getId() {
        return getId(ServerVersion.V_26_2);
    }

    public static DiggingAction getById(ServerVersion version, int id) {
        if (version.isNewerThanOrEquals(ServerVersion.V_26_3) || id <= 0) {
            return VALUES[id];
        }
        return VALUES[id + 1];
    }

    /**
     * Pre-26.3 wire id. Prefer {@link #getById(ServerVersion, int)}.
     */
    @Deprecated
    public static DiggingAction getById(int id) {
        return getById(ServerVersion.V_26_2, id);
    }
}
