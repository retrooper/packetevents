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

import com.github.retrooper.packetevents.protocol.nbt.*;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

public final class SequentialNBTReader implements NBTReader<NBT, DataInputStream> {

    public static final SequentialNBTReader INSTANCE = new SequentialNBTReader();

    private static final Map<NBTType<?>, TagSkip> TAG_SKIPS = new HashMap<>(16, 1);

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
            nbt = new Compound(from, limiter, () -> {});
        } else if (type == NBTType.LIST) {
            nbt = new List(from, limiter, () -> {});
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

                String name = DefaultNBTSerializer.INSTANCE.readTagName(limiter, stream);

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
                    String name = DefaultNBTSerializer.INSTANCE.readTagName(limiter, stream);
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

    static {
        TAG_SKIPS.put(NBTType.BYTE, (limiter, in) -> {
            limiter.increment(9);
            in.skipBytes(Byte.BYTES);
        });
        TAG_SKIPS.put(NBTType.SHORT, (limiter, in) -> {
            limiter.increment(10);
            in.skipBytes(Short.BYTES);
        });
        TAG_SKIPS.put(NBTType.INT, (limiter, in) -> {
            limiter.increment(12);
            in.skipBytes(Integer.BYTES);
        });
        TAG_SKIPS.put(NBTType.LONG, (limiter, in) -> {
            limiter.increment(16);
            in.skipBytes(Long.BYTES);
        });
        TAG_SKIPS.put(NBTType.FLOAT, (limiter, in) -> {
            limiter.increment(12);
            in.skipBytes(Float.BYTES);
        });
        TAG_SKIPS.put(NBTType.DOUBLE, (limiter, in) -> {
            limiter.increment(16);
            in.skipBytes(Double.BYTES);
        });
        TAG_SKIPS.put(NBTType.BYTE_ARRAY, (limiter, in) -> {
            limiter.increment(24);
            int length = in.readInt();

            limiter.checkReadability(length);
            limiter.increment(length);

            in.skipBytes(length);
        });
        TAG_SKIPS.put(NBTType.STRING, (limiter, in) -> {
            limiter.increment(36);
            int length = in.readUnsignedShort();
            limiter.increment(length * 2);
            in.skipBytes(length);
        });
        TAG_SKIPS.put(NBTType.LIST, (limiter, in) -> {
            limiter.increment(37);
            NBTType<?> listType = DefaultNBTSerializer.INSTANCE.readTagType(limiter, in);
            int length = in.readInt();
            limiter.increment(length * 4);
            for (int i = 0; i < length; i++) {
                TAG_SKIPS.get(listType).skip(limiter, in);
            }
        });
        TAG_SKIPS.put(NBTType.COMPOUND, (limiter, in) -> {
            limiter.increment(48);
            NBTType<?> valueType;
            while ((valueType = DefaultNBTSerializer.INSTANCE.readTagType(limiter, in)) != NBTType.END) {
                int utfLen = in.readUnsignedShort();
                in.skipBytes(utfLen);
                limiter.increment(36);
                TAG_SKIPS.get(valueType).skip(limiter, in);
            }
        });
        TAG_SKIPS.put(NBTType.INT_ARRAY, (limiter, in) -> {
            limiter.increment(24);
            int length = in.readInt();

            limiter.checkReadability(length * 4);
            limiter.increment(length * 4);

            in.skipBytes(length * Integer.BYTES);
        });
        TAG_SKIPS.put(NBTType.LONG_ARRAY, (limiter, in) -> {
            limiter.increment(24);
            int length = in.readInt();

            limiter.checkReadability(length * 8);
            limiter.increment(length * 8);

            in.skipBytes(length * Long.BYTES);
        });
    }

}
