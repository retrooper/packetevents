/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.util;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
public final class VerticalAnchor {

    public static final VerticalAnchor BOTTOM = aboveBottom(0);
    public static final VerticalAnchor TOP = belowTop(0);

    public static final NbtMapCodec<VerticalAnchor> MAP_CODEC = new NbtMapCodec<VerticalAnchor>() {
        @Override
        public VerticalAnchor decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            Type type = null;
            for (Type possibleType : Type.values()) {
                if (tag.contains(possibleType.getCodecName())) {
                    if (type != null) {
                        throw new NbtCodecException("Multiple anchor types in " + tag);
                    }
                    type = possibleType;
                }
            }
            if (type == null) {
                throw new NbtCodecException("No anchor type in " + tag);
            }
            int offset = tag.getNumberTagValueOrThrow(type.getCodecName()).intValue();
            return new VerticalAnchor(type, offset);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, VerticalAnchor value) throws NbtCodecException {
            tag.setTag(value.type.getCodecName(), new NBTInt(value.offset));
        }
    };

    private final Type type;
    private final int offset;

    public VerticalAnchor(Type type, int offset) {
        this.type = type;
        this.offset = offset;
    }

    public static VerticalAnchor absolute(int value) {
        return new VerticalAnchor(Type.ABSOLUTE, value);
    }

    public static VerticalAnchor aboveBottom(int offset) {
        return new VerticalAnchor(Type.ABOVE_BOTTOM, offset);
    }

    public static VerticalAnchor belowTop(int offset) {
        return new VerticalAnchor(Type.BELOW_TOP, offset);
    }

    public static VerticalAnchor relativeToSeaLevel(int offset) {
        return new VerticalAnchor(Type.RELATIVE_TO_SEA_LEVEL, offset);
    }

    public Type getType() {
        return this.type;
    }

    public int getOffset() {
        return this.offset;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        VerticalAnchor that = (VerticalAnchor) obj;
        if (this.offset != that.offset) return false;
        return this.type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.offset);
    }

    public enum Type implements CodecNameable {

        ABSOLUTE("absolute"),
        ABOVE_BOTTOM("above_bottom"),
        BELOW_TOP("below_top"),
        RELATIVE_TO_SEA_LEVEL("relative_to_sea_level"),
        ;

        private final String id;

        Type(String id) {
            this.id = id;
        }

        @Override
        public String getCodecName() {
            return this.id;
        }
    }
}
