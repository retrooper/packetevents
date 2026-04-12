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

import com.github.retrooper.packetevents.protocol.color.AlphaColor;
import com.github.retrooper.packetevents.protocol.color.Color;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTByteArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import com.github.retrooper.packetevents.protocol.nbt.NBTLongArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Either;
import com.github.retrooper.packetevents.util.UniqueIdUtil;
import com.github.retrooper.packetevents.util.mappings.IRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@NullMarked
@ApiStatus.Experimental
public final class NbtCodecs {

    public static final NbtCodec<Integer> INT = new NbtCodec<Integer>() {
        @Override
        public Integer decode(NBT nbt, PacketWrapper<?> wrapper) {
            return nbt.castOrThrow(NBTNumber.class).getAsInt();
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, Integer value) {
            return new NBTInt(value);
        }
    };
    public static final NbtCodec<Double> DOUBLE = new NbtCodec<Double>() {
        @Override
        public Double decode(NBT nbt, PacketWrapper<?> wrapper) {
            return nbt.castOrThrow(NBTNumber.class).getAsDouble();
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, Double value) {
            return new NBTDouble(value);
        }
    };
    public static final NbtCodec<Float> FLOAT = new NbtCodec<Float>() {
        @Override
        public Float decode(NBT nbt, PacketWrapper<?> wrapper) {
            return nbt.castOrThrow(NBTNumber.class).getAsFloat();
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, Float value) {
            return new NBTFloat(value);
        }
    };
    public static final NbtCodec<Boolean> BOOLEAN = new NbtCodec<Boolean>() {
        @Override
        public Boolean decode(NBT nbt, PacketWrapper<?> wrapper) {
            return nbt.castOrThrow(NBTNumber.class).getAsByte() != 0;
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, Boolean value) {
            return new NBTByte(value);
        }
    };

    public static final NbtCodec<String> STRING = new NbtCodec<String>() {
        @Override
        public String decode(NBT nbt, PacketWrapper<?> wrapper) {
            return nbt.castOrThrow(NBTString.class).getValue();
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
            throw new NbtCodecException("Not a list: " + nbt);
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
            int size = list.size();
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = list.get(i).castOrThrow(NBTNumber.class).getAsInt();
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
    public static final NbtCodec<UUID> STRING_UUID = new NbtCodec<UUID>() {
        @Override
        public UUID decode(NBT nbt, PacketWrapper<?> wrapper) throws NbtCodecException {
            String uuidStr = ((NBTString) nbt).getValue();
            if (uuidStr.length() == 36) { // fast length check
                try {
                    // try parsing uuid
                    return java.util.UUID.fromString(uuidStr);
                } catch (IllegalArgumentException ignored) {
                }
            }
            throw new NbtCodecException("Invalid UUID " + uuidStr);
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, UUID value) throws NbtCodecException {
            return new NBTString(value.toString());
        }
    };
    public static final NbtCodec<UUID> LENIENT_UUID = UUID.withAlternative(STRING_UUID);

    public static final NbtCodec<Color> RGB_COLOR = new NbtCodec<Color>() {
        @Override
        public Color decode(NBT nbt, PacketWrapper<?> wrapper) {
            if (nbt instanceof NBTString) {
                String string = ((NBTString) nbt).getValue();
                if (string.isEmpty() || string.charAt(0) != '#') {
                    throw new NbtCodecException("Hex color must begin with #");
                } else if (string.length() - 1 != 6) {
                    throw new NbtCodecException("Hex color is wrong, expected 6 digits but got " + string);
                }
                try {
                    String digits = string.substring(1);
                    int rgb = Integer.parseInt(digits, 16);
                    return new Color(rgb);
                } catch (NumberFormatException exception) {
                    throw new NbtCodecException(exception);
                }
            } else if (nbt instanceof NBTNumber) {
                return new Color(((NBTNumber) nbt).getAsInt());
            }
            return Color.WHITE; // TODO vector, oh yeah!
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, Color value) {
            return new NBTInt(value.asRGB());
        }
    };
    public static final NbtCodec<AlphaColor> ARGB_COLOR = new NbtCodec<AlphaColor>() {
        @Override
        public AlphaColor decode(NBT nbt, PacketWrapper<?> wrapper) {
            if (nbt instanceof NBTString) {
                String string = ((NBTString) nbt).getValue();
                if (string.isEmpty() || string.charAt(0) != '#') {
                    throw new NbtCodecException("Hex color must begin with #");
                } else if (string.length() - 1 != 8) {
                    throw new NbtCodecException("Hex color is wrong, expected 8 digits but got " + string);
                }
                try {
                    String digits = string.substring(1);
                    int rgb = Integer.parseUnsignedInt(digits, 16);
                    return new AlphaColor(rgb);
                } catch (NumberFormatException exception) {
                    throw new NbtCodecException(exception);
                }
            } else if (nbt instanceof NBTNumber) {
                return new AlphaColor(((NBTNumber) nbt).getAsInt());
            }
            return AlphaColor.WHITE; // TODO vector, oh yeah!
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, AlphaColor value) {
            return new NBTInt(value.asRGB());
        }
    };

    public static final NbtCodec<NBT> NOOP = new NbtCodec<NBT>() {
        @Override
        public NBT decode(NBT nbt, PacketWrapper<?> wrapper) {
            return nbt;
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, NBT value) {
            return value;
        }
    };

    private static final NbtCodec<?> ERROR_CODEC = new NbtCodec<Object>() {
        @Override
        public Object decode(NBT nbt, PacketWrapper<?> wrapper) {
            throw new UnsupportedOperationException();
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, Object value) {
            throw new UnsupportedOperationException();
        }
    };

    private NbtCodecs() {
    }

    /**
     * @return a codec which always throws {@link UnsupportedOperationException} when called
     */
    @SuppressWarnings("unchecked") // safe to cast, type isn't used
    public static <T> NbtCodec<T> errorCodec() {
        return (NbtCodec<T>) ERROR_CODEC;
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
                    throw new NbtCodecException("Can't find " + key + " in " + this.map.keySet());
                }
                return value;
            }

            @Override
            public NBT encode(PacketWrapper<?> wrapper, T value) {
                return new NBTString(value.getCodecName());
            }
        };
    }

    public static <T extends MappedEntity> NbtCodec<T> forRegistry(IRegistry<T> registry) {
        return new NbtCodec<T>() {
            @Override
            public T decode(NBT nbt, PacketWrapper<?> wrapper) {
                IRegistry<T> replacedRegistry = wrapper.replaceRegistry(registry);
                T entry = null;
                if (nbt instanceof NBTNumber) {
                    ClientVersion version = wrapper.getServerVersion().toClientVersion();
                    int id = ((NBTNumber) nbt).getAsInt();
                    entry = replacedRegistry.getById(version, id);
                } else if (nbt instanceof NBTString) {
                    entry = replacedRegistry.getByName(((NBTString) nbt).getValue());
                }
                if (entry == null) {
                    throw new NbtCodecException("Can't decode registry " + registry.getRegistryKey());
                }
                return entry;
            }

            @Override
            public NBT encode(PacketWrapper<?> wrapper, T value) {
                if (!value.isRegistered()) {
                    throw new NbtCodecException("Unregistered entry");
                }
                return ResourceLocation.CODEC.encode(wrapper, value.getName());
            }
        };
    }

    public static <L, R> NbtCodec<Either<L, R>> either(NbtCodec<L> left, NbtCodec<R> right) {
        return new NbtCodec<Either<L, R>>() {
            @Override
            public Either<L, R> decode(NBT nbt, PacketWrapper<?> wrapper) throws NbtCodecException {
                try {
                    return Either.createLeft(left.decode(nbt, wrapper));
                } catch (NbtCodecException leftException) {
                    try {
                        return Either.createRight(right.decode(nbt, wrapper));
                    } catch (NbtCodecException rightException) {
                        rightException.addSuppressed(leftException);
                        throw rightException;
                    }
                }
            }

            @Override
            public NBT encode(PacketWrapper<?> wrapper, Either<L, R> value) throws NbtCodecException {
                return value.map(
                        v -> left.encode(wrapper, v),
                        v -> right.encode(wrapper, v)
                );
            }
        };
    }

    public static <T> NbtMapCodec<T> forUnit(Supplier<T> supplier) {
        return new NbtMapCodec<T>() {
            @Override
            public T decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
                return supplier.get();
            }

            @Override
            public void encode(NBTCompound tag, PacketWrapper<?> wrapper, T value) throws NbtCodecException {
                // NO-OP
            }
        };
    }
}
