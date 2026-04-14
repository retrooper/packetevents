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

package com.github.retrooper.packetevents.protocol.entity.cow;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CowSoundVariants {

    private static final VersionedRegistry<CowSoundVariant> REGISTRY = new VersionedRegistry<>("cow_sound_variant");

    private CowSoundVariants() {
    }

    @ApiStatus.Internal
    private static CowSoundVariant define(String name, String soundId) {
        CowSoundSet sounds = CowSoundSet.getOrThrow("entity." + soundId + ".");
        return REGISTRY.define(name, data ->
                new StaticCowSoundVariant(data, sounds));
    }

    public static VersionedRegistry<CowSoundVariant> getRegistry() {
        return REGISTRY;
    }

    public static final CowSoundVariant CLASSIC = define("classic", "cow");
    public static final CowSoundVariant MOODY = define("moody", "cow_moody");

    static {
        REGISTRY.unloadMappings();
    }
}
