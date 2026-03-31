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

package com.github.retrooper.packetevents.protocol.entity.pig;

import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PigSoundVariants {

    private static final VersionedRegistry<PigSoundVariant> REGISTRY = new VersionedRegistry<>("pig_sound_variant");

    private PigSoundVariants() {
    }

    @ApiStatus.Internal
    private static PigSoundVariant define(String name, String soundId) {
        PigSoundSet adultSounds = PigSoundSet.getOrThrow("entity." + soundId + ".", Sounds.ENTITY_PIG_STEP);
        PigSoundSet babySounds = PigSoundSet.getOrThrow("entity.baby_pig.", Sounds.ENTITY_BABY_PIG_STEP);
        return REGISTRY.define(name, data ->
                new StaticPigSoundVariant(data, adultSounds, babySounds));
    }

    public static VersionedRegistry<PigSoundVariant> getRegistry() {
        return REGISTRY;
    }

    public static final PigSoundVariant CLASSIC = define("classic", "pig");
    public static final PigSoundVariant MINI = define("mini", "pig_mini");
    public static final PigSoundVariant BIG = define("big", "pig_big");

    static {
        REGISTRY.unloadMappings();
    }
}
