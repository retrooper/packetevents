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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.netty.buffer.ByteBufInputStream;
import com.github.retrooper.packetevents.netty.buffer.ByteBufOutputStream;
import com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.codec.NBTCodec;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagType;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.EndBinaryTag;
import net.kyori.adventure.nbt.TagStringIO;
import org.jspecify.annotations.NullMarked;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@NullMarked
public final class AdventureNbtUtil {

    private static final byte END_TAG_ID = 0;
    private static final BinaryTagType<?>[] NBT_TAG_TYPES = buildNbtTagTypes();

    private static final TagStringIO TAG_STRING_IO;

    static {
        TagStringIO tagStringIo;
        try {
            tagStringIo = TagStringIO.tagStringIO();
        } catch (Throwable ignored) {
            // pre adventure v4.22.0
            tagStringIo = TagStringIO.get();
        }
        TAG_STRING_IO = tagStringIo;
    }

    // BinaryTagType is an interface since adventure v5, we need to access everything via Reflection to support both v4 and v5
    private static final Method TAG_TYPE_GET_ID = Reflection.getMethodExact(BinaryTagType.class, "id", byte.class);
    private static final Method TAG_TYPE_READ = Reflection.getMethodExact(BinaryTagType.class, "read", BinaryTag.class, DataInput.class);
    private static final Method TAG_TYPE_WRITE = Reflection.getMethodExact(BinaryTagType.class, "write", void.class, BinaryTag.class, DataOutput.class);

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
            if (type.id() != i) {
                throw new IllegalStateException("Registered nbt tag types are wrong: " + type.id() + " != " + i);
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
        BinaryTag advTag;
        try {
            advTag = TAG_STRING_IO.asTag(string);
        } catch (IOException exception) {
            throw new RuntimeException("Error while decoding nbt from string: " + string, exception);
        }
        return fromAdventure(advTag);
    }

    public static String toString(NBT tag) {
        BinaryTag advTag = toAdventure(tag);
        try {
            return TAG_STRING_IO.asString(advTag);
        } catch (IOException exception) {
            throw new RuntimeException("Error while encoding nbt to string: " + advTag, exception);
        }
    }
}
