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

package com.github.retrooper.packetevents.protocol.debug.struct;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.21.9+
 */
@NullMarked
public final class DebugEntityBlockIntersection {

    private final IntersectionType type;

    public DebugEntityBlockIntersection(IntersectionType type) {
        this.type = type;
    }

    public static DebugEntityBlockIntersection read(PacketWrapper<?> wrapper) {
        IntersectionType type = wrapper.readEnum(IntersectionType.values());
        return new DebugEntityBlockIntersection(type);
    }

    public static void write(PacketWrapper<?> wrapper, DebugEntityBlockIntersection intersection) {
        wrapper.writeEnum(intersection.type);
    }

    public IntersectionType getType() {
        return this.type;
    }

    public enum IntersectionType {
        IN_BLOCK,
        IN_FLUID,
        IN_AIR,
    }
}
