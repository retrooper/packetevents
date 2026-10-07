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

package com.github.retrooper.packetevents.protocol.valueproviders.ints;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

/**
 * @versions 1.17+
 */
@NullMarked
public final class ConstantInt implements IntProvider {

    public static final NbtMapCodec<ConstantInt> MAP_CODEC = new NbtMapCodec<ConstantInt>() {
        @Override
        public ConstantInt decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            int value = tag.getNumberTagValueOrThrow("value").intValue();
            return new ConstantInt(value);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, ConstantInt value) throws NbtCodecException {
            tag.setTag("value", new NBTInt(value.value));
        }
    };

    private final int value;

    public ConstantInt(int value) {
        this.value = value;
    }

    @Override
    public IntProviderType<?> getType() {
        return IntProviderTypes.CONSTANT;
    }

    @Override
    public int getSample(Random random) {
        return this.value;
    }

    @Override
    public int getMin() {
        return this.value;
    }

    @Override
    public int getMax() {
        return this.value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        ConstantInt that = (ConstantInt) obj;
        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }
}
