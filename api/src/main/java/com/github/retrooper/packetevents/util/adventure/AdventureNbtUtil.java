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

package com.github.retrooper.packetevents.util.adventure;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.netty.buffer.ByteBufInputStream;
import com.github.retrooper.packetevents.netty.buffer.ByteBufOutputStream;
import com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.codec.NBTCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagType;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.EndBinaryTag;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static net.kyori.adventure.nbt.TagStringIO.tagStringIO;

@NullMarked
public final class AdventureNbtUtil {

    private static final BinaryTagType<?>[] NBT_TAG_TYPES = buildNbtTagTypes();

    private AdventureNbtUtil() {
    }

    @SuppressWarnings("unchecked")
    private static BinaryTagType<?>[] buildNbtTagTypes() {
        BinaryTagTypes.BYTE.id(); // initialize types

        // there is no way to get all registered types...
        List<BinaryTagType<? extends BinaryTag>> types;
        try {
            Field typesField = BinaryTagType.class.getDeclaredField("TYPES");
            typesField.setAccessible(true);
            types = (List<BinaryTagType<? extends BinaryTag>>) typesField.get(null);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Error while accessing registered binary tag types", exception);
        }

        // accessing by array is a lot faster than looping through a list
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

    public static BinaryTag readAdventureTag(Object buf) {
        byte tagTypeId = ByteBufHelper.readByte(buf);
        if (tagTypeId == BinaryTagTypes.END.id()) {
            return EndBinaryTag.endBinaryTag();
        }
        BinaryTagType<?> tagType = NBT_TAG_TYPES[tagTypeId];
        try {
            return tagType.read(new ByteBufInputStream(buf));
        } catch (IOException exception) {
            throw new RuntimeException("Error while reading adventure nbt tag from buf: " + buf, exception);
        }
    }

    public static void writeAdventureTag(Object buf, BinaryTag tag) {
        @SuppressWarnings("unchecked")
        BinaryTagType<? super BinaryTag> tagType = (BinaryTagType<? super BinaryTag>) tag.type();
        ByteBufHelper.writeByte(buf, tagType.id());
        if (tagType.id() != BinaryTagTypes.END.id()) {
            try {
                tagType.write(tag, new ByteBufOutputStream(buf));
            } catch (IOException exception) {
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
            advTag = tagStringIO().asTag(string);
        } catch (IOException exception) {
            throw new RuntimeException("Error while decoding nbt from string: " + string, exception);
        }
        return fromAdventure(advTag);
    }

    public static String toString(NBT tag) {
        BinaryTag advTag = toAdventure(tag);
        try {
            return tagStringIO().asString(advTag);
        } catch (IOException exception) {
            throw new RuntimeException("Error while encoding nbt to string: " + advTag, exception);
        }
    }
}
