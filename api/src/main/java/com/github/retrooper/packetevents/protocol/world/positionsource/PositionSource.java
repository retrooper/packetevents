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

package com.github.retrooper.packetevents.protocol.world.positionsource;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;

public abstract class PositionSource {

    protected final PositionSourceType<?> type;

    public PositionSource(PositionSourceType<?> type) {
        this.type = type;
    }

    public static PositionSource decode(NBT nbt, ClientVersion version) {
        NBTCompound compound = (NBTCompound) nbt;
        String typeId = compound.getStringTagValueOrThrow("type");
        PositionSourceType<?> sourceType = PositionSourceTypes.getByName(typeId);
        if (sourceType == null) {
            throw new IllegalStateException("Can't find position source type with id " + typeId);
        }
        return sourceType.decode(compound, version);
    }

    @SuppressWarnings("unchecked") // should not cause issues if used correctly
    public static NBT encode(PositionSource source, ClientVersion version) {
        return encode(source, (PositionSourceType<? super PositionSource>) source.getType(), version);
    }

    public static <T extends PositionSource> NBT encode(T source, PositionSourceType<T> type, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("type", new NBTString(type.getName().toString()));
        type.encode(source, version, compound);
        return compound;
    }

    public PositionSourceType<?> getType() {
        return this.type;
    }
}
