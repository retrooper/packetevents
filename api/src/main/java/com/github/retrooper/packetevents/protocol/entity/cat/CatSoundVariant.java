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

package com.github.retrooper.packetevents.protocol.entity.cat;

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
public interface CatSoundVariant extends MappedEntity, CopyableEntity<CatSoundVariant>, DeepComparableEntity {

    NbtCodec<CatSoundVariant> CODEC = new NbtMapCodec<CatSoundVariant>() {
        @Override
        public CatSoundVariant decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            CatSoundSet adultSounds = tag.getOrThrow("adult_sounds", CatSoundSet.CODEC, wrapper);
            CatSoundSet babySounds = tag.getOrThrow("baby_sounds", CatSoundSet.CODEC, wrapper);
            return new StaticCatSoundVariant(adultSounds, babySounds);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, CatSoundVariant value) throws NbtCodecException {
            tag.set("adult_sounds", value.getAdultSounds(), CatSoundSet.CODEC, wrapper);
            tag.set("baby_sounds", value.getBabySounds(), CatSoundSet.CODEC, wrapper);
        }
    }.codec();

    static CatSoundVariant read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntity(CatSoundVariants.getRegistry());
    }

    static void write(PacketWrapper<?> wrapper, CatSoundVariant variant) {
        wrapper.writeMappedEntity(variant);
    }

    CatSoundSet getAdultSounds();

    CatSoundSet getBabySounds();
}
