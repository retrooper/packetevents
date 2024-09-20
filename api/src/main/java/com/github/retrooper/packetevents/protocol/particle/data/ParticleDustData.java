/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.particle.data;

import com.github.retrooper.packetevents.protocol.color.Color;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTByteArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTLongArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public class ParticleDustData extends ParticleData {
    //0.01 - 4
    private float scale;

    //0-1
    private float red;
    private float green;
    private float blue;

    public ParticleDustData(float scale, float red, float green, float blue) {
        this.scale = scale;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public ParticleDustData(float scale, float[] rgb) {
        this(scale, rgb[0], rgb[1], rgb[2]);
    }

    public ParticleDustData(float scale, Vector3f rgb) {
        this(scale, rgb.getX(), rgb.getY(), rgb.getZ());
    }

    public ParticleDustData(float scale, Color color) {
        this(scale, color.red() / 255f, color.green() / 255f, color.blue() / 255f);
    }

    public float getRed() {
        return red;
    }

    public void setRed(float red) {
        this.red = red;
    }

    public float getGreen() {
        return green;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    public float getBlue() {
        return blue;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public static ParticleDustData read(PacketWrapper<?> wrapper) {
        float red = wrapper.readFloat();
        float green = wrapper.readFloat();
        float blue = wrapper.readFloat();
        float scale = wrapper.readFloat();
        return new ParticleDustData(scale, red, green, blue);
    }

    public static void write(PacketWrapper<?> wrapper, ParticleDustData data) {
        wrapper.writeFloat(data.getRed());
        wrapper.writeFloat(data.getGreen());
        wrapper.writeFloat(data.getBlue());
        wrapper.writeFloat(data.getScale());
    }

    @ApiStatus.Internal
    public static float[] decodeColor(NBT tag) {
        // I hate how nbt works
        if (tag instanceof NBTList<?>) {
            NBTList<?> colorTagList = (NBTList<?>) tag;
            float first = ((NBTNumber) colorTagList.getTag(0)).getAsFloat();
            float second = ((NBTNumber) colorTagList.getTag(1)).getAsFloat();
            float third = ((NBTNumber) colorTagList.getTag(2)).getAsFloat();
            if (colorTagList.size() > 3) {
                float fourth = ((NBTNumber) colorTagList.getTag(3)).getAsFloat();
                return new float[]{first, second, third, fourth}; // ARGB
            }
            return new float[]{first, second, third}; // RGB
        } else if (tag instanceof NBTByteArray) {
            byte[] colors = ((NBTByteArray) tag).getValue();
            return colors.length > 3
                    ? new float[]{colors[0], colors[1], colors[2], colors[3]}
                    : new float[]{colors[0], colors[1], colors[2]};
        } else if (tag instanceof NBTIntArray) {
            int[] colors = ((NBTIntArray) tag).getValue();
            return colors.length > 3
                    ? new float[]{colors[0], colors[1], colors[2], colors[3]}
                    : new float[]{colors[0], colors[1], colors[2]};
        } else if (tag instanceof NBTLongArray) {
            long[] colors = ((NBTLongArray) tag).getValue();
            return colors.length > 3
                    ? new float[]{colors[0], colors[1], colors[2], colors[3]}
                    : new float[]{colors[0], colors[1], colors[2]};
        } else {
            throw new UnsupportedOperationException("Unsupported color nbt tag: " + tag);
        }
    }

    @ApiStatus.Internal
    public static NBT encodeColor(@Nullable Float alpha, float red, float green, float blue) {
        NBTList<NBTFloat> nbtList = new NBTList<>(NBTType.FLOAT,
                alpha == null ? 3 : 4);
        if (alpha != null) {
            nbtList.addTag(new NBTFloat(alpha));
        }
        nbtList.addTag(new NBTFloat(red));
        nbtList.addTag(new NBTFloat(green));
        nbtList.addTag(new NBTFloat(blue));
        return nbtList;
    }

    public static ParticleDustData decode(NBTCompound compound, ClientVersion version) {
        NBT colorNBT = compound.getTagOrNull("color");
        float[] color = decodeColor(colorNBT);
        float scale = compound.getNumberTagOrThrow("scale").getAsFloat();
        return new ParticleDustData(scale, color);
    }

    public static void encode(ParticleDustData data, ClientVersion version, NBTCompound compound) {
        compound.setTag("color", encodeColor(null, data.red, data.green, data.blue));
        compound.setTag("scale", new NBTFloat(data.scale));
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
