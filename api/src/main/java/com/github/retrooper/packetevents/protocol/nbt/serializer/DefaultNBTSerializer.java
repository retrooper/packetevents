/*
 * This file is part of ProtocolSupport - https://github.com/ProtocolSupport/ProtocolSupport
 * Copyright (C) 2021 ProtocolSupport
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.protocol.nbt.serializer;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTByteArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import com.github.retrooper.packetevents.protocol.nbt.NBTLongArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import org.jetbrains.annotations.ApiStatus;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

public class DefaultNBTSerializer extends NBTSerializer<DataInput, DataOutput> {

    @ApiStatus.Internal
    public static final int OBJECT_HEADER_BYTES = 8;
    @ApiStatus.Internal
    public static final int ARRAY_HEADER_BYTES = 12;
    @ApiStatus.Internal
    public static final int OBJECT_REF_BYTES = 4;
    @ApiStatus.Internal
    public static final int STRING_SIZE_BYTES = 28;

    public static final DefaultNBTSerializer INSTANCE = new DefaultNBTSerializer();

    @SuppressWarnings("unchecked")
    public DefaultNBTSerializer() {
        super(
                (limiter, dataInput) -> dataInput.readByte(),
                DataOutput::writeByte,
                (limiter, dataInput) -> {
                    // only used for skipping strings
                    dataInput.skipBytes(dataInput.readUnsignedShort());
                    return "";
                },
                DataOutput::writeUTF
        );
        registerType(NBTType.END, 0, (limiter, stream) -> {
            limiter.increment(OBJECT_HEADER_BYTES);
            return NBTEnd.INSTANCE;
        }, (stream, tag) -> {
        });
        registerType(NBTType.BYTE, 1, (limiter, stream) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Byte.BYTES);
            return new NBTByte(stream.readByte());
        }, (stream, tag) -> stream.writeByte(tag.getAsByte()));
        registerType(NBTType.SHORT, 2, (limiter, stream) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Short.SIZE);
            return new NBTShort(stream.readShort());
        }, (stream, tag) -> stream.writeShort(tag.getAsShort()));
        registerType(NBTType.INT, 3, (limiter, stream) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Integer.BYTES);
            return new NBTInt(stream.readInt());
        }, (stream, tag) -> stream.writeInt(tag.getAsInt()));
        registerType(NBTType.LONG, 4, (limiter, stream) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Long.BYTES);
            return new NBTLong(stream.readLong());
        }, (stream, tag) -> stream.writeLong(tag.getAsLong()));
        registerType(NBTType.FLOAT, 5, (limiter, stream) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Float.BYTES);
            return new NBTFloat(stream.readFloat());
        }, (stream, tag) -> stream.writeFloat(tag.getAsFloat()));
        registerType(NBTType.DOUBLE, 6, (limiter, stream) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Double.BYTES);
            return new NBTDouble(stream.readDouble());
        }, (stream, tag) -> stream.writeDouble(tag.getAsDouble()));
        registerType(
                NBTType.BYTE_ARRAY, 7,
                (limiter, stream) -> {
                    limiter.increment(OBJECT_HEADER_BYTES + ARRAY_HEADER_BYTES + Integer.BYTES);
                    int length = stream.readInt();
                    if (length >= 1 << 24) {
                        throw new IllegalArgumentException("Byte array length is too large: " + length);
                    }
                    limiter.increment(Byte.BYTES * length);
                    limiter.checkReadability(Byte.BYTES * length);

                    byte[] array = new byte[length];
                    stream.readFully(array);
                    return new NBTByteArray(array);
                },
                (stream, tag) -> {
                    byte[] array = tag.getValue();
                    stream.writeInt(array.length);
                    stream.write(array);
                }
        );
        registerType(NBTType.STRING, 8, (limiter, stream) -> {
            limiter.increment(OBJECT_HEADER_BYTES + STRING_SIZE_BYTES);
            String string = stream.readUTF();
            limiter.increment(string.length() * Character.BYTES);
            return new NBTString(string);
        }, (stream, tag) -> stream.writeUTF(tag.getValue()));
        registerType(
                NBTType.LIST, 9,
                (limiter, stream) -> {
                    limiter.enterDepth();
                    try {
                        limiter.increment(OBJECT_HEADER_BYTES + OBJECT_REF_BYTES // list tag
                                + OBJECT_HEADER_BYTES + ARRAY_HEADER_BYTES + Integer.BYTES); // arraylist
                        NBTType<? extends NBT> valueType = readTagType(limiter, stream);
                        int size = stream.readInt();
                        if (valueType == NBTType.END && size > 0) {
                            throw new IllegalStateException("Missing nbt list values tag type");
                        }
                        limiter.increment(OBJECT_REF_BYTES * size);
                        NBTList<NBT> list = new NBTList<>((NBTType<NBT>) valueType, size);
                        for (int i = 0; i < size; i++) {
                            list.addTag(readTag(limiter, stream, valueType));
                        }
                        return list;
                    } finally {
                        limiter.exitDepth();
                    }
                },
                (stream, tag) -> {
                    writeTagType(stream, tag.getTagsType());
                    stream.writeInt(tag.size());
                    for (NBT value : ((List<NBT>) tag.getTags())) {
                        writeTag(stream, value);
                    }
                }
        );
        registerType(
                NBTType.COMPOUND, 10,
                (limiter, stream) -> {
                    limiter.enterDepth();
                    try {
                        limiter.increment(OBJECT_HEADER_BYTES + OBJECT_REF_BYTES // compound tag
                                + OBJECT_HEADER_BYTES + ARRAY_HEADER_BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES + Float.BYTES); // hashmap
                        NBTCompound compound = new NBTCompound();
                        NBTType<?> valueType;
                        while ((valueType = readTagType(limiter, stream)) != NBTType.END) {
                            String name = readString(limiter, stream);
                            NBT tag = readTag(limiter, stream, valueType);
                            if (!compound.getTags().containsKey(name)) {
                                limiter.increment(12 + OBJECT_HEADER_BYTES + Integer.BYTES + OBJECT_REF_BYTES + OBJECT_REF_BYTES + OBJECT_REF_BYTES);
                            }
                            compound.setTag(name, tag);
                        }
                        return compound;
                    } finally {
                        limiter.exitDepth();
                    }
                },
                (stream, tag) -> {
                    for (Entry<String, NBT> entry : tag.getTags().entrySet()) {
                        NBT value = entry.getValue();
                        writeTagType(stream, value.getType());
                        writeTagName(stream, entry.getKey());
                        writeTag(stream, value);
                    }
                    writeTagType(stream, NBTType.END);
                }
        );
        registerType(
                NBTType.INT_ARRAY, 11,
                (limiter, stream) -> {
                    limiter.increment(OBJECT_HEADER_BYTES + ARRAY_HEADER_BYTES + Integer.BYTES);
                    int length = stream.readInt();
                    if (length >= 1 << 24) {
                        throw new IllegalArgumentException("Int array length is too large: " + length);
                    }
                    limiter.increment(length * Integer.BYTES);
                    limiter.checkReadability(length * Integer.BYTES);

                    int[] array = new int[length];
                    for (int i = 0; i < array.length; i++) {
                        array[i] = stream.readInt();
                    }
                    return new NBTIntArray(array);
                },
                (stream, tag) -> {
                    int[] array = tag.getValue();
                    stream.writeInt(array.length);
                    for (int i : array) {
                        stream.writeInt(i);
                    }
                }
        );
        registerType(
                NBTType.LONG_ARRAY, 12,
                (limiter, stream) -> {
                    limiter.increment(OBJECT_HEADER_BYTES + ARRAY_HEADER_BYTES + Integer.BYTES);
                    int length = stream.readInt();
                    if (length >= 1 << 24) {
                        throw new IllegalArgumentException("Long array length is too large: " + length);
                    }
                    limiter.increment(length * Long.BYTES);
                    limiter.checkReadability(length * Long.BYTES);

                    long[] array = new long[length];
                    for (int i = 0; i < array.length; i++) {
                        array[i] = stream.readLong();
                    }
                    return new NBTLongArray(array);
                },
                (stream, tag) -> {
                    long[] array = tag.getValue();
                    stream.writeInt(array.length);
                    for (long i : array) {
                        stream.writeLong(i);
                    }
                }
        );
    }

    @ApiStatus.Internal
    public static String readString(NBTLimiter limiter, DataInput input) throws IOException {
        String string = input.readUTF();
        limiter.increment(STRING_SIZE_BYTES + Character.BYTES * string.length());
        return string;
    }
}
