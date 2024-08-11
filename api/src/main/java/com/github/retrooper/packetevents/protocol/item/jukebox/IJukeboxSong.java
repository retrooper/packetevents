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

import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface IJukeboxSong extends MappedEntity, CopyableEntity<IJukeboxSong> {
    Sound getSound();

    Component getDescription();

    float getLengthInSeconds();

    int getComparatorOutput();

    static IJukeboxSong decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        Sound sound = Sound.decode(compound.getTagOrThrow("sound_event"), version);
        Component description = AdventureSerializer.fromNbt(compound.getTagOrThrow("description"));
        float length = compound.getNumberTagOrThrow("length_in_seconds").getAsFloat();
        int comparator_output = compound.getNumberTagOrThrow("comparator_output").getAsInt();
        return new JukeboxSong(data, sound, description, length, comparator_output);
    }

    static NBT encode(IJukeboxSong jukeboxSong, ClientVersion version) {
        NBTCompound compound = new NBTCompound();

        compound.setTag("sound_event", Sound.encode(jukeboxSong.getSound(), version));
        compound.setTag("description", AdventureSerializer.toNbt(jukeboxSong.getDescription()));
        compound.setTag("length_in_seconds", new NBTFloat(jukeboxSong.getLengthInSeconds()));
        compound.setTag("comparator_output", new NBTInt(jukeboxSong.getComparatorOutput()));

        return compound;
    }

    static IJukeboxSong read(PacketWrapper<?> wrapper) {
        Sound sound = Sound.read(wrapper);
        Component description = wrapper.readComponent();
        float lengthInSeconds = wrapper.readFloat();
        int comparatorOutput = wrapper.readVarInt();

        return new JukeboxSong(null, sound, description, lengthInSeconds, comparatorOutput);
    }

    static void write(PacketWrapper<?> wrapper, IJukeboxSong song) {
        Sound.write(wrapper, song.getSound());
        wrapper.writeComponent(song.getDescription());
        wrapper.writeFloat(song.getLengthInSeconds());
        wrapper.writeVarInt(song.getComparatorOutput());
    }
}
