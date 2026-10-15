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

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 26.3+
 */
@NullMarked
public final class PositionPathTypes {

    private static final VersionedRegistry<PositionPathType<?>> REGISTRY = new VersionedRegistry<>("position_path_type");

    private PositionPathTypes() {
    }

    private static <T extends PositionPath> PositionPathType<T> define(
            String name, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer
    ) {
        return REGISTRY.define(name, data -> new StaticPositionPathType<>(data, reader, writer));
    }

    public static VersionedRegistry<PositionPathType<?>> getRegistry() {
        return REGISTRY;
    }

    public static final PositionPathType<LinearPositionPath> LINEAR = define("linear",
            LinearPositionPath::read, LinearPositionPath::write);
    public static final PositionPathType<SteppedPositionPath> STEPPED = define("stepped",
            SteppedPositionPath::read, SteppedPositionPath::write);

    static {
        REGISTRY.unloadMappings();
    }
}
