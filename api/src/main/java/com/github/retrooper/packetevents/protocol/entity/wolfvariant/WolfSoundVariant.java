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

import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

public interface WolfSoundVariant extends MappedEntity, CopyableEntity<WolfSoundVariant>, DeepComparableEntity {

    Sound getAmbientSound();

    Sound getDeathSound();

    Sound getGrowlSound();

    Sound getHurtSound();

    Sound getPantSound();

    Sound getWhineSound();

    static WolfSoundVariant read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntity(WolfSoundVariants.getRegistry());
    }

    static void write(PacketWrapper<?> wrapper, WolfSoundVariant variant) {
        wrapper.writeMappedEntity(variant);
    }

    static WolfSoundVariant decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        Sound ambientSound = Sound.decode(compound.getTagOrThrow("ambient_sound"), version);
        Sound deathSound = Sound.decode(compound.getTagOrThrow("death_sound"), version);
        Sound growlSound = Sound.decode(compound.getTagOrThrow("growl_sound"), version);
        Sound hurtSound = Sound.decode(compound.getTagOrThrow("hurt_sound"), version);
        Sound pantSound = Sound.decode(compound.getTagOrThrow("pant_sound"), version);
        Sound whineSound = Sound.decode(compound.getTagOrThrow("whine_sound"), version);
        return new StaticWolfSoundVariant(data, ambientSound, deathSound, growlSound, hurtSound, pantSound, whineSound);
    }

    static NBT encode(WolfSoundVariant variant, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("ambient_sound", Sound.encode(variant.getAmbientSound(), version));
        compound.setTag("death_sound", Sound.encode(variant.getDeathSound(), version));
        compound.setTag("growl_sound", Sound.encode(variant.getGrowlSound(), version));
        compound.setTag("hurt_sound", Sound.encode(variant.getHurtSound(), version));
        compound.setTag("pant_sound", Sound.encode(variant.getPantSound(), version));
        compound.setTag("whine_sound", Sound.encode(variant.getWhineSound(), version));
        return compound;
    }
}
