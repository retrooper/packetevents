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

package com.github.retrooper.packetevents.protocol.world.attributes.timelines;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.21.11+
 */
@NullMarked
public class Keyframe<T> {

    private final int ticks;
    private final T value;

    public Keyframe(int ticks, T value) {
        this.ticks = ticks;
        this.value = value;
    }

    public static <T> NbtCodec<Keyframe<T>> codec(NbtCodec<T> codec) {
        return new NbtMapCodec<Keyframe<T>>() {
            @Override
            public Keyframe<T> decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
                int ticks = compound.getNumberTagOrThrow("ticks").getAsInt();
                T value = compound.getOrThrow("value", codec, wrapper);
                return new Keyframe<>(ticks, value);
            }

            @Override
            public void encode(NBTCompound compound, PacketWrapper<?> wrapper, Keyframe<T> value) throws NbtCodecException {
                compound.setTag("ticks", new NBTInt(value.ticks));
                compound.set("value", value.value, codec, wrapper);
            }
        }.codec();
    }

    public int getTicks() {
        return this.ticks;
    }

    public T getValue() {
        return this.value;
    }
}
