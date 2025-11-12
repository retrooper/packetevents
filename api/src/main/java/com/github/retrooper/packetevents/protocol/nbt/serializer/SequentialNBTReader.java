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
package com.github.retrooper.packetevents.protocol.nbt.serializer;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.github.retrooper.packetevents.protocol.nbt.serializer.DefaultNBTSerializer.ARRAY_HEADER_BYTES;
import static com.github.retrooper.packetevents.protocol.nbt.serializer.DefaultNBTSerializer.OBJECT_HEADER_BYTES;
import static com.github.retrooper.packetevents.protocol.nbt.serializer.DefaultNBTSerializer.OBJECT_REF_BYTES;
import static com.github.retrooper.packetevents.protocol.nbt.serializer.DefaultNBTSerializer.STRING_SIZE_BYTES;

public final class SequentialNBTReader implements NBTReader<NBT, DataInputStream> {

    public static final SequentialNBTReader INSTANCE = new SequentialNBTReader();

    private static final Map<NBTType<?>, TagSkip> TAG_SKIPS = new HashMap<>(16);
    private static final Map<NBTType<?>, TagBinaryReader> TAG_BINARY_READERS = new HashMap<>(16);

    @Override
    public NBT deserializeTag(NBTLimiter limiter, DataInputStream from, boolean named) throws IOException {
        NBTType<?> type = DefaultNBTSerializer.INSTANCE.readTagType(limiter, from);

        if (named) {
            // skip name
            int len = from.readUnsignedShort();
            from.skipBytes(len);
        }

        NBT nbt;
        if (type == NBTType.COMPOUND) {
            nbt = new Compound(from, limiter, () -> { /**/ });
        } else if (type == NBTType.LIST) {
            nbt = new List(from, limiter, () -> { /**/ });
        } else {
            nbt = DefaultNBTSerializer.INSTANCE.readTag(limiter, from, type);
        }

        return nbt;
    }

    private static void checkReadable(NBT lastRead) {
        if (lastRead == null) return;
        if (lastRead instanceof Iterator && ((Iterator<NBT>) lastRead).hasNext()) {
            throw new IllegalStateException("Previous nbt has not been read completely");
        }
    }

    public static class Compound extends NBT implements Iterator<Map.Entry<String, NBT>>, Iterable<Map.Entry<String, NBT>>, Skippable, Closeable {

        private final DataInputStream stream;
        private final NBTLimiter limiter;
        private final Runnable onComplete;
        private NBTType<?> nextType;
        private NBT lastRead;
        private boolean hasReadType;

        private Compound(DataInputStream stream, NBTLimiter limiter, Runnable onComplete) {
            this.stream = stream;
            this.limiter = limiter;
            this.onComplete = onComplete;
            limiter.increment(48);
            runCompleted();
        }

        @Override
        public NBTType<?> getType() {
            return NBTType.COMPOUND;
        }

        @Override
        public boolean equals(Object other) {
            return this == other;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public NBT copy() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            checkReadable(lastRead);
            if (!hasReadType) {
                try {
                    nextType = DefaultNBTSerializer.INSTANCE.readTagType(limiter, stream);
                    hasReadType = true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return nextType != NBTType.END;
        }

        @Override
        public Map.Entry<String, NBT> next() {
            if (!hasNext()) {
                throw new IllegalStateException("No more elements in compound");
            }

            try {
                hasReadType = false;

                String name = DefaultNBTSerializer.readString(limiter, stream);

                if (nextType == NBTType.COMPOUND) {
                    lastRead = new Compound(stream, limiter, this::runCompleted);
                } else if (nextType == NBTType.LIST) {
                    lastRead = new List(stream, limiter, this::runCompleted);
                } else {
                    lastRead = DefaultNBTSerializer.INSTANCE.readTag(limiter, stream, nextType);
                    runCompleted();
                }
                limiter.increment(36);

                return new AbstractMap.SimpleEntry<>(name, lastRead);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void runCompleted() {
            if (!hasNext()) {
                onComplete.run();
            }
        }

        @NotNull
        @Override
        public Iterator<Map.Entry<String, NBT>> iterator() {
            return this;
        }

        @Override
        public void skip() {
            if (lastRead instanceof Skippable) {
                ((Skippable) lastRead).skip();
            }

            if (!hasNext()) return;

            try {
                int len = stream.readUnsignedShort();
                stream.skipBytes(len);

                TAG_SKIPS.get(nextType).skip(limiter, stream);
                limiter.increment(36);

                TAG_SKIPS.get(NBTType.COMPOUND).skip(limiter, stream);

                hasReadType = true;
                nextType = NBTType.END;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            runCompleted();
        }

        @Override
        public void skipOne() {
            checkReadable(lastRead);

            if (!hasNext()) return;

            try {
                int len = stream.readUnsignedShort();
                stream.skipBytes(len);

                TAG_SKIPS.get(nextType).skip(limiter, stream);
                limiter.increment(36);
                hasReadType = false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            runCompleted();
        }

        public NBTCompound readFully() {
            try {
                // skip last read
                if (lastRead instanceof Skippable) {
                    ((Skippable) lastRead).skip();
                }

                if (!hasNext()) return new NBTCompound();

                NBTCompound compound = new NBTCompound();

                // we have read a tag type, so we can start reading the tag
                do {
                    String name = DefaultNBTSerializer.readString(limiter, stream);
                    NBT nbt = DefaultNBTSerializer.INSTANCE.readTag(limiter, stream, nextType);
                    limiter.increment(36);
                    compound.setTag(name, nbt);
                } while ((nextType = DefaultNBTSerializer.INSTANCE.readTagType(limiter, stream)) != NBTType.END);

                hasReadType = true;
                runCompleted();
                return compound;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public byte[] readFullyAsBytes() {
            try {
                // skip last read
                if (lastRead instanceof Skippable) {
                    ((Skippable) lastRead).skip();
                }

                if (!hasNext()) return new byte[]{10, 0}; // empty compound

                try (ByteArrayOutputStream bytes = new ByteArrayOutputStream(); DataOutputStream out = new DataOutputStream(bytes)) {
                    out.write(10); // compound type

                    do {
                        out.write(DefaultNBTSerializer.INSTANCE.typeToId.get(nextType));

                        // name
                        byte[] name = TAG_BINARY_READERS.get(NBTType.STRING).read(NBTLimiter.noop(), stream);
                        limiter.increment(name.length * 2 + 28);
                        out.write(name);

                        // nbt
                        byte[] nbt = TAG_BINARY_READERS.get(nextType).read(limiter, stream);
                        limiter.increment(36);

                        out.write(nbt);
                    } while ((nextType = DefaultNBTSerializer.INSTANCE.readTagType(limiter, stream)) != NBTType.END);

                    out.write(0);
                    hasReadType = true;
                    runCompleted();
                    return bytes.toByteArray();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() throws IOException {
            stream.close();
        }
    }

    public static class List extends NBT implements Iterator<NBT>, Iterable<NBT>, Skippable, Closeable {

        private final DataInputStream stream;
        private final NBTLimiter limiter;
        private final Runnable onComplete;
        private final NBTType<?> listType;
        private NBT lastRead;
        public int remaining;

        private List(DataInputStream stream, NBTLimiter limiter, Runnable onComplete) {
            this.stream = stream;
            this.limiter = limiter;
            this.onComplete = onComplete;
            limiter.increment(37);
            try {
                this.listType = DefaultNBTSerializer.INSTANCE.readTagType(limiter, stream);
                this.remaining = stream.readInt();
                limiter.increment(remaining * 4);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            runCompleted();
        }

        @Override
        public NBTType<?> getType() {
            return NBTType.LIST;
        }

        @Override
        public boolean equals(Object other) {
            return this == other;
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public NBT copy() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public NBT next() {
            checkReadable(lastRead);
            if (!hasNext()) {
                throw new IllegalStateException("No more elements in list");
            }

            try {
                remaining--;
                if (listType == NBTType.COMPOUND) {
                    lastRead = new Compound(stream, limiter, this::runCompleted);
                } else if (listType == NBTType.LIST) {
                    lastRead = new List(stream, limiter, this::runCompleted);
                } else {
                    lastRead = DefaultNBTSerializer.INSTANCE.readTag(limiter, stream, listType);
                    runCompleted();
                }

                return lastRead;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void runCompleted() {
            if (!hasNext()) {
                onComplete.run();
            }
        }

        @NotNull
        @Override
        public Iterator<NBT> iterator() {
            return this;
        }

        @Override
        public void skip() {
            if (lastRead instanceof Skippable) {
                ((Skippable) lastRead).skip();
            }

            if (!hasNext()) return;

            try {
                TagSkip typeSkip = TAG_SKIPS.get(listType);
                for (int i = 0; i < remaining; i++) {
                    typeSkip.skip(limiter, stream);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            remaining = 0;
            runCompleted();
        }

        @Override
        public void skipOne() {
            checkReadable(lastRead);

            if (!hasNext()) return;

            try {
                TAG_SKIPS.get(listType).skip(limiter, stream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            remaining--;
            runCompleted();
        }

        public NBTList<NBT> readFully() {
            try {
                // skip last read
                if (lastRead instanceof Skippable) {
                    ((Skippable) lastRead).skip();
                }

                if (!hasNext()) return new NBTList<>((NBTType<NBT>) listType, 0);

                NBTList<NBT> list = new NBTList<>((NBTType<NBT>) listType, remaining);
                for (int i = 0; i < remaining; i++) {
                    list.addTag(DefaultNBTSerializer.INSTANCE.readTag(limiter, stream, listType));
                }

                remaining = 0;
                runCompleted();
                return list;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public byte[] readFullyAsBinary() {
            try {
                // skip last read
                if (lastRead instanceof Skippable) {
                    ((Skippable) lastRead).skip();
                }

                if (!hasNext()) return new byte[]{9};

                byte[] array = null;
                for (int i = 0; i < remaining; i++) {
                    byte[] element = TAG_BINARY_READERS.get(listType).read(limiter, stream);

                    if (array == null) {
                        array = new byte[2 + Integer.BYTES + remaining * element.length]; // all the remaining child tags will be of same size
                        array[0] = 9; // list tag
                        array[1] = DefaultNBTSerializer.INSTANCE.typeToId.get(listType).byteValue();

                        // write length
                        array[2] = (byte) (remaining >>> 24);
                        array[3] = (byte) (remaining >>> 16);
                        array[4] = (byte) (remaining >>> 8);
                        array[5] = (byte) remaining;
                    }

                    System.arraycopy(element, 0, array, 1 + Integer.BYTES + i * element.length, element.length);
                }

                remaining = 0;
                runCompleted();
                return array;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() throws IOException {
            stream.close();
        }
    }

    interface Skippable {
        void skip();

        void skipOne();
    }

    @FunctionalInterface
    private interface TagSkip {
        void skip(NBTLimiter limiter, DataInput in) throws IOException;
    }

    @FunctionalInterface
    private interface TagBinaryReader {
        byte[] read(NBTLimiter limiter, DataInput in) throws IOException;
    }

    static {
        TAG_SKIPS.put(NBTType.BYTE, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Byte.BYTES);
            in.skipBytes(Byte.BYTES);
        });
        TAG_SKIPS.put(NBTType.SHORT, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Short.BYTES);
            in.skipBytes(Short.BYTES);
        });
        TAG_SKIPS.put(NBTType.INT, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Integer.BYTES);
            in.skipBytes(Integer.BYTES);
        });
        TAG_SKIPS.put(NBTType.LONG, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Long.BYTES);
            in.skipBytes(Long.BYTES);
        });
        TAG_SKIPS.put(NBTType.FLOAT, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Float.BYTES);
            in.skipBytes(Float.BYTES);
        });
        TAG_SKIPS.put(NBTType.DOUBLE, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Double.BYTES);
            in.skipBytes(Double.BYTES);
        });
        TAG_SKIPS.put(NBTType.BYTE_ARRAY, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + ARRAY_HEADER_BYTES + Integer.BYTES);
            int length = in.readInt();
            limiter.increment(length * Byte.BYTES);
            limiter.checkReadability(length * Byte.BYTES);
            in.skipBytes(length);
        });
        TAG_SKIPS.put(NBTType.STRING, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + STRING_SIZE_BYTES);
            int length = in.readUnsignedShort();
            limiter.increment(length * Character.BYTES);
            in.skipBytes(length);
        });
        TAG_SKIPS.put(NBTType.LIST, (limiter, in) -> {
            limiter.enterDepth();
            try {
                limiter.increment(OBJECT_HEADER_BYTES + OBJECT_REF_BYTES // list tag
                        + OBJECT_HEADER_BYTES + ARRAY_HEADER_BYTES + Integer.BYTES); // arraylist
                NBTType<?> listType = DefaultNBTSerializer.INSTANCE.readTagType(limiter, in);
                int length = in.readInt();
                limiter.increment(length * OBJECT_REF_BYTES);
                for (int i = 0; i < length; i++) {
                    TAG_SKIPS.get(listType).skip(limiter, in);
                }
            } finally {
                limiter.exitDepth();
            }
        });
        TAG_SKIPS.put(NBTType.COMPOUND, (limiter, in) -> {
            limiter.enterDepth();
            try {
                limiter.increment(OBJECT_HEADER_BYTES + OBJECT_REF_BYTES // compound tag
                        + OBJECT_HEADER_BYTES + ARRAY_HEADER_BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES + Float.BYTES); // hashmap
                Set<String> names = new HashSet<>();
                NBTType<?> valueType;
                while ((valueType = DefaultNBTSerializer.INSTANCE.readTagType(limiter, in)) != NBTType.END) {
                    String name = DefaultNBTSerializer.readString(limiter, in);
                    if (names.add(name)) {
                        limiter.increment(12 + OBJECT_HEADER_BYTES + Integer.BYTES + OBJECT_REF_BYTES + OBJECT_REF_BYTES + OBJECT_REF_BYTES);
                    }
                    TAG_SKIPS.get(valueType).skip(limiter, in);
                }
            } finally {
                limiter.exitDepth();
            }
        });
        TAG_SKIPS.put(NBTType.INT_ARRAY, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + ARRAY_HEADER_BYTES + Integer.BYTES);
            int length = in.readInt();
            limiter.increment(length * Integer.BYTES);
            limiter.checkReadability(length * Integer.BYTES);
            in.skipBytes(length * Integer.BYTES);
        });
        TAG_SKIPS.put(NBTType.LONG_ARRAY, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + ARRAY_HEADER_BYTES + Integer.BYTES);
            int length = in.readInt();
            limiter.increment(length * Long.BYTES);
            limiter.checkReadability(length * Long.BYTES);
            in.skipBytes(length * Long.BYTES);
        });

        TAG_BINARY_READERS.put(NBTType.BYTE, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Byte.BYTES);
            return new byte[]{in.readByte()};
        });
        TAG_BINARY_READERS.put(NBTType.SHORT, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Short.BYTES);
            return new byte[]{in.readByte(), in.readByte()};
        });
        TAG_BINARY_READERS.put(NBTType.INT, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Integer.BYTES);
            byte[] bytes = new byte[Integer.BYTES];
            in.readFully(bytes);
            return bytes;
        });
        TAG_BINARY_READERS.put(NBTType.LONG, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Long.BYTES);
            byte[] bytes = new byte[Long.BYTES];
            in.readFully(bytes);
            return bytes;
        });
        TAG_BINARY_READERS.put(NBTType.FLOAT, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Float.BYTES);
            byte[] bytes = new byte[Float.BYTES];
            in.readFully(bytes);
            return bytes;
        });
        TAG_BINARY_READERS.put(NBTType.DOUBLE, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + Double.BYTES);
            byte[] bytes = new byte[Double.BYTES];
            in.readFully(bytes);
            return bytes;
        });
        TAG_BINARY_READERS.put(NBTType.BYTE_ARRAY, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + ARRAY_HEADER_BYTES + Integer.BYTES);
            int len = in.readInt();
            if (len >= 1 << 24) {
                throw new IllegalArgumentException("Byte array length is too large: " + len);
            }
            limiter.increment(len * Byte.BYTES);
            limiter.checkReadability(len * Byte.BYTES);

            byte[] array = new byte[Integer.BYTES + len];
            intToBytes(array, len, 0);
            in.readFully(array, Integer.BYTES, len);
            return array;
        });
        TAG_BINARY_READERS.put(NBTType.STRING, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + STRING_SIZE_BYTES);
            String string = in.readUTF();
            limiter.increment(string.length() * Character.BYTES);

            try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(bytes)) {
                out.writeUTF(string);
                return bytes.toByteArray();
            }
        });
        TAG_BINARY_READERS.put(NBTType.LIST, (limiter, in) -> {
            limiter.enterDepth();
            try {
                limiter.increment(OBJECT_HEADER_BYTES + OBJECT_REF_BYTES // list tag
                        + OBJECT_HEADER_BYTES + ARRAY_HEADER_BYTES + Integer.BYTES); // arraylist
                byte type = in.readByte();
                NBTType<? extends NBT> nbtType = DefaultNBTSerializer.INSTANCE.idToType.get((int) type);

                int len = in.readInt();
                limiter.increment(len * OBJECT_REF_BYTES);

                try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                     DataOutputStream out = new DataOutputStream(bytes)) {
                    out.write(type);
                    out.writeInt(len);
                    for (int i = 0; i < len; i++) {
                        out.write(TAG_BINARY_READERS.get(nbtType).read(limiter, in));
                    }
                    return bytes.toByteArray();
                }
            } finally {
                limiter.exitDepth();
            }
        });
        TAG_BINARY_READERS.put(NBTType.COMPOUND, (limiter, in) -> {
            limiter.enterDepth();
            try {
                limiter.increment(OBJECT_HEADER_BYTES + OBJECT_REF_BYTES // compound tag
                        + OBJECT_HEADER_BYTES + ARRAY_HEADER_BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES + Float.BYTES); // hashmap
                try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                     DataOutputStream out = new DataOutputStream(bytes)) {
                    Set<String> names = new HashSet<>();
                    byte type;
                    while ((type = in.readByte()) != 0) {
                        out.write(type);
                        String name = DefaultNBTSerializer.readString(limiter, in);
                        out.writeUTF(name);
                        out.write(TAG_BINARY_READERS.get(DefaultNBTSerializer.INSTANCE.idToType.get((int) type)).read(limiter, in));
                        if (names.add(name)) {
                            limiter.increment(12 + OBJECT_HEADER_BYTES + Integer.BYTES + OBJECT_REF_BYTES + OBJECT_REF_BYTES + OBJECT_REF_BYTES);
                        }
                    }
                    out.write(type);
                    return bytes.toByteArray();
                }
            } finally {
                limiter.exitDepth();
            }
        });
        TAG_BINARY_READERS.put(NBTType.INT_ARRAY, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + ARRAY_HEADER_BYTES + Integer.BYTES);
            int len = in.readInt();
            limiter.increment(len * Integer.BYTES);
            limiter.checkReadability(len * Integer.BYTES);

            byte[] array = new byte[Integer.BYTES + len * Integer.BYTES];
            intToBytes(array, len, 0);
            in.readFully(array, Integer.BYTES, len * Integer.BYTES);
            return array;
        });
        TAG_BINARY_READERS.put(NBTType.LONG_ARRAY, (limiter, in) -> {
            limiter.increment(OBJECT_HEADER_BYTES + ARRAY_HEADER_BYTES + Integer.BYTES);
            int len = in.readInt();
            limiter.increment(len * Long.BYTES);
            limiter.checkReadability(len * Long.BYTES);

            byte[] array = new byte[Integer.BYTES + len * Long.BYTES];
            intToBytes(array, len, 0);
            in.readFully(array, Integer.BYTES, len * Long.BYTES);
            return array;
        });
    }

    public static void intToBytes(byte[] array, int val, int offset) {
        array[offset] = (byte) ((val >>> 24) & 0xFF);
        array[offset + 1] = (byte) ((val >>> 16) & 0xFF);
        array[offset + 2] = (byte) ((val >>> 8) & 0xFF);
        array[offset + 3] = (byte) (val & 0xFF);
    }
}
