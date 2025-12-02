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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.color.Color;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ParticleDustData extends ParticleData {
    //0.01 - 4
    private float scale;
    private Color color;

    public ParticleDustData(float scale, float red, float green, float blue) {
        this(scale, new Color(red, green, blue));
    }

    public ParticleDustData(float scale, float[] rgb) {
        this(scale, rgb[0], rgb[1], rgb[2]);
    }

    public ParticleDustData(float scale, Vector3f rgb) {
        this(scale, rgb.getX(), rgb.getY(), rgb.getZ());
    }

    public ParticleDustData(float scale, int red, int green, int blue) {
        this(scale, new Color(red, green, blue));
    }

    public ParticleDustData(float scale, Color color) {
        this.scale = scale;
        this.color = color;
    }

    public static ParticleDustData read(PacketWrapper<?> wrapper) {
        Color color;
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            color = new Color(wrapper.readInt());
        } else {
            float red = wrapper.readFloat();
            float green = wrapper.readFloat();
            float blue = wrapper.readFloat();
            color = new Color(red, green, blue);
        }
        float scale = wrapper.readFloat();
        return new ParticleDustData(scale, color);
    }

    public static void write(PacketWrapper<?> wrapper, ParticleDustData data) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            wrapper.writeInt(data.color.asRGB());
        } else {
            wrapper.writeFloat(data.getRed());
            wrapper.writeFloat(data.getGreen());
            wrapper.writeFloat(data.getBlue());
        }
        wrapper.writeFloat(data.scale);
    }

    public static ParticleDustData decode(NBTCompound compound, ClientVersion version) {
        Color color = Color.decode(compound.getTagOrThrow("color"), version);
        float scale = compound.getNumberTagOrThrow("scale").getAsFloat();
        return new ParticleDustData(scale, color);
    }

    public static void encode(ParticleDustData data, ClientVersion version, NBTCompound compound) {
        compound.setTag("color", Color.encode(data.color, version));
        compound.setTag("scale", new NBTFloat(data.scale));
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public float getRed() {
        return this.color.red() / 255f;
    }

    public void setRed(float red) {
        this.color = new Color(red, this.getGreen(), this.getBlue());
    }

    public float getGreen() {
        return this.color.green() / 255f;
    }

    public void setGreen(float green) {
        this.color = new Color(this.getRed(), green, this.getBlue());
    }

    public float getBlue() {
        return this.color.blue() / 255f;
    }

    public void setBlue(float blue) {
        this.color = new Color(this.getRed(), this.getGreen(), blue);
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
