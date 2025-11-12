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

package com.github.retrooper.packetevents.protocol.dialog;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class StaticDialogType<T extends Dialog> extends AbstractMappedEntity implements DialogType<T> {

    private final NbtMapDecoder<T> decoder;
    private final NbtMapEncoder<T> encoder;

    @ApiStatus.Internal
    public StaticDialogType(@Nullable TypesBuilderData data, NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder) {
        super(data);
        this.decoder = decoder;
        this.encoder = encoder;
    }

    @Override
    public T decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        return this.decoder.decode(compound, wrapper);
    }

    @Override
    public void encode(NBTCompound compound, PacketWrapper<?> wrapper, T dialog) {
        this.encoder.encode(compound, wrapper, dialog);
    }
}
