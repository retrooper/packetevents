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

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 1.21.5+
 */
@NullMarked
public class StaticWolfSoundVariant extends AbstractMappedEntity implements WolfSoundVariant {

    /**
     * @versions 26.1+
     */
    private final WolfSoundSet adultSounds;
    /**
     * @versions 26.1+
     */
    private final WolfSoundSet babySounds;

    /**
     * @versions 1.21.5-1.21.11
     */
    @Deprecated
    public StaticWolfSoundVariant(
            Sound ambientSound, Sound deathSound, Sound growlSound,
            Sound hurtSound, Sound pantSound, Sound whineSound
    ) {
        this(new WolfSoundSet(ambientSound, deathSound, growlSound, hurtSound, pantSound, whineSound, Sounds.ENTITY_WOLF_STEP));
    }

    /**
     * @versions 1.21.5-1.21.11
     */
    @ApiStatus.Obsolete
    public StaticWolfSoundVariant(WolfSoundSet sounds) {
        this(null, sounds, sounds);
    }

    /**
     * @versions 26.1+
     */
    public StaticWolfSoundVariant(WolfSoundSet adultSounds, WolfSoundSet babySounds) {
        this(null, adultSounds, babySounds);
    }

    @ApiStatus.Internal
    public StaticWolfSoundVariant(
            @Nullable TypesBuilderData data,
            WolfSoundSet adultSounds,
            WolfSoundSet babySounds
    ) {
        super(data);
        this.adultSounds = adultSounds;
        this.babySounds = babySounds;
    }

    @Override
    public WolfSoundVariant copy(@Nullable TypesBuilderData newData) {
        return new StaticWolfSoundVariant(newData, this.adultSounds, this.babySounds);
    }

    /**
     * @versions 26.1+
     */
    @Override
    public WolfSoundSet getAdultSounds() {
        return this.adultSounds;
    }

    /**
     * @versions 26.1+
     */
    @Override
    public WolfSoundSet getBabySounds() {
        return this.babySounds;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof StaticWolfSoundVariant)) return false;
        StaticWolfSoundVariant that = (StaticWolfSoundVariant) obj;
        if (!this.adultSounds.equals(that.adultSounds)) return false;
        return this.babySounds.equals(that.babySounds);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.adultSounds, this.babySounds);
    }
}
