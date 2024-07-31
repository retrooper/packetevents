/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.item.jukebox;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class JukeboxSong extends AbstractMappedEntity implements IJukeboxSong {
    private Sound sound;
    private Component description;
    private float lengthInSeconds;
    private int comparatorOutput;

    public JukeboxSong(Sound sound, Component description, float lengthInSeconds, int comparatorOutput) {
        this(null, sound, description, lengthInSeconds, comparatorOutput);
    }

    public JukeboxSong(@Nullable TypesBuilderData data, Sound sound, Component description, float lengthInSeconds,
            int comparatorOutput) {
        super(data);
        this.sound = sound;
        this.description = description;
        this.lengthInSeconds = lengthInSeconds;
        this.comparatorOutput = comparatorOutput;
    }

    @Override
    public IJukeboxSong copy(@Nullable TypesBuilderData newData) {
        return new JukeboxSong(newData, sound, description, lengthInSeconds, comparatorOutput);
    }

    @Override
    public Sound getSound() {
        return this.sound;
    }

    @Override
    public void setSound(Sound sound) {
        this.sound = sound;
    }

    @Override
    public Component getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(Component description) {
        this.description = description;
    }

    @Override
    public float getLengthInSeconds() {
        return this.lengthInSeconds;
    }

    @Override
    public void setLengthInSeconds(float lengthInSeconds) {
        this.lengthInSeconds = lengthInSeconds;
    }

    @Override
    public int getComparatorOutput() {
        return this.comparatorOutput;
    }

    @Override
    public void setComparatorOutput(int comparatorOutput) {
        this.comparatorOutput = comparatorOutput;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        JukeboxSong that = (JukeboxSong) object;
        return Float.compare(lengthInSeconds, that.lengthInSeconds) == 0 && comparatorOutput == that.comparatorOutput &&
                Objects.equals(sound, that.sound) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sound, description, lengthInSeconds, comparatorOutput);
    }
}
