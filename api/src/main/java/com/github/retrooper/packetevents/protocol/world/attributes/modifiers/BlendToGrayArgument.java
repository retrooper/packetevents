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

package com.github.retrooper.packetevents.protocol.world.attributes.modifiers;

import com.github.retrooper.packetevents.protocol.color.AlphaColor;
import com.github.retrooper.packetevents.protocol.color.Color;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.21.11+
 */
@NullMarked
public final class BlendToGrayArgument {

    public static final NbtCodec<BlendToGrayArgument> CODEC = new NbtMapCodec<BlendToGrayArgument>() {
        @Override
        public BlendToGrayArgument decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            float brightness = compound.getNumberTagValueOrThrow("brightness").floatValue();
            float factor = compound.getNumberTagValueOrThrow("factor").floatValue();
            return new BlendToGrayArgument(brightness, factor);
        }

        @Override
        public void encode(NBTCompound compound, PacketWrapper<?> wrapper, BlendToGrayArgument value) throws NbtCodecException {
            compound.setTag("brightness", new NBTFloat(value.brightness));
            compound.setTag("factor", new NBTFloat(value.factor));
        }
    }.codec();

    private final float brightness;
    private final float factor;

    public BlendToGrayArgument(float brightness, float factor) {
        this.brightness = brightness;
        this.factor = factor;
    }

    public AlphaColor blend(AlphaColor color) {
        AlphaColor scaledGrayscale = color.asGrayscale().scale(this.brightness);
        return color.lerpSrgb(scaledGrayscale, this.factor);
    }

    public Color blend(Color color) {
        Color scaledGrayscale = color.asGrayscale().scale(this.brightness);
        return color.lerpSrgb(scaledGrayscale, this.factor);
    }

    public float getBrightness() {
        return this.brightness;
    }

    public float getFactor() {
        return this.factor;
    }
}
