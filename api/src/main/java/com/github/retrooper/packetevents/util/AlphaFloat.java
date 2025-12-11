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

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AlphaFloat {

    public static final NbtCodec<AlphaFloat> CODEC = new NbtCodec<AlphaFloat>() {
        @Override
        public AlphaFloat decode(NBT nbt, PacketWrapper<?> wrapper) throws NbtCodecException {
            if (nbt instanceof NBTNumber) {
                return new AlphaFloat(((NBTNumber) nbt).getAsFloat(), 1f);
            }
            NBTCompound compound = nbt.castOrThrow(NBTCompound.class);
            float value = compound.getNumberTagValueOrThrow("value").floatValue();
            float alpha = compound.getNumberTagValueOrDefault("alpha", 1f).floatValue();
            return new AlphaFloat(value, alpha);
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, AlphaFloat value) throws NbtCodecException {
            if (value.alpha == 1f) {
                return new NBTFloat(value.value);
            }
            NBTCompound compound = new NBTCompound();
            compound.setTag("value", new NBTFloat(value.value));
            compound.setTag("alpha", new NBTFloat(value.alpha));
            return compound;
        }
    };

    private final float value;
    private final float alpha;

    public AlphaFloat(float value, float alpha) {
        this.value = value;
        this.alpha = alpha;
    }

    public float getValue() {
        return this.value;
    }

    public float getAlpha() {
        return this.alpha;
    }
}
