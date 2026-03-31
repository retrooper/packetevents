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

package com.github.retrooper.packetevents.protocol.entity.chicken;

import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ChickenSoundVariants {

    private static final VersionedRegistry<ChickenSoundVariant> REGISTRY = new VersionedRegistry<>("chicken_sound_variant");

    private ChickenSoundVariants() {
    }

    @ApiStatus.Internal
    private static ChickenSoundVariant define(String name, String soundId) {
        ChickenSoundSet adultSounds = ChickenSoundSet.getOrThrow("entity." + soundId + ".", Sounds.ENTITY_CHICKEN_STEP);
        ChickenSoundSet babySounds = ChickenSoundSet.getOrThrow("entity.baby_chicken.", Sounds.ENTITY_BABY_CHICKEN_STEP);
        return REGISTRY.define(name, data ->
                new StaticChickenSoundVariant(data, adultSounds, babySounds));
    }

    public static VersionedRegistry<ChickenSoundVariant> getRegistry() {
        return REGISTRY;
    }

    public static final ChickenSoundVariant CLASSIC = define("classic", "chicken");
    public static final ChickenSoundVariant PICKY = define("picky", "chicken_picky");

    static {
        REGISTRY.unloadMappings();
    }
}
