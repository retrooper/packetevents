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

package com.github.retrooper.packetevents.protocol.entity.axolotl;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

public final class AxolotlVariants {

    private static final VersionedRegistry<AxolotlVariant> REGISTRY =
            new VersionedRegistry<>("axolotl_variant");

    private AxolotlVariants() {
    }

    @ApiStatus.Internal
    public static AxolotlVariant define(String name) {
        return REGISTRY.define(name, StaticAxolotlVariant::new);
    }

    public static VersionedRegistry<AxolotlVariant> getRegistry() {
        return REGISTRY;
    }

    public static final AxolotlVariant LUCY = define("lucy");
    public static final AxolotlVariant WILD = define("wild");
    public static final AxolotlVariant GOLD = define("gold");
    public static final AxolotlVariant CYAN = define("cyan");
    public static final AxolotlVariant BLUE = define("blue");

    static {
        REGISTRY.unloadMappings();
    }
}
