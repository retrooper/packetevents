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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class Color implements RGBLike {

    protected static final int BIT_MASK = 0xFF;

    protected final int red, green, blue;

    public Color(@Range(from = 0L, to = 255L) int red, @Range(from = 0L, to = 255L) int green, @Range(from = 0L, to = 255L) int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
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

    public @NotNull Color withRed(@Range(from = 0L, to = 255L) int red) {
        return new Color(red, green, blue);
    }

    public @NotNull Color withGreen(@Range(from = 0L, to = 255L) int green) {
        return new Color(red, green, blue);
    }

    public @NotNull Color withBlue(@Range(from = 0L, to = 255L) int blue) {
        return new Color(red, green, blue);
    }

    public int asRGB() {
        return (this.red << 16) | (this.green << 8) | this.blue;
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
}
