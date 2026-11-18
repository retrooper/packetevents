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

package com.github.retrooper.packetevents.protocol.valueproviders.ints;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.Random;

/**
 * @versions 1.17+
 */
@NullMarked
public interface IntProvider {

    NbtMapCodec<IntProvider> MAP_CODEC = new NbtMapCodec<IntProvider>() {
        @Override
        public IntProvider decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            IntProviderType<?> type = tag.getOrThrow("type", IntProviderType.CODEC, wrapper);
            return type.getCodec().decode(tag, wrapper);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, IntProvider value) throws NbtCodecException {
            @SuppressWarnings("unchecked")
            IntProviderType<IntProvider> unsafeType = (IntProviderType<IntProvider>) value.getType();
            unsafeType.getCodec().encode(tag, wrapper, value);
            tag.set("type", value.getType(), IntProviderType.CODEC, wrapper);
        }
    };
    NbtCodec<IntProvider> CODEC = new NbtCodec<IntProvider>() {
        private final NbtCodec<IntProvider> delegate = MAP_CODEC.codec();

        @Override
        public IntProvider decode(NBT tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            if (tag instanceof NBTNumber) {
                return new ConstantInt(((NBTNumber) tag).getAsInt());
            }
            return this.delegate.decode(tag, wrapper);
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, IntProvider value) throws NbtCodecException {
            if (value instanceof ConstantInt) {
                return new NBTInt(((ConstantInt) value).getValue());
            }
            return this.delegate.encode(wrapper, value);
        }
    };

    IntProviderType<?> getType();

    int getSample(Random random);

    int getMin();

    int getMax();
}
