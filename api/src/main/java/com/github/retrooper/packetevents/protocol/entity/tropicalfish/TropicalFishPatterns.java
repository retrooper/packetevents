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

package com.github.retrooper.packetevents.protocol.entity.tropicalfish;

import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.function.Function;

public final class TropicalFishPatterns {

    private static final VersionedRegistry<TropicalFishPattern> REGISTRY =
            new VersionedRegistry<>("tropical_fish_pattern");

    private TropicalFishPatterns() {
    }

    @ApiStatus.Internal
    public static TropicalFishPattern define(String name, TropicalFishPattern.Base base) {
        return REGISTRY.define(name, (Function<TypesBuilderData, TropicalFishPattern>)
                typesBuilderData -> new StaticTropicalFishPattern(typesBuilderData, base));
    }

    public static VersionedRegistry<TropicalFishPattern> getRegistry() {
        return REGISTRY;
    }

    public static final TropicalFishPattern KOB = define("kob", TropicalFishPattern.Base.SMALL);
    public static final TropicalFishPattern SUNSTREAK = define("sunstreak", TropicalFishPattern.Base.SMALL);
    public static final TropicalFishPattern SNOOPER = define("snooper", TropicalFishPattern.Base.SMALL);
    public static final TropicalFishPattern DASHER = define("dasher", TropicalFishPattern.Base.SMALL);
    public static final TropicalFishPattern BRINELY = define("brinely", TropicalFishPattern.Base.SMALL);
    public static final TropicalFishPattern SPOTTY = define("spotty", TropicalFishPattern.Base.SMALL);
    public static final TropicalFishPattern FLOPPER = define("flopper", TropicalFishPattern.Base.LARGE);
    public static final TropicalFishPattern STRIPEY = define("stripey", TropicalFishPattern.Base.LARGE);
    public static final TropicalFishPattern GLITTER = define("glitter", TropicalFishPattern.Base.LARGE);
    public static final TropicalFishPattern BLOCKFISH = define("blockfish", TropicalFishPattern.Base.LARGE);
    public static final TropicalFishPattern BETTY = define("betty", TropicalFishPattern.Base.LARGE);
    public static final TropicalFishPattern CLAYFISH = define("clayfish", TropicalFishPattern.Base.LARGE);

    public static Collection<TropicalFishPattern> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}
