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

import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

import java.util.Objects;

public class JukeboxSong {

    private Sound sound;
    private Component description;
    private float lengthInSeconds;
    private int comparatorOutput;

    public JukeboxSong(Sound sound, Component description, float lengthInSeconds, int comparatorOutput) {
        this.sound = sound;
        this.description = description;
        this.lengthInSeconds = lengthInSeconds;
        this.comparatorOutput = comparatorOutput;
    }

    public static JukeboxSong read(PacketWrapper<?> wrapper) {
        Sound sound = Sound.read(wrapper);
        Component description = wrapper.readComponent();
        float lengthInSeconds = wrapper.readFloat();
        int comparatorOutput = wrapper.readVarInt();
        return new JukeboxSong(sound, description, lengthInSeconds, comparatorOutput);
    }

    public static void write(PacketWrapper<?> wrapper, JukeboxSong song) {
        Sound.write(wrapper, song.sound);
        wrapper.writeComponent(song.description);
        wrapper.writeFloat(song.lengthInSeconds);
        wrapper.writeVarInt(song.comparatorOutput);
    }

    public Sound getSound() {
        return this.sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public Component getDescription() {
        return this.description;
    }

    public void setDescription(Component description) {
        this.description = description;
    }

    public float getLengthInSeconds() {
        return this.lengthInSeconds;
    }

    public void setLengthInSeconds(float lengthInSeconds) {
        this.lengthInSeconds = lengthInSeconds;
    }

    public int getComparatorOutput() {
        return this.comparatorOutput;
    }

    public void setComparatorOutput(int comparatorOutput) {
        this.comparatorOutput = comparatorOutput;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof JukeboxSong)) return false;
        JukeboxSong that = (JukeboxSong) obj;
        if (Float.compare(that.lengthInSeconds, this.lengthInSeconds) != 0) return false;
        if (this.comparatorOutput != that.comparatorOutput) return false;
        if (!this.sound.equals(that.sound)) return false;
        return this.description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.sound, this.description, this.lengthInSeconds, this.comparatorOutput);
    }
}
