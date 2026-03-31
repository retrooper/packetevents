/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.clock;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 26.1+
 */
@NullMarked
public final class WorldClocks {

    private static final VersionedRegistry<WorldClock> REGISTRY = new VersionedRegistry<>("world_clock");

    private WorldClocks() {
    }

    public static VersionedRegistry<WorldClock> getRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Internal
    public static WorldClock define(String name) {
        return REGISTRY.define(name, StaticWorldClock::new);
    }

    public static final WorldClock OVERWORLD = define("overworld");
    public static final WorldClock THE_END = define("the_end");

    static {
        REGISTRY.unloadMappings();
    }
}
