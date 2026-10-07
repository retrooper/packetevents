/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 packetevents contributors
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

package com.github.retrooper.packetevents.protocol.vector.positionpath;

import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 26.3+
 */
@NullMarked
@ApiStatus.NonExtendable
public interface PositionPath {

    static PositionPath read(PacketWrapper<?> wrapper) {
        PositionPathType<?> type = wrapper.readMappedEntity(PositionPathTypes.getRegistry());
        return type.read(wrapper);
    }

    @SuppressWarnings("unchecked")
    static void write(PacketWrapper<?> wrapper, PositionPath path) {
        wrapper.writeMappedEntity(path.getType());
        ((PositionPathType<? super PositionPath>) path.getType()).write(wrapper, path);
    }

    PositionPathType<?> getType();

    Vector3d getEndPosition();
}
