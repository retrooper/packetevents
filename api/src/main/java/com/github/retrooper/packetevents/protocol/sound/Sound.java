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

package com.github.retrooper.packetevents.protocol.sound;

import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface Sound extends MappedEntity, DeepComparableEntity {

    NbtCodec<Sound> CODEC = new NbtCodec<Sound>() {
        @Override
        public Sound decode(NBT nbt, PacketWrapper<?> wrapper) {
            if (nbt instanceof NBTString) {
                return Sounds.getByNameOrCreate(((NBTString) nbt).getValue());
            }
            NBTCompound compound = nbt.castOrThrow(NBTCompound.class);
            ResourceLocation soundId = compound.getOrThrow("sound_id", ResourceLocation.CODEC, wrapper);
            Float range = compound.getOrNull("range", NbtCodecs.FLOAT, wrapper);
            return new StaticSound(soundId, range);
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, Sound value) {
            if (value.isRegistered()) {
                return new NBTString(value.getName().toString());
            }
            NBTCompound compound = new NBTCompound();
            compound.set("sound_id", value.getSoundId(), ResourceLocation.CODEC, wrapper);
            if (value.getRange() != null) {
                compound.set("range", value.getRange(), NbtCodecs.FLOAT, wrapper);
            }
            return compound;
        }
    };

    ResourceLocation getSoundId();

    @Nullable
    Float getRange();

    static Sound read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntityOrDirect(Sounds::getById, Sound::readDirect);
    }

    static Sound readDirect(PacketWrapper<?> wrapper) {
        ResourceLocation soundId = wrapper.readIdentifier();
        Float range = wrapper.readOptional(PacketWrapper::readFloat);
        return new StaticSound(soundId, range);
    }

    static void write(PacketWrapper<?> wrapper, Sound sound) {
        wrapper.writeMappedEntityOrDirect(sound, Sound::writeDirect);
    }

    static void writeDirect(PacketWrapper<?> wrapper, Sound sound) {
        wrapper.writeIdentifier(sound.getSoundId());
        wrapper.writeOptional(sound.getRange(), PacketWrapper::writeFloat);
    }

    @Deprecated
    static Sound decode(NBT nbt, ClientVersion version) {
        return decode(nbt, PacketWrapper.createDummyWrapper(version));
    }

    @Deprecated
    static Sound decode(NBT nbt, PacketWrapper<?> wrapper) {
        return CODEC.decode(nbt, wrapper);
    }

    @Deprecated
    static NBT encode(Sound sound, ClientVersion version) {
        return encode(PacketWrapper.createDummyWrapper(version), sound);
    }

    @Deprecated
    static NBT encode(PacketWrapper<?> wrapper, Sound sound) {
        return CODEC.encode(wrapper, sound);
    }
}
