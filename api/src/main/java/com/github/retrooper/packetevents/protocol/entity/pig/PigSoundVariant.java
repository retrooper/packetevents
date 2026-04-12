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

package com.github.retrooper.packetevents.protocol.entity.pig;

import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 26.1
 */
@NullMarked
public interface PigSoundVariant extends MappedEntity, CopyableEntity<PigSoundVariant>, DeepComparableEntity {

    NbtCodec<PigSoundVariant> CODEC = new NbtMapCodec<PigSoundVariant>() {
        @Override
        public PigSoundVariant decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            PigSoundSet adultSounds = tag.getOrThrow("adult_sounds", PigSoundSet.CODEC, wrapper);
            PigSoundSet babySounds = tag.getOrThrow("baby_sounds", PigSoundSet.CODEC, wrapper);
            return new StaticPigSoundVariant(adultSounds, babySounds);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, PigSoundVariant value) throws NbtCodecException {
            tag.set("adult_sounds", value.getAdultSounds(), PigSoundSet.CODEC, wrapper);
            tag.set("baby_sounds", value.getBabySounds(), PigSoundSet.CODEC, wrapper);
        }
    }.codec();

    static PigSoundVariant read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntity(PigSoundVariants.getRegistry());
    }

    static void write(PacketWrapper<?> wrapper, PigSoundVariant variant) {
        wrapper.writeMappedEntity(variant);
    }

    PigSoundSet getAdultSounds();

    PigSoundSet getBabySounds();
}
