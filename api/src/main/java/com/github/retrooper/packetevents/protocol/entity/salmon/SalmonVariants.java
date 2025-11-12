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

package com.github.retrooper.packetevents.protocol.entity.salmon;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

public final class SalmonVariants {

    private static final VersionedRegistry<SalmonVariant> REGISTRY =
            new VersionedRegistry<>("salmon_variant");

    private SalmonVariants() {
    }

    @ApiStatus.Internal
    public static SalmonVariant define(String name) {
        return REGISTRY.define(name, StaticSalmonVariant::new);
    }

    public static VersionedRegistry<SalmonVariant> getRegistry() {
        return REGISTRY;
    }

    public static final SalmonVariant SMALL = define("small");
    public static final SalmonVariant MEDIUM = define("medium");
    public static final SalmonVariant LARGE = define("large");

    static {
        REGISTRY.unloadMappings();
    }
}
