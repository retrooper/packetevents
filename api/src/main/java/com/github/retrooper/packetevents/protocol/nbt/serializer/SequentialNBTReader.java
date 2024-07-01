package com.github.retrooper.packetevents.protocol.nbt.serializer;

import com.github.retrooper.packetevents.protocol.nbt.*;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

public final class SequentialNBTReader implements Iterator<NBT>, Closeable {

    private static final NBTLimiter DUMMY_LIMITER = NBTLimiter.noop();
    private static final Map<NBTType<?>, TagSkip> TAG_SKIPS = new HashMap<>(16, 1);

    private final DataInputStream stream;
    private NBTType<?> nextType = null;
    private boolean hasReadType = false;
    private NBT lastRead = null;

    public SequentialNBTReader(DataInputStream stream) {
        this.stream = stream;
    }

    @Override
    public boolean hasNext() {
        checkRead();
        if (!hasReadType) {
            try {
                nextType = DefaultNBTSerializer.INSTANCE.readTagType(DUMMY_LIMITER, stream);
                hasReadType = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return nextType != NBTType.END;
    }

    @Override
    public NBT next() {
        checkRead();
        if (!hasReadType) {
            hasNext();
        }

        try {
            hasReadType = false;
            DefaultNBTSerializer.INSTANCE.readTagName(DUMMY_LIMITER, stream);
            if (nextType == NBTType.COMPOUND) {
                lastRead = new Compound(stream);
            } else if (nextType == NBTType.LIST) {
                lastRead = new List(stream);
            } else {
                lastRead = DefaultNBTSerializer.INSTANCE.readTag(DUMMY_LIMITER, stream, nextType);
            }

            return lastRead;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkRead() {
        if (lastRead == null) return;
        if (lastRead instanceof Iterator && ((Iterator<NBT>) lastRead).hasNext()) {
            throw new IllegalStateException("Previous nbt has not been read completely");
        }
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }

    public static class Compound extends NBT implements Iterator<Map.Entry<String, NBT>>, Iterable<Map.Entry<String, NBT>>, Skippable, Closeable {

        private boolean hasReadCompletely = false;
        private final SequentialNBTReader reader;

        private Compound(DataInputStream stream) {
            this.reader = new SequentialNBTReader(stream);
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
            hasReadCompletely = !reader.hasNext();
            return hasReadCompletely;
        }

        @Override
        public Map.Entry<String, NBT> next() {
            if (!reader.hasReadType) {
                hasNext();
            }

            if (hasReadCompletely) {
                throw new IllegalStateException("No more elements in compound");
            }

            try {
                reader.hasReadType = false;
                String name = DefaultNBTSerializer.INSTANCE.readTagName(DUMMY_LIMITER, reader.stream);
                if (reader.nextType == NBTType.COMPOUND) {
                    reader.lastRead = new Compound(reader.stream);
                } else if (reader.nextType == NBTType.LIST) {
                    reader.lastRead = new List(reader.stream);
                } else {
                    reader.lastRead = DefaultNBTSerializer.INSTANCE.readTag(DUMMY_LIMITER, reader.stream, reader.nextType);
                }

                return new AbstractMap.SimpleEntry<>(name, reader.lastRead);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @NotNull
        @Override
        public Iterator<Map.Entry<String, NBT>> iterator() {
            return this;
        }

        @Override
        public void skip() {
            if (hasReadCompletely) return;

            try {
                if (reader.lastRead instanceof Skippable) {
                    ((Skippable) reader.lastRead).skip();
                }

                TAG_SKIPS.get(NBTType.COMPOUND).skip(reader.stream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            hasReadCompletely = true;
        }

        public NBTCompound readFully() {
            try {
                NBTCompound compound = new NBTCompound();
                NBTType<?> valueType;
                while ((valueType = DefaultNBTSerializer.INSTANCE.readTagType(DUMMY_LIMITER, reader.stream)) != NBTType.END) {
                    String name = DefaultNBTSerializer.INSTANCE.readTagName(DUMMY_LIMITER, reader.stream);
                    NBT nbt = DefaultNBTSerializer.INSTANCE.readTag(DUMMY_LIMITER, reader.stream, valueType);
                    compound.setTag(name, nbt);
                }
                return compound;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() throws IOException {
            reader.close();
        }
    }

    public static class List extends NBT implements Iterator<NBT>, Iterable<NBT>, Skippable, Closeable {

        private final SequentialNBTReader reader;
        private final NBTType<?> listType;
        private int remaining;

        private List(DataInputStream stream) {
            this.reader = new SequentialNBTReader(stream);
            try {
                this.listType = DefaultNBTSerializer.INSTANCE.readTagType(DUMMY_LIMITER, stream);
                this.remaining = stream.readInt();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
            if (!hasNext()) {
                throw new IllegalStateException("No more elements in list");
            }
            try {
                NBT nbt = DefaultNBTSerializer.INSTANCE.readTag(DUMMY_LIMITER, reader.stream, listType);
                remaining--;
                return nbt;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @NotNull
        @Override
        public Iterator<NBT> iterator() {
            return this;
        }

        @Override
        public void skip() {
            if (remaining == 0) return;

            try {
                if (reader.lastRead instanceof Skippable) {
                    ((Skippable) reader.lastRead).skip();
                }

                for (int i = 0; i < remaining; i++) {
                    TAG_SKIPS.get(listType).skip(reader.stream);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            remaining = 0;
        }

        public NBTList<NBT> readFully() {
            try {
                NBTList<NBT> list = new NBTList<>((NBTType<NBT>) listType, remaining);
                for (int i = 0; i < remaining; i++) {
                    list.addTag(DefaultNBTSerializer.INSTANCE.readTag(DUMMY_LIMITER, reader.stream, listType));
                }
                remaining = 0;
                return list;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() throws IOException {
            reader.close();
        }
    }

    interface Skippable {
        void skip();
    }

    @FunctionalInterface
    private interface TagSkip {
        void skip(DataInput in) throws IOException;
    }

    static {
        TAG_SKIPS.put(NBTType.BYTE, in -> in.skipBytes(1));
        TAG_SKIPS.put(NBTType.SHORT, in -> in.skipBytes(2));
        TAG_SKIPS.put(NBTType.INT, in -> in.skipBytes(4));
        TAG_SKIPS.put(NBTType.LONG, in -> in.skipBytes(8));
        TAG_SKIPS.put(NBTType.FLOAT, in -> in.skipBytes(4));
        TAG_SKIPS.put(NBTType.DOUBLE, in -> in.skipBytes(8));
        TAG_SKIPS.put(NBTType.BYTE_ARRAY, in -> {
            int length = in.readInt();
            in.skipBytes(length);
        });
        TAG_SKIPS.put(NBTType.STRING, in -> {
            int length = in.readUnsignedShort();
            in.skipBytes(length);
        });
        TAG_SKIPS.put(NBTType.LIST, in -> {
            NBTType<?> listType = DefaultNBTSerializer.INSTANCE.readTagType(DUMMY_LIMITER, in);
            int length = in.readInt();
            for (int i = 0; i < length; i++) {
                TAG_SKIPS.get(listType).skip(in);
            }
        });
        TAG_SKIPS.put(NBTType.COMPOUND, in -> {
            NBTType<?> valueType;
            while ((valueType = DefaultNBTSerializer.INSTANCE.readTagType(DUMMY_LIMITER, in)) != NBTType.END) {

                int utfLen = in.readUnsignedShort();
                in.skipBytes(utfLen);

                TAG_SKIPS.get(valueType).skip(in);
            }
        });
        TAG_SKIPS.put(NBTType.INT_ARRAY, in -> {
            int length = in.readInt();
            in.skipBytes(length * 4);
        });
        TAG_SKIPS.put(NBTType.LONG_ARRAY, in -> {
            int length = in.readInt();
            in.skipBytes(length * 8);
        });
    }

}
