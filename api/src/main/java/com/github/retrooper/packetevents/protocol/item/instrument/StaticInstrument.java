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

package com.github.retrooper.packetevents.protocol.item.instrument;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.resources.ResourceLocation;

import java.util.Objects;

public class StaticInstrument implements Instrument {

    private final Sound sound;
    private final int useDuration;
    private final float range;

    public StaticInstrument(Sound sound, int useDuration, float range) {
        this.sound = sound;
        this.useDuration = useDuration;
        this.range = range;
    }

    @Override
    public Sound getSound() {
        return this.sound;
    }

    @Override
    public int getUseDuration() {
        return this.useDuration;
    }

    @Override
    public float getRange() {
        return this.range;
    }

    @Override
    public ResourceLocation getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getId(ClientVersion version) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRegistered() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticInstrument)) return false;
        StaticInstrument that = (StaticInstrument) obj;
        if (this.useDuration != that.useDuration) return false;
        if (Float.compare(that.range, this.range) != 0) return false;
        return this.sound.equals(that.sound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.sound, this.useDuration, this.range);
    }
}
