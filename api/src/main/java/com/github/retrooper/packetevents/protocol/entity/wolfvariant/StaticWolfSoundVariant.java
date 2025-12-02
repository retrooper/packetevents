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
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StaticWolfSoundVariant extends AbstractMappedEntity implements WolfSoundVariant {

    private final Sound ambientSound;
    private final Sound deathSound;
    private final Sound growlSound;
    private final Sound hurtSound;
    private final Sound pantSound;
    private final Sound whineSound;

    public StaticWolfSoundVariant(
            Sound ambientSound, Sound deathSound, Sound growlSound,
            Sound hurtSound, Sound pantSound, Sound whineSound
    ) {
        this(null, ambientSound, deathSound, growlSound, hurtSound, pantSound, whineSound);
    }

    @ApiStatus.Internal
    public StaticWolfSoundVariant(
            @Nullable TypesBuilderData data, Sound ambientSound, Sound deathSound,
            Sound growlSound, Sound hurtSound, Sound pantSound, Sound whineSound
    ) {
        super(data);
        this.ambientSound = ambientSound;
        this.deathSound = deathSound;
        this.growlSound = growlSound;
        this.hurtSound = hurtSound;
        this.pantSound = pantSound;
        this.whineSound = whineSound;
    }

    @Override
    public WolfSoundVariant copy(@Nullable TypesBuilderData newData) {
        return new StaticWolfSoundVariant(newData, this.ambientSound, this.deathSound,
                this.growlSound, this.hurtSound, this.pantSound, this.whineSound);
    }

    @Override
    public Sound getAmbientSound() {
        return this.ambientSound;
    }

    @Override
    public Sound getDeathSound() {
        return this.deathSound;
    }

    @Override
    public Sound getGrowlSound() {
        return this.growlSound;
    }

    @Override
    public Sound getHurtSound() {
        return this.hurtSound;
    }

    @Override
    public Sound getPantSound() {
        return this.pantSound;
    }

    @Override
    public Sound getWhineSound() {
        return this.whineSound;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (!(obj instanceof StaticWolfSoundVariant)) return false;
        if (!super.equals(obj)) return false;
        StaticWolfSoundVariant that = (StaticWolfSoundVariant) obj;
        if (!this.ambientSound.equals(that.ambientSound)) return false;
        if (!this.deathSound.equals(that.deathSound)) return false;
        if (!this.growlSound.equals(that.growlSound)) return false;
        if (!this.hurtSound.equals(that.hurtSound)) return false;
        if (!this.pantSound.equals(that.pantSound)) return false;
        return this.whineSound.equals(that.whineSound);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.ambientSound, this.deathSound, this.growlSound, this.hurtSound, this.pantSound, this.whineSound);
    }
}
