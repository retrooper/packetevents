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

package com.github.retrooper.packetevents.protocol.entity.llama;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

public final class LlamaVariants {

    private static final VersionedRegistry<LlamaVariant> REGISTRY =
            new VersionedRegistry<>("llama_variant");

    private LlamaVariants() {
    }

    @ApiStatus.Internal
    public static LlamaVariant define(String name) {
        return REGISTRY.define(name, StaticLlamaVariant::new);
    }

    public static VersionedRegistry<LlamaVariant> getRegistry() {
        return REGISTRY;
    }

    public static final LlamaVariant CREAMY = define("creamy");
    public static final LlamaVariant WHITE = define("white");
    public static final LlamaVariant BROWN = define("brown");
    public static final LlamaVariant GRAY = define("gray");

    static {
        REGISTRY.unloadMappings();
    }
}
