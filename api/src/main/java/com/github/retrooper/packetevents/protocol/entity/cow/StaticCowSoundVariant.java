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

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
public class StaticCowSoundVariant extends AbstractMappedEntity implements CowSoundVariant {

    private final CowSoundSet sounds;

    public StaticCowSoundVariant(CowSoundSet sounds) {
        this(null, sounds);
    }

    @ApiStatus.Internal
    public StaticCowSoundVariant(@Nullable TypesBuilderData data, CowSoundSet sounds) {
        super(data);
        this.sounds = sounds;
    }

    @Override
    public CowSoundSet getSounds() {
        return this.sounds;
    }

    @Override
    public CowSoundVariant copy(@Nullable TypesBuilderData newData) {
        return new StaticCowSoundVariant(newData, this.sounds);
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof StaticCowSoundVariant)) return false;
        if (!super.equals(obj)) return false;
        StaticCowSoundVariant that = (StaticCowSoundVariant) obj;
        return this.sounds.equals(that.sounds);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.sounds);
    }
}
