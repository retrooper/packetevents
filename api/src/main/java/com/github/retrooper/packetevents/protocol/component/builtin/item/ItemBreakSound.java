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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class ItemBreakSound {

    private Sound sound;

    public ItemBreakSound(Sound sound) {
        this.sound = sound;
    }

    public static ItemBreakSound read(PacketWrapper<?> wrapper) {
        Sound sound = Sound.read(wrapper);
        return new ItemBreakSound(sound);
    }

    public static void write(PacketWrapper<?> wrapper, ItemBreakSound sound) {
        Sound.write(wrapper, sound.sound);
    }

    public Sound getSound() {
        return this.sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemBreakSound)) return false;
        ItemBreakSound that = (ItemBreakSound) obj;
        return this.sound.equals(that.sound);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.sound);
    }
}
