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

package com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

/**
 * @versions 1.21.5+
 */
public final class WolfSoundVariants {

    private static final VersionedRegistry<WolfSoundVariant> REGISTRY =
            new VersionedRegistry<>("wolf_sound_variant");

    private WolfSoundVariants() {
    }

    public static VersionedRegistry<WolfSoundVariant> getRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Internal
    public static WolfSoundVariant define(String name, String soundId) {
        WolfSoundSet adultSounds = WolfSoundSet.getOrThrow("entity." + soundId + ".", Sounds.ENTITY_WOLF_STEP);
        WolfSoundSet babySounds = WolfSoundSet.getOrThrow("entity.baby_wolf.", Sounds.ENTITY_BABY_WOLF_STEP);
        return define(name, adultSounds, babySounds);
    }

    @ApiStatus.Internal
    public static WolfSoundVariant define(String name, WolfSoundSet adultSounds, WolfSoundSet babySounds) {
        return REGISTRY.define(name, data -> new StaticWolfSoundVariant(
                data, adultSounds, babySounds));
    }

    public static final WolfSoundVariant CLASSIC = define("classic", "wolf");
    public static final WolfSoundVariant PUGLIN = define("puglin", "wolf_puglin");
    public static final WolfSoundVariant SAD = define("sad", "wolf_sad");
    public static final WolfSoundVariant ANGRY = define("angry", "wolf_angry");
    public static final WolfSoundVariant GRUMPY = define("grumpy", "wolf_grumpy");
    public static final WolfSoundVariant BIG = define("big", "wolf_big");
    public static final WolfSoundVariant CUTE = define("cute", "wolf_cute");

    static {
        REGISTRY.unloadMappings();
    }
}
