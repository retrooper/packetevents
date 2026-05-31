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

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
public class StaticChickenSoundVariant extends AbstractMappedEntity implements ChickenSoundVariant {

    private final ChickenSoundSet adultSounds;
    private final ChickenSoundSet babySounds;

    public StaticChickenSoundVariant(ChickenSoundSet adultSounds, ChickenSoundSet babySounds) {
        this(null, adultSounds, babySounds);
    }

    @ApiStatus.Internal
    public StaticChickenSoundVariant(@Nullable TypesBuilderData data, ChickenSoundSet adultSounds, ChickenSoundSet babySounds) {
        super(data);
        this.adultSounds = adultSounds;
        this.babySounds = babySounds;
    }

    @Override
    public ChickenSoundVariant copy(@Nullable TypesBuilderData newData) {
        return new StaticChickenSoundVariant(newData, this.adultSounds, this.babySounds);
    }

    @Override
    public ChickenSoundSet getAdultSounds() {
        return this.adultSounds;
    }

    @Override
    public ChickenSoundSet getBabySounds() {
        return this.babySounds;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof StaticChickenSoundVariant)) return false;
        if (!super.equals(obj)) return false;
        StaticChickenSoundVariant that = (StaticChickenSoundVariant) obj;
        if (!this.adultSounds.equals(that.adultSounds)) return false;
        return this.babySounds.equals(that.babySounds);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.adultSounds, this.babySounds);
    }
}
