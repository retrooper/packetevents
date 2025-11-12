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

public final class AlphaColor extends Color {

    public static final AlphaColor WHITE = new AlphaColor(0xFFFFFFFF);

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
        this.alpha = alpha;
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

    public AlphaColor withAlpha(@Range(from = 0L, to = 255L) int alpha) {
        return new AlphaColor(alpha, this.red, this.green, this.blue);
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

    public @Range(from = 0L, to = 255L) int alpha() {
        return this.alpha;
    }

    @Override
    public @Range(from = 0L, to = 255L) int red() {
        return this.red;
    }

    @Override
    public @Range(from = 0L, to = 255L) int green() {
        return this.green;
    }

    @Override
    public @Range(from = 0L, to = 255L) int blue() {
        return this.blue;
    }
}
