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

package com.github.retrooper.packetevents.protocol.entity.rabbit;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

public final class RabbitVariants {

    private static final VersionedRegistry<RabbitVariant> REGISTRY =
            new VersionedRegistry<>("rabbit_variant");

    private RabbitVariants() {
    }

    @ApiStatus.Internal
    public static RabbitVariant define(String name) {
        return REGISTRY.define(name, StaticRabbitVariant::new);
    }

    public static VersionedRegistry<RabbitVariant> getRegistry() {
        return REGISTRY;
    }

    public static final RabbitVariant BROWN = define("brown");
    public static final RabbitVariant WHITE = define("white");
    public static final RabbitVariant BLACK = define("black");
    public static final RabbitVariant WHITE_SPLOTCHED = define("white_splotched");
    public static final RabbitVariant GOLD = define("gold");
    public static final RabbitVariant SALT = define("salt");
    public static final RabbitVariant EVIL = define("evil");

    static {
        REGISTRY.unloadMappings();
    }
}
