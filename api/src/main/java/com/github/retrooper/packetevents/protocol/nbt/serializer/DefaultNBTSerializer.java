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

import com.github.retrooper.packetevents.protocol.nbt.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.List;
import java.util.Map.Entry;

public class DefaultNBTSerializer extends NBTSerializer<DataInput, DataOutput> {

    public static final DefaultNBTSerializer INSTANCE = new DefaultNBTSerializer();

    @SuppressWarnings("unchecked")
    public DefaultNBTSerializer() {
        super(
                (limiter, dataInput) -> {
                    limiter.increment(1);
                    return dataInput.readByte();
                },
                DataOutput::writeByte,
                (limiter, dataInput) -> {
                    String name = dataInput.readUTF();
                    limiter.increment(name.length() * 2 + 28);
                    return name;
                },
                DataOutput::writeUTF
        );
        registerType(NBTType.END, 0, (limiter, stream) -> {
            limiter.increment(8);
            return NBTEnd.INSTANCE;
        }, (stream, tag) -> {
        });
        registerType(NBTType.BYTE, 1, (limiter, stream) -> {
            limiter.increment(9);
            return new NBTByte(stream.readByte());
        }, (stream, tag) -> stream.writeByte(tag.getAsByte()));
        registerType(NBTType.SHORT, 2, (limiter, stream) -> {
            limiter.increment(10);
            return new NBTShort(stream.readShort());
        }, (stream, tag) -> stream.writeShort(tag.getAsShort()));
        registerType(NBTType.INT, 3, (limiter, stream) -> {
            limiter.increment(12);
            return new NBTInt(stream.readInt());
        }, (stream, tag) -> stream.writeInt(tag.getAsInt()));
        registerType(NBTType.LONG, 4, (limiter, stream) -> {
            limiter.increment(16);
            return new NBTLong(stream.readLong());
        }, (stream, tag) -> stream.writeLong(tag.getAsLong()));
        registerType(NBTType.FLOAT, 5, (limiter, stream) -> {
            limiter.increment(12);
            return new NBTFloat(stream.readFloat());
        }, (stream, tag) -> stream.writeFloat(tag.getAsFloat()));
        registerType(NBTType.DOUBLE, 6, (limiter, stream) -> {
            limiter.increment(16);
            return new NBTDouble(stream.readDouble());
        }, (stream, tag) -> stream.writeDouble(tag.getAsDouble()));
        registerType(
                NBTType.BYTE_ARRAY, 7,
                (limiter, stream) -> {
                    limiter.increment(24);
                    int length = stream.readInt();

                    if (length >= 1 << 24)
                        throw new IllegalArgumentException("Byte array length is too large: " + length);

                    limiter.checkReadability(length);
                    limiter.increment(length);

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
            limiter.increment(36);
            String string = stream.readUTF();
            limiter.increment(string.length() * 2);
            return new NBTString(string);
        }, (stream, tag) -> stream.writeUTF(tag.getValue()));
        registerType(
                NBTType.LIST, 9,
                (limiter, stream) -> {
                    limiter.increment(37);

                    NBTType<? extends NBT> valueType = readTagType(limiter, stream);
                    int size = stream.readInt();

                    if ((valueType == NBTType.END) && (size > 0)) {
                        throw new IllegalStateException("Missing nbt list values tag type");
                    }
                    limiter.increment(4 * size);
                    NBTList<NBT> list = new NBTList<>((NBTType<NBT>) valueType, size);
                    for (int i = 0; i < size; i++) {
                        list.addTag(readTag(limiter, stream, valueType));
                    }
                    return list;
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
                    limiter.increment(48);

                    NBTCompound compound = new NBTCompound();
                    NBTType<?> valueType;
                    while ((valueType = readTagType(limiter, stream)) != NBTType.END) {
                        String name = readTagName(limiter, stream);
                        NBT nbt = readTag(limiter, stream, valueType);
                        if(!compound.getTags().containsKey(name)) limiter.increment(36);
                        compound.setTag(name, nbt);
                    }
                    return compound;
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
                    limiter.increment(24);
                    int length = stream.readInt();

                    if (length >= 1 << 24)
                        throw new IllegalArgumentException("Int array length is too large: " + length);

                    limiter.checkReadability(length * 4);
                    limiter.increment(length * 4);

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
                    limiter.increment(24);
                    int length = stream.readInt();

                    if (length >= 1 << 24)
                        throw new IllegalArgumentException("Long array length is too large: " + length);

                    limiter.checkReadability(length * 8);
                    limiter.increment(length * 8);

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

}
