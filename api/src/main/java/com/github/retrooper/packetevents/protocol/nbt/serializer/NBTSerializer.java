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
import com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class NBTSerializer<IN, OUT> implements NBTReader<NBT, IN>, NBTWriter<NBT, OUT> {

    protected final IdReader<IN> idReader;
    protected final IdWriter<OUT> idWriter;
    protected final NameReader<IN> nameReader;
    protected final NameWriter<OUT> nameWriter;
    protected final Map<Integer, NBTType<? extends NBT>> idToType = new HashMap<>();
    protected final Map<NBTType<? extends NBT>, Integer> typeToId = new HashMap<>();
    protected final Map<NBTType<? extends NBT>, TagReader<IN, ? extends NBT>> typeReaders = new HashMap<>();
    protected final Map<NBTType<? extends NBT>, TagWriter<OUT, ? extends NBT>> typeWriters = new HashMap<>();
    public NBTSerializer(
            IdReader<IN> idReader, IdWriter<OUT> idWriter,
            NameReader<IN> nameReader, NameWriter<OUT> nameWriter
    ) {
        this.idReader = idReader;
        this.idWriter = idWriter;
        this.nameReader = nameReader;
        this.nameWriter = nameWriter;
    }

    @Override
    public NBT deserializeTag(NBTLimiter limiter, IN from, boolean named) throws IOException {
        NBTType<?> type = readTagType(limiter, from);
        if (type == NBTType.END) {
            return null;
        }
        if (named) {
            readTagName(limiter, from);
        }
        return readTag(limiter, from, type);
    }

    @Override
    public void serializeTag(OUT to, NBT tag, boolean named) throws IOException {
        NBTType<?> type = tag.getType();
        writeTagType(to, type);
        if (tag.getType() == NBTType.END) {
            return;
        }
        if (named) {
            writeTagName(to, "");
        }
        writeTag(to, tag);
    }

    protected <T extends NBT> void registerType(
            NBTType<T> type, int id,
            TagReader<IN, T> typeReader,
            TagWriter<OUT, T> typeWriter
    ) {
        if (typeToId.containsKey(type)) {
            throw new IllegalArgumentException(MessageFormat.format("Nbt type {0} is already registered", type));
        }
        if (idToType.containsKey(id)) {
            throw new IllegalArgumentException(MessageFormat.format("Nbt type id {0} is already registered", id));
        }
        idToType.put(id, type);
        typeToId.put(type, id);
        typeReaders.put(type, typeReader);
        typeWriters.put(type, typeWriter);
    }

    NBTType<?> readTagType(NBTLimiter limiter, IN from) throws IOException {
        int id = idReader.readId(limiter, from);
        NBTType<?> type = idToType.get(id);
        if (type == null) {
            throw new IOException(MessageFormat.format("Unknown nbt type id {0}", id));
        }
        return type;
    }

    String readTagName(NBTLimiter limiter, IN from) throws IOException {
        return nameReader.readName(limiter, from);
    }

    NBT readTag(NBTLimiter limiter, IN from, NBTType<?> type) throws IOException {
        TagReader<IN, ? extends NBT> f = typeReaders.get(type);
        if (f == null) {
            throw new IOException(MessageFormat.format("No reader registered for nbt type {0}", type));
        }
        return f.readTag(limiter, from);
    }

    void writeTagType(OUT stream, NBTType<?> type) throws IOException {
        int id = typeToId.getOrDefault(type, -1);
        if (id == -1) {
            throw new IOException(MessageFormat.format("Unknown nbt type {0}", type));
        }
        idWriter.writeId(stream, id);
    }

    void writeTagName(OUT stream, String name) throws IOException {
        nameWriter.writeName(stream, name);
    }

    @SuppressWarnings("unchecked")
    void writeTag(OUT stream, NBT tag) throws IOException {
        TagWriter<OUT, NBT> f = (TagWriter<OUT, NBT>) typeWriters.get(tag.getType());
        if (f == null) {
            throw new IOException(MessageFormat.format("No writer registered for nbt type {0}", tag.getType()));
        }
        f.writeTag(stream, tag);
    }

    @FunctionalInterface
    protected interface IdReader<T> {
        int readId(NBTLimiter limiter, T from) throws IOException;
    }

    @FunctionalInterface
    protected interface IdWriter<T> {
        void writeId(T to, int id) throws IOException;
    }

    @FunctionalInterface
    protected interface NameReader<T> {
        String readName(NBTLimiter limiter, T from) throws IOException;
    }

    @FunctionalInterface
    protected interface NameWriter<T> {
        void writeName(T to, String name) throws IOException;
    }

    @FunctionalInterface
    protected interface TagReader<IN, T extends NBT> {
        T readTag(NBTLimiter limiter, IN from) throws IOException;
    }

    @FunctionalInterface
    public interface TagWriter<OUT, T extends NBT> {
        void writeTag(OUT to, T tag) throws IOException;
    }

}
