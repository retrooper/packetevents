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

package com.github.retrooper.packetevents.protocol.entity.horse;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

public final class HorseVariants {

    private static final VersionedRegistry<HorseVariant> REGISTRY =
            new VersionedRegistry<>("horse_variant");

    private HorseVariants() {
    }

    @ApiStatus.Internal
    public static HorseVariant define(String name) {
        return REGISTRY.define(name, StaticHorseVariant::new);
    }

    public static VersionedRegistry<HorseVariant> getRegistry() {
        return REGISTRY;
    }

    public static final HorseVariant WHITE = define("white");
    public static final HorseVariant CREAMY = define("creamy");
    public static final HorseVariant CHESTNUT = define("chestnut");
    public static final HorseVariant BROWN = define("brown");
    public static final HorseVariant BLACK = define("black");
    public static final HorseVariant GRAY = define("gray");
    public static final HorseVariant DARK_BROWN = define("dark_brown");

    static {
        REGISTRY.unloadMappings();
    }
}
