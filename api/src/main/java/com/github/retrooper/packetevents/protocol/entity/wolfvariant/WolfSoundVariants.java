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

import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

public final class WolfSoundVariants {

    private static final VersionedRegistry<WolfSoundVariant> REGISTRY =
            new VersionedRegistry<>("wolf_sound_variant");

    private WolfSoundVariants() {
    }

    public static VersionedRegistry<WolfSoundVariant> getRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Internal
    public static WolfSoundVariant define(String name, String suffix) {
        return define(name, Sounds.getByName("entity.wolf" + suffix + ".ambient"),
                Sounds.getByName("entity.wolf" + suffix + ".death"),
                Sounds.getByName("entity.wolf" + suffix + ".growl"),
                Sounds.getByName("entity.wolf" + suffix + ".hurt"),
                Sounds.getByName("entity.wolf" + suffix + ".pant"),
                Sounds.getByName("entity.wolf" + suffix + ".whine"));
    }

    @ApiStatus.Internal
    public static WolfSoundVariant define(
            String name, Sound ambientSound, Sound deathSound, Sound growlSound,
            Sound hurtSound, Sound pantSound, Sound whineSound
    ) {
        return REGISTRY.define(name, data -> new StaticWolfSoundVariant(
                data, ambientSound, deathSound, growlSound, hurtSound, pantSound, whineSound));
    }

    public static final WolfSoundVariant CLASSIC = define("classic", "");
    public static final WolfSoundVariant PUGLIN = define("puglin", "_puglin");
    public static final WolfSoundVariant SAD = define("sad", "_sad");
    public static final WolfSoundVariant ANGRY = define("angry", "_angry");
    public static final WolfSoundVariant GRUMPY = define("grumpy", "_grumpy");
    public static final WolfSoundVariant BIG = define("big", "_big");
    public static final WolfSoundVariant CUTE = define("cute", "_cute");

    static {
        REGISTRY.unloadMappings();
    }
}
