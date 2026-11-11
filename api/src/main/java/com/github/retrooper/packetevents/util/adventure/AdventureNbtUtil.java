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

package com.github.retrooper.packetevents.util.adventure;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.netty.buffer.ByteBufInputStream;
import com.github.retrooper.packetevents.netty.buffer.ByteBufOutputStream;
import com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.codec.NBTCodec;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagType;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.EndBinaryTag;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@NullMarked
public final class AdventureNbtUtil {

    private static final byte END_TAG_ID = 0;

    // BinaryTagType is an interface since adventure v5, we need to access everything via Reflection to support both v4 and v5
    private static final Method TAG_TYPE_GET_ID = Reflection.getMethodExact(BinaryTagType.class, "id", byte.class);
    private static final Method TAG_TYPE_READ = Reflection.getMethodExact(BinaryTagType.class, "read", BinaryTag.class, DataInput.class);
    private static final Method TAG_TYPE_WRITE = Reflection.getMethodExact(BinaryTagType.class, "write", void.class, BinaryTag.class, DataOutput.class);

    private static final Constructor<?> CHAR_BUFFER_CTOR;
    private static final Method CHAR_BUFFER_SKIP_WHITESPACE;
    private static final Method CHAR_BUFFER_HAS_MORE;
    private static final Constructor<?> TAG_STRING_READER_CTOR;
    private static final @Nullable Method TAG_STRING_READER_HETEROGENEOUS_LISTS;
    private static final Method TAG_STRING_READER_TAG;

    private static final Constructor<?> TAG_STRING_WRITER_CTOR;
    private static final @Nullable Method TAG_STRING_WRITER_HETEROGENEOUS_LISTS;
    private static final Method TAG_STRING_WRITER_WRITE_TAG;

    static {
        try {
            Class<?> charBuffer = Class.forName("net.kyori.adventure.nbt.CharBuffer");
            Class<?> tagStringReader = Class.forName("net.kyori.adventure.nbt.TagStringReader");
            Class<?> tagStringWriter = Class.forName("net.kyori.adventure.nbt.TagStringWriter");

            CHAR_BUFFER_CTOR = Reflection.getConstructor(charBuffer, CharSequence.class);
            CHAR_BUFFER_SKIP_WHITESPACE = Reflection.getMethodExact(charBuffer, "skipWhitespace", charBuffer);
            CHAR_BUFFER_HAS_MORE = Reflection.getMethodExact(charBuffer, "hasMore", boolean.class);
            TAG_STRING_READER_CTOR = Reflection.getConstructor(tagStringReader, charBuffer);
            TAG_STRING_READER_HETEROGENEOUS_LISTS = Reflection.getMethodExact(tagStringReader, "heterogeneousLists", tagStringReader, boolean.class);
            TAG_STRING_READER_TAG = Reflection.getMethodExact(tagStringReader, "tag", BinaryTag.class);

            TAG_STRING_WRITER_CTOR = Reflection.getConstructor(tagStringWriter, Appendable.class, String.class);
            TAG_STRING_WRITER_HETEROGENEOUS_LISTS = Reflection.getMethodExact(tagStringWriter, "heterogeneousLists", tagStringWriter, boolean.class);
            TAG_STRING_WRITER_WRITE_TAG = Reflection.getMethodExact(tagStringWriter, "writeTag", tagStringWriter, BinaryTag.class);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Error looking up adventure string binary tag i/o");
        }
    }

    private static final BinaryTagType<?>[] NBT_TAG_TYPES = buildNbtTagTypes();

    private AdventureNbtUtil() {
    }

    @SuppressWarnings("unchecked")
    private static BinaryTagType<?>[] buildNbtTagTypes() {
        // initialize class
        try {
            BinaryTagTypes.class.getField("BYTE").get(null);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Error while initializing adventure binary tag types");
        }

        // there is no way to get all registered types...
        List<BinaryTagType<? extends BinaryTag>> types;
        try {
            // adventure v4
            Field typesField = Reflection.getField(BinaryTagType.class, "TYPES");
            if (typesField == null) {
                // adventure v5
                Class<?> binaryTagTypeImpl = Class.forName("net.kyori.adventure.nbt.BinaryTagTypeImpl");
                typesField = Reflection.getField(binaryTagTypeImpl, "TYPES");
            }
            types = (List<BinaryTagType<? extends BinaryTag>>) typesField.get(null);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Error while accessing registered binary tag types", exception);
        }

        // accessing by array index is a lot faster than looping through a list
        BinaryTagType<?>[] nbtTagTypes = new BinaryTagType[types.size()];
        for (int i = 0; i < nbtTagTypes.length; i++) {
            BinaryTagType<? extends BinaryTag> type = types.get(i);
            byte tagId = getTagId(type);
            if (tagId != i) {
                throw new IllegalStateException("Registered nbt tag types are wrong: " + tagId + " != " + i);
            }
            nbtTagTypes[i] = type;
        }
        return nbtTagTypes;
    }

    private static byte getTagId(BinaryTagType<?> tagType) {
        try {
            return (byte) TAG_TYPE_GET_ID.invoke(tagType);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static BinaryTag readAdventureTag(Object buf) {
        byte tagTypeId = ByteBufHelper.readByte(buf);
        if (tagTypeId == END_TAG_ID) {
            return EndBinaryTag.endBinaryTag();
        }
        BinaryTagType<?> tagType = NBT_TAG_TYPES[tagTypeId];
        try {
            return (BinaryTag) TAG_TYPE_READ.invoke(tagType, new ByteBufInputStream(buf));
        } catch (Exception exception) {
            throw new RuntimeException("Error while reading adventure nbt tag from buf: " + buf, exception);
        }
    }

    public static void writeAdventureTag(Object buf, BinaryTag tag) {
        @SuppressWarnings("unchecked")
        BinaryTagType<? super BinaryTag> tagType = (BinaryTagType<? super BinaryTag>) tag.type();
        byte tagId = getTagId(tagType);
        ByteBufHelper.writeByte(buf, tagId);
        if (tagId != END_TAG_ID) {
            try {
                TAG_TYPE_WRITE.invoke(tagType, tag, new ByteBufOutputStream(buf));
            } catch (Exception exception) {
                throw new RuntimeException("Error while writing adventure nbt tag to buf: " + tag, exception);
            }
        }
    }

    public static NBT fromAdventure(BinaryTag tag) {
        // TODO used pooled byte buf here + below?
        Object buf = UnpooledByteBufAllocationHelper.buffer();
        try {
            writeAdventureTag(buf, tag);
            return NBTCodec.readNBTFromBuffer(buf, ServerVersion.getLatest());
        } finally {
            ByteBufHelper.release(buf);
        }
    }

    public static BinaryTag toAdventure(NBT tag) {
        Object buf = UnpooledByteBufAllocationHelper.buffer();
        try {
            NBTCodec.writeNBTToBuffer(buf, ServerVersion.getLatest(), tag);
            return readAdventureTag(buf);
        } finally {
            ByteBufHelper.release(buf);
        }
    }

    public static NBT fromString(String string) {
        return fromString(string, PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    public static NBT fromString(String string, ClientVersion version) {
        BinaryTag advTag;
        try {
            Object buffer = CHAR_BUFFER_CTOR.newInstance(string);
            Object reader = TAG_STRING_READER_CTOR.newInstance(buffer);
            if (TAG_STRING_READER_HETEROGENEOUS_LISTS != null) {
                TAG_STRING_READER_HETEROGENEOUS_LISTS.invoke(reader, version.isNewerThanOrEquals(ClientVersion.V_1_21_5));
            }
            advTag = (BinaryTag) TAG_STRING_READER_TAG.invoke(reader);
            CHAR_BUFFER_SKIP_WHITESPACE.invoke(buffer);
            if ((boolean) CHAR_BUFFER_HAS_MORE.invoke(buffer)) {
                throw new IOException("Document had trailing content after first Tag");
            }
        } catch (Exception exception) {
            throw new RuntimeException("Error while decoding nbt from string: " + string, exception);
        }
        return fromAdventure(advTag);
    }

    public static String toString(NBT tag) {
        return toString(tag, PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    public static String toString(NBT tag, ClientVersion version) {
        BinaryTag advTag = toAdventure(tag);
        StringBuilder bob = new StringBuilder();
        try (AutoCloseable writer = (AutoCloseable) TAG_STRING_WRITER_CTOR.newInstance(bob, "")) {
            if (TAG_STRING_WRITER_HETEROGENEOUS_LISTS != null) {
                TAG_STRING_WRITER_HETEROGENEOUS_LISTS.invoke(writer, version.isNewerThanOrEquals(ClientVersion.V_1_21_5));
            }
            TAG_STRING_WRITER_WRITE_TAG.invoke(writer, advTag);
        } catch (Exception exception) {
            throw new RuntimeException("Error while encoding nbt to string: " + advTag, exception);
        }
        return bob.toString();
    }
}
