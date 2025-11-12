/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.waypoint;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EmptyWaypointInfo implements WaypointInfo {

    public static final EmptyWaypointInfo EMPTY = new EmptyWaypointInfo();

    private EmptyWaypointInfo() {
    }

    @ApiStatus.Internal
    public static EmptyWaypointInfo read(PacketWrapper<?> wrapper) {
        return EMPTY;
    }

    @ApiStatus.Internal
    public static void write(PacketWrapper<?> wrapper, WaypointInfo info) {
        // NO-OP
    }

    @Override
    public Type getType() {
        return Type.EMPTY;
    }
}
