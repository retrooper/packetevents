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

package com.github.retrooper.packetevents.protocol.util;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NullMarked
public interface NbtCodec<T> extends NbtEncoder<T>, NbtDecoder<T> {

    default <Z> NbtCodec<Z> apply(Function<T, Z> forward, Function<Z, T> back) {
        return new NbtCodec<Z>() {
            @Override
            public Z decode(NBT nbt, PacketWrapper<?> wrapper) {
                return forward.apply(NbtCodec.this.decode(nbt, wrapper));
            }

            @Override
            public NBT encode(PacketWrapper<?> wrapper, Z value) {
                return NbtCodec.this.encode(wrapper, back.apply(value));
            }
        };
    }

    default NbtCodec<List<T>> applyList() {
        return new NbtCodec<List<T>>() {
            @Override
            public List<T> decode(NBT nbt, PacketWrapper<?> wrapper) {
                List<? extends NBT> list = NbtCodecs.GENERIC_LIST.decode(nbt, wrapper);
                List<T> ret = new ArrayList<>(list.size());
                for (NBT tag : list) {
                    ret.add(NbtCodec.this.decode(tag, wrapper));
                }
                return ret;
            }

            @Override
            public NBT encode(PacketWrapper<?> wrapper, List<T> value) {
                List<NBT> list = new ArrayList<>(value.size());
                for (T ele : value) {
                    list.add(NbtCodec.this.encode(wrapper, ele));
                }
                return NbtCodecs.GENERIC_LIST.encode(wrapper, list);
            }
        };
    }
}
