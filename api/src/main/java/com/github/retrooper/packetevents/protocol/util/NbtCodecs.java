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
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTByteArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import com.github.retrooper.packetevents.protocol.nbt.NBTLongArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import com.github.retrooper.packetevents.util.UniqueIdUtil;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@NullMarked
@ApiStatus.Experimental
public final class NbtCodecs {

    public static final NbtCodec<String> STRING = new NbtCodec<String>() {
        @Override
        public String decode(NBT nbt, PacketWrapper<?> wrapper) {
            return ((NBTString) nbt).getValue();
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, String value) {
            return new NBTString(value);
        }
    };
    public static final NbtCodec<List<String>> STRING_LIST = STRING.applyList();

    // this is the part of codecs I absolutely hate... there are a billion possible ways
    // to represent a single damn thing with nbt/codecs...
    // this codec converts the billion possible ways into a simple list of tags (at the cost
    // of creating way too many nbt tag objects)
    public static final NbtCodec<List<? extends NBT>> GENERIC_LIST = new NbtCodec<List<? extends NBT>>() {
        @Override
        public List<? extends NBT> decode(NBT nbt, PacketWrapper<?> wrapper) {
            if (nbt instanceof NBTList) {
                return ((NBTList<?>) nbt).unwrapTags();
            } else if (nbt instanceof NBTIntArray) {
                int[] arr = ((NBTIntArray) nbt).getValue();
                List<NBTInt> list = new ArrayList<>(arr.length);
                for (int num : arr) {
                    list.add(new NBTInt(num));
                }
                return list;
            } else if (nbt instanceof NBTByteArray) {
                byte[] arr = ((NBTByteArray) nbt).getValue();
                List<NBTByte> list = new ArrayList<>(arr.length);
                for (byte num : arr) {
                    list.add(new NBTByte(num));
                }
                return list;
            } else if (nbt instanceof NBTLongArray) {
                long[] arr = ((NBTLongArray) nbt).getValue();
                List<NBTLong> list = new ArrayList<>(arr.length);
                for (long num : arr) {
                    list.add(new NBTLong(num));
                }
                return list;
            }
            throw new RuntimeException("Not a list: " + nbt);
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, List<? extends NBT> value) {
            if (value.isEmpty()) {
                return new NBTList<>(NBTType.END, 0);
            }
            NBTType<?> type = NBTList.getCommonTagType(value);
            if (type == NBTType.COMPOUND) {
                // possibly heterogeneous list, try to wrap everything
                NBTList<NBTCompound> list = new NBTList<>(NBTType.COMPOUND, value.size());
                for (NBT tag : value) {
                    list.addTagOrWrap(tag);
                }
                return list;
            } else if (type == NBTType.INT) {
                // encode as int array
                int[] arr = new int[value.size()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = ((NBTInt) value.get(i)).getAsInt();
                }
                return new NBTIntArray(arr);
            } else if (type == NBTType.BYTE) {
                // encode as byte array
                byte[] arr = new byte[value.size()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = ((NBTByte) value.get(i)).getAsByte();
                }
                return new NBTByteArray(arr);
            } else if (type == NBTType.LONG) {
                // encode as long array
                long[] arr = new long[value.size()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = ((NBTLong) value.get(i)).getAsLong();
                }
                return new NBTLongArray(arr);
            } else {
                // a single common list type, simple to construct
                @SuppressWarnings("unchecked") // doesn't matter at runtime
                NBTList<?> list = new NBTList<>((NBTType<NBT>) type, (List<NBT>) value);
                return list;
            }
        }
    };

    public static final NbtCodec<int[]> INT_ARRAY = new NbtCodec<int[]>() {
        @Override
        public int[] decode(NBT nbt, PacketWrapper<?> wrapper) {
            if (nbt instanceof NBTIntArray) {
                return ((NBTIntArray) nbt).getValue();
            }
            List<? extends NBT> list = GENERIC_LIST.decode(nbt, wrapper);
            int[] array = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                NBT tag = list.get(i);
                if (!(tag instanceof NBTNumber)) {
                    throw new RuntimeException("Some elements are not numbers: " + list);
                }
                array[i] = ((NBTNumber) tag).getAsInt();
            }
            return array;
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, int[] value) {
            return new NBTIntArray(value);
        }
    };

    public static final NbtCodec<UUID> UUID = INT_ARRAY
            .apply(UniqueIdUtil::fromIntArray, UniqueIdUtil::toIntArray);

    private NbtCodecs() {
    }

    public static <T extends Enum<T> & CodecNameable> NbtCodec<T> forEnum(T[] values) {
        return new NbtCodec<T>() {
            private final Map<String, T> map = new HashMap<>(values.length);

            {
                for (T value : values) {
                    T existingValue = this.map.putIfAbsent(value.getCodecName(), value);
                    if (existingValue != null) {
                        throw new IllegalStateException("Can't create codec for enum with duplicate names: " + existingValue);
                    }
                }
            }

            @Override
            public T decode(NBT nbt, PacketWrapper<?> wrapper) {
                String key = ((NBTString) nbt).getValue();
                T value = this.map.get(key);
                if (value == null) {
                    throw new RuntimeException("Can't find " + key + " in " + this.map.keySet());
                }
                return value;
            }

            @Override
            public NBT encode(PacketWrapper<?> wrapper, T value) {
                return new NBTString(value.getCodecName());
            }
        };
    }
}
