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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class Color implements RGBLike {

    public static final Color WHITE = new Color(0xFFFFFFFF);
    public static final Color BLACK = new Color(0xFF000000);

    protected static final int BIT_MASK = 0xFF;

    protected final int red, green, blue;

    public Color(
            @Range(from = 0L, to = 255L) int red,
            @Range(from = 0L, to = 255L) int green,
            @Range(from = 0L, to = 255L) int blue
    ) {
        this.red = MathUtil.clamp(red, 0, 255);
        this.green = MathUtil.clamp(green, 0, 255);
        this.blue = MathUtil.clamp(blue, 0, 255);
    }

    public Color(
            @Range(from = 0L, to = 1L) float red,
            @Range(from = 0L, to = 1L) float green,
            @Range(from = 0L, to = 1L) float blue
    ) {
        this(MathUtil.floor(red * 255f),
                MathUtil.floor(green * 255f),
                MathUtil.floor(blue * 255f));
    }

    public Color(int rgb) {
        this((rgb >> 16) & BIT_MASK, (rgb >> 8) & BIT_MASK, rgb & BIT_MASK);
    }

    public static Color read(PacketWrapper<?> wrapper) {
        return new Color(wrapper.readInt());
    }

    public static void write(PacketWrapper<?> wrapper, Color color) {
        wrapper.writeInt(color.asRGB());
    }

    public static Color readShort(PacketWrapper<?> wrapper) {
        return new Color(wrapper.readUnsignedByte(), wrapper.readUnsignedByte(), wrapper.readUnsignedByte());
    }

    public static void writeShort(PacketWrapper<?> wrapper, Color color) {
        wrapper.writeByte(color.red);
        wrapper.writeByte(color.green);
        wrapper.writeByte(color.blue);
    }

    public static Color decode(NBT nbt, PacketWrapper<?> wrapper) {
        return decode(nbt, wrapper.getServerVersion().toClientVersion());
    }

    @Deprecated
    public static Color decode(NBT nbt, ClientVersion version) {
        if (nbt instanceof NBTNumber) {
            return new Color(((NBTNumber) nbt).getAsInt());
        }
        NBTList<?> list = (NBTList<?>) nbt;
        float red = ((NBTNumber) list.getTag(0)).getAsFloat();
        float green = ((NBTNumber) list.getTag(1)).getAsFloat();
        float blue = ((NBTNumber) list.getTag(2)).getAsFloat();
        return new Color(red, green, blue);
    }

    public static NBT encode(PacketWrapper<?> wrapper, Color color) {
        return encode(color, wrapper.getServerVersion().toClientVersion());
    }

    @Deprecated
    public static NBT encode(Color color, ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_21_2)) {
            return new NBTInt(color.asRGB());
        }
        NBTList<NBTFloat> list = new NBTList<>(NBTType.FLOAT, 3);
        list.addTag(new NBTFloat(color.red));
        list.addTag(new NBTFloat(color.green));
        list.addTag(new NBTFloat(color.blue));
        return list;
    }

    public AlphaColor withAlpha() {
        return this.withAlpha(255);
    }

    public AlphaColor withAlpha(@Range(from = 0L, to = 255L) int alpha) {
        return new AlphaColor(alpha, this.red, this.green, this.blue);
    }

    public Color withRed(@Range(from = 0L, to = 255L) int red) {
        return new Color(red, green, blue);
    }

    public Color withGreen(@Range(from = 0L, to = 255L) int green) {
        return new Color(red, green, blue);
    }

    public Color withBlue(@Range(from = 0L, to = 255L) int blue) {
        return new Color(red, green, blue);
    }

    public int asRGB() {
        return (this.red << 16) | (this.green << 8) | this.blue;
    }

    public Color plus(Color other) {
        return new Color(
                this.red + other.red,
                this.green + other.green,
                this.blue + other.blue
        );
    }

    public Color minus(Color other) {
        return new Color(
                this.red - other.red,
                this.green - other.green,
                this.blue - other.blue
        );
    }

    public Color times(Color other) {
        if (other instanceof AlphaColor) {
            return other.times(this);
        } else if (this.red == 255 && this.green == 255 && this.blue == 255) {
            return other;
        } else if (other.red == 255 && other.green == 255 && other.blue == 255) {
            return this;
        }
        return new Color(
                (this.red * other.red) / 255,
                (this.green * other.green) / 255,
                (this.blue * other.blue) / 255
        );
    }

    public AlphaColor blendWith(AlphaColor source) {
        int srcAlpha = source.alpha();
        if (srcAlpha == 255) {
            return source;
        } else if (srcAlpha == 0) {
            return this.withAlpha();
        }
        int alpha = srcAlpha + (255 - srcAlpha);
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

    public Color asGrayscale() {
        int grayscale = (int) ((float) this.red * 0.30f + (float) this.green * 0.59f + (float) this.blue * 0.11f);
        return new Color(grayscale, grayscale, grayscale);
    }

    public Color scale(float scale) {
        return this.scale(scale, scale, scale);
    }

    public Color scale(float redScale, float greenScale, float blueScale) {
        return new Color(
                (int) ((float) this.red * redScale),
                (int) ((float) this.green * greenScale),
                (int) ((float) this.blue * blueScale)
        );
    }

    public Color lerpSrgb(Color dest, float t) {
        return new Color(
                MathUtil.lerp(t, this.red, dest.red),
                MathUtil.lerp(t, this.green, dest.green),
                MathUtil.lerp(t, this.blue, dest.blue)
        );
    }

    public @Range(from = 0L, to = 255L) int alpha() {
        return 255;
    }

    @Override
    public @Range(from = 0L, to = 255L) int red() {
        return red;
    }

    @Override
    public @Range(from = 0L, to = 255L) int green() {
        return green;
    }

    @Override
    public @Range(from = 0L, to = 255L) int blue() {
        return blue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Color) {
            return this.asRGB() == ((Color) obj).asRGB();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.asRGB());
    }
}
