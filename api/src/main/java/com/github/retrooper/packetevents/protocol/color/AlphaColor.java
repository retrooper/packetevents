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

package com.github.retrooper.packetevents.protocol.color;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.MathUtil;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AlphaColor extends Color {

    public static final AlphaColor WHITE = new AlphaColor(0xFFFFFFFF);
    public static final AlphaColor BLACK = new AlphaColor(0xFF000000);
    public static final AlphaColor TRANSPARENT = new AlphaColor(0x00000000);

    private final int alpha;

    public AlphaColor(
            @Range(from = 0L, to = 255L) int red,
            @Range(from = 0L, to = 255L) int green,
            @Range(from = 0L, to = 255L) int blue
    ) {
        this(255, red, green, blue);
    }

    public AlphaColor(
            @Range(from = 0L, to = 255L) int alpha,
            @Range(from = 0L, to = 255L) int red,
            @Range(from = 0L, to = 255L) int green,
            @Range(from = 0L, to = 255L) int blue
    ) {
        super(red, green, blue);
        this.alpha = MathUtil.clamp(alpha, 0, 255);
    }

    public AlphaColor(
            @Range(from = 0L, to = 1L) float red,
            @Range(from = 0L, to = 1L) float green,
            @Range(from = 0L, to = 1L) float blue
    ) {
        this(1f, red, green, blue);
    }

    public AlphaColor(
            @Range(from = 0L, to = 1L) float alpha,
            @Range(from = 0L, to = 1L) float red,
            @Range(from = 0L, to = 1L) float green,
            @Range(from = 0L, to = 1L) float blue
    ) {
        super(red, green, blue);
        this.alpha = MathUtil.floor(alpha * 255f);
    }

    public AlphaColor(int rgb) {
        this((rgb >> 24) & BIT_MASK,
                (rgb >> 16) & BIT_MASK,
                (rgb >> 8) & BIT_MASK,
                rgb & BIT_MASK);
    }

    public static AlphaColor decode(NBT nbt, ClientVersion version) {
        if (nbt instanceof NBTNumber) {
            return new AlphaColor(((NBTNumber) nbt).getAsInt());
        }
        NBTList<?> list = (NBTList<?>) nbt;
        float red = ((NBTNumber) list.getTag(0)).getAsFloat();
        float green = ((NBTNumber) list.getTag(1)).getAsFloat();
        float blue = ((NBTNumber) list.getTag(2)).getAsFloat();
        float alpha = ((NBTNumber) list.getTag(3)).getAsFloat();
        return new AlphaColor(alpha, red, green, blue);
    }

    public static NBT encode(AlphaColor color, ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_21_2)) {
            return new NBTInt(color.asRGB());
        }
        NBTList<NBTFloat> list = new NBTList<>(NBTType.FLOAT, 4);
        list.addTag(new NBTFloat(color.red));
        list.addTag(new NBTFloat(color.green));
        list.addTag(new NBTFloat(color.blue));
        list.addTag(new NBTFloat(color.alpha));
        return list;
    }

    @Override
    public AlphaColor withAlpha() {
        return this;
    }

    @Override
    public AlphaColor withRed(@Range(from = 0L, to = 255L) int red) {
        return new AlphaColor(this.alpha, red, this.green, this.blue);
    }

    @Override
    public AlphaColor withGreen(@Range(from = 0L, to = 255L) int green) {
        return new AlphaColor(this.alpha, this.red, green, this.blue);
    }

    @Override
    public AlphaColor withBlue(@Range(from = 0L, to = 255L) int blue) {
        return new AlphaColor(this.alpha, this.red, this.green, blue);
    }

    @Override
    public int asRGB() {
        return (this.alpha << 24) | (this.red << 16) | (this.green << 8) | this.blue;
    }

    @Override
    public AlphaColor plus(Color other) {
        return new AlphaColor(
                this.alpha,
                this.red + other.red,
                this.green + other.green,
                this.blue + other.blue
        );
    }

    @Override
    public AlphaColor minus(Color other) {
        return new AlphaColor(
                this.alpha,
                this.red - other.red,
                this.green - other.green,
                this.blue - other.blue
        );
    }

    @Override
    public AlphaColor times(Color other) {
        if (other.alpha() == 255 && other.red == 255 && other.green == 255 && other.blue == 255) {
            return this;
        }
        return new AlphaColor(
                (this.alpha * other.alpha()) / 255,
                (this.red * other.red) / 255,
                (this.green * other.green) / 255,
                (this.blue * other.blue) / 255
        );
    }

    public AlphaColor blendWith(AlphaColor source) {
        int srcAlpha = source.alpha;
        if (srcAlpha == 255) {
            return source;
        } else if (srcAlpha == 0) {
            return this;
        }
        int alpha = srcAlpha + (this.alpha * (255 - srcAlpha)) / 255;
        return new AlphaColor(
                alpha,
                alphaBlendChannel(alpha, srcAlpha, this.red, source.red),
                alphaBlendChannel(alpha, srcAlpha, this.green, source.green),
                alphaBlendChannel(alpha, srcAlpha, this.blue, source.blue)
        );
    }

    protected static int alphaBlendChannel(int alpha, int srcAlpha, int dest, int src) {
        return (src * srcAlpha + dest * (alpha - srcAlpha)) / alpha;
    }

    @Override
    public AlphaColor asGrayscale() {
        int grayscale = (int) ((float) this.red * 0.30f + (float) this.green * 0.59f + (float) this.blue * 0.11f);
        return new AlphaColor(this.alpha, grayscale, grayscale, grayscale);
    }

    @Override
    public AlphaColor scale(float scale) {
        return this.scale(scale, scale, scale);
    }

    @Override
    public AlphaColor scale(float redScale, float greenScale, float blueScale) {
        return new AlphaColor(
                this.alpha,
                (int) ((float) this.red * redScale),
                (int) ((float) this.green * greenScale),
                (int) ((float) this.blue * blueScale)
        );
    }

    @Override
    public AlphaColor lerpSrgb(Color dest, float t) {
        return new AlphaColor(
                MathUtil.lerp(t, this.alpha, dest.alpha()),
                MathUtil.lerp(t, this.red, dest.red),
                MathUtil.lerp(t, this.green, dest.green),
                MathUtil.lerp(t, this.blue, dest.blue)
        );
    }

    @Override
    public @Range(from = 0L, to = 255L) int alpha() {
        return this.alpha;
    }
}
