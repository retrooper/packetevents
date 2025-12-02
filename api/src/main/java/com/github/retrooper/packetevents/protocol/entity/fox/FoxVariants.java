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

package com.github.retrooper.packetevents.protocol.entity.fox;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

public final class FoxVariants {

    private static final VersionedRegistry<FoxVariant> REGISTRY =
            new VersionedRegistry<>("fox_variant");

    private FoxVariants() {
    }

    @ApiStatus.Internal
    public static FoxVariant define(String name) {
        return REGISTRY.define(name, StaticFoxVariant::new);
    }

    public static VersionedRegistry<FoxVariant> getRegistry() {
        return REGISTRY;
    }

    public static final FoxVariant RED = define("red");
    public static final FoxVariant SNOW = define("snow");

    static {
        REGISTRY.unloadMappings();
    }
}
