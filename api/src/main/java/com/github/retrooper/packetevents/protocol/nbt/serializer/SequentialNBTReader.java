package com.github.retrooper.packetevents.protocol.nbt.serializer;

import com.github.retrooper.packetevents.protocol.nbt.*;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

public final class SequentialNBTReader implements Closeable {

    private static final NBTLimiter DUMMY_LIMITER = NBTLimiter.noop();
    private static final Map<NBTType<?>, TagSkip> TAG_SKIPS = new HashMap<>(16, 1);

    private final DataInputStream stream;

    public SequentialNBTReader(DataInputStream stream) {
        this.stream = stream;
    }

    public NBT read() throws IOException {
        NBTType<?> type = DefaultNBTSerializer.INSTANCE.readTagType(DUMMY_LIMITER, stream);
        // skip name
        int len = stream.readUnsignedShort();
        stream.skipBytes(len);

        NBT nbt;
        if (type == NBTType.COMPOUND) {
            nbt = new Compound("root", stream, () -> {});
        } else if (type == NBTType.LIST) {
            nbt = new List("root", stream, () -> {});
        } else {
            nbt = DefaultNBTSerializer.INSTANCE.readTag(DUMMY_LIMITER, stream, type);
        }

        return nbt;
    }

    private static void checkRead(NBT lastRead) {
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

        private final String name;
        private final DataInputStream stream;
        private final Runnable onComplete;
        private NBTType<?> nextType;
        private NBT lastRead;
        private boolean hasReadType;


        private Compound(String name, DataInputStream stream, Runnable onComplete) {
            this.name = name;
            this.stream = stream;
            this.onComplete = onComplete;
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
            checkRead(lastRead);
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
        public Map.Entry<String, NBT> next() {
            if (!hasNext()) {
                throw new IllegalStateException("No more elements in compound");
            }

            try {
                hasReadType = false;

                String name = DefaultNBTSerializer.INSTANCE.readTagName(DUMMY_LIMITER, stream);

                if (nextType == NBTType.COMPOUND) {
                    lastRead = new Compound(name, stream, this::runCompleted);
                } else if (nextType == NBTType.LIST) {
                    lastRead = new List(name, stream, this::runCompleted);
                } else {
                    lastRead = DefaultNBTSerializer.INSTANCE.readTag(DUMMY_LIMITER, stream, nextType);
                    runCompleted();
                }

                return new AbstractMap.SimpleEntry<>(name, lastRead);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void runCompleted() {
            if (!hasNext()) {
                System.out.println("Running complete for " + name + "'s parent");
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
            if (!hasNext()) return;

            try {
                if (lastRead instanceof Skippable) {
                    ((Skippable) lastRead).skip();
                }

                if (!hasNext()) return;

                int len = stream.readUnsignedShort();
                stream.skipBytes(len);

                TAG_SKIPS.get(nextType).skip(stream);
                hasReadType = false;

                TAG_SKIPS.get(NBTType.COMPOUND).skip(stream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            runCompleted();
        }

        @Override
        public void skipOne() {
            if (!hasNext()) return;

            checkRead(lastRead);

            try {
                int len = stream.readUnsignedShort();
                stream.skipBytes(len);

                TAG_SKIPS.get(nextType).skip(stream);
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

                // we have read a tag type
                NBTCompound compound = new NBTCompound();
                String name = DefaultNBTSerializer.INSTANCE.readTagName(DUMMY_LIMITER, stream);
                NBT nbt = DefaultNBTSerializer.INSTANCE.readTag(DUMMY_LIMITER, stream, nextType);
                compound.setTag(name, nbt);

                // rest tags
                while ((nextType = DefaultNBTSerializer.INSTANCE.readTagType(DUMMY_LIMITER, stream)) != NBTType.END) {
                    name = DefaultNBTSerializer.INSTANCE.readTagName(DUMMY_LIMITER, stream);
                    nbt = DefaultNBTSerializer.INSTANCE.readTag(DUMMY_LIMITER, stream, nextType);
                    compound.setTag(name, nbt);
                }

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

        private final String name;
        private final DataInputStream stream;
        private final Runnable onComplete;
        private final NBTType<?> listType;
        private NBT lastRead;
        public int remaining;

        private List(String name, DataInputStream stream, Runnable onComplete) {
            this.name = name;
            this.stream = stream;
            this.onComplete = onComplete;
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
                remaining--;
                if (listType == NBTType.COMPOUND) {
                    lastRead = new Compound(String.valueOf(this.remaining), stream, this::runCompleted);
                } else if (listType == NBTType.LIST) {
                    lastRead = new List(String.valueOf(this.remaining), stream, this::runCompleted);
                } else {
                    lastRead = DefaultNBTSerializer.INSTANCE.readTag(DUMMY_LIMITER, stream, listType);
                    runCompleted();
                }

                return lastRead;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void runCompleted() {
            if (!hasNext()) {
                System.out.println("Running complete for " + name + "'s parent");
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
            if (!hasNext()) return;

            try {
                if (lastRead instanceof Skippable) {
                    ((Skippable) lastRead).skip();
                }

                if (!hasNext()) return;

                TagSkip typeSkip = TAG_SKIPS.get(listType);
                for (int i = 0; i < remaining; i++) {
                    typeSkip.skip(stream);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            remaining = 0;
            runCompleted();
        }

        @Override
        public void skipOne() {
            if (!hasNext()) return;

            checkRead(lastRead);

            try {
                TAG_SKIPS.get(listType).skip(stream);
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
                    list.addTag(DefaultNBTSerializer.INSTANCE.readTag(DUMMY_LIMITER, stream, listType));
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
        void skip(DataInput in) throws IOException;
    }

    static {
        TAG_SKIPS.put(NBTType.BYTE, in -> in.skipBytes(Byte.BYTES));
        TAG_SKIPS.put(NBTType.SHORT, in -> in.skipBytes(Short.BYTES));
        TAG_SKIPS.put(NBTType.INT, in -> in.skipBytes(Integer.BYTES));
        TAG_SKIPS.put(NBTType.LONG, in -> in.skipBytes(Long.BYTES));
        TAG_SKIPS.put(NBTType.FLOAT, in -> in.skipBytes(Float.BYTES));
        TAG_SKIPS.put(NBTType.DOUBLE, in -> in.skipBytes(Double.BYTES));
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
            in.skipBytes(length * Integer.BYTES);
        });
        TAG_SKIPS.put(NBTType.LONG_ARRAY, in -> {
            int length = in.readInt();
            in.skipBytes(length * Long.BYTES);
        });
    }

}
