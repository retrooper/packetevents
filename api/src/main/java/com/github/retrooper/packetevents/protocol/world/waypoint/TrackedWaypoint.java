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

import com.github.retrooper.packetevents.util.Either;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

@NullMarked
public final class TrackedWaypoint {

    private final Either<UUID, String> identifier;
    private final WaypointIcon icon;
    private final WaypointInfo info;

    public TrackedWaypoint(Either<UUID, String> identifier, WaypointIcon icon, WaypointInfo info) {
        this.identifier = identifier;
        this.icon = icon;
        this.info = info;
    }

    public static TrackedWaypoint read(PacketWrapper<?> wrapper) {
        Either<UUID, String> identifier = wrapper.readEither(PacketWrapper::readUUID, PacketWrapper::readString);
        WaypointIcon icon = WaypointIcon.read(wrapper);
        WaypointInfo info = wrapper.readEnum(WaypointInfo.Type.class).read(wrapper);
        return new TrackedWaypoint(identifier, icon, info);
    }

    public static void write(PacketWrapper<?> wrapper, TrackedWaypoint waypoint) {
        wrapper.writeEither(waypoint.identifier, PacketWrapper::writeUUID, PacketWrapper::writeString);
        WaypointIcon.write(wrapper, waypoint.icon);
        wrapper.writeEnum(waypoint.info.getType());
        waypoint.info.getType().write(wrapper, waypoint.info);
    }

    public Either<UUID, String> getIdentifier() {
        return this.identifier;
    }

    public WaypointIcon getIcon() {
        return this.icon;
    }

    public WaypointInfo getInfo() {
        return this.info;
    }
}
