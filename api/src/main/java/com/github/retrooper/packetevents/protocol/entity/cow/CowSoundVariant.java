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

import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 26.1
 */
@NullMarked
public interface CowSoundVariant extends MappedEntity, CopyableEntity<CowSoundVariant>, DeepComparableEntity {

    NbtCodec<CowSoundVariant> CODEC = CowSoundSet.CODEC
            .apply(StaticCowSoundVariant::new, CowSoundVariant::getSounds);

    static CowSoundVariant read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntity(CowSoundVariants.getRegistry());
    }

    static void write(PacketWrapper<?> wrapper, CowSoundVariant variant) {
        wrapper.writeMappedEntity(variant);
    }

    CowSoundSet getSounds();
}
